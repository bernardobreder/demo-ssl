package rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

public class RmiServer {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    System.setProperty("javax.net.ssl.keyStore", "localhost.keystore");
    System.setProperty("javax.net.ssl.keyStorePassword", "simulator");
    int port = 8000;
    Registry registry =
      LocateRegistry.createRegistry(port, new SslRMIClientSocketFactory(),
        new SslRMIServerSocketFactory());
    registry.rebind(LoginService.class.getSimpleName(), UnicastRemoteObject
      .exportObject(new LoginServiceDB(), 0, new RMISSLClientSocketFactory(),
        new RMISSLServerSocketFactory()));
  }
}
