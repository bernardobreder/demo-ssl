package study.rsa;

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
      int keySize = 4 * 1024;
      random = new SecureRandom(SecureRandom.getSeed(keySize / 2));
      KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
      generator.initialize(keySize, random);
      KeyPair pair = generator.generateKeyPair();
      pubKey = pair.getPublic();
      System.out.println(pubKey.toString());
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
    int size = 100;
    long[] encryptTimes = new long[size];
    long[] decryptTimes = new long[size];
    for (int n = 0; n < size; n++) {
      long time = System.currentTimeMillis();
      byte[] encrypt =
        encrypt("Hello World!!!!Hello World!!!!Hello World!!!!Hello World!!!!Hello World!!!!Hello World!!!!"
          .getBytes());
      encryptTimes[n] = System.currentTimeMillis() - time;
      time = System.currentTimeMillis();
      byte[] decrypt = decrypt(encrypt);
      decryptTimes[n] = System.currentTimeMillis() - time;
      System.out.println(new String(decrypt));
    }
    {
      double medium = 0;
      for (int n = 0; n < size; n++) {
        medium += encryptTimes[n];
      }
      System.out.println(medium / size);
    }
    {
      double medium = 0;
      for (int n = 0; n < size; n++) {
        medium += decryptTimes[n];
      }
      System.out.println(medium / size);
    }
  }

}
