package browser.diff;

public class TagAttrSetDiff implements Diff {

  private final String id;

  private final String key;

  private final String value;

  public TagAttrSetDiff(int id, String key, String value) {
    this.id = Integer.toString(id);
    this.key = key;
    this.value = value;
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
    sb.append(".attr('");
    sb.append(this.key);
    sb.append("','");
    sb.append(this.value);
    sb.append("');");
    return sb.toString();
  }

  @Override
  public void rollback() {
    // TODO Auto-generated method stub

  }

}
