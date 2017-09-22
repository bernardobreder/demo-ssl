import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public class CriphyTest {

  /**
   * @param args
   * @throws IllegalBlockSizeException
   * @throws BadPaddingException
   * @throws NoSuchAlgorithmException
   * @throws NoSuchPaddingException
   * @throws NoSuchProviderException
   * @throws InvalidKeyException
   */
  public static void main(String[] args) throws IllegalBlockSizeException,
    BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException,
    NoSuchProviderException, InvalidKeyException {
    SecureRandom random = new SecureRandom();
    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");

    generator.initialize(2048, random); // chaves de 2048 bits
    KeyPair pair = generator.generateKeyPair();
    PublicKey pubKey = pair.getPublic();
    PrivateKey privKey = pair.getPrivate(); // chave privada
    System.out.println(pubKey);
    System.out.println(privKey);

    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, pubKey, random);
    // mensagem cifrada
    byte[] cipherText = cipher.doFinal("abc".getBytes());
    // decifra com a chave privada
    cipher.init(Cipher.DECRYPT_MODE, privKey);
    // mensagem decifrada
    byte[] plainText = cipher.doFinal(cipherText);
    System.out.println(new String(plainText));
  }

}
