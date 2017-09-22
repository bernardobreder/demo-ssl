// package demo;
//
// import http.HttpRequest;
// import http.HttpResponse;
// import http.HttpServerSocket;
//
// import java.io.FileNotFoundException;
// import java.io.IOException;
// import java.io.InputStream;
// import java.security.KeyStore;
//
// import socket.jdk.StandardServerSocket;
// import socket.monitor.MonitorServerSocket;
// import ssl.StandardSSLServerSocket;
// import ws.standard.WebServerSocket;
//
// /**
// *
// *
// * @author bernardobreder
// */
// public class Main {
//
// /**
// * @param args
// * @throws Exception
// */
// public static void main(String[] args) throws Exception {
// InputStream keystoreInput = Main.class.getResourceAsStream("/lig.pem");
// if (keystoreInput == null) {
// throw new FileNotFoundException("classpath:/lig.pem");
// }
// char[] keystorepass =
// new char[] { 's', 'i', 'm', 'u', 'l', 'a', 't', 'o', 'r' };
// KeyStore keyStore =
// StandardSSLServerSocket.getKeyStore(keystoreInput, keystorepass);
// // SslServerSocket httpServerSocket = new SslServerSocket(
// // StandardSSLServerSocket.create(8080, keyStore, keystorepass));
// HttpServerSocket httpsServer =
// new HttpServerSocket(new MonitorServerSocket("http",
// new StandardServerSocket(8080)));
// WebServerSocket webServer =
// new WebServerSocket(new MonitorServerSocket("ws",
// new StandardServerSocket(9090)));
// new DemoServer() {
// @Override
// public boolean login(String username, String password) throws IOException {
// return username.equals("bbreder") && password.equals("1234");
// }
//
// @Override
// public void https(HttpRequest req, HttpResponse resp) throws IOException {
// resp.setContentType("text/html");
// resp.answerSuccess();
// resp.writeString("<!DOCTYPE html>");
// resp.writeBytes(new IndexHtml().getBytes());
// }
// }.start(httpsServer, webServer);
// System.out.println("Server started");
// }
// }
