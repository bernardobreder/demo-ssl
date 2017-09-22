package ssl;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;

/**
 *
 *
 * @author Tecgraf
 */
public class StandardSSLServerSocket {

  /**
   * @param port
   * @param ks
   * @param keyPass
   * @return server socket
   * @throws IOException
   * @throws NoSuchAlgorithmException
   * @throws KeyStoreException
   * @throws UnrecoverableKeyException
   * @throws KeyManagementException
   */
  public static SSLServerSocket create(int port, KeyStore ks, char[] keyPass)
    throws NoSuchAlgorithmException, UnrecoverableKeyException,
    KeyStoreException, KeyManagementException, IOException {
    KeyManagerFactory kmf =
      KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(ks, keyPass);
    // SSLContext sslcontext = SSLContext.getInstance("TLS");
    SSLContext sslcontext = SSLContext.getInstance("SSLv3");
    sslcontext.init(kmf.getKeyManagers(), null, null);
    ServerSocketFactory ssf = sslcontext.getServerSocketFactory();
    return (SSLServerSocket) ssf.createServerSocket(port);
  }

  /**
   * @param in
   * @param pass
   * @return chave
   * @throws KeyStoreException
   * @throws NoSuchAlgorithmException
   * @throws CertificateException
   * @throws IOException
   */
  public static KeyStore getKeyStore(InputStream in, char[] pass)
    throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
    IOException {
    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
    try {
      ks.load(in, pass);
    }
    finally {
      in.close();
    }
    return ks;
  }

}
