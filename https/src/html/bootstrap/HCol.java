package html.bootstrap;

import html.primitive.HDiv;

public class HCol extends HDiv {

  public HCol(String responsive, int size) {
    this.addClass("col-" + responsive + "-" + size);
  }

}
