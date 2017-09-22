package socket.jdk;

import java.io.IOException;
import java.net.InetAddress;

import socket.IServerSocket;
import socket.ISocket;

/**
 *
 *
 * @author bernardobreder
 */
public class StandardServerSocket implements IServerSocket {

  /** Servidor */
  private java.net.ServerSocket server;

  /**
   * @param server
   */
  public StandardServerSocket(java.net.ServerSocket server) {
    this.server = server;
  }

  /**
   * @throws IOException
   */
  public StandardServerSocket() throws IOException {
    this(new java.net.ServerSocket());
  }

  /**
   * @param port
   * @param backlog
   * @param bindAddr
   * @throws IOException
   */
  public StandardServerSocket(int port, int backlog, InetAddress bindAddr)
    throws IOException {
    this(new java.net.ServerSocket(port, backlog, bindAddr));
  }

  /**
   * @param port
   * @param backlog
   * @throws IOException
   */
  public StandardServerSocket(int port, int backlog) throws IOException {
    this(new java.net.ServerSocket(port, backlog));
  }

  /**
   * @param port
   * @throws IOException
   */
  public StandardServerSocket(int port) throws IOException {
    this(new java.net.ServerSocket(port));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ISocket accept() throws IOException {
    return new StandardSocket(this.server.accept());
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

  /**
   * {@inheritDoc}
   */
  @Override
  public String getHost() {
    return "ws://" + this.server.getInetAddress().getHostAddress();
  }

}
