package study.rmi;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class RMISSLClientSocketFactory implements RMIClientSocketFactory,
  Serializable {

  /**
   * {@inheritDoc}
   */
  @Override
  public Socket createSocket(String host, int port) throws IOException {
    SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
    SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
    return socket;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    else if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    return true;
  }
}