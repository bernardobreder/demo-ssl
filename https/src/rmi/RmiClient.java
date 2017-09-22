package rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.rmi.ssl.SslRMIClientSocketFactory;

public class RmiClient {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    // https://blogs.oracle.com/lmalventosa/entry/using_the_ssl_tls_based
    // http://stackoverflow.com/questions/13645530/rmi-over-ssl-how-do-i-integrate-truststore-and-keystore-into-application
    // http://www.cs.columbia.edu/~akonstan/rmi-ssl/
    System.setProperty("javax.net.ssl.trustStore", "localhost.keystore");
    System.setProperty("javax.net.ssl.trustStorePassword", "simulator");
    Registry registry =
      LocateRegistry.getRegistry("localhost", 8000,
        new SslRMIClientSocketFactory());
    LoginService loginService =
      (LoginService) registry.lookup(LoginService.class.getSimpleName());
    System.out.println(loginService.login("bbreder", null));
    System.out.println(loginService.login("breder", null));
  }

}
