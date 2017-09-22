package html.primitive;

public class HStrong extends HTextElement {

  /**
   *
   */
  public HStrong() {
    super("strong");
  }

  /**
   * @param text
   */
  public HStrong(String text) {
    this();
    this.setText(text);
  }

}
