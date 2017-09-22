package html.primitive;

import html.HElement;

/**
 *
 *
 * @author bernardobreder
 */
public class HTable extends HElement {

  /** Borda */
  private int border;

  /**
   * Construtor
   */
  public HTable() {
    super("table");
  }

  /**
   * @param size
   * @return this
   */
  public HTable setBorder(int size) {
    this.border = size;
    this.addAttribute("border", Integer.toString(size));
    return this;
  }

  /**
   * @return borda
   */
  public int getBorder() {
    return border;
  }

  /**
   * @return linha
   */
  public HTableRow addRow() {
    HTableRow element = new HTableRow();
    this.addElement(element);
    return element;
  }

  /**
   * 
   * 
   * @author bernardobreder
   */
  public static class HTableRow extends HElement {

    /**
     *
     */
    public HTableRow() {
      super("tr");
    }

    /**
     * @return linha
     */
    public HTableCell addColumn() {
      HTableCell element = new HTableCell();
      this.addElement(element);
      return element;
    }

    /**
     * @return linha
     */
    public HTableHeader addColumnHeader() {
      HTableHeader element = new HTableHeader();
      this.addElement(element);
      return element;
    }

  }

  /**
   * 
   * 
   * @author bernardobreder
   */
  public static class HTableCell extends HElement {

    /**
     *
     */
    public HTableCell() {
      super("td");
    }

  }

  /**
   * 
   * 
   * @author bernardobreder
   */
  public static class HTableHeader extends HElement {

    /**
     *
     */
    public HTableHeader() {
      super("th");
    }

  }

}
