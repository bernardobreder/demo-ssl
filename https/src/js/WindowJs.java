package js;

import javax.script.ScriptException;

import sun.org.mozilla.javascript.internal.IdFunctionCall;

public class WindowJs {

  private final Js js;

  public Object document;

  public WindowJs(Js js) {
    this.js = js;
    try {
      this.document = js.eval("document");
    }
    catch (ScriptException e) {
      e.printStackTrace();
    }
  }

  public void alert(String text) {
    System.out.println("Alert: " + text);
  }

  public void addEventListener(String name, IdFunctionCall function,
    boolean flag) {
  }

}
