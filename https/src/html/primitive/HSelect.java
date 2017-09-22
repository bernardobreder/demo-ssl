package html.primitive;

import html.HElement;

public class HSelect extends HElement {

  public HSelect(String... options) {
    super("select");
    for (String option : options) {
      this.addElement(new HOption(option));
    }
  }

  public static class HOption extends HTextElement {

    public HOption(String text) {
      super("option");
      this.setText(text);
    }

  }

}
