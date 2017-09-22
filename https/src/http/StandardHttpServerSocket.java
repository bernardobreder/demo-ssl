package http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import socket.ServerSocket;
import socket.Socket;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public class StandardHttpServerSocket {

  /** Server */
  private final ServerSocket server;
  /** Listeners */
  private List<HttpServerSocketListener> listeners =
    new ArrayList<HttpServerSocketListener>();

  /**
   * @param port
   * @throws IOException
   */
  public StandardHttpServerSocket(ServerSocket server) throws IOException {
    this.server = server;
  }

  /**
   * {@inheritDoc}
   */
  public StandardHttpSocket accept() throws IOException {
    for (;;) {
      Socket socket = this.server.accept();
      try {
        StandardHttpSocket httpSocket = new StandardHttpSocket(socket);
        httpSocket.readRequest();
        this.fireAcceptSocket(httpSocket);
        return httpSocket;
      }
      catch (Throwable e) {
        this.fireError(e);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public void close() throws IOException {
    try {
      this.server.close();
    }
    finally {
      this.fireCloseServer();
    }
  }

  /**
   * Dispara evento
   * 
   * @param e
   */
  protected void fireError(Throwable e) {
    if (!this.listeners.isEmpty()) {
      for (HttpServerSocketListener listener : this.listeners) {
        listener.error(e);
      }
    }
  }

  /**
   * Dispara evento
   * 
   * @param socket
   */
  protected void fireAcceptSocket(StandardHttpSocket socket) {
    if (!this.listeners.isEmpty()) {
      for (HttpServerSocketListener listener : this.listeners) {
        listener.acceptHttpSocket(socket);
      }
    }
  }

  /**
   * Dispara evento
   */
  protected void fireCloseServer() {
    if (!this.listeners.isEmpty()) {
      for (HttpServerSocketListener listener : this.listeners) {
        listener.closeServer();
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public void addListener(HttpServerSocketListener listener) {
    this.listeners.add(listener);
  }

  /**
   * {@inheritDoc}
   */
  public boolean removeListener(HttpServerSocketListener listener) {
    return this.listeners.remove(listener);
  }

  /**
   * {@inheritDoc}
   */
  public void clearListeners() {
    this.listeners.clear();
  }

  /**
   * Interface de eventos do servidor
   * 
   * @author bernardobreder
   * 
   */
  public static interface HttpServerSocketListener {

    /**
     * Evento de aceitação da comunicação entre o cliente e o servidor
     * 
     * @param socket
     */
    public void acceptHttpSocket(StandardHttpSocket socket);

    /**
     * Evento do servidor sendo fechado
     */
    public void closeServer();

    /**
     * Evento de erro interno
     * 
     * @param t
     */
    public void error(Throwable t);

  }

}
// http://www.w3.org/Protocols/rfc2616/rfc2616.html