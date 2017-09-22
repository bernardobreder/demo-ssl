package browser.mock.html;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HtmlTagElement extends HtmlElement {

  /** Nome da tag */
  private String name;
  /** Atributos */
  private Map<String, String> attributes;
  /** Nós filhos */
  private List<HtmlElement> nodes;
  /** Conteúdo */
  private String content;

  /**
   * @param name
   */
  public HtmlTagElement(String name) {
    this.name = name;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @return the attributes
   */
  public Map<String, String> getAttributes() {
    return attributes;
  }

  /**
   * Indica se tem o atributo
   * 
   * @param attribute
   * @return tem o atributo
   */
  public boolean hasAttribute(String attribute) {
    if (this.attributes == null) {
      return false;
    }
    return this.attributes.get(attribute) != null;
  }

  /**
   * Indica se tem o atributo
   * 
   * @param attribute
   * @return tem o atributo
   */
  public String getAttribute(String attribute) {
    if (this.attributes == null) {
      return null;
    }
    return this.attributes.get(attribute);
  }

  /**
   * Adiciona ou modifica um atributo
   * 
   * @param key
   * @param value
   * @return owner
   */
  public HtmlTagElement setAttribute(String key, String value) {
    if (this.attributes == null) {
      this.attributes = new HashMap<String, String>();
    }
    this.attributes.put(key, value);
    return this;
  }

  /**
   * @param name the name to set
   * @return this
   */
  public HtmlTagElement setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * @param content the content to set
   * @return this
   */
  public HtmlTagElement setContent(String content) {
    this.content = content;
    return this;
  }

  /**
   * @param input
   * @return this
   * @throws IOException
   * @throws UnsupportedEncodingException
   */
  public HtmlTagElement setContent(InputStream input) throws IOException {
    StringBuilder sb = new StringBuilder();
    byte[] bytes = new byte[8 * 1024];
    for (int n; (n = input.read(bytes)) != -1;) {
      sb.append(new String(bytes, 0, n, "utf-8"));
    }
    return this.setContent(sb.toString());
  }

  /**
   * @return the nodes
   */
  public List<HtmlElement> getNodes() {
    return nodes;
  }

  /**
   * Indica se tem filho
   * 
   * @return se tem filho
   */
  public boolean hasNodes() {
    return this.nodes != null;
  }

  /**
   * Adiciona uma tag
   * 
   * @param node
   * @return owner
   */
  public HtmlTagElement addNode(HtmlElement node) {
    if (this.nodes == null) {
      this.nodes = new ArrayList<HtmlElement>();
    }
    this.nodes.add(node);
    return this;
  }

  /**
   * Adiciona uma tag
   * 
   * @param node
   * @param index
   * @return owner
   */
  public HtmlTagElement addNode(HtmlElement node, int index) {
    if (this.nodes == null) {
      this.nodes = new ArrayList<HtmlElement>();
    }
    this.nodes.add(index, node);
    return this;
  }

  /**
   * @param name
   * @return the nodes
   */
  public List<HtmlElement> getNodesByTagName(String name) {
    List<HtmlElement> list = new ArrayList<HtmlElement>();
    for (HtmlElement node : this.nodes) {
      if (node instanceof HtmlTagElement) {
        HtmlTagElement tagElement = (HtmlTagElement) node;
        if (tagElement.name.equals(name)) {
          list.add(node);
        }
      }
    }
    return list;
  }

  /**
   * @param name
   * @return the nodes
   */
  public HtmlTagElement getNodeByTagName(String name) {
    for (HtmlElement node : this.nodes) {
      if (node instanceof HtmlTagElement) {
        HtmlTagElement tagElement = (HtmlTagElement) node;
        if (tagElement.name.equals(name)) {
          return tagElement;
        }
      }
    }
    return null;
  }

  /**
   * @param names
   * @return the nodes
   */
  public HtmlTagElement getNodeByTagNames(String... names) {
    HtmlTagElement root = this;
    for (int n = 0; n < names.length; n++) {
      if (root.nodes == null) {
        return null;
      }
      String name = names[n];
      boolean found = false;
      for (HtmlElement node : root.nodes) {
        if (node instanceof HtmlTagElement) {
          HtmlTagElement tagElement = (HtmlTagElement) node;
          if (tagElement.name.equals(name)) {
            root = tagElement;
            found = true;
            break;
          }
        }
      }
      if (!found) {
        return null;
      }
    }
    return root;
  }

  /**
   * @param attribute
   * @param value
   * @return the nodes
   */
  public List<HtmlElement> getNodesByAttributeValue(String attribute,
    String value) {
    List<HtmlElement> list = new ArrayList<HtmlElement>();
    for (HtmlElement node : this.nodes) {
      if (node instanceof HtmlTagElement) {
        HtmlTagElement tagElement = (HtmlTagElement) node;
        if (tagElement.attributes != null) {
          String attValue = tagElement.attributes.get(attribute);
          if (attValue != null && attValue.equals(value)) {
            list.add(node);
          }
        }
      }
    }
    return list;
  }

  /**
   * @param attribute
   * @param value
   * @return the nodes
   */
  public List<HtmlElement> getNodesByAttributeKey(String attribute, String value) {
    List<HtmlElement> list = new ArrayList<HtmlElement>();
    for (HtmlElement node : this.nodes) {
      if (node instanceof HtmlTagElement) {
        HtmlTagElement tagElement = (HtmlTagElement) node;
        if (tagElement.attributes != null) {
          String attValue = tagElement.attributes.get(attribute);
          if (attValue != null) {
            list.add(node);
          }
        }
      }
    }
    return list;
  }

  /**
   * @param attribute
   * @param value
   * @return the nodes
   */
  public List<HtmlElement> getNodesByAttributeContainValue(String attribute,
    String value) {
    List<HtmlElement> list = new ArrayList<HtmlElement>();
    for (HtmlElement node : this.nodes) {
      if (node instanceof HtmlTagElement) {
        HtmlTagElement tagElement = (HtmlTagElement) node;
        if (tagElement.attributes != null) {
          String attValue = tagElement.attributes.get(attribute);
          if (attValue != null && attValue.contains(value)) {
            list.add(node);
          }
        }
      }
    }
    return list;
  }

  /**
   * @param attribute
   * @param value
   * @return the nodes
   */
  public List<HtmlElement> getNodesByAttributeMatchValue(String attribute,
    String value) {
    List<HtmlElement> list = new ArrayList<HtmlElement>();
    for (HtmlElement node : this.nodes) {
      if (node instanceof HtmlTagElement) {
        HtmlTagElement tagElement = (HtmlTagElement) node;
        if (tagElement.attributes != null) {
          String attValue = tagElement.attributes.get(attribute);
          if (attValue != null && attValue.matches(value)) {
            list.add(node);
          }
        }
      }
    }
    return list;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuilder atts = new StringBuilder();
    if (this.attributes != null) {
      for (String key : this.attributes.keySet()) {
        atts.append(' ');
        atts.append(key);
        atts.append('=');
        atts.append('\"');
        atts.append(this.attributes.get(key));
        atts.append('\"');
      }
    }
    StringBuilder list = new StringBuilder();
    if (this.nodes != null) {
      for (HtmlElement node : this.nodes) {
        list.append(node.toString());
      }
    }
    else if (this.content != null) {
      list.append(this.content);
    }
    return "<" + this.name + atts + ">" + list + "</" + this.name + ">";
  }

}
