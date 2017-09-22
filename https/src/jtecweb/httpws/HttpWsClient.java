package jtecweb.httpws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jtecweb.IHttpWsClient;
import ws.IWsSocket;

/**
 * Cliente reune Http e Websocket
 *
 * @author Tecgraf/PUC-Rio
 */
public abstract class HttpWsClient implements IHttpWsClient {

  /** Socket */
  private IWsSocket socket;
  /** Sessão */
  private final String session;
  /** Fila de eventos na Thread do Navegador */
  private final List<IHttpWsClientListener> listeners =
    new ArrayList<IHttpWsClientListener>();

  /**
   * Construtor
   *
   * @param socket
   */
  public HttpWsClient(IWsSocket socket, String session) {
    this.socket = socket;
    this.session = session;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IWsSocket getSocket() {
    return this.socket;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IHttpWsClient setSocket(IWsSocket socket) {
    this.socket = socket;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IHttpWsClient addListener(IHttpWsClientListener listener) {
    this.listeners.add(listener);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IHttpWsClient removeListener(IHttpWsClientListener listener) {
    this.listeners.remove(listener);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IHttpWsClient removeListeners() {
    this.listeners.clear();
    return this;
  }

  /**
   * Retorna a sessão do cliente
   *
   * @return sessão do cliente
   */
  @Override
  public String getSession() {
    return session;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() {
    try {
      this.getSocket().close();
    }
    catch (IOException e) {
    }
    this.fireClosed();
  }

  /**
   * Dispara o evento de fechar o navegador
   */
  protected void fireClosed() {
    for (IHttpWsClientListener listener : this.listeners) {
      listener.closed(this);
    }
  }

}
