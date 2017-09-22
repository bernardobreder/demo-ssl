package socket.monitor;

import java.io.IOException;

import socket.IServerSocket;
import socket.ISocket;

/**
 *
 *
 * @author bernardobreder
 */
public class MonitorServerSocket implements IServerSocket {

  /** Servidor */
  private final IServerSocket server;
  private final String owner;

  /**
   * @param owner
   * @param server
   */
  public MonitorServerSocket(String owner, IServerSocket server) {
    this.owner = owner;
    this.server = server;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ISocket accept() throws IOException {
    return new MonitorSocket(this.server.accept(), owner);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTimeout(int timeout) throws IOException {
    this.server.setTimeout(timeout);
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
    return this.server.getPort();
  }

  @Override
  public String getHost() {
    return this.server.getHost();
  }

}
