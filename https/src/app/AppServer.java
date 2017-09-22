package app;

import html.HElement;
import html.IBrowser;
import html.listener.HMouseEvent;
import html.listener.HMouseListener;
import html.primitive.HText;
import http.HttpRequest;
import http.HttpResponse;

import java.io.IOException;
import java.util.TreeMap;

import jtecweb.IHttpWsClient;
import jtecweb.IHttpWsServer;
import jtecweb.httpws.HttpWsClient;
import jtecweb.httpws.HttpWsServer;
import socket.IServerSocket;
import util.Bytes;
import util.StringUtil;
import ws.WsRequest;
import ws.WsResponse;
import demo.bandeirabr.desktop.DesktopFrame;
import demo.server.MyAppServer;

/**
 *
 *
 * @author bernardobreder
 */
public abstract class AppServer implements IAppServer {

  /** Http Server */
  private final IHttpWsServer server;
  /** Navegador */
  private IBrowser browser;

  /**
   * @param httpServer
   * @param websocketServer
   * @param browser
   */
  public AppServer(IServerSocket httpServer, IServerSocket websocketServer,
    IBrowser browser) {
    this.server = new MyHttpWsServer(httpServer, websocketServer);
    this.browser = browser;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public abstract void servlet(HttpRequest req, HttpResponse resp)
    throws IOException, ServletException;

  /**
   * @param req
   * @param resp
   */
  public void websocket(WsRequest req, WsResponse resp) {
    IBrowser.browers.set(browser);
    try {
      try {
        try {
          String method = req.getHeader("method");
          if (method != null) {
            if (method.equals("start")) {
              TreeMap<String, Object> map = new TreeMap<String, Object>();
              resp.write(StringUtil.toString(Bytes.getResource(
                MyAppServer.class, "RichHttpWsServerInit.js"), map));
            }
            else if (method.equals("page")) {
              String url = req.getHeader("url");
              if (url != null && url.equals("/")) {
                browser.setRoot(new DesktopFrame());
              }
              else {
                browser.setRoot(new HText("Hello" + url));
              }
            }
            else if (method.equals("action")) {
              String targetAttr = req.getHeader("target");
              HElement element =
                browser.getElement(Integer.parseInt(targetAttr));
              String eventAttr = req.getHeader("event");
              if (eventAttr.equals("click")) {
                int x = Integer.parseInt(req.getHeader("x"));
                int y = Integer.parseInt(req.getHeader("y"));
                HMouseListener listener =
                  element.getListener(eventAttr, HMouseListener.class);
                if (listener != null) {
                  HMouseEvent event = new HMouseEvent(element, x, y);
                  listener.action(event);
                }
              }
            }
          }
        }
        catch (Throwable e) {
          e.printStackTrace();
        }
      }
      finally {
        browser.fireChanged();
      }
    }
    finally {
      IBrowser.browers.remove();
    }
    String changes = browser.consumeChanges();
    if (changes != null) {
      resp.write(changes);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isClosed() {
    return this.server.isClosed();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IAppServer start() {
    this.server.start();
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() {
    this.server.close();
  }

  /**
   *
   *
   * @author Tecgraf
   */
  public class MyHttpWsClient extends HttpWsClient {

    /**
     * {@inheritDoc}
     */
    @Override
    public void websocket(WsRequest req, WsResponse resp) throws IOException {
      AppServer.this.websocket(req, resp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void http(HttpRequest req, HttpResponse resp) throws IOException {
      servlet(req, resp);
    }

  }

  /**
   *
   *
   * @author Tecgraf
   */
  public class MyHttpWsServer extends HttpWsServer {

    /**
     * @param httpServerSocket
     * @param wsServerSocket
     */
    public MyHttpWsServer(IServerSocket httpServerSocket,
      IServerSocket wsServerSocket) {
      super(httpServerSocket, wsServerSocket);
    }

    //    /**
    //     * {@inheritDoc}
    //     */
    //    @Override
    //    public void http(HttpRequest req, HttpResponse resp) throws IOException {
    //      servlet(req, resp);
    //    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IHttpWsClient create() throws IOException {
      return new MyHttpWsClient();
    }

  }

}
