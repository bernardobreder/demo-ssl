package jtecweb.standard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jtecweb.IWsClient;
import ws.IWsSocket;
import ws.WsRequest;
import ws.WsResponse;

/**
 * Servidor Http e Websocket
 *
 * @author bernardobreder
 *
 */
public abstract class WsClient implements IWsClient {

  /** Socket */
  private final IWsSocket socket;
  /** Indica se está fechado */
  private boolean closed;
  /** Fila de eventos na Thread do Navegador */
  private final List<IWsClientListener> listeners =
    new ArrayList<IWsClientListener>();

  /**
   * Construtor
   * 
   * @param socket
   */
  public WsClient(IWsSocket socket) {
    this.socket = socket;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IWsClient start() {
    if (this.closed) {
      throw new IllegalStateException("client already closed");
    }
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        runWebsocketThread();
      }
    }, "Server-Processor-Websocket[" + System.identityHashCode(socket) + "]");
    thread.start();
    return this;
  }

  /**
   *
   */
  protected void runWebsocketThread() {
    while (!closed) {
      try {
        String message = socket.readMessage();
        WsRequest req = new WsRequest(message);
        WsResponse resp = new WsResponse(socket);
        this.service(req, resp);
        resp.flush();
      }
      catch (Throwable e) {
        if (this.socket.isClosed() || HttpClient.checkClosed(e)) {
          this.close();
        }
        else {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IWsClient close() {
    this.closed = true;
    try {
      this.socket.close();
    }
    catch (IOException e) {
    }
    this.fireClosed();
    return this;
  }

  /**
   * Adiciona um listener
   * 
   * @param listener
   */
  @Override
  public IWsClient addListener(IWsClientListener listener) {
    this.listeners.add(listener);
    return this;
  }

  /**
   * Remove um listener
   * 
   * @param listener
   */
  @Override
  public IWsClient removeListener(IWsClientListener listener) {
    this.listeners.remove(listener);
    return this;
  }

  /**
   * Remove todos os listeners
   */
  @Override
  public IWsClient removeWsListeners() {
    this.listeners.clear();
    return this;
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
