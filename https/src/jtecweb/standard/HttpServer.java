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
import jtecweb.IHttpServer;
import socket.IServerSocket;

/**
 * Servidor Http e Websocket
 *
 * @author bernardobreder
 *
 */
public abstract class HttpServer implements IHttpServer {

  /** Servidor Http */
  private final HttpServerSocket serverSocket;
  /** Lista de Https abertos */
  private List<Reference<IHttpClient>> clients;
  /** Indica se está fechado */
  private boolean closed;
  /** Indica o timeout para accept */
  private int timeout;
  /** Fila de eventos na Thread do Navegador */
  private final List<IHttpServerListener> listeners;

  /**
   * Construtor
   * 
   * @param serverSocket
   */
  public HttpServer(IServerSocket serverSocket) {
    this.serverSocket = new HttpServerSocket(serverSocket);
    this.clients = new LinkedList<Reference<IHttpClient>>();
    this.listeners = new ArrayList<IHttpServerListener>();
    this.timeout = 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IHttpServer start() {
    if (this.closed) {
      throw new IllegalStateException("server already closed");
    }
    Thread httpThread = new Thread(new Runnable() {
      @Override
      public void run() {
        whileHttpThread();
      }
    }, "Server-Accept-Http");
    httpThread.start();
    return this;
  }

  /**
   * Loop para tratar as requisições http
   */
  protected void whileHttpThread() {
    while (!closed) {
      runHttpThread();
      sleep();
    }
  }

  /**
   * Trata um só requisição http
   */
  protected void runHttpThread() {
    try {
      HttpSocket socket = this.serverSocket.accept();
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
      client.start();
      synchronized (this.clients) {
        this.clients.add(new WeakReference<IHttpClient>(client));
      }
    }
    catch (Throwable e) {
      this.fireError(e);
    }
  }

  /**
   * Dorme por um instante
   */
  protected void sleep() {
    try {
      Thread.sleep(0);
    }
    catch (InterruptedException e) {
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IHttpServer close() {
    this.closed = true;
    try {
      this.serverSocket.close();
    }
    catch (IOException e) {
      this.fireError(e);
    }
    for (Reference<IHttpClient> ref : this.clients) {
      IHttpClient client = ref.get();
      if (client != null) {
        client.close();
      }
    }
    this.clients.clear();
    return this;
  }

  /**
   * @param client
   */
  protected void close(IHttpClient client) {
    synchronized (this.clients) {
      Iterator<Reference<IHttpClient>> iterator = this.clients.iterator();
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
  public void addListener(IHttpServerListener listener) {
    this.listeners.add(listener);
  }

  /**
   * Remove um listener
   * 
   * @param listener
   */
  public void removeListener(IHttpServerListener listener) {
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
    for (IHttpServerListener listener : this.listeners) {
      listener.closed();
    }
  }

  /**
   * Dispara o evento de fechar o navegador
   * 
   * @param e
   */
  protected void fireError(Throwable e) {
    for (IHttpServerListener listener : this.listeners) {
      listener.error(e);
    }
  }

}
