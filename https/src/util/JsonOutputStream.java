package util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Escreve objeto no formato json
 *
 *
 * @author Tecgraf
 */
public class JsonOutputStream {

  /** Delega a saída */
  private final StringBuilder output;

  /**
   * Construtor
   */
  public JsonOutputStream() {
    this.output = new StringBuilder();
  }

  /**
   * Escreve um objeto
   * 
   * @param object
   * @return this
   */
  public JsonOutputStream writeObject(Object object) {
    if (object == null) {
      this.writeNull();
    }
    else if (object instanceof String) {
      this.writeString((String) object);
    }
    else if (object instanceof Id) {
      this.writeIdentify((Id) object);
    }
    else if (object instanceof Boolean) {
      this.writeBoolean((Boolean) object);
    }
    else if (object instanceof Integer) {
      this.writeInteger((Integer) object);
    }
    else if (object instanceof Long) {
      this.writeLong((Long) object);
    }
    else if (object instanceof Float) {
      this.writeFloat((Float) object);
    }
    else if (object instanceof Double) {
      this.writeDouble((Double) object);
    }
    else if (object instanceof Number) {
      this.writeNumber((Number) object);
    }
    else if (object instanceof Date) {
      this.writeDate((Date) object);
    }
    else if (object instanceof Throwable) {
      this.writeThrowable((Throwable) object);
    }
    else if (object instanceof ArrayList<?>) {
      this.writeArrayList((ArrayList<?>) object);
    }
    else if (object instanceof Map<?, ?>) {
      this.writeMap((Map<?, ?>) object);
    }
    else if (object instanceof Collection<?>) {
      this.writeCollection((Collection<?>) object);
    }
    else {
      this.writeStruct(object);
    }
    return this;
  }

  /**
   * Escreve uma estrutura de dados qualquer
   * 
   * @param object
   * @return this
   */
  public JsonOutputStream writeStruct(Object object) {
    output.append('{');
    Class<?> c = object.getClass();
    while (c != Object.class) {
      Field[] fields = c.getDeclaredFields();
      for (int n = 0; n < fields.length; n++) {
        try {
          Field field = fields[n];
          int modifiers = field.getModifiers();
          if (!Modifier.isTransient(modifiers) && !Modifier.isStatic(modifiers)) {
            field.setAccessible(true);
            Object value = field.get(object);
            String name = field.getName();
            output.append(",\"");
            int length = name.length();
            for (int m = 0; m < length; m++) {
              output.append(name.charAt(m));
            }
            output.append("\":");
            this.writeObject(value);
          }
        }
        catch (Exception e) {
        }
      }
      c = c.getSuperclass();
    }
    output.append('}');
    return this;
  }

  /**
   * Imprime o null
   * 
   * @return this
   */
  public JsonOutputStream writeNull() {
    output.append("null");
    return this;
  }

  /**
   * Escreve uma String
   * 
   * @param text
   * @return this
   */
  public JsonOutputStream writeString(String text) {
    output.append('\"');
    int length = text.length();
    for (int n = 0; n < length; n++) {
      char c = text.charAt(n);
      if (c == '\\') {
        output.append("\\\\");
      }
      else if (c == '/') {
        output.append("\\/");
      }
      else if (c == '\"') {
        output.append("\\\"");
      }
      else if (c == '\r') {
        output.append("\\r");
      }
      else if (c == '\n') {
        output.append("\\n");
      }
      else if (c == '\t') {
        output.append("\\t");
      }
      else if (c == '\b') {
        output.append("\\b");
      }
      else if (c == '\f') {
        output.append("\\f");
      }
      else if (c <= 0x7F) {
        output.append(c);
      }
    }
    output.append('\"');
    return this;
  }

  /**
   * Escreve uma String
   * 
   * @param id
   * @return this
   */
  public JsonOutputStream writeIdentify(Id id) {
    String text = id.id;
    int length = text.length();
    for (int n = 0; n < length; n++) {
      char c = text.charAt(n);
      output.append(c);
    }
    return this;
  }

  /**
   * Escreve True
   * 
   * @return this
   */
  public JsonOutputStream writeTrue() {
    output.append("true");
    return this;
  }

  /**
   * Escreve false
   * 
   * @return this
   */
  public JsonOutputStream writeFalse() {
    output.append("false");
    return this;
  }

  /**
   * Escreve um Boolean
   * 
   * @param flag
   * @return this
   */
  public JsonOutputStream writeBoolean(Boolean flag) {
    if (flag) {
      this.writeTrue();
    }
    else {
      this.writeFalse();
    }
    return this;
  }

  /**
   * Escreve um Boolean
   * 
   * @param flag
   * @return this
   */
  public JsonOutputStream writeBoolean(boolean flag) {
    if (flag) {
      this.writeTrue();
    }
    else {
      this.writeFalse();
    }
    return this;
  }

