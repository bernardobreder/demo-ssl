package browser.diff;

public class TagTextSetDiff implements Diff {

  private final String id;

  private final String text;

  public TagTextSetDiff(int id, String text) {
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
    sb.append(".text('");
    sb.append(this.text);
    sb.append("');");
    return sb.toString();
  }

  @Override
  public void rollback() {
    // TODO Auto-generated method stub

  }

}
