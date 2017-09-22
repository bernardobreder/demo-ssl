package browser;

import java.net.Socket;

import util.Bytes;

public class SocketTest {

  public static void main(String[] args) throws Exception {
    int n = 0;
    for (;;) {
      Socket socket = new Socket("breder.org", 5000);
      socket.getOutputStream().write("Hello".getBytes());
      String text = Bytes.toString(socket.getInputStream());
      System.out.println(n++ + text);
      socket.close();
    }
  }

}
