package html.primitive;

public class HSmall extends HTextElement {

  /**
   * @param text
   */
  public HSmall() {
    super("small");
  }

  /**
   * @param text
   */
  public HSmall(String text) {
    this();
    this.setText(text);
  }

}
