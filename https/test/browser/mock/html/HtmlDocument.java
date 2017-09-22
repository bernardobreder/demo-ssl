package browser.mock.html;

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

import util.Bytes;

public class HtmlDocument {

  private final HtmlTagElement root = new HtmlTagElement("document");
  private final HtmlTagElement html;
  private final HtmlTagElement head;
  private final HtmlTagElement body;

  public HtmlDocument(InputStream in) throws SAXException,
    ParserConfigurationException, IOException {
    in =
      new WrappedInputStream(Bytes.getUtf8Bytes("<html>"), in, Bytes
        .getUtf8Bytes("</html>"));
    root.addNode(this.html =
      new HtmlTagElement("html")
        .addNode(this.head = new HtmlTagElement("head")).addNode(
          this.body = new HtmlTagElement("body")));
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
  public HtmlTagElement getDocumentNode() {
    return root;
  }

  public class HtmlHandler extends DefaultHandler {

    private LinkedList<HtmlElement> nodes = new LinkedList<HtmlElement>();

    private boolean heading = false;

    @Override
    public void startElement(String uri, String localName, String qName,
      Attributes attributes) throws SAXException {
      if (qName.equals("head")) {
        heading = true;
      }
      HtmlTagElement node;
      if (qName.equals("html")) {
        node = html;
      }
      else if (qName.equals("head")) {
        node = head;
      }
      else if (qName.equals("body")) {
        node = body;
      }
      else {
        node = new HtmlTagElement(qName);
        HtmlTagElement peek = (HtmlTagElement) nodes.peek();
        if (peek != null) {
          peek.addNode(node);
        }
        else {
          if (node != html && node != head && node != body) {
            if (heading) {
              head.addNode(node);
            }
            else {
              body.addNode(node);
            }
          }
        }
        nodes.push(node);
      }
      for (int n = 0; n < attributes.getLength(); n++) {
        String key = attributes.getQName(n);
        String value = attributes.getValue(n);
        node.setAttribute(key, value);
      }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
      throws SAXException {
      if (qName.equals("head")) {
        heading = false;
      }
      if (!qName.equals("html") && !qName.equals("head")
        && !qName.equals("body")) {
        nodes.remove();
      }
    }

    @Override
    public void characters(char[] ch, int start, int length)
      throws SAXException {
      String text = new String(ch, start, length);
      if (text.trim().length() > 0) {
        HtmlTagElement peek = (HtmlTagElement) nodes.peek();
        peek.setContent(text);
      }
    }

  }

  private static class WrappedInputStream extends InputStream {

    private final byte[] prefix;

    private final InputStream input;

    private final byte[] suffix;

    private int prefixIndex;

    private int suffixIndex;

    public WrappedInputStream(byte[] prefix, InputStream input, byte[] suffix) {
      this.prefix = prefix;
      this.input = input;
      this.suffix = suffix;
    }

    @Override
    public int read() throws IOException {
      if (prefix != null && prefixIndex < prefix.length) {
        return prefix[prefixIndex++];
      }
      int n = input.read();
      if (n >= 0) {
        return n;
      }
      if (suffix != null && suffixIndex < suffix.length) {
        return suffix[suffixIndex++];
      }
      return -1;
    }

    @Override
    public int available() throws IOException {
      int available = input.available();
      if (prefix != null && prefixIndex < prefix.length) {
        return prefix.length - prefixIndex + available + suffix.length;
      }
      if (available > 0) {
        return available;
      }
      if (suffix != null && suffixIndex < suffix.length) {
        return suffix.length - suffixIndex;
      }
      return 0;
    }

    @Override
    public void close() throws IOException {
      input.close();
    }

  }

}
