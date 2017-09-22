package demo.bandeirabr;

import html.javascript.JavaScript;

import java.io.IOException;

import ws.WebSocket;
import browser.Browser;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public class BandeiraBrowserTest extends Browser {

  /**
   * @param socket
   * @param javascript
   * @throws IOException
   */
  public BandeiraBrowserTest(WebSocket socket, JavaScript javascript)
    throws IOException {
    super(socket, javascript);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void init() {
    this.setRoot(new ScenariumFrame());
  }

  //
  //  /**
  //   * @param server
  //   * @throws IOException
  //   */
  //  public static void main(WebServerSocket server) throws IOException {
  //    final Map<String, Browser> browsers = new TreeMap<String, Browser>();
  //    try {
  //      for (;;) {
  //        WebSocket socket = server.accept();
  //        final String session = readSession(socket);
  //        if (session == null) {
  //          socket.close();
  //          continue;
  //        }
  //        boolean hasBrowser = browsers.containsKey(session);
  //        Browser browser = browsers.get(session);
  //        if (browser == null) {
  //          browser = new BandeiraBrowserTest(socket, new StandardJavaScript());
  //          browsers.put(session, browser);
  //        }
  //        if (hasBrowser) {
  //          browser.restore(socket);
  //        }
  //        else {
  //          browser.start();
  //        }
  //      }
  //    }
  //    catch (Throwable e) {
  //      e.printStackTrace();
  //    }
  //    finally {
  //      server.close();
  //    }
  //  }

  private static String readSession(WebSocket socket) throws IOException {
    String session = socket.readMessage();
    if (session == null) {
      return null;
    }
    if (!session.startsWith("session:")) {
      return null;
    }
    return session.substring("session:".length()).trim();
  }

}
