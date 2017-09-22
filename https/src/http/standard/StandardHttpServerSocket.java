// package http.standard;
//
// import http.HttpSocket;
// import http.base.AbstractHttpServerSocket;
//
// import java.io.IOException;
// import java.net.ServerSocket;
// import java.net.Socket;
//
// /**
// *
// *
// * @author Tecgraf/PUC-Rio
// */
// public class StandardHttpServerSocket extends AbstractHttpServerSocket {
//
// /** Server */
// private final ServerSocket server;
//
// /**
// * @param port
// * @throws IOException
// */
// public StandardHttpServerSocket(int port) throws IOException {
// this.server = new ServerSocket(port);
// }
//
// /**
// * {@inheritDoc}
// */
// @Override
// public HttpSocket accept() throws IOException {
// for (;;) {
// Socket socket = this.server.accept();
// try {
// HttpSocket httpSocket = new StandardHttpSocket(socket);
// httpSocket.readRequest();
// this.fireAcceptSocket(httpSocket);
// return httpSocket;
// } catch (Throwable e) {
// this.fireError(e);
// }
// }
// }
//
// /**
// * {@inheritDoc}
// */
// @Override
// public void close() throws IOException {
// try {
// this.server.close();
// } finally {
// this.fireCloseServer();
// }
// }
//
// }
//// http://www.w3.org/Protocols/rfc2616/rfc2616.html