package jtecweb;

import http.HttpRequest;
import http.HttpResponse;

import java.io.IOException;

/**
 * Interface da especificação do cliente Http
 *
 * @author bernardobreder
 *
 */
public interface IHttpClient {

  /**
   * Responde ao serviço
   * 
   * @param req
   * @param resp
   * @throws IOException
   */
  public void service(HttpRequest req, HttpResponse resp) throws IOException;

  /**
   * Inicia o cliente
   * 
   * @return this
   */
  public IHttpClient start();

  /**
   * Finaliza o cliente
   * 
   * @return this
   */
  public IHttpClient close();

  /**
   * Adiciona um listener
   * 
   * @param listener
   * @return this
   */
  public IHttpClient addListener(IHttpClientListener listener);

  /**
   * Remove um listener
   * 
   * @param listener
   * @return this
   */
  public IHttpClient removeListener(IHttpClientListener listener);

  /**
   * Remove todos os listeners
   * 
   * @return this
   */
  public IHttpClient removeHttpListeners();

  /**
   * Implementação de um adaptador para a interface
   * 
   * @author bernardobreder
   * 
   */
  public static class AdapterHttpClientListener implements IHttpClientListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void closed(IHttpClient client) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(IHttpClient client, Throwable e) {
    }

  }

  /**
   * Listener do navegador do cliente
   * 
   * @author bernardobreder
   */
  public static interface IHttpClientListener {

    /**
     * Indica que foi fechado o navegador
     * 
     * @param client
     */
    public void closed(IHttpClient client);

    /**
     * Indica o erro ocorrido no cliente
     * 
     * @param client
     * @param e
     */
    public void error(IHttpClient client, Throwable e);

  }

}