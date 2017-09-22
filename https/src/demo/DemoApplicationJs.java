package demo;

import html.builder.JsBuilder;
import html.file.ApplicationJs;

/**
 *
 *
 * @author bernardobreder
 */
public class DemoApplicationJs extends ApplicationJs {

  /**
   * @param wsUrl
   */
  public DemoApplicationJs(String wsUrl) {
    super(wsUrl);
    this.onWebSocketOpenFunc(js);
    this.onWebSocketMessageFunc(js);
    this.onWebSocketConnectedFunc(js);
    this.onWebSocketConnectingFunc(js);
    this.onWebSocketStartAction(js);
  }

  /**
   * @param js
   */
  protected void onWebSocketStartAction(JsBuilder js) {
    js.append("$(function() { $WS.websocketStart(); });");
  }

  /**
   * @param js
   */
  protected void onWebSocketMessageFunc(JsBuilder js) {
    js.append("$WS.onWebSocketMessage = function() {");
    {
      js.append("$('body').tooltip({selector : '[title]', container : 'body'});");
    }
    js.append("};");
  }

  /**
   * @param js
   */
  protected void onWebSocketConnectedFunc(JsBuilder js) {
    js.append("$WS.onWebSocketConnected = function() {");
    js.append("};");
  }

  /**
   * @param js
   */
  protected void onWebSocketConnectingFunc(JsBuilder js) {
    js.append("$WS.onWebSocketConnecting = function() {");
    js.append("};");
  }

}
