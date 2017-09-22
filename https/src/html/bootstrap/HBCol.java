package html.bootstrap;

import html.primitive.HDiv;

public class HBCol extends HDiv {

  public HBCol(String responsive, int size) {
    this.addClass("col-" + responsive + "-" + size);
  }

}
