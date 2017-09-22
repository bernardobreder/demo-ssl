package html.javascript;

import html.builder.JsBuilder;
import util.JsonOutputStream;
import util.TreeMapBuilder;

/**
 *
 *
 * @author bernardobreder
 */
public class JqueryJavaScript implements JavaScript {

  /**
   * Atribua um atributo a uma tag
   * 
   * @param id
   * @param key
   * @param value
   * @return código
   */
  @Override
  public String addAttribute(int id, String key, Object value) {
    return String.format("$C[%d].attr('%s','%s');", id, key, value);
  }

  /**
   * Atribua um atributo a uma tag
   * 
   * @param id
   * @param key
   * @return código
   */
  @Override
  public String removeAttribute(int id, String key) {
    return String.format("$C[%d].attr('%s',null);", id, key);
  }

  /**
   * Acrescente uma classe
   * 
   * @param id
   * @param name
   * @return código
   */
  @Override
  public String addClass(int id, String name) {
    return String.format("$C[%d].addClass('%s');", id, name);
  }

  /**
   * Acrescente uma classe
   * 
   * @param id
   * @param name
   * @return código
   */
  @Override
  public String removeClass(int id, String name) {
    return String.format("$C[%d].removeClass('%s');", id, name);
  }

  /**
   * Armazene uma tag em memória
   * 
   * @param id
   * @param name
   * @return código
   */
  @Override
  public String store(int id, String name) {
    return String.format("$C[%d]=$('<%s>');", id, name);
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @param key
   * @param value
   * @return código
   */
  @Override
  public String addCss(int id, String key, Object value) {
    return String.format("$C[%d].css('%s','%s');", id, key, value);
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @param key
   * @return código
   */
  @Override
  public String removeCss(int id, String key) {
    return String.format("$C[%d].css('%s',null);", id, key);
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param parentId
   * @param childId
   * @return código
   */
  @Override
  public String append(int parentId, int childId) {
    return String.format("$C[%d].append($C[%d]);", parentId, childId);
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  @Override
  public String empty(int id) {
    return String.format("$C[%d].empty();", id);
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @param text
   * @return código
   */
  @Override
  public String text(int id, String text) {
    return String.format("$C[%d].text('%s');", id, fixString(text));
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @param text
   * @return código
   */
  @Override
  public String html(int id, String text) {
    return String.format("$C[%d].html('%s');", id, fixString(text));
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  @Override
  public String disable(int id) {
    return String.format("$C[%d].attr('disabled','disabled');", id);
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  @Override
  public String enable(int id) {
    return String.format("$C[%d].attr('disabled',null);", id);
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  @Override
  public String remoteElement(int id) {
    return String.format("delete $C[%d];", id);
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  @Override
  public String addEventClick(int id) {
    JsBuilder js = new JsBuilder();
    js.appendInline("$C[%d].bind('click',function(e){", id);
    {
      js.appendInline("$C[%d].attr('disabled','disabled');", id);
      String json =
        new JsonOutputStream().writeMap(
          new TreeMapBuilder<String, Object>().add("method", "action").add(
            "target", id).add("event", "click").add("x",
            new JsonOutputStream.Id("e.pageX")).add("y",
            new JsonOutputStream.Id("e.pageY"))).toString();
      js.appendInline("$WS.websocket.send(JSON.stringify(%s));", json);
    }
    js.appendInline("});");
    return js.toString();
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  @Override
  public String addEventDoubleClick(int id) {
    return String.format("$C[%d].bind('dblclick',function(e){"
      + "$C[%d].attr('disabled','disabled');"
      + "$WS.websocket.send('action:%d:dblclick:'+e.pageX+','+e.pageY);});",
      id, id, id);
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  @Override
  public String removeEventClick(int id) {
    return String.format("$C[%d].unbind('click');", id);
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param text
   * @return código
   */
  @Override
  public String alert(String text) {
    return String.format("alert('%s');", fixString(text));
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @return código
   */
  @Override
  public String bodyEmpty() {
    return "$('body').empty();$('body').scrollTop(0);";
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  @Override
  public String bodyHtml(int id) {
    return String.format("$('body').html($C[%d]);$('body').scrollTop(0);", id);
  }

  /**
   * @param text
   * @return texto
   */
  protected String fixString(String text) {
    return text.replace("\\", "\\\\").replace("'", "\\'").replace("\"", "\\\"")
      .replace("\n", "\\n").replace("\r", "\\r");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String pushState(String url) {
    return String.format("history.pushState('%s', null, '%s');", url, url);
  }

}
