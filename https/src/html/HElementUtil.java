package html;

public abstract class HElementUtil {

  /**
   * @param text the text to set
   */
  public static void setText(int id, String oldText, String newText) {
    IBrowser browser = IBrowser.browers.get();
    if (newText == null) {
      browser.addChange(browser.getJavascript().empty(id));
    }
    else if (oldText == null || !oldText.equals(newText)) {
      browser.addChange(browser.getJavascript().text(id, newText));
    }
  }

}
