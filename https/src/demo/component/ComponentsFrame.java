package demo.component;

import html.HElement;
import html.bootstrap.HBBlockquote;
import html.bootstrap.HBButton;
import html.bootstrap.HBColor;
import html.bootstrap.HBContainer;
import html.bootstrap.HBDescription;
import html.bootstrap.HBDescription.HBDescriptionModel;
import html.bootstrap.HBListInline;
import html.bootstrap.HBPageHeader;
import html.bootstrap.HBPanel;
import html.bootstrap.layout.HBCol;
import html.bootstrap.layout.HBRow;
import html.listener.HMouseEvent;
import html.listener.HMouseListener;
import html.primitive.HHr;
import html.primitive.HStrong;
import html.primitive.HText;
import html.primitive.HTextHeader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import component.HTree;
import component.HTree.DefaultHTreeNode;
import component.HTree.HTreeNode;
import component.HTree.HTreeNodeRenderer;

/**
 *
 *
 * @author bernardobreder
 */
public class ComponentsFrame extends HBContainer {

  public ComponentsFrame() {
    this.addElement(new HBPageHeader("Componentes", null));
    {
      this.addElement(new HTextHeader(2, "HTree"));
      class FileTreeNode extends DefaultHTreeNode<File> {

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
      this.addElement(new HTree<File>(new FileTreeNode(null))
        .setCellRenderer(new HTreeNodeRenderer<File>() {
          @Override
          public HElement getElement(HTreeNode<File> node, Boolean expanded,
            boolean selected) {
            File file = node.getValue();
            String name =
              file.getName().length() == 0 ? file.getAbsolutePath() : file
                .getName();
            HText textElem = new HText(name);
            if (selected) {
              textElem.setColor(HBColor.INFO);
            }
            return textElem;
          }
        }));
    }
    {
      this.addElement(new HTextHeader(2, "HBBlockquote"));
      this.addElement(new HBPanel(new HBBlockquote().addParagraph(
        "Teste de Paragrafo").addSmall("Teste de Small").addSmall(
        "Small novamente")));
    }
    this.addElement(new HHr());
    {
      this.addElement(new HTextHeader(2, "HBDescription"));
      this.addElement(new HBPanel(new HBDescription(new HBDescriptionModel() {

        String[] keys = new String[] { "Nome", "Sobrenome", "Telefone" };

        String[] values = new String[] { "Nome do Usuário",
            "Sobrenome do Usuário", "(21) 9XXXX-XXXX" };

        @Override
        public HElement getDescriptionValueAt(int row) {
          return new HText(values[row]);
        }

        @Override
        public HElement getDescriptionKeyAt(int row) {
          return new HStrong(keys[row]);
        }

        @Override
        public int getDescriptionCount() {
          return keys.length;
        }
      })));
      this.addElement(new HBPanel(new HBDescription(new HBDescriptionModel() {

        String[] keys = new String[] { "Nome", "Sobrenome", "Telefone" };

        String[] values = new String[] { "Nome do Usuário",
            "Sobrenome do Usuário", "(21) 9XXXX-XXXX" };

        @Override
        public HElement getDescriptionValueAt(int row) {
          return new HText(values[row]);
        }

        @Override
        public HElement getDescriptionKeyAt(int row) {
          return new HStrong(keys[row]);
        }

        @Override
        public int getDescriptionCount() {
          return keys.length;
        }
      }).setHorizontal(true)));
    }
    this.addElement(new HHr());
    {
      this.addElement(new HTextHeader(2, "HBListInline"));
      this.addElement(new HBListInline().addElement(new HText("Item 1"))
        .addSpace().addElement(new HText("Item 2")).addSpace().addElement(
          new HText("Item 3")).addSpace().addElement(new HText("Item 4"))
        .addSpace().addElement(new HText("Item 5")));
    }
    this.addElement(new HHr());
    {
      this.addElement(new HBRow().addElement(new HBCol("xs", 12).addClass(
        "text-right").addElement(
        new HBButton("Exportar", "Exporte o cenário para Excel")
          .addEventClick(new HMouseListener() {
            @Override
            public void action(HMouseEvent e) {
            }
          }))));
    }
    this.addSpace();
  }

}
