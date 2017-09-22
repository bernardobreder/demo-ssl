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
public abstract class WsClient implements IWsClient {

  /** Socket */
  private final WebSocket socket;
  /** Indica se está fechado */
  private boolean closed;
  /** Fila de eventos na Thread do Navegador */
  private final List<IWsClientListener> listeners =
    new ArrayList<IWsClientListener>();

  /**
   * Construtor
   * 
   * @param httpServerSocket
   * @param websocketServerSocket
   * @throws IOException
   */
  public WsClient(WebSocket socket) throws IOException {
    this.socket = socket;
  }

  @Override
  public abstract void service(WebsocketRequest req, WebsocketResponse resp)
    throws IOException;

  @Override
  public void start() {
    if (this.closed) {
      throw new IllegalStateException("client already closed");
    }
    Thread websocketThread = new Thread(new Runnable() {
      @Override
      public void run() {
        whileWebsocketThread();
      }
    }, "Server-Processor-Websocket");
    websocketThread.start();
  }

  protected void whileWebsocketThread() {
    while (!closed) {
      runWebsocketThread();
      sleep();
    }
  }

  protected void runWebsocketThread() {
    try {
      String message = socket.readMessage();
      WebsocketRequest req = new WebsocketRequest(message);
      WebsocketResponse resp = new WebsocketResponse(socket);
      this.service(req, resp);
    }
    catch (EOFException e) {
      this.close();
    }
    catch (Throwable e) {
      if (this.socket.isClosed()) {
        this.close();
      }
      else {
        e.printStackTrace();
      }
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
  @Override
  public void addListener(IWsClientListener listener) {
    this.listeners.add(listener);
  }

  /**
   * Remove um listener
   * 
   * @param listener
   */
  @Override
  public void removeListener(IWsClientListener listener) {
    this.listeners.remove(listener);
  }

  /**
   * Remove todos os listeners
   */
  @Override
  public void removeListeners() {
    this.listeners.clear();
  }

  /**
   * Dispara o evento de fechar o navegador
   */
  protected void fireClosed() {
    for (IWsClientListener listener : this.listeners) {
      listener.closed(this);
    }
  }

}
