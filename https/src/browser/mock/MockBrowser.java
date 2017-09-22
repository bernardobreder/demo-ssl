package browser.mock;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import junit.framework.Assert;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import socket.Socket;
import socket.mock.MockSocket;
import util.Bytes;

/**
 * Navegador que simula um real
 *
 * @author Tecgraf/PUC-Rio
 */
public class MockBrowser {

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
  public MockBrowser(int httpPort) {
    this.httpPort = httpPort;
  }

  /**
   * @param path
   * @return socket
   * @throws IOException
   */
  public MockBrowser open(final String path) throws IOException {
    this.checkClosed();
    Socket socket = createSocket("localhost", httpPort);
    writeHttpRequest(socket, path);
    socket.getOutputStream().flush();
    try {
      InputStream in = socket.getInputStream();
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(in);
      //      HtmlDocument document = new HtmlDocument(in);
      //      XmlNode node = document.getDocumentNode();
      //      node.setAttribute("url", path);
      this.pages.push(new Page(doc));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    socket.close();
    return this;
  }

  /**
   * Retorna a página atual
   *
   * @return pagina
   */
  public Page getPage() {
    return this.pages.isEmpty() ? null : this.pages.peek();
  }

  /**
   * Realiza a ação de voltar para a página anterior
   *
   * @return this
   */
  public MockBrowser back() {
    if (!this.pages.isEmpty()) {
      this.pages.pop();
    }
    return this;
  }

  /**
   * @param expectedTitle
   * @return this
   */
  public MockBrowser requireTitle(String expectedTitle) {
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
  public MockBrowser setAuthorization(String username, String password) {
    this.username = username;
    this.password = password;
    return this;
  }

  /**
   * Fecha o navegador
   */
  public void close() {
    this.closed = true;
    this.username = null;
    this.password = null;
    this.httpPort = -1;
    this.pages = null;
  }

  protected void writeHttpRequest(Socket socket, String path)
    throws IOException, UnsupportedEncodingException {
    OutputStream out = socket.getOutputStream();
    out.write(("GET " + path + " HTTP/1.1\r\n").getBytes());
    out.write(("Host: " + "localhost:test" + "\r\n").getBytes());
    out.write(("Test: " + "true" + "\r\n").getBytes());
    out.write(("Connection: " + "keep-alive" + "\r\n").getBytes());
    if (this.username != null && this.password != null) {
      out
      .write(("Authorization: "
        + Bytes.toBase64(Bytes.getUtf8Bytes(username + ":" + password)) + "\r\n")
        .getBytes("utf-8"));
    }
    out.write("\r\n".getBytes());
  }

  protected Socket createSocket(String host, int port) {
    return new MockSocket(port);
  }

  protected MockBrowser checkClosed() {
    if (closed) {
      throw new IllegalStateException("browser closed");
    }
    return this;
  }

  protected static class Page {

    protected Document document;

    public Page(Document document) {
      super();
      this.document = document;
    }

  }

}
