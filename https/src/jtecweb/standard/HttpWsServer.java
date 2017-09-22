package jtecweb.standard;

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
import socket.IServerSocket;
import util.StringUtil;
import ws.WebSocket;
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
  /** Host do Websocket */
  private final String wsHost;

  /**
   * Construtor
   * 
   * @param httpServerSocket
   * @param wsServerSocket
   * @throws IOException
   */
  public HttpWsServer(IServerSocket httpServerSocket,
    IServerSocket wsServerSocket) throws IOException {
    this.wsHost = wsServerSocket.getHost() + ":" + wsServerSocket.getPort();
    this.clients = new TreeMap<String, IHttpWsClient>();
    this.httpServer = new HttpServer(httpServerSocket) {

      @Override
      public IHttpClient createHttpClient(HttpSocket socket) throws IOException {
        return HttpWsServer.this.createHttpClient(socket);
      }
    };
    this.wsServer = new WsServer(wsServerSocket) {

      @Override
      public IWsClient createWsClient(WebSocket socket) throws IOException {
        return HttpWsServer.this.createWsClient(socket);
      }
    };
  }

  public abstract void service(WsRequest req, WsResponse resp)
    throws IOException;

  @Override
  public IHttpClient createHttpClient(HttpSocket socket) throws IOException {
    HttpWsClient client = new MyHttpWebClient(wsHost, socket);
    clients.put(client.getSession(), client);
    return client;
  }

  @Override
  public IWsClient createWsClient(WebSocket socket) throws IOException {
    String message = socket.readMessage();
    Map<String, String> header = StringUtil.splitHeader(message);
    socket.getHeader().putAll(header);
    String session = header.get("session");
    if (session == null) {
      return null;
    }
    IHttpWsClient httpWsClient = clients.get(session);
    if (httpWsClient == null) {
      httpWsClient = new MyHttpWebClient(wsHost, null);
    }
    httpWsClient.setWsClient(socket);
    return httpWsClient;
  }

  @Override
  public IHttpWsServer start() {
    if (this.closed) {
      throw new IllegalStateException("server already closed");
    }
    this.httpServer.start();
    this.wsServer.start();
    return this;
  }

  @Override
  public IHttpWsServer close() {
    this.closed = true;
    this.httpServer.close();
    this.wsServer.close();
    return this;
  }

  public class MyHttpWebClient extends HttpWsClient {

    public MyHttpWebClient(String wsHostPort, HttpSocket httpSocket)
      throws IOException {
      super(wsHostPort, httpSocket);
    }

    @Override
    public void service(WsRequest req, WsResponse resp) throws IOException {
      HttpWsServer.this.service(req, resp);
    }

  }

}
