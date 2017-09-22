package app;

import http.HttpRequest;
import http.HttpResponse;

import java.io.IOException;

/**
 * Servidor de uma aplicação
 *
 * @author Tecgraf
 */
public interface IAppServer {

  /**
   * @param req
   * @param resp
   * @throws IOException
   * @throws ServletException
   */
  public abstract void servlet(HttpRequest req, HttpResponse resp)
    throws IOException, ServletException;

  /**
   * @return the closed
   */
  public abstract boolean isClosed();

  /**
   * @return this
   */
  public abstract IAppServer start();

  /**
   * Fecha o servidor
   */
  public abstract void close();

}