package html.bootstrap;

import html.primitive.HSelect;

public class HBSelect extends HSelect {

  public HBSelect(String... options) {
    super(options);
    this.addClass("form-control");
  }

}
