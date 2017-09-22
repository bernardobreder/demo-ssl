package html.primitive;

import html.IBrowser;

/**
 *
 *
 * @author bernardobreder
 */
public class HSpace extends HTextElement {

  /**
   *
   */
  public HSpace() {
    super("span");
    IBrowser browser = this.getBrowser();
    browser.addChange(browser.getJavascript().html(getId(), "&nbsp;"));
  }

}
