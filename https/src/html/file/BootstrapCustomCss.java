package html.file;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 *
 * @author Tecgraf
 */
public class BootstrapCustomCss {

  /** Conteúdo */
  private static String CONTENT;

  /**
   * @return conteudo
   * @throws IOException
   */
  public static String getContent() throws IOException {
    // if (CONTENT == null) {
    InputStream in =
      HtmlFileUtil.class.getResourceAsStream("/html/file/bootstrap-custom.css");
    try {
      CONTENT = HtmlFileUtil.read(in);
    }
    finally {
      in.close();
    }
    // }
    return CONTENT;
  }

}
