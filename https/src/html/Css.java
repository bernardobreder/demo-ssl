package html;

/**
 * Css gerador
 *
 * @author Tecgraf/PUC-Rio
 */
public class Css {

  /** Conte;udo */
  private final StringBuilder sb = new StringBuilder();

  /**
   * @param pixels
   * @return this
   */
  public Css width(int pixels) {
    this.sb.append("width: ");
    sb.append(pixels);
    sb.append("px;");
    return this;
  }

  /**
   * @param pixels
   * @return this
   */
  public Css height(int pixels) {
    this.sb.append("height: ");
    sb.append(pixels);
    sb.append("px;");
    return this;
  }

  /**
   * @return retorna o toString
   */
  public String consume() {
    String result = this.sb.toString();
    this.sb.delete(0, sb.length());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return this.sb.toString();
  }

}
