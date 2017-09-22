package http.mock;

import http.base.AbstractHttpSocket;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

import util.ByteArrayOutputStream;
import util.Bytes;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public class MockHttpSocket extends AbstractHttpSocket {

  /** Texto a ser enviado */
  private final StringBuilder sb = new StringBuilder();
  /** Texto a ser enviado */
  private ByteArrayOutputStream out = new ByteArrayOutputStream();
  /** Leitura de stream */
  private final MockHttpSocketInputStream in = new MockHttpSocketInputStream();
  /** Leitura de dados */
  private ByteArrayInputStream bytes;
  /** Thread do Servidor e do Cliente */
  private MockStream[] streams = new MockStream[2];
  /** Fechado */
  protected boolean closed;
  /** Porta */
  private final int port;

  public MockHttpSocket(int port) {
    this.port = port;
    for (int n = 0; n < 2; n++) {
      BufferedOutputStream out = new BufferedOutputStream(new Out(n));
      In in = new In(n);
      this.streams[n] = new MockStream(null, out, in);
    }
    this.streams[1].thread = Thread.currentThread();
    MockHttpServerSocket.addSocket(this);
  }

  public MockHttpSocket setServer() {
    this.streams[0].thread = Thread.currentThread();
    return this;
  }

  public MockHttpSocket setClient() {
    this.streams[1] =
      new MockStream(Thread.currentThread(), new ByteArrayOutputStream(),
        new ByteArrayInputStream(new byte[0]));
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
    this.getOutputStream().flush();
    this.in.close();
    this.out.close();
    this.closed = true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isClosed() {
    return this.closed;
  }

  /**
   * @return stream de saida
   */
  @Override
  public OutputStream getOutputStream() {
    Thread thread = Thread.currentThread();
    for (int n = 0; n < 2; n++) {
      if (thread == this.streams[n].thread) {
        return this.streams[n].out;
      }
    }
    throw new IllegalStateException("thread não registrada");
  }

  /**
   * @return stream de saida
   */
  public OutputStream getServerOutputStream() throws IOException {
    return this.streams[0].out;
  }

  /**
   * @return stream de saida
   */
  public OutputStream getClientOutputStream() throws IOException {
    return this.streams[1].out;
  }

  /**
   * @return stream de entrada
   */
  @Override
  public InputStream getInputStream() {
    Thread thread = Thread.currentThread();
    for (int n = 0; n < 2; n++) {
      if (thread == this.streams[n].thread) {
        return this.streams[n].in;
      }
    }
    throw new IllegalStateException("thread não registrada");
  }

  /**
   * @return stream
   */
  public InputStream getServerInputStream() {
    return this.streams[0].in;
  }

  /**
   * @return stream
   */
  public InputStream getClientInputStream() {
    return this.streams[1].in;
  }

  /**
   * @param text
   * @return this
   */
  public MockHttpSocket writeServer(String text) {
    if (text.length() == 0) {
      return this;
    }
    ByteArrayInputStream in =
      new ByteArrayInputStream(Bytes.getUtf8Bytes(text));
    LinkedList<ByteArrayInputStream> list = this.streams[1].inText;
    synchronized (list) {
      list.offer(in);
    }
    return this;
  }

  /**
   * @param text
   * @return this
   */
  public MockHttpSocket writeClient(String text) {
    if (text.length() == 0) {
      return this;
    }
    ByteArrayInputStream in =
      new ByteArrayInputStream(Bytes.getUtf8Bytes(text));
    LinkedList<ByteArrayInputStream> list = this.streams[0].inText;
    synchronized (list) {
      list.offer(in);
    }
    return this;
  }

  // /**
  // * @return this
  // * @throws IOException
  // */
  // public MockHttpSocket flush() {
  // Thread thread = Thread.currentThread();
  // MockStream serverStream = this.streams[0];
  // MockStream clientStream = this.streams[1];
  // if (clientStream != null && thread == clientStream.thread) {
  // MockStream auxStream = serverStream;
  // serverStream = clientStream;
  // clientStream = auxStream;
  // }
  // byte[] bytes = serverStream.out.toByteArray();
  // int available = clientStream.in.available();
  // ByteArrayOutputStream out = new ByteArrayOutputStream(bytes.length
  // + available);
  // if (available > 0) {
  // byte[] oldBytes = new byte[available];
  // clientStream.in.read(oldBytes, 0, available);
  // out.write(out.toByteArray());
  // }
  // out.write(bytes);
  // clientStream.in = new ByteArrayInputStream(out.toByteArray());
  // return this;
  // }

  /**
   * @return the port
   */
  public int getPort() {
    return port;
  }

  /**
   *
   *
   * @author Tecgraf/PUC-Rio
   */
  public class MockHttpSocketInputStream extends InputStream {

    /**
     * {@inheritDoc}
     */
    @Override
    public int read() throws IOException {
      while (bytes == null) {
        try {
          Thread.sleep(0);
        }
        catch (InterruptedException e) {
        }
      }
      int c = bytes.read();
      if (c == -1) {
        bytes = null;
      }
      return c;
    }

  }

  /**
   *
   *
   * @author Tecgraf/PUC-Rio
   */
  public class MockStream {

    private Thread thread;

    private OutputStream out;

    private InputStream in;

    private final LinkedList<ByteArrayInputStream> inText =
      new LinkedList<ByteArrayInputStream>();

    public MockStream(Thread thread, OutputStream out, InputStream in) {
      super();
      this.thread = thread;
      this.out = out;
      this.in = in;
    }

  }

  public class Out extends OutputStream {

    private final int index;

    private final int next;

    public Out(int index) {
      this.index = index;
      this.next = (index + 1) % 2;
    }

    @Override
    public void write(int b) throws IOException {
      LinkedList<ByteArrayInputStream> inText = streams[next].inText;
      synchronized (inText) {
        inText.offer(new ByteArrayInputStream(new byte[] { (byte) b }));
      }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
      LinkedList<ByteArrayInputStream> inText = streams[next].inText;
      synchronized (inText) {
        inText.offer(new ByteArrayInputStream(b, off, len));
      }
    }

  }

  public class In extends InputStream {

    private final int index;

    private final int next;

    public In(int index) {
      this.index = index;
      this.next = (index + 1) % 2;
    }

    @Override
    public int read() throws IOException {
      if (closed) {
        return -1;
      }
      LinkedList<ByteArrayInputStream> inText = streams[index].inText;
      boolean empty = true;
      while (empty) {
        synchronized (inText) {
          empty = inText.isEmpty();
        }
        if (!empty) {
          break;
        }
        try {
          Thread.sleep(0);
        }
        catch (InterruptedException e) {
          Thread.interrupted();
          return -1;
        }
      }
      int c;
      ByteArrayInputStream in;
      synchronized (inText) {
        in = inText.peek();
        int available = in.available();
        if (available == 1) {
          inText.remove();
        }
        c = in.read();
      }
      return c;
    }

  }

}
