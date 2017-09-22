package websocket.standard;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

import websocket.WebSocket;

/**
 * Implementação padrão do WebSocket
 *
 * @author Tecgraf/PUC-Rio
 */
public class StandardWebSocket implements WebSocket {

  /** Chave de Default */
  private static final String SEC_WEB_SOCKET_KEY = "Sec-WebSocket-Key";
  /** Socket de comunicação */
  private final Socket socket;
  /** Indica se está trabalhando com a implementação padrão */
  private boolean defaultSpecification;
  /** Bytes a serem transferidos */
  private byte[] bytes = new byte[1024];
  /** Bytes de 2 bytes */
  private final byte[] bytes2 = new byte[2];
  /** Bytes de 4 bytes */
  private final byte[] bytes4 = new byte[4];
  /** Bytes de 8 bytes */
  private final byte[] bytes8 = new byte[8];
  /** Bytes de mascara */
  private final int[] mask = new int[4];
  /** Bytes de envio */
  private final byte[] frame = new byte[10];

  /**
   * Construtor padrão
   * 
   * @param socket
   * @param headers
   */
  public StandardWebSocket(Socket socket, Map<String, String> headers) {
    this.socket = socket;
    this.defaultSpecification = headers.containsKey(SEC_WEB_SOCKET_KEY);
    // try {
    // socket.setSoTimeout(5000);
    // } catch (SocketException e) {
    // }
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
  public String readMessage() throws IOException {
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
    else if (opcode != 1) {
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

  @Override
  public void sendMessage(String message) throws IOException {
    if (this.defaultSpecification) {
      sendMessageDefault(message);
    }
    else {
      sendMessageDarft(message);
    }
  }

  protected void sendMessageDefault(String message) throws IOException {
    OutputStream out = socket.getOutputStream();
    byte[] bytes = getUtf8Bytes(message);
    int byteLen = bytes.length;
    int frameCount = 0;
    frame[0] = (byte) 129;
    if (byteLen <= 125) {
      frame[1] = (byte) byteLen;
      frameCount = 2;
    }
    else if (byteLen >= 126 && byteLen <= 65535) {
      int len = byteLen;
      frame[1] = (byte) 126;
      frame[2] = (byte) ((len >> 8) & (byte) 255);
      frame[3] = (byte) (len & (byte) 255);
      frameCount = 4;
    }
    else {
      long len = byteLen;
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
    out.write(frame, 0, frameCount);
    out.write(bytes, 0, byteLen);
    out.flush();
  }

  protected void sendMessageDarft(String message) throws IOException {
    OutputStream out = socket.getOutputStream();
    byte[] bytes = getUtf8Bytes(message);
    out.write(0);
    out.write(bytes);
    out.write(0xFF);
    out.flush();
  }

  protected static int checkSize(long payload) throws EOFException {
    if (payload > Integer.MAX_VALUE || payload < 0) {
      throw new EOFException("message is too big");
    }
    return (int) payload;
  }

  protected static byte[] getUtf8Bytes(String text) {
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

}

// http://tools.ietf.org/html/rfc6455
// http://datatracker.ietf.org/doc/rfc6455/
// http://www.websocket.org/echo.html