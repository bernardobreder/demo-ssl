package html.file;

import java.io.IOException;
import java.io.InputStream;

/**
 * JQuery Javascript
 *
 * @author bernardobreder
 */
public class JQueryHistory {

  /** Conteúdo */
  private static String CONTENT;

  /**
   * @return conteudo
   * @throws IOException
   */
  public static String getContent() throws IOException {
    if (CONTENT == null) {
      InputStream in =
        HtmlFileUtil.class.getResourceAsStream("/html/file/jquery.history.js");
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
