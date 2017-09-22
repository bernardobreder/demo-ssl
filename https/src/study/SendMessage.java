package study;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;

public class SendMessage {

  public static void main(String[] args) throws Exception {
    SecureRandom random = new SecureRandom();
    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
    generator.initialize(4096 * 2, random);

    KeyPair pair = generator.generateKeyPair();
    PublicKey pubKey = pair.getPublic();
    PrivateKey privKey = pair.getPrivate();

    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, pubKey, random);
    byte[] input = "Hello".getBytes();
    byte[] cipherText = cipher.doFinal(input);

    cipher.init(Cipher.DECRYPT_MODE, privKey);
    byte[] plainText = cipher.doFinal(cipherText);
    System.out.println(new String(plainText));
  }

}