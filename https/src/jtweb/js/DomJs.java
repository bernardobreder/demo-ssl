package jtweb.js;

import java.io.IOException;

import util.Bytes;
import util.StringUtil;

public class DomJs {

  public static String get(String wsHost, String session) throws IOException {
    return StringUtil.toString(Bytes.getResource(DomJs.class, "dom.js"), null);
  }

}
