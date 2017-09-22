// package jtweb;
//
// import html.HElement;
// import html.IBrowser;
// import html.javascript.JqueryJavaScript;
// import html.primitive.HText;
//
// import java.io.IOException;
// import java.util.Map;
//
// import jtweb.js.ScriptJs;
//
// import org.junit.After;
// import org.junit.Before;
// import org.junit.Test;
//
// import socket.IServerSocket;
// import socket.mock.MockServerSocket;
// import util.XmlNode;
// import browser.standard.Browser;
//
// public class ServerTest {
//
// private IAppServer server;
//
// private IAppClient client;
//
// @Test
// public void testTitle() {
// client.open("/");
// client.requireTitle("BandeiraBR");
// }
//
// @Before
// public void before() {
// IServerSocket httpServer = new MockServerSocket(8080);
// IServerSocket wsServer = new MockServerSocket(5000);
// server = new MyAppServer(httpServer, wsServer, new Browser(new
// JqueryJavaScript()));
// server.start();
// client = new AppClient(8080);
// client.start();
// }
//
// @After
// public void after() {
// client.close();
// server.close();
// }
//
// public static class MyAppServer extends AbstractAppServer {
//
// public MyAppServer(IServerSocket httpServer, IServerSocket wsServer, IBrowser
// browser) {
// super(httpServer, wsServer, browser);
// }
//
// @Override
// public XmlNode html() throws IOException {
// XmlNode htmlElem = new XmlNode("html");
// XmlNode headElem = new XmlNode("head");
// {
// headElem.addNode(new XmlNode("title").setContent("BandeiraBR"));
// headElem.addNode(new XmlNode("script").setContent(ScriptJs.getDom()));
// }
// XmlNode bodyElem = new XmlNode("body");
// {
//
// }
// htmlElem.addNode(headElem).addNode(bodyElem);
// return htmlElem;
// }
//
// @Override
// public HElement page(String url, Map<String, Object> header) {
// return new HElement("div").addElement(new HText("Hello"));
// }
// }
//
// }
