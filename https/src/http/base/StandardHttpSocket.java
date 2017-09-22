package http.base;

import http.HttpRequest;
import http.HttpResponse;
import http.HttpSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public class StandardHttpSocket implements HttpSocket {

  /** Socket */
  private final Socket socket;
  /** Requisição */
  private HttpRequest request;
  /** Response */
  private HttpResponse response;

  /**
   * @param socket
   * @throws IOException
   */
  public StandardHttpSocket(Socket socket) throws IOException {
    this.socket = socket;
    this.readRequest();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
    this.socket.close();
  }

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

  /**
   * @return stream de saida
   * @throws IOException
   */
  @Override
  public OutputStream getOutputStream() throws IOException {
    return socket.getOutputStream();
  }

  /**
   * @return stream de entrada
   * @throws IOException
   */
  @Override
  public InputStream getInputStream() throws IOException {
    return socket.getInputStream();
  }

}
