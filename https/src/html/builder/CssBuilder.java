package html.builder;

/**
 * Css gerador
 *
 * @author bernardobreder
 */
public class CssBuilder {

  /** Conte;udo */
  private final StringBuilder sb = new StringBuilder();

  /**
   * @param name
   * @return this
   */
  public CssBuilder beginClass(String name) {
    sb.append(".");
    sb.append(name);
    sb.append(" { ");
    return this;
  }

  /**
   * @return this
   */
  public CssBuilder end() {
    sb.append(" }\n");
    return this;
  }

  /**
   * @param pixels
   * @return this
   */
  public CssBuilder marginLeft(int pixels) {
    sb.append("margin-left: ");
    sb.append(pixels);
    sb.append("px; ");
    return this;
  }

  /**
   * @param pixels
   * @return this
   */
  public CssBuilder marginRight(int pixels) {
    sb.append("margin-right: ");
    sb.append(pixels);
    sb.append("px; ");
    return this;
  }

  /**
   * @param pixels
   * @return this
   */
  public CssBuilder marginTop(int pixels) {
    sb.append("margin-top: ");
    sb.append(pixels);
    sb.append("px; ");
    return this;
  }

  /**
   * @param pixels
   * @return this
   */
  public CssBuilder marginBottom(int pixels) {
    sb.append("margin-bottom: ");
    sb.append(pixels);
    sb.append("px; ");
    return this;
  }

  /**
   * @param scroll
   * @return this
   */
  public CssBuilder overflowX(boolean scroll) {
    sb.append("overflow-x: ");
    sb.append(scroll ? "scroll" : "hidden");
    sb.append("; ");
    return this;
  }

  /**
   * @param scroll
   * @return this
   */
  public CssBuilder overflowY(boolean scroll) {
    sb.append("overflow-y: ");
    sb.append(scroll ? "scroll" : "hidden; ");
    sb.append("-ms-overflow-style: -ms-autohiding-scrollbar; ");
    sb.append("-webkit-overflow-scrolling: touch;; ");
    return this;
  }

  /**
   * @param value
   * @return this
   */
  public CssBuilder border(String value) {
    sb.append("border: ");
    sb.append(value);
    sb.append("; ");
    return this;
  }

  /**
   * @param value
   * @return this
   */
  public CssBuilder borderLeft(String value) {
    sb.append("border-left: ");
    sb.append(value);
    sb.append("; ");
    return this;
  }

  /**
   * @param value
   * @return this
   */
  public CssBuilder borderRight(String value) {
    sb.append("border-right: ");
    sb.append(value);
    sb.append("; ");
    return this;
  }

  /**
   * @param value
   * @return this
   */
  public CssBuilder borderTop(String value) {
    sb.append("border-top: ");
    sb.append(value);
    sb.append("; ");
    return this;
  }

  /**
   * @param value
   * @return this
   */
  public CssBuilder borderBottom(String value) {
    sb.append("border-bottom: ");
    sb.append(value);
    sb.append("; ");
    return this;
  }

  /**
   * @return this
   */
  public CssBuilder whiteSpaceNowrap() {
    sb.append("white-space: nowrap; ");
    return this;
  }

  /**
   * @param pixels
   * @return this
   */
  public CssBuilder width(int pixels) {
    sb.append("width: ");
    sb.append(pixels);
    sb.append("px; ");
    return this;
  }

  /**
   * @param pixels
   * @return this
   */
  public CssBuilder height(int pixels) {
    sb.append("height: ");
    sb.append(pixels);
    sb.append("px; ");
    return this;
  }

  /**
   * @return this
   */
  public CssBuilder width100() {
    sb.append("width: 100%; ");
    return this;
  }

  /**
   * @return retorna o toString
   */
  public String consume() {
    String result = sb.toString();
    sb.delete(0, sb.length());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return sb.toString();
  }

}
