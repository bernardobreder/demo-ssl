package rmi;

import javax.net.ssl.SSLSocket;

public class EchoClient {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    SSLSocket socket = HttpsServerSocket.createSocket("localhost", 9000);
    socket.getOutputStream().write('a');
    System.out.println((char) socket.getInputStream().read());
  }

}
