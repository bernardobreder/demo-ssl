package html.bootstrap;

import html.HElement;
import html.primitive.HTable;

/**
 *
 *
 * @author bernardobreder
 */
public class HBTable extends HTable {

  /** Modelo */
  private final HBTableModel model;
  /** Condensed */
  private boolean condensed;
  /** Hover */
  private boolean hover;
  /** Bordered */
  private boolean bordered;
  /** Striped */
  private boolean striped;

  /**
   * Construtor
   * 
   * @param model
   */
  public HBTable(HBTableModel model) {
    this.model = model;
    this.addClass("table");
  }

  public HBTable setCondensed(boolean flag) {
    this.condensed = flag;
    if (flag) {
      this.addClass("table-condensed");
    }
    else {
      this.removeClass("table-condensed");
    }
    return this;
  }

  public HBTable setHover(boolean flag) {
    this.hover = flag;
    if (flag) {
      this.addClass("table-hover");
    }
    else {
      this.removeClass("table-hover");
    }
    return this;
  }

  public HBTable setBordered(boolean flag) {
    this.bordered = flag;
    if (flag) {
      this.addClass("table-bordered");
    }
    else {
      this.removeClass("table-bordered");
    }
    return this;
  }

  public HBTable setStriped(boolean flag) {
    this.striped = flag;
    if (flag) {
      this.addClass("table-striped");
    }
    else {
      this.removeClass("table-striped");
    }
    return this;
  }

  /**
   * @return linha
   */
  @Override
  public HBTableRow addRow() {
    HBTableRow element = new HBTableRow();
    this.addElement(element);
    return element;
  }

  public HBTableModel getModel() {
    return model;
  }

  public boolean isCondensed() {
    return condensed;
  }

  public boolean isHover() {
    return hover;
  }

  public boolean isBordered() {
    return bordered;
  }

  public boolean isStriped() {
    return striped;
  }

  /**
   * Dispara o evento de recalcular a tabela
   */
  @Override
  public HBTable fireChanged() {
    if (this.isChanged()) {
      this.setChanged(false);
      this.removeElements();
      int rowCount = this.model.getRowCount();
      int columnCount = this.model.getColumnCount();
      for (int r = 0; r < rowCount; r++) {
        HBTableRow row = this.addRow();
        this.model.configRow(row, r);
        for (int c = 0; c < columnCount; c++) {
          HBTableCell col = row.addColumn();
          HElement elementAt = model.getElementAt(r, c);
          this.model.configCell(row, col, r, c, elementAt);
          col.addElement(elementAt);
        }
      }
      super.fireChanged();
    }
    return this;
  }

  /**
   * 
   * 
   * @author bernardobreder
   */
  public static abstract class HBAbstractTableModel implements HBTableModel {

    @Override
    public void configRow(HBTableRow element, int row) {
    }

    @Override
    public void configCell(HBTableRow rowElement, HBTableCell cellElement,
      int row, int column, HElement elementAt) {
    }

  }

  /**
   * 
   * 
   * @author bernardobreder
   */
  public static interface HBTableModel {

    /**
     * @return número de linhas
     */
    public int getRowCount();

    /**
     * @return número de colunas
     */
    public int getColumnCount();

    /**
     * @param row
     * @param column
     * @return elemento
     */
    public HElement getElementAt(int row, int column);

    /**
     * Configura a linha
     * 
     * @param element
     * @param row
     */
    public void configRow(HBTableRow element, int row);

    /**
     * Configura a celula
     * 
     * @param rowElement
     * @param columnElement
     * @param row
     * @param column
     * @param elementAt
     */
    public void configCell(HBTableRow rowElement, HBTableCell columnElement,
      int row, int column, HElement elementAt);

  }

  /**
   * 
   * 
   * @author bernardobreder
   */
  public static class HBTableRow extends HTableRow {

    /** Cor do fundo */
    private HBTableColor bgColor;

    /**
     * @param color
     * @return this
     */
    public HBTableRow setBackgroundColor(HBTableColor color) {
      this.bgColor = color;
      this.addClass(color.name().toLowerCase());
      return this;
    }

    /**
     * @return linha
     */
    @Override
    public HBTableCell addColumn() {
      HBTableCell element = new HBTableCell();
      this.addElement(element);
      return element;
    }

  }

  /**
   * 
   * 
   * @author bernardobreder
   */
  public static class HBTableCell extends HTableCell {

    /** Cor do fundo */
    private HBTableColor bgColor;

    /**
     * @param color
     * @return this
     */
    public HBTableCell setBackgroundColor(HBTableColor color) {
      this.bgColor = color;
      this.addClass(color.name().toLowerCase());
      return this;
    }

  }

  public static enum HBTableColor {

    ACTIVE,
    SUCCESS,
    WARNING,
    DANGER;

  }

}
