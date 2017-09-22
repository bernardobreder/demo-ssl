package socket.mock;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

import socket.ISocket;
import util.ByteArrayInputStream;

/**
 * Mock de um Socket
 *
 * @author bernardobreder
 *
 */
public class MockSocket implements ISocket {

  /** Porta */
  private final int port;
  /** Próximo objeto sincronizado */
  private final MockSocket next;
  /** Entrada de dados */
  private final MockSocketInputStream in = new MockSocketInputStream();
  /** Saída de dados */
  private final MockSocketOutputStream out = new MockSocketOutputStream();
  /** Indica se está fechado */
  private boolean closed;

  /**
   * Construtor
   * 
   * @param port
   */
  public MockSocket(int port) {
    this.port = port;
    this.next = new MockSocket(this);
    MockServerSocket.addSocket(this.next);
  }

  /**
   * Construtor interno
   * 
   * @param parent
   */
  private MockSocket(MockSocket parent) {
    this.next = parent;
    this.port = parent.port;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MockSocketInputStream getInputStream() {
    return this.in;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MockSocketOutputStream getOutputStream() {
    return this.out;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() {
    this.closed = true;
    this.in.close();
    this.out.close();
    if (!this.next.closed) {
      this.next.close();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isClosed() {
    return this.closed;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getPort() {
    return this.port;
  }

  @Override
  public ISocket setTimeout(int milisegs) {
    return this;
  }

  /**
   * Classe de Entrada de Dados
   * 
   * @author bernardobreder
   * 
   */
  public class MockSocketInputStream extends InputStream {

    /** Texto */
    LinkedList<ByteArrayInputStream> inText =
      new LinkedList<ByteArrayInputStream>();
    /** Fechado */
    private boolean closed;

    /**
     * {@inheritDoc}
     */
    @Override
    public int read() {

      LinkedList<ByteArrayInputStream> inText = in.inText;
      for (;;) {
        synchronized (inText) {
          if (!inText.isEmpty()) {
            break;
          }
          if (this.closed) {
            return -1;
          }
          try {
            inText.wait(100);
          }
          catch (InterruptedException e) {
          }
          if (!inText.isEmpty()) {
            break;
          }
        }
      }
      synchronized (inText) {
        ByteArrayInputStream in = inText.peek();
        if (in.available() == 1) {
          inText.remove();
        }
        return in.read();
      }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(byte[] b) {
      return this.read(b, 0, b.length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(byte[] b, int off, int len) {
      int result = 0;
      LinkedList<ByteArrayInputStream> inText = in.inText;
      for (int n = 0; n < len; n++) {
        for (;;) {
          synchronized (inText) {
            if (!inText.isEmpty()) {
              break;
            }
            if (this.closed) {
              if (result == 0 && this.closed) {
                return -1;
              }
              else {
                return result;
              }
            }
            try {
              inText.wait(100);
            }
            catch (InterruptedException e) {
            }
            if (!inText.isEmpty()) {
              break;
            }
          }
        }
        synchronized (inText) {
          ByteArrayInputStream in = inText.peek();
          int available = in.available();
          if (available <= len) {
            inText.remove();
          }
          int max = Math.min(available, len - n);
          int count = in.read(b, n, max);
          n += count;
          result += count;
        }
      }
      if (result == 0 && this.closed) {
        return -1;
      }
      return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long skip(long n) {
      long remaining = n;
      int nr;
      if (n <= 0) {
        return 0;
      }
      int size = (int) Math.min(1024, remaining);
      byte[] skipBuffer = new byte[size];
      while (remaining > 0) {
        nr = read(skipBuffer, 0, (int) Math.min(size, remaining));
        if (nr < 0) {
          break;
        }
        remaining -= nr;
      }
      return n - remaining;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int available() {
      LinkedList<ByteArrayInputStream> inText = in.inText;
      synchronized (inText) {
        if (inText.isEmpty()) {
          return 0;
        }
        return inText.peek().available();
      }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
      this.closed = true;
      LinkedList<ByteArrayInputStream> thisIn = this.inText;
      LinkedList<ByteArrayInputStream> nextIn = next.in.inText;
      synchronized (thisIn) {
        thisIn.notifyAll();
      }
      synchronized (nextIn) {
        nextIn.notifyAll();
      }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mark(int readlimit) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean markSupported() {
      return false;
    }

  }

  /**
   * Classe de Saída de Dados
   * 
   * @author bernardobreder
   * 
   */
  public class MockSocketOutputStream extends OutputStream {

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(int b) {
      write(new byte[] { (byte) b }, 0, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(byte[] b) {
      writeInternal(b, 0, b.length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(byte[] b, int off, int len) {
      writeInternal(b, off, len);
    }

    /**
     * Implementação interna da escrita
     * 
     * @param b
     * @param off
     * @param len
     */
    protected void writeInternal(byte[] b, int off, int len) {
      LinkedList<ByteArrayInputStream> inText = next.in.inText;
      ByteArrayInputStream in = new ByteArrayInputStream(b, off, len);
      synchronized (inText) {
        inText.offer(in);
        inText.notifyAll();
      }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
      flush();
    }

  }

}
