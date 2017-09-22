package browser.standard;

import html.HElement;
import html.javascript.JavaScript;
import html.listener.HMouseEvent;
import html.listener.HMouseListener;
import html.primitive.HTextHeader;
import http.HttpRequest;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ws.IWsSocket;

/**
 * Navegador do cliente
 * 
 * @author bernardobreder
 */
public abstract class CopyOfBrowser {

  /** Navegadores */
  private static final ThreadLocal<CopyOfBrowser> browsers =
    new ThreadLocal<CopyOfBrowser>();
  /** Socket */
  private IWsSocket socket;
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
  private final LinkedList<BrowserSync> eventQueue =
    new LinkedList<BrowserSync>();
  /** Fila de eventos na Thread do Navegador */
  private final LinkedList<String> readMessageQueue = new LinkedList<String>();
  /** Indica se a stream está fechada */
  private boolean close;
  /** Propriedades */
  private final Map<String, String> properties = new TreeMap<String, String>();
  /** Fila de eventos na Thread do Navegador */
  private final List<BrowserListener> listeners =
    new ArrayList<BrowserListener>();

  /**
   * @param socket
   * @param javascript
   */
  public CopyOfBrowser(IWsSocket socket, JavaScript javascript) {
    if (socket == null) {
      throw new NullPointerException();
    }
    this.socket = socket;
    this.javascript = javascript;
  }

  /**
   * Inicializa o cliente
   * 
   * @param request
   * @return elemento
   * @throws ServletException
   * @throws IOException
   */
  public abstract HElement request(HttpRequest request)
    throws ServletException, IOException;

  /**
   * Inicializa o cliente
   * 
   * @param request
   * @param e
   * @return elemento
   */
  public HElement error(HttpRequest request, Throwable e) {
    String message = e.getMessage();
    return new HTextHeader(1, message);
  }

  /**
   * Inicializa o cliente
   * 
   * @param url
   * @return elemento
   */
  public CopyOfBrowser goTo(String url) {
    try {
      HttpRequest request = new HttpRequest(url);
      HElement element = this.request(request);
      this.addChange(this.getJavascript().pushState(url));
      this.setRoot(element);
    }
    catch (Throwable e) {
      HttpRequest request = new HttpRequest(url);
      HElement element = this.error(request, e);
      this.addChange(this.getJavascript().pushState(url));
      this.setRoot(element);
    }
    return this;
  }

  /**
   * 
   */
  protected void runQueue() {
    browsers.set(this);
    try {
      while (!this.close) {
        synchronized (this.eventQueue) {
          while (!this.eventQueue.isEmpty()) {
            BrowserSync sync = this.eventQueue.poll();
            try {
              sync.run(this);
            }
            catch (Throwable e) {
              e.printStackTrace();
            }
          }
        }
        String message = null;
        synchronized (this.readMessageQueue) {
          if (!this.readMessageQueue.isEmpty()) {
            message = this.readMessageQueue.poll();
          }
        }
        if (message != null) {
          this.runMessage(message);
        }
        if (this.root != null && this.root.isChanged()) {
          this.root.fireChanged();
        }
        String response = null;
        synchronized (changes) {
          if (this.changes.length() > 0) {
            response = this.changes.toString();
            this.changes.delete(0, this.changes.length());
          }
        }
        if (response != null) {
          try {
            this.sendMessage(response);
          }
          catch (Throwable e) {
            break;
          }
        }
        try {
          Thread.sleep(10);
        }
        catch (InterruptedException e) {
          break;
        }
      }
    }
    finally {
      this.close();
    }
  }

  /**
   * 
   */
  protected void runStream() {
    try {
      while (!this.close) {
        try {
          String message = this.readMessage();
          if (message == null) {
            break;
          }
          synchronized (this.readMessageQueue) {
            this.readMessageQueue.offer(message);
          }
        }
        catch (Throwable e) {
          break;
        }
      }
    }
    finally {
      this.close();
    }
  }

  /**
   * @param message
   */
  protected void runMessage(String message) {
    System.out.println("Message:" + message);
    try {
      String[] args = message.split(":");
      String reason = args[0];
      if (reason.equals("action")) {
        int id = Integer.parseInt(args[1]);
        this.addChange(javascript.enable(id));
        String eventName = args[2];
        HElement element = this.getElement(id);
        if (element != null) {
          HMouseListener listener =
            element.getListener(eventName, HMouseListener.class);
          if (listener != null) {
            String[] params = args[3].split(",");
            int x = Integer.parseInt(params[0]);
            int y = Integer.parseInt(params[1]);
            HMouseEvent mouseEvent = new HMouseEvent(element, x, y);
            listener.action(mouseEvent);
          }
        }
      }
      else if (reason.equals("page")) {
        HttpRequest request = new HttpRequest(args[1]);
        HElement element = this.request(request);
        this.setRoot(element);
      }
      else if (reason.equals("navigator")) {
        String key = args[1];
        if (key.equals("browser")) {
          this.properties.put("browser", args[2]);
        }
        else if (key.equals("language")) {
          this.properties.put("language", args[2]);
        }
        else if (key.equals("platform")) {
          this.properties.put("platform", args[2]);
        }
      }
    }
    catch (Throwable e) {
      e.printStackTrace();
    }
  }

