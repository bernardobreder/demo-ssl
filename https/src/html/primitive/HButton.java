package html.primitive;

public class HButton extends HTextElement {

  public HButton() {
    super("button");
  }

  public HButton(String text) {
    this();
    this.setText(text);
  }

}
