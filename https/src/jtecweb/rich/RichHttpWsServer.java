package jtecweb.rich;

import java.io.IOException;
import java.util.TreeMap;

import jtecweb.httpws.HttpWsServer;
import socket.jdk.StandardServerSocket;
import util.Bytes;
import util.StringUtil;
import ws.WsRequest;
import ws.WsResponse;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public class RichHttpWsServer extends HttpWsServer {

  /**
   * Construtor
   * 
   * @param httpServerSocket
   * @param wsServerSocket
   * @throws IOException
   */
  public RichHttpWsServer(StandardServerSocket httpServerSocket,
    StandardServerSocket wsServerSocket) throws IOException {
    super(httpServerSocket, wsServerSocket);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void service(WsRequest req, WsResponse resp) throws IOException {
    if (req.isConnected()) {
    }
    else {
      TreeMap<String, Object> map = new TreeMap<String, Object>();
      resp.write(StringUtil.toString(Bytes.getResource(RichHttpWsServer.class,
        "RichHttpWsServerInit.js"), map));
    }
  }

}
