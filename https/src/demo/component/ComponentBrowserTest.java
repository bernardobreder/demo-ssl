package demo.component;

import html.HElement;
import html.bootstrap.HBBlockquote;
import html.bootstrap.HBDescription;
import html.bootstrap.HBDescription.HBDescriptionModel;
import html.bootstrap.HBListInline;
import html.bootstrap.HBPageHeader;
import html.bootstrap.HBPanel;
import html.primitive.HHr;
import html.primitive.HStrong;
import html.primitive.HText;
import html.primitive.HTextHeader;
import websocket.WebServerSocket;
import websocket.WebSocket;
import websocket.standard.StandardWebServerSocket;
import browser.Browser;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public class ComponentBrowserTest extends Browser {

  /**
   * @param socket
   */
  public ComponentBrowserTest(WebSocket socket) {
    super(socket);
  }

  @Override
  protected void init() {
    final HContainer root = new HContainer();
    root.addElement(new HBPageHeader("Componentes", null));
    {
      root.addElement(new HTextHeader(2, "HBBlockquote"));
      root.addElement(new HBPanel(new HBBlockquote().addParagraph(
        "Teste de Paragrafo").addSmall("Teste de Small").addSmall(
          "Small novamente")));
    }
    root.addElement(new HHr());
    {
      root.addElement(new HTextHeader(2, "HBDescription"));
      root.addElement(new HBPanel(new HBDescription(new HBDescriptionModel() {

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
      root.addElement(new HBPanel(new HBDescription(new HBDescriptionModel() {

        String[] keys = new String[] { "Nome", "Sobrenome", "Telefone" };

        String[] values = new String[] { "Nome do Usuário",
          "Sobrenome do Usuário", "(21) 9XXXX-XXXX" };

        @Override
        public HElement getDescriptionValueAt(int row) {
          return new HText(keys[row]);
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
    root.addElement(new HHr());
    {
      root.addElement(new HTextHeader(2, "HBListInline"));
      root.addElement(new HBListInline().addElement(new HText("Item 1"))
        .addSpace().addElement(new HText("Item 2")).addSpace().addElement(
          new HText("Item 3")).addSpace().addElement(new HText("Item 4"))
          .addSpace().addElement(new HText("Item 5")));
    }
    root.addElement(new HHr());
    setRoot(root);
  }

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    WebServerSocket server = new StandardWebServerSocket(8080);
    try {
      for (;;) {
        WebSocket socket = server.accept();
        new ComponentBrowserTest(socket).start();
      }
    }
    finally {
      server.close();
    }
  }

}
