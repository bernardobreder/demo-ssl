package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import sun.misc.BASE64Encoder;

/**
 *
 *
 * @author bernardobreder
 */
public class Bytes {

  /**
   * @param text
   * @return bytes
   */
  public static byte[] getUtf8Bytes(String text) {
    int len = 0;
    int textlen = text.length();
    for (int n = 0; n < textlen; n++) {
      char c = text.charAt(n);
      if (c <= 0x7F) {
        len++;
      }
      else if (c <= 0x7FF) {
        len += 2;
      }
      else {
        len += 3;
      }
    }
    byte[] bytes = new byte[len];
    for (int n = 0, i = 0; n < textlen; n++, i++) {
      char c = text.charAt(n);
      if (c <= 0x7F) {
        bytes[i] = (byte) c;
      }
      else if (c <= 0x7FF) {
        bytes[i++] = (byte) (((c >> 6) & 0x1F) + 0xC0);
        bytes[i] = (byte) ((c & 0x3F) + 0x80);
      }
      else {
        bytes[i++] = (byte) (((c >> 12) & 0xF) + 0xE0);
        bytes[i++] = (byte) (((c >> 6) & 0x3F) + 0x80);
        bytes[i] = (byte) ((c & 0x3F) + 0x80);
      }
    }
    return bytes;
  }

  /**
   * @param bytes
   * @return hex
   */
  public static String toHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (int n = 0; n < bytes.length; n++) {
      int c = bytes[n] & 0xFF;
      String hex = Integer.toHexString(c);
      while (hex.length() < 2) {
        hex = "0" + hex;
      }
      sb.append(hex);
    }
    return sb.toString();
  }

  /**
   * @param in
   * @return base64
   * @throws IOException
   */
  public static String toBase64(InputStream in) throws IOException {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      byte[] bytes = new byte[1024];
      for (int n; (n = in.read(bytes)) != -1;) {
        out.write(bytes, 0, n);
      }
      return new BASE64Encoder().encode(out.toByteArray());
    }
    finally {
      in.close();
    }
  }

  /**
   * @param bytes
   * @return base64
   */
  public static String toBase64(byte[] bytes) {
    return new BASE64Encoder().encode(bytes);
  }

  /**
   * @param text
   * @return md5
   */
  public static byte[] md5(String text) {
    return md5(getUtf8Bytes(text));
  }

  /**
   * @param bytes
   * @return session
   */
  public static String session(int bytes) {
    StringBuilder sb = new StringBuilder();
    Random random = new Random(System.currentTimeMillis());
    while (sb.length() < bytes) {
      sb.append(Bytes.toBase64(Bytes.md5(Long.toString(random.nextLong()))));
      int index = sb.indexOf("=");
      while (index >= 0) {
        sb.deleteCharAt(index);
        index = sb.indexOf("=", index);
      }
      index = sb.indexOf("+");
      while (index >= 0) {
        sb.deleteCharAt(index);
        index = sb.indexOf("+", index);
      }
      index = sb.indexOf("/");
      while (index >= 0) {
        sb.deleteCharAt(index);
        index = sb.indexOf("/", index);
      }
    }
    sb.delete(bytes, sb.length());
    return sb.toString();
  }

  /**
   * @param bytes
   * @return md5
   */
  public static byte[] md5(byte[] bytes) {
    try {
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(bytes);
      byte[] digest = md5.digest();
      return digest;
    }
    catch (NoSuchAlgorithmException e) {
      return bytes;
    }
  }

  /**
   * @param text
   * @return bytes
   */
  public static byte[] sha1(String text) {
    byte[] bytes = getUtf8Bytes(text);
    try {
      MessageDigest sha1 = MessageDigest.getInstance("SHA1");
      sha1.update(bytes);
      byte[] digest = sha1.digest();
      return digest;
    }
    catch (NoSuchAlgorithmException e) {
      return bytes;
    }
  }

  /**
   * @param in
   * @return string
   * @throws IOException
   */
  public static String toString(InputStream in) throws IOException {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      byte[] bytes = new byte[1024];
      for (int n; (n = in.read(bytes)) != -1;) {
        out.write(bytes, 0, n);
      }
      byte[] array = out.toByteArray();
      StringBuilder sb = new StringBuilder(array.length);
      for (int n = 0; n < array.length; n++) {
        int c = array[n] & 0xFF;
        if (c <= 0x7F) {
        }
        else if ((c >> 5) == 0x6) {
          int i2 = array[++n] & 0xFF;
          c = (((c & 0x1F) << 6) + (i2 & 0x3F));
        }
        else {
          int i2 = array[++n] & 0xFF;
          int i3 = array[++n] & 0xFF;
          c = (((c & 0xF) << 12) + ((i2 & 0x3F) << 6) + (i3 & 0x3F));
        }
        sb.append((char) c);
      }
      return sb.toString();
    }
    finally {
      in.close();
    }
  }

  /**
   * Retorna a stream de um arquivo está está localizado no mesmo diretório da
   * classe
   * 
   * @param pathClass
   * @param name
   * @return stream de leitura
   */
  public static InputStream getResource(Class<?> pathClass, String name) {
    String path =
      "/" + pathClass.getPackage().getName().replace('.', '/') + "/" + name;
    return pathClass.getResourceAsStream(path);
  }

  public static void main(String[] args) throws FileNotFoundException,
    IOException {
    System.out.println(toBase64(new FileInputStream("img/minus.png")));
  }

}
