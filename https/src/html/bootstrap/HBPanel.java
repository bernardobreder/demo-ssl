package html.bootstrap;

import html.HElement;
import html.primitive.HDiv;
import html.primitive.HText;
import html.primitive.HTextHeader;

/**
 *
 *
 * @author bernardobreder
 */
public class HBPanel extends HDiv {

  private HElement body;
  private HElement head;
  private HElement foot;
  private HDiv bodyDiv;
  private HDiv headDiv;
  private HDiv footerDiv;
  private HBColor color;

  public HBPanel() {
    this.addClass("panel");
    this.setBackgroundColor(HBColor.MUTED);
  }

  public HBPanel(HElement element) {
    this();
    this.setBody(element);
  }

  public HBPanel setBody(HElement body) {
    if (this.bodyDiv != null) {
      this.removeElement(this.bodyDiv);
    }
    this.body = body;
    this.addElement(this.bodyDiv =
      new HDiv().addClass("panel-body").addElement(body));
    return this;
  }

  public HBPanel setHead(HElement head) {
    if (this.headDiv != null) {
      this.removeElement(this.headDiv);
    }
    this.head = head;
    this.addElement(this.headDiv =
      new HDiv().addClass("panel-heading").addElement(head));
    return this;
  }

  public HBPanel setHead(String title) {
    return this.setHead(new HText(title));
  }

  public HBPanel setHeadTitle(HElement head) {
    return this.setHead(new HTextHeader(3).addClass("panel-title").addElement(
      head));
  }

  public HBPanel setHeadTitle(String title) {
    return this.setHeadTitle(new HText(title));
  }

  public HBPanel setFoot(HElement foot) {
    if (this.footerDiv != null) {
      this.removeElement(this.headDiv);
    }
    this.foot = foot;
    this.addElement(this.footerDiv =
      new HDiv().addClass("panel-footer").addElement(foot));
    return this;
  }

  public HBPanel setFoot(String title) {
    return this.setFoot(new HText(title));
  }

  public HBPanel setFootTitle(HElement head) {
    return this.setFoot(new HTextHeader(3).addClass("panel-title").addElement(
      head));
  }

  public HBPanel setFootTitle(String title) {
    return this.setFootTitle(new HText(title));
  }

  public HBPanel setBackgroundColor(HBColor color) {
    if (this.color != null) {
      this.removeClass("panel-" + toString(this.color));
    }
    this.color = color;
    if (color != null) {
      this.addClass("panel-" + toString(color));
    }
    return this;
  }

  protected String toString(HBColor color) {
    switch (color) {
      case MUTED: {
        return "default";
      }
      default:
        return color.name().toLowerCase();
    }
  }

}
