package jtecweb.standard;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jtecweb.IWsClient;
import jtecweb.IWsClient.AdapterWsClientListener;
import jtecweb.IWsServer;
import socket.IServerSocket;
import ws.IWsServerSocket;
import ws.IWsSocket;

/**
 * Servidor Http e Websocket
 *
 * @author bernardobreder
 *
 */
public abstract class WsServer implements IWsServer {

  /** Servidor Websocket */
  private final IWsServerSocket serverSocket;
  /** Lista de Websockets abertos */
  private List<Reference<IWsClient>> clients;
  /** Indica se está fechado */
  private boolean closed;
  /** Indica o timeout para accept */
  private int timeout;
  /** Fila de eventos na Thread do Navegador */
  private final List<IWsServerListener> listeners;

  /**
   * Construtor
   *
   * @param serverSocket
   */
  public WsServer(IServerSocket serverSocket) {
    this.serverSocket = new WsServerSocket(serverSocket);
    this.clients = new LinkedList<Reference<IWsClient>>();
    this.listeners = new ArrayList<IWsServerListener>();
    this.timeout = 10 * 1000;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WsServer start() {
    if (this.closed) {
      throw new IllegalStateException("server already closed");
    }
    Thread websocketThread = new Thread(new Runnable() {
      @Override
      public void run() {
        whileWebsocketThread();
      }
    }, "Server-Accept-Ws");
    websocketThread.start();
    return this;
  }

  /**
   * Loop que trata as mensagens do websocket
   */
  protected void whileWebsocketThread() {
    while (!closed) {
      try {
        IWsSocket socket = this.serverSocket.accept();
        IWsClient client = this.createWsClient(socket);
        if (client == null) {
          socket.close();
          return;
        }
        client.addListener(new AdapterWsClientListener() {
          @Override
          public void closed(IWsClient client) {
            close(client);
          }
        });
        client.start();
        synchronized (this.clients) {
          this.clients.add(new WeakReference<IWsClient>(client));
        }
      }
      catch (Throwable e) {
        this.fireError(e);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WsServer close() {
    this.closed = true;
    try {
      this.serverSocket.close();
    }
    catch (IOException e) {
      this.fireError(e);
    }
    for (Reference<IWsClient> ref : this.clients) {
      IWsClient client = ref.get();
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
  protected void close(IWsClient client) {
    synchronized (this.clients) {
      Iterator<Reference<IWsClient>> iterator = this.clients.iterator();
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

  /**
   * Adiciona um listener
   *
   * @param listener
   */
  public void addListener(IWsServerListener listener) {
    this.listeners.add(listener);
  }

  /**
   * Remove um listener
   *
   * @param listener
   */
  public void removeListener(IWsServerListener listener) {
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
    for (IWsServerListener listener : this.listeners) {
      listener.closed();
    }
  }

  /**
   * Dispara o evento de fechar o navegador
   *
   * @param e
   */
  protected void fireError(Throwable e) {
    for (IWsServerListener listener : this.listeners) {
      listener.error(e);
    }
  }

}
