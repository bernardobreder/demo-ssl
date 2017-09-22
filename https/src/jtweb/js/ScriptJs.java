package jtweb.js;

import java.io.IOException;

import util.Bytes;
import util.StringUtil;

public class ScriptJs {

  public static String getDom() throws IOException {
    return StringUtil.toString(Bytes.getResource(ScriptJs.class, "dom.js"),
      null);
  }

}
