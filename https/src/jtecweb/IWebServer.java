package jtecweb;

import http.HttpSocket;

import java.io.IOException;

import ws.WebSocket;

public interface IWebServer {

  public abstract IWsClient websocket(WebSocket socket) throws IOException;

  public abstract IHttpClient http(HttpSocket socket) throws IOException;

  public abstract IWebServer start();

  public abstract IWebServer close();

}