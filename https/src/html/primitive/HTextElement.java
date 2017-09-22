package html.primitive;

import html.HElement;
import html.HElementUtil;

/**
 *
 *
 * @author bernardobreder
 */
public class HTextElement extends HElement {

  /** Texto */
  private String text;

  /**
   * @param name
   */
  public HTextElement(String name) {
    super(name);
  }

  /**
   * @param name
   * @param text
   */
  public HTextElement(String name, String text) {
    this(name);
    this.setText(text);
  }

  /**
   * @return the text
   */
  public String getText() {
    return text;
  }

  /**
   * @param text the text to set
   */
  public void setText(String text) {
    HElementUtil.setText(getId(), this.text, text);
    this.text = text;
  }

}
