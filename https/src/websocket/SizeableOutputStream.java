package websocket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public class SizeableOutputStream extends OutputStream {

  /** Maximo de memória */
  private static final int MAX = 0xFFFF;
  /** Tamanho */
  private long size;
  /** Saida de Memoria */
  private ByteArrayOutputStream bout;
  /** Saida de Arquivo */
  private FileOutputStream fout;
  /** Arquivo */
  private File file;
  /** Stream de leitura */
  private InputStream in;

  /**
   * @param size
   * @throws IOException
   */
  public SizeableOutputStream(long size) throws IOException {
    this.bout = new ByteArrayOutputStream((int) size);
    if (size > MAX) {
      this.checkGrow();
    }
  }

  /**
   * @return stream
   * @throws IOException
   */
  public InputStream getInputStream() throws IOException {
    if (this.in == null) {
      if (this.file != null) {
        try {
          this.fout.close();
        }
        catch (IOException e) {
          this.delete();
          throw e;
        }
        this.in = new FileInputStream(this.file) {
          @Override
          public void close() throws IOException {
            try {
              super.close();
            }
            catch (IOException e) {
            }
            delete();
          }
        };
      }
      else {
        this.in = new ByteArrayInputStream(this.bout.toByteArray());
      }
    }
    return this.in;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void write(int b) throws IOException {
    this.size++;
    if (this.file != null) {
      try {
        this.fout.write(b);
      }
      catch (IOException e) {
        this.delete();
        throw e;
      }
    }
    else {
      this.bout.write(b);
      if (this.size > MAX) {
        this.checkGrow();
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void write(byte[] b) throws IOException {
    this.size += b.length;
    if (this.file != null) {
      try {
        this.fout.write(b);
      }
      catch (IOException e) {
        this.delete();
        throw e;
      }
    }
    else {
      if (this.size > MAX) {
        this.checkGrow();
        this.size -= b.length;
        this.write(b);
      }
      else {
        this.bout.write(b);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    this.size += len;
    if (this.file != null) {
      try {
        this.fout.write(b, off, len);
      }
      catch (IOException e) {
        this.delete();
        throw e;
      }
    }
    else {
      if (this.size > MAX) {
        this.checkGrow();
        this.size -= len;
        this.write(b, off, len);
      }
      else {
        this.bout.write(b, off, len);
      }
    }
  }

  /**
   * @return tamanho da stream
   */
  public long length() {
    return size;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void flush() throws IOException {
    if (this.file != null) {
      this.fout.flush();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
    if (this.in != null) {
      this.in.close();
      this.in = null;
    }
    if (this.file != null) {
      this.delete();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void finalize() throws Throwable {
    this.close();
  }

  /**
   * @throws IOException
   * @throws FileNotFoundException
   */
  private void checkGrow() throws IOException {
    this.file = File.createTempFile("jstream.", ".tmp");
    try {
      this.fout = new FileOutputStream(file);
      if (this.bout.size() > 0) {
        this.fout.write(this.bout.toByteArray());
      }
      this.bout = null;
    }
    catch (IOException e) {
      this.delete();
      throw e;
    }
  }

  /**
   * Deleta o arquivo
   */
  protected void delete() {
    if (file != null) {
      if (!file.delete()) {
        try {
          this.fout.close();
        }
        catch (IOException e) {
        }
        if (!file.delete()) {
          Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
              while (!file.delete()) {
                try {
                  Thread.sleep(100);
                }
                catch (InterruptedException e) {
                  file.delete();
                  file.deleteOnExit();
                  break;
                }
              }
            }
          }, SizeableOutputStream.class.getSimpleName());
          thread.setDaemon(false);
          thread.start();
        }
      }
    }
    this.file = null;
    this.fout = null;
    this.size = 0;
  }

}
