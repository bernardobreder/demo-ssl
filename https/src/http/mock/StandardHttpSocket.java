package http.mock;

import http.base.AbstractHttpSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public class StandardHttpSocket extends AbstractHttpSocket {

  /** Socket */
  protected final Socket socket;

  /**
   * @param socket
   * @throws IOException
   */
  public StandardHttpSocket(Socket socket) throws IOException {
    this.socket = socket;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
    this.socket.close();
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
