package study;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

class JcePublicCipher {

  public static void main(String[] a) {
    if (a.length < 5) {
      System.out.println("Usage:");
      System.out.println("java JceSecretCipher algorithm mode keyFile"
        + " input output");
      return;
    }
    String algorithm = a[0];
    String mode = a[1];
    String keyFile = a[2];
    String input = a[3];
    String output = a[4];
    try {
      Key ky = readKey(algorithm, mode, keyFile);
      publicCipher(algorithm, mode, ky, input, output);
    }
    catch (Exception e) {
      System.out.println("Exception: " + e);
      return;
    }
  }

  private static Key readKey(String algorithm, String mode, String input)
    throws Exception {
    KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
    System.out.println();
    System.out.println("KeyFactory Object Info: ");
    System.out.println("Algorithm = " + keyFactory.getAlgorithm());
    System.out.println("Provider = " + keyFactory.getProvider());
    System.out.println("toString = " + keyFactory.toString());

    FileInputStream fis = new FileInputStream(input);
    int kl = fis.available();
    byte[] kb = new byte[kl];
    fis.read(kb);
    fis.close();
    Key ky = null;
    if (mode.equalsIgnoreCase("encrypt")) {
      X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(kb);
      ky = keyFactory.generatePublic(pubKeySpec);
    }
    else if (mode.equalsIgnoreCase("decrypt")) {
      PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(kb);
      ky = keyFactory.generatePrivate(priKeySpec);
    }
    else {
      throw new Exception("Invalid mode: " + mode);
    }
    System.out.println();
    System.out.println("Key Object Info: ");
    System.out.println("Algorithm = " + ky.getAlgorithm());
    System.out.println("Saved File = " + input);
    System.out.println("Length = " + kl);
    System.out.println("Format = " + ky.getFormat());
    System.out.println("toString = " + ky.toString());
    return ky;
  }

  private static void publicCipher(String algorithm, String mode, Key ky,
    String input, String output) throws Exception {
    Cipher cf = Cipher.getInstance(algorithm);
    if (mode.equalsIgnoreCase("encrypt")) {
      cf.init(Cipher.ENCRYPT_MODE, ky);
    }
    else if (mode.equalsIgnoreCase("decrypt")) {
      cf.init(Cipher.DECRYPT_MODE, ky);
    }
    else {
      throw new Exception("Invalid mode: " + mode);
    }
    System.out.println();
    System.out.println("Cipher Object Info: ");
    System.out.println("Block Size = " + cf.getBlockSize());
    System.out.println("Algorithm = " + cf.getAlgorithm());
    System.out.println("Provider = " + cf.getProvider());
    System.out.println("toString = " + cf.toString());

    FileInputStream fis = new FileInputStream(input);
    FileOutputStream fos = new FileOutputStream(output);
    int bufSize = 1024;
    byte[] buf = new byte[bufSize];
    int n = fis.read(buf, 0, bufSize);
    int fisSize = 0;
    int fosSize = 0;
    while (n != -1) {
      fisSize += n;
      byte[] out = cf.update(buf, 0, n);
      fosSize += out.length;
      fos.write(out);
      n = fis.read(buf, 0, bufSize);
    }
    byte[] out = cf.doFinal();
    fosSize += out.length;
    fos.write(out);
    fis.close();
    fos.close();
    System.out.println();
    System.out.println("Cipher Process Info: ");
    System.out.println("Input Size = " + fisSize);
    System.out.println("Output Size = " + fosSize);
  }

}