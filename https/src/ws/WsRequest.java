package ws;

import java.util.Map;
import java.util.TreeMap;

import util.StringUtil;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public class WsRequest {

  /** Connecting */
  private final boolean connected;
  /** Mensagem */
  private final String message;
  /** Header do Ws */
  private final Map<String, String> header;

  /**
   * @param message
   */
  public WsRequest(String message) {
    this.connected = true;
    if (StringUtil.hasHeader(message)) {
      this.header = StringUtil.splitHeader(message);
      message = message.substring(message.indexOf("\r\n\r\n") + 4);
    }
    else {
      this.header = new TreeMap<String, String>();
    }
    this.message = message;
  }

  public WsRequest(boolean started, String message, Map<String, String> header) {
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

  public String getHeader(String key) {
    return this.header.get(key);
  }

}
