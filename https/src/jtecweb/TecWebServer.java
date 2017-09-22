package jtecweb;

import http.HttpRequest;
import http.HttpResponse;
import http.HttpServerSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import socket.ServerSocket;
import websocket.standard.StandardWebServerSocket;
import ws.WebSocket;
import ws.WebsocketRequest;
import ws.WebsocketResponse;

/**
 * Servidor Http e Websocket
 *
 * @author bernardobreder
 *
 */
public abstract class TecWebServer {

  /** Servidor Http */
  private final HttpServerSocket httpServerSocket;
  /** Servidor Websocket */
  private final StandardWebServerSocket websocketServerSocket;
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
  public TecWebServer(ServerSocket httpServerSocket,
    ServerSocket websocketServerSocket) throws IOException {
    this.httpServerSocket = new HttpServerSocket(httpServerSocket);
    this.websocketServerSocket =
      new StandardWebServerSocket(websocketServerSocket);
  }

  public abstract String websocket(WebsocketRequest req, WebsocketResponse resp)
    throws IOException;

  public abstract void http(HttpRequest req, HttpResponse resp)
    throws IOException;

  public TecWebServer start() {
    Thread httpThread = new Thread(new Runnable() {
      @Override
      public void run() {
        runHttpThread();
      }
    }, "Server-Accept-Http");
    Thread websocketThread = new Thread(new Runnable() {
      @Override
      public void run() {
        runWebsocketThread();
      }
    }, "Server-Accept-Websocket");
    httpThread.start();
    websocketThread.start();
    return this;
  }

  protected void runWebsocketThread() {
    while (!closed) {
      try {
        WebSocket socket = this.websocketServerSocket.accept();
        String message = socket.readMessage();

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
