// package websockettmp;
//
// import java.io.ByteArrayInputStream;
// import java.io.ByteArrayOutputStream;
// import java.io.DataInputStream;
// import java.io.EOFException;
// import java.io.File;
// import java.io.FileInputStream;
// import java.io.FileOutputStream;
// import java.io.IOException;
// import java.io.InputStream;
// import java.io.InputStreamReader;
// import java.io.LineNumberReader;
// import java.io.OutputStream;
// import java.net.ServerSocket;
// import java.net.Socket;
// import java.security.MessageDigest;
// import java.security.NoSuchAlgorithmException;
// import java.util.Arrays;
// import java.util.HashMap;
// import java.util.Map;
//
// import sun.misc.BASE64Encoder;
//
// /**
// * Servidor para o WebSocket do HTML5
// *
// * @author Tecgraf
// */
// public class WebServerSocket {
//
// /** Servidor */
// private final ServerSocket server;
//
// /**
// * @param port
// * @throws IOException
// */
// public WebServerSocket(int port) throws IOException {
// this.server = new ServerSocket(port);
// }
//
// /**
// * @return socket
// * @throws IOException
// */
// public WebSocket accept() throws IOException {
// for (;;) {
// Socket socket = this.server.accept();
// try {
// return new WebSocket(socket);
// }
// catch (Exception e) {
// try {
// socket.close();
// }
// catch (IOException e1) {
// }
// }
// }
// }
//
// /**
// * Socket para Web
// *
// * @author Tecgraf
// */
// public static class WebSocket {
//
// /** Socket */
// private final Socket socket;
// /** Indica se está fechado */
// private boolean closed;
// /** Header */
// private Map<String, String> headers;
//
// /**
// * @param socket
// * @throws IOException
// */
// public WebSocket(Socket socket) throws IOException {
// this.socket = socket;
// LineNumberReader reader =
// new LineNumberReader(new InputStreamReader(socket.getInputStream()));
// String line = reader.readLine();
// line = reader.readLine();
// this.headers = new HashMap<String, String>();
// while (line != null && line.trim().length() > 0) {
// int index = line.indexOf(':');
// String key = line.substring(0, index).trim();
// String value = line.substring(index + 1).trim();
// headers.put(key, value);
// line = reader.readLine();
// }
// if (headers.containsKey("Sec-WebSocket-Key")) {
// this.acceptOneKey();
// }
// else if (headers.containsKey("Sec-WebSocket-Key1")) {
// byte[] bytes = new byte[8];
// for (int n = 0; n < bytes.length; n++) {
// bytes[n] = (byte) reader.read();
// }
// this.acceptTwoKeys(bytes);
// }
// else {
// throw new IOException("web socket protocol unknown");
// }
// }
//
// /**
// * @return stream para ser lido
// * @throws IOException
// */
// public InputStream readStream() throws IOException {
// if (headers.containsKey("Sec-WebSocket-Key")) {
// return this.readMessageOneKey();
// }
// else {
// return this.readMessageTwoKeys();
// }
// }
//
// /**
// * @return stream para ser escrito e fechado
// * @throws IOException
// */
// public OutputStream writeStream() throws IOException {
// if (headers.containsKey("Sec-WebSocket-Key")) {
// }
// else {
// }
// }
//
// /**
// * Fecha o socket
// *
// * @throws IOException
// */
// public void close() throws IOException {
// socket.close();
// }
//
// /**
// * Indica se está fechado
// *
// * @return fechado
// */
// public boolean isClosed() {
// return this.closed || socket.isClosed();
// }
//
// /**
// * @throws IOException
// */
// protected void acceptOneKey() throws IOException {
// OutputStream out = this.socket.getOutputStream();
// String key = headers.get("Sec-WebSocket-Key");
// String session = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
// MessageDigest sha1Digest = getSha1Digest();
// sha1Digest.update((key + session).getBytes());
// BASE64Encoder decoder = new BASE64Encoder();
// String acceptCode = decoder.encode(sha1Digest.digest());
// out.write("HTTP/1.1 101 Switching Protocols\r\n".getBytes());
// out.write("Upgrade: websocket\r\n".getBytes());
// out.write("Connection: Upgrade\r\n".getBytes());
// out.write(("Sec-WebSocket-Accept: " + acceptCode + "\r\n").getBytes());
// out.write("Sec-WebSocket-Protocol: chat\r\n".getBytes());
// out.write("\r\n".getBytes());
// }
//
// /**
// * @param out
// * @param headers
// * @param bytes
// * @throws IOException
// */
// protected void acceptTwoKeys(byte[] bytes) throws IOException {
// OutputStream out = this.socket.getOutputStream();
// out.write("HTTP/1.1 101 WebSocket Protocol Handshake\r\n".getBytes());
// out.write("Upgrade: WebSocket\r\n".getBytes());
// out.write("Connection: Upgrade\r\n".getBytes());
// out.write("Sec-WebSocket-Origin: file://\r\n".getBytes());
// out.write("Sec-WebSocket-Location: ws://localhost:8080/\r\n".getBytes());
// out.write("Sec-WebSocket-Protocol: chat\r\n".getBytes());
// out.write("\r\n".getBytes());
// out.write(openHandshake(headers.get("Sec-WebSocket-Key1"), headers
// .get("Sec-WebSocket-Key2"), bytes));
// }
//
// private byte[] openHandshake(String key1, String key2, byte[] bytes)
// throws IOException {
// MessageDigest md = getMd5Digest();
// StringBuilder mds = new StringBuilder(8);
// String[] keys = new String[2];
// keys[0] = key1;
// keys[1] = key2;
// byte[] out = new byte[16];
// for (int n = 0; n < keys.length; n++) {
// String key = keys[n];
// int numberOfSpaces = 0;
// long value = 0;
// for (int m = 0; m < key.length(); m++) {
// char c = key.charAt(m);
// if (c >= '0' && c <= '9') {
// value = value * 10 + (c - '0');
// }
// else if (c == ' ') {
// numberOfSpaces++;
// }
// }
// value /= numberOfSpaces;
// System.out.println("DivValue:" + value);
// System.out.println("Send:" + ((value >> 24) & 0xFF) + ","
// + ((value >> 16) & 0xFF) + "," + ((value >> 8) & 0xFF) + ","
// + (value & 0xFF));
// out[n * 4] = (byte) ((value >> 24) & 0xFF);
// out[n * 4 + 1] = (byte) ((value >> 16) & 0xFF);
// out[n * 4 + 2] = (byte) ((value >> 8) & 0xFF);
// out[n * 4 + 3] = (byte) (value & 0xFF);
// }
// System.arraycopy(bytes, 0, out, 8, 8);
// md.update(out);
// byte[] md5sum = md.digest();
// System.out.println("Key1:" + key1);
// System.out.println("Key2:" + key2);
// System.out.println("Key:" + Arrays.toString(bytes));
// System.out.println("Out:" + Arrays.toString(out));
// System.out.println("Result:" + Arrays.toString(md5sum));
// System.out.println("---");
// return md5sum;
// }
//
// /**
// * @return sha1 digest
// * @throws IOException
// */
// protected static MessageDigest getSha1Digest() throws IOException {
// try {
// return MessageDigest.getInstance("SHA-1");
// }
// catch (NoSuchAlgorithmException e) {
// throw new IOException(e);
// }
// }
//
// /**
// * @return md5 sum digest
// * @throws IOException
// */
// protected static MessageDigest getMd5Digest() throws IOException {
// try {
// return MessageDigest.getInstance("MD5");
// }
// catch (NoSuchAlgorithmException e) {
// throw new IOException(e);
// }
// }
//
// private InputStream readMessageOneKey() throws IOException {
// InputStream in = this.socket.getInputStream();
// DataInputStream din = new DataInputStream(in);
// int byte1 = din.read();
// int byte2 = din.read();
// if (byte1 < 0 || byte2 < 0) {
// return null;
// }
// int opcode = byte1 & 0xF;
// if (opcode == 8) {
// return null;
// }
// boolean isFinal = (byte1 & 0x80) != 0;
// int rsv1 = byte1 & 0x40;
// int rsv2 = byte1 & 0x20;
// int rsv3 = byte1 & 0x10;
// boolean isMask = (byte2 & 0x80) == 1;
// long payload = byte2 & 0x7F;
// if (payload == 126) {
// payload = din.readShort();
// }
// else if (payload == 127) {
// payload = din.readLong();
// }
// int[] mask = new int[4];
// for (int n = 0; n < 4; n++) {
// mask[n] = din.read();
// }
// if (payload > 0xFFFFFF) {
// ByteArrayOutputStream out = new ByteArrayOutputStream((int) payload);
// byte[] bytes = new byte[1024];
// while (payload > 0) {
// int max = (int) Math.min(bytes.length, payload);
// int n = din.read(bytes, 0, max);
// for (int i = 0; i < n; i++) {
// bytes[i] = (byte) (bytes[i] ^ mask[i % 4]);
// }
// payload -= n;
// out.write(bytes, 0, n);
// }
// return new ByteArrayInputStream(out.toByteArray());
// }
// else {
// final File file = File.createTempFile("websocket.", ".tmp");
// FileOutputStream out = new FileOutputStream(file);
// try {
// byte[] bytes = new byte[64 * 1024];
// while (payload > 0) {
// int max = (int) Math.min(bytes.length, payload);
// int n = din.read(bytes, 0, max);
// for (int i = 0; i < n; i++) {
// bytes[i] = (byte) (bytes[i] ^ mask[i % 4]);
// }
// payload -= n;
// out.write(bytes, 0, n);
// }
// }
// catch (IOException e) {
// try {
// out.close();
// }
// catch (IOException e1) {
// }
// file.delete();
// }
// finally {
// out.close();
// }
// return new FileInputStream(file) {
// @Override
// public void close() throws IOException {
// super.close();
// file.delete();
// }
// };
// }
// }
//
// private InputStream readMessageTwoKeys() throws IOException {
// InputStream in = this.socket.getInputStream();
// DataInputStream din = new DataInputStream(in);
// int frameType = din.read();
// if (frameType < 0) {
// return null;
// }
// else if ((frameType & 0x80) == 0x80) {
// long length = 0;
// int b;
// do {
// b = din.read();
// int b_v = b & 0x7F;
// length = length * 128 + b_v;
// } while ((b & 0x80) == 0x80);
// if (length > Integer.MAX_VALUE || length < 0) {
// throw new IOException("read message is too large");
// }
// int intLength = (int) length;
// byte[] bytes = new byte[intLength];
// int bytesReadedLength = din.read(bytes);
// if (bytesReadedLength != bytes.length) {
// throw new EOFException(String.format(
// "read message try to read %d bytes and only has %d bytes",
// intLength, bytesReadedLength));
// }
// if (frameType == 0xFF && length == 0) {
// return null;
// }
// return new String(bytes, "utf-8");
// }
// else {
// ByteArrayOutputStream rawData = new ByteArrayOutputStream();
// int b = din.read();
// while (b != 0xFF) {
// rawData.write(b);
// b = din.read();
// }
// String data = new String(rawData.toByteArray(), "utf-8");
// if (frameType != 0) {
// throw new EOFException("expected frame type equal zero");
// }
// return data;
// }
// }
//
// public static void sendMessageOneKey(OutputStream out, String mess)
// throws IOException {
// byte[] bytes = mess.getBytes("utf-8");
// int frameCount = 0;
// byte[] frame = new byte[10];
// frame[0] = (byte) 129;
// if (bytes.length <= 125) {
// frame[1] = (byte) bytes.length;
// frameCount = 2;
// }
// else if (bytes.length >= 126 && bytes.length <= 65535) {
// int len = bytes.length;
// frame[1] = (byte) 126;
// frame[2] = (byte) ((len >> 8) & (byte) 255);
// frame[3] = (byte) (len & (byte) 255);
// frameCount = 4;
// }
// else {
// long len = bytes.length;
// frame[1] = (byte) 127;
// frame[2] = (byte) ((len >> 56) & 0xFF);
// frame[3] = (byte) ((len >> 22) & 0xFF);
// frame[4] = (byte) ((len >> 40) & 0xFF);
// frame[5] = (byte) ((len >> 32) & 0xFF);
// frame[6] = (byte) ((len >> 24) & 0xFF);
// frame[7] = (byte) ((len >> 16) & 0xFF);
// frame[8] = (byte) ((len >> 8) & 0xFF);
// frame[9] = (byte) (len & (byte) 255);
// frameCount = 10;
// }
// int bLength = frameCount + bytes.length;
// byte[] reply = new byte[bLength];
// int bLim = 0;
// for (int i = 0; i < frameCount; i++) {
// reply[bLim] = frame[i];
// bLim++;
// }
// for (int i = 0; i < bytes.length; i++) {
// reply[bLim] = bytes[i];
// bLim++;
// }
// out.write(reply);
// out.flush();
// }
//
// public static void sendMessageTwoKeys(OutputStream out, String mess)
// throws IOException {
// out.write(0);
// out.write(mess.getBytes("utf-8"));
// out.write(0xFF);
// out.flush();
// }
//
// /**
// * @param in
// * @return char
// * @throws IOException
// * @throws EOFException
// */
// public static int readUtf8(InputStream in) throws IOException, EOFException {
// int c = in.read();
// if (c <= 0x7F) {
// return c;
// }
// else if ((c >> 5) == 0x6) {
// int i2 = in.read();
// if (i2 < 0) {
// throw new EOFException();
// }
// return (((c & 0x1F) << 6) + (i2 & 0x3F));
// }
// else {
// int i2 = in.read();
// if (i2 < 0) {
// throw new EOFException();
// }
// int i3 = in.read();
// if (i3 < 0) {
// throw new EOFException();
// }
// return (((c & 0xF) << 12) + ((i2 & 0x3F) << 6) + (i3 & 0x3F));
// }
// }
//
// /**
// * @param out
// * @param text
// * @throws IOException
// */
// public static void writeUtf8(OutputStream out, String text)
// throws IOException {
// int length = text.length();
// for (int n = 0; n < length; n++) {
// char c = text.charAt(n);
// if (c <= 0x7F) {
// out.write(c);
// }
// else if (c <= 0x7FF) {
// out.write(((c >> 6) & 0x1F) + 0xC0);
// out.write((c & 0x3F) + 0x80);
// }
// else {
// out.write(((c >> 12) & 0xF) + 0xE0);
// out.write(((c >> 6) & 0x3F) + 0x80);
// out.write((c & 0x3F) + 0x80);
// }
// }
// }
//
// }
//
// /**
// * InputStream do WebSocket
// *
// * @author Tecgraf
// */
// protected static class DefaultWebSocketInputStream extends
// WebSocketInputStream {
//
// private static final int MAX = 4;
// /** Ainda lendo */
// private long payload;
// /** Ultima mascara */
// private int[] mask;
//
// /**
// * @param socket
// * @throws IOException
// */
// public DefaultWebSocketInputStream(WebSocket socket) throws IOException {
// super(socket);
// }
//
// /**
// * {@inheritDoc}
// */
// @Override
// protected ByteArrayInputStream readSocket() throws IOException {
// InputStream in = socket.socket.getInputStream();
// if (this.payload > 0) {
// int max = (int) Math.min(MAX, payload);
// byte[] bytes = new byte[max];
// int n = in.read(bytes, 0, max);
// for (int i = 0; i < n; i++) {
// bytes[i] = (byte) (bytes[i] ^ mask[i % 4]);
// }
// this.payload -= n;
// return new ByteArrayInputStream(bytes);
// }
// else {
// DataInputStream din = new DataInputStream(in);
// int byte1 = din.read();
// int byte2 = din.read();
// if (byte1 < 0 || byte2 < 0) {
// return null;
// }
// int opcode = byte1 & 0xF;
// if (opcode == 8) {
// return null;
// }
// boolean isFinal = (byte1 & 0x80) != 0;
// int rsv1 = byte1 & 0x40;
// int rsv2 = byte1 & 0x20;
// int rsv3 = byte1 & 0x10;
// boolean isMask = (byte2 & 0x80) == 1;
// payload = byte2 & 0x7F;
// if (payload == 126) {
// payload = din.readShort();
// }
// else if (payload == 127) {
// payload = din.readLong();
// }
// mask = new int[4];
// for (int n = 0; n < 4; n++) {
// mask[n] = din.read();
// }
// return this.readSocket();
// }
// }
//
// /**
// * {@inheritDoc}
// */
// @Override
// protected boolean finished() throws IOException {
// return payload <= 0;
// }
//
// }
//
// /**
// * InputStream do WebSocket
// *
// * @author Tecgraf
// */
// protected static class DraftWebSocketInputStream extends WebSocketInputStream
// {
//
// private static final int MAX = 4;
// /** Ainda lendo */
// private long payload;
// /** Ultima mascara */
// private int[] mask;
//
// /**
// * @param socket
// * @throws IOException
// */
// public DraftWebSocketInputStream(WebSocket socket) throws IOException {
// super(socket);
// }
//
// /**
// * {@inheritDoc}
// */
// @Override
// protected ByteArrayInputStream readSocket() throws IOException {
// DataInputStream din = new DataInputStream(input);
// int frameType = din.read();
// if (frameType < 0) {
// return null;
// }
// else if ((frameType & 0x80) == 0x80) {
// long length = 0;
// int b;
// do {
// b = din.read();
// int b_v = b & 0x7F;
// length = length * 128 + b_v;
// } while ((b & 0x80) == 0x80);
// if (length > Integer.MAX_VALUE || length < 0) {
// throw new IOException("read message is too large");
// }
// int intLength = (int) length;
// byte[] bytes = new byte[intLength];
// int bytesReadedLength = din.read(bytes);
// if (bytesReadedLength != bytes.length) {
// throw new EOFException(String.format(
// "read message try to read %d bytes and only has %d bytes",
// intLength, bytesReadedLength));
// }
// if (frameType == 0xFF && length == 0) {
// return null;
// }
// return new ByteArrayInputStream(bytes);
// }
// else {
// ByteArrayOutputStream rawData = new ByteArrayOutputStream();
// int b = din.read();
// while (b != 0xFF) {
// rawData.write(b);
// b = din.read();
// }
// if (frameType != 0) {
// throw new EOFException("expected frame type equal zero");
// }
// return new ByteArrayInputStream(rawData.toByteArray());
// }
// }
//
// /**
// * {@inheritDoc}
// */
// @Override
// protected boolean finished() throws IOException {
// return true;
// }
//
// }
//
// /**
// * InputStream do WebSocket
// *
// * @author Tecgraf
// */
// protected static abstract class WebSocketInputStream extends InputStream {
//
// /** Socket */
// protected final WebSocket socket;
// /** Input do Socket */
// protected final InputStream input;
// /** Bytes para serem lidos */
// private ByteArrayInputStream in;
// /** Indica que est� fechado */
// private boolean closed;
//
// /**
// * @param socket
// * @throws IOException
// */
// public WebSocketInputStream(WebSocket socket) throws IOException {
// this.socket = socket;
// this.input = socket.socket.getInputStream();
// }
//
// /**
// * @return bytes
// * @throws IOException
// */
// protected abstract ByteArrayInputStream readSocket() throws IOException;
//
// /**
// * @return bytes
// * @throws IOException
// */
// protected abstract boolean finished() throws IOException;
//
// /**
// * {@inheritDoc}
// */
// @Override
// public int read() throws IOException {
// if (this.closed) {
// return -1;
// }
// if (this.in == null) {
// this.in = readSocket();
// }
// int c = this.in.read();
// if (c < 0 && !this.finished()) {
// this.in = readSocket();
// c = this.in.read();
// }
// if (c < 0) {
// this.in = null;
// return -1;
// }
// return c;
// }
//
// /**
// * {@inheritDoc}
// */
// @Override
// public long skip(long n) throws IOException {
// throw new IOException("not supported yet");
// }
//
// /**
// * {@inheritDoc}
// */
// @Override
// public int available() throws IOException {
// if (this.closed) {
// return 0;
// }
// if (this.in == null) {
// this.in = readSocket();
// }
// return this.in.available();
// }
//
// /**
// * {@inheritDoc}
// */
// @Override
// public void close() throws IOException {
// this.in = null;
// this.closed = true;
// }
//
// /**
// * {@inheritDoc}
// */
// @Override
// public void mark(int readlimit) {
// if (this.closed) {
// return;
// }
// if (this.in == null) {
// try {
// this.in = readSocket();
// }
// catch (IOException e) {
// return;
// }
// }
// this.in.mark(readlimit);
// }
//
// /**
// * {@inheritDoc}
// */
// @Override
// public synchronized void reset() throws IOException {
// this.in = null;
// }
//
// /**
// * {@inheritDoc}
// */
// @Override
// public boolean markSupported() {
// if (this.closed) {
// return false;
// }
// if (this.in == null) {
// try {
// this.in = readSocket();
// }
// catch (IOException e) {
// return false;
// }
// }
// return this.in.markSupported();
// }
//
// }
//
// protected static class DefaultWebSocketOutputStream extends
// WebSocketOutputStream {
//
// /**
// * @param socket
// * @throws IOException
// */
// public DefaultWebSocketOutputStream(WebSocket socket) throws IOException {
// super(socket);
// }
//
// @Override
// public void write(int b) throws IOException {
// this.write(new byte[] { (byte) b });
// }
//
// @Override
// public void write(byte[] bytes) throws IOException {
// int frameCount = 0;
// byte[] frame = new byte[10];
// frame[0] = (byte) 129;
// if (bytes.length <= 125) {
// frame[1] = (byte) bytes.length;
// frameCount = 2;
// }
// else if (bytes.length >= 126 && bytes.length <= 65535) {
// int len = bytes.length;
// frame[1] = (byte) 126;
// frame[2] = (byte) ((len >> 8) & (byte) 255);
// frame[3] = (byte) (len & (byte) 255);
// frameCount = 4;
// }
// else {
// long len = bytes.length;
// frame[1] = (byte) 127;
// frame[2] = (byte) ((len >> 56) & 0xFF);
// frame[3] = (byte) ((len >> 22) & 0xFF);
// frame[4] = (byte) ((len >> 40) & 0xFF);
// frame[5] = (byte) ((len >> 32) & 0xFF);
// frame[6] = (byte) ((len >> 24) & 0xFF);
// frame[7] = (byte) ((len >> 16) & 0xFF);
// frame[8] = (byte) ((len >> 8) & 0xFF);
// frame[9] = (byte) (len & (byte) 255);
// frameCount = 10;
// }
// int bLength = frameCount + bytes.length;
// byte[] reply = new byte[bLength];
// int bLim = 0;
// for (int i = 0; i < frameCount; i++) {
// reply[bLim] = frame[i];
// bLim++;
// }
// for (int i = 0; i < bytes.length; i++) {
// reply[bLim] = bytes[i];
// bLim++;
// }
// out.write(reply);
// out.flush();
// }
//
// /**
// * {@inheritDoc}
// */
// @Override
// public void write(byte[] b, int off, int len) throws IOException {
// this.write(Arrays.copyOfRange(b, off, off + len));
// }
//
// }
//
// protected static class DraftWebSocketOutputStream extends
// WebSocketOutputStream {
//
// /**
// * @param socket
// * @throws IOException
// */
// public DraftWebSocketOutputStream(WebSocket socket) throws IOException {
// super(socket);
// }
//
// @Override
// public void write(int b) throws IOException {
// this.write(new byte[] { (byte) b });
// }
//
// @Override
// public void write(byte[] bytes) throws IOException {
// out.write(0);
// out.write(bytes);
// out.write(0xFF);
// out.flush();
// }
//
// /**
// * {@inheritDoc}
// */
// @Override
// public void write(byte[] b, int off, int len) throws IOException {
// this.write(Arrays.copyOfRange(b, off, off + len));
// }
//
// }
//
// protected static abstract class WebSocketOutputStream extends OutputStream {
//
// /** Socket */
// protected final WebSocket socket;
// /** Sa�da */
// protected final OutputStream out;
//
// /**
// * @param socket
// * @throws IOException
// */
// public WebSocketOutputStream(WebSocket socket) throws IOException {
// this.socket = socket;
// this.out = this.socket.socket.getOutputStream();
// }
//
// /**
// * {@inheritDoc}
// */
// @Override
// public void flush() throws IOException {
// out.flush();
// }
//
// /**
// * {@inheritDoc}
// */
// @Override
// public void close() throws IOException {
// this.flush();
// out.close();
// }
//
// }
//
// public static void main(String[] args) throws Exception {
// WebServerSocket server = new WebServerSocket(8080);
// for (;;) {
// try {
// Socket socket = server.accept();
// try {
// byte[] bytes = new byte[1024];
// int n = socket.getInputStream().read(bytes);
// String msg = new String(bytes, 0, n, "utf-8");
// socket.getOutputStream().write(msg.getBytes("utf-8"));
// socket.close();
// }
// catch (Exception e) {
// e.printStackTrace();
// }
// finally {
// socket.close();
// }
// }
// catch (Exception e) {
// e.printStackTrace();
// }
// }
// }
//
// protected static class SizeableInputStream extends InputStream {
//
// @Override
// public int read() throws IOException {
// // TODO Auto-generated method stub
// return 0;
// }
//
// }
//
// }
//
// // http://www.websocket.org/echo.html
//// http://datatracker.ietf.org/doc/rfc6455/