package browser;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketTest {

  public static void main(String[] args) throws Exception {
    ServerSocket server = new ServerSocket(5000);
    for (;;) {
      Socket socket = server.accept();
      socket.getOutputStream().write("Hello".getBytes());
      socket.close();
    }
  }

}
