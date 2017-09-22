package socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 *
 * @author bernardobreder
 */
public interface ISocket {

  /**
   * @return stream de entrada
   * @throws IOException
   */
  public InputStream getInputStream() throws IOException;

  /**
   * @return stream de saida
   * @throws IOException
   */
  public OutputStream getOutputStream() throws IOException;

  /**
   * @throws IOException
   */
  public void close() throws IOException;

  /**
   * @return fechado
   */
  public boolean isClosed();

  /**
   * @return port
   */
  public int getPort();

  /**
   * Atritui um timeout
   * 
   * @param milisegs
   */
  public ISocket setTimeout(int milisegs);

}
