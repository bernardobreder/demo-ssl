package http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

import util.Bytes;
import util.XmlNode;

/**
 * Classe de responsta do Http
 *
 * @author bernardobreder
 */
public class HttpResponse {

  /** Saída */
  private final OutputStream out;
  /** Comprimento do conteúdo */
  private long contentLength = -1;
  /** Tipo do Conteudo */
  private String contentType;
  /** Encoding do Conteudo */
  private String contentEncoding;
  /** Indica se já está respondendo */
  private boolean answering;
  /** Requisições */
  private final HttpRequest request;
  /** Saída */
  private final StringBuilder hout;
  /** Bytes */
  private ByteArrayOutputStream bout = new ByteArrayOutputStream();

  /**
   * @param request
   * @param out
   * @throws IOException
   */
  public HttpResponse(HttpRequest request, OutputStream out) throws IOException {
    this.request = request;
    this.hout = new StringBuilder();
    this.out = out;
    String encoding = request.getHeader("accept-encoding", "");
    if (encoding.contains("deflate")) {
      this.contentEncoding = "deflate";
    }
    else if (encoding.contains("gzip")) {
      this.contentEncoding = "gzip";
    }
  }

  /**
   * @param key
   * @param value
   * @return this
   */
  public HttpResponse setCookie(String key, Object value) {
    this.hout.append("Set-Cookie: " + key + "=" + value + "\r\n");
    return this;
  }

  /**
   * @return the contentLength
   */
  public long getContentLength() {
    return contentLength;
  }

  /**
   * @param contentLength the contentLength to set
   * @return this
   */
  public HttpResponse setContentLength(long contentLength) {
    this.contentLength = contentLength;
    return this;
  }

  /**
   * @return the contentType
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * @param contentType the contentType to set
   * @return this
   */
  public HttpResponse setContentType(String contentType) {
    this.contentType = contentType;
    return this;
  }

  /**
   * @throws IOException
   */
  public void answerSuccess() throws IOException {
    hout.append("HTTP/1.1 200 OK\r\n");
  }

  /**
   * @return this
   * @throws IOException
   */
  public HttpResponse answerBadRequest() throws IOException {
    hout.append("HTTP/1.1 400 Error\r\n");
    return this;
  }

  /**
   * @return this
   * @throws IOException
   */
  public HttpResponse answerNotFound() throws IOException {
    hout.append("HTTP/1.1 404 Error\r\n");
    return this;
  }

  /**
   * @param e
   * @return this
   * @throws IOException
   */
  public HttpResponse answerInternalServerError(Throwable e) throws IOException {
    hout.append("HTTP/1.1 500 Error\r\n");
    return this;
  }

  /**
   * @return this
   * @throws IOException
   */
  public HttpResponse answerNotAuthorized() throws IOException {
    hout.append("HTTP/1.1 401 Unauthorized\r\n");
    hout.append("WWW-Authenticate: Basic realm=\"Username and Password\"\r\n");
    return this;
  }

  /**
   * Indica se a requisição já foi atendida
   * 
   * @return já respondeu a requisição
   */
  public boolean isAnswered() {
    return answering;
  }

  /**
   * @param bytes
   * @return this
   * @throws IOException
   */
  public HttpResponse writeBytes(byte[] bytes) throws IOException {
    this.writeBytes(bytes, 0, bytes.length);
    return this;
  }

  /**
   * @param bytes
   * @param off
   * @param len
   * @return this
   * @throws IOException
   */
  public HttpResponse writeBytes(byte[] bytes, int off, int len)
    throws IOException {
    this.bout.write(bytes, off, len);
    return this;
  }

  /**
   * @param text
   * @return this
   * @throws IOException
   */
  public HttpResponse writeString(String text) throws IOException {
    this.writeBytes(Bytes.getUtf8Bytes(text));
    return this;
  }

  /**
   * @return this
   * @throws IOException
   */
  public HttpResponse writeHtml5Tag() throws IOException {
    this.writeString("<!DOCTYPE html>");
    return this;
  }

  /**
   * @param input
   * @return this
   * @throws IOException
   */
  public HttpResponse writeInputStream(InputStream input) throws IOException {
    try {
      byte[] bytes = new byte[1024];
      for (int n; (n = input.read(bytes)) != -1;) {
        this.writeBytes(bytes, 0, n);
      }
    }
    finally {
      input.close();
    }
    return this;
  }

  /**
   * @param root
   * @return this
   * @throws IOException
   */
  public HttpResponse writeXmlNode(XmlNode root) throws IOException {
    if (this.request.getHeader("test", "false").equals("true")) {
      List<XmlNode> list = new ArrayList<XmlNode>();
      list.add(root);
      for (int n = 0; n < list.size(); n++) {
        XmlNode node = list.get(n);
        List<XmlNode> children = node.getNodes();
        if (children != null) {
          list.addAll(children);
        }
        if (node.getName().equals("script") || node.getName().equals("style")) {
          node.setContent(node.getContent());
        }
      }
    }
    this.writeBytes(root.getBytes());
    return this;
  }

  /**
   * @return this
   * @throws IOException
   */
  public HttpResponse flush() throws IOException {
    Calendar c = Calendar.getInstance();
    c.setTime(new Date());
    Date date = c.getTime();
    c.add(Calendar.DAY_OF_MONTH, 1);
    Date expires = c.getTime();
    Date lastModified = c.getTime();
    hout.append("Cache-Control: public, max-age=30\r\n");
    hout.append("Connection: keep-alive\r\n");
    // hout.append("Connection: close\r\n");
    hout.append("Date: " + date + "\r\n");
    hout.append("Expires: " + expires + "\r\n");
    hout.append("Last-Modified: " + lastModified + "\r\n");
    if (this.contentType != null) {
      hout.append("Content-Type: " + this.contentType + "\r\n");
    }
    if (this.contentEncoding != null) {
      hout.append("Content-Encoding: " + this.contentEncoding + "\r\n");
      bout = convert(bout, this.contentEncoding);
    }
    hout.append("Content-Length: " + bout.size() + "\r\n");
    hout.append("\r\n");
    this.out.write(Bytes.getUtf8Bytes(hout.toString()));
    this.out.write(this.bout.toByteArray());
    this.out.flush();
    return this;
  }

  /**
   * @param bout
   * @param encoding
   * @return bytes convertido
   * @throws IOException
   */
  private static ByteArrayOutputStream convert(ByteArrayOutputStream bout,
    String encoding) throws IOException {
    if (encoding.equals("gzip")) {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      GZIPOutputStream out = new GZIPOutputStream(bytes);
      out.write(bout.toByteArray());
      out.close();
      return bytes;
    }
    else if (encoding.equals("deflate")) {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      DeflaterOutputStream out = new DeflaterOutputStream(bytes);
      out.write(bout.toByteArray());
      out.close();
      return bytes;
    }
    else {
      return bout;
    }
  }

}
