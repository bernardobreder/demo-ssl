package socket.mock;

import java.io.EOFException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

import socket.IServerSocket;

/**
 *
 *
 * @author bernardobreder
 */
public class MockServerSocket implements IServerSocket {

  /** Servidores */
  private static Map<Integer, MockServerSocket> servers =
    new TreeMap<Integer, MockServerSocket>();
  /** Porta */
  private final int port;
  /** Sockets */
  private Queue<MockSocket> sockets = new LinkedList<MockSocket>();
  /** Indica que está fechado */
  private boolean closed;
  /** Timeout */
  private int timeout;

  /**
   * @param port
   */
  public MockServerSocket(int port) {
    this.port = port;
    servers.put(port, this);
    this.timeout = 2000;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MockSocket accept() throws EOFException {
    // long time = System.currentTimeMillis();
    MockSocket socket = null;
    for (;;) {
      if (this.closed) {
        throw new EOFException("closed");
      }
      synchronized (sockets) {
        if (!sockets.isEmpty()) {
          socket = sockets.poll();
          break;
        }
      }
      synchronized (this) {
        try {
          this.wait(100);
        }
        catch (InterruptedException e) {
        }
      }
      // if (System.currentTimeMillis() - time > timeout) {
      // throw new RuntimeException("timeout");
      // }
    }
    return socket;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getPort() {
    return this.port;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTimeout(int timeout) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() {
    this.closed = true;
    synchronized (this) {
      this.notify();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isClosed() {
    return this.closed;
  }

  /**
   * Adiciona um socket na fila
   * 
   * @param socket
   */
  public static void addSocket(MockSocket socket) {
    MockServerSocket server;
    synchronized (servers) {
      server = servers.get(socket.getPort());
    }
    if (server == null) {
      throw new IllegalArgumentException("no server for port "
        + socket.getPort());
    }
    synchronized (server.sockets) {
      server.sockets.offer(socket);
    }
    synchronized (server) {
      server.notify();
    }
  }

  @Override
  public String getHost() {
    return "localhost";
  }

  // public static void main(String[] args) {
  // MockServerSocket server = new MockServerSocket(8080);
  // MockSocket clientSocket = new MockSocket(8080);
  // MockSocket serverSocket = server.accept();
  // clientSocket.getOutputStream().write("ab".getBytes());
  // Assert.assertEquals('a', serverSocket.getInputStream().read());
  // Assert.assertEquals('b', serverSocket.getInputStream().read());
  // clientSocket.getOutputStream().write("cd".getBytes());
  // Assert.assertEquals('c', serverSocket.getInputStream().read());
  // Assert.assertEquals('d', serverSocket.getInputStream().read());
  // clientSocket.close();
  // Assert.assertEquals(-1, serverSocket.getInputStream().read());
  // serverSocket.close();
  // server.close();
  // }

}
