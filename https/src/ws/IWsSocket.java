package ws;

import http.HttpRequest;

import java.io.IOException;

/**
 * Comunicação entre o cliente e o servidor
 *
 * @author bernardobreder
 */
public interface IWsSocket {

  /**
   * @return requisição
   */
  public HttpRequest getRequest();

  /**
   * Recebe uma mensagem da comunicação. Essa operação aguarda enquanto não foi
   * enviada a mensagem ainda por completo.
   *
   * @return mensagem
   * @throws IOException
   */
  public String readMessage() throws IOException;

  /**
   * Envia uma mensagem na comunicação.
   *
   * @param message
   * @throws IOException
   */
  public void sendMessage(String message) throws IOException;

  /**
   * Atribui um timeout
   *
   * @param milisegs
   * @return this
   */
  public IWsSocket setTimeout(int milisegs);

  /**
   * Finaliza a comunicação
   *
   * @throws IOException
   */
  public void close() throws IOException;

  /**
   * Indica se está fechado
   *
   * @return fechado
   */
  public boolean isClosed();

  /**
   * @param listener
   */
  public void addListener(IWsSocketListener listener);

  /**
   * @param listener
   * @return indica se removeu
   */
  public boolean removeListener(IWsSocketListener listener);

  /**
   * Remove todos os listeners
   */
  public void clearListeners();

  /**
   *
   *
   * @author Tecgraf/PUC-Rio
   */
  public static interface IWsSocketListener {

    /**
     * Evento do socket fechado
     */
    public void closed();

    /**
     * @param text
     */
    public void receive(String text);

    /**
     * @param text
     */
    public void send(String text);

    /**
     * @param e
     */
    public void error(Throwable e);

  }

}
