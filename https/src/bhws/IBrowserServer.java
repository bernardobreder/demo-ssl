package bhws;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public interface IBrowserServer {

  /**
   * Inicia o servidor
   *
   * @return this
   */
  public IBrowserServer start();

  /**
   * Fecha o servidor
   */
  public void close();

}
