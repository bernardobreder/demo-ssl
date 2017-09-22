package html.bootstrap.menu;

import html.HElement;
import html.primitive.HButton;
import html.primitive.HDiv;
import html.primitive.HLink;
import html.primitive.HList;
import html.primitive.HSpan;

public class HBMenuBar extends HDiv {

  private boolean bginverse;
  /** Titulo */
  private HLink title;
  /** Lista de Elementos */
  private HElement listElem;

  /**
   * Construtor
   */
  public HBMenuBar() {
    this.addClass("navbar-inverse");
    this.addAttribute("role", "navigation");
    HDiv containerElem = new HDiv().addClass("container");
    HDiv navbarHeader = new HDiv().addClass("navbar-header");
    navbarHeader.addElement(
      new HButton().addClass("navbar-toggle").addAttribute("type", "button")
        .addAttribute("data-toggle", "collapse").addAttribute("data-target",
          ".navbar-collapse").addElement(new HSpan().addClass("icon-bar"))
        .addElement(new HSpan().addClass("icon-bar")).addElement(
          new HSpan().addClass("icon-bar"))).addElement(
      this.title = new HLink().addClass("navbar-brand"));
    containerElem.addElement(navbarHeader);
    containerElem.addElement(new HDiv().addClass("collapse").addClass(
      "navbar-collapse").addElement(
      this.listElem = new HList().addClass("nav").addClass("navbar-nav")));
    this.addElement(containerElem);
  }

  /**
   * @param menu
   * @return this
   */
  public HBMenuBar addItem(HBMenu menu) {
    this.listElem.addElement(menu);
    return this;
  }

  /**
   * @return this
   */
  public HBMenuBar clear() {
    this.listElem.removeElements();
    return this;
  }

  /**
   * @param menu
   * @return this
   */
  public HBMenuBar removeItem(HBMenu menu) {
    this.listElem.removeElement(menu);
    return this;
  }

  /**
   * @param title
   * @return this
   */
  public HBMenuBar setTitle(String title) {
    this.title.setText(title);
    return this;
  }

  /**
   * @return titulo
   */
  public String getTitle() {
    return this.title.getText();
  }

  /**
   * @param link
   * @return this
   */
  public HBMenuBar setTitleLink(String link) {
    this.title.setLink(link);
    return this;
  }

  /**
   * @return link do titulo
   */
  public String getTitleLink() {
    return this.title.getLink();
  }

  /**
   * @param flag
   * @return this
   */
  public HBMenuBar setBackgroundInverse(boolean flag) {
    this.bginverse = flag;
    this.removeClass("navbar-default");
    this.removeClass("navbar-inverse");
    if (flag) {
      this.addClass("navbar-inverse");
    }
    else {
      this.addClass("navbar-default");
    }
    return this;
  }

}
