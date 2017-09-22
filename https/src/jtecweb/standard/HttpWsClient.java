package jtecweb.standard;

import http.HttpRequest;
import http.HttpResponse;
import http.HttpSocket;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

import jtecweb.IHttpClient;
import jtecweb.IHttpClient.IHttpClientListener;
import jtecweb.IHttpWsClient;
import jtecweb.IWsClient;
import jtecweb.IWsClient.IWsClientListener;
import jtecweb.demo.ApplicationJs;
import util.Bytes;
import util.XmlNode;
import ws.WebSocket;
import ws.WsRequest;
import ws.WsResponse;

/**
 * Cliente reune Http e Websocket
 *
 * @author Tecgraf/PUC-Rio
 */
public abstract class HttpWsClient implements IHttpWsClient {

  /** Host do Websocket */
  private final String wsHostPort;
  /** Sessão */
  private final String session;
  /** Cliente Http */
  private IHttpClient httpClient;
  /** Cliente Websocket */
  private IWsClient wsClient;

  /**
   * Construtor
   *
   * @param wsHostPort
   * @param httpSocket
   * @throws IOException
   */
  public HttpWsClient(String wsHostPort, HttpSocket httpSocket)
    throws IOException {
    this.wsHostPort = wsHostPort;
    this.session =
      Bytes.toHex(Bytes.md5(Long
        .toString(new Random(System.currentTimeMillis()).nextLong())));
    if (httpSocket != null) {
      this.httpClient = new HttpClient(httpSocket) {
        @Override
        public void service(HttpRequest req, HttpResponse resp)
          throws IOException {
          HttpWsClient.this.service(req, resp);
        }
      };
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void service(HttpRequest req, HttpResponse resp) throws IOException {
    String url = req.getLastUrl();
    if (!url.contains(".")) {
      String html5Tag = "<!DOCTYPE html>";
      XmlNode headElem = new XmlNode("head");
      headElem.addNode(new XmlNode("meta").setAttribute("charset", "utf-8"));
      headElem.addNode(new XmlNode("script").setContent(new ApplicationJs(
        this.wsHostPort, this.getSession()).getContent()));
      XmlNode bodyElem = new XmlNode("body");
      XmlNode htmlElem =
        new XmlNode("html").addNode(headElem).addNode(bodyElem);
      byte[] bytes = htmlElem.getBytes();
      resp.setContentType("text/html;charset=utf-8");
      resp.setContentLength(html5Tag.length() + bytes.length);
      resp.answerSuccess();
      resp.writeString(html5Tag);
      resp.writeBytes(bytes);
    }
    else {
      resp.answerNotFound();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IHttpClient startHttp() {
    if (this.httpClient != null) {
      this.httpClient.startHttp();
      this.httpClient = null;
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IWsClient startWebsocket() {
    this.wsClient.startWebsocket();
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IHttpClient closeHttp() {
    this.httpClient.closeHttp();
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IWsClient closeWebsocket() {
    this.wsClient.closeWebsocket();
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IHttpClient addListener(IHttpClientListener listener) {
    this.httpClient.addListener(listener);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IWsClient addListener(IWsClientListener listener) {
    this.wsClient.addListener(listener);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IWsClient removeListener(IWsClientListener listener) {
    this.wsClient.removeListener(listener);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IHttpClient removeListener(IHttpClientListener listener) {
    this.httpClient.removeListener(listener);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IWsClient removeWsListeners() {
    this.wsClient.removeWsListeners();
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IHttpClient removeHttpListeners() {
    this.httpClient.removeHttpListeners();
    return this;
  }

  /**
   * Retorna a sessão do cliente
   *
   * @return sessão do cliente
   */
  @Override
  public String getSession() {
    return session;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setWsClient(WebSocket socket) throws IOException {
    if (this.wsClient != null) {
      this.wsClient.closeWebsocket();
    }
    this.wsClient = new WsClient(socket) {
      @Override
      public void service(WsRequest req, WsResponse resp) throws IOException {
        HttpWsClient.this.service(req, resp);
      }
    };
    {
      Map<String, String> header = socket.getHeader();
      WsRequest req = new WsRequest(false, header.get("url"), header);
      WsResponse resp = new WsResponse(socket);
      this.service(req, resp);
      if (!resp.isEmpty()) {
        resp.flush();
      }
    }
  }

}
