package html.bootstrap;

/**
 * Cor padrão para aviso
 *
 * @author bernardobreder
 */
public enum HBColor {

  /**
   * Cor cinza
   */
  MUTED,
  /**
   * Azul escuro
   */
  PRIMARY,
  /**
   * Verde
   */
  SUCCESS,
  /**
   * Azul claro
   */
  INFO,
  /**
   * Amarelo
   */
  WARNING,
  /**
   * Vermelho
   */
  DANGER;

  /**
   * @return nome
   */
  public String text() {
    return this.name().toLowerCase();
  }

}
