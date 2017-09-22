package ws;

import java.util.Map;

import util.StringUtil;

public class WebsocketRequest {

  private final boolean connected;

  private final String message;

  private final Map<String, String> header;

  public WebsocketRequest(String message) {
    this.connected = true;
    if (StringUtil.hasHeader(message)) {
      this.header = StringUtil.splitHeader(message);
      message = message.substring(message.indexOf("\r\n\r\n") + 4);
    }
    else {
      this.header = null;
    }
    this.message = message;
  }

  public WebsocketRequest(boolean started, String message,
    Map<String, String> header) {
    super();
    this.connected = started;
    this.message = message;
    this.header = header;
  }

  public String getMessage() {
    return message;
  }

  public boolean isConnected() {
    return connected;
  }

  public Map<String, String> getHeader() {
    return header;
  }

}
