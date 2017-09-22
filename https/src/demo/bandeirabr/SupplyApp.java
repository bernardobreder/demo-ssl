// package demo.bandeirabr;
//
// import html.HElement;
// import html.IBrowser;
// import html.javascript.JavaScript;
// import html.javascript.StandardJavaScript;
// import html.primitive.HTextHeader;
// import http.HttpRequest;
//
// import java.io.IOException;
// import java.util.Map;
// import java.util.TreeMap;
//
// import util.StringUtil;
// import ws.IWsSocket;
// import ws.standard.WebServerSocket;
// import browser.standard.Browser;
// import demo.bandeirabr.desktop.DesktopFrame;
// import demo.bandeirabr.scenarium.ScenariumFrame;
//
// /**
// *
// *
// * @author bernardobreder
// */
// public class SupplyApp extends Browser {
//
// /** Javascript */
// private static final StandardJavaScript JAVASCRIPT = new
// StandardJavaScript();
//
// /**
// * @param socket
// * @param javascript
// * @throws IOException
// */
// public SupplyApp(JavaScript javascript) throws IOException {
// super(javascript);
// }
//
// /**
// * {@inheritDoc}
// */
// @Override
// public HElement request(HttpRequest request) {
// String url = request.getUrl();
// if (url.equals("/")) {
// return new DesktopFrame();
// } else if (url.equals("/scenarium")) {
// int scenaId = Integer.parseInt(request.checkHeaderString("id"));
// return new ScenariumFrame(scenaId);
// } else {
// return new HTextHeader(1, "Página não encontrada");
// }
// }
//
// /**
// * @param server
// * @throws IOException
// */
// public static void main(WebServerSocket server) throws IOException {
// final Map<String, Browser> browsers = new TreeMap<String, Browser>();
// try {
// for (;;) {
// IWsSocket socket = server.accept();
// String message = socket.readMessage();
// if (message == null) {
// socket.close();
// continue;
// }
// Map<String, String> header = StringUtil.splitHeader(message);
// String session = header.get("session");
// if (session == null) {
// socket.close();
// continue;
// }
// Browser browser = browsers.get(session);
// if (browser == null) {
// browser = new SupplyApp(socket, JAVASCRIPT);
// browsers.put(session, browser);
// browser.start();
// } else {
// browser.restore(socket);
// }
// final String url = !header.containsKey("url") ? "/" : header.get("url");
// browser.sync(new BrowserSync() {
// @Override
// public void run(IBrowser browser) {
// browser.goTo(url);
// }
// });
// }
// } catch (Throwable e) {
// e.printStackTrace();
// } finally {
// server.close();
// }
// }
//
// }
