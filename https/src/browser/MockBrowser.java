package browser;

import http.HttpSocket;
import http.mock.MockHttpSocket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;

import util.Bytes;
import util.XmlNode;

public class MockBrowser {

  /** Indica se o navegador está fechado */
  private boolean closed;
  /** Usuário */
  private String username;
  /** Senha */
  private String password;

  /**
   * Construtor
   */
  public MockBrowser() {
  }

  /**
   * @param path
   * @return socket
   */
  public HttpSocket open(String path) {
    this.checkClosed();
    MockHttpSocket socket = new MockHttpSocket() {
      @Override
      public void close() throws IOException {
        byte[] bytes = this.getOutputStream().toByteArray();
        String content = new String(bytes, "utf-8");
        try {
          openContent(content);
        }
        catch (ParseException e) {
          e.printStackTrace();
        }
      }
    };
    socket.write("GET " + path + " HTTP/1.1\r\n");
    socket.write("Host: " + "localhost:test" + "\r\n");
    socket.write("Connection: " + "keep-alive" + "\r\n");
    if (this.username != null && this.password != null) {
      socket.write("Authorization: "
        + Bytes.toBase64(Bytes.getUtf8Bytes(username + ":" + password))
        + "\r\n");
    }
    socket.write("\r\n");
    socket.flush();
    return socket;
  }

  protected void openContent(String content) throws ParseException, IOException {
    XmlNode root =
      new XmlNode(new ByteArrayInputStream(content.getBytes("utf-8")));
  }

  public MockBrowser requireTitle(String expectedTitle) {
    return this.checkClosed();
  }

  public MockBrowser setAuthorization(String username, String password) {
    this.username = username;
    this.password = password;
    return this;
  }

  public void close() {
    this.closed = true;
  }

  protected MockBrowser checkClosed() {
    if (closed) {
      throw new IllegalStateException("browser closed");
    }
    return this;
  }

}
