package study;

import java.util.Map;

import study.org.veryquick.embweb.EmbeddedServer;
import study.org.veryquick.embweb.HttpRequestHandler;
import study.org.veryquick.embweb.Response;

public class VQEmbWeb {

  public static void main(String[] args) throws Exception {
    EmbeddedServer.createInstance(7777, new HttpRequestHandler() {
      @Override
      public Response handleRequest(Type type, Map<String, String> headers,
        String url, Map<String, String> parameters) {
        Response response = new Response();
        if (!headers.containsKey("Authorization")) {
          response.setAuthorized();
        }
        else {
          response.addContent("<h1>Hello</h1>");
          response.setOk();
        }
        return response;
      }
    });
  }

}
