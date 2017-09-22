package html.bootstrap;

import html.HElement;

public class HBDescription extends HElement {

  private HBDescriptionModel model;

  public HBDescription(HBDescriptionModel model) {
    super("dl");
    this.setModel(model);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HBDescription fireChanged() {
    if (this.isChanged()) {
      this.setChanged(false);
      this.removeElements();
      int rows = this.model.getDescriptionCount();
      for (int n = 0; n < rows; n++) {
        HElement keyElement = this.model.getDescriptionKeyAt(n);
        this.addElement(new HBDescriptionKey().addElement(keyElement));
        HElement valueElement = this.model.getDescriptionValueAt(n);
        this.addElement(new HBDescriptionValue().addElement(valueElement));
      }
      super.fireChanged();
    }
    return this;
  }

  public HBDescription setHorizontal(boolean flag) {
    if (flag) {
      this.addClass("dl-horizontal");
    }
    else {
      this.removeClass("dl-horizontal");
    }
    return this;
  }

  /**
   * @return the model
   */
  public HBDescriptionModel getModel() {
    return model;
  }

  /**
   * @param model the model to set
   */
  public void setModel(HBDescriptionModel model) {
    this.model = model;
    this.fireChanged();
  }

  public class HBDescriptionKey extends HElement {

    public HBDescriptionKey() {
      super("dt");
    }

  }

  public class HBDescriptionValue extends HElement {

    public HBDescriptionValue() {
      super("dd");
    }

  }

  public static class HBDefaultDescriptionModel {

  }

  public static interface HBDescriptionModel {

    /**
     * Indica quantas descrições terão
     * 
     * @return numero de descrições
     */
    public int getDescriptionCount();

    /**
     * Retorna a descrição
     * 
     * @param row
     * @return elemento
     */
    public HElement getDescriptionKeyAt(int row);

    /**
     * Retorna a descrição
     * 
     * @param row
     * @return elemento
     */
    public HElement getDescriptionValueAt(int row);

  }

}
