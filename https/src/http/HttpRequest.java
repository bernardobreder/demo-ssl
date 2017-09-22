package http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

import sun.misc.BASE64Decoder;
import util.Bytes;
import util.JsonInputStream;
import util.JsonInputStream.SyntaxException;
import util.StringUtil;

/**
 *
 *
 * @author bernardobreder
 */
public class HttpRequest {

  /** Metodo */
  private final String method;
  /** Url */
  private String url;
  /** Protocolo */
  private final String protocol;
  /** Sessão do usuário */
  private String session;
  /** Header */
  private final Map<String, String> headers;
  /** InputStream */
  private final InputStream input;
  /** Parametros */
  private Map<String, Object> params;

  /**
   * @param method
   * @param url
   * @param protocol
   * @param input
   */
  public HttpRequest(String method, String url, String protocol,
    InputStream input) {
    super();
    this.method = method;
    this.url = url;
    this.protocol = protocol;
    this.input = input;
    this.headers = new TreeMap<String, String>();
  }

  /**
   * @param url
   */
  public HttpRequest(String url) {
    String[] args = StringUtil.splitUrlAndParam(url);
    this.url = args[0];
    this.headers = StringUtil.splitUrlParams(args[1]);
    this.protocol = null;
    this.method = null;
    this.input = null;
  }

  /**
   * @param input
   * @throws IOException
   */
  public HttpRequest(InputStream input) throws IOException {
    this.headers = new TreeMap<String, String>();
    StringBuilder sb = new StringBuilder(512);
    int step = 0;
    for (;;) {
      int n = input.read();
      if (n < 0) {
        throw new EOFException();
      }
      else if (n == '\n') {
        step++;
      }
      else if (n == '\r') {
      }
      else {
        step = 0;
      }
      sb.append((char) n);
      if (step == 2) {
        break;
      }
    }
    String request = sb.toString().trim();
    if (request.length() == 0) {
      throw new IllegalArgumentException();
    }
    String[] lines = request.split("\n");
    String[] arg0s = lines[0].split(" ");
    this.method = arg0s[0].trim();
    this.url = arg0s[1].trim();
    this.protocol = arg0s[2].trim();
    for (int n = 1; n < lines.length; n++) {
      String[] split = lines[n].trim().split(": ");
      String key = split[0];
      String value = split[1];
      this.headers.put(key.toLowerCase(), value);
    }
    String authorization = this.headers.get("authorization");
    if (authorization != null && authorization.startsWith("Basic ")) {
      authorization =
        new String(new BASE64Decoder().decodeBuffer(authorization
          .substring("Basic ".length())), "utf-8");
      int indexOf = authorization.indexOf(':');
      String user = authorization.substring(0, indexOf);
      String pass = authorization.substring(indexOf + 1);
      String session =
        Bytes.toHex(Bytes.md5(user)) + Bytes.toHex(Bytes.md5(pass));
      this.headers.put("auth-user", user);
      this.headers.put("auth-pass", pass);
      this.headers.put("session", session);
    }
    if (this.headers.containsKey("content-length")) {
      int len = Integer.parseInt(this.headers.get("content-length"));
      ByteArrayOutputStream out = new ByteArrayOutputStream(len);
      byte[] bytes = new byte[1024];
      while (len > 0) {
        int n = input.read(bytes, 0, Math.min(len, 1024));
        if (n < 0) {
          throw new EOFException();
        }
        out.write(bytes, 0, n);
        len -= n;
      }
      this.input = new ByteArrayInputStream(out.toByteArray());
    }
    else {
      this.input = input;
    }
  }

  /**
   * @return indica se possui autenticação
   */
  public boolean hasAuthorization() {
    return this.headers.containsKey("authorization");
  }

  /**
   * @return usuário
   * @throws IOException
   */
  public String getUsername() throws IOException {
    if (!this.hasAuthorization()) {
      return null;
    }
    if (!this.headers.containsKey("auth-user")) {
      String authorization = this.headers.get("authorization");
      authorization =
        new String(new BASE64Decoder().decodeBuffer(authorization
          .substring("Basic ".length())), "utf-8");
      int indexOf = authorization.indexOf(':');
      String user = authorization.substring(0, indexOf);
      String pass = authorization.substring(indexOf + 1);
      String session =
        Bytes.toHex(Bytes.md5(user)) + Bytes.toHex(Bytes.md5(pass));
      this.headers.put("auth-user", user);
      this.headers.put("auth-pass", pass);
      this.headers.put("session", session);
    }
    return this.headers.get("auth-user");
  }

