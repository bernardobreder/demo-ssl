package browser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import websocket.WebSocket;

public abstract class ClientBrowser implements Runnable {

  protected BrowserEnvironment env;

  protected BrowserBody body;

  protected WebSocket socket;

  protected Map<Pattern, BrowserServlet> servlets =
    new HashMap<Pattern, BrowserServlet>();

  public ClientBrowser(WebSocket socket) {
    super();
    this.socket = socket;
    this.env = new BrowserEnvironment();
    this.body = new BrowserBody();
  }

  public void addServlet(String pattern, BrowserServlet servlet) {
    this.servlets.put(Pattern.compile(pattern), servlet);
  }

  @Override
  public void run() {
    try {
      this.fireServlet("index");
    }
    finally {
      try {
        this.socket.close();
      }
      catch (IOException e) {
      }
    }
  }

  protected void fireServlet(String servletName) {
    BrowserServlet serlvet = null;
    for (Entry<Pattern, BrowserServlet> entry : servlets.entrySet()) {
      if (entry.getKey().matcher(servletName).matches()) {
        serlvet = entry.getValue();
        break;
      }
    }
    if (serlvet != null) {
      String message = serlvet.get();
      if (message != null) {
        try {
          this.socket.sendMessage(message);
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void start() {
    new Thread(this, "Browser").start();
  }

}
