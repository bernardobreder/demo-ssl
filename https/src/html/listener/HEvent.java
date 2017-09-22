package html.listener;

import html.HElement;

/**
 *
 *
 * @author bernardobreder
 */
public class HEvent {

  /** Elemento */
  private HElement element;

  /**
   * @param element
   */
  public HEvent(HElement element) {
    super();
    this.element = element;
  }

  /**
   * @return elemento
   */
  public HElement getElement() {
    return element;
  }

}
