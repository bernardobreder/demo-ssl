package jtecweb.standard;

import http.HttpServerSocket;
import http.HttpSocket;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jtecweb.IHttpClient;
import jtecweb.IHttpClient.AdapterHttpClientListener;
import jtecweb.IHttpWsServer;
import jtecweb.IWsClient;
import jtecweb.IWsClient.AdapterWsClientListener;
import socket.ServerSocket;
import websocket.standard.StandardWebServerSocket;
import ws.WebSocket;

/**
 * Servidor Http e Websocket
 *
 * @author bernardobreder
 *
 */
public abstract class CopyOfHttpWsServer implements IHttpWsServer {

  /** Servidor Http */
  private final HttpServerSocket httpServerSocket;
  /** Servidor Websocket */
  private final StandardWebServerSocket websocketServerSocket;
  /** Lista de Https abertos */
  private List<Reference<IHttpClient>> https;
  /** Lista de Websockets abertos */
  private List<Reference<IWsClient>> websokets;
  /** Indica se está fechado */
  private boolean closed;
  /** Indica o timeout para accept */
  private int timeout;
  /** Fila de eventos na Thread do Navegador */
  private final List<TecWebServerListener> listeners;

  /**
   * Construtor
   * 
   * @param httpServerSocket
   * @param websocketServerSocket
   * @throws IOException
   */
  public CopyOfHttpWsServer(ServerSocket httpServerSocket,
    ServerSocket websocketServerSocket) throws IOException {
    this.httpServerSocket = new HttpServerSocket(httpServerSocket);
    this.websocketServerSocket =
      new StandardWebServerSocket(websocketServerSocket);
    this.https = new LinkedList<Reference<IHttpClient>>();
    this.websokets = new LinkedList<Reference<IWsClient>>();
    this.listeners = new ArrayList<TecWebServerListener>();
    this.timeout = 10 * 1000;
  }

  @Override
  public IHttpWsServer start() {
    if (this.closed) {
      throw new IllegalStateException("server already closed");
    }
    Thread httpThread = new Thread(new Runnable() {
      @Override
      public void run() {
        whileHttpThread();
      }
    }, "Server-Accept-Http");
    Thread websocketThread = new Thread(new Runnable() {
      @Override
      public void run() {
        whileWebsocketThread();
      }
    }, "Server-Accept-Ws");
    httpThread.start();
    websocketThread.start();
    return this;
  }

  protected void whileWebsocketThread() {
    while (!closed) {
      runWebsocketThread();
      sleep();
    }
  }

  protected void whileHttpThread() {
    while (!closed) {
      runHttpThread();
      sleep();
    }
  }

  protected void runWebsocketThread() {
    try {
      WebSocket socket = this.websocketServerSocket.accept();
      socket.setTimeout(this.timeout);
      IWsClient client = this.createWsClient(socket);
      if (client == null) {
        socket.close();
        return;
      }
      socket.setTimeout(0);
      client.addListener(new AdapterWsClientListener() {
        @Override
        public void closed(IWsClient client) {
          close(client);
        }
      });
      client.startWebsocket();
      synchronized (this.websokets) {
        this.websokets.add(new WeakReference<IWsClient>(client));
      }
    }
    catch (Throwable e) {
      this.fireError(e);
    }
  }

  protected void runHttpThread() {
    try {
      HttpSocket socket = this.httpServerSocket.accept();
      socket.setTimeout(this.timeout);
      IHttpClient client = this.createHttpClient(socket);
      if (client == null) {
        socket.close();
        return;
      }
      client.addListener(new AdapterHttpClientListener() {
        @Override
        public void closed(IHttpClient client) {
          close(client);
        }
      });
      client.startHttp();
      synchronized (this.https) {
        this.https.add(new WeakReference<IHttpClient>(client));
      }
    }
    catch (Throwable e) {
      this.fireError(e);
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
  public IHttpWsServer close() {
    this.closed = true;
    try {
      this.httpServerSocket.close();
    }
    catch (IOException e) {
      this.fireError(e);
    }
    try {
      this.websocketServerSocket.close();
    }
    catch (IOException e) {
      this.fireError(e);
    }
    for (Reference<IHttpClient> ref : this.https) {
      IHttpClient client = ref.get();
      if (client != null) {
        client.closeHttp();
      }
    }
    for (Reference<IWsClient> ref : this.websokets) {
      IWsClient client = ref.get();
      if (client != null) {
        client.closeWebsocket();
      }
    }
    this.https.clear();
    this.websokets.clear();
    return this;
  }

  protected void close(IWsClient client) {
    synchronized (this.websokets) {
      Iterator<Reference<IWsClient>> iterator = this.websokets.iterator();
      while (iterator.hasNext()) {
        Reference<IWsClient> ref = iterator.next();
        IWsClient other = ref.get();
        if (other != null) {
          if (client == other || client.equals(other)) {
            iterator.remove();
          }
        }
        else {
          iterator.remove();
        }
      }
    }
  }

  protected void close(IHttpClient client) {
    synchronized (this.https) {
      Iterator<Reference<IHttpClient>> iterator = this.https.iterator();
      while (iterator.hasNext()) {
        Reference<IHttpClient> ref = iterator.next();
        IHttpClient other = ref.get();
        if (other != null) {
          if (client == other || client.equals(other)) {
            iterator.remove();
          }
        }
        else {
          iterator.remove();
        }
      }
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
   * Dispara o evento de fechar o navegador
   */
  protected void fireError(Throwable e) {
    for (TecWebServerListener listener : this.listeners) {
      listener.error(e);
    }
  }

  /**
   * Listener do navegador do cliente
   * 
   * @author bernardobreder
   */
  public static interface TecWebServerListener {

    /**
     * Indica que foi fechado o navegador
     */
    public void closed();

    /**
     * Notifica um erro
     * 
     * @param e
     */
    public void error(Throwable e);

  }

}
