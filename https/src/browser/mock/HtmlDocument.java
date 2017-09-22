package browser.mock;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import util.XmlNode;

public class HtmlDocument {

  private XmlNode documentNode = new XmlNode("document");

  public HtmlDocument(InputStream in) throws SAXException,
    ParserConfigurationException, IOException {
    SAXParserFactory spf = SAXParserFactory.newInstance();
    SAXParser saxParser = spf.newSAXParser();
    XMLReader xmlReader = saxParser.getXMLReader();
    xmlReader.setContentHandler(new HtmlHandler());
    InputSource source = new InputSource(in);
    source.setEncoding("utf-8");
    xmlReader.parse(source);
  }

  /**
   * @return the documentElem
   */
  public XmlNode getDocumentNode() {
    return documentNode;
  }

  public class HtmlHandler extends DefaultHandler {

    private LinkedList<XmlNode> nodes = new LinkedList<XmlNode>();

    @Override
    public void startElement(String uri, String localName, String qName,
      Attributes attributes) throws SAXException {
      XmlNode node = new XmlNode(qName);
      for (int n = 0; n < attributes.getLength(); n++) {
        String key = attributes.getQName(n);
        String value = attributes.getValue(n);
        node.setAttribute(key, value);
      }
      XmlNode peek = nodes.peek();
      if (peek != null) {
        peek.addNode(node);
      }
      nodes.push(node);
    }

    @Override
    public void endElement(String uri, String localName, String qName)
      throws SAXException {
      if (nodes.size() == 1) {
        documentNode.addNode(nodes.peek());
      }
      nodes.pop();
    }

    @Override
    public void characters(char[] ch, int start, int length)
      throws SAXException {
      String text = new String(ch, start, length);
      if (text.trim().length() > 0) {
        XmlNode peek = nodes.peek();
        peek.setContent(text);
      }
    }

  }

}
