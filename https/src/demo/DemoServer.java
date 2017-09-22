// package demo;
//
// import http.HttpRequest;
// import http.HttpResponse;
// import http.HttpServerSocket;
// import http.HttpSocket;
//
// import java.io.IOException;
// import java.io.InputStream;
//
// import ws.standard.WebServerSocket;
// import demo.bandeirabr.SupplyApp;
//
// /**
// *
// *
// * @author bernardobreder
// */
// public abstract class DemoServer {
//
// /**
// * @param username
// * @param password
// * @return autentico
// * @throws IOException
// */
// public abstract boolean login(String username, String password) throws
// IOException;
//
// /**
// * @param req
// * @param resp
// * @throws IOException
// */
// public abstract void https(HttpRequest req, HttpResponse resp) throws
// IOException;
//
// /**
// * @param server
// * @throws IOException
// */
// public void runHttps(HttpServerSocket server) throws IOException {
// try {
// for (;;) {
// try {
// HttpSocket httpSocket = server.accept();
// HttpRequest request = httpSocket.getRequest();
// HttpResponse response = httpSocket.getResponse();
// try {
// if (!request.hasAuthorization() || !this.login(request.getUsername(),
// request.getPassword())) {
// response.answerNotAuthorized();
// continue;
// }
// String url = request.getUrl();
// if (url.contains("..")) {
// response.answerBadRequest();
// continue;
// }
// while (url.startsWith("/")) {
// url = url.substring(1);
// }
// this.https(request, response);
// if (!response.isAnswered()) {
// InputStream in = getResource(url);
// if (in == null) {
// response.answerNotFound();
// continue;
// }
// response.answerSuccess();
// try {
// byte[] bytes = new byte[8 * 1024];
// for (int n; (n = in.read(bytes)) != -1;) {
// response.writeBytes(bytes, 0, n);
// }
// } finally {
// in.close();
// }
// }
// response.flush();
// } finally {
// httpSocket.close();
// }
// } catch (Throwable e) {
// e.printStackTrace();
// }
// }
// } finally {
// server.close();
// }
// }
//
// /**
// * @param url
// * @return input stream
// */
// protected static InputStream getResource(String url) {
// if (url.contains("..")) {
// return null;
// }
// return url.getClass().getResourceAsStream("/webapp/" + url);
// }
//
// /**
// * @param httpsServer
// * @param webServer
// * @throws Exception
// */
// public void start(final HttpServerSocket httpsServer, final WebServerSocket
// webServer) throws Exception {
// new Thread(new Runnable() {
// @Override
// public void run() {
// try {
// runHttps(httpsServer);
// } catch (IOException e) {
// e.printStackTrace();
// }
// }
// }, "Https Server").start();
// new Thread(new Runnable() {
// @Override
// public void run() {
// try {
// SupplyApp.main(webServer);
// } catch (IOException e) {
// e.printStackTrace();
// }
// }
// }, "WebSocket Server").start();
// }
//
// }
