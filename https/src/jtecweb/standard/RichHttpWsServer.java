package jtecweb.standard;

import html.builder.JsBuilder;

import java.io.IOException;

import socket.jdk.StandardServerSocket;
import ws.WsRequest;
import ws.WsResponse;

public class RichHttpWsServer extends HttpWsServer {

  public RichHttpWsServer(StandardServerSocket httpServerSocket,
    StandardServerSocket wsServerSocket) throws IOException {
    super(httpServerSocket, wsServerSocket);
  }

  @Override
  public void service(WsRequest req, WsResponse resp) throws IOException {
    if (req.isConnected()) {
    }
    else {
      resp.write(new InitApp().consume());
    }
  }

  public static class InitApp extends JsBuilder {

    public InitApp() {
      String[] jsArray = { "http://code.jquery.com/jquery-2.1.0.min.js", "http://getbootstrap.com/dist/js/bootstrap.min.js" };
      String[] cssArray = { "http://getbootstrap.com/dist/css/bootstrap.min.css" };
      append("$S = {};")
      append("inited = %d;", jsArray.length + cssArray.length);
      append("var init = function() { if (inited == 0 ) { console.info('Init'); } }");
      for (int n = 0; n < jsArray.length; n++) {
        String path = jsArray[n];
        addScript(path);
      }
      for (int n = 0; n < cssArray.length; n++) {
        String path = cssArray[n];
        addStyle(path);
      }
    }

    protected void addScriptFunction(String path) {
      append("$S.function(path, callback) {");
      append("var tag = document.createElement('script');");
      append("tag.type = 'text/javascript';");
      append("tag.src = '%s';", path);
      append("tag.onload = function() { inited--; init(); };");
      append("document.head.appendChild(tag);");
      append("}");
    }

    protected void addScript(String path) {
      append("var tag = document.createElement('script');");
      append("tag.type = 'text/javascript';");
      append("tag.src = '%s';", path);
      append("tag.onload = function() { inited--; init(); };");
      append("document.head.appendChild(tag);");
    }

    protected void addStyle(String path) {
      append("var tag = document.createElement('link');");
      append("tag.rel = 'stylesheet';");
      append("tag.href = '%s';", path);
      append("tag.onload = function() { inited--; init(); };");
      append("document.head.appendChild(tag);");
    }

  }

}
