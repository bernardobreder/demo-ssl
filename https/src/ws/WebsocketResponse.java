package ws;

import java.io.IOException;

public class WebsocketResponse {

  private final StringBuilder output = new StringBuilder();

  private final WebSocket socket;

  public WebsocketResponse(WebSocket socket) {
    this.socket = socket;
  }

  public WebsocketResponse write(String format, Object... objects) {
    if (objects.length > 0) {
      format = String.format(format, objects);
    }
    this.output.append(format);
    return this;
  }

  public boolean isEmpty() {
    return this.output.length() == 0;
  }

  public WebsocketResponse flush() throws IOException {
    this.socket.sendMessage(output.toString());
    output.delete(0, output.length());
    return this;
  }

  public WebsocketResponse close() throws IOException {
    return this.flush();
  }

}
