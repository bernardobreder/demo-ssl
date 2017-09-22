package html.bootstrap;

import html.HElement;
import html.primitive.HDiv;

/**
 *
 *
 * @author bernardobreder
 */
public class HBScroll extends HDiv {

  private boolean verticalVisible;

  private boolean horizontalVisible;

  public HBScroll() {
    this.addClass("table-responsive");
  }

  public HBScroll(HElement body) {
    this();
    this.addElement(body);
  }

  public HBScroll setVerticalScrollVisible(boolean flag) {
    if (flag) {
      this.addStyle("overflow-y", "scroll");
    }
    else {
      this.addStyle("overflow-y", "hidden");
    }
    this.verticalVisible = flag;
    return this;
  }

  public HBScroll setHorizontalScrollVisible(boolean flag) {
    if (flag) {
      this.addStyle("overflow-x", "scroll");
    }
    else {
      this.addStyle("overflow-x", "hidden");
    }
    this.horizontalVisible = flag;
    return this;
  }

}
