package secho;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class SimpleEchoServer {

  public static void main(String[] arstring) {
    try {
      SSLServerSocket ss = createServerSocket(9999);
      SSLSocket socket = (SSLSocket) ss.accept();
      BufferedReader in =
        new BufferedReader(new InputStreamReader(socket.getInputStream()));
      String string = null;
      while ((string = in.readLine()) != null) {
        System.out.println(string);
        System.out.flush();
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * @param port
   * @return server socket
   * @throws IOException
   * @throws GeneralSecurityException
   */
  public static SSLServerSocket createServerSocket(int port)
    throws IOException, GeneralSecurityException {
    SSLServerSocketFactory ssf =
      (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
    SSLServerSocket ss = (SSLServerSocket) ssf.createServerSocket(port);
    return ss;
  }

}