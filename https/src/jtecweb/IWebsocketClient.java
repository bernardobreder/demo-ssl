package jtecweb;

import java.io.IOException;

import ws.WebsocketRequest;
import ws.WebsocketResponse;

public interface IWebsocketClient {

  public abstract void websocket(WebsocketRequest req, WebsocketResponse resp)
    throws IOException;

  public abstract void start();

  public abstract void close();

}