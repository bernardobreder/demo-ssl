package jtecweb;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ws.WebSocket;
import ws.WebsocketRequest;
import ws.WebsocketResponse;

/**
 * Servidor Http e Websocket
 *
 * @author bernardobreder
 *
 */
public abstract class WebsocketClient implements IWebsocketClient {

  /** Socket */
  private final WebSocket socket;
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
  public WebsocketClient(WebSocket socket) throws IOException {
    this.socket = socket;
  }

  @Override
  public abstract void websocket(WebsocketRequest req, WebsocketResponse resp)
    throws IOException;

  @Override
  public void start() {
    Thread httpThread = new Thread(new Runnable() {
      @Override
      public void run() {
        runHttpThread();
      }
    }, "Server-Processor-Http");
    Thread websocketThread = new Thread(new Runnable() {
      @Override
      public void run() {
        runWebsocketThread();
      }
    }, "Server-Processor-Websocket");
    httpThread.start();
    websocketThread.start();
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

  @Override
  public void close() {
    this.closed = true;
    try {
      this.socket.close();
    }
    catch (IOException e) {
    }
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
