package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Estrutura simples de Xml para fazer parse. O parse pode ser feito pelo
 * construtor da classe.
 *
 * @author bernardobreder
 *
 */
public class XmlNode {

  /** Pai */
  private XmlNode parent;
  /** Nome da tag */
  private String name;
  /** Atributos */
  private Map<String, String> attributes;
  /** Nós filhos */
  private List<XmlNode> nodes;
  /** Conteúdo */
  private String content;

  /**
   * @param name
   */
  public XmlNode(String name) {
    this.name = name;
  }

  /**
   * @param input
   * @throws ParseException
   * @throws IOException
   */
  public XmlNode(InputStream input) throws ParseException, IOException {
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      InputSource source = new InputSource(input);
      source.setEncoding("utf-8");
      Document document = db.parse(source);
      if (document.getFirstChild() instanceof Element) {
        Element element = (Element) document.getFirstChild();
        this.read(element);
      }
    }
    catch (ParserConfigurationException e) {
      throw new ParseException(e.getMessage(), 0);
    }
    catch (SAXException e) {
      throw new ParseException(e.getMessage(), 0);
    }
  }

  /**
   * @param element
   */
  private XmlNode(Element element) {
    this.read(element);
  }

  /**
   * @param root
   */
  private void read(Element root) {
    this.name = root.getNodeName();
    NamedNodeMap atts = root.getAttributes();
    if (atts != null) {
      int size = atts.getLength();
      if (size > 0) {
        this.attributes = new HashMap<String, String>(size);
      }
      for (int n = 0; n < size; n++) {
        Node node = atts.item(n);
        if (node instanceof Attr) {
          Attr attr = (Attr) node;
          this.attributes.put(attr.getName(), attr.getValue());
        }
      }
    }
    String content = root.getTextContent().trim();
    if (content.length() > 0) {
      this.content = content;
    }
    NodeList nodes = root.getChildNodes();
    if (nodes != null) {
      int size = nodes.getLength();
      for (int n = 0; n < size; n++) {
        Node node = nodes.item(n);
        if (node instanceof Element) {
          Element element = (Element) node;
          if (this.nodes == null) {
            this.nodes = new ArrayList<XmlNode>();
          }
          XmlNode child = new XmlNode(element);
          child.parent = this;
          this.nodes.add(child);
        }
      }
    }
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
  public XmlNode setAttribute(String key, String value) {
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
  public XmlNode setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * @param content the content to set
   * @return this
   */
  public XmlNode setContent(String content) {
    // content = content.replace("<", "&lt;");
    // content = content.replace(">", "&gt;");
    // content = content.replace("&", "&amp;");
    this.content = content;
    return this;
  }

  /**
   * @param input
   * @return this
   * @throws IOException
   * @throws UnsupportedEncodingException
   */
  public XmlNode setContent(InputStream input) throws IOException {
    String content = readStream(input);
    return this.setContent(content);
  }

  /**
   * @param input
   * @param variables
   * @return this
   * @throws IOException
   * @throws UnsupportedEncodingException
   */
  public XmlNode setContent(InputStream input, Map<String, Object> variables)
    throws IOException {
    try {
      String content = readStream(input);
      for (Entry<String, Object> entry : variables.entrySet()) {
        String value = entry.getValue().toString();
        content = content.replace(entry.getKey(), value);
      }
      return this.setContent(content);
    }
    finally {
      input.close();
    }
  }

  /**
   * @param input
   * @return conteudo
   * @throws IOException
   */
  protected String readStream(InputStream input) throws IOException {
    try {
      StringBuilder sb = new StringBuilder();
      byte[] bytes = new byte[1024];
      for (int n; (n = input.read(bytes)) != -1;) {
        sb.append(new String(bytes, 0, n, "utf-8"));
      }
      String content = sb.toString();
      return content;
    }
    finally {
      input.close();
    }
  }

  /**
   * @return the nodes
   */
  public List<XmlNode> getNodes() {
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
  public XmlNode addNode(XmlNode node) {
    if (this.nodes == null) {
      this.nodes = new ArrayList<XmlNode>();
    }
    node.parent = this;
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
  public XmlNode addNode(XmlNode node, int index) {
    if (this.nodes == null) {
      this.nodes = new ArrayList<XmlNode>();
    }
    node.parent = this;
    this.nodes.add(index, node);
    return this;
  }

  /**
   * @param name
   * @return the nodes
   */
  public List<XmlNode> getNodesByTagName(String name) {
    List<XmlNode> list = new ArrayList<XmlNode>();
    for (XmlNode node : this.nodes) {
      if (node.name.equals(name)) {
        list.add(node);
      }
    }
    return list;
  }

  /**
   * @param name
   * @return the nodes
   */
  public XmlNode getNodeByTagName(String name) {
    for (XmlNode node : this.nodes) {
      if (node.name.equals(name)) {
        return node;
      }
    }
    return null;
  }

  /**
   * @param names
   * @return the nodes
   */
  public XmlNode getNodeByTagNames(String... names) {
    XmlNode root = this;
    for (int n = 0; n < names.length; n++) {
      if (root.nodes == null) {
        return null;
      }
      String name = names[n];
      boolean found = false;
      for (XmlNode node : root.nodes) {
        if (node.name.equals(name)) {
          root = node;
          found = true;
          break;
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
  public List<XmlNode> getNodesByAttributeValue(String attribute, String value) {
    List<XmlNode> list = new ArrayList<XmlNode>();
    for (XmlNode node : this.nodes) {
      if (node.attributes != null) {
        String attValue = node.attributes.get(attribute);
        if (attValue != null && attValue.equals(value)) {
          list.add(node);
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
  public List<XmlNode> getNodesByAttributeKey(String attribute, String value) {
    List<XmlNode> list = new ArrayList<XmlNode>();
    for (XmlNode node : this.nodes) {
      if (node.attributes != null) {
        String attValue = node.attributes.get(attribute);
        if (attValue != null) {
          list.add(node);
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
  public List<XmlNode> getNodesByAttributeContainValue(String attribute,
    String value) {
    List<XmlNode> list = new ArrayList<XmlNode>();
    for (XmlNode node : this.nodes) {
      if (node.attributes != null) {
        String attValue = node.attributes.get(attribute);
        if (attValue != null && attValue.contains(value)) {
          list.add(node);
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
  public List<XmlNode> getNodesByAttributeMatchValue(String attribute,
    String value) {
    List<XmlNode> list = new ArrayList<XmlNode>();
    for (XmlNode node : this.nodes) {
      if (node.attributes != null) {
        String attValue = node.attributes.get(attribute);
        if (attValue != null && attValue.matches(value)) {
          list.add(node);
        }
      }
    }
    return list;
  }

  /**
   * @return the parent
   */
  public XmlNode getParent() {
    return parent;
  }

  /**
   * @return the content
   */
  public String getContent() {
    return content;
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
      for (XmlNode node : this.nodes) {
        list.append(node.toString());
      }
    }
    else if (this.content != null) {
      list.append(this.content);
    }
    return "<" + this.name + atts + ">" + list + "</" + this.name + ">";
  }

  /**
   * @return bytes
   * @throws IOException
   */
  public byte[] getBytes() throws IOException {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    StringOutputStream string = new StringOutputStream(output);
    getBytes(string, this);
    return output.toByteArray();
  }

  /**
   * @param output
   * @param root
   * @throws IOException
   */
  protected static void getBytes(StringOutputStream output, XmlNode root)
    throws IOException {
    output.write('<');
    output.append(root.name);
    if (root.attributes != null) {
      for (String key : root.attributes.keySet()) {
        output.write(' ');
        output.append(key);
        output.write('=');
        output.write('\"');
        output.append(root.attributes.get(key));
        output.write('\"');
      }
    }
    output.write('>');
    if (root.nodes != null) {
      for (XmlNode node : root.nodes) {
        getBytes(output, node);
      }
    }
    else if (root.content != null) {
      output.append(root.content);
    }
    output.write('<');
    output.write('/');
    output.append(root.name);
    output.write('>');
  }

  /**
   * String para output stream
   * 
   * @author bernardobreder
   * 
   */
  private static class StringOutputStream extends OutputStream {

    /** Saída */
    private final OutputStream output;

    /**
     * Construtor
     * 
     * @param output
     */
    public StringOutputStream(OutputStream output) {
      this.output = output;
    }

    /**
     * Acrescenta uma string
     * 
     * @param text
     * @throws IOException
     */
    public void append(String text) throws IOException {
      int size = text.length();
      for (int n = 0; n < size; n++) {
        char c = text.charAt(n);
        if (c <= 0x7F) {
          output.write(c);
        }
        else if (c <= 0x7FF) {
          output.write(((c >> 6) & 0x1F) + 0xC0);
          output.write((c & 0x3F) + 0x80);
        }
        else {
          output.write(((c >> 12) & 0xF) + 0xE0);
          output.write(((c >> 6) & 0x3F) + 0x80);
          output.write((c & 0x3F) + 0x80);
        }
      }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(int n) throws IOException {
      output.write(n);
    }

  }

}
