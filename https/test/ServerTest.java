import html.HElement;
import html.primitive.HText;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpServerSocket;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import socket.mock.MockServerSocket;
import util.XmlNode;
import websocket.standard.StandardWebServerSocket;
import ws.WebServerSocket;
import app.AbstractApplicationServer;
import app.ServletException;
import browser.mock.MockBrowser;

/**
 * Testador de uma aplicação simples
 *
 * @author Tecgraf/PUC-Rio
 */
public class ServerTest {

  /** Porta */
  private static final int HTTP_PORT = 8080;
  /** Porta */
  private static final int WEBSOCKET_PORT = 9090;
  /** Servidor */
  private AbstractApplicationServer app;
  /** Navegador */
  private MockBrowser browser;

  /**
   * @throws IOException
   */
  @Before
  public void before() throws IOException {
    HttpServerSocket httpServer =
      new HttpServerSocket(new MockServerSocket(HTTP_PORT));
    StandardWebServerSocket websocketServer =
      new StandardWebServerSocket(new MockServerSocket(WEBSOCKET_PORT));
    this.app = new MyAppServer(httpServer, websocketServer).start();
    this.browser = new MockBrowser(HTTP_PORT);
  }

  /**
   * @throws IOException
   */
  @After
  public void after() throws IOException {
    this.browser.close();
    this.app.close();
  }

  /**
   * @throws IOException
   */
  @Test
  public void testTitle() throws IOException {
    browser.open("/");
    browser.requireTitle("Hello Title");
  }

  /**
   * @throws IOException
   */
  @Test
  public void testTitle1() throws IOException {
    browser.open("/");
    browser.requireTitle("Hello Title");
  }

  /**
   * Aplicação de teste
   *
   * @author Tecgraf/PUC-Rio
   */
  public static class MyAppServer extends AbstractApplicationServer {

    /**
     * @param httpServer
     * @param websocketServer
     */
    public MyAppServer(HttpServerSocket httpServer,
      WebServerSocket websocketServer) {
      super(httpServer, websocketServer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void servlet(HttpRequest req, HttpResponse resp) throws IOException,
    ServletException {
      if (req.getUrl().equals("/")) {
        resp.writeXmlNode(new XmlNode("html").addNode(new XmlNode("head")
        .addNode(new XmlNode("title").setContent("Hello Title"))));
      }
    }
  }

  /**
   *
   *
   * @author Tecgraf/PUC-Rio
   */
  public static class HomePage extends HElement {

    /**
     *
     */
    public HomePage() {
      super("html");
      HElement headTag =
        new HElement("head").addElement(new HElement("title")
        .addElement(new HText("Hello Title")));
      this.addElement(headTag);
    }

  }

}
