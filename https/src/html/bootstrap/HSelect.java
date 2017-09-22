package html.bootstrap;

import html.primitive.HPrimitive;

public class HSelect extends HPrimitive {

  public HSelect(String... options) {
    super("select");
    for (String option : options) {
      this.addElement(new HOption(option));
    }
  }

  @Override
  protected String getTagName() {
    return "select";
  }

  public static class HOption extends HPrimitive {

    private final String text;

    public HOption(String text) {
      super("option");
      this.text = text;
    }

    @Override
    protected String getTagName() {
      return "option";
    }

    @Override
    protected String getContent() {
      return this.text;
    }

  }

}
