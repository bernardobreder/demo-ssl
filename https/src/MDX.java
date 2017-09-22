import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class MDX {

  public static byte[] mdx(InputStream input, long seed) throws IOException {
    int len = 16;
    seed = Long.MAX_VALUE - seed;
    long[] bytes = new long[len];
    for (int n = 0; n < bytes.length; n++) {
      bytes[n] = seed * (n + 1) * seed;
    }
    byte[] buff = new byte[8 * 1024];
    for (int n; (n = input.read(buff)) != -1;) {
      for (int m = 0; m < n; m++) {
        long c = buff[m] & 0xFF;
        if (c == 0) {
          c = seed;
        }
        c *= seed;
        for (int p = 0; p < bytes.length; p++) {
          bytes[p] += c;
        }
      }
    }
    byte[] result = new byte[len];
    for (int n = 0; n < bytes.length; n++) {
      result[n] = (byte) bytes[n];
    }
    return result;
  }

  public static byte[] mdxl(InputStream input, long seed) throws IOException {
    int len = 16;
    seed = Long.MAX_VALUE - seed;
    long[] bytes = new long[len];
    for (int n = 0; n < bytes.length; n++) {
      bytes[n] = seed * (n + 1);
    }
    byte[] buff = new byte[8 * 1024];
    for (int n; (n = input.read(buff)) != -1;) {
      for (int m = 0; m < n; m++) {
        int c = buff[m] * 0xFF + Integer.MAX_VALUE / 2;
        for (int p = 0; p < bytes.length; p++) {
          bytes[p] += seed * (c);
        }
      }
    }
    byte[] result = new byte[len];
    for (int n = 0; n < bytes.length; n++) {
      result[n] = (byte) bytes[n];
    }
    return result;
  }

  public static void main(String[] args) throws IOException {
    {
      InputStream input = getMovieStream();
      try {
        System.out.println(Arrays.toString(mdx(input, 342254235244231432l)));
      }
      finally {
        input.close();
      }
    }
    for (int n = 0; n < 100; n++) {
      System.out.println(Arrays.toString(mdx(getBytesStream(n), System
        .currentTimeMillis())));
    }
  }

  private static FileInputStream getMovieStream() throws FileNotFoundException {
    return new FileInputStream("/Users/bernardobreder/scena.xml");
  }

  private static ByteArrayInputStream getBytesStream(int n) {
    return new ByteArrayInputStream(new byte[] { (byte) n });
  }

}