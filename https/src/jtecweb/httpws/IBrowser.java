package jtecweb.httpws;

import html.HElement;
import html.IBrowser.IBrowserSync;
import html.javascript.JavaScript;

/**
 * Implementação que armazena o contexto do navegador do cliente
 *
 * @author Tecgraf/PUC-Rio
 */
public interface IBrowser {

  /**
   * Inicializa o cliente
   *
   * @param url
   * @return elemento
   */
  public abstract IBrowser goTo(String url);

  /**
   * @param runnable
   */
  public abstract void sync(IBrowserSync runnable);

  /**
   * @param change
   */
  public abstract void addChange(String change);

  /**
   * @return root
   */
  public abstract HElement getRoot();

  /**
   * @param root
   */
  public abstract void setRoot(HElement root);

  /**
   * Apresenta um alerta no navegador do usuário
   *
   * @param message
   */
  public abstract void showMessage(String message);

  /**
   * @return the javascript
   */
  public abstract JavaScript getJavascript();

  /**
   * @param javascript the javascript to set
   */
  public abstract void setJavascript(JavaScript javascript);

  /**
   * Adiciona um elemento html no navegador do usuário
   *
   * @param element
   */
  public abstract void addElement(HElement element);

  /**
   * Retorna um elemento html do navegador do usuário
   *
   * @param id
   * @return elemento html
   */
  public abstract HElement getElement(int id);

}