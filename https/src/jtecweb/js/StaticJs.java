package jtecweb.js;

import httpws.HttpWsClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.TreeMap;

import util.Bytes;
import util.StringUtil;

public class StaticJs {

  public static String HttpWsClientInitJs(String wsHost, String session)
    throws IOException {
    InputStream js =
      Bytes.getResource(HttpWsClient.class, "HttpWsClientInit.js");
    TreeMap<String, Object> map = new TreeMap<String, Object>();
    map.put("wsHost", wsHost);
    map.put("session", session);
    String content = StringUtil.toString(js, map);
    return content;
  }

}
