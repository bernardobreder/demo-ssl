package ssl;

import java.io.IOException;

import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

public class StandardSSLSocket extends SSLSocket {

  @Override
  public String[] getSupportedCipherSuites() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String[] getEnabledCipherSuites() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setEnabledCipherSuites(String[] suites) {
    // TODO Auto-generated method stub

  }

  @Override
  public String[] getSupportedProtocols() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String[] getEnabledProtocols() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setEnabledProtocols(String[] protocols) {
    // TODO Auto-generated method stub

  }

  @Override
  public SSLSession getSession() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void addHandshakeCompletedListener(HandshakeCompletedListener listener) {
    // TODO Auto-generated method stub

  }

  @Override
  public void removeHandshakeCompletedListener(
    HandshakeCompletedListener listener) {
    // TODO Auto-generated method stub

  }

  @Override
  public void startHandshake() throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public void setUseClientMode(boolean mode) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean getUseClientMode() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void setNeedClientAuth(boolean need) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean getNeedClientAuth() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void setWantClientAuth(boolean want) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean getWantClientAuth() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void setEnableSessionCreation(boolean flag) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean getEnableSessionCreation() {
    // TODO Auto-generated method stub
    return false;
  }

}
