package study.rmi;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 *
 * @author bernardobreder
 */
public interface LoginService extends Remote, Serializable {

  /**
   * @param username
   * @param password
   * @return usuário encontrado
   * @throws RemoteException
   */
  public boolean login(String username, char[] password) throws RemoteException;

}
