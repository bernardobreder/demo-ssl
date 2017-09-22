package browser.diff;

import html.HElement;

public class SetRootDiff implements Diff {

  private final HElement elem;

  public SetRootDiff(HElement elem) {
    this.elem = elem;
  }

  @Override
  public String commit() {
    StringBuilder sb = new StringBuilder();
    if (this.elem != null) {
      sb.append("$('body').html($C['");
      sb.append(this.elem.getId());
      sb.append("']);");
    }
    else {
      sb.append("$('body').empty();");
    }
    return sb.toString();
  }

  @Override
  public void rollback() {
    // TODO Auto-generated method stub

  }

}
