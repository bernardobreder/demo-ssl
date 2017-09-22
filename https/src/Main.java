import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;

public class Main {

  public static void main(String[] args) {
    try {
      // setup the socket address
      InetSocketAddress address =
        new InetSocketAddress(InetAddress.getLocalHost(), 8080);

      // initialise the HTTPS server
      HttpsServer httpsServer = HttpsServer.create(address, 0);
      SSLContext sslContext = SSLContext.getInstance("TLS");

      // initialise the keystore
      char[] password = "simulator".toCharArray();
      KeyStore ks = KeyStore.getInstance("JKS");
      FileInputStream fis = new FileInputStream("lig.keystore");
      try {
        ks.load(fis, password);
      }
      finally {
        fis.close();
      }

      // setup the key manager factory
      KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
      kmf.init(ks, password);

      // setup the trust manager factory
      TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
      tmf.init(ks);

      // setup the HTTPS context and parameters
      sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
      httpsServer.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
        @Override
        public void configure(HttpsParameters params) {
          try {
            // initialise the SSL context
            SSLContext c = SSLContext.getDefault();
            SSLEngine engine = c.createSSLEngine();
            params.setNeedClientAuth(false);
            params.setCipherSuites(engine.getEnabledCipherSuites());
            params.setProtocols(engine.getEnabledProtocols());

            // get the default parameters
            SSLParameters defaultSSLParameters = c.getDefaultSSLParameters();
            params.setSSLParameters(defaultSSLParameters);
          }
          catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      });
      // httpsServer.start();
      SSLServerSocketFactory ssf = sslContext.getServerSocketFactory();
      SSLServerSocket s = (SSLServerSocket) ssf.createServerSocket(8888);
      System.out.println("Server started:");
      printServerSocketInfo(s);
      // Listening to the port
      SSLSocket c = (SSLSocket) s.accept();
      // printSocketInfo(c);
      BufferedWriter w =
        new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
      BufferedReader r =
        new BufferedReader(new InputStreamReader(c.getInputStream()));
      // String m = r.readLine();
      w.write("HTTP/1.0 200 OK");
      w.newLine();
      w.write("Content-Type: text/html");
      w.newLine();
      w.newLine();
      w.write("<html><body>Hello world!</body></html>");
      w.newLine();
      w.flush();
      w.close();
      r.close();
      c.close();
      // LigServer server = new LigServer(httpsServer);
      // joinableThreadList.add(server.getJoinableThread());
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  private static void printSocketInfo(SSLSocket s) {
    System.out.println("Socket class: " + s.getClass());
    System.out.println("   Remote address = " + s.getInetAddress().toString());
    System.out.println("   Remote port = " + s.getPort());
    System.out.println("   Local socket address = "
      + s.getLocalSocketAddress().toString());
    System.out.println("   Local address = " + s.getLocalAddress().toString());
    System.out.println("   Local port = " + s.getLocalPort());
    System.out.println("   Need client authentication = "
      + s.getNeedClientAuth());
    SSLSession ss = s.getSession();
    System.out.println("   Cipher suite = " + ss.getCipherSuite());
    System.out.println("   Protocol = " + ss.getProtocol());
  }

  private static void printServerSocketInfo(SSLServerSocket s) {
    System.out.println("Server socket class: " + s.getClass());
    System.out.println("   Socker address = " + s.getInetAddress().toString());
    System.out.println("   Socker port = " + s.getLocalPort());
    System.out.println("   Need client authentication = "
      + s.getNeedClientAuth());
    System.out.println("   Want client authentication = "
      + s.getWantClientAuth());
    System.out.println("   Use client mode = " + s.getUseClientMode());
  }

}
