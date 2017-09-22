package html.bootstrap;

import html.HElement;
import html.primitive.HDiv;
import html.primitive.HSmall;
import html.primitive.HStrong;
import html.primitive.HTextHeader;

/**
 *
 *
 * @author bernardobreder
 */
public class HBPageHeader extends HDiv {

  public HBPageHeader() {
    this.addClass("page-header");
  }

  /**
   * @param strong
   * @param small
   */
  public HBPageHeader(String strong, String small) {
    this();
    HElement header = new HTextHeader(1);
    if (strong != null) {
      header.addElement(new HStrong().addText(strong));
    }
    if (small != null) {
      header.addSpace().addElement(new HSmall().addText(small));
    }
    this.addElement(header);
  }

}
