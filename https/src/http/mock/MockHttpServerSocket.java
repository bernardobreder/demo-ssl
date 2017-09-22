package http.mock;

import http.HttpSocket;
import http.base.AbstractHttpServerSocket;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public class MockHttpServerSocket extends AbstractHttpServerSocket {

  /** Indica se está fechado */
  private boolean closed;
  /** Sockets */
  private Queue<MockHttpSocket> sockets = new LinkedList<MockHttpSocket>();
  /** Servidores */
  private static Map<Integer, MockHttpServerSocket> servers =
    new TreeMap<Integer, MockHttpServerSocket>();

  /**
   * Construtor
   *
   * @param port
   */
  public MockHttpServerSocket(int port) {
    synchronized (servers) {
      if (servers.containsKey(port)) {
        throw new IllegalArgumentException("port " + port + " already used");
      }
      servers.put(port, this);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HttpSocket accept() throws IOException {
    if (this.closed) {
      throw new IllegalStateException("closed");
    }
    MockHttpSocket httpSocket = null;
    while (!closed) {
      synchronized (sockets) {
        if (!sockets.isEmpty()) {
          httpSocket = sockets.poll();
          break;
        }
      }
      try {
        Thread.sleep(0);
      }
      catch (InterruptedException e) {
      }
    }
    httpSocket.setServer();
    httpSocket.readRequest();
    return httpSocket;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() {
    this.closed = true;
  }

  /**
   * Adiciona um socket na fila
   *
   * @param socket
   * @return this
   */
  public static void addSocket(MockHttpSocket socket) {
    MockHttpServerSocket server;
    synchronized (servers) {
      server = servers.get(socket.getPort());
    }
    if (server == null) {
      throw new IllegalArgumentException("no server for port "
        + socket.getPort());
    }
    server.sockets.offer(socket);
  }

}
