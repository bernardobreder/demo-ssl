package demo;

import html.HElement;
import html.bootstrap.HBButton;
import html.bootstrap.HBCol;
import html.bootstrap.HBColor;
import html.bootstrap.HBMobileSpace;
import html.bootstrap.HBPageHeader;
import html.bootstrap.HBPanel;
import html.bootstrap.HBRow;
import html.bootstrap.HBScroll;
import html.bootstrap.HBSelect;
import html.bootstrap.HBTable;
import html.bootstrap.HBTable.HBAbstractTableModel;
import html.bootstrap.HBTable.HBTableCell;
import html.bootstrap.HBTable.HBTableColor;
import html.bootstrap.HBTable.HBTableRow;
import html.bootstrap.HBTextArea;
import html.listener.HMouseEvent;
import html.listener.HMouseListener;
import html.primitive.HDiv;
import html.primitive.HHr;
import html.primitive.HSmall;
import html.primitive.HStrong;
import html.primitive.HText;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import websocket.WebServerSocket;
import websocket.WebSocket;
import websocket.standard.StandardWebServerSocket;
import browser.Browser;

/**
 *
 *
 * @author Tecgraf/PUC-Rio
 */
public class BandeiraBrowserTest extends Browser {

  private HElement printButton;

  /**
   * @param socket
   * @throws IOException
   */
  public BandeiraBrowserTest(WebSocket socket) throws IOException {
    super(socket);
  }

  @Override
  protected void init() {
    final HContainer root = new HContainer();
    root.addElement(new HBPageHeader("Cenário 1", "Janeiro 2008 a Março 2008")
    .addEventClick(new HMouseListener() {
      @Override
      public void action(HMouseEvent e) {
        root.addElement(new HHr());
        e.getElement().removeEventClick();
      }
    }));
    {
      HElement viewCol =
        new HBCol("md", 2).addElement(
          new HBSelect("2 Grupamentos", "1 Ponto e Produto", "Nafta Refino")
          .addAttribute("title", "Selecione a Visão")).addElement(
            new HBMobileSpace());
      HElement stateCol =
        new HBCol("md", 3).addElement(
          new HBSelect("Operações x Produtos", "Operações x Locais",
            "Locais x Operações", "Locais x Produtos", "Produtos x Operações",
            "Produtos x Locais").addAttribute("title",
              "Selecione o Eixo da tabela")).addElement(new HBMobileSpace());
      HElement fixedCol =
        new HBCol("md", 2).addElement(
          new HBSelect("BRASIL", "Aracaju", "Manaus", "Reduc", "FERT",
            "FERT BA", "Aracaju").addAttribute("title",
              "Selecione o Ponto ou Região")).addElement(new HBMobileSpace());
      HElement trackingModeCol =
        new HBCol("md", 2).addElement(
          new HBSelect("Previsto", "Realizado", "A Realizar", "P. Linearizado",
            "Desvio").addAttribute("title",
              "Selecione o Modo de Acompanhamento")).addElement(
                new HBMobileSpace());
      HElement dayCol =
        new HBCol("md", 1).addElement(
          new HBSelect("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11",
            "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22",
            "23", "24", "25", "26", "27", "28", "29", "30", "31").addAttribute(
              "title", "Selecione o Dia")).addElement(new HBMobileSpace());
      HElement monthYearCol =
        new HBCol("md", 2).addElement(
          new HBSelect("Janeiro 2008", "Fevereiro 2008", "Março 2008")
          .addAttribute("title", "Selecione o Mês e o Ano")).addElement(
            new HBMobileSpace());
      root.addElement(new HBRow().addElement(viewCol).addElement(stateCol)
        .addElement(fixedCol).addElement(trackingModeCol).addElement(dayCol)
        .addElement(monthYearCol));
    }
    root.addElement(new HHr());
    String[] months =
      new String[] { "Janeiro 2008", "Fevereiro 2008", "Março 2008" };
    for (int n = 0; n < 3; n++) {
      HElement fccCol =
        new HBCol("sm", 3).addAttribute("title",
          "Constante de Processamento FCC").addElement(new HSmall("FCC:"))
          .addSpace().addElement(new HStrong("65,9")).addSpace().addElement(
            new HSmall("mil m3/dia"));
      HElement destCol =
        new HBCol("sm", 3).addAttribute("title",
          "Constante de Processamento DEST").addElement(new HSmall("DEST:"))
          .addSpace().addElement(new HStrong("231,5")).addSpace().addElement(
            new HSmall("mil m3/dia"));
      HElement hdtCol =
        new HBCol("sm", 3).addAttribute("title",
          "Constante de Processamento HDT").addElement(new HSmall("HDT:"))
          .addSpace().addElement(new HStrong("28,9")).addSpace().addElement(
            new HSmall("mil m3/dia"));
      HElement coqCol =
        new HBCol("sm", 3).addAttribute("title",
          "Constante de Processamento COQ").addElement(new HSmall("COQ:"))
          .addSpace().addElement(new HStrong("13,4")).addSpace().addElement(
            new HSmall("mil m3/dia"));
      HBPanel constantPanel =
        new HBPanel().setBody(new HBRow().addElement(fccCol)
          .addElement(destCol).addElement(hdtCol).addElement(coqCol));
      HBTable table = new HBTable(new HBAbstractTableModel() {

        String[] cols = new String[] { "Estoque de Abertura",
          "Entrega para Terceiros", "Fornecimento Interno", "Processamento",
          "Produção", "Movimentação", "Exportação", "Importação",
          "Fechamento", "Estoque Meta" };

        String[] rows =
          new String[] { "NAFTA", "BUTANO", "PROPANO", "ACABADO NORMAL",
          "ACABADO ESPECIAL", "GLP", "QUEROSENE", "DIESEL", "OQ",
          "GLP ENERG", "NAFTA", "BUTANO", "PROPANO", "ACABADO NORMAL",
          "ACABADO ESPECIAL", "GLP", "QUEROSENE", "DIESEL", "OQ",
          "GLP ENERG", "NAFTA", "BUTANO", "PROPANO", "ACABADO NORMAL",
          "ACABADO ESPECIAL", "GLP", "QUEROSENE", "DIESEL", "OQ",
          "GLP ENERG", "NAFTA", "BUTANO", "PROPANO", "ACABADO NORMAL",
          "ACABADO ESPECIAL", "GLP", "QUEROSENE", "DIESEL", "OQ",
        "GLP ENERG" };

        @Override
        public int getRowCount() {
          return cols.length + 1;
        }

        @Override
        public int getColumnCount() {
          return rows.length + 1;
        }

        @Override
        public HElement getElementAt(int row, int column) {
          if (column == 0 && row == 0) {
            return new HText("");
          }
          else if (row == 0) {
            HDiv div = new HDiv();
            div
            .addElement(new HDiv().addElement(new HStrong(rows[column - 1])));
            div.addElement(new HDiv().addElement(new HSmall("mil m3")));
            return div;
          }
          else if (column == 0) {
            return new HStrong(cols[row - 1]);
          }
          else {
            Random r = new Random();
            if (r.nextInt(10) < 3) {
              return new HText("");
            }
            BigDecimal value =
              new BigDecimal(r.nextFloat() * 100).round(new MathContext(3));
            float fvalue = value.floatValue();
            int ivalue = (int) fvalue;
            if (r.nextInt(10) < 4) {
              return new HText(Integer.toString(ivalue));
            }
            else {
              return new HText(value.toString());
            }
          }
        }

        @Override
        public void configCell(HBTableRow rowElement, HBTableCell cellElement,
          int row, int column, HElement elementAt) {
          if (row == 0 || column == 0) {
            cellElement.setBackgroundColor(HBTableColor.ACTIVE);
          }
          if (row == 0) {
            cellElement.alignCenter();
          }
        }

      }).setBordered(true).setCondensed(true);
      root.addElement(new HBPanel().setBackgroundColor(HBColor.INFO)
        .setHeadTitle(months[n]).setBody(
          new HDiv().addElement(constantPanel).addElement(
            new HBScroll().addElement(table)).addElement(
              new HBTextArea().setRowCount(3).setResizable(false).addAttribute(
                "title", "Anotações referente ao mês de Janeiro de 2008"))));
    }
    root.addElement(new HHr());
    {
      root.addElement(new HBRow().addElement(new HBCol("xs", 12).addClass(
        "text-right").addElement(
          this.printButton =
          new HBButton("Imprimir", "Imprime o cenário")
          .addEventClick(new HMouseListener() {
            @Override
            public void action(HMouseEvent e) {
              onPrintAction(e);
            }
          })).addSpace().addElement(
            new HBButton("Exportar", "Exporte o cenário para Excel")
            .addEventClick(new HMouseListener() {
              @Override
              public void action(HMouseEvent e) {
                onExportAction(e);
              }
            }))));
    }
    root.addSpace();
    this.setRoot(root);
  }

