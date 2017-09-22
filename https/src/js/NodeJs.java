package js;

import org.w3c.dom.Node;

import sun.org.mozilla.javascript.internal.NativeJavaObject;
import sun.org.mozilla.javascript.internal.Scriptable;

public class NodeJs extends NativeJavaObject {

  private Node node;

  public NodeJs(Node node) {
    this.node = node;
  }

  @Override
  public Object get(int arg0, Scriptable arg1) {
    return super.get(arg0, arg1);
  }

}
