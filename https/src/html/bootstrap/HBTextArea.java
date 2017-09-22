package html.bootstrap;

import html.primitive.HTextArea;

/**
 *
 *
 * @author bernardobreder
 */
public class HBTextArea extends HTextArea {

  /** Número de linhas */
  private int rowCount;
  /** Resizeable */
  private boolean resizeable;

  /**
   * Construtor
   */
  public HBTextArea() {
    this.addClass("form-control");
  }

  /**
   * @param size
   * @return this
   */
  public HBTextArea setRowCount(int size) {
    this.rowCount = size;
    this.addAttribute("rows", Integer.toString(size));
    return this;
  }

  /**
   * @param flag
   * @return this
   */
  public HBTextArea setResizable(boolean flag) {
    this.resizeable = flag;
    if (flag) {
      this.addStyle("resize", "both");
    }
    else {
      this.addStyle("resize", "none");
    }
    return this;
  }

}
