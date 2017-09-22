package jtecweb;

import ws.IWsSocket;

/**
 * Interface da especificação do servidor Http e Websocket.
 *
 * @author bernardobreder
 *
 */
public interface IHttpWsServer {

  /**
   * @param socket
   * @param session
   * @return client
   */
  public IHttpWsClient create(IWsSocket socket, String session);

  /**
   * Inicia o servidor
   * 
   * @return this
   */
  public IHttpWsServer start();

  /**
   * Finaliza o servidor
   * 
   * @return this
   */
  public IHttpWsServer close();

  /**
   * Indica se está fechado
   * 
   * @return fechado
   */
  public boolean isClosed();

  /**
   * Listener do navegador do cliente
   * 
   * @author bernardobreder
   */
  public static interface IHttpServerListener {

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