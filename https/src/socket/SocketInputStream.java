package socket;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 *
 * @author bernardobreder
 */
public class SocketInputStream extends InputStream {

  /** Entrada */
  protected final InputStream in;

  /**
   * @param in
   */
  public SocketInputStream(InputStream in) {
    this.in = in;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int read() throws IOException {
    return in.read();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return in.hashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int read(byte[] b) throws IOException {
    return in.read(b);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    return in.equals(obj);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    return in.read(b, off, len);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long skip(long n) throws IOException {
    return in.skip(n);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return in.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int available() throws IOException {
    return in.available();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
    in.close();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void mark(int readlimit) {
    in.mark(readlimit);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reset() throws IOException {
    in.reset();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean markSupported() {
    return in.markSupported();
  }

}
