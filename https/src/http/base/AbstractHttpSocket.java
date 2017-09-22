package http.base;

import http.HttpRequest;
import http.HttpResponse;
import http.HttpSocket;

import java.io.IOException;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public abstract class AbstractHttpSocket implements HttpSocket {

  /** Requisição */
  protected HttpRequest request;
  /** Response */
  protected HttpResponse response;

  /**
   * @return the request
   */
  @Override
  public HttpRequest getRequest() {
    return request;
  }

  /**
   * @return the response
   */
  @Override
  public HttpResponse getResponse() {
    return response;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HttpSocket readRequest() throws IOException {
    this.request = new HttpRequest(this.getInputStream());
    this.response = new HttpResponse(this.getRequest(), getOutputStream());
    return this;
  }

}
