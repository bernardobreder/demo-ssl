package bhws;

import html.HElement;

import java.util.Map;

import util.XmlNode;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public interface IBrowserClient {

  /**
   * @return xml da página html
   */
  public XmlNode http();

  /**
   * @param url
   * @param params
   * @return elemento da página a ser aberta
   */
  public HElement open(String url, Map<String, Object> params);

}
