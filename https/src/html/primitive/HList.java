package html.primitive;

import html.HElement;

/**
 *
 *
 * @author bernardobreder
 */
public class HList extends HElement {

  /**
   * Construtor
   */
  public HList() {
    super("ul");
  }

  /**
   * @return item acidionado
   */
  public HListItem addItem() {
    HListItem element = new HListItem();
    this.addElement(element);
    return element;
  }

  /**
   * 
   * 
   * @author bernardobreder
   */
  public static class HListItem extends HElement {

    /**
     * Construtor
     */
    public HListItem() {
      super("li");
    }

  }

}
