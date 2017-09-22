package html;

import html.listener.HListener;
import html.primitive.HSpace;
import html.primitive.HText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import browser.Browser;

/**
 * Elemento Tag
 *
 * @author Tecgraf/PUC-Rio
 */
public class HTagElement extends HElement {

  /** Navegador */
  private Browser browser;
  /** Name */
  private String name;
  /** Filhos */
  private List<HElement> children;
  /** Classes */
  private Set<String> classes;
  /** Atributos */
  private Map<String, String> attrs;
  /** Atributos */
  private Map<String, HListener> events;
  /** Vazio */
  private static final List<HElement> CHILDREN_EMPTY = Collections.emptyList();
  /** Vazio */
  private static final Set<String> CLASSES_EMPTY = Collections.emptySet();
  /** Vazio */
  private static final Map<String, String> ATTRS_EMPTY = Collections.emptyMap();
  /** Vazio */
  private static final Map<String, HListener> EVENTS_EMPTY = Collections
    .emptyMap();

  /**
   * Construtor
   *
   * @param name
   */
  public HTagElement(String name) {
    super(name);
    this.browser = Browser.getBrowser();
    this.browser.addElement(this);
  }

  /**
   * @param element
   * @return this
   */
  @Override
  public <E extends HElement> E addElement(HElement element) {
    if (this.children == null) {
      this.children = new ArrayList<HElement>(5);
    }
    this.children.add(element);
    this.browser.addChange("$C[%d].append($C[%d]);", this.getId(), element
      .getId());
    @SuppressWarnings("unchecked")
    E ethis = (E) this;
    return ethis;
  }

  /**
   * @param listener
   * @return this
   */
  public <E extends HTagElement> E addEventClick(HListener listener) {
    if (this.events == null) {
      this.events = new HashMap<String, HListener>();
    }
    this.events.put("click", listener);
    this.browser.addChange("$C[%d].bind('click',function(e){$WS.send("
      + "'action:click:%d:'+e.pageX+','+e.pageY);});", this.getId(), this
      .getId());
    @SuppressWarnings("unchecked")
    E ethis = (E) this;
    return ethis;
  }

  /**
   * @return this
   */
  public <E extends HTagElement> E removeEventClick() {
    if (this.events != null) {
      if (this.events.remove("click") != null) {
        this.browser.addChange("$C[%d].unbind('click');", this.getId(), this
          .getId());
      }
    }
    @SuppressWarnings("unchecked")
    E ethis = (E) this;
    return ethis;
  }

  /**
   * @param text
   * @return this
   */
  public <E extends HTagElement> E addText(String text) {
    return this.addElement(new HText(text));
  }

  /**
   * @return this
   */
  public <E extends HTagElement> E addSpace() {
    return this.addElement(new HSpace());
  }

  /**
   * @return lista de filhos
   */
  @Override
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
  @Override
  public int size() {
    return this.children == null ? 0 : this.children.size();
  }

  /**
   * @param name
   * @return adiciona uma classe
   */
  public <E extends HTagElement> E addClass(String name) {
    if (this.classes == null) {
      this.classes = new HashSet<String>(5);
    }
    this.classes.add(name);
    this.browser.addChange("$C[%d].addClass('%s');", this.getId(), name);
    @SuppressWarnings("unchecked")
    E ethis = (E) this;
    return ethis;
  }

  /**
   * @return classes
   */
  @Override
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
  public <E extends HTagElement> E addAttribute(String key, String value) {
    if (this.attrs == null) {
      this.attrs = new HashMap<String, String>();
    }
    this.attrs.put(key, value);
    this.browser.addChange("$C[%d].attr('%s','%s');", this.getId(), key, value);
    @SuppressWarnings("unchecked")
    E ethis = (E) this;
    return ethis;
  }

  /**
   * @return lista de atributos
   */
  @Override
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
  @Override
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
   * @return listener
   */
  public HListener getListener(String name) {
    if (this.events == null) {
      return null;
    }
    return this.events.get(name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getId() {
    return System.identityHashCode(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IBrowser getBrowser() {
    return (IBrowser) this.browser;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void finalize() throws Throwable {
    this.browser.removeElement(this.getId());
    super.finalize();
  }

}
