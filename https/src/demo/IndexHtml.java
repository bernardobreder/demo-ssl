package demo;

import html.file.BootstrapCss;
import html.file.BootstrapCustomCss;
import html.file.BootstrapJs;
import html.file.BootstrapThemeCss;
import html.file.JQueryJs;
import html.file.NormalizeCss;
import httpws.HttpWsClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.TreeMap;

import util.Bytes;
import util.StringUtil;
import util.XmlNode;

public class IndexHtml extends XmlNode {

  private static final String WEBSOCKET_URL = "ws://localhost:5000";

  /**
   * @throws IOException
   */
  public IndexHtml() throws IOException {
    super("html");
    this.addNode(this.createHead());
    this.addNode(this.createBody());
  }

  /**
   * @return xml
   * @throws IOException
   */
  private XmlNode createHead() throws IOException {
    XmlNode node = new XmlNode("head");
    node.addNode(new XmlNode("meta").setAttribute("charset", "utf-8"));
    node.addNode(new XmlNode("meta").setAttribute("http-equiv",
      "X_UA-Compatible").setAttribute("content", "IE=edge"));
    node.addNode(new XmlNode("meta").setAttribute("name", "viewport")
      .setAttribute("content", "width=device-width, initial-scale=1.0"));
    node.addNode(new XmlNode("meta").setAttribute("name", "description")
      .setAttribute("content", ""));
    node.addNode(new XmlNode("meta").setAttribute("name", "author")
      .setAttribute("content", ""));
    node.addNode(new XmlNode("title").setContent("BandeiraBR"));
    node.addNode(new XmlNode("link").setAttribute("rel", "shortcut icon")
      .setAttribute("href", "data:image/png;base64," + getLogoBase64())
      .setAttribute("type", "image/gif"));
    String[] cssStrings =
      new String[] { NormalizeCss.getContent(), BootstrapCss.getContent(),
          BootstrapThemeCss.getContent(), BootstrapCustomCss.getContent() };
    for (String cssString : cssStrings) {
      node.addNode(new XmlNode("style").setContent(cssString).setAttribute(
        "type", "text/css"));
    }
    String[] jsStrings =
      new String[] { JQueryJs.getContent(), BootstrapJs.getContent(),
          new DemoApplicationJs(WEBSOCKET_URL).toString() };
    for (String jsString : jsStrings) {
      node.addNode(new XmlNode("script").setContent(jsString).setAttribute(
        "type", "text/javascript"));
    }
    String[] jsPaths = new String[] {};
    for (String jsPath : jsPaths) {
      node.addNode(new XmlNode("script").setContent(getResource(jsPath)))
        .setAttribute("type", "text/javascript");
    }
    return node;
  }

  private String getLogoBase64() throws IOException {
    return Bytes.toBase64(getResource("img/logo_bandeira32.gif"));
  }

  private InputStream getResource(String path) {
    return this.getClass().getResourceAsStream("/webapp/" + path);
  }

  private XmlNode createBody() {
    XmlNode body = new XmlNode("body");
    body
      .setAttribute(
        "onpopstate",
        "if ($WS.websocket.readyState == WebSocket.OPEN) { "
          + "$WS.websocket.send('method:page\\r\\nurl:' + window.location.pathname + window.location.search + '\\r\\n\\r\\n'); "
          + "}");
    return body;
  }

  public static String HttpWsClientInitJs(String wsHost, String session)
    throws IOException {
    InputStream js =
      Bytes.getResource(HttpWsClient.class, "HttpWsClientInit.js");
    TreeMap<String, Object> map = new TreeMap<String, Object>();
    map.put("wsHost", wsHost);
    map.put("session", session);
    String content = StringUtil.toString(js, map);
    return content;
  }

}
