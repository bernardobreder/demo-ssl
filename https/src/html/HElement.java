package html;

import html.bootstrap.HBColor;
import html.listener.HListener;
import html.listener.HMouseListener;
import html.primitive.HSpace;
import html.primitive.HText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Elemento Tag
 *
 * @author bernardobreder
 */
public class HElement {

  /** Navegador */
  private IBrowser browser;
  /** Name */
  private String name;
  /** Filhos */
  private List<HElement> children;
  /** Classes */
  private Set<String> classes;
  /** Atributos */
  private Map<String, String> attrs;
  /** Atributos */
  private Map<String, String> styles;
  /** Atributos */
  private Map<String, HListener> events;
  /** Indica se está ativado */
  private boolean enabled = true;
  /** Indica se houve mudança */
  private boolean changed = true;

  /** Vazio */
  private static final List<HElement> CHILDREN_EMPTY = Collections
    .unmodifiableList(new ArrayList<HElement>(0));
  /** Vazio */
  private static final Set<String> CLASSES_EMPTY = Collections
    .unmodifiableSet(new TreeSet<String>());
  /** Vazio */
  private static final Map<String, String> ATTRS_EMPTY = Collections
    .unmodifiableMap(new HashMap<String, String>(0));
  /** Vazio */
  private static final Map<String, HListener> EVENTS_EMPTY = Collections
    .unmodifiableMap(new HashMap<String, HListener>(0));

  /**
   * Construtor
   * 
   * @param name
   */
  public HElement(String name) {
    this.name = name;
    this.browser = IBrowser.browers.get();
    this.browser.addElement(this);
  }

  /**
   * @param element
   * @return this
   */
  public <E extends HElement> E addElement(HElement element) {
    if (this.children == null) {
      this.children = new ArrayList<HElement>(5);
    }
    this.children.add(element);
    IBrowser browser = this.getBrowser();
    browser.addChange(browser.getJavascript().append(getId(), element.getId()));
    return this.cast();
  }

  /**
   * @param element
   * @return this
   */
  public <E extends HElement> E removeElement(HElement element) {
    if (this.children != null) {
      if (this.children.remove(element)) {
        IBrowser browser = this.getBrowser();
        browser.addChange(browser.getJavascript().remoteElement(getId()));
      }
    }
    return this.cast();
  }

  /**
   * @return this
   */
  public <E extends HElement> E removeElements() {
    if (this.children != null && !this.children.isEmpty()) {
      this.children.clear();
      IBrowser browser = this.getBrowser();
      browser.addChange(browser.getJavascript().empty(getId()));
    }
    return this.cast();
  }

  /**
   * @param listener
   * @return this
   */
  public <E extends HElement> E addEventClick(HMouseListener listener) {
    if (this.events == null) {
      this.events = new HashMap<String, HListener>();
    }
    this.events.put("click", listener);
    IBrowser browser = this.getBrowser();
    browser.addChange(browser.getJavascript().addEventClick(getId()));
    return this.cast();
  }

  /**
   * @param listener
   * @return this
   */
  public <E extends HElement> E addEventDoubleClick(HMouseListener listener) {
    if (this.events == null) {
      this.events = new HashMap<String, HListener>();
    }
    this.events.put("dblclick", listener);
    IBrowser browser = this.getBrowser();
    browser.addChange(browser.getJavascript().addEventDoubleClick(getId()));
    return this.cast();
  }

  /**
   * @return this
   */
  public <E extends HElement> E removeEventClick() {
    if (this.events != null) {
      if (this.events.remove("click") != null) {
        IBrowser browser = this.getBrowser();
        browser.addChange(browser.getJavascript().removeEventClick(getId()));
      }
    }
    return this.cast();
  }

  /**
   * @param text
   * @return this
   */
  public <E extends HElement> E addText(String text) {
    return this.addElement(new HText(text));
  }

  /**
   * @return this
   */
  public <E extends HElement> E addSpace() {
    return this.addElement(new HSpace());
  }

  /**
   * Altera o alinhamento horizontal
   * 
   * @return this
   */
  public <E extends HElement> E alignLeft() {
    this.addClass("text-left");
    return this.cast();
  }

  /**
   * Altera o alinhamento horizontal
   * 
   * @return this
   */
  public <E extends HElement> E alignCenter() {
    this.addClass("text-center");
    return this.cast();
  }

  /**
   * Altera o alinhamento horizontal
   * 
   * @return this
   */
  public <E extends HElement> E alignRight() {
    this.addClass("text-right");
    return this.cast();
  }

  /**
   * @param flag
   * @return this
   */
  public <E extends HElement> E setSmall(boolean flag) {
    if (flag) {
      this.addClass("small");
    }
    else {
      this.removeClass("small");
    }
    return this.cast();
  }

  /**
   * @param color
   * @return this
   */
  public <E extends HElement> E setColor(HBColor color) {
    this.addClass("text-" + color.name().toLowerCase());
    return this.cast();
  }

  /**
   * @param flag
   * @return this
   */
  public <E extends HElement> E setEnabled(boolean flag) {
    this.enabled = flag;
    IBrowser browser = this.getBrowser();
    if (flag) {
      browser.addChange(browser.getJavascript().enable(getId()));
    }
    else {
      browser.addChange(browser.getJavascript().disable(getId()));
    }
    return this.cast();
  }

