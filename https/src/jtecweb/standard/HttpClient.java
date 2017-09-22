package jtecweb.standard;

import http.HttpRequest;
import http.HttpResponse;
import http.HttpSocket;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import jtecweb.IHttpClient;

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
  private final List<IHttpClientListener> listeners =
    new ArrayList<IHttpClientListener>();

  /**
   * Construtor
   * 
   * @param socket
   * @throws IOException
   */
  public HttpClient(HttpSocket socket) throws IOException {
    this.socket = socket;
    socket.setTimeout(0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IHttpClient start() {
    if (this.closed) {
      throw new IllegalStateException("client already closed");
    }
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        whileHttpThread();
      }
    }, "Server-Processor-Http[" + System.identityHashCode(socket) + "]");
    thread.start();
    return this;
  }

  /**
   * Loop de tratamento de requisição
   */
  protected void whileHttpThread() {
    while (!closed) {
      try {
        HttpRequest req = socket.getRequest();
        HttpResponse resp = socket.getResponse();
        this.service(req, resp);
        this.socket.flush();
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
   * Verifica se o erro corresponde um que deva fechar a conexão
   * 
   * @param e
   * @return erro de fechamento de conexão
   */
  public static boolean checkClosed(Throwable e) {
    if (e instanceof EOFException || e instanceof SocketTimeoutException) {
      return true;
    }
    else if (e instanceof SocketException) {
      SocketException socketException = (SocketException) e;
      String message = socketException.getMessage();
      if (message.contains("Connection reset")) {
        return true;
      }
      else if (message.contains("Broken pipe")) {
        return true;
      }
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IHttpClient close() {
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
  public IHttpClient addListener(IHttpClientListener listener) {
    this.listeners.add(listener);
    return this;
  }

  /**
   * Remove um listener
   * 
   * @param listener
   */
  @Override
  public IHttpClient removeListener(IHttpClientListener listener) {
    this.listeners.remove(listener);
    return this;
  }

  /**
   * Remove todos os listeners
   */
  @Override
  public IHttpClient removeHttpListeners() {
    this.listeners.clear();
    return this;
  }

  /**
   * Dispara o evento de fechar o navegador
   */
  protected void fireClosed() {
    for (IHttpClientListener listener : this.listeners) {
      listener.closed(this);
    }
  }

  /**
   * @return socket
   */
  public HttpSocket getSocket() {
    return this.socket;
  }

}
