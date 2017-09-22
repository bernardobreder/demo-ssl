import html.primitive.HDiv;
import html.primitive.HTextHeader;
import http.HttpRequest;
import http.HttpResponse;
import http.mock.MockHttpServerSocket;

import java.io.IOException;

import org.junit.Before;

import app.AbstractApplicationServer;
import app.ServletException;
import browser.mock.MockBrowser;
import demo.IndexHtml;

public class Test {

  private AbstractApplicationServer app;
  private MockBrowser browser;

  @Before
  private void before() {
    this.app = new MyAppServer(new MockHttpServerSocket()).start();
    this.browser = new MockBrowser();
  }

  @Test
  public void test() {

  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    AbstractApplicationServer app =
      new MyAppServer(new MockHttpServerSocket()).start();
    MockBrowser browser = new MockBrowser();
    browser.open("/");
    browser.requireTitle("Hello");
    // app.addSocket(browser.open("/")).waitForIdle();
    browser.close();
    app.close();
  }

  public static class MyAppServer extends AbstractApplicationServer {

    public MyAppServer(MockHttpServerSocket server) {
      super(server);
    }

    @Override
    public void servlet(HttpRequest req, HttpResponse resp) throws IOException,
      ServletException {
      if (req.getUrl().equals("/")) {
        resp.writeBytes(new IndexHtml().getBytes());
      }
    }
  }

  public static class HomePage extends HDiv {

    public HomePage() {
      this.addElement(new HTextHeader(1, "Hello"));
    }

  }

}
