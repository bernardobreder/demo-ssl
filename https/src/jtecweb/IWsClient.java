package jtecweb;

import java.io.IOException;

import ws.WsRequest;
import ws.WsResponse;

/**
 * Interface da especificação do cliente Http
 *
 * @author bernardobreder
 *
 */
public interface IWsClient {

  /**
   * Responde ao serviço Websocket
   * 
   * @param req
   * @param resp
   * @throws IOException
   */
  public void service(WsRequest req, WsResponse resp) throws IOException;

  /**
   * Inicia o cliente
   * 
   * @return this
   */
  public IWsClient start();

  /**
   * Finaliza o cliente
   * 
   * @return this
   */
  public IWsClient close();

  /**
   * Adiciona um listener
   * 
   * @param listener
   * @return this
   */
  public IWsClient addListener(IWsClientListener listener);

  /**
   * Remove um listener
   * 
   * @param listener
   * @return this
   */
  public IWsClient removeListener(IWsClientListener listener);

  /**
   * Remove todos os listeners
   * 
   * @return this
   */
  public IWsClient removeWsListeners();

  /**
   * Implementação de um adaptador para a interface
   * 
   * @author bernardobreder
   * 
   */
  public static class AdapterWsClientListener implements IWsClientListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void closed(IWsClient client) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(IWsClient client, Throwable e) {
    }

  }

  /**
   * Listener do navegador do cliente
   * 
   * @author bernardobreder
   */
  public static interface IWsClientListener {

    /**
     * Indica que foi fechado o navegador
     * 
     * @param client
     */
    public void closed(IWsClient client);

    /**
     * Indica o erro ocorrido no cliente
     * 
     * @param client
     * @param e
     */
    public void error(IWsClient client, Throwable e);

  }

}