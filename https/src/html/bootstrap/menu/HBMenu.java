package html.bootstrap.menu;

import html.primitive.HLink;
import html.primitive.HList;

/**
 *
 *
 * @author bernardobreder
 */
public class HBMenu extends HList.HListItem {

  /** Link */
  private HLink linkElem;

  /**
   * Construtor
   * 
   * @param title
   */
  public HBMenu(String title) {
    this.linkElem = new HLink();
    this.setTitle(title);
  }

  /**
   * @param title
   * @return this
   */
  public HBMenu setTitle(String title) {
    this.linkElem.setText(title);
    return this;
  }

  /**
   * @param link
   * @return this
   */
  public HBMenu setTitleLink(String link) {
    this.linkElem.setLink(link);
    return this;
  }

  /**
   * @return link do titulo
   */
  public String getTitleLink() {
    return this.linkElem.getLink();
  }

}
