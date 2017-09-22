package demo.http;

import http.HttpResponse;
import http.HttpServerSocket;
import http.HttpSocket;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import websocket.standard.StandardWebServerSocket;
import demo.bandeirabr.BandeiraBrowserTest;

public class HttpsTest {

  public static void runHttps(HttpServerSocket server) throws IOException {
    try {
      for (;;) {
        try {
          HttpSocket httpSocket = server.accept();
          String url = httpSocket.getRequest().getUrl();
          if (url.equals("/")) {
            url = "/websocket.html";
          }
          File file = new File("pub", url);
          HttpResponse response = httpSocket.getResponse();
          if (url.contains("..")) {
            response.answerError();
          }
          else if (url.equals("/")) {
            response.writeString("Teste");
          }
          else if (file.exists()) {
            // response.setContentLength(file.length());
            response.answerSuccess();
            FileInputStream in = new FileInputStream(file);
            try {
              byte[] bytes = new byte[8 * 1024];
              for (int n; (n = in.read(bytes)) != -1;) {
                response.writeBytes(bytes, 0, n);
              }
            }
            finally {
              in.close();
            }
          }
          response.flush();
          httpSocket.close();
        }
        catch (Throwable e) {
        }
      }
    }
    finally {
      server.close();
    }
  }

  public static void main(String[] args) throws Exception {
    final StandardHttpsServerSocket httpsServer =
      new StandardHttpsServerSocket(7777);
    final StandardWebServerSocket webServer = new StandardWebServerSocket(8080);
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          runHttps(httpsServer);
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      }
    }, "Https Server").start();
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          BandeiraBrowserTest.main(webServer);
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      }
    }, "WebSocket Server").start();
  }
}
