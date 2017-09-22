package html.javascript;

import html.builder.JsBuilder;
import util.JsonOutputStream;
import util.TreeMapBuilder;

/**
 *
 *
 * @author bernardobreder
 */
public class NativeJavaScript implements JavaScript {

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
    return String.format("$C[%d].setAttribute('%s','%s');", id, key, value);
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
    return String.format("$C[%d].removeAttribute('%s');", id, key);
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
    return String.format("$C[%d].classList.add('%s');", id, name);
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
    return String.format("$C[%d].classList.remove('%s');", id, name);
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
    return String.format("$C[%d]=document.createElement('%s');", id, name);
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
    return String.format("$C[%d].style['%s']='%s';", id, key, value);
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
    return String.format("$C[%d].style.removeProperty('%s');", id, key);
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
    return String.format("$C[%d].appendChild($C[%d]);", parentId, childId);
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  @Override
  public String empty(int id) {
    return String.format("$C[%d].innerHTML='';", id);
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
    return String.format("$C[%d].innerText='%s';", id, fixString(text));
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
    return String.format("$C[%d].innerHTML='%s';", id, fixString(text));
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  @Override
  public String disable(int id) {
    return this.addAttribute(id, "disabled", "disabled");
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  @Override
  public String enable(int id) {
    return this.removeAttribute(id, "disabled");
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
    js.appendInline("$C[%d].onclick=function(e){", id);
    {
      this.disable(id);
      String json =
        new JsonOutputStream().writeMap(
          new TreeMapBuilder<String, Object>().add("method", "action").add(
            "target", id).add("event", "click").add("x",
            new JsonOutputStream.Id("e.pageX")).add("y",
            new JsonOutputStream.Id("e.pageY"))).toString();
      js.appendInline("$WS.websocket.send(JSON.stringify(%s));", json);
    }
    js.appendInline("};");
    return js.toString();
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  @Override
  public String removeEventClick(int id) {
    return String.format("$C[%d].removeEventListener('click');", id);
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
    return "document.body.innerHTML='';";
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  @Override
  public String bodyHtml(int id) {
    return String.format("document.body.appendChild($C[%d]);", id);
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

  /**
   * {@inheritDoc}
   */
  @Override
  public String addEventDoubleClick(int id) {
    throw new RuntimeException();
  }

}
