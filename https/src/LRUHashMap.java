import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRUHashMap implements Least Recently Used algorithm to store and retrive the
 * values from {Map}.
 */

public class LRUHashMap<K, V> extends LinkedHashMap<K, V> {

  private static final long serialVersionUID = -6805360112277349979L;
  private final static int DEFAULT_INITIALCAPACITY = 10;
  private final static float LOADFACTOR = 0.75f;

  /**
   * Number of entries possible to keep maximum in given time in this {Map}
   */
  private final int cacheSize;

  public LRUHashMap() {
    super(DEFAULT_INITIALCAPACITY, LOADFACTOR, true);
    cacheSize = -1;
  }

  public LRUHashMap(int cacheSize) {
    super(DEFAULT_INITIALCAPACITY, LOADFACTOR, true);
    this.cacheSize = cacheSize;
  }

  /**
   * @param initialCapacity
   */
  public LRUHashMap(int initialCapacity, int cacheSize) {
    super(initialCapacity, LOADFACTOR, true);
    this.cacheSize = cacheSize;
  }

  /**
   * @param m
   */
  public LRUHashMap(Map<K, V> m, int cacheSize) {
    super(DEFAULT_INITIALCAPACITY, LOADFACTOR, true);
    putAll(m);
    this.cacheSize = cacheSize;
  }

  /**
   * @param initialCapacity
   * @param loadFactor
   */
  public LRUHashMap(int initialCapacity, float loadFactor, int cacheSize) {
    super(initialCapacity, loadFactor, true);
    this.cacheSize = cacheSize;
  }

  /**
   * To achieve Thread Safe
   * 
   * @return
   */
  public Map<K, V> getsynchronizedMap() {
    return Collections.synchronizedMap(this);
  }

  @Override
  protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
    return size() > cacheSize;
  }

  public static void main(String[] args) {
    Map<Integer, String> map = new LRUHashMap<Integer, String>(10);
    for (int i = 0; i < 100; i++) {
      map.put(i, "valueof :" + i);
      for (int j = i - 5; j >= 0; j--) {
        map.get(j);
      }
    }
    System.out.println(map);
  }

}