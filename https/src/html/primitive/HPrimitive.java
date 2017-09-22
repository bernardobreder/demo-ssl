package html.primitive;

import html.HTagElement;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public abstract class HPrimitive extends HTagElement {

  /**
   * @param name
   */
  public HPrimitive(String name) {
    super(name);
  }

  /**
   * Nome da tag
   *
   * @return nome da tag
   */
  protected abstract String getTagName();

  /**
   * Conteúdo
   *
   * @return conteúdo
   */
  protected String getContent() {
    return null;
  }

}
