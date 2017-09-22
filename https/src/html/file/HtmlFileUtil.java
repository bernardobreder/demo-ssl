package html.file;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import util.ByteArrayOutputStream;

/**
 *
 *
 * @author Tecgraf
 */
public class HtmlFileUtil {

  /**
   * @param in
   * @return conteudo da stream
   * @throws IOException
   */
  public static String read(InputStream in) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] bytes = new byte[1024];
    for (int n; (n = in.read(bytes)) != -1;) {
      out.write(bytes, 0, n);
    }
    return new String(out.toByteArray(), Charset.forName("utf-8"));
  }

}