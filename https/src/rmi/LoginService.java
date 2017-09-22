package rmi;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
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
