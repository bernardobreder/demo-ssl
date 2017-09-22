import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.util.Enumeration;
import java.util.Properties;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;

// http://www.javafundu.com/2010/02/sample-https-server.html
// keytool -genkey -alias alias -keypass simulator -keystore lig.keystore
// -storepass simulator
// keytool -genkey -alias my_host -keypass simulator -keystore
// localhost.keystore

public class HTTPs {

  public static final String CONTENT_LENGTH = "Content-length";
  public static final String CONTENT_TYPE = "Content-type";

  private static char NEW_LINE = '\n';

  private static final String EOL = "\r\n";

  private BufferedInputStream is;
  private DataOutputStream os;
  String keystore = "lig.keystore";
  char keystorepass[] = "simulator".toCharArray();
  char keypassword[] = "simulator".toCharArray();

  private static final String SC_NO_CONTENT = "HTTP/1.1 200" + EOL
    + "Content-Type: text/plain" + EOL + "Cache-Control: private" + EOL + EOL;

  // The port number which the server will be listening on
  public static final int HTTPS_PORT = 7777;

  public ServerSocket createServerSocket() throws Exception {
    // Create the KeyStore
    KeyStore ks = getKeyStore();
    // Create the factory that store the algorithm
    KeyManagerFactory kmf =
      KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(ks, keypassword);
    // Constroy the socket server
    SSLContext sslcontext = SSLContext.getInstance("SSLv3");
    sslcontext.init(kmf.getKeyManagers(), null, null);
    ServerSocketFactory ssf = sslcontext.getServerSocketFactory();
    SSLServerSocket server =
      (SSLServerSocket) ssf.createServerSocket(HTTPS_PORT);
    return server;
  }

  private KeyStore getKeyStore() {
    try {
      KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
      ks.load(new FileInputStream(keystore), keystorepass);
      return ks;
    }
    catch (Exception e) {
      System.out.println(e.getLocalizedMessage());
      System.exit(-1);
    }

    return null;

  }

  // multi-threading -- create a new connection
  // for each request
  public void run() {
    ServerSocket listen;
    try {
      listen = createServerSocket();
      while (true) {
        Socket socket = listen.accept();

        is = new BufferedInputStream(socket.getInputStream());
        os = new DataOutputStream(socket.getOutputStream());
        readHTTPMessage();

        StringBuffer strbuffer = new StringBuffer();

        strbuffer.append(SC_NO_CONTENT);
        strbuffer.append("ab");
        writebytes(socket, strbuffer.toString());
      }
    }
    catch (Exception e) {
      System.out.println("Exception: " + e.getMessage());
    }
  }

  private void writebytes(Socket socket, String data) {
    try {
      //
      os.write(data.getBytes());
      os.flush();
      if (!socket.isClosed()) {
        is.close();
      }
      if (!socket.isClosed()) {
        os.close();
      }

    }
    catch (Throwable e) {
      System.out.println(e);
    }

    try {
      if (!socket.isClosed()) {
        socket.close();
      }
    }
    catch (Throwable e) {
      System.out.println("Problem in data writing in channel");
      System.out.println(e);
    }
  }

  public String readHTTPMessage() throws IOException {
    String result = null;
    StringBuffer strb = new StringBuffer();
    int bufferSize = 1024;
    int contentLength = 0;
    int headerLength = 0;
    int bodyLength = 0;
    try {
      byte[] buf = new byte[bufferSize];
      int nread = 0;
      while ((nread = is.read(buf)) != -1) {
        if (nread == 0) {
          continue;
        }
        strb.append(new String(buf, 0, nread));
        // System.out.println(strb);
        result = strb.toString();

        if (contentLength == 0) {

          // atleast 50 bytes required to identify content length
          if (result.length() < 50) {
            continue;
          }

          HTTPRequest request = new HTTPRequest(result.getBytes());
          String contentStr =
            getPropertyIgnoreCase(request.getHeaders(), CONTENT_LENGTH);

          // if length specified
          if (null == contentStr || "".equals(contentStr)) {
            break;
          }

          contentLength = Integer.parseInt("" + contentStr.trim());
          bodyLength = request.getBodyContent().length;
          headerLength = result.length() - bodyLength;
        }
        else {
          bodyLength = result.length() - headerLength;
        }

        if (bodyLength < contentLength) {
          bufferSize = contentLength - bodyLength;
          buf = new byte[bufferSize];
        }
        else {

          if (bodyLength >= contentLength || result.endsWith(EOL)) {

            // try{socket.shutdownInput();}catch(Exception e){}
            break;
          }

        }
      }
    }
    catch (Exception e) {
      System.out.println(e);
    }
    finally {
      // try{is.close();}catch(Exception e){}

    }
    return result;
  }

  // main program
  public static void main(String argv[]) throws Exception {
    HTTPs https = new HTTPs();
    https.run();
  }

  private class HTTPRequest {

    private byte[] request;

    private int headerIndex = 0;

    private Properties headers;
    private byte[] bodyContent;

    public HTTPRequest(byte[] request) {

      this.request = request;
      init();

    }

    private void init() {

      if (request == null) {
        return;
      }

      for (int i = 0; i < request.length; i++) {
        if ((NEW_LINE == (char) request[i]) && (13 == request[i + 1])) {
          headerIndex = i;
          break;
        }
      }

      initHeaders();
      initBodyContent();
    }

    private void initHeaders() {
      String httpMethod;
      String httpVersion;
      String path;
      String headerContent = new String(request, 0, headerIndex);

      String[] availbleHeaders = headerContent.split("\n");

      if (availbleHeaders != null && availbleHeaders.length > 0) {
        String[] typeContainer = availbleHeaders[0].split(" ");

        if (typeContainer != null && typeContainer.length > 2) {
          httpMethod = typeContainer[0];
          path = typeContainer[1];
          httpVersion = typeContainer[2];
        }
      }

      if (availbleHeaders.length > 1) {
        this.headers = new Properties();

        for (int index = 1; index < availbleHeaders.length; index++) {
          String key =
            availbleHeaders[index].substring(0, availbleHeaders[index]
              .indexOf(':'));
          String value =
            availbleHeaders[index].substring(availbleHeaders[index]
              .indexOf(':') + 1, availbleHeaders[index].length() - 1);
          this.headers.put(key, value);
        }
      }
    }

    private void initBodyContent() {
      int bodyIndex = headerIndex + 3;
      bodyContent = new byte[request.length - bodyIndex];

      for (int i = (bodyIndex), j = 0; i < request.length; i++, j++) {
        bodyContent[j] = request[i];
      }
    }

    public Properties getHeaders() {
      return headers;
    }

    public byte[] getBodyContent() {
      return bodyContent;
    }
  }

  public static String getPropertyIgnoreCase(Properties props, String key) {
    String value = null;

    Enumeration enum0 = props.propertyNames();
    while (enum0.hasMoreElements()) {
      String name = (String) enum0.nextElement();
      if (key.equalsIgnoreCase(name)) {
        value = props.getProperty(name);
        break;
      }
    }

    return value;
  }
}
