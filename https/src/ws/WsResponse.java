package ws;

import java.io.IOException;
import java.io.InputStream;

import util.Bytes;

/**
 * Classe responsável por responder a uma pergunta do usuário através do
 * Websocket.
 *
 * @author Tecgraf/PUC-Rio
 */
public class WsResponse {

  /** Saída da mensagem */
  private final StringBuilder output = new StringBuilder();
  /** Socket Websocket */
  private final IWsSocket socket;

  /**
   * @param socket
   */
  public WsResponse(IWsSocket socket) {
    this.socket = socket;
  }

  /**
   * Escreve uma mensagem para o cliente formatada ou não.
   * 
   * @param format
   * @param objects
   * @return this
   */
  public WsResponse write(String format, Object... objects) {
    if (objects.length > 0) {
      format = String.format(format, objects);
    }
    this.output.append(format);
    return this;
  }

  /**
   * Indica se foi escrito alguma coisa
   * 
   * @return saída é vazia
   */
  public boolean isEmpty() {
    return this.output.length() == 0;
  }

  /**
   * @return envia para o cliente a resposta
   * @throws IOException
   */
  public WsResponse flush() throws IOException {
    this.socket.sendMessage(output.toString());
    output.delete(0, output.length());
    return this;
  }

  /**
   * @return fecha a resposta
   * @throws IOException
   */
  public WsResponse close() throws IOException {
    return this.flush();
  }

  /**
   * @param input
   * @return this
   * @throws IOException
   */
  public WsResponse write(InputStream input) throws IOException {
    output.append(Bytes.toString(input));
    return this;
  }

}
