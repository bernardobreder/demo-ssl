package socket.jdk;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Proxy;
import java.net.SocketException;
import java.net.UnknownHostException;

import socket.ISocket;

/**
 *
 *
 * @author bernardobreder
 */
public class StandardSocket implements ISocket {

  /** Socket */
  private final java.net.Socket socket;

  /**
   *
   */
  public StandardSocket() {
    this(new java.net.Socket());
  }

  /**
   * @param address
   * @param port
   * @param localAddr
   * @param localPort
   * @throws IOException
   */
  public StandardSocket(InetAddress address, int port, InetAddress localAddr,
    int localPort) throws IOException {
    this(new java.net.Socket(address, port, localAddr, localPort));
  }

  /**
   * @param address
   * @param port
   * @throws IOException
   */
  public StandardSocket(InetAddress address, int port) throws IOException {
    this(new java.net.Socket(address, port));
  }

  /**
   * @param proxy
   */
  public StandardSocket(Proxy proxy) {
    this(new java.net.Socket(proxy));
  }

  /**
   * @param host
   * @param port
   * @param localAddr
   * @param localPort
   * @throws IOException
   */
  public StandardSocket(String host, int port, InetAddress localAddr,
    int localPort) throws IOException {
    this(new java.net.Socket(host, port, localAddr, localPort));
  }

  /**
   * @param host
   * @param port
   * @throws UnknownHostException
   * @throws IOException
   */
  public StandardSocket(String host, int port) throws UnknownHostException,
    IOException {
    this(new java.net.Socket(host, port));
  }

  /**
   * @param socket
   */
  public StandardSocket(java.net.Socket socket) {
    this.socket = socket;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public InputStream getInputStream() throws IOException {
    return this.socket.getInputStream();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public OutputStream getOutputStream() throws IOException {
    return this.socket.getOutputStream();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
    this.socket.close();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isClosed() {
    return this.socket.isClosed();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getPort() {
    return this.socket.getPort();
  }

  @Override
  public ISocket setTimeout(int milisegs) {
    try {
      this.socket.setSoTimeout(milisegs);
    }
    catch (SocketException e) {
      try {
        this.socket.close();
      }
      catch (IOException e1) {
      }
    }
    return this;
  }

}
