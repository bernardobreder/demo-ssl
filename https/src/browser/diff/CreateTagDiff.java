package browser.diff;

import html.HElement;

public class CreateTagDiff implements Diff {

  private final HElement elem;

  public CreateTagDiff(HElement elem) {
    this.elem = elem;
  }

  @Override
  public String commit() {
    StringBuilder sb = new StringBuilder();
    sb.append("$('body').empty();");
    if (this.elem != null) {
      sb.append("$('body').append(");
      sb.append(this.elem.toHtml());
      sb.append(");");
    }
    return sb.toString();
  }

  @Override
  public void rollback() {
    // TODO Auto-generated method stub

  }

}
