package rsa;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;

public class RSA {

  private static final PublicKey pubKey;

  private static final PrivateKey privKey;

  private static final SecureRandom random;

  private static final Cipher cipher;

  static {
    try {
      random = new SecureRandom(SecureRandom.getSeed(256));
      KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
      generator.initialize(256 * 2, random);
      KeyPair pair = generator.generateKeyPair();
      pubKey = pair.getPublic();
      privKey = pair.getPrivate();
      cipher = Cipher.getInstance("RSA");
    }
    catch (Exception e) {
      throw new Error(e);
    }
  }

  public static byte[] encrypt(byte[] bytes) throws Exception {
    cipher.init(Cipher.ENCRYPT_MODE, pubKey, random);
    return cipher.doFinal(bytes);
  }

  public static byte[] decrypt(byte[] bytes) throws Exception {
    cipher.init(Cipher.DECRYPT_MODE, privKey);
    return cipher.doFinal(bytes);
  }

  public static void main(String[] args) throws Exception {
    System.out.println(new String(decrypt(encrypt("Hello World".getBytes()))));
  }

}
