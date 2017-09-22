package ws;

import java.io.IOException;

/**
 * Cria um servidor WebSocket
 *
 * @author bernardobreder
 */
public interface WebServerSocket {

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
  public void addListener(IWsServerSocket.WebServerSocketListener listener);

  /**
   * Remove um listener
   *
   * @return indica se foi encontrado
   * @param listener
   */
  public boolean removeListener(IWsServerSocket.WebServerSocketListener listener);

  /**
   * Remove todos os listeners
   */
  public void clearListeners();

}
