package study.rmi;

import java.rmi.RemoteException;

/**
 *
 *
 * @author bernardobreder
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
