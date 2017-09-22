package crypto;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import util.Bytes;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class AESCrypto {

  public static void main(String[] args) throws Exception {
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    kgen.init(128);
    SecretKey skey = kgen.generateKey();
    byte[] key = skey.getEncoded();
    byte[] input = Bytes.getUtf8Bytes("Bernardo Breder");
    SecretKeySpec keySpec = null;
    keySpec = new SecretKeySpec(key, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, keySpec);
    byte[] output = cipher.doFinal(input);
    System.out.println(Base64.encode(output));
  }

}
