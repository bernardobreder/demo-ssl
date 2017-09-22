// package http.standard;
//
// import http.HttpServerSocket;
// import http.HttpSocket;
//
// import java.io.IOException;
// import java.net.Socket;
// import java.security.KeyManagementException;
// import java.security.KeyStore;
// import java.security.KeyStoreException;
// import java.security.NoSuchAlgorithmException;
// import java.security.UnrecoverableKeyException;
// import java.security.cert.CertificateException;
// import java.util.ArrayList;
// import java.util.List;
//
// import javax.net.ssl.SSLServerSocket;
//
// import ssl.StandardSSLServerSocket;
//
// /**
// *
// *
// * @author Tecgraf/PUC-Rio
// */
// public class StandardHttpsServerSocket implements HttpServerSocket {
//
// /** Server */
// private final SSLServerSocket server;
// /** Listeners */
// private List<HttpServerSocketListener> listeners = new
// ArrayList<HttpServerSocketListener>();
//
// /**
// * @param port
// * @param keyStore
// * @param keyPass
// * @throws IOException
// * @throws NoSuchAlgorithmException
// * @throws UnrecoverableKeyException
// * @throws KeyStoreException
// * @throws KeyManagementException
// * @throws CertificateException
// */
// public StandardHttpsServerSocket(int port, KeyStore keyStore, char[] keyPass)
// throws IOException, NoSuchAlgorithmException,
// UnrecoverableKeyException, KeyStoreException,
// KeyManagementException, CertificateException {
// this.server = StandardSSLServerSocket.create(port, keyStore, keyPass);
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
// /**
// * Dispara evento
// *
// * @param e
// */
// protected void fireError(Throwable e) {
// if (!this.listeners.isEmpty()) {
// for (HttpServerSocketListener listener : this.listeners) {
// listener.error(e);
// }
// }
// }
//
// /**
// * Dispara evento
// *
// * @param socket
// */
// protected void fireAcceptSocket(HttpSocket socket) {
// if (!this.listeners.isEmpty()) {
// for (HttpServerSocketListener listener : this.listeners) {
// listener.acceptHttpSocket(socket);
// }
// }
// }
//
// /**
// * Dispara evento
// */
// protected void fireCloseServer() {
// if (!this.listeners.isEmpty()) {
// for (HttpServerSocketListener listener : this.listeners) {
// listener.closeServer();
// }
// }
// }
//
// /**
// * {@inheritDoc}
// */
// @Override
// public void addListener(HttpServerSocketListener listener) {
// this.listeners.add(listener);
// }
//
// /**
// * {@inheritDoc}
// */
// @Override
// public boolean removeListener(HttpServerSocketListener listener) {
// return this.listeners.remove(listener);
// }
//
// /**
// * {@inheritDoc}
// */
// @Override
// public void clearListeners() {
// this.listeners.clear();
// }
//
//}
