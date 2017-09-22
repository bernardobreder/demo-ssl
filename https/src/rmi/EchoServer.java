package rmi;

import java.net.Socket;

import javax.net.ssl.SSLServerSocket;

public class EchoServer {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    SSLServerSocket serverSocket = HttpsServerSocket.createServerSocket(9000);
    for (;;) {
      try {
        Socket socket = serverSocket.accept();
        int c = socket.getInputStream().read();
        socket.getOutputStream().write(c);
        socket.close();
      }
      catch (Exception e) {
      }
    }
  }

}
