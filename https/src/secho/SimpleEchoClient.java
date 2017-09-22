package secho;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.GeneralSecurityException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SimpleEchoClient {

  public static void main(String[] args) {
    try {
      SSLSocket socket = createSocket("breder.org", 9999);
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      BufferedWriter out =
        new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
      String string = null;
      while ((string = in.readLine()) != null) {
        out.write(string + '\n');
        out.flush();
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * @param host
   * @param port
   * @return socket
   * @throws IOException
   * @throws GeneralSecurityException
   */
  public static SSLSocket createSocket(String host, int port)
    throws IOException, GeneralSecurityException {
    SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
    SSLSocket socket = (SSLSocket) sf.createSocket(host, port);
    return socket;
  }

}
