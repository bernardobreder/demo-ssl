package jtecweb.rich;

import html.HElement;
import html.IBrowser.IBrowserSync;
import html.javascript.JavaScript;
import http.HttpRequest;
import http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jtecweb.IHttpWsClient;
import util.Bytes;
import util.StringUtil;
import util.XmlNode;

/**
 * Cliente reune Http e Websocket
 *
 * @author Tecgraf/PUC-Rio
 */
public abstract class HttpWsClient implements IHttpWsClient {

  /** Navegadores */
  public static final ThreadLocal<IHttpWsClient> clients =
    new ThreadLocal<IHttpWsClient>();

  /** Sessão */
  private final String session;
  /** Root */
  private HElement root;
  /** Elementos */
  private final Map<String, Reference<HElement>> elements =
    new HashMap<String, Reference<HElement>>();
  /** Buffer */
  private StringBuilder changes = new StringBuilder();
  /** Javascript */
  private JavaScript javascript;
  /** Fila de eventos na Thread do Navegador */
  private final LinkedList<IBrowserSync> eventQueue =
    new LinkedList<IBrowserSync>();
  /** Fila de eventos na Thread do Navegador */
  private final LinkedList<String> readMessageQueue = new LinkedList<String>();
  /** Indica se a stream está fechada */
  private boolean close;
  /** Propriedades */
  private final Map<String, String> properties = new TreeMap<String, String>();
  /** Fila de eventos na Thread do Navegador */
  private final List<IHttpWsClientListener> listeners =
    new ArrayList<IHttpWsClientListener>();

  /**
   * Construtor
   */
  public HttpWsClient() {
    this.session = Bytes.session(64);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void http(HttpRequest req, HttpResponse resp) throws IOException {
    String url = req.getLastUrl();
    if (!url.contains(".")) {
      InputStream js =
        Bytes.getResource(HttpWsClient.class, "HttpWsClientInit.js");
      TreeMap<String, Object> map = new TreeMap<String, Object>();
      map.put("wsHost", "ws://localhost:5000");
      map.put("session", this.getSession());
      String content = StringUtil.toString(js, map);
      XmlNode headElem = new XmlNode("head");
      headElem.addNode(new XmlNode("meta").setAttribute("charset", "utf-8"));
      headElem.addNode(new XmlNode("script").setContent(content));
      XmlNode bodyElem = new XmlNode("body");
      XmlNode htmlElem =
        new XmlNode("html").addNode(headElem).addNode(bodyElem);
      resp.setContentType("text/html;charset=utf-8");
      resp.answerSuccess();
      resp.setCookie("key", req.hashCode());
      resp.writeHtml5Tag();
      resp.writeBytes(htmlElem.getBytes());
    }
    else {
      resp.answerNotFound();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IHttpWsClient addListener(IHttpWsClientListener listener) {
    this.listeners.add(listener);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IHttpWsClient removeListener(IHttpWsClientListener listener) {
    this.listeners.remove(listener);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IHttpWsClient removeListeners() {
    this.listeners.clear();
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
  public void close() {
    this.fireClosed();
  }

  /**
   * Dispara o evento de fechar o navegador
   */
  protected void fireClosed() {
    for (IHttpWsClientListener listener : this.listeners) {
      listener.closed(this);
    }
  }

}
