package jtweb;

import http.HttpRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import javax.script.ScriptException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import js.Js;
import junit.framework.Assert;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import socket.mock.MockSocket;
import socket.mock.MockSocket.MockSocketInputStream;
import socket.mock.MockSocket.MockSocketOutputStream;
import util.Bytes;

public class AppClient implements IAppClient {

  /** Indica se o navegador está fechado */
  private boolean closed;
  /** Usuário */
  private String username;
  /** Senha */
  private String password;
  /** Porta do servidor http */
  private int httpPort;
  /** Páginas do navegador */
  private LinkedList<Page> pages = new LinkedList<Page>();

  /**
   * Construtor
   * 
   * @param httpPort
   */
  public AppClient(int httpPort) {
    this.httpPort = httpPort;
  }

  /**
   * @param url
   * @return socket
   * @throws IOException
   */
  @Override
  public IAppClient open(String url) {
    this.checkClosed();
    MockSocket socket = new MockSocket(httpPort);
    writeHttpRequest(socket, url);
    socket.getOutputStream().flush();
    try {
      MockSocketInputStream in = socket.getInputStream();
      HttpRequest request = new HttpRequest(in);
      InputStream input = request.getInput();
      this.open(request, input);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    socket.close();
    return this;
  }

  /**
   * @param request
   * @param in
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws XPathExpressionException
   */
  public void open(HttpRequest request, InputStream in) throws SAXException,
    ParserConfigurationException, IOException, XPathExpressionException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(in);
    Page page = new Page(doc);
    XPathFactory xPathfactory = XPathFactory.newInstance();
    XPath xpath = xPathfactory.newXPath();
    XPathExpression expr = xpath.compile("/html/head/script");
    NodeList scriptList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
    for (int n = 0; n < scriptList.getLength(); n++) {
      Node scriptNode = scriptList.item(n);
      String text = scriptNode.getTextContent();
      text = text.replace("&lt;", "<");
      text = text.replace("&gt;", ">");
      text = text.replace("&amp;", "&");
      try {
        page.js.eval(text);
        page.js.eval("$('html','head','title')");
        page.js.eval("$('html','head','title').text()");
      }
      catch (ScriptException e) {
        e.printStackTrace();
      }
    }
    this.pages.push(page);
  }

  /**
   * Retorna a página atual
   * 
   * @return pagina
   */
  protected Page getPage() {
    return this.pages.isEmpty() ? null : this.pages.peek();
  }

  /**
   * Realiza a ação de voltar para a página anterior
   * 
   * @return this
   */
  public IAppClient back() {
    if (!this.pages.isEmpty()) {
      this.pages.pop();
    }
    return this;
  }

  /**
   * @param expectedTitle
   * @return this
   */
  @Override
  public IAppClient requireTitle(String expectedTitle) {
    String value = xpathToString("/html/head/title");
    if (value == null) {
      Assert.fail("not found the tag title at: html > head > title");
    }
    Assert.assertEquals(expectedTitle, value);
    return this;
  }

  public void click(String xpath) {
    Node node = xpathToNode(xpath);
  }

  /**
   * @param expression
   * @return texto
   */
  public String xpathToString(String expression) {
    try {
      Document doc = this.getPage().document;
      XPathFactory xPathfactory = XPathFactory.newInstance();
      XPath xpath = xPathfactory.newXPath();
      XPathExpression expr = xpath.compile(expression);
      if (!(Boolean) expr.evaluate(doc, XPathConstants.BOOLEAN)) {
        Assert.fail("not found xpath: " + expression);
      }
      return (String) expr.evaluate(doc, XPathConstants.STRING);
    }
    catch (XPathExpressionException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * @param expression
   * @return texto
   */
  public Node xpathToNode(String expression) {
    try {
      Document doc = this.getPage().document;
      XPathFactory xPathfactory = XPathFactory.newInstance();
      XPath xpath = xPathfactory.newXPath();
      XPathExpression expr = xpath.compile(expression);
      if (!(Boolean) expr.evaluate(doc, XPathConstants.BOOLEAN)) {
        Assert.fail("not found xpath: " + expression);
      }
      return (Node) expr.evaluate(doc, XPathConstants.NODE);
    }
    catch (XPathExpressionException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * @param username
   * @param password
   * @return this
   */
  public IAppClient setAuthorization(String username, String password) {
    this.username = username;
    this.password = password;
    return this;
  }

  /**
   * Fecha o navegador
   */
  @Override
  public void close() {
    this.closed = true;
    this.username = null;
    this.password = null;
    this.httpPort = -1;
    this.pages = null;
  }

  protected void writeHttpRequest(MockSocket socket, String path) {
    MockSocketOutputStream out = socket.getOutputStream();
    out.write(("GET " + path + " HTTP/1.1\r\n").getBytes());
    out.write(("Host: " + "localhost:test" + "\r\n").getBytes());
    out.write(("Test: " + "true" + "\r\n").getBytes());
    out.write(("Connection: " + "keep-alive" + "\r\n").getBytes());
    if (this.username != null && this.password != null) {
      out
        .write(Bytes
          .getUtf8Bytes(("Authorization: "
            + Bytes.toBase64(Bytes.getUtf8Bytes(username + ":" + password)) + "\r\n")));
    }
    out.write("\r\n".getBytes());
  }

  protected IAppClient checkClosed() {
    if (closed) {
      throw new IllegalStateException("browser closed");
    }
    return this;
  }

  protected static class Page {

    protected Document document;

    protected Js js;

    public Page(Document doc) {
      super();
      this.document = doc;
      this.js = new Js(document);
    }

  }

  @Override
  public IAppClient start() {
    return this;
  }

}
