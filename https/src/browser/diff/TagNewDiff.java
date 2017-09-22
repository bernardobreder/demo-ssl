package browser.diff;

public class TagNewDiff implements Diff {

  private final String name;

  private final String id;

  public TagNewDiff(String name, int id) {
    this.name = name;
    this.id = Integer.toString(id);
  }

  @Override
  public String commit() {
    StringBuilder sb = new StringBuilder();
    sb.append("$C[");
    sb.append(id);
    sb.append("]=");
    sb.append("$('<");
    sb.append(this.name);
    sb.append(">')");
    //    sb.append(".attr('id','");
    //    sb.append(this.id);
    //    sb.append("');");
    sb.append(";");
    return sb.toString();
  }

  @Override
  public void rollback() {
    // TODO Auto-generated method stub

  }

}