  /**
   * @return senha
   * @throws IOException
   */
  public String getPassword() throws IOException {
    this.getUsername();
    return this.headers.get("auth-pass");
  }

  /**
   * Retorna a extensão da url
   * 
   * @return extensão da url
   */
  public String getExtensionUrl() {
    int index = url.lastIndexOf('.');
    if (index < 0) {
      return "";
    }
    else {
      return url.substring(index);
    }
  }

  /**
   * @return the method
   */
  public String getMethod() {
    return method;
  }

  /**
   * @return the url
   */
  public String getUrl() {
    int index = url.indexOf('?');
    if (index >= 0) {
      this.getParameters();
      url = url.substring(0, index);
    }
    return url;
  }

  /**
   * @return the url
   */
  public String getLastUrl() {
    String url = this.url;
    int index = url.lastIndexOf('/');
    if (index < 0) {
      return url;
    }
    return url.substring(index + 1);
  }

  /**
   * @return the protocol
   */
  public String getProtocol() {
    return protocol;
  }

  /**
   * @param flag se for true, será criado a sessão caso não exista. Se for
   *        false, será retornado a sessão do usuário, existindo ou não.
   * @return sessão do usuário
   */
  public String getSession(boolean flag) {
    if (flag && session == null) {
      session = Bytes.session(64);
    }
    return session;
  }

  /**
   * @param session
   */
  public void setSession(String session) {
    this.session = session;
  }

  /**
   * @return the headers
   */
  public Map<String, String> getHeaders() {
    return headers;
  }

  /**
   * @param key
   * @param defaultValue
   * @return the headers
   */
  public String getHeader(String key, String defaultValue) {
    String value = headers.get(key);
    if (value == null) {
      value = defaultValue;
    }
    return value;
  }

  /**
   * @param key
   * @return the headers
   */
  public String getHeader(String key) {
    return headers.get(key);
  }

  /**
   * @param key
   * @return the headers
   */
  public String checkHeaderString(String key) {
    String value = headers.get(key);
    if (value == null) {
      throw new IllegalArgumentException("not found " + key);
    }
    return value;
  }

  /**
   * @param key
   * @return the headers
   */
  public int checkHeaderInt(String key) {
    String value = headers.get(key);
    if (value == null) {
      throw new IllegalArgumentException("not found " + key);
    }
    return Integer.parseInt(value);
  }

  /**
   * @param key
   * @return valor do parametro ou nulo
   */
  public Object getParameter(String key) {
    return this.getParameters().get(key);
  }

  /**
   * @return parametros
   */
  public Map<String, Object> getParameters() {
    if (this.params == null) {
      this.params = new TreeMap<String, Object>();
      if (this.method.equals("GET")) {
        String url = this.url;
        int index = url.indexOf('?');
        if (index >= 0) {
          url = url.substring(index + 1);
          String[] params = url.split("&");
          for (String param : params) {
            String[] item = param.split("=");
            String key = item[0];
            Object value = item[1];
            try {
              value = new JsonInputStream(value.toString()).readObject();
            }
            catch (SyntaxException e) {
            }
            this.params.put(key, value);
          }
        }
      }
    }
    return this.params;
  }

  /**
   * @return the input
   */
  public InputStream getInput() {
    return input;
  }

  /**
   * @return linha
   * @throws IOException
   */
  public String readLine() throws IOException {
    InputStream in = this.input;
    StringBuilder sb = new StringBuilder();
    for (;;) {
      int c = in.read();
      if (c <= 0x7F) {
      }
      else if ((c >> 5) == 0x6) {
        int i2 = in.read();
        if (i2 < 0) {
          break;
        }
        c = (((c & 0x1F) << 6) + (i2 & 0x3F));
      }
      else {
        int i2 = in.read();
        if (i2 < 0) {
          break;
        }
        int i3 = in.read();
        if (i3 < 0) {
          break;
        }
        c = (((c & 0xF) << 12) + ((i2 & 0x3F) << 6) + (i3 & 0x3F));
      }
      if (c == '\n') {
        break;
      }
      else if (c == '\r') {
        continue;
      }
      sb.append((char) c);
    }
    return sb.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return this.getUrl();
  }

}
