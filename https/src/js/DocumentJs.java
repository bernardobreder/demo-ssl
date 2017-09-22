package js;

import javax.script.ScriptException;

import sun.org.mozilla.javascript.internal.IdFunctionCall;

public class DocumentJs {

  private final Js js;

  public NodeListJs childNodes;

  public DocumentJs(Js js) {
    this.js = js;
    this.childNodes = new NodeListJs(js.getDocument().getChildNodes());
  }

  public void write(String text) {
    System.out.println("Write: " + text);
  }

  public Object defaultView() throws ScriptException {
    return js.eval("window");
  }

  public Object createElement(String name) throws ScriptException {
    return new Object();
  }

  public void addEventListener(String name, IdFunctionCall function,
    boolean flag) {
  }

}
