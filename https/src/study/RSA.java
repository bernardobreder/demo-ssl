package study;

import java.math.BigInteger;
import java.util.Random;

public class RSA {

  static int SIZE = 4 * 1024;

  public static void main(String[] args) {
    Random rnd = new Random(0);
    // BigInteger p = BigInteger.probablePrime(SIZE, rnd);
    // BigInteger p = new BigInteger("61");
    BigInteger p =
      new BigInteger(
        "916710808511028924078340883265307740980477466806796086270136727430213118960817309456430135421916677208837093152379272015151749549457324814035400766396149201942259612313175616056728888585405501927176905918926929600316306514041267485152049615068149385253331533748854765920289250161945438847509112231969102651178585436369492461202733847126073611816901350560441404418058645781139893340940758170477054261638607820083003391525736227510429016631362046490465203974955056248159871070570872412166961919057972863687237747059076734218373260362053440598822893830004329646809827803378361556236349842558955903509532955704971907774676458350866552366065482106962102073662899017591971941050126750165095993499788200888805962110768959929362071642111243850770502714442227077174111769895130509125056716337920271882727084801547838703791765325877485343060385610882374792627887799288356857590348591948976436193834518138330859034990145860383578894347540728423552210057137029502341525910816138507517515262388837719267638102704270432571535678836160526605958585470856298802266682605064712345538880526229978283211404842921806375834711222422996339546699497889144898804741756583232707180953267783415441986350333781126544082577572691220686399794324053225812233235263");
    System.out.println("p:" + p);
    // BigInteger q = BigInteger.probablePrime(SIZE, rnd);
    // BigInteger q = new BigInteger("53");
    BigInteger q =
      new BigInteger(
        "955191538891798411949783046494513113219963493224102466095260741689012297566528295914480973839461921031969790870154313744688632583723376075672406294798959207994591661206731253459368333117497323604833699901831615641238873840103011099740057644063728826451584547605386466533973324775669859993635625309300819780124520170445009154330571798852246498404594906683895967564137663525102139477010851006480297880086519801392582362190855813320609290815020207808328710379374438712210503398064542971184991422174910374680968210486776216480302773153725245191521590191989631747582418080073910789696033886032917564701662640122584527921269426565312872675931707120580094968753377266453537729843887879453011699265654360110717954837062953518966047859791843379656116835421734591512047709065434081092091336398713384285906011458503872563976869562246412903276692435168570571902998658428639714516251346586914988426356843659605920335026686053040675638442433897131975941948431840970886145190092708067670029886200042789250751208464770918417704381203979414669918387906872200444923084446584560826529143176192862191937174138649714285593329090436523584163045274759320401701852581679269422876969530393837948812072144256147264852430684667193858525859975595531338236707413");
    System.out.println("q:" + q);
    BigInteger n = p.multiply(q);
    BigInteger totient =
      p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
    System.out.println("totient:" + totient);
    // long e = new Random().nextInt((int) totient) + 1;
    BigInteger e = coprime(totient);
    // BigInteger e = new BigInteger("17");
    // D = modular multiplicative inverse
    BigInteger d = e.modInverse(totient);
    MyPublicKey publicKey = new MyPublicKey();
    publicKey.n = n;
    publicKey.e = e;
    MyPrivateKey privateKey = new MyPrivateKey();
    privateKey.n = n;
    privateKey.d = d;
    System.out.println("e:" + e);
    System.out.println("d:" + d);
    int c = 1000;
    //
    BigInteger i = new BigInteger("" + c);
    i = i.modPow(publicKey.e, publicKey.n);
    //
    i = i.modPow(privateKey.d, publicKey.n);
    System.out.println(c + " == " + i.toString());
  }

  public static class MyPublicKey {
    BigInteger n;
    BigInteger e;
  }

  public static class MyPrivateKey {
    BigInteger n;
    BigInteger d;
  }

  /**
   * Generates a prime number of a chosen length
   * 
   * @param length The size of the prime number
   * @return Returns the generated prime number
   */
  public static BigInteger generatePrime(int length) {
    Random rnd = new Random(System.currentTimeMillis());
    return BigInteger.probablePrime(length, rnd);
  }

  /**
   * Finds a coprime for a given number and within the specified range
   * 
   * @param value The value for which a coprime should be found
   * @param max The top range of the coprime search
   * @return Returns the coprime for the given number
   */
  public static BigInteger coprime(BigInteger P) {
    // for (BigInteger x = max.subtract(BigInteger.ONE); x
    // .compareTo(BigInteger.valueOf(2)) > 0; x = x
    // .subtract(BigInteger.ONE)) {
    // if (greatestCommonDivisor(x, value).compareTo(BigInteger.ONE) == 0) {
    // return x;
    // }
    // }
    // return BigInteger.valueOf(-1);
    BigInteger e;
    Random rnd = new Random();
    do {
      e = new BigInteger(2 * SIZE, rnd);
    } while ((e.compareTo(P) != -1)
      || (e.gcd(P).compareTo(BigInteger.ONE) != 0));
    return e;
  }

  /**
   * Returns the greatest common divisor between two numbers
   * 
   * @param x The first number
   * @param y The second number
   * @return The greatest common divisor between the two given numbers
   */
  public static BigInteger greatestCommonDivisor(BigInteger x, BigInteger y) {
    BigInteger max = BigInteger.ONE;
    BigInteger top = (x.compareTo(y) >= 0) ? x : y;
    BigInteger temp = BigInteger.ONE;

    while (temp.compareTo(top) <= 0) {
      if (x.mod(temp).equals(BigInteger.ZERO)
        && y.mod(temp).equals(BigInteger.ZERO)) {
        max = temp;
      }
      temp = temp.add(BigInteger.ONE);
    }

    return max;
  }

  /**
   * Returns the greatest common divisor between two numbers
   * 
   * @param x The first number
   * @param y The second number
   * @return The greatest common divisor between the two given numbers
   */
  public static int greatestCommonDivisor(int x, int y) {
    int max = 1;
    int top = (x >= y) ? x : y;
    int temp = 1;

    while (temp <= top) {
      if (x % temp == 0 && y % temp == 0) {
        max = temp;
      }
      temp++;
    }

    return max;
  }

  public static int coprime(int value, int max) {
    for (int x = 1; x < max; x++) {
      if (greatestCommonDivisor(x, value) == 1) {
        return x;
      }
    }
    return -1;
  }

}
