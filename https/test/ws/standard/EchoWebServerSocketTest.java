package ws.standard;

import socket.jdk.StandardServerSocket;
import ws.IWsSocket;

public class EchoWebServerSocketTest {

  public static void main(String[] args) throws Exception {
    // InputStream keystoreInput = EchoWebServerSocketTest.class
    // .getResourceAsStream("/lig.pem");
    // char[] keystorepass = new char[] { 's', 'i', 'm', 'u', 'l', 'a', 't',
    // 'o', 'r' };
    // KeyStore keyStore =
    // StandardSSLServerSocket.getKeyStore(keystoreInput,
    // keystorepass);
    // SSLServerSocket server = StandardSSLServerSocket.create(9090,
    // keyStore,
    // keystorepass);
    WebServerSocket webServerSocket =
      new WebServerSocket(new StandardServerSocket(9090));
    try {
      for (;;) {
        IWsSocket socket = webServerSocket.accept();
        long time = System.currentTimeMillis();
        for (int n = 0; n < 1024; n++) {
          String message = socket.readMessage();
          if (message == null) {
            break;
          }
          socket.sendMessage(message);
        }
        System.out.println("Time: " + (System.currentTimeMillis() - time));
      }
    }
    finally {
      webServerSocket.close();
    }
  }
}
