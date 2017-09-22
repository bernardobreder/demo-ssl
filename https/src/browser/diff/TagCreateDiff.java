package browser.diff;

public class TagCreateDiff implements Diff {

  private final String name;

  private final String id;

  public TagCreateDiff(String name, String id) {
    this.name = name;
    this.id = id;
  }

  @Override
  public String commit() {
    StringBuilder sb = new StringBuilder();
    sb.append("$C[");
    sb.append(id);
    sb.append("]=");
    sb.append("$('<");
    sb.append(this.name);
    sb.append("'>)");
    sb.append(".attr('id','");
    sb.append(this.id);
    sb.append("');");
    return sb.toString();
  }

  @Override
  public void rollback() {
    // TODO Auto-generated method stub

  }

}
