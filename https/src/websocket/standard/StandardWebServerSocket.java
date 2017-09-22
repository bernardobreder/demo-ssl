package websocket.standard;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import sun.misc.BASE64Encoder;
import websocket.WebServerSocket;
import websocket.WebSocket;

/**
 * Implementação padrão de Servidor WebSocket
 *
 * @author Tecgraf/PUC-Rio
 */
public class StandardWebServerSocket implements WebServerSocket {

  /** Separador de Linha */
  private static final Pattern COMPILE = Pattern.compile("\n");
  /** Chave de Default */
  private static final String SEC_WEB_SOCKET_KEY = "Sec-WebSocket-Key";
  /** Chave de Darft */
  private static final String SEC_WEB_SOCKET_KEY1 = "Sec-WebSocket-Key1";
  /** Servidor WebSocket */
  private ServerSocket server;
  /** Listeners */
  private List<WebServerSocketListener> listeners =
    new ArrayList<WebServerSocket.WebServerSocketListener>();
  /** Bytes de 8 bytes */
  private byte[] bytes8 = new byte[8];

  /**
   * @param port
   * @throws IOException
   */
  public StandardWebServerSocket(int port) throws IOException {
    this.server = new ServerSocket(port);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WebSocket accept() throws IOException {
    for (;;) {
      Socket socket = server.accept();
      try {
        StandardWebSocket webSocket =
          new StandardWebSocket(socket, openHandshake(socket, bytes8));
        this.fireAcceptWebSocket(webSocket);
        return webSocket;
      }
      catch (Throwable e) {
        this.fireError(e);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
    try {
      this.server.close();
    }
    finally {
      this.fireCloseServer();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addListener(WebServerSocketListener listener) {
    this.listeners.add(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean removeListener(WebServerSocketListener listener) {
    return this.listeners.remove(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clearListeners() {
    this.listeners.clear();
  }

  /**
   * @param in
   * @return header
   * @throws IOException
   * @throws EOFException
   */
  protected static Map<String, String> readHeaders(InputStream in)
    throws IOException, EOFException {
    StringBuilder sb = new StringBuilder();
    String header = null;
    int step = 0;
    for (int n; (n = in.read()) != -1; n++) {
      if (n == '\n') {
        if (++step == 2) {
          header = sb.toString().trim();
          break;
        }
      }
      else if (n == '\r') {
        continue;
      }
      else {
        step = 0;
      }
      sb.append((char) n);
    }
    if (header == null) {
      throw new EOFException();
    }
    Map<String, String> map = new HashMap<String, String>();
    String[] args = COMPILE.split(header, 0);
    for (int n = 1; n < args.length; n++) {
      String arg = args[n];
      int index = arg.indexOf(':');
      String key = arg.substring(0, index);
      String value = arg.substring(index + 2);
      map.put(key, value);
    }
    return map;
  }

  /**
   * @param socket
   * @param bytes8
   * @return header
   * @throws IOException
   */
  protected static Map<String, String> openHandshake(Socket socket,
    byte[] bytes8) throws IOException {
    InputStream in = socket.getInputStream();
    Map<String, String> headers = readHeaders(in);
    if (headers.containsKey(SEC_WEB_SOCKET_KEY)) {
      openHandshakeDefault(socket.getOutputStream(), headers);
    }
    else if (headers.containsKey(SEC_WEB_SOCKET_KEY1)) {
      in.read(bytes8);
      openHandshakeDarft(socket.getOutputStream(), headers, bytes8);
    }
    else {
      throw new IOException("web socket protocol unknown");
    }
    return headers;
  }

  /**
   * @param headers
   * @param out
   * @throws IOException
   */
  protected static void openHandshakeDefault(OutputStream out,
    Map<String, String> headers) throws IOException {
    StringBuilder sb = new StringBuilder();
    String key = headers.get(SEC_WEB_SOCKET_KEY);
    sb.append("HTTP/1.1 101 Switching Protocols\r\n");
    sb.append("Upgrade: websocket\r\n");
    sb.append("Connection: Upgrade\r\n");
    sb.append("Sec-WebSocket-Accept: " + openHandshakeDefaultCode(key) + "\r\n");
    sb.append("\r\n");
    writeAscii(out, sb.toString());
  }

  /**
   * @param key
   * @return handshake
   * @throws IOException
   */
  protected static String openHandshakeDefaultCode(String key)
    throws IOException {
    String session = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
    MessageDigest sha1Digest = getSha1Digest();
    sha1Digest.update((key + session).getBytes());
    BASE64Encoder encoder = new BASE64Encoder();
    return encoder.encode(sha1Digest.digest());
  }

  /**
   * @param out
   * @param headers
   * @param bytes
   * @throws IOException
   */
  protected static void openHandshakeDarft(OutputStream out,
    Map<String, String> headers, byte[] bytes) throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append("HTTP/1.1 101 WebSocket Protocol Handshake\r\n");
    sb.append("Upgrade: WebSocket\r\n");
    sb.append("Connection: Upgrade\r\n");
    if (headers.containsKey("Origin")) {
      sb.append("Sec-WebSocket-Origin: " + headers.get("Origin") + "\r\n");
    }
    if (headers.containsKey("Host")) {
      sb.append("Sec-WebSocket-Location: ws://" + headers.get("Host") + "/\r\n");
    }
    sb.append("Sec-WebSocket-Protocol: chat\r\n");
    sb.append("\r\n");
    writeAscii(out, sb.toString());
    out.write(openHandshakeDarftCode(headers.get(SEC_WEB_SOCKET_KEY1), headers
      .get("Sec-WebSocket-Key2"), bytes));
  }

  /**
   * @param key1
   * @param key2
   * @param bytes
   * @return bytes
   * @throws IOException
   */
  protected static byte[] openHandshakeDarftCode(String key1, String key2,
    byte[] bytes) throws IOException {
    String[] keys = new String[2];
    keys[0] = key1;
    keys[1] = key2;
    byte[] out = new byte[16];
    for (int n = 0; n < keys.length; n++) {
      String key = keys[n];
      int numberOfSpaces = 0;
      long value = 0;
      for (int m = 0; m < key.length(); m++) {
        char c = key.charAt(m);
        if (c >= '0' && c <= '9') {
          value = value * 10 + (c - '0');
        }
        else if (c == ' ') {
          numberOfSpaces++;
        }
      }
      value /= numberOfSpaces;
      out[n * 4] = (byte) ((value >> 24) & 0xFF);
      out[n * 4 + 1] = (byte) ((value >> 16) & 0xFF);
      out[n * 4 + 2] = (byte) ((value >> 8) & 0xFF);
      out[n * 4 + 3] = (byte) (value & 0xFF);
    }
    System.arraycopy(bytes, 0, out, 8, 8);
    MessageDigest md = getMd5Digest();
    md.update(out);
    return md.digest();
  }

  /**
   * @return sha1 digest
   * @throws IOException
   */
  protected static MessageDigest getSha1Digest() throws IOException {
    try {
      return MessageDigest.getInstance("SHA-1");
    }
    catch (NoSuchAlgorithmException e) {
      throw new IOException(e);
    }
  }

  /**
   * @return md5 sum digest
   * @throws IOException
   */
  protected static MessageDigest getMd5Digest() throws IOException {
    try {
      return MessageDigest.getInstance("MD5");
    }
    catch (NoSuchAlgorithmException e) {
      throw new IOException(e);
    }
  }

  /**
   * @param out
   * @param text
   * @throws IOException
   */
  protected static void writeAscii(OutputStream out, String text)
    throws IOException {
    int length = text.length();
    byte[] bytes = new byte[length];
    for (int n = 0; n < length; n++) {
      bytes[n] = (byte) text.charAt(n);
    }
    out.write(bytes);
  }

  /**
   * Dispara evento
   * 
   * @param e
   */
  protected void fireError(Throwable e) {
    if (!this.listeners.isEmpty()) {
      for (WebServerSocketListener listener : this.listeners) {
        listener.error(e);
      }
    }
  }

  /**
   * Dispara evento
   * 
   * @param socket
   */
  protected void fireAcceptWebSocket(WebSocket socket) {
    if (!this.listeners.isEmpty()) {
      for (WebServerSocketListener listener : this.listeners) {
        listener.acceptWebSocket(socket);
      }
    }
  }

  /**
   * Dispara evento
   */
  protected void fireCloseServer() {
    if (!this.listeners.isEmpty()) {
      for (WebServerSocketListener listener : this.listeners) {
        listener.closeServer();
      }
    }
  }

}

// http://tools.ietf.org/html/rfc6455
// http://datatracker.ietf.org/doc/rfc6455/
// http://www.websocket.org/echo.html