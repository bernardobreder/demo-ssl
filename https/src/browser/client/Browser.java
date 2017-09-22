package browser.client;

import html.HElement;
import html.javascript.JavaScript;
import html.listener.HMouseEvent;
import html.listener.HMouseListener;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ws.WebSocket;

/**
 * Navegador do cliente
 *
 * @author Tecgraf/PUC-Rio
 */
public abstract class Browser {

  /** Navegadores */
  private static final ThreadLocal<Browser> browsers =
    new ThreadLocal<Browser>();
  /** Socket */
  private WebSocket socket;
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
  private final LinkedList<Runnable> eventQueue = new LinkedList<Runnable>();
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
   */
  public Browser(WebSocket socket, JavaScript javascript) {
    if (socket == null) {
      throw new NullPointerException();
    }
    this.socket = socket;
    this.javascript = javascript;
  }

  /**
   * Inicializa o cliente
   */
  protected abstract void init();

  /**
   * {@inheritDoc}
   */
  protected void runQueue() {
    browsers.set(this);
    try {
      while (!this.close) {
        synchronized (this.eventQueue) {
          while (!this.eventQueue.isEmpty()) {
            this.eventQueue.poll().run();
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

  protected void runMessage(String message) {
    System.out.println("Message:" + message);
    try {
      String[] args = message.split(":");
      String reason = args[0];
      if (reason.equals("action")) {
        int id = Integer.parseInt(args[1]);
        this.addChange(javascript.enable(id));
        String event = args[2];
        if (event.equals("click")) {
          HElement element = this.getElement(id);
          if (element != null) {
            HMouseListener listener =
              element.getListener("click", HMouseListener.class);
            if (listener != null) {
              String[] params = args[3].split(",");
              int x = Integer.parseInt(params[0]);
              int y = Integer.parseInt(params[1]);
              listener.action(new HMouseEvent(element, x, y));
            }
          }
        }
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
  public void sync(Runnable runnable) {
    synchronized (this.eventQueue) {
      this.eventQueue.offer(runnable);
    }
  }

  /**
   * @param format
   * @param objects
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

  protected String readMessage() throws IOException {
    return this.socket.readMessage();
  }

  protected void sendMessage(String message) throws IOException {
    this.socket.sendMessage(message);
  }

  public void restore(WebSocket socket) {
    if (!this.close) {
      throw new IllegalStateException("browser not closed");
    }
    this.close = false;
    this.socket = socket;
    this.startThread();
  }

  public void start() {
    if (this.close) {
      throw new IllegalStateException("browser already closed");
    }
    this.sync(new Runnable() {
      @Override
      public void run() {
        init();
      }
    });
    this.startThread();
  }

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

  public void removeElement(int id) {
    elements.remove(Integer.toString(id));
    this.addChange(javascript.remoteElement(id));
    this.addChange(javascript.free(id));
  }

  public void addElement(HElement element) {
    int id = element.getId();
    elements.put(Integer.toString(id), new WeakReference<HElement>(element));
    this.addChange(javascript.store(id, element.getName()));
  }

  public HElement getElement(int id) {
    Reference<HElement> ref = elements.get(Integer.toString(id));
    if (ref == null) {
      return null;
    }
    return ref.get();
  }

  public static Browser getBrowser() {
    Browser browser = browsers.get();
    if (browser == null) {
      throw new IllegalStateException("");
    }
    return browser;
  }

  public void addListener(BrowserListener listener) {
    this.listeners.add(listener);
  }

  public void removeListener(BrowserListener listener) {
    this.listeners.remove(listener);
  }

  public void removeListeners() {
    this.listeners.clear();
  }

  protected void fireClosed() {
    for (BrowserListener listener : this.listeners) {
      listener.closed();
    }
  }

  /**
   * Listener do navegador do cliente
   * 
   * @author Tecgraf/PUC-Rio
   */
  public static interface BrowserListener {

    public void closed();

  }

}
