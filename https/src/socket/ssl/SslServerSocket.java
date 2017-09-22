package socket.ssl;

import java.io.IOException;

import javax.net.ssl.SSLServerSocket;

import socket.IServerSocket;
import socket.ISocket;

/**
 *
 *
 * @author bernardobreder
 */
public class SslServerSocket implements IServerSocket {

  /** Server */
  private SSLServerSocket server;

  /**
   * @param server
   */
  public SslServerSocket(SSLServerSocket server) {
    this.server = server;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ISocket accept() throws IOException {
    return new SslSocket(this.server.accept());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTimeout(int timeout) throws IOException {
    this.server.setSoTimeout(timeout);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
    this.server.close();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isClosed() {
    return this.server.isClosed();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getPort() {
    return this.server.getLocalPort();
  }

  @Override
  public String getHost() {
    return "wss://" + this.server.getInetAddress().getHostAddress();
  }

}
