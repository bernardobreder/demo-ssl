package browser.diff;

public class TagHtmlSetDiff implements Diff {

  private final String id;

  private final String text;

  public TagHtmlSetDiff(int id, String text) {
    this.id = Integer.toString(id);
    this.text = text;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String commit() {
    StringBuilder sb = new StringBuilder();
    sb.append("$C[");
    sb.append(id);
    sb.append("]");
    sb.append(".html('");
    sb.append(this.text);
    sb.append("');");
    return sb.toString();
  }

  @Override
  public void rollback() {
    // TODO Auto-generated method stub

  }

}
