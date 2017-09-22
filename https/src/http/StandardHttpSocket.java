package http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import socket.Socket;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public class StandardHttpSocket {

  /** Socket */
  protected final Socket socket;
  /** Requisição */
  protected HttpRequest request;
  /** Response */
  protected HttpResponse response;

  /**
   * @param socket
   * @throws IOException
   */
  public StandardHttpSocket(Socket socket) throws IOException {
    this.socket = socket;
  }

  /**
   * @return the request
   */
  public HttpRequest getRequest() {
    return request;
  }

  /**
   * @return the response
   */
  public HttpResponse getResponse() {
    return response;
  }

  /**
   * {@inheritDoc}
   */
  public StandardHttpSocket readRequest() throws IOException {
    this.request = new HttpRequest(this.getInputStream());
    this.response = new HttpResponse(this.getRequest(), getOutputStream());
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public void close() throws IOException {
    this.socket.close();
  }

  /**
   * {@inheritDoc}
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

}
