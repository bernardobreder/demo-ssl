package html;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public class JavaScript {

  /**
   * Atribua um atributo a uma tag
   * 
   * @param id
   * @param key
   * @param value
   * @return código
   */
  public static String addAttribute(int id, String key, Object value) {
    return String.format("$C[%d].attr(\"%s\",\"%s\");", id, key, value);
  }

  /**
   * Atribua um atributo a uma tag
   * 
   * @param id
   * @param key
   * @return código
   */
  public static String removeAttribute(int id, String key) {
    return String.format("$C[%d].attr(\"%s\",null);", id, key);
  }

  /**
   * Acrescente uma classe
   * 
   * @param id
   * @param name
   * @return código
   */
  public static String addClass(int id, String name) {
    return String.format("$C[%d].addClass(\"%s\");", id, name);
  }

  /**
   * Acrescente uma classe
   * 
   * @param id
   * @param name
   * @return código
   */
  public static String removeClass(int id, String name) {
    return String.format("$C[%d].removeClass(\"%s\");", id, name);
  }

  /**
   * Armazene uma tag em memória
   * 
   * @param id
   * @param name
   * @return código
   */
  public static String store(int id, String name) {
    return String.format("$C[%d]=$(\"<%s>\");", id, name);
  }

  /**
   * Libere uma tag da memória
   * 
   * @param id
   * @return código
   */
  public static String free(int id) {
    return String.format("$C[%d]=null;", id);
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @param key
   * @param value
   * @return código
   */
  public static String addCss(int id, String key, Object value) {
    return String.format("$C[%d].css(\"%s\",\"%s\");", id, key, value);
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @param key
   * @return código
   */
  public static String removeCss(int id, String key) {
    return String.format("$C[%d].css(\"%s\",null);", id, key);
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param parentId
   * @param childId
   * @return código
   */
  public static String append(int parentId, int childId) {
    return String.format("$C[%d].append($C[%d]);", parentId, childId);
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  public static String empty(int id) {
    return String.format("$C[%d].empty();", id);
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @param text
   * @return código
   */
  public static String text(int id, String text) {
    return String.format("$C[%d].text(\"%s\");", id, text.replace("\n", "\\n")
      .replace("\r", "\\r"));
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @param text
   * @return código
   */
  public static String html(int id, String text) {
    return String.format("$C[%d].html(\"%s\");", id, text);
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  public static String disable(int id) {
    return String.format("$C[%d].attr(\"disabled\",\"disabled\");", id);
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  public static String enable(int id) {
    return String.format("$C[%d].attr(\"disabled\",null);", id);
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  public static String remoteElement(int id) {
    return String.format("$C[%d].remove();", id);
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  public static String addEventClick(int id) {
    return String.format("$C[%d].bind(\"click\",function(e){"
      + "$C[%d].attr(\"disabled\",\"disabled\");"
      + "$WS.websocket.send(\"action:%d:click:\"+e.pageX+\",\"+e.pageY);});",
      id, id, id);
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  public static String removeEventClick(int id) {
    return String.format("$C[%d].unbind(\"click\");", id);
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param text
   * @return código
   */
  public static String alert(String text) {
    return String.format("alert(\"%s\");", text);
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @return código
   */
  public static String bodyEmpty() {
    return "$(\"body\").empty();$(\"body\").scrollTop(0);";
  }

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  public static String bodyHtml(int id) {
    return String.format("$(\"body\").html($C[%d]);$(\"body\").scrollTop(0);",
      id);
  }

}