  protected void onExportAction(HMouseEvent e) {
    this.init();
  }

  protected void onPrintAction(HMouseEvent e) {
    this.printButton.setEnabled(false);
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          Thread.sleep(5000);
        }
        catch (Exception e) {
        }
        sync(new Runnable() {
          @Override
          public void run() {
            printButton.setEnabled(true);
          }
        });
      }
    }).start();
  }

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    final Map<String, Browser> browsers = new TreeMap<String, Browser>();
    WebServerSocket server = new StandardWebServerSocket(8080);
    try {
      for (;;) {
        WebSocket socket = server.accept();
        final String session = readSession(socket);
        if (session == null) {
          socket.close();
          continue;
        }
        boolean hasBrowser = browsers.containsKey(session);
        Browser browser = browsers.get(session);
        if (browser == null) {
          browser = new BandeiraBrowserTest(socket);
          browsers.put(session, browser);
        }
        // browser.addListener(new BrowserListener() {
        // @Override
        // public void closed() {
        // browsers.remove(session);
        // }
        // });
        if (hasBrowser) {
          browser.restore(socket);
        }
        else {
          browser.start();
        }
      }
    }
    finally {
      server.close();
    }
  }

  private static String readSession(WebSocket socket) throws IOException {
    String session = socket.readMessage();
    if (session == null) {
      return null;
    }
    if (!session.startsWith("session:")) {
      return null;
    }
    return session.substring("session:".length()).trim();
  }

}
