package jtecweb;

import http.HttpRequest;
import http.HttpResponse;
import http.HttpSocket;

import java.io.IOException;
import java.io.InputStream;
import java.util.TreeMap;

import jtecweb.httpws.HttpWsClient;
import jtecweb.httpws.HttpWsServer;
import socket.jdk.StandardServerSocket;
import util.Bytes;
import util.StringUtil;
import util.XmlNode;
import ws.IWsSocket;
import ws.WsRequest;
import ws.WsResponse;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public class Main {

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    int httpPort = 8080;
    int wsPort = 5000;
    StandardServerSocket httpServerSocket = new StandardServerSocket(httpPort);
    StandardServerSocket wsServerSocket = new StandardServerSocket(wsPort);
    new HttpWsServer(httpServerSocket, wsServerSocket) {
      @Override
      public IHttpWsClient createHttpWsClient(HttpSocket socket)
        throws IOException {
        return new MyApp();
      }

      @Override
      public IHttpWsClient createHttpWsClient(IWsSocket socket)
        throws IOException {
        return new MyApp();
      }
    }.start();
  }

  public static class MyApp extends HttpWsClient {

    public MyApp() throws IOException {
      super();
    }

    @Override
    public void http(HttpRequest req, HttpResponse resp) throws IOException {
      String url = req.getLastUrl();
      if (!url.contains(".")) {
        String html5Tag = "<!DOCTYPE html>";
        XmlNode headElem = new XmlNode("head");
        headElem.addNode(new XmlNode("meta").setAttribute("charset", "utf-8"));
        InputStream js =
          Bytes.getResource(HttpWsClient.class, "HttpWsClientInit.js");
        TreeMap<String, Object> map = new TreeMap<String, Object>();
        map.put("wsHost", "ws://localhost:5000");
        map.put("session", this.getSession());
        headElem.addNode(new XmlNode("script").setContent(StringUtil.toString(
          js, map)));
        XmlNode bodyElem = new XmlNode("body");
        XmlNode htmlElem =
          new XmlNode("html").addNode(headElem).addNode(bodyElem);
        byte[] bytes = htmlElem.getBytes();
        resp.setContentType("text/html;charset=utf-8");
        resp.setContentLength(html5Tag.length() + bytes.length);
        resp.answerSuccess();
        resp.writeString(html5Tag);
        resp.writeBytes(bytes);
      }
      else {
        resp.answerNotFound();
      }
    }

    @Override
    public void websocket(WsRequest req, WsResponse resp) throws IOException {
      resp.write("alert(1);");
    }

  }

}
