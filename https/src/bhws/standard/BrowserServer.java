package bhws.standard;

import httpws.HttpWsClient;
import httpws.HttpWsServer;

import java.io.IOException;

import jtecweb.IHttpWsClient;
import socket.IServerSocket;
import socket.jdk.StandardServerSocket;
import ws.IWsSocket;
import ws.WsRequest;
import ws.WsResponse;
import bhws.IBrowserServer;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public class BrowserServer implements IBrowserServer {

  /** Servidor */
  private HttpWsServer server;

  /**
   * @param httpServerSocket
   * @param wsServerSocket
   * @param hostPort
   */
  public BrowserServer(IServerSocket httpServerSocket,
    IServerSocket wsServerSocket, String hostPort) {
    this.server = new HttpWsServer(httpServerSocket, wsServerSocket, hostPort) {
      @Override
      public IHttpWsClient create(IWsSocket socket, String session) {
        return BrowserServer.this.create(socket, session);
      }
    };
  }

  /**
   * @param socket
   * @param session
   * @return this
   */
  public IHttpWsClient create(IWsSocket socket, String session) {
    return new HttpWsClient(socket, session) {

      @Override
      public void message(WsRequest req, WsResponse resp) throws IOException {

      }

      @Override
      public void connect(IWsSocket socket) throws IOException {
        BrowserClient client = new BrowserClient();
      }
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IBrowserServer start() {
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
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    StandardServerSocket httpServerSocket = new StandardServerSocket(8080);
    StandardServerSocket wsServerSocket = new StandardServerSocket(5000);
    new BrowserServer(httpServerSocket, wsServerSocket, "ws://localhost:5000")
    .start();
  }

}
