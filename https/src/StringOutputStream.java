import java.io.IOException;
import java.io.OutputStream;

/**
 *
 *
 * @author Tecgraf
 */
public class StringOutputStream extends OutputStream {

  /** Saida */
  private final OutputStream out;

  /**
   * @param out
   */
  public StringOutputStream(OutputStream out) {
    this.out = out;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void write(int n) throws IOException {
    out.write(n);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    out.write(b, off, len);
  }

  /**
   * @param text
   * @throws IOException
   */
  public void write(String text) throws IOException {
    out.write(getUtf8Bytes(text));
  }

  /**
   * @param text
   * @return bytes
   */
  public static byte[] getUtf8Bytes(String text) {
    int len = 0;
    int textlen = text.length();
    for (int n = 0; n < textlen; n++) {
      char c = text.charAt(n);
      if (c <= 0x7F) {
        len++;
      }
      else if (c <= 0x7FF) {
        len += 2;
      }
      else {
        len += 3;
      }
    }
    byte[] bytes = new byte[len];
    for (int n = 0, i = 0; n < textlen; n++, i++) {
      char c = text.charAt(n);
      if (c <= 0x7F) {
        bytes[i] = (byte) c;
      }
      else if (c <= 0x7FF) {
        bytes[i++] = (byte) (((c >> 6) & 0x1F) + 0xC0);
        bytes[i] = (byte) ((c & 0x3F) + 0x80);
      }
      else {
        bytes[i++] = (byte) (((c >> 12) & 0xF) + 0xE0);
        bytes[i++] = (byte) (((c >> 6) & 0x3F) + 0x80);
        bytes[i] = (byte) ((c & 0x3F) + 0x80);
      }
    }
    return bytes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void flush() throws IOException {
    out.flush();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
    out.close();
  }

}
