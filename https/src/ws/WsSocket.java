package ws;

import java.io.IOException;
import java.util.Map;

/**
 * Comunicação entre o cliente e o servidor
 *
 * @author bernardobreder
 */
public interface WsSocket {

  /**
   * Retorna o cabeçalho
   *
   * @return cabeçalho
   */
  public Map<String, String> getHeader();

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
  public WsSocket setTimeout(int milisegs);

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

}
