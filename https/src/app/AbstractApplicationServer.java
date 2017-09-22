package app;

import http.HttpRequest;
import http.HttpResponse;
import http.HttpServerSocket;
import http.HttpSocket;

import java.io.IOException;

import ws.WebServerSocket;
import ws.WebSocket;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public abstract class AbstractApplicationServer {

  /** Http Server */
  private final HttpServerSocket httpServer;
  /** Websocket Server */
  private final WebServerSocket websocketServer;
  /** Http Server Thread */
  private Thread httpThread;
  /** Websocket Server Thread */
  private Thread websocketThread;
  /** Fechado */
  private boolean closed;

  public AbstractApplicationServer(HttpServerSocket httpServer,
    WebServerSocket websocketServer) {
    this.httpServer = httpServer;
    this.websocketServer = websocketServer;
  }

  public abstract void servlet(HttpRequest req, HttpResponse resp)
    throws IOException, ServletException;

  public void runHttpServer() {
    try {
      while (!this.closed) {
        try {
          this.waitHttpForIdle();
        }
        catch (Throwable e) {
        }
      }
    }
    finally {
      this.close();
    }
  }

  public void runWebsocketServer() {
    try {
      while (!this.closed) {
        try {
          this.waitWebsocketForIdle();
        }
        catch (Throwable e) {
        }
      }
    }
    finally {
      this.close();
    }
  }

  public AbstractApplicationServer waitHttpForIdle() throws IOException {
    while (this.readHttpSocket()) {
      sleep();
    }
    return this;
  }

  protected void sleep() {
    try {
      Thread.sleep(0);
    }
    catch (InterruptedException e) {
    }
  }

  public boolean readHttpSocket() throws IOException {
    HttpSocket httpSocket = this.httpServer.accept();
    if (httpSocket != null) {
      try {
        try {
          HttpRequest request = httpSocket.getRequest();
          HttpResponse response = httpSocket.getResponse();
          this.servlet(request, response);
        }
        catch (ServletException e) {
          e.printStackTrace();
        }
        catch (IOException e) {
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
      finally {
        httpSocket.close();
      }
    }
    return httpSocket != null;
  }

  public AbstractApplicationServer waitWebsocketForIdle() throws IOException {
    while (this.readWebsocketSocket()) {
      sleep();
    }
    for (;;) {
      WebSocket websocketSocket = this.websocketServer.accept();
      if (websocketSocket == null) {
        break;
      }
      try {
      }
      finally {
        websocketSocket.close();
      }
    }
    return this;
  }

  public boolean readWebsocketSocket() throws IOException {
    WebSocket websocketSocket = this.websocketServer.accept();
    if (websocketSocket == null) {
      break;
    }
    try {
    }
    finally {
      websocketSocket.close();
    }
    return websocketSocket != null;
  }

  /**
   * @return the closed
   */
  public boolean isClosed() {
    return closed;
  }

  public AbstractApplicationServer start() {
    if (this.httpServer != null) {
      this.httpThread = new Thread(new Runnable() {
        @Override
        public void run() {
          runHttpServer();
        }
      }, System.identityHashCode(this) + " [Http Server]");
      this.httpThread.start();
    }
    if (this.websocketServer != null) {
      this.websocketThread = new Thread(new Runnable() {
        @Override
        public void run() {
          runWebsocketServer();
        }
      }, System.identityHashCode(this) + " [Websocket Server]");
      this.websocketThread.start();
    }
    return this;
  }

  public void close() {
    this.closed = true;
    if (this.httpThread != null) {
      try {
        this.httpServer.close();
      }
      catch (Exception e) {
      }
      this.httpThread = null;
    }
    if (this.websocketThread != null) {
      try {
        this.websocketServer.close();
      }
      catch (Exception e) {
      }
      this.websocketThread = null;
    }
  }

}
