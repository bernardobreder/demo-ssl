package jtecweb;

import java.io.IOException;

import ws.IWsSocket;
import ws.WsRequest;
import ws.WsResponse;

/**
 * Interface da especificação do cliente Http e Websocket.
 *
 * @author bernardobreder
 *
 */
public interface IHttpWsClient {

  /**
   * Responde a página inicial que realiza a conexão com o websocket.
   * 
   * @param socket
   * @throws IOException
   */
  public void connect(IWsSocket socket) throws IOException;

  /**
   * Responde a página inicial que realiza a conexão com o websocket.
   * 
   * @param req
   * @param resp
   * @throws IOException
   */
  public void message(WsRequest req, WsResponse resp) throws IOException;

  /**
   * @return sessão do usuário
   */
  public String getSession();

  /**
   * @return socket
   */
  public IWsSocket getSocket();

  /**
   * @param socket
   * @return socket
   */
  public IHttpWsClient setSocket(IWsSocket socket);

  /**
   * Adiciona um listener
   * 
   * @param listener
   * @return this
   */
  public IHttpWsClient addListener(IHttpWsClientListener listener);

  /**
   * Remove um listener
   * 
   * @param listener
   * @return this
   */
  public IHttpWsClient removeListener(IHttpWsClientListener listener);

  /**
   * Remove todos os listeners
   * 
   * @return this
   */
  public IHttpWsClient removeListeners();

  /**
   * Fecha o cliente
   */
  public void close();

  /**
   * Listener do navegador do cliente
   * 
   * @author bernardobreder
   */
  public static interface IHttpWsClientListener {

    /**
     * Indica que foi fechado o navegador
     * 
     * @param client cliente
     */
    public void closed(IHttpWsClient client);

    /**
     * Indica o erro ocorrido no cliente
     * 
     * @param client cliente
     * @param e
     */
    public void error(IHttpWsClient client, Throwable e);

  }

}