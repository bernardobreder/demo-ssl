package html.builder;

/**
 *
 *
 * @author bernardobreder
 */
public class JsBuilder {

  /** Conte;udo */
  private final StringBuilder sb = new StringBuilder();

  /**
   * @return retorna o toString
   */
  public String consume() {
    String result = sb.toString();
    sb.delete(0, sb.length());
    return result;
  }

  /**
   * @param format
   * @param objects
   * @return this
   */
  public JsBuilder append(String format, Object... objects) {
    this.appendInline(format, objects);
    sb.append('\n');
    return this;
  }

  /**
   * @param format
   * @param objects
   * @return this
   */
  public JsBuilder appendInline(String format, Object... objects) {
    if (objects.length > 0) {
      sb.append(String.format(format, objects));
    }
    else {
      sb.append(format);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return sb.toString();
  }

}
