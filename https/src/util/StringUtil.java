package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 *
 *
 * @author bernardobreder
 */
public class StringUtil {

  public static boolean hasHeader(String text) {
    int end = text.indexOf("\r\n\r\n");
    if (end < 0) {
      return false;
    }
    String[] split = text.substring(0, end).trim().split("\n");
    for (String line : split) {
      if (line.indexOf(':') < 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * @param text
   * @return map
   */
  public static Map<String, String> splitHeader(String text) {
    text = text.trim();
    Map<String, String> map = new TreeMap<String, String>();
    if (text.length() == 0) {
      return map;
    }
    List<String> lines = new ArrayList<String>(10);
    int begin = 0, index = text.indexOf('\n');
    while (index >= 0) {
      lines.add(text.substring(begin, index).trim());
      begin = index + 1;
      index = text.indexOf('\n', begin);
    }
    lines.add(text.substring(begin));
    for (String line : lines) {
      index = line.indexOf(':');
      if (index >= 0) {
        String key = line.substring(0, index).trim();
        String value = line.substring(index + 1).trim();
        map.put(key, value);
      }
    }
    return map;
  }

  /**
   * @param text
   * @return map
   */
  public static String[] splitUrlAndParam(String text) {
    int index = text.indexOf('?');
    if (index < 0) {
      return new String[] { text, "" };
    }
    String url = text.substring(0, index);
    String param = text.substring(index + 1);
    return new String[] { url, param };
  }

  /**
   * @param text
   * @return map
   */
  public static Map<String, String> splitUrlParams(String text) {
    if (text.trim().length() == 0) {
      return new TreeMap<String, String>();
    }
    Map<String, String> map = new TreeMap<String, String>();
    int beginLine = 0, endLine = text.indexOf('&');
    while (endLine >= 0) {
      String line = text.substring(beginLine, endLine);
      text = text.substring(endLine + 1);
      int index = line.indexOf('=');
      if (index >= 0) {
        String key = line.substring(0, index);
        String value = line.substring(index + 1).trim();
        map.put(key, value);
      }
      beginLine = endLine + 1;
      endLine = text.indexOf('&', beginLine);
      if (endLine < 0) {
        break;
      }
      line = text.substring(beginLine, endLine);
    }
    if (text.length() > 0) {
      int index = text.indexOf('=');
      if (index >= 0) {
        String key = text.substring(0, index);
        String value = text.substring(index + 1).trim();
        map.put(key, value);
      }
    }
    return map;
  }

  public static String toString(InputStream in, Map<String, Object> map)
    throws IOException {
    String result = Bytes.toString(in);
    if (map != null) {
      for (Entry<String, Object> entry : map.entrySet()) {
        String key = "${" + entry.getKey() + "}";
        int index = result.indexOf(key);
        while (index >= 0) {
          Object value = entry.getValue();
          if (value instanceof String) {
            value = "\"" + value + "\"";
          }
          result =
            result.substring(0, index) + value
              + result.substring(index + key.length());
          index = result.indexOf(key);
        }
      }
    }
    return result;
  }

}
