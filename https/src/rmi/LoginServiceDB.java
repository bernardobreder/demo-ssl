package rmi;

import java.rmi.RemoteException;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public class LoginServiceDB implements LoginService {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean login(String username, char[] password) throws RemoteException {
    if (username.toLowerCase().equals("bbreder")) {
      return true;
    }
    return false;
  }

}
