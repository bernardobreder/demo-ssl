package util;

import java.io.OutputStream;
import java.util.Arrays;

/**
 *
 *
 * @author bernardobreder
 */
public class ByteArrayOutputStream extends OutputStream {

  /**
   * The buffer where data is stored.
   */
  protected byte buf[];

  /**
   * The number of valid bytes in the buffer.
   */
  protected int count;

  /**
   * Closed
   */
  protected boolean closed;

  /**
   * Creates a new byte array output stream. The buffer capacity is initially 32
   * bytes, though its size increases if necessary.
   */
  public ByteArrayOutputStream() {
    this(32);
  }

  /**
   * Creates a new byte array output stream, with a buffer capacity of the
   * specified size, in bytes.
   * 
   * @param size the initial size.
   * @exception IllegalArgumentException if size is negative.
   */
  public ByteArrayOutputStream(int size) {
    if (size < 0) {
      throw new IllegalArgumentException("Negative initial size: " + size);
    }
    buf = new byte[size];
  }

  /**
   * Writes the specified byte to this byte array output stream.
   * 
   * @param b the byte to be written.
   */
  @Override
  public void write(int b) {
    int newcount = count + 1;
    if (newcount > buf.length) {
      buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
    }
    buf[count] = (byte) b;
    count = newcount;
  }

  /**
   * Writes <code>b.length</code> bytes from the specified byte array to this
   * output stream. The general contract for <code>write(b)</code> is that it
   * should have exactly the same effect as the call
   * <code>write(b, 0, b.length)</code>.
   * 
   * @param b the data.
   * @see java.io.OutputStream#write(byte[], int, int)
   */
  @Override
  public void write(byte b[]) {
    write(b, 0, b.length);
  }

  /**
   * Writes <code>len</code> bytes from the specified byte array starting at
   * offset <code>off</code> to this byte array output stream.
   * 
   * @param b the data.
   * @param off the start offset in the data.
   * @param len the number of bytes to write.
   */
  @Override
  public void write(byte b[], int off, int len) {
    if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length)
      || ((off + len) < 0)) {
      throw new IndexOutOfBoundsException();
    }
    else if (len == 0) {
      return;
    }
    int newcount = count + len;
    if (newcount > buf.length) {
      buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
    }
    System.arraycopy(b, off, buf, count, len);
    count = newcount;
  }

  /**
   * Resets the <code>count</code> field of this byte array output stream to
   * zero, so that all currently accumulated output in the output stream is
   * discarded. The output stream can be used again, reusing the already
   * allocated buffer space.
   */
  public void reset() {
    count = 0;
  }

  /**
   * Creates a newly allocated byte array. Its size is the current size of this
   * output stream and the valid contents of the buffer have been copied into
   * it.
   * 
   * @return the current contents of this output stream, as a byte array.
   * @see java.io.ByteArrayOutputStream#size()
   */
  public byte[] toByteArray() {
    return Arrays.copyOf(buf, count);
  }

  /**
   * Returns the current size of the buffer.
   * 
   * @return the value of the <code>count</code> field, which is the number of
   *         valid bytes in this output stream.
   */
  public int size() {
    return count;
  }

  /**
   * Converts the buffer's contents into a string decoding bytes using the
   * platform's default character set. The length of the new <tt>String</tt> is
   * a function of the character set, and hence may not be equal to the size of
   * the buffer.
   * 
   * <p>
   * This method always replaces malformed-input and unmappable-character
   * sequences with the default replacement string for the platform's default
   * character set. The {@linkplain java.nio.charset.CharsetDecoder} class
   * should be used when more control over the decoding process is required.
   * 
   * @return String decoded from the buffer's contents.
   * @since JDK1.1
   */
  @Override
  public String toString() {
    return new String(buf, 0, count);
  }

  /**
   * Closing a <tt>ByteArrayOutputStream</tt> has no effect. The methods in this
   * class can be called after the stream has been closed without generating an
   * <tt>IOException</tt>.
   * <p>
   * 
   */
  @Override
  public void close() {
    this.closed = true;
  }

}
