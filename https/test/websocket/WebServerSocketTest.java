package websocket;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

import websocket.standard.DefaultWebServerSocket;

public class WebServerSocketTest {

  @Test
  public void test() throws IOException {
    Assert.assertEquals("s3pPLMBiTxaQ9kYGzzhZRbK+xOo=", DefaultWebServerSocket
      .openHandshakeDefaultCode("dGhlIHNhbXBsZSBub25jZQ=="));
  }

}
