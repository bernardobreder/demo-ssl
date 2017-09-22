package study;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.spec.KeySpec;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

class JceSecretKeyTest {

  public static void main(String[] a) {
    if (a.length < 3) {
      System.out.println("Usage:");
      System.out.println("java JceSecretKeyTest keySize output" + " algorithm");
      return;
    }
    int keySize = Integer.parseInt(a[0]);
    String output = a[1];
    String algorithm = a[2]; // Blowfish, DES, DESede, HmacMD5
    try {
      writeKey(keySize, output, algorithm);
      readKey(output, algorithm);
    }
    catch (Exception e) {
      System.out.println("Exception: " + e);
      return;
    }
  }

  private static void writeKey(int keySize, String output, String algorithm)
    throws Exception {
    KeyGenerator kg = KeyGenerator.getInstance(algorithm);
    kg.init(keySize);
    System.out.println();
    System.out.println("KeyGenerator Object Info: ");
    System.out.println("Algorithm = " + kg.getAlgorithm());
    System.out.println("Provider = " + kg.getProvider());
    System.out.println("Key Size = " + keySize);
    System.out.println("toString = " + kg.toString());

    SecretKey ky = kg.generateKey();
    String fl = output + ".key";
    FileOutputStream fos = new FileOutputStream(fl);
    byte[] kb = ky.getEncoded();
    fos.write(kb);
    fos.close();
    System.out.println();
    System.out.println("SecretKey Object Info: ");
    System.out.println("Algorithm = " + ky.getAlgorithm());
    System.out.println("Saved File = " + fl);
    System.out.println("Size = " + kb.length);
    System.out.println("Format = " + ky.getFormat());
    System.out.println("toString = " + ky.toString());
  }

  private static void readKey(String input, String algorithm) throws Exception {
    String fl = input + ".key";
    FileInputStream fis = new FileInputStream(fl);
    int kl = fis.available();
    byte[] kb = new byte[kl];
    fis.read(kb);
    fis.close();
    KeySpec ks = null;
    SecretKey ky = null;
    SecretKeyFactory kf = null;
    if (algorithm.equalsIgnoreCase("DES")) {
      ks = new DESKeySpec(kb);
      kf = SecretKeyFactory.getInstance("DES");
      ky = kf.generateSecret(ks);
    }
    else if (algorithm.equalsIgnoreCase("DESede")) {
      ks = new DESedeKeySpec(kb);
      kf = SecretKeyFactory.getInstance("DESede");
      ky = kf.generateSecret(ks);
    }
    else {
      ks = new SecretKeySpec(kb, algorithm);
      ky = new SecretKeySpec(kb, algorithm);
    }

    System.out.println();
    System.out.println("KeySpec Object Info: ");
    System.out.println("Saved File = " + fl);
    System.out.println("Length = " + kb.length);
    System.out.println("toString = " + ks.toString());

    System.out.println();
    System.out.println("SecretKey Object Info: ");
    System.out.println("Algorithm = " + ky.getAlgorithm());
    System.out.println("toString = " + ky.toString());
  }

}