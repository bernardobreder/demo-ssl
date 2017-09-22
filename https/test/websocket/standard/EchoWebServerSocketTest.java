package websocket.standard;

import ws.WebServerSocket;
import ws.WebSocket;

public class EchoWebServerSocketTest {

  public static void main(String[] args) throws Exception {
    WebServerSocket server = new StandardWebServerSocket(9090);
    try {
      for (;;) {
        WebSocket socket = server.accept();
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
      server.close();
    }
  }
}
