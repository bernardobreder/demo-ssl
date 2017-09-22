package browser.diff;

public class TagFreeDiff implements Diff {

  private final String id;

  public TagFreeDiff(int id) {
    this.id = Integer.toString(id);
  }

  @Override
  public String commit() {
    StringBuilder sb = new StringBuilder();
    sb.append("$C[");
    sb.append(id);
    sb.append("]=null;");
    return sb.toString();
  }

  @Override
  public void rollback() {
    // TODO Auto-generated method stub

  }

}
