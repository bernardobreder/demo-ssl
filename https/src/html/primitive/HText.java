package html.primitive;

/**
 *
 *
 * @author bernardobreder
 */
public class HText extends HTextElement {

  /**
   *
   */
  public HText() {
    super("span");
  }

  /**
   * @param text
   */
  public HText(String text) {
    this();
    this.setText(text);
  }

}
