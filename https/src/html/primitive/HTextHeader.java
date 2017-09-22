package html.primitive;

public class HTextHeader extends HTextElement {

  public HTextHeader(int size) {
    super("h" + size);
    if (size < 1 || size > 6) {
      throw new IllegalArgumentException("size must be between 1 and 6");
    }
  }

  public HTextHeader(int size, String text) {
    this(size);
    this.setText(text);
  }

}
