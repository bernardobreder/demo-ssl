package html.file;

import java.io.IOException;
import java.io.InputStream;

/**
 * JQuery Javascript
 *
 * @author bernardobreder
 */
public class JQueryJs {

  /** Conteúdo */
  private static String CONTENT;

  /**
   * @return conteudo
   * @throws IOException
   */
  public static String getContent() throws IOException {
    if (CONTENT == null) {
      InputStream in =
        HtmlFileUtil.class.getResourceAsStream("/html/file/jquery-2.0.3.js");
      try {
        CONTENT = HtmlFileUtil.read(in);
      }
      finally {
        in.close();
      }
    }
    return CONTENT;
  }

}
