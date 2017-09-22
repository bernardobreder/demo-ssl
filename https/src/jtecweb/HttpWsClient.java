package jtecweb;

import http.HttpRequest;
import http.HttpResponse;

import java.io.IOException;

import ws.WebsocketRequest;
import ws.WebsocketResponse;

public class HttpWsClient implements IHttpClient, IWsClient {

  @Override
  public void service(WebsocketRequest req, WebsocketResponse resp)
    throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public void service(HttpRequest req, HttpResponse resp) throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public void start() {
    // TODO Auto-generated method stub
  }

  @Override
  public void close() {
    // TODO Auto-generated method stub
  }

  @Override
  public void addListener(IWsClientListener listener) {
    // TODO Auto-generated method stub

  }

  @Override
  public void addListener(IHttpClientListener listener) {
    // TODO Auto-generated method stub

  }

  @Override
  public void removeListener(IWsClientListener listener) {
    // TODO Auto-generated method stub

  }

  @Override
  public void removeListener(IHttpClientListener listener) {
    // TODO Auto-generated method stub

  }

  @Override
  public void removeListeners() {
    // TODO Auto-generated method stub

  }

}
