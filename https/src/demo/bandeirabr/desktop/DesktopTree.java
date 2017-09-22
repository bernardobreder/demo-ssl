package demo.bandeirabr.desktop;

import html.HElement;
import html.bootstrap.HBColor;
import html.primitive.HText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import component.HTree;

/**
 *
 *
 * @author bernardobreder
 */
public class DesktopTree extends HTree<File> {

  /**
   * Construtor
   */
  public DesktopTree() {
    super(new FileTreeNode(null));
    this.setCellRenderer(new HTreeNodeRenderer<File>() {
      @Override
      public HElement getElement(HTreeNode<File> node, Boolean expanded,
        boolean selected) {
        File file = node.getValue();
        String name =
          file.getName().length() == 0 ? file.getAbsolutePath() : file
            .getName();
        HText textElem = new HText(name);
        if (selected) {
          textElem.setColor(HBColor.DANGER);
        }
        return textElem;
      }
    });
  }

  public static class FileTreeNode extends DefaultHTreeNode<File> {

    private final File file;

    public FileTreeNode(File file) {
      this.file = file;
    }

    @Override
    public File getValue() {
      return this.file;
    }

    @Override
    public List<HTreeNode<File>> list() {
      if (file != null && (file.isFile() || !file.isDirectory())) {
        return new ArrayList<HTree.HTreeNode<File>>();
      }
      List<HTreeNode<File>> list = new ArrayList<HTree.HTreeNode<File>>();
      File[] files = null;
      if (this.file == null) {
        files = File.listRoots();
        if (files != null) {
          for (File f : files) {
            list.add(new FileTreeNode(f));
          }
        }
      }
      else {
        files = file.listFiles();
        if (files != null) {
          for (File f : files) {
            if (!f.isHidden()) {
              list.add(new FileTreeNode(f));
            }
          }
        }
      }
      return list;
    }

    @Override
    public boolean isLeaf() {
      return file.isFile();
    }

    @Override
    public String toString() {
      return this.file == null ? "" : this.file.getName();
    }
  }

}
