package demo.server;

import html.file.BootstrapCss;
import html.file.BootstrapCustomCss;
import html.file.BootstrapJs;
import html.file.BootstrapThemeCss;
import html.file.JQueryJs;
import html.javascript.JqueryJavaScript;
import http.HttpRequest;
import httpws.HttpWsServer;

import java.io.IOException;

import jtecweb.IHttpWsClient;
import socket.IServerSocket;
import socket.jdk.StandardServerSocket;
import util.Bytes;
import util.XmlNode;
import ws.IWsSocket;
import browser.standard.Browser;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public class MyAppServer extends HttpWsServer {

  /**
   * @param httpServer
   * @param websocketServer
   * @param hostPort
   */
  public MyAppServer(IServerSocket httpServer, IServerSocket websocketServer,
    String hostPort) {
    super(httpServer, websocketServer, hostPort);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IHttpWsClient create(final IWsSocket socket, String session) {
    return new Browser(socket, session, new JqueryJavaScript());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected XmlNode httpNode(HttpRequest req) throws IOException {
    XmlNode htmlNode = super.httpNode(req);
    XmlNode headNode = htmlNode.getNodeByTagName("head");
    XmlNode bodyNode = htmlNode.getNodeByTagName("body");
    configHead(headNode);
    configBody(bodyNode);
    return htmlNode;
  }

  protected void configBody(XmlNode bodyNode) {
    bodyNode.setAttribute("onpopstate", "$WS.onWebSocketPopState()");
  }

  protected void configHead(XmlNode headNode) throws IOException {
    headNode.addNode(new XmlNode("title").setContent("BandeiraBR"));
    headNode.addNode(new XmlNode("meta").setAttribute("charset", "utf-8"));
    headNode.addNode(new XmlNode("meta").setAttribute("http-equiv",
      "X_UA-Compatible").setAttribute("content", "IE=edge"));
    headNode.addNode(new XmlNode("meta").setAttribute("name", "viewport")
      .setAttribute("content", "width=device-width, initial-scale=1.0"));
    headNode.addNode(new XmlNode("meta").setAttribute("name", "description")
      .setAttribute("content", ""));
    headNode.addNode(new XmlNode("meta").setAttribute("name", "author")
      .setAttribute("content", ""));
    headNode.addNode(new XmlNode("link").setAttribute("rel", "shortcut icon")
      .setAttribute(
        "href",
        "data:image/gif;base64,"
          + Bytes.toBase64(this.getClass().getResourceAsStream(
            "/webapp/img/logo_bandeira32.gif"))).setAttribute("type",
              "image/gif"));
    headNode.addNode(new XmlNode("script").setContent(
      Bytes.getResource(MyAppServer.class, "MyAppServer.js")).setAttribute(
        "type", "text/javascript"));
    headNode.addNode(new XmlNode("script").setContent(JQueryJs.getContent())
      .setAttribute("type", "text/javascript"));
    headNode.addNode(new XmlNode("script").setContent(BootstrapJs.getContent())
      .setAttribute("type", "text/javascript"));
    headNode.addNode(new XmlNode("style").setContent(BootstrapCss.getContent())
      .setAttribute("type", "text/css"));
    headNode.addNode(new XmlNode("style").setContent(
      BootstrapCustomCss.getContent()).setAttribute("type", "text/css"));
    headNode.addNode(new XmlNode("style").setContent(
      BootstrapThemeCss.getContent()).setAttribute("type", "text/css"));
  }

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    int httpPort = 8080;
    int wsPort = 9090;
    StandardServerSocket httpServerSocket = new StandardServerSocket(httpPort);
    StandardServerSocket wsServerSocket = new StandardServerSocket(wsPort);
    new MyAppServer(httpServerSocket, wsServerSocket,
      "ws://breder.no-ip.org:9090").start();
  }

}
