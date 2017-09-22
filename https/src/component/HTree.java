package component;

import html.HElement;
import html.bootstrap.HBColor;
import html.listener.HMouseEvent;
import html.listener.HMouseListener;
import html.primitive.HDiv;
import html.primitive.HTable;
import html.primitive.HTable.HTableCell;
import html.primitive.HTable.HTableRow;
import html.primitive.HText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Componente de Árvore.
 *
 * @author bernardobreder
 * @param <E>
 */
public class HTree<E> extends HDiv {

  /** Root da árvore */
  private HTreeNode<E> root;
  /** Renderizador da Celula */
  private HTreeNodeRenderer<E> cellRenderer;
  /** Renderizador do Caret */
  private HTreeNodeRenderer<E> caretRenderer;
  /** Indica se o root será visivel */
  private boolean rootVisible;
  /** Mapa que guarda o estado de expansão dos nós */
  private Map<E, Boolean> expandedMap = new HashMap<E, Boolean>();
  /** Nó selecionado */
  private HTreeNode<E> selectedNode;
  /** Listeners */
  private List<HTreeListener<E>> listeners;

  /**
   * @param root
   */
  public HTree(HTreeNode<E> root) {
    this.addClass("htree");
    this.setRootVisible(false);
    this.setCellRenderer(new DefaultCellRenderer<E>());
    this.setCaretRenderer(new DefaultCaretRenderer<E>());
    this.setRoot(root);
  }

  /**
   * @return the root
   */
  public HTreeNode<E> getRoot() {
    return root;
  }

  /**
   * @param root the root to set
   */
  public void setRoot(HTreeNode<E> root) {
    this.root = root;
  }

  /**
   * @return node selected
   */
  public HTreeNode<E> getSelectedNode() {
    return selectedNode;
  }

  /**
   * @param selectedNode
   */
  public void setSelectedNode(HTreeNode<E> selectedNode) {
    this.selectedNode = selectedNode;
  }

  /**
   * @return the rootVisible
   */
  public boolean isRootVisible() {
    return rootVisible;
  }

  /**
   * @param rootVisible the rootVisible to set
   */
  public void setRootVisible(boolean rootVisible) {
    this.rootVisible = rootVisible;
  }

  /**
   * @return the renderer
   */
  public HTreeNodeRenderer<E> getCellRenderer() {
    return cellRenderer;
  }

  /**
   * @param renderer the renderer to set
   * @return this
   */
  public HTree<E> setCellRenderer(HTreeNodeRenderer<E> renderer) {
    this.cellRenderer = renderer;
    return this;
  }

  /**
   * @return renderizador
   */
  public HTreeNodeRenderer<E> getCaretRenderer() {
    return caretRenderer;
  }

  /**
   * @param caretRenderer
   */
  public void setCaretRenderer(HTreeNodeRenderer<E> caretRenderer) {
    this.caretRenderer = caretRenderer;
  }

  /**
   * Atualiza a arvore
   * 
   * @return this
   */
  @Override
  public HTree<E> fireChanged() {
    if (this.isChanged()) {
      this.setChanged(false);
      this.removeElements();
      HTreeNode<E> root = this.getRoot();
      if (root != null) {
        this.addElement(fireChanged(root));
      }
      super.fireChanged();
    }
    return this;
  }

  /**
   * @param root
   * @return table
   */
  private HTable fireChanged(HTreeNode<E> root) {
    HTable table = new HTable().addAttribute("width", "100%");
    HTreeNodeRenderer<E> cellRenderer = this.getCellRenderer();
    HTreeNodeRenderer<E> caretRenderer = this.getCaretRenderer();
    HTreeNode<E> selectedNode = this.getSelectedNode();
    List<HTreeNode<E>> list = root.list();
    for (final HTreeNode<E> item : list) {
      final E value = item.getValue();
      boolean leaf = item.isLeaf();
      boolean selected =
        selectedNode == null ? false : value.equals(selectedNode.getValue());
      Boolean expanded = leaf ? null : expandedMap.get(value) == Boolean.TRUE;
      HElement element = cellRenderer.getElement(item, expanded, selected);
      HElement caretElement =
        caretRenderer.getElement(item, expanded, selected);
      HTableRow row = table.addRow().addClass("htree-row");
      HTableCell caretCell = row.addColumn().addAttribute("width", "1px");
      HTableCell contentCell =
        row.addColumn().addClass("pointer").addClass("noselect");
      caretCell.addEventClick(new HMouseListener() {
        @Override
        public void action(HMouseEvent e) {
          onCaretClickEvent(item, value);
        }
      });
      contentCell.addEventClick(new HMouseListener() {
        @Override
        public void action(HMouseEvent e) {
          onContentClickEvent(item);
        }
      });
      caretCell.addElement(caretElement);
      contentCell.addElement(element);
      if (!leaf) {
        caretCell.addClass("htree-caret");
        if (this.expandedMap.get(value) == Boolean.TRUE) {
          HTable child = this.fireChanged(item);
          HTableRow childRow = table.addRow();
          childRow.addColumn();
          HTableCell childColumn = childRow.addColumn();
          childColumn.addElement(child);
        }
      }
    }
    return table;
  }

  /**
   * @param node
   */
  protected void onContentClickEvent(final HTreeNode<E> node) {
    HTreeNode<E> selectedNode = this.getSelectedNode();
    if (selectedNode != null && node.getValue().equals(selectedNode.getValue())) {
      this.action(node).setChanged(true);
    }
    else {
      this.select(node).setChanged(true);
    }
  }

  /**
   * @param item
   */
  protected void onContentRightClickEvent(final HTreeNode<E> item) {
    popup(item);
  }

  /**
   * @param item
   * @param value
   */
  protected void onCaretClickEvent(final HTreeNode<E> item, final E value) {
    if (expandedMap.get(value) == Boolean.TRUE) {
      collapse(item);
    }
    else {
      expand(item);
    }
    setChanged(true);
  }

  /**
   * Colapsa o nó
   * 
   * @param node
   * @return this
   */
  public HTree<E> collapse(HTreeNode<E> node) {
    this.expandedMap.put(node.getValue(), Boolean.FALSE);
    this.fireCollapsed(node);
    return this;
  }

  /**
   * Expande o nó
   * 
   * @param node
   * @return this
   */
  public HTree<E> expand(HTreeNode<E> node) {
    this.expandedMap.put(node.getValue(), Boolean.TRUE);
    this.fireExpanded(node);
    return this;
  }

  /**
   * @param node
   * @return expandido
   */
  public boolean isExpanded(HTreeNode<E> node) {
    return this.expandedMap.get(node.getValue()) == Boolean.TRUE;
  }

  /**
   * @param node
   * @param flag
   * @return this
   */
  public HTree<E> setExpanded(HTreeNode<E> node, boolean flag) {
    this.expandedMap.put(node.getValue(), flag ? Boolean.TRUE : Boolean.FALSE);
    return this;
  }

  /**
   * Expande o nó
   * 
   * @param node
   * @return this
   */
  public HTree<E> select(HTreeNode<E> node) {
    this.selectedNode = node;
    this.fireSelected(node);
    return this;
  }

  /**
   * Expande o nó
   * 
   * @param node
   * @return this
   */
  public HTree<E> action(HTreeNode<E> node) {
    this.fireDoubleClick(node);
    return this;
  }

  /**
   * Expande o nó
   * 
   * @param node
   * @return this
   */
  public HTree<E> popup(HTreeNode<E> node) {
    return this;
  }

  /**
   * @param listener
   */
  public void addListener(HTreeListener<E> listener) {
    if (this.listeners == null) {
      this.listeners = new ArrayList<HTree.HTreeListener<E>>();
    }
    this.listeners.add(listener);
  }

  /**
   * @param listener
   */
  public void removeListener(HTreeListener<E> listener) {
    if (this.listeners == null) {
      this.listeners = new ArrayList<HTree.HTreeListener<E>>();
    }
    this.listeners.remove(listener);
  }

  /**
   *
   */
  public void removeListeners() {
    if (this.listeners == null) {
      this.listeners = new ArrayList<HTree.HTreeListener<E>>();
    }
    this.listeners.clear();
  }

  /**
   * @param node
   */
  protected void fireSelected(HTreeNode<E> node) {
    if (this.listeners != null) {
      for (HTreeListener<E> listener : this.listeners) {
        listener.selected(node);
      }
    }
  }

  /**
   * @param node
   */
  protected void firePopup(HTreeNode<E> node) {
    if (this.listeners != null) {
      for (HTreeListener<E> listener : this.listeners) {
        listener.popup(node);
      }
    }
  }

  /**
   * @param node
   */
  protected void fireDoubleClick(HTreeNode<E> node) {
    if (this.listeners != null) {
      for (HTreeListener<E> listener : this.listeners) {
        listener.doubleClick(node);
      }
    }
  }

  /**
   * @param node
   */
  protected void fireExpanded(HTreeNode<E> node) {
    if (this.listeners != null) {
      for (HTreeListener<E> listener : this.listeners) {
        listener.expanded(node);
      }
    }
  }

  /**
   * @param node
   */
  protected void fireCollapsed(HTreeNode<E> node) {
    if (this.listeners != null) {
      for (HTreeListener<E> listener : this.listeners) {
        listener.collapsed(node);
      }
    }
  }

  /**
   * Renderizador de um nó
   * 
   * @author Tecgraf
   * @param <E>
   */
  public static class DefaultCellRenderer<E> implements HTreeNodeRenderer<E> {

    /**
     * {@inheritDoc}
     */
    @Override
    public HElement getElement(HTreeNode<E> node, Boolean expanded,
      boolean selected) {
      HText textElem = new HText(node.toString());
      if (selected) {
        textElem.setColor(HBColor.INFO);
      }
      return textElem;
    }

  }

  /**
   * Renderizador de um nó
   * 
   * @author Tecgraf
   * @param <E>
   */
  public static class DefaultCaretRenderer<E> implements HTreeNodeRenderer<E> {

    /**
     * {@inheritDoc}
     */
    @Override
    public HElement getElement(HTreeNode<E> node, Boolean expanded,
      boolean selected) {
      HDiv div = new HDiv();
      if (expanded == Boolean.TRUE) {
        div.addClass("img-minus");
      }
      else if (expanded == Boolean.FALSE) {
        div.addClass("img-plus");
      }
      return div;
    }

  }

  /**
   * Implementação padrão do nó
   * 
   * @author Tecgraf
   * @param <E>
   */
  public static abstract class DefaultHTreeNode<E> implements HTreeNode<E> {

  }

  /**
   * Estrutura de nó de uma árvore
   * 
   * @author Tecgraf
   * @param <E>
   */
  public static interface HTreeNode<E> {

    /**
     * Retorna o objeto que representa o nó
     * 
     * @return objeto
     */
    public E getValue();

    /**
     * Lista os filhos do nó
     * 
     * @return filhos
     */
    public List<HTreeNode<E>> list();

    /**
     * Indica se o nó é folha
     * 
     * @return folha
     */
    public boolean isLeaf();

  }

  /**
   * Estrutura que renderiza o nó
   * 
   * @author Tecgraf
   * @param <E>
   */
  public static interface HTreeNodeRenderer<E> {

    /**
     * Retorna o elemento do conteúdo
     * 
     * @param node nó que deseja renderizar
     * @param expanded indica se está expandido ou não. Caso o nó seja folha, o
     *        valor nulo será atribuido.
     * @param selected
     * @return elemento
     */
    public HElement getElement(HTreeNode<E> node, Boolean expanded,
      boolean selected);

  }

  /**
   * 
   * 
   * @author bernardobreder
   * @param <E>
   */
  public static class AdapterHTreeListener<E> implements HTreeListener<E> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void selected(HTreeNode<E> node) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doubleClick(HTreeNode<E> node) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void expanded(HTreeNode<E> node) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void collapsed(HTreeNode<E> node) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void popup(HTreeNode<E> node) {
    }

  }

  /**
   * Estrutura que renderiza o nó
   * 
   * @author Tecgraf
   * @param <E>
   */
  public static interface HTreeListener<E> {

    /**
     * @param node
     */
    public void selected(HTreeNode<E> node);

    /**
     * @param node
     */
    public void popup(HTreeNode<E> node);

    /**
     * @param node
     */
    public void doubleClick(HTreeNode<E> node);

    /**
     * @param node
     */
    public void expanded(HTreeNode<E> node);

    /**
     * @param node
     */
    public void collapsed(HTreeNode<E> node);

  }

}
