package browser.mock.html;

public class HtmlTextElement extends HtmlElement {

  private final String text;

  public HtmlTextElement(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }

  @Override
  public String toString() {
    return text;
  }

}
