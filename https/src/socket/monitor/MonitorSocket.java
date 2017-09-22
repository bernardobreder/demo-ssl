package socket.monitor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import socket.ISocket;

/**
 *
 *
 * @author bernardobreder
 */
public class MonitorSocket implements ISocket {

  /** Socket */
  private final ISocket socket;
  /** Entrada */
  private In in;
  /** Saída */
  private Out out;
  /** Owner do Monitor */
  private final String owner;

  // /** Fila de eventos na Thread do Navegador */
  // private final List<BrowserListener> listeners = new
  // ArrayList<BrowserListener>();

  /**
   * @param socket
   * @param owner
   */
  public MonitorSocket(ISocket socket, String owner) {
    this.socket = socket;
    this.owner = owner;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public InputStream getInputStream() throws IOException {
    if (this.in == null) {
      this.in = new In(socket.getInputStream());
    }
    return this.in;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public OutputStream getOutputStream() throws IOException {
    if (this.out == null) {
      this.out = new Out(socket.getOutputStream());
    }
    return this.out;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
    this.socket.close();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isClosed() {
    return this.socket.isClosed();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getPort() {
    return this.socket.getPort();
  }

  @Override
  public ISocket setTimeout(int milisegs) {
    this.socket.setTimeout(milisegs);
    return this;
  }

  /**
   * @param string
   */
  public void log(String string) {
    // System.out.println("[" + owner + "] " + string);
  }

  /**
   * @param b
   * @param off
   * @param len
   * @return string
   */
  protected String fixString(byte[] b, int off, int len) {
    StringBuilder sb = new StringBuilder();
    for (int n = off; n < len; n++) {
      int c = b[n] & 0xFF;
      if (c == '\n') {
        sb.append("\\n");
      }
      else if (c == '\r') {
        sb.append("\\r");
      }
      else if (c == '\t') {
        sb.append("\\t");
      }
      else if (c == '\b') {
        sb.append("\\b");
      }
      else if (c == '\f') {
        sb.append("\\f");
      }
      else if (c < ' ' || c > '~') {
        sb.append("\\u");
        sb.append(Integer.toHexString(c));
      }
      else {
        sb.append((char) c);
      }
    }
    return sb.toString();
  }

  /**
   * 
   * 
   * @author bernardobreder
   */
  public class In extends InputStream {

    /** Stream */
    private final InputStream in;

    /**
     * @param in
     */
    public In(InputStream in) {
      this.in = in;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read() throws IOException {
      int c = this.in.read();
      if (c > 0) {
        log("read: " + fixString(new byte[] { (byte) c }, 0, 1));
      }
      return c;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(byte[] b) throws IOException {
      int n = this.in.read(b);
      if (n > 0) {
        log("read: " + fixString(b, 0, b.length));
      }
      return n;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
      int n = this.in.read(b, off, len);
      if (n > 0) {
        log("read: " + fixString(b, off, n));
      }
      return n;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long skip(long n) throws IOException {
      long skip = this.in.skip(n);
      log("skip: " + Long.toString(skip));
      return skip;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int available() throws IOException {
      return this.in.available();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
      log("close");
      this.in.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void mark(int readlimit) {
      log("mark: " + Integer.toString(readlimit));
      this.in.mark(readlimit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void reset() throws IOException {
      log("reset");
      this.in.reset();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean markSupported() {
      return this.in.markSupported();
    }

  }

  /**
   * 
   * 
   * @author bernardobreder
   */
  public class Out extends OutputStream {

    /** Stream */
    private OutputStream out;

    /**
     * @param out
     */
    public Out(OutputStream out) {
      this.out = out;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(int b) throws IOException {
      log("write: " + fixString(new byte[] { (byte) b }, 0, 1));
      this.out.write(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(byte[] b) throws IOException {
      this.write(b, 0, b.length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
      log("write: (" + len + "): " + fixString(b, off, len));
      this.out.write(b, off, len);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() throws IOException {
      log("flush:");
      this.out.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
      log("close:");
      this.out.close();
    }

  }

  /**
   * Classe de listener do monitor
   * 
   * @author bernardobreder
   */
  public static class MonitorSocketListener {

  }

}
