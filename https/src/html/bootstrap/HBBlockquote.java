package html.bootstrap;

import html.HElement;
import html.primitive.HParagraph;
import html.primitive.HSmall;

/**
 *
 *
 * @author bernardobreder
 */
public class HBBlockquote extends HElement {

  public static final int FONT_SIZE_DEFAULT = 14;

  public static final float LINE_HEIGHT_DEFAULT = 1.428f;

  public HBBlockquote() {
    super("blockquote");
  }

  public HBBlockquote addParagraph(String text) {
    return this.addElement(new HParagraph().addText(text));
  }

  public HBBlockquote addSmall(String text) {
    return this.addElement(new HSmall().addText(text));
  }

  public HBBlockquote setPullRight(boolean flag) {
    if (flag) {
      this.addClass("pull-right");
    }
    else {
      this.removeClass("pull-right");
    }
    return this;
  }

}
