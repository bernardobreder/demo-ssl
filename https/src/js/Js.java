package js;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.w3c.dom.Document;

public class Js {

  /** Engine */
  private ScriptEngine engine;
  /** Documento */
  private final Document document;

  public Js(Document document) {
    this.document = document;
    ScriptEngineManager manager = new ScriptEngineManager();
    engine = manager.getEngineByName("javascript");
    engine.getContext().setAttribute("document", new DocumentJs(this),
      ScriptContext.GLOBAL_SCOPE);
    engine.getContext().setAttribute("window", new WindowJs(this),
      ScriptContext.GLOBAL_SCOPE);
  }

  public Object eval(String format, Object... objects) throws ScriptException {
    if (objects.length > 0) {
      format = String.format(format, objects);
    }
    try {
      return engine.eval(format);
    }
    catch (ScriptException e) {
      if (e.getMessage().contains("ReferenceError:")) {
        return null;
      }
      else {
        throw e;
      }
    }
  }

  public Document getDocument() {
    return document;
  }

  // public static void main(String[] args) throws IOException {
  // String user = "bbreder";
  // String pass = "1234";
  // String url = "/";
  // for (int n = 0; n < 1024; n++) {
  // Document doc = open(user, pass, url);
  // doc.select("script");
  // System.out.println(n);
  // }
  // }

}
