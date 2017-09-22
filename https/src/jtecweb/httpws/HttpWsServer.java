package jtecweb.httpws;

import http.HttpRequest;
import http.HttpResponse;
import http.HttpSocket;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import jtecweb.IHttpClient;
import jtecweb.IHttpServer;
import jtecweb.IHttpWsClient;
import jtecweb.IHttpWsServer;
import jtecweb.IWsClient;
import jtecweb.IWsServer;
import jtecweb.standard.HttpClient;
import jtecweb.standard.HttpServer;
import jtecweb.standard.WsClient;
import jtecweb.standard.WsServer;
import socket.IServerSocket;
import socket.jdk.StandardServerSocket;
import util.Bytes;
import util.StringUtil;
import util.TreeMapBuilder;
import util.XmlNode;
import ws.IWsSocket;
import ws.WsRequest;
import ws.WsResponse;

/**
 * Servidor Http e Websocket
 *
 * @author bernardobreder
 *
 */
public abstract class HttpWsServer implements IHttpWsServer {

  /** Mapeamento da sessão com os clientes */
  private final Map<String, IHttpWsClient> clients;
  /** Servidor Http */
  private final IHttpServer httpServer;
  /** Servidor Websocket */
  private final IWsServer wsServer;
  /** Indica se está fechado */
  private boolean closed;
  /** O Host e a Porta */
  private String hostPort;

  /**
   * Construtor
   *
   * @param httpServerSocket
   * @param wsServerSocket
   */
  public HttpWsServer(IServerSocket httpServerSocket,
    IServerSocket wsServerSocket) {
    this.clients = new TreeMap<String, IHttpWsClient>();
    this.httpServer = new HttpServer(httpServerSocket) {
      @Override
      public IHttpClient createHttpClient(HttpSocket socket) throws IOException {
        return new HttpClient(socket) {
          @Override
          public void service(HttpRequest req, HttpResponse resp)
            throws IOException {
            http(req, resp);
          }
        };
      }
    };
    this.wsServer = new WsServer(wsServerSocket) {
      @Override
      public IWsClient createWsClient(IWsSocket socket) throws IOException {
        String session = (String) socket.getRequest().getParameter("session");
        IHttpWsClient client = clients.get(session);
        if (client == null) {
          client = create(socket, session);
          clients.put(session, client);
        }
        else {
          client.setSocket(socket);
        }
        client.connect(socket);
        final IHttpWsClient hwclient = client;
        return new WsClient(socket) {
          @Override
          public void service(WsRequest req, WsResponse resp)
            throws IOException {
            hwclient.message(req, resp);
          }
        };
      }
    };
    this.hostPort = wsServerSocket.getHost() + ":" + wsServerSocket.getPort();
  }

  /**
   * @param req
   * @param resp
   * @throws IOException
   */
  protected void http(HttpRequest req, HttpResponse resp) throws IOException {
    XmlNode headNode = new XmlNode("head");
    headNode.addNode(new XmlNode("script").setContent(StringUtil.toString(Bytes
      .getResource(this.getClass(), "HttpWsClient.js"),
      new TreeMapBuilder<String, Object>().add("url", hostPort)
        .add("path", "/").add("session", Bytes.session(32)))));
    XmlNode bodyNode = new XmlNode("body");
    XmlNode htmlNode = new XmlNode("html").addNode(headNode).addNode(bodyNode);
    resp.answerSuccess();
    resp.writeHtml5Tag();
    resp.writeXmlNode(htmlNode);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IHttpWsServer start() {
    if (this.closed) {
      throw new IllegalStateException("server already closed");
    }
    this.httpServer.start();
    this.wsServer.start();
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IHttpWsServer close() {
    this.closed = true;
    this.httpServer.close();
    this.wsServer.close();
    for (IHttpWsClient client : this.clients.values()) {
      client.close();
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isClosed() {
    return this.closed;
  }

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    new HttpWsServer(new StandardServerSocket(8080), new StandardServerSocket(
      5000)) {
      @Override
      public IHttpWsClient create(IWsSocket socket, String session) {
        return new HttpWsClient(socket, session) {

          @Override
          public void message(WsRequest req, WsResponse resp)
            throws IOException {
            System.out.println("Message: " + req.getMessage());
          }

          @Override
          public void connect(IWsSocket socket) throws IOException {
            System.out.println("Connect: "
              + socket.getRequest().getParameter("session"));
          }

        };
      }
    }.start();
  }

}
