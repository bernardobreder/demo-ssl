package browser.diff;

public class TagChildAddDiff implements Diff {

  private final String parentId;

  private String childId;

  public TagChildAddDiff(int parentId, int childId) {
    this.parentId = Integer.toString(parentId);
    this.childId = Integer.toString(childId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String commit() {
    StringBuilder sb = new StringBuilder();
    sb.append("$C[");
    sb.append(parentId);
    sb.append("]");
    sb.append(".append($C[");
    sb.append(childId);
    sb.append("]);");
    return sb.toString();
  }

  @Override
  public void rollback() {
    // TODO Auto-generated method stub

  }

}
