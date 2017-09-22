package browser.diff;

public class TagClassAddDiff implements Diff {

  private final String id;

  private final String name;

  public TagClassAddDiff(int id, String name) {
    this.id = Integer.toString(id);
    this.name = name;
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
    sb.append(".addClass('");
    sb.append(this.name);
    sb.append("');");
    return sb.toString();
  }

  @Override
  public void rollback() {
    // TODO Auto-generated method stub

  }

}
