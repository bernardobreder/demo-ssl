/**
 * Copyright 2006-2007, subject to LGPL version 3 User: garethc Date: Mar 13,
 * 2007 Time: 2:11:17 PM
 */
package study.org.veryquick.embweb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;

import org.apache.log4j.Logger;

/**
 * Ultra lightweight web server for embedding in applications
 * <p/>
 * Copyright 2006-2007, subject to LGPL version 3
 *
 * @author $Author:garethc$ Last Modified: $Date:Mar 13, 2007$ $Id: blah$
 */
public class EmbeddedServer {

  /**
   * Logger
   */
  public static final Logger logger = Logger.getLogger(EmbeddedServer.class);

  /**
   * Port to serve clients on
   */
  private int serverPort;

  /**
   * Server lives as long as this is true
   */
  private volatile boolean alive;

  /**
   * Handler
   */
  private HttpRequestHandler clientHandler;

  /**
   * New instance
   * 
   * @param serverPort
   * @throws Exception
   */
  public static EmbeddedServer createInstance(int serverPort,
    HttpRequestHandler handler) throws Exception {
    final EmbeddedServer server = new EmbeddedServer(serverPort, handler);
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          server.start();
        }
        catch (IOException e) {
          logger.error("Failed to start server", e);
        }
      }
    }, "server thread");
    thread.start();
    return server;
  }

  /**
   * New instance
   * 
   * @param serverPort
   * @param handler
   * @throws Exception
   */
  private EmbeddedServer(int serverPort, HttpRequestHandler handler)
    throws Exception {
    this.serverPort = serverPort;
    this.clientHandler = handler;
  }

  /**
   * Start the server
   * 
   * @throws java.io.IOException
   */
  public void start() throws IOException {
    this.alive = true;
    ServerSocket serverSocket;
    if (System.getProperty("javax.net.ssl.keyStore") != null) {
      ServerSocketFactory ssocketFactory = SSLServerSocketFactory.getDefault();
      serverSocket = ssocketFactory.createServerSocket(this.serverPort);
    }
    else {
      serverSocket = new ServerSocket(this.serverPort);
    }
    logger.info("Server up on " + this.serverPort);
    while (alive) {
      Socket clientRequestSocket = serverSocket.accept();
      Thread thread =
        new Thread(new RequestHandler(clientRequestSocket), clientRequestSocket
          .getInetAddress().getCanonicalHostName());
      thread.start();
    }
  }

  /**
   * Request handler
   */
  private class RequestHandler implements Runnable {

    /**
     * Socket to handle the request on
     */
    private Socket clientSocket;

    /**
     * New handler
     * 
     * @param clientRequestSocket Socket to handle the request on
     */
    public RequestHandler(Socket clientRequestSocket) {
      this.clientSocket = clientRequestSocket;
    }

    /**
     * Handle the request
     * 
     * @see Thread#run()
     */
    @Override
    public void run() {
      try {
        BufferedReader in =
          new BufferedReader(new InputStreamReader(clientSocket
            .getInputStream()));
        Map<String, String> headers = new HashMap<String, String>();
        String url = null;
        HttpRequestHandler.Type type = null;
        Map<String, String> parameters = new HashMap<String, String>();
        String line = in.readLine();
        while (line != null && line.length() != 0) {
          if (line.startsWith("GET") || line.startsWith("POST")) {
            StringTokenizer tokenizer = new StringTokenizer(line, " ");
            String requestType = tokenizer.nextToken();
            url = tokenizer.nextToken();

            int indexOfQuestionMark = url.indexOf("?");
            if (indexOfQuestionMark >= 0) {
              // there are URL parameters
              String parametersToParse = url.substring(indexOfQuestionMark + 1);
              url = url.substring(0, indexOfQuestionMark);
              StringTokenizer parameterTokenizer =
                new StringTokenizer(parametersToParse, "&");
              while (parameterTokenizer.hasMoreTokens()) {
                String[] keyAndValue =
                  parameterTokenizer.nextToken().split("=");
                String key = URLDecoder.decode(keyAndValue[0], "utf-8");
                String value = URLDecoder.decode(keyAndValue[1], "utf-8");
                parameters.put(key, value);
              }
            }

            if ("POST".equals(requestType)) {
              type = HttpRequestHandler.POST;
            }
            else {
              type = HttpRequestHandler.GET;
            }

          }
          else if (line.indexOf(':') > 0) {
            int index = line.indexOf(':');
            String key = line.substring(0, index);
            String value = line.substring(index + 1);
            headers.put(key, value);
          }
          line = in.readLine();
        }
        if (type == HttpRequestHandler.GET || type == HttpRequestHandler.POST) {
          Response response =
            EmbeddedServer.this.clientHandler.handleRequest(type, headers, url,
              parameters);
          OutputStream outputStream = clientSocket.getOutputStream();
          response.writeToStream(outputStream);
          outputStream.close();
        }
      }
      catch (SocketException e) {
        logger.debug("Socket error", e);
      }
      catch (IOException e) {
        logger.error("I/O Error", e);
      }
    }

  }

}
