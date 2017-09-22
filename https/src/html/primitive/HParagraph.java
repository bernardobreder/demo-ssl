package html.primitive;

/**
 *
 *
 * @author bernardobreder
 */
public class HParagraph extends HTextElement {

  public static final int FONT_SIZE_DEFAULT = 14;

  public static final float LINE_HEIGHT_DEFAULT = 1.428f;

  private boolean lead;

  public HParagraph() {
    super("p");
  }

  public HParagraph(String text) {
    this();
    this.setText(text);
  }

  public HParagraph setLead(boolean flag) {
    this.lead = flag;
    if (flag) {
      this.addClass("lead");
    }
    else {
      this.removeClass("lead");
    }
    return this;
  }

  /**
   * @return the lead
   */
  public boolean isLead() {
    return lead;
  }

}
