package browser.diff;

public class TagMouseClickAddDiff implements Diff {

  private final String id;

  public TagMouseClickAddDiff(int id) {
    this.id = Integer.toString(id);
  }

  @Override
  public String commit() {
    StringBuilder sb = new StringBuilder();
    sb.append("$C[");
    sb.append(id);
    sb.append("].bind('click',function(e){$WS.send('action:click:");
    sb.append(id);
    sb.append(":'+e.pageX+','+e.pageY);});");
    return sb.toString();
  }

  @Override
  public void rollback() {
    // TODO Auto-generated method stub

  }

}
