package util;

import java.util.TreeMap;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 * @param <K>
 * @param <V>
 */
public class TreeMapBuilder<K, V> extends TreeMap<K, V> {

  /**
   * @param key
   * @param value
   * @return this
   */
  public TreeMapBuilder<K, V> add(K key, V value) {
    this.put(key, value);
    return this;
  }

}
