package http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import socket.ISocket;

/**
 *
 *
 * @author bernardobreder
 */
public class HttpSocket {

  /** Socket */
  protected final ISocket socket;
  /** Requisição */
  protected HttpRequest request;
  /** Response */
  protected HttpResponse response;

  /**
   * @param socket
   * @throws IOException
   */
  public HttpSocket(ISocket socket) throws IOException {
    this.socket = socket;
  }

  /**
   * @return the request
   * @throws IOException
   */
  public HttpRequest getRequest() throws IOException {
    if (this.request == null) {
      this.readRequest();
    }
    return request;
  }

  /**
   * @return the response
   */
  public HttpResponse getResponse() {
    return response;
  }

  /**
   * @return socket
   * @throws IOException
   */
  public HttpSocket readRequest() throws IOException {
    InputStream input = this.getInputStream();
    OutputStream output = getOutputStream();
    this.request = new HttpRequest(input);
    this.response = new HttpResponse(this.getRequest(), output);
    return this;
  }

  /**
   * @throws IOException
   */
  public void flush() throws IOException {
    this.request = null;
    this.response.flush();
  }

  /**
   * @throws IOException
   */
  public void close() throws IOException {
    this.socket.close();
  }

  /**
   * @return closed
   */
  public boolean isClosed() {
    return this.socket.isClosed();
  }

  /**
   * @return stream de saida
   * @throws IOException
   */
  public OutputStream getOutputStream() throws IOException {
    return socket.getOutputStream();
  }

  /**
   * @return stream de entrada
   * @throws IOException
   */
  public InputStream getInputStream() throws IOException {
    return socket.getInputStream();
  }

  /**
   * Atribui um timeout
   * 
   * @param milisegs
   * @return this
   */
  public HttpSocket setTimeout(int milisegs) {
    this.socket.setTimeout(milisegs);
    return this;
  }

}