  /**
   * @return the enabled
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * @return lista de filhos
   */
  public List<HElement> list() {
    if (this.children == null) {
      return CHILDREN_EMPTY;
    }
    else {
      return this.children;
    }
  }

  /**
   * @return tamanho
   */
  public int size() {
    return this.children == null ? 0 : this.children.size();
  }

  /**
   * @param name
   * @return adiciona uma classe
   */
  public <E extends HElement> E addClass(String name) {
    if (this.classes == null) {
      this.classes = new TreeSet<String>();
    }
    this.classes.add(name);
    IBrowser browser = this.getBrowser();
    browser.addChange(browser.getJavascript().addClass(getId(), name));
    return this.cast();
  }

  /**
   * @param name
   * @return indica se tem classe
   */
  public boolean hasClass(String name) {
    if (this.classes == null) {
      return false;
    }
    return this.classes.contains(name);
  }

  /**
   * @param name
   * @return this
   */
  public <E extends HElement> E removeClass(String name) {
    if (this.classes != null) {
      if (this.classes.remove(name)) {
        IBrowser browser = this.getBrowser();
        browser.addChange(browser.getJavascript().removeClass(getId(), name));
      }
    }
    return this.cast();
  }

  /**
   * @param key
   * @param value
   * @return adiciona uma classe
   */
  public <E extends HElement> E addStyle(String key, String value) {
    if (this.styles == null) {
      this.styles = new TreeMap<String, String>();
    }
    this.styles.put(key, value);
    IBrowser browser = this.getBrowser();
    browser.addChange(browser.getJavascript().addCss(getId(), key, value));
    return this.cast();
  }

  /**
   * @param key
   * @return this
   */
  public <E extends HElement> E removeStyle(String key) {
    if (this.styles != null) {
      if (this.styles.remove(key) != null) {
        IBrowser browser = this.getBrowser();
        browser.addChange(browser.getJavascript().removeCss(getId(), key));
      }
    }
    return this.cast();
  }

  /**
   * @return classes
   */
  public Set<String> listClasses() {
    if (this.classes == null) {
      return CLASSES_EMPTY;
    }
    else {
      return this.classes;
    }
  }

  /**
   * @param key
   * @param value
   * @return this
   */
  public <E extends HElement> E addAttribute(String key, String value) {
    if (this.attrs == null) {
      this.attrs = new TreeMap<String, String>();
    }
    this.attrs.put(key, value);
    IBrowser browser = this.getBrowser();
    browser.addChange(browser.getJavascript().addAttribute(this.getId(), key,
      value));
    return this.cast();
  }

  /**
   * @param key
   * @param defaultValue
   * @return valor
   */
  public String getAttribute(String key, String defaultValue) {
    if (this.attrs == null) {
      return defaultValue;
    }
    String value = this.attrs.get(key);
    if (value == null) {
      value = defaultValue;
    }
    return value;
  }

  /**
   * @param key
   * @return this
   */
  public <E extends HElement> E removeAttribute(String key) {
    if (this.attrs != null) {
      if (this.attrs.remove(key) != null) {
        IBrowser browser = this.getBrowser();
        browser
          .addChange(browser.getJavascript().removeAttribute(getId(), key));
      }
    }
    return this.cast();
  }

  /**
   * @return lista de atributos
   */
  public Set<Entry<String, String>> listAttributes() {
    if (this.attrs == null) {
      return ATTRS_EMPTY.entrySet();
    }
    else {
      return this.attrs.entrySet();
    }
  }

  /**
   * @return lista de atributos
   */
  public Set<Entry<String, HListener>> listEvents() {
    if (this.events == null) {
      return EVENTS_EMPTY.entrySet();
    }
    else {
      return this.events.entrySet();
    }
  }

  /**
   * @param name
   * @param c
   * @return listener
   */
  public <E extends HListener> E getListener(String name, Class<E> c) {
    if (this.events == null) {
      return null;
    }
    HListener listener = this.events.get(name);
    if (listener == null) {
      return null;
    }
    if (!c.isInstance(listener)) {
      return null;
    }
    return c.cast(listener);
  }

  /**
   * @return id
   */
  public int getId() {
    return System.identityHashCode(this);
  }

  /**
   * @return filhos
   */
  public List<HElement> getChildren() {
    if (children == null) {
      return CHILDREN_EMPTY;
    }
    return children;
  }

  /**
   * @return mudou
   */
  public boolean isChanged() {
    if (changed) {
      return true;
    }
    List<HElement> children = this.getChildren();
    if (children != null) {
      for (HElement child : children) {
        if (child.isChanged()) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * @param changed
   */
  public void setChanged(boolean changed) {
    this.changed = changed;
  }

  /**
   * @return nome da tag
   */
  public String getName() {
    return this.name;
  }

  /**
   * @return navegador
   */
  public IBrowser getBrowser() {
    return this.browser;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }

  /**
   * @return this
   */
  protected <E> E cast() {
    @SuppressWarnings("unchecked")
    E ethis = (E) this;
    return ethis;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void finalize() throws Throwable {
    IBrowser browser = this.getBrowser();
    browser.removeElement(this.getId());
    super.finalize();
  }

  /**
   * @return this
   */
  public HElement fireChanged() {
    this.setChanged(false);
    List<HElement> children = this.getChildren();
    if (children != null) {
      for (HElement child : children) {
        if (child.isChanged()) {
          child.fireChanged();
        }
      }
    }
    return this;
  }

}
