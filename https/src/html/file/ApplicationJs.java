package html.file;

import html.builder.JsBuilder;

/**
 *
 *
 * @author Tecgraf
 */
public class ApplicationJs {

  /** Js Builder */
  protected JsBuilder js = new JsBuilder();

  /**
   * Construtor
   * 
   * @param wsUrl
   */
  public ApplicationJs(String wsUrl) {
    this.guidFunc(js);
    this.componentStruct(js);
    this.websocketStruct(js, wsUrl);
    this.onWebSocketOpenFunc(js);
    this.webSocketFunc(js);
  }

  /**
   * @param js
   */
  protected void componentStruct(JsBuilder js) {
    js.append("var $C = {};");
    js.append("var $H = {};");
  }

  /**
   * @param js
   * @param wsUrl
   */
  protected void websocketStruct(JsBuilder js, String wsUrl) {
    js.append("var $WS = {");
    {
      js.append("url : '" + wsUrl + "',");
      js.append("websocket : null,");
      js.append("session : guid(),");
      js.append("onWebSocketOpen : function(e) {},");
      js.append("onWebSocketClose : function(e) {},");
      js.append("onWebSocketMessage : function(e) {},");
      js.append("onWebSocketError : function(e) {},");
      js.append("onWebSocketConnecting : function() {},");
      js.append("onWebSocketConnected : function() {},");
    }
    js.append("};");
  }

  /**
   * @param js
   */
  protected void guidFunc(JsBuilder js) {
    js.append("function guid() {");
    {
      js.append("var s4 = function() {");
      {
        js.append("return Math.floor((1 + Math.random()) * 0x10000).toString(36);");
      }
      js.append("};");
      js.append("var result = \"\";");
      js.append("for ( var n = 0; n < 128; n++) {");
      {
        js.append("result += s4();");
      }
      js.append("}");
      js.append("return result;");
    }
    js.append("};");
  }

  /**
   * @param js
   */
  protected void webSocketFunc(JsBuilder js) {
    js.append("$WS.websocketStart = function() {");
    {
      js.append("$WS.onWebSocketConnecting();");
      js.append("$WS.websocket = new WebSocket($WS.url);");
      js.append("$WS.websocket.onopen = function(evt) {");
      {
        js.append("$WS.onWebSocketConnected();");
        js.append("$WS.onWebSocketOpen(evt)");
      }
      js.append("};");
      js.append("$WS.websocket.onclose = function(evt) {");
      {
        js.append("$WS.onWebSocketConnecting();");
        js.append("$WS.onWebSocketClose(evt)");
        js.append("setTimeout(function() {");
        {
          js.append("$WS.websocketStart();");
          js.append("}, 1000);");
        }
      }
      js.append("};");
      js.append("$WS.websocket.onmessage = function(evt) {");
      {
        js.append("console.info(evt.data);");
        js.append("eval(evt.data);");
        js.append("$WS.onWebSocketMessage(evt)");
      }
      js.append("};");
      js.append("$WS.websocket.onerror = function(evt) {");
      {
        js.append("$WS.onWebSocketError(evt)");
      }
      js.append("};");
    }
    js.append("};");
  }

  /**
   * @param js
   */
  protected void onWebSocketOpenFunc(JsBuilder js) {
    js.append("$WS.onWebSocketOpen = function() {");
    {
      js.append("$WS.websocket.send('session:' + $WS.session + '\\nurl: ' + window.location.pathname + window.location.search);");
    }
    js.append("};");
  }

  /**
   * @return conteúdo
   */
  public String getContent() {
    return js.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return this.getContent();
  }

}
