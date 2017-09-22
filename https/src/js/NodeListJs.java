package js;

import org.w3c.dom.NodeList;

import sun.org.mozilla.javascript.internal.NativeArray;

public class NodeListJs extends NativeArray {

  public NodeList list;

  public NodeListJs(NodeList list) {
    super(list.getLength());
    for (int n = 0; n < list.getLength(); n++) {
      NodeJs value = new NodeJs(list.item(n));
      this.put(n, this, value);
    }
  }

}
