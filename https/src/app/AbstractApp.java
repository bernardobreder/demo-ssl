package app;

import http.HttpRequest;
import http.HttpResponse;
import http.mock.MockHttpServerSocket;

import java.io.IOException;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public abstract class AbstractApp implements Runnable {

  private final MockHttpServerSocket server;

  public AbstractApp(MockHttpServerSocket server) {
    this.server = server;
  }

  public abstract void servlet(HttpRequest req, HttpResponse resp)
    throws IOException, ServletException;

  /**
   * {@inheritDoc}
   */
  @Override
  public void run() {
  }

}
