package rmi;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public class HttpsServerSocket {

  /**  */
  private static final String ksName = "localhost.keystore";
  /**  */
  private static final char ksPass[] = "simulator".toCharArray();
  /**  */
  private static final char ctPass[] = "simulator".toCharArray();

  /**
   * @param port
   * @return server socket
   * @throws IOException
   * @throws GeneralSecurityException
   */
  public static SSLServerSocket createServerSocket(int port)
    throws IOException, GeneralSecurityException {
    KeyStore ks = createKeyStore();
    KeyManagerFactory kmf = createKeyManagerFactory();
    SSLContext sc = createContext();
    kmf.init(ks, ctPass);
    sc.init(kmf.getKeyManagers(), null, null);
    SSLServerSocketFactory ssf = sc.getServerSocketFactory();
    SSLServerSocket s = (SSLServerSocket) ssf.createServerSocket(port);
    return s;
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
    KeyStore ks = createKeyStore();
    SSLContext sc = createContext();
    KeyManagerFactory kmf = createKeyManagerFactory();
    kmf.init(ks, ctPass);
    sc.init(kmf.getKeyManagers(), null, null);
    SSLSocketFactory sf = sc.getSocketFactory();
    SSLSocket s = (SSLSocket) sf.createSocket();
    return s;
  }

  private static SSLContext createContext() throws NoSuchAlgorithmException {
    return SSLContext.getInstance("TLS");
  }

  /**
   * @return fabrica
   * @throws GeneralSecurityException
   */
  private static KeyManagerFactory createKeyManagerFactory()
    throws GeneralSecurityException {
    return KeyManagerFactory.getInstance("SunX509");
  }

  /**
   * @return keyStore
   * @throws IOException
   * @throws GeneralSecurityException
   */
  private static KeyStore createKeyStore() throws IOException,
    GeneralSecurityException {
    KeyStore ks = KeyStore.getInstance("JKS");
    FileInputStream input = new FileInputStream(ksName);
    try {
      ks.load(input, ksPass);
    }
    finally {
      input.close();
    }
    return ks;
  }
}
