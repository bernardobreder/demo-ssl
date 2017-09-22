package jtecweb;

import java.io.IOException;

import ws.IWsSocket;

/**
 * Interface da especificação do servidor Http e Websocket.
 *
 * @author bernardobreder
 *
 */
public interface IWsServer {

  /**
   * Cria um cliente através do socket
   * 
   * @param socket
   * @return cliente
   * @throws IOException
   */
  public IWsClient createWsClient(IWsSocket socket) throws IOException;

  /**
   * Inicia o servidor
   * 
   * @return this
   */
  public IWsServer start();

  /**
   * Finaliza o servidor
   * 
   * @return this
   */
  public IWsServer close();

  /**
   * Listener do navegador do cliente
   * 
   * @author bernardobreder
   */
  public static interface IWsServerListener {

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