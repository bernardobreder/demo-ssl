package jtecweb.demo;

import html.IBrowser;
import html.javascript.StandardJavaScript;

import java.io.IOException;

import socket.IServerSocket;
import socket.jdk.StandardServerSocket;
import app.AppServer;
import browser.standard.Browser;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public class Main extends AppServer {

  /**
   * @param httpServer
   * @param websocketServer
   * @param browser
   */
  public Main(IServerSocket httpServer, IServerSocket websocketServer,
    IBrowser browser) {
    super(httpServer, websocketServer, browser);
  }

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    int httpPort = 8080;
    int wsPort = 5000;
    StandardServerSocket httpServerSocket = new StandardServerSocket(httpPort);
    StandardServerSocket wsServerSocket = new StandardServerSocket(wsPort);
    new Main(httpServer, websocketServer, new Browser(new StandardJavaScript()))
    .start();
  }

}
