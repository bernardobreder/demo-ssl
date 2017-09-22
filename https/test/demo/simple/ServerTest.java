// package demo.simple;
//
// import http.HttpRequest;
// import http.HttpResponse;
// import http.HttpServerSocket;
//
// import java.io.IOException;
//
// import org.junit.After;
// import org.junit.Before;
// import org.junit.Test;
//
// import socket.IServerSocket;
// import socket.mock.MockServerSocket;
// import ws.standard.WebServerSocket;
// import app.AppServer;
// import app.ServletException;
// import browser.mock.MockBrowser;
//
// /**
// * Testador de uma aplicação simples
// *
// * @author bernardobreder
// */
// public class ServerTest {
//
// /** Porta */
// private static final int HTTP_PORT = 8080;
// /** Porta */
// private static final int WEBSOCKET_PORT = 9090;
// /** Servidor */
// private AppServer server;
// /** Navegador */
// private MockBrowser client;
//
// /**
// * @throws IOException
// */
// @Before
// public void before() throws IOException {
// HttpServerSocket httpServer =
// new HttpServerSocket(new MockServerSocket(HTTP_PORT));
// WebServerSocket websocketServer =
// new WebServerSocket(new MockServerSocket(WEBSOCKET_PORT));
// this.server = new MyAppServer(httpServer, websocketServer);
// this.client = new MockBrowser(HTTP_PORT);
// }
//
// /**
// * @throws IOException
// */
// @After
// public void after() throws IOException {
// this.client.close();
// this.server.close();
// }
//
// /**
// * @throws IOException
// */
// @Test
// public void testTitle() throws IOException {
// client.sendOpen("/");
// server.readHttpSocket();
// client.readOpen();
// client.requireTitle("Index Title");
// }
//
// /**
// * @throws IOException
// */
// @Test
// public void testGotoAbout() throws IOException {
// client.sendOpen("/");
// server.readHttpSocket();
// client.readOpen();
// client.requireTitle("Index Title");
// // client.click("//a[@id='menu-about']");
// server.readHttpSocket();
// client.readOpen();
// }
//
// /**
// * Aplicação de teste
// *
// * @author bernardobreder
// */
// public static class MyAppServer extends AppServer {
//
// /**
// * @param httpServer
// * @param websocketServer
// */
// public MyAppServer(IServerSocket httpServer, IServerSocket websocketServer) {
// super(httpServer, websocketServer);
// }
//
// /**
// * {@inheritDoc}
// */
// @Override
// public void servlet(HttpRequest req, HttpResponse resp) throws IOException,
// ServletException {
// String url = req.getUrl();
// if (url.equals("/")) {
// resp.writeInputStream(ServerTest.class
// .getResourceAsStream("/demo/simple/index.html"));
// }
// else if (url.equals("/about")) {
// resp.writeInputStream(ServerTest.class
// .getResourceAsStream("/demo/simple/about.html"));
// }
// }
// }
//
// }
