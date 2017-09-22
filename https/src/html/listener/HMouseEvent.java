package html.listener;

import html.HElement;

/**
 *
 *
 * @author bernardobreder
 */
public class HMouseEvent extends HEvent {

  /** X */
  private int x;
  /** Y */
  private int y;

  /**
   * @param element
   * @param x
   * @param y
   */
  public HMouseEvent(HElement element, int x, int y) {
    super(element);
    this.x = x;
    this.y = y;
  }

  /**
   * @return x
   */
  public int getX() {
    return x;
  }

  /**
   * @param x
   */
  public void setX(int x) {
    this.x = x;
  }

  /**
   * @return y
   */
  public int getY() {
    return y;
  }

  /**
   * @param y
   */
  public void setY(int y) {
    this.y = y;
  }

}
