package js;

import javax.script.ScriptException;

public class WindowJs {

  private final Js js;

  public WindowJs(Js js) {
    this.js = js;
  }

  public void write(String text) {
    System.out.println("Write: " + text);
  }

  public Object document(String text) throws ScriptException {
    return js.eval("document");
  }

}
