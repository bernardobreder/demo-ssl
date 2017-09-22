package jtecweb;

import http.HttpSocket;

import java.io.IOException;

/**
 * Interface da especificação do servidor Http e Websocket.
 *
 * @author bernardobreder
 *
 */
public interface IHttpServer {

  /**
   * Cria um cliente através do socket
   * 
   * @param socket
   * @return cliente
   * @throws IOException
   */
  public IHttpClient createHttpClient(HttpSocket socket) throws IOException;

  /**
   * Inicia o servidor
   * 
   * @return this
   */
  public IHttpServer start();

  /**
   * Finaliza o servidor
   * 
   * @return this
   */
  public IHttpServer close();

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