package util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Leitor de Json Stream
 *
 *
 * @author Tecgraf
 */
public class JsonInputStream {

  /** Mapa vazia */
  private static final Map<String, Object> EMPTY_MAP = Collections
    .unmodifiableMap(new TreeMap<String, Object>());
  /** Lista vazia */
  private static final List<Object> EMPTY_LIST = Collections
    .unmodifiableList(new ArrayList<Object>(0));
  /** Stream */
  private final String input;
  /** Proximo */
  private int offset;
  /** Proximo */
  private int next;

  /** Formatador de data */

  /**
   * Construtor
   * 
   * @param input
   * @throws SyntaxException
   * @throws IOException
   */
  public JsonInputStream(String input) throws SyntaxException {
    this.input = input;
    this.next = this.readInternalNoSpace(false);
  }

  /**
   * Ler o objeto
   * 
   * @return obj
   * @throws SyntaxException
   */
  public Object readObject() throws SyntaxException {
    int c = this.lookahead();
    if (c == '[') {
      return readList();
    }
    else if (c == '{') {
      return readMap();
    }
    else if ((c >= '0' && c <= '9') || c == '-') {
      return readNumber();
    }
    else if (c == '\"' || c == '\'') {
      return readStringOrDate();
    }
    else if (c == 't') {
      return readTrue();
    }
    else if (c == 'f') {
      return readFalse();
    }
    else if (c == 'n') {
      return readNull();
    }
    else {
      throw new SyntaxException();
    }
  }

  /**
   * @return null
   * @throws IOException
   * @throws SyntaxException
   */
  public Object readNull() throws SyntaxException {
    if (this.readWithSpace() != 'n') {
      throw new SyntaxException();
    }
    if (this.readWithSpace() != 'u') {
      throw new SyntaxException();
    }
    if (this.readWithSpace() != 'l') {
      throw new SyntaxException();
    }
    if (this.readWithSpace() != 'l') {
      throw new SyntaxException();
    }
    return null;
  }

  /**
   * @return false
   * @throws IOException
   * @throws SyntaxException
   */
  public Object readFalse() throws SyntaxException {
    if (this.readWithSpace() != 'f') {
      throw new SyntaxException();
    }
    if (this.readWithSpace() != 'a') {
      throw new SyntaxException();
    }
    if (this.readWithSpace() != 'l') {
      throw new SyntaxException();
    }
    if (this.readWithSpace() != 's') {
      throw new SyntaxException();
    }
    if (this.readWithSpace() != 'e') {
      throw new SyntaxException();
    }
    return false;
  }

  /**
   * @return true
   * @throws SyntaxException
   * @throws IOException
   */
  public Object readTrue() throws SyntaxException {
    if (this.readWithSpace() != 't') {
      throw new RuntimeException();
    }
    if (this.readWithSpace() != 'r') {
      throw new RuntimeException();
    }
    if (this.readWithSpace() != 'u') {
      throw new RuntimeException();
    }
    if (this.readWithSpace() != 'e') {
      throw new RuntimeException();
    }
    return true;
  }

  /**
   * @return a string
   * @throws IOException
   * @throws SyntaxException
   */
  public String readString() throws SyntaxException {
    if (this.readWithSpace() != '\"') {
      throw new RuntimeException();
    }
    StringBuilder sb = new StringBuilder();
    int c = this.readWithSpace();
    while (c != '\"') {
      if (c == '\\') {
        c = this.readWithSpace();
        if (c == '\"' || c == '/' || c == '\\') {
        }
        else if (c == 'b') {
          c = '\b';
        }
        else if (c == 'f') {
          c = '\f';
        }
        else if (c == 'n') {
          c = '\n';
        }
        else if (c == 'r') {
          c = '\r';
        }
        else if (c == 't') {
          c = '\t';
        }
        else if (c == 'u') {
          throw new SyntaxException();
        }
        else {
          throw new SyntaxException();
        }
      }
      sb.append((char) c);
      c = this.readWithSpace();
    }
    String text = sb.toString();
    text = text.replace("\\\\", "\\");
    text = text.replace("\\/", "/");
    text = text.replace("\\\"", "\"");
    text = text.replace("\\r", "\r");
    text = text.replace("\\n", "\n");
    text = text.replace("\\t", "\t");
    text = text.replace("\\b", "\b");
    text = text.replace("\\f", "\f");
    return text;
  }

  /**
   * @return a string or date
   * @throws IOException
   * @throws SyntaxException
   */
  public Object readStringOrDate() throws SyntaxException {
    String text = this.readString();
    if (text.length() == 24) {
      String format = "dddd-dd-ddTdd:dd:dd.dddZ";
      for (int n = 0; n < 24; n++) {
        char c = text.charAt(n);
        char f = format.charAt(n);
        switch (f) {
          case 'd': {
            if (c < '0' || c > '9') {
              return text;
            }
            break;
          }
          default: {
            if (c != f) {
              return text;
            }
          }
        }
      }
      try {
        char c;
        int year = 0;
        c = text.charAt(0);
        if (c != '0') {
          year += (c - '0') * 1000;
        }
        c = text.charAt(1);
        if (c != '0') {
          year += (c - '0') * 100;
        }
        c = text.charAt(2);
        if (c != '0') {
          year += (c - '0') * 10;
        }
        c = text.charAt(3);
        if (c != '0') {
          year += (c - '0');
        }
        int month = 0;
        c = text.charAt(5);
        if (c != '0') {
          month += (c - '0') * 10;
        }
        c = text.charAt(6);
        if (c != '0') {
          month += (c - '0');
        }
        int day = 0;
        c = text.charAt(8);
        if (c != '0') {
          day += (c - '0') * 10;
        }
        c = text.charAt(9);
        if (c != '0') {
          day += (c - '0');
        }
        int hour = 0;
        c = text.charAt(11);
        if (c != '0') {
          hour += (c - '0') * 10;
        }
        c = text.charAt(12);
        if (c != '0') {
          hour += (c - '0');
        }
        int minute = 0;
        c = text.charAt(14);
        if (c != '0') {
          minute += (c - '0') * 10;
        }
        c = text.charAt(15);
        if (c != '0') {
          minute += (c - '0');
        }
        int second = 0;
        c = text.charAt(17);
        if (c != '0') {
          second += (c - '0') * 10;
        }
        c = text.charAt(18);
        if (c != '0') {
          second += (c - '0');
        }
        int milisecond = 0;
        c = text.charAt(20);
        if (c != '0') {
          milisecond += (c - '0') * 100;
        }
        c = text.charAt(21);
        if (c != '0') {
          milisecond += (c - '0') * 10;
        }
        c = text.charAt(22);
        if (c != '0') {
          milisecond += (c - '0');
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute, second);
        calendar.set(Calendar.MILLISECOND, milisecond);
        return calendar.getTime();
      }
      catch (NumberFormatException e) {
        return text;
      }
    }
    else {
      return text;
    }
  }

  /**
   * @return a number
   * @throws IOException
   * @throws SyntaxException
   */
  public Object readNumber() throws SyntaxException {
    StringBuilder sb = new StringBuilder();
    int c = this.lookahead();
    if (c == '-') {
      sb.append((char) this.readWithSpace());
    }
    c = this.lookahead();
    if (c == '0') {
      sb.append((char) this.readWithSpace());
    }
    else {
      while (c >= '0' && c <= '9') {
        sb.append((char) this.readWithSpace());
        c = this.lookahead();
      }
    }
    if (c == '.') {
      do {
        sb.append((char) this.readWithSpace());
        c = this.lookahead();
      } while (c >= '0' && c <= '9');
    }
    if (c == 'e' || c == 'E') {
      throw new SyntaxException();
    }
    try {
      return new Integer(sb.toString());
    }
    catch (NumberFormatException e) {
      return new Long(sb.toString());
    }
  }

  /**
   * @return a map
   * @throws IOException
   * @throws SyntaxException
   */
  public Map<String, Object> readMap() throws SyntaxException {
    if (this.readIgnoringSpace() != '{') {
      throw new SyntaxException();
    }
    if (this.lookahead() == '}') {
      return EMPTY_MAP;
    }
    Map<String, Object> map = new TreeMap<String, Object>();
    for (;;) {
      String key = this.readString();
      if (this.readIgnoringSpace() != ':') {
        throw new SyntaxException();
      }
      Object value = this.readObject();
      map.put(key, value);
      int c = this.readIgnoringSpace();
      if (c == ',') {
        continue;
      }
      else if (c == '}') {
        break;
      }
      else {
        throw new SyntaxException();
      }
    }
    return map;
  }

  /**
   * @return a list
   * @throws SyntaxException
   */
  public Object readList() throws SyntaxException {
    if (this.readIgnoringSpace() != '[') {
      throw new SyntaxException();
    }
    if (this.lookahead() == ']') {
      this.readWithSpace();
      return EMPTY_LIST;
    }
    List<Object> list = new ArrayList<Object>();
    for (;;) {
      Object value = this.readObject();
      list.add(value);
      int c = this.readIgnoringSpace();
      if (c == ']') {
        break;
      }
      else if (c == ',') {
        continue;
      }
      else {
        throw new SyntaxException();
      }
    }
    return list;
  }

  /**
   * Realiza a leitura de um caracter que não seja de espaçamento. Essa leitura
   * considera que não está no final de arquivo.
   * 
   * @return leitura
   * @throws SyntaxException
   * @throws IOException
   */
  protected int readIgnoringSpace() throws SyntaxException {
    int c = this.next;
    if (c >= 0) {
      while (c <= 32) {
        c = this.readInternalNoSpace(true);
      }
      this.next = this.readInternalNoSpace(true);
    }
    return c;
  }

  /**
   * Realiza a leitura de um caracter que não seja de espaçamento. Essa leitura
   * considera que não está no final de arquivo.
   * 
   * @return leitura
   * @throws SyntaxException
   * @throws IOException
   */
  protected int readWithSpace() throws SyntaxException {
    int c = this.next;
    if (c >= 0) {
      this.next = this.readInternalWithSpace();
    }
    else {
      throw new SyntaxException();
    }
    return c;
  }

  /**
   * Realiza o lookahead
   * 
   * @return lookahead
   */
  protected int lookahead() {
    return this.next;
  }

  /**
   * Realiza a leitura de um caracter que não seja de espaçamento. Essa leitura
   * considera que não está no final de arquivo.
   * 
   * @param safe
   * @return leitura
   * @throws SyntaxException
   * @throws IOException
   */
  protected int readInternalNoSpace(boolean safe) throws SyntaxException {
    int c = this.read();
    while (c <= 32) {
      c = this.read();
      if (c < 0) {
        if (safe) {
          return -1;
        }
        else {
          throw new SyntaxException();
        }
      }
    }
    return c;
  }

  /**
   * Realiza a leitura de um caracter que não seja de espaçamento. Essa leitura
   * considera que não está no final de arquivo.
   * 
   * @return leitura
   * @throws IOException
   */
  protected int readInternalWithSpace() {
    int c = this.read();
    if (c < 0) {
      return -1;
    }
    return c;
  }

  /**
   * Realiza a leitura
   * 
   * @return leitura
   * @throws IOException
   */
  protected int read() {
    if (offset >= input.length()) {
      return -1;
    }
    return this.input.charAt(offset++);
  }

  /**
   * Erro de syntax na leitura do json
   * 
   * @author bernardobreder
   * 
   */
  public static class SyntaxException extends Exception {

    /**
     * Construtor
     */
    public SyntaxException() {
      super();
    }

    /**
     * Construtor
     * 
     * @param e
     */
    public SyntaxException(Exception e) {
      super(e);
    }

  }

}
