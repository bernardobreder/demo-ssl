import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

public class WebSocket {

  /** Chave de Default */
  private static final String SEC_WEB_SOCKET_KEY = "Sec-WebSocket-Key";
  private final Socket socket;
  private final Map<String, String> headers;
  private boolean defaultSpecification;
  private byte[] bytes = new byte[1024];
  private final byte[] bytes2 = new byte[2];
  private final byte[] bytes4 = new byte[4];
  private final byte[] bytes8 = new byte[8];
  private final int[] mask = new int[4];
  private long bytesTime;
  private static final int BYTES_TIMEOUT = 60 * 1000;

  public WebSocket(Socket socket, Map<String, String> headers) {
    this.socket = socket;
    this.headers = headers;
    this.defaultSpecification = headers.containsKey(SEC_WEB_SOCKET_KEY);
  }

  protected String readMessage() throws IOException {
    if (this.defaultSpecification) {
      return this.readMessageDefault();
    }
    else {
      return this.readMessageDarft();
    }
  }

  protected String readMessageDefault() throws IOException, EOFException {
    InputStream in = socket.getInputStream();
    in.read(bytes2);
    int byte1 = bytes2[0] & 0xFF;
    int byte2 = bytes2[1] & 0xFF;
    if (byte1 < 0 || byte2 < 0) {
      return null;
    }
    int opcode = byte1 & 0xF;
    if (opcode == 0x8) {
      return null;
    }
    long length = byte2 & 0x7F;
    if (length == 126) {
      in.read(bytes2);
      length = ((bytes2[0] & 0xFF) << 8) + (bytes2[1] & 0xFF);
    }
    else if (length == 127) {
      in.read(bytes8);
      length =
        (((long) (bytes8[0] & 0xFF) << 56) + ((long) (bytes8[1] & 255) << 48)
          + ((long) (bytes8[2] & 255) << 40) + ((long) (bytes8[3] & 255) << 32)
          + ((long) (bytes8[4] & 255) << 24) + ((bytes8[5] & 255) << 16)
          + ((bytes8[6] & 255) << 8) + ((bytes8[7] & 255) << 0));
    }
    int intPayload = checkSize(length);
    in.read(bytes4);
    for (int n = 0; n < 4; n++) {
      mask[n] = bytes4[n] & 0xFF;
    }
    return this.readData(intPayload);
  }

  protected String readMessageDarft() throws IOException {
    InputStream in = socket.getInputStream();
    int frameType = in.read();
    if (frameType < 0) {
      return null;
    }
    else if ((frameType & 0x80) == 0x80) {
      long length = 0;
      int b;
      do {
        b = in.read();
        int b_v = b & 0x7F;
        length = length * 128 + b_v;
      } while ((b & 0x80) == 0x80);
      int intLength = checkSize(length);
      return this.readData(intLength);
    }
    else {
      return this.readData();
    }
  }

  protected String readData(int len) throws IOException {
    InputStream in = socket.getInputStream();
    StringBuilder sb = new StringBuilder(len);
    for (int n = 0, j = 0, c, c2, c3; n < len;) {
      n += in.read(bytes, 0, Math.min(bytes.length, len - n));
      for (int i = 0; j < n; i++, j++) {
        c = (bytes[i] & 0xFF) ^ mask[j % 4];
        if (c <= 0x7F) {
        }
        else if ((c >> 5) == 0x6) {
          if (++i >= bytes.length) {
            n += in.read(bytes, 0, Math.min(bytes.length, len - n));
            i = 0;
          }
          c2 = (bytes[i] & 0xFF) ^ mask[++j % 4];
          c = (((c & 0x1F) << 6) + (c2 & 0x3F));
        }
        else {
          if (++i >= bytes.length) {
            n += in.read(bytes, 0, Math.min(bytes.length, len - n));
            i = 0;
          }
          c2 = (bytes[i] & 0xFF) ^ mask[++j % 4];
          c3 = (bytes[++i] & 0xFF) ^ mask[++j % 4];
          c = (((c & 0xF) << 12) + ((c2 & 0x3F) << 6) + (c3 & 0x3F));
        }
        sb.append((char) c);
      }
    }
    return sb.toString();
  }

  protected String readData() throws IOException {
    InputStream in = socket.getInputStream();
    StringBuilder sb = new StringBuilder();
    int c = in.read();
    while (c != 0xFF) {
      if (c <= 0x7F) {
      }
      else if ((c >> 5) == 0x6) {
        int i2 = in.read();
        if (i2 < 0) {
          throw new EOFException();
        }
        c = (((c & 0x1F) << 6) + (i2 & 0x3F));
      }
      else {
        int i2 = in.read();
        if (i2 < 0) {
          throw new EOFException();
        }
        int i3 = in.read();
        if (i3 < 0) {
          throw new EOFException();
        }
        c = (((c & 0xF) << 12) + ((i2 & 0x3F) << 6) + (i3 & 0x3F));
      }
      sb.append((char) c);
      c = in.read();
    }
    return sb.toString();
  }

