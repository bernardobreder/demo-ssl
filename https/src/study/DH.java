package study;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class DH {

  public static void main(String[] argv) throws Exception {
    KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("DH");
    keyGenerator.initialize(1024);
    KeyPair kpair = keyGenerator.genKeyPair();
    PrivateKey priKey = kpair.getPrivate();
    PublicKey pubKey = kpair.getPublic();
    String frm = priKey.getFormat();
    System.out.println("Private key format :" + frm);
    System.out.println("Diffie-Helman Private key parameters are:" + priKey);
    frm = pubKey.getFormat();
    System.out.println("Public key format :" + frm);
    System.out.println("Diffie-Helman Public key parameters are:" + pubKey);
  }

}