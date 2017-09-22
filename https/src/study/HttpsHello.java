package study;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

public class HttpsHello {

  public static void main(String[] args) {
    try {
      {
        MessageDigest digest = MessageDigest.getInstance("SHA");
        digest.update("ae".getBytes());
        digest.update("ea".getBytes());
        System.out.println("SHA: " + toHex(digest.digest()));
      }
      {
        String algorithm = "DSA";
        KeyPairGenerator pairGenerator =
          KeyPairGenerator.getInstance(algorithm);
        KeyPair keyPair = pairGenerator.generateKeyPair();
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKeyEncoded =
          keyFactory.generatePublic(new X509EncodedKeySpec(publicKey
            .getEncoded()));
        PrivateKey privateKeyEncoded =
          keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKey
            .getEncoded()));
        System.out.println("Public Key Generator : "
          + toHex(publicKey.getEncoded()));
        System.out.println("Public Key Encoded   : "
          + toHex(publicKeyEncoded.getEncoded()));
        System.out.println("Private Key Generator: "
          + toHex(privateKey.getEncoded()));
        System.out.println("Private Key Encoded  : "
          + toHex(privateKeyEncoded.getEncoded()));
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    String ksName = "localhost.keystore";
    char ksPass[] = "simulator".toCharArray();
    char ctPass[] = "simulator".toCharArray();
    try {
      KeyStore ks = KeyStore.getInstance("JKS");
      ks.load(new FileInputStream(ksName), ksPass);
      KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
      kmf.init(ks, ctPass);
      SSLContext sc = SSLContext.getInstance("TLS");
      sc.init(kmf.getKeyManagers(), null, null);
      SSLServerSocketFactory ssf = sc.getServerSocketFactory();
      SSLServerSocket s = (SSLServerSocket) ssf.createServerSocket(8888);
      System.out.println("Server started:");
      printServerSocketInfo(s);
      // Listening to the port
      for (;;) {
        try {
          SSLSocket c = (SSLSocket) s.accept();
          BufferedWriter w =
            new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
          BufferedReader r =
            new BufferedReader(new InputStreamReader(c.getInputStream()));
          if (!c.isConnected() || c.isClosed() || c.isInputShutdown()
            || c.isOutputShutdown() || !c.getSession().isValid()) {
            c.close();
            continue;
          }
          String m = r.readLine();
          if (m == null) {
            c.close();
            continue;
          }
          printSocketInfo(c, m);
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
        }
        catch (Exception e) {
          e.printStackTrace();
        }

      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static String toHex(byte[] bs) {
    StringBuilder sb = new StringBuilder();
    for (int n = 0; n < bs.length; n++) {
      int i = bs[n];
      i += 128;
      sb.append(Integer.toHexString(i));
    }
    return sb.toString().toUpperCase();
  }

  private static void printSocketInfo(SSLSocket s, String firstLine) {
    System.out.println("Socket class: " + s.getClass());
    System.out.println("   Request Content = " + firstLine);
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