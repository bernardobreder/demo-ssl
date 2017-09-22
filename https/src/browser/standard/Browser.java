package browser.standard;

import html.HElement;
import html.IBrowser;
import html.javascript.JavaScript;
import html.listener.HMouseEvent;
import html.listener.HMouseListener;
import http.HttpRequest;
import httpws.HttpWsClient;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import jtecweb.IHttpWsClient;
import util.JsonInputStream;
import util.JsonInputStream.SyntaxException;
import util.TreeMapBuilder;
import ws.IWsSocket;
import ws.WsRequest;
import ws.WsResponse;
import demo.bandeirabr.desktop.DesktopFrame;
import demo.bandeirabr.scenarium.ScenariumFrame;

/**
 * Navegador do cliente
 *
 * @author bernardobreder
 */
public class Browser extends HttpWsClient implements IBrowser, IHttpWsClient {

  /** Root */
  private HElement root;
  /** Elementos */
  private final Map<String, Reference<HElement>> elements =
    new TreeMap<String, Reference<HElement>>();
  /** Buffer */
  private StringBuilder changes = new StringBuilder();
  /** Javascript */
  private JavaScript javascript;
  /** Fila de eventos na Thread do Navegador */
  private final LinkedList<IBrowserSync> eventQueue =
    new LinkedList<IBrowserSync>();
  /** Fila de eventos na Thread do Navegador */
  private final LinkedList<String> readMessageQueue = new LinkedList<String>();
  /** Propriedades */
  private final Map<String, String> properties = new TreeMap<String, String>();

  /**
   * @param socket
   * @param javascript
   */
  public Browser(IWsSocket socket, String session, JavaScript javascript) {
    super(socket, session);
    this.javascript = javascript;
  }

  @Override
  public void connect(IWsSocket socket) throws IOException {
    IBrowser.browers.set(this);
    try {
      HttpRequest request = socket.getRequest();
      String url = request.getUrl();
      Map<String, Object> params = request.getParameters();
      this.openPage(url, params);
      if (this.root.isChanged()) {
        this.root.fireChanged();
      }
    }
    finally {
      IBrowser.browers.remove();
    }
    String changes = this.consumeChanges();
    if (!changes.isEmpty()) {
      socket.sendMessage(changes.replace("\"", "'"));
    }
  }

  @Override
  public void message(WsRequest req, WsResponse resp) throws IOException {
    String message = req.getMessage();
    System.out.println("Message: " + message);
    try {
      JsonInputStream json = new JsonInputStream(message);
      Map<String, Object> tree = json.readMap();
      this.message(tree);
    }
    catch (SyntaxException e) {
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void message(Map<String, Object> tree) throws IOException {
    IBrowser.browers.set(this);
    try {
      String method = tree.get("method").toString();
      if (method.equals("action")) {
        String event = tree.get("event").toString();
        int target = Integer.parseInt(tree.get("target").toString());
        int x = Integer.parseInt(tree.get("x").toString());
        int y = Integer.parseInt(tree.get("y").toString());
        HElement element = this.getElement(target);
        if (element != null) {
          HMouseListener listener =
            element.getListener(event, HMouseListener.class);
          HMouseEvent e = new HMouseEvent(element, x, y);
          try {
            listener.action(e);
          }
          catch (Throwable e1) {
            e1.printStackTrace();
          }
        }
      }
      else if (method.equals("page")) {
        String url = tree.get("url").toString();
        openPage(url, tree);
      }
      if (this.getRoot().isChanged()) {
        this.getRoot().fireChanged();
      }
    }
    finally {
      IBrowser.browers.remove();
    }
    String changes = this.consumeChanges();
    if (!changes.isEmpty()) {
      this.getSocket().sendMessage(changes.replace("\"", "'"));
    }
  }

  protected void openPage(String url, Map<String, Object> params) {
    if (url.equals("/")) {
      this.setRoot(new DesktopFrame());
    }
    else if (url.equals("/scenarium")) {
      this.setRoot(new ScenariumFrame(1));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IBrowser fireChanged() {
    if (this.root != null && this.root.isChanged()) {
      this.root.fireChanged();
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IBrowser goTo(String url) throws IOException {
    TreeMapBuilder<String, Object> tree = new TreeMapBuilder<String, Object>();
    tree.put("method", "page");
    tree.put("url", url);
    this.message(tree);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sync(IBrowserSync runnable) {
    synchronized (this.eventQueue) {
      this.eventQueue.offer(runnable);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addChange(String change) {
    synchronized (changes) {
      changes.append(change);
    }
  }

  @Override
  public String consumeChanges() {
    String result;
    synchronized (changes) {
      result = changes.toString();
      changes.delete(0, changes.length());
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HElement getRoot() {
    return this.root;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRoot(HElement root) {
    this.root = root;
    JavaScript javascript = getJavascript();
    if (root == null) {
      this.addChange(javascript.bodyEmpty());
    }
    else {
      this.addChange(javascript.bodyHtml(root.getId()));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showMessage(String message) {
    this.addChange(javascript.alert(message));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JavaScript getJavascript() {
    return javascript;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setJavascript(JavaScript javascript) {
    this.javascript = javascript;
  }

  /**
   * Remove um componente html no navegador do usuário
   *
   * @param id
   */
  @Override
  public void removeElement(int id) {
    elements.remove(Integer.toString(id));
    this.addChange(javascript.remoteElement(id));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addElement(HElement element) {
    int id = element.getId();
    elements.put(Integer.toString(id), new WeakReference<HElement>(element));
    this.addChange(javascript.store(id, element.getName()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HElement getElement(int id) {
    Reference<HElement> ref = elements.get(Integer.toString(id));
    if (ref == null) {
      return null;
    }
    return ref.get();
  }

}
