package jtecweb.demo;

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
   * @param session
   */
  public ApplicationJs(String wsUrl, String session) {
    this.componentStruct(js);
    this.websocketStruct(js, wsUrl, session);
    this.onWebSocketOpenFunc(js);
    this.webSocketFunc(js);
    js.append("$WS.websocketStart();");
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
   * @param session
   */
  protected void websocketStruct(JsBuilder js, String wsUrl, String session) {
    js.append("var $WS = {");
    {
      js.append("url : '%s',", wsUrl);
      js.append("session : '%s',", session);
      js.append("websocket : null,");
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
