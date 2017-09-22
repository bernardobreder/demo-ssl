package jtecweb;

import http.HttpSocket;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ws.WebsocketRequest;
import ws.WebsocketResponse;

/**
 * Servidor Http e Websocket
 *
 * @author bernardobreder
 *
 */
public abstract class HttpTWebClient {

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
  public HttpTWebClient(HttpSocket socket) throws IOException {
    this.socket = socket;
  }

  public abstract String websocket(WebsocketRequest req, WebsocketResponse resp)
    throws IOException;

  public HttpTWebClient start() {
    Thread websocketThread = new Thread(new Runnable() {
      @Override
      public void run() {
        runHttpThread();
      }
    }, "Server-Processor-Http");
    websocketThread.start();
    return this;
  }

  protected void runWebsocketThread() {
    while (!closed) {
      try {
        String message = socket.readMessage();
        WebsocketRequest req = new WebsocketRequest(message);
        WebsocketResponse resp = new WebsocketResponse(socket);
        this.websocket(req, resp);
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

  protected void runHttpThread() {
    while (!closed) {
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

  public HttpTWebClient close() {
    this.closed = true;
    try {
      this.socket.close();
    }
    catch (IOException e) {
    }
    return this;
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
