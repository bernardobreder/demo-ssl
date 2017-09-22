package demo.bandeirabr.desktop;

import html.IBrowser;
import html.bootstrap.HBContainer;
import html.bootstrap.layout.HBCol;
import html.bootstrap.layout.HBRow;
import html.bootstrap.menu.HBMenu;
import html.bootstrap.menu.HBMenuBar;
import html.primitive.HDiv;

import java.io.File;
import java.io.IOException;

import util.TreeMapBuilder;

import component.HTree.AdapterHTreeListener;
import component.HTree.HTreeNode;

public class DesktopFrame extends HDiv {

  private DesktopTree desktopTree;

  public DesktopFrame() {
    this.addElement(new HBMenuBar().addItem(new HBMenu("Cenário")).setTitle(
      "Supply"));
    HBContainer container = new HBContainer();
    container.addElement(new HBRow().addElement(createLeftPanel()).addElement(
      createRightPanel()));
    this.addElement(container);
  }

  protected HBCol createLeftPanel() {
    HBCol panel = new HBCol("lg", 3);
    desktopTree = new DesktopTree();
    desktopTree.addListener(new AdapterHTreeListener<File>() {
      @Override
      public void doubleClick(HTreeNode<File> node) {
        onDesktopTreeAction(node);
      }
    });
    panel.addElement(desktopTree);
    return panel;
  }

  protected HBCol createRightPanel() {
    HBCol panel =
      new HBCol("lg", 9).addClass("visible-lg").addClass("hidden-print");
    panel.addElement(new DesktopTree());
    return panel;
  }

  protected void onDesktopTreeAction(HTreeNode<File> node) {
    File file = node.getValue();
    if (file.isFile()) {
      try {
        TreeMapBuilder<String, Object> tree =
          new TreeMapBuilder<String, Object>().add("method", "page").add("url",
            "/scenarium").add("id", 1);
        IBrowser browser = this.getBrowser();
        browser.addChange(browser.getJavascript().pushState("/scenarium?id=1"));
        browser.message(tree);
      }
      catch (IOException e) {
      }
    }
    else {
      desktopTree.setExpanded(node, !desktopTree.isExpanded(node));
    }
  }

}
