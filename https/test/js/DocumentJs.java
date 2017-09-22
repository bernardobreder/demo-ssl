package js;

import javax.script.ScriptException;

public class DocumentJs {

  private final Js js;

  public DocumentJs(Js js) {
    this.js = js;
  }

  public void write(String text) {
    System.out.println("Write: " + text);
  }

  public Object defaultView() throws ScriptException {
    return js.eval("window");
  }

}