  /**
   * Escreve um Integer
   * 
   * @param number
   * @return this
   */
  public JsonOutputStream writeInteger(Integer number) {
    output.append(number.toString());
    return this;
  }

  /**
   * Escreve um int
   * 
   * @param number
   * @return this
   */
  public JsonOutputStream writeInteger(int number) {
    output.append(Integer.toString(number));
    return this;
  }

  /**
   * Escreve um Long
   * 
   * @param number
   * @return this
   */
  public JsonOutputStream writeLong(Long number) {
    output.append(number.toString());
    return this;
  }

  /**
   * Escreve um Long
   * 
   * @param number
   * @return this
   */
  public JsonOutputStream writeLong(long number) {
    output.append(Long.toString(number));
    return this;
  }

  /**
   * Escreve um Float
   * 
   * @param number
   * @return this
   */
  public JsonOutputStream writeFloat(Float number) {
    output.append(number.toString());
    return this;
  }

  /**
   * Escreve um float
   * 
   * @param number
   * @return this
   */
  public JsonOutputStream writeFloat(float number) {
    output.append(Float.toString(number));
    return this;
  }

  /**
   * Escreve um Double
   * 
   * @param number
   * @return this
   */
  public JsonOutputStream writeDouble(Double number) {
    output.append(number.toString());
    return this;
  }

  /**
   * Escreve um Double
   * 
   * @param number
   * @return this
   */
  public JsonOutputStream writeDouble(double number) {
    output.append(Double.toString(number));
    return this;
  }

  /**
   * Escreve um número
   * 
   * @param number
   * @return this
   */
  public JsonOutputStream writeNumber(Number number) {
    output.append(number.toString());
    return this;
  }

  /**
   * Escreve uma data
   * 
   * @param object
   * @return this
   */
  public JsonOutputStream writeDate(Date object) {
    Calendar c = Calendar.getInstance();
    c.setTime(object);
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH) + 1;
    int day = c.get(Calendar.DAY_OF_MONTH);
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);
    int second = c.get(Calendar.SECOND);
    int milisecond = c.get(Calendar.MILLISECOND);
    StringBuilder sb = new StringBuilder(24);
    if (year < 1000) {
      sb.append('0');
    }
    if (year < 100) {
      sb.append('0');
    }
    if (year < 10) {
      sb.append('0');
    }
    sb.append(year);
    sb.append('-');
    if (month < 10) {
      sb.append('0');
    }
    sb.append(month);
    sb.append('-');
    if (day < 10) {
      sb.append('0');
    }
    sb.append(day);
    sb.append('T');
    if (hour < 10) {
      sb.append('0');
    }
    sb.append(hour);
    sb.append(':');
    if (minute < 10) {
      sb.append('0');
    }
    sb.append(minute);
    sb.append(':');
    if (second < 10) {
      sb.append('0');
    }
    sb.append(second);
    sb.append('.');
    if (milisecond < 100) {
      sb.append('0');
    }
    if (milisecond < 10) {
      sb.append('0');
    }
    sb.append(milisecond);
    sb.append('Z');
    this.writeString(sb.toString());
    return this;
  }

  /**
   * Escreve um erro
   * 
   * @param object
   * @return this
   */
  public JsonOutputStream writeThrowable(Throwable object) {
    output.append(("{\"type\":\"exception\",\"message\":\""
      + object.getMessage() + "\"}"));
    return this;
  }

  /**
   * Escreve uma lista
   * 
   * @param list
   * @return this
   */
  public JsonOutputStream writeArrayList(ArrayList<?> list) {
    output.append('[');
    int size = list.size();
    for (int n = 0; n < size; n++) {
      this.writeObject(list.get(n));
      if (n != size - 1) {
        output.append(',');
      }
    }
    output.append(']');
    return this;
  }

  /**
   * Escreve uma lista
   * 
   * @param map
   * @return this
   */
  public JsonOutputStream writeMap(Map<?, ?> map) {
    output.append('{');
    Iterator<?> iterator = map.entrySet().iterator();
    while (iterator.hasNext()) {
      Entry<?, ?> next = (Entry<?, ?>) iterator.next();
      Object key = next.getKey();
      Object value = next.getValue();
      output.append(key);
      output.append(':');
      this.writeObject(value);
      if (iterator.hasNext()) {
        output.append(',');
      }
    }
    output.append('}');
    return this;
  }

  /**
   * Escreve uma coleção
   * 
   * @param list
   * @return this
   */
  public JsonOutputStream writeCollection(Collection<?> list) {
    output.append('[');
    Iterator<?> iterator = list.iterator();
    while (iterator.hasNext()) {
      this.writeObject(iterator.next());
      if (iterator.hasNext()) {
        output.append(',');
      }
    }
    output.append(']');
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return this.output.toString();
  }

  /**
   * 
   * 
   * @author Tecgraf/PUC-Rio
   */
  public static class Id {

    /** Identify */
    public String id;

    /**
     * @param id
     */
    public Id(String id) {
      super();
      this.id = id;
    }

  }

}
