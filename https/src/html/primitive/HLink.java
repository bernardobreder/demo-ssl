package html.primitive;

/**
 *
 *
 * @author bernardobreder
 */
public class HLink extends HTextElement {

  /** Link */
  private String link;

  /**
   *
   */
  public HLink() {
    super("a");
  }

  /**
   * @param value
   * @return this
   */
  public HLink setLink(String value) {
    this.link = value;
    this.addAttribute("href", value);
    return this;
  }

  /**
   * @return valor do link
   */
  public String getLink() {
    return this.getAttribute("href", null);
  }

}
