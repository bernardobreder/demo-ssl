package http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import socket.IServerSocket;
import socket.ISocket;

/**
 *
 *
 * @author bernardobreder
 */
public class HttpServerSocket {

  /** Server */
  private final IServerSocket server;
  /** Listeners */
  private List<HttpServerSocketListener> listeners =
    new ArrayList<HttpServerSocketListener>();

  /**
   * @param server
   */
  public HttpServerSocket(IServerSocket server) {
    this.server = server;
  }

  /**
   * @return socket
   * @throws IOException
   */
  public HttpSocket accept() throws IOException {
    for (;;) {
      ISocket socket = this.server.accept();
      try {
        HttpSocket httpSocket = new HttpSocket(socket);
        this.fireAcceptSocket(httpSocket);
        return httpSocket;
      }
      catch (Throwable e) {
        this.fireError(e);
      }
    }
  }

  /**
   * @throws IOException
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
  protected void fireAcceptSocket(HttpSocket socket) {
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
   * @param listener
   */
  public void addListener(HttpServerSocketListener listener) {
    this.listeners.add(listener);
  }

  /**
   * @param listener
   * @return indica se removeu
   */
  public boolean removeListener(HttpServerSocketListener listener) {
    return this.listeners.remove(listener);
  }

  /**
   *
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
    public void acceptHttpSocket(HttpSocket socket);

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