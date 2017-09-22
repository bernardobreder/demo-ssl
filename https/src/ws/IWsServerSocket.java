package ws;

import java.io.IOException;

/**
 * Cria um servidor WebSocket
 *
 * @author bernardobreder
 */
public interface IWsServerSocket {

  /**
   * Recebe uma comunicação. Essa comunicação só será aceita se for realiza os
   * devidos comprimentos de acordo com o protocolo de comunicação WebSocket.
   *
   * @return Socket do WebSocket
   * @throws IOException
   */
  public WsSocket accept() throws IOException;

  /**
   * Finaliza o servidor.
   *
   * @throws IOException
   */
  public void close() throws IOException;

  /**
   * Adiciona um listener
   *
   * @param listener
   */
  public void addListener(WebServerSocketListener listener);

  /**
   * Remove um listener
   *
   * @return indica se foi encontrado
   * @param listener
   */
  public boolean removeListener(WebServerSocketListener listener);

  /**
   * Remove todos os listeners
   */
  public void clearListeners();

  /**
   * Interface de eventos do servidor
   *
   * @author bernardobreder
   *
   */
  public static interface WebServerSocketListener {

    /**
     * Evento de aceitação da comunicação entre o cliente e o servidor
     *
     * @param socket
     */
    public void acceptWebSocket(IWsSocket socket);

    /**
     * Evento do servidor sendo fechado
     */
    public void closeServer();

    /**
     * Evento de erro interno
     *
     * @param t
     */
    public void error(Throwable t);

  }

}
