package ws.standard;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

public class StandardWebServerSocketTest {

  @Test
  public void test() throws IOException {
    Assert.assertEquals("s3pPLMBiTxaQ9kYGzzhZRbK+xOo=", WebServerSocket
      .openHandshakeDefaultCode("dGhlIHNhbXBsZSBub25jZQ=="));
  }

}
