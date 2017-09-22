package html.bootstrap;

import html.HElement;

public class HBUtil {

  public static void addTooltip(HElement element, String tooltip) {
    element.addAttribute("data-toggle", "tooltip");
    element.addAttribute("title", tooltip);
  }

}
