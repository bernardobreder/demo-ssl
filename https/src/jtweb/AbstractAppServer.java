package jtweb;

import html.HElement;
import html.IBrowser;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpServerSocket;
import http.HttpSocket;
import http.WebServerSocket;

import java.io.IOException;
import java.util.Map;

import jtecweb.standard.HttpClient;
import socket.IServerSocket;
import util.JsonInputStream;
import util.XmlNode;
import ws.IWsSocket;

public abstract class AbstractAppServer implements IAppServer {

  private boolean closed;

  private final HttpServerSocket httpServer;

  private final WebServerSocket wsServer;

  private final IBrowser browser;

  public AbstractAppServer(IServerSocket httpServer, IServerSocket wsServer,
    IBrowser browser) {
    this.httpServer = new HttpServerSocket(httpServer);
    this.wsServer = new WebServerSocket(wsServer);
    this.browser = browser;
  }

  @Override
  public void close() {
    this.closed = true;
    try {
      this.httpServer.close();
    }
    catch (IOException e) {
    }
    try {
      this.wsServer.close();
    }
    catch (IOException e) {
    }
  }

  @Override
  public IAppServer start() {
    if (closed) {
      throw new IllegalStateException("server already closed");
    }
    new Thread(new Runnable() {
      @Override
      public void run() {
        runHttp();
      }
    }, "Http").start();
    new Thread(new Runnable() {
      @Override
      public void run() {
        runWs();
      }
    }, "Ws").start();
    return this;
  }

  protected String action(Map<String, Object> map) {
    try {
      String method = map.get("method").toString();
      if (method.equals("page")) {
        String url = map.get("url").toString();
        try {
          HElement element = this.page(url, map);
          if (element != null) {
            this.browser.setRoot(element);
          }
        }
        catch (Throwable e) {
          handler(e);
        }
      }
    }
    catch (Throwable e) {
    }
    finally {
    }
    return this.browser.consumeChanges();
  }

  protected void handler(Throwable e) {
    e.printStackTrace();
  }

  protected void runClientWs(IWsSocket socket) {
    while (!closed) {
      try {
        String message = socket.readMessage();
        JsonInputStream json = new JsonInputStream(message);
        Map<String, Object> map = json.readMap();
        String changes = action(map);
        if (!changes.isEmpty()) {
          socket.sendMessage(changes);
        }
      }
      catch (JsonInputStream.SyntaxException e) {
      }
      catch (Throwable e) {
        if (HttpClient.checkClosed(e)) {
          this.close();
        }
        else {
          e.printStackTrace();
        }
      }
    }
  }

  protected void runWs() {
    while (!closed) {
      try {
        final IWsSocket socket = this.wsServer.accept();
        new Thread(new Runnable() {
          @Override
          public void run() {
            runClientWs(socket);
          }
        }).start();
      }
      catch (Throwable e) {
        if (HttpClient.checkClosed(e)) {
          this.close();
        }
        else {
          e.printStackTrace();
        }
      }
    }
    this.close();
  }

  protected void runHttp() {
    while (!closed) {
      try {
        HttpSocket socket = this.httpServer.accept();
        HttpRequest request = new HttpRequest(socket.getInputStream());
        HttpResponse response =
          new HttpResponse(request, socket.getOutputStream());
        XmlNode html = this.html();
        response.answerSuccess();
        response.writeBytes(html.getBytes());
        response.flush();
      }
      catch (Throwable e) {
        if (HttpClient.checkClosed(e)) {
          this.close();
        }
        else {
          e.printStackTrace();
        }
      }
    }
    this.close();
  }

}