  protected void sendMessage(String message) throws IOException {
    if (this.defaultSpecification) {
      sendMessageDefault(message);
    }
    else {
      sendMessageDarft(message);
    }
  }

  protected void sendMessageDefault(String mess) throws IOException {
    OutputStream out = socket.getOutputStream();
    byte[] bytes = mess.getBytes("utf-8");
    int frameCount = 0;
    byte[] frame = new byte[10];
    frame[0] = (byte) 129;
    if (bytes.length <= 125) {
      frame[1] = (byte) bytes.length;
      frameCount = 2;
    }
    else if (bytes.length >= 126 && bytes.length <= 65535) {
      int len = bytes.length;
      frame[1] = (byte) 126;
      frame[2] = (byte) ((len >> 8) & (byte) 255);
      frame[3] = (byte) (len & (byte) 255);
      frameCount = 4;
    }
    else {
      long len = bytes.length;
      frame[1] = (byte) 127;
      frame[2] = (byte) ((len >> 56) & 0xFF);
      frame[3] = (byte) ((len >> 22) & 0xFF);
      frame[4] = (byte) ((len >> 40) & 0xFF);
      frame[5] = (byte) ((len >> 32) & 0xFF);
      frame[6] = (byte) ((len >> 24) & 0xFF);
      frame[7] = (byte) ((len >> 16) & 0xFF);
      frame[8] = (byte) ((len >> 8) & 0xFF);
      frame[9] = (byte) (len & (byte) 255);
      frameCount = 10;
    }
    int bLength = frameCount + bytes.length;
    byte[] reply = new byte[bLength];
    int bLim = 0;
    for (int i = 0; i < frameCount; i++) {
      reply[bLim] = frame[i];
      bLim++;
    }
    for (int i = 0; i < bytes.length; i++) {
      reply[bLim] = bytes[i];
      bLim++;
    }
    out.write(reply);
    out.flush();
  }

  protected void sendMessageDarft(String mess) throws IOException {
    OutputStream out = socket.getOutputStream();
    out.write(0);
    out.write(mess.getBytes("utf-8"));
    out.write(0xFF);
    out.flush();
  }

  protected byte[] getBytes(int size) {
    if (bytes.length < size) {
      bytes = new byte[size];
      bytesTime = System.currentTimeMillis();
    }
    else if (bytes.length / 4 > size
      && System.currentTimeMillis() - bytesTime > BYTES_TIMEOUT) {
      bytes = new byte[bytes.length / 2];
      bytesTime = System.currentTimeMillis();
    }
    return bytes;
  }

  protected static int checkSize(long payload) throws EOFException {
    if (payload > Integer.MAX_VALUE || payload < 0) {
      throw new EOFException("message is too big");
    }
    return (int) payload;
  }

  /**
   * @param in
   * @return char
   * @throws IOException
   * @throws EOFException
   */
  protected static int readUtf8(InputStream in) throws IOException,
    EOFException {
    int c = in.read();
    if (c <= 0x7F) {
      return c;
    }
    else if ((c >> 5) == 0x6) {
      int i2 = in.read();
      if (i2 < 0) {
        throw new EOFException();
      }
      return (((c & 0x1F) << 6) + (i2 & 0x3F));
    }
    else {
      int i2 = in.read();
      if (i2 < 0) {
        throw new EOFException();
      }
      int i3 = in.read();
      if (i3 < 0) {
        throw new EOFException();
      }
      return (((c & 0xF) << 12) + ((i2 & 0x3F) << 6) + (i3 & 0x3F));
    }
  }

  /**
   * @param out
   * @param text
   * @throws IOException
   */
  protected static void writeUtf8(OutputStream out, String text)
    throws IOException {
    int length = text.length();
    for (int n = 0; n < length; n++) {
      char c = text.charAt(n);
      if (c <= 0x7F) {
        out.write(c);
      }
      else if (c <= 0x7FF) {
        out.write(((c >> 6) & 0x1F) + 0xC0);
        out.write((c & 0x3F) + 0x80);
      }
      else {
        out.write(((c >> 12) & 0xF) + 0xE0);
        out.write(((c >> 6) & 0x3F) + 0x80);
        out.write((c & 0x3F) + 0x80);
      }
    }
  }

}

// http://tools.ietf.org/html/rfc6455
// http://datatracker.ietf.org/doc/rfc6455/
// http://www.websocket.org/echo.html