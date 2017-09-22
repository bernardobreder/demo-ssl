package html.file;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 *
 * @author bernardobreder
 */
public class BootstrapJs {

  /** Conteúdo */
  private static String CONTENT;

  /**
   * @return conteudo
   * @throws IOException
   */
  public static String getContent() throws IOException {
    if (CONTENT == null) {
      InputStream in =
        HtmlFileUtil.class.getResourceAsStream("/html/file/bootstrap.js");
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