  /**
   * 
   */
  protected synchronized void close() {
    if (!this.close) {
      this.close = true;
      browsers.remove();
      try {
        this.socket.close();
      }
      catch (Throwable e) {
      }
      this.fireClosed();
    }
  }

  /**
   * @param runnable
   */
  public void sync(BrowserSync runnable) {
    synchronized (this.eventQueue) {
      this.eventQueue.offer(runnable);
    }
  }

  /**
   * @param change
   */
  public void addChange(String change) {
    synchronized (changes) {
      changes.append(change);
    }
  }

  /**
   * @return root
   */
  public HElement getRoot() {
    return this.root;
  }

  /**
   * @param root
   */
  public void setRoot(HElement root) {
    this.root = root;
    if (root == null) {
      this.addChange(javascript.bodyEmpty());
    }
    else {
      this.addChange(javascript.bodyHtml(root.getId()));
    }
  }

  /**
   * @return message
   * @throws IOException
   */
  protected String readMessage() throws IOException {
    return this.socket.readMessage();
  }

  /**
   * @param message
   * @throws IOException
   */
  protected void sendMessage(String message) throws IOException {
    this.socket.sendMessage(message);
  }

  /**
   * @param socket
   */
  public void restore(IWsSocket socket) {
    if (!this.close) {
      throw new IllegalStateException("browser not closed");
    }
    this.close = false;
    this.socket = socket;
    this.startThread();
  }

  /**
   * 
   */
  public void start() {
    if (this.close) {
      throw new IllegalStateException("browser already closed");
    }
    this.startThread();
  }

  /**
   * 
   */
  private void startThread() {
    int id = System.identityHashCode(this);
    Thread queueThread = new Thread(new Runnable() {
      @Override
      public void run() {
        runQueue();
      }
    }, "Browser-" + id);
    Thread streamThread = new Thread(new Runnable() {
      @Override
      public void run() {
        runStream();
      }
    }, "Browser-" + id + "-Stream");
    queueThread.setDaemon(true);
    streamThread.setDaemon(true);
    queueThread.start();
    streamThread.start();
  }

  /**
   * Apresenta um alerta no navegador do usuário
   * 
   * @param message
   */
  public void showMessage(String message) {
    this.addChange(javascript.alert(message));
  }

  /**
   * @return the javascript
   */
  public JavaScript getJavascript() {
    return javascript;
  }

  /**
   * @param javascript the javascript to set
   */
  public void setJavascript(JavaScript javascript) {
    this.javascript = javascript;
  }

  /**
   * Remove um componente html no navegador do usuário
   * 
   * @param id
   */
  public void removeElement(int id) {
    elements.remove(Integer.toString(id));
    this.addChange(javascript.remoteElement(id));
  }

  /**
   * Adiciona um elemento html no navegador do usuário
   * 
   * @param element
   */
  public void addElement(HElement element) {
    int id = element.getId();
    elements.put(Integer.toString(id), new WeakReference<HElement>(element));
    this.addChange(javascript.store(id, element.getName()));
  }

  /**
   * Retorna um elemento html do navegador do usuário
   * 
   * @param id
   * @return elemento html
   */
  public HElement getElement(int id) {
    Reference<HElement> ref = elements.get(Integer.toString(id));
    if (ref == null) {
      return null;
    }
    return ref.get();
  }

  /**
   * @return navegador
   */
  public static CopyOfBrowser getBrowser() {
    CopyOfBrowser browser = browsers.get();
    if (browser == null) {
      throw new IllegalStateException("");
    }
    return browser;
  }

  /**
   * Adiciona um listener
   * 
   * @param listener
   */
  public void addListener(BrowserListener listener) {
    this.listeners.add(listener);
  }

  /**
   * Remove um listener
   * 
   * @param listener
   */
  public void removeListener(BrowserListener listener) {
    this.listeners.remove(listener);
  }

  /**
   * Remove todos os listeners
   */
  public void removeListeners() {
    this.listeners.clear();
  }

  /**
   * Dispara o evento de fechar o navegador
   */
  protected void fireClosed() {
    for (BrowserListener listener : this.listeners) {
      listener.closed();
    }
  }

  /**
   * Listener do navegador do cliente
   * 
   * @author bernardobreder
   */
  public static interface BrowserListener {

    /**
     * Indica que foi fechado o navegador
     */
    public void closed();

  }

  /**
   * Listener do navegador do cliente
   * 
   * @author bernardobreder
   */
  public static interface BrowserSync {

    /**
     * Indica que foi fechado o navegador
     * 
     * @param browser
     */
    public void run(CopyOfBrowser browser);

  }

}
