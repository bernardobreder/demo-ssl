package jtecweb;

import http.HttpRequest;
import http.HttpResponse;
import http.HttpSocket;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Servidor Http e Websocket
 *
 * @author bernardobreder
 *
 */
public abstract class HttpClient implements IHttpClient {

  /** Socket */
  private final HttpSocket socket;
  /** Indica se está fechado */
  private boolean closed;
  /** Fila de eventos na Thread do Navegador */
  private final List<TecWebServerListener> listeners =
    new ArrayList<TecWebServerListener>();

  /**
   * Construtor
   * 
   * @param httpServerSocket
   * @param websocketServerSocket
   * @throws IOException
   */
  public HttpClient(HttpSocket socket) throws IOException {
    this.socket = socket;
  }

  @Override
  public abstract void service(HttpRequest req, HttpResponse resp)
    throws IOException;

  @Override
  public void start() {
    if (this.closed) {
      throw new IllegalStateException("client already closed");
    }
    Thread websocketThread = new Thread(new Runnable() {
      @Override
      public void run() {
        runHttpThread();
      }
    }, "Server-Processor-Http");
    websocketThread.start();
  }

  protected void runHttpThread() {
    while (!closed) {
      try {
        InputStream input = socket.getInputStream();
        HttpRequest req = new HttpRequest(input);
        HttpResponse resp = new HttpResponse(req, socket.getOutputStream());
        this.service(req, resp);
      }
      catch (EOFException e) {
        this.close();
        break;
      }
      catch (Throwable e) {
        e.printStackTrace();
      }
      sleep();
    }
  }

  protected void sleep() {
    try {
      Thread.sleep(0);
    }
    catch (InterruptedException e) {
    }
  }

  @Override
  public void close() {
    this.closed = true;
    try {
      this.socket.close();
    }
    catch (IOException e) {
    }
    this.fireClosed();
  }

  /**
   * Adiciona um listener
   * 
   * @param listener
   */
  public void addListener(TecWebServerListener listener) {
    this.listeners.add(listener);
  }

  /**
   * Remove um listener
   * 
   * @param listener
   */
  public void removeListener(TecWebServerListener listener) {
    this.listeners.remove(listener);
  }

  /**
   * Remove todos os listeners
   */
  public void removeListeners() {
    this.listeners.clear();
  }

  /**
   * Dispara o evento de fechar o navegador
   */
  protected void fireClosed() {
    for (TecWebServerListener listener : this.listeners) {
      listener.closed();
    }
  }

  /**
   * Listener do navegador do cliente
   * 
   * @author Tecgraf/PUC-Rio
   */
  public static interface TecWebServerListener {

    /**
     * Indica que foi fechado o navegador
     */
    public void closed();

  }

}
