package jtweb;

import org.junit.After;
import org.junit.Before;

public class Test {

  private IAppServer server;

  private IAppClient client;

  @org.junit.Test
  public void test() {

  }

  @Before
  public void before() {
    server = null;
    server.start();
  }

  @After
  public void after() {
    server.stop();
  }

}
