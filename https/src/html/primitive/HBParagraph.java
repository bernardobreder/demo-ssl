package html.primitive;

import html.HElement;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public class HBParagraph extends HElement {

  public static final int FONT_SIZE_DEFAULT = 14;

  public static final float LINE_HEIGHT_DEFAULT = 1.428f;

  private boolean lead;

  public HBParagraph() {
    super("p");
  }

  public HBParagraph setLead(boolean flag) {
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
