package socket;

import java.io.IOException;

/**
 *
 *
 * @author bernardobreder
 */
public interface ServerSocket {

  /**
   * @return socket
   * @throws IOException
   */
  public Socket accept() throws IOException;

  /**
   * @param timeout
   * @throws IOException
   */
  public void setTimeout(int timeout) throws IOException;

  /**
   * @throws IOException
   */
  public void close() throws IOException;

  /**
   * @return fechado
   */
  public boolean isClosed();

  /**
   * @return host
   */
  public String getHost();

  /**
   * @return porta
   */
  public int getPort();

}
