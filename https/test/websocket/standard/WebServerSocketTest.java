package websocket.standard;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

public class WebServerSocketTest {

  @Test
  public void test() throws IOException {
    Assert.assertEquals("s3pPLMBiTxaQ9kYGzzhZRbK+xOo=", StandardWebServerSocket
      .openHandshakeDefaultCode("dGhlIHNhbXBsZSBub25jZQ=="));
  }

}
