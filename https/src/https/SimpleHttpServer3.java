package https;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.Security;
import java.util.concurrent.Executors;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;

import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;

public class SimpleHttpServer3 {

  public static void main(String[] args) throws Exception {
    HttpsServer server = HttpsServer.create(new InetSocketAddress(8000), 0);
    try {
      System.out.println("\nInitializing context ...\n");
      Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
      KeyStore ks = KeyStore.getInstance("JKS");
      char[] password = "simulator".toCharArray();
      ks.load(new FileInputStream("lig.keystore"), password);
      KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
      kmf.init(ks, password);
      SSLContext sslContext = SSLContext.getInstance("SSLv3");
      sslContext.init(kmf.getKeyManagers(), null, null);
      server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
        @Override
        public void configure(HttpsParameters params) {
          try {
            SSLContext c = getSSLContext();
            SSLParameters sslparams = c.getDefaultSSLParameters();
            params.setNeedClientAuth(true);
            params.setSSLParameters(sslparams);
            System.out.println("SSL context created ...\n");
          }
          catch (Exception e2) {
            System.out.println("Invalid parameter ...\n");
            e2.printStackTrace();
          }
        }
      });
    }
    catch (Exception e1) {
      e1.printStackTrace();
    }
    server.createContext("/info", new InfoHandler());
    server.createContext("/get", new GetHandler()).setAuthenticator(
      new BasicAuthenticator("get") {
        @Override
        public boolean checkCredentials(String user, String pwd) {
          return user.equals("admin") && pwd.equals("password");
        }
      });
    server.setExecutor(Executors.newCachedThreadPool());
    server.start();
    System.out.println("The server is running");
  }

  // http://localhost:8000/info
  static class InfoHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
      String response = "Use /get to authenticate (user:admin pwd:password)";
      SimpleHttpServer3.writeResponse(httpExchange, response.toString());
    }
  }

  static class GetHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
      StringBuilder response = new StringBuilder();
      response.append("<html><body>");
      response.append("hello " + httpExchange.getPrincipal().getUsername());
      response.append("</body></html>");
      SimpleHttpServer3.writeResponse(httpExchange, response.toString());
    }
  }

  public static void writeResponse(HttpExchange httpExchange, String response)
    throws IOException {
    httpExchange.sendResponseHeaders(200, response.length());
    OutputStream os = httpExchange.getResponseBody();
    os.write(response.getBytes());
    os.close();
  }

}