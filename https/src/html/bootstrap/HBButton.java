package html.bootstrap;

import html.primitive.HButton;

/**
 *
 *
 * @author bernardobreder
 */
public class HBButton extends HButton {

  /** Cor */
  private HBColor bgcolor;
  /** Tooltip */
  private String tooltip;

  public HBButton() {
    this(null, null);
  }

  public HBButton(String text, String tooltip) {
    super(text);
    this.addAttribute("type", "button");
    this.addClass("btn");
    this.setBackgroundColor(HBColor.MUTED);
    this.setTooltip(tooltip);
  }

  public HBButton setBackgroundColor(HBColor color) {
    if (this.bgcolor != null) {
      this.removeClass("btn-" + getBackgroundColor(color));
    }
    this.bgcolor = color;
    if (color != null) {
      this.addClass("btn-" + getBackgroundColor(color));
    }
    return this;
  }

  private String getBackgroundColor(HBColor color) {
    switch (color) {
      case MUTED:
        return "default";
      default:
        return color.name().toLowerCase();
    }
  }

  /**
   * @return the tooltip
   */
  public String getTooltip() {
    return tooltip;
  }

  /**
   * @param tooltip the tooltip to set
   */
  public void setTooltip(String tooltip) {
    if (tooltip != null) {
      this.addAttribute("title", tooltip);
    }
    else {
      this.removeAttribute("title");
    }
    this.tooltip = tooltip;
  }

}
