package html.javascript;

/**
 *
 * @author bernardobreder
 */
public interface JavaScript {

  /**
   * Atribua um atributo a uma tag
   * 
   * @param id
   * @param key
   * @param value
   * @return código
   */
  public abstract String addAttribute(int id, String key, Object value);

  /**
   * Atribua um atributo a uma tag
   * 
   * @param id
   * @param key
   * @return código
   */
  public abstract String removeAttribute(int id, String key);

  /**
   * Acrescente uma classe
   * 
   * @param id
   * @param name
   * @return código
   */
  public abstract String addClass(int id, String name);

  /**
   * Acrescente uma classe
   * 
   * @param id
   * @param name
   * @return código
   */
  public abstract String removeClass(int id, String name);

  /**
   * Armazene uma tag em memória
   * 
   * @param id
   * @param name
   * @return código
   */
  public abstract String store(int id, String name);

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @param key
   * @param value
   * @return código
   */
  public abstract String addCss(int id, String key, Object value);

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @param key
   * @return código
   */
  public abstract String removeCss(int id, String key);

  /**
   * Insere no final um filho a uma tag
   * 
   * @param parentId
   * @param childId
   * @return código
   */
  public abstract String append(int parentId, int childId);

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  public abstract String empty(int id);

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @param text
   * @return código
   */
  public abstract String text(int id, String text);

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @param text
   * @return código
   */
  public abstract String html(int id, String text);

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  public abstract String disable(int id);

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  public abstract String enable(int id);

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  public abstract String remoteElement(int id);

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  public abstract String addEventClick(int id);

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  public abstract String addEventDoubleClick(int id);

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  public abstract String removeEventClick(int id);

  /**
   * Insere no final um filho a uma tag
   * 
   * @param text
   * @return código
   */
  public abstract String alert(String text);

  /**
   * Insere no final um filho a uma tag
   * 
   * @return código
   */
  public abstract String bodyEmpty();

  /**
   * Insere no final um filho a uma tag
   * 
   * @param id
   * @return código
   */
  public abstract String bodyHtml(int id);

  /**
   * @param url
   * @return código
   */
  public abstract String pushState(String url);

}