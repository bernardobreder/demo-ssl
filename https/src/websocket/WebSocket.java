package websocket;

import java.io.IOException;

/**
 * Comunicação entre o cliente e o servidor
 *
 * @author bernardobreder
 */
public interface WebSocket {

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
   * Finaliza a comunicação
   * 
   * @throws IOException
   */
  public void close() throws IOException;

}
