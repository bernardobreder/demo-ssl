package util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class StringDiff {

  public static LinkedList<String> toLinkedList(String text) {
    LinkedList<String> list = new LinkedList<String>();
    int index = text.indexOf('\n');
    if (index < 0) {
      list.add(text);
      return list;
    }
    int begin = 0;
    while (index >= 0) {
      String line = text.substring(begin, index);
      list.add(line);
      begin = index + 1;
      index = text.indexOf('\n', begin);
    }
    if (begin != text.length()) {
      String line = text.substring(begin);
      list.add(line);
    }
    return list;
  }

  public static String toString(LinkedList<String> lines) {
    int count = lines.size() - 1;
    for (String line : lines) {
      count += line.length();
    }
    StringBuilder sb = new StringBuilder(count);
    Iterator<String> iterator = lines.iterator();
    while (iterator.hasNext()) {
      sb.append(iterator.next());
      if (iterator.hasNext()) {
        sb.append('\n');
      }
    }
    return sb.toString();
  }

  public static DiffList diff(String left, String right) {
    DiffList list = new DiffList();
    String[] x = left.split("\n");
    String[] y = right.split("\n");
    int M = x.length;
    int N = y.length;
    int[][] opt = new int[M + 1][N + 1];
    for (int i = M - 1; i >= 0; i--) {
      for (int j = N - 1; j >= 0; j--) {
        if (x[i].equals(y[j])) {
          opt[i][j] = opt[i + 1][j + 1] + 1;
        }
        else {
          opt[i][j] = Math.max(opt[i + 1][j], opt[i][j + 1]);
        }
      }
    }
    int i = 0, j = 0;
    while (i < M && j < N) {
      if (x[i].equals(y[j])) {
        i++;
        j++;
      }
      else if (opt[i + 1][j] >= opt[i][j + 1]) {
        list.add(new DiffItem(false, i, x[i]));
        System.out.println("[*" + i + "," + j + "]:" + "< " + x[i]);
        i++;
      }
      else {
        list.add(new DiffItem(true, j, y[j]));
        System.out.println("[" + i + "," + j + "*]:" + "> " + y[j]);
        j++;
      }
    }
    while (i < M || j < N) {
      if (i == M) {
        list.add(new DiffItem(true, j, y[j]));
        System.out.println("[" + i + "," + j + "*]:" + "> " + y[j]);
        j++;
      }
      else if (j == N) {
        list.add(new DiffItem(false, i, x[i]));
        System.out.println("[*" + i + "," + j + "]:" + "< " + x[i]);
        i++;
      }
    }
    return list;
  }

  public static DiffList diff(LinkedList<String> left, LinkedList<String> right) {
    left = new LinkedList<String>(left);
    left.add(null);
    right = new LinkedList<String>(right);
    right.add(null);
    DiffList list = new DiffList();
    int M = left.size() - 1;
    int N = right.size() - 1;
    int[][] opt = new int[M + 1][N + 1];
    {
      ListIterator<String> leftIterator = left.listIterator(M);
      for (int i = M - 1; i >= 0; i--) {
        String x = leftIterator.previous();
        ListIterator<String> rightIterator = right.listIterator(N);
        for (int j = N - 1; j >= 0; j--) {
          String y = rightIterator.previous();
          if (x.equals(y)) {
            opt[i][j] = opt[i + 1][j + 1] + 1;
          }
          else {
            opt[i][j] = Math.max(opt[i + 1][j], opt[i][j + 1]);
          }
        }
      }
    }
    {
      int i = 0, j = 0;
      ListIterator<String> leftIterator = left.listIterator(i);
      ListIterator<String> rightIterator = right.listIterator(j);
      String x = leftIterator.next();
      String y = rightIterator.next();
      while (i < M && j < N) {
        if (x.equals(y)) {
          i++;
          j++;
          x = leftIterator.next();
          y = rightIterator.next();
        }
        else if (opt[i + 1][j] >= opt[i][j + 1]) {
          list.addFirst(new DiffItem(false, i, x));
          System.out.println("[*" + i + "," + j + "]:" + "< " + x);
          i++;
          x = leftIterator.next();
        }
        else {
          list.addLast(new DiffItem(true, j, y));
          System.out.println("[" + i + "," + j + "*]:" + "> " + y);
          j++;
          y = rightIterator.next();
        }
      }
      while (i < M || j < N) {
        if (i == M) {
          list.addLast(new DiffItem(true, j, y));
          System.out.println("[" + i + "," + j + "*]:" + "> " + y);
          j++;
          y = rightIterator.next();
        }
        else if (j == N) {
          list.addFirst(new DiffItem(false, i, x));
          System.out.println("[*" + i + "," + j + "]:" + "< " + x);
          i++;
          x = leftIterator.next();
        }
      }
    }
    return list;
  }

  public static class DiffList extends LinkedList<DiffItem> {

    public LinkedList<String> apply(LinkedList<String> left) {
      for (DiffItem item : this) {
        item.apply(left);
      }
      return left;
    }

  }

  public static class DiffItem {

    private final boolean update;

    private final int line;

    private final String content;

    public DiffItem(boolean update, int line, String content) {
      super();
      this.update = update;
      this.line = line;
      this.content = content;
    }

    public void apply(LinkedList<String> left) {
      if (update) {
        left.add(line, content);
      }
      else {
        left.remove(line);
      }
    }

    @Override
    public String toString() {
      return (update ? "+" : "-") + line + ":" + content;
    }

  }

  public static void main(String[] args) {
    System.out.println(diff("b\na\ne", "b\nb\nd"));
    LinkedList<String> left = toLinkedList("b\na\ne");
    LinkedList<String> right = toLinkedList("b\nb\nd");
    DiffList diff = diff(left, right);
    System.out.println(diff);
    System.out.println(toString(diff.apply(left)));
  }

}
