package html;

import html.javascript.JavaScript;

import java.io.IOException;
import java.util.Map;

/**
 * Implementação que armazena o contexto do navegador do cliente
 *
 * @author Tecgraf/PUC-Rio
 */
public interface IBrowser {

  /** Navegadores */
  public static final ThreadLocal<IBrowser> browers =
    new ThreadLocal<IBrowser>();

  /**
   * @param tree
   * @throws IOException
   */
  public abstract void message(Map<String, Object> tree) throws IOException;

  /**
   * Inicializa o cliente
   *
   * @param url
   * @return elemento
   * @throws IOException
   */
  public abstract IBrowser goTo(String url) throws IOException;

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

  /**
   * Remove um elemento do navegador
   *
   * @param id
   */
  public abstract void removeElement(int id);

  /**
   * Dispara evento de mudança
   *
   * @return this
   */
  public IBrowser fireChanged();

  /**
   * @return mudanças
   */
  public String consumeChanges();

  /**
   * Listener do navegador do cliente
   *
   * @author bernardobreder
   */
  public static interface IBrowserSync {

    /**
     * Indica que foi fechado o navegador
     *
     * @param browser
     */
    public void run(IBrowser browser);

  }

}