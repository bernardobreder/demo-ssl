// package browser.mock;
//
// import java.io.ByteArrayInputStream;
// import java.io.IOException;
//
// import javax.xml.parsers.ParserConfigurationException;
//
// import junit.framework.Assert;
//
// import org.junit.Test;
// import org.xml.sax.SAXException;
//
// import util.Bytes;
//
// public class MockBrowserTest {
//
// @Test
// public void testOnlyOneTag() throws Exception {
// MockBrowser browser = createBrowser();
// String content = open(browser, "<h1></h1>").getContent();
// Assert.assertEquals("<html><head></head><body><h1></h1></body></html>",
// content);
// }
//
// @Test
// public void testScriptBodyPosition() throws Exception {
// MockBrowser browser = createBrowser();
// String content = open(browser, "<script></script>").getContent();
// Assert.assertEquals("<html><head></head><body><script></script></body></html>",
// content);
// }
//
// @Test
// public void testScriptHeadPosition() throws Exception {
// MockBrowser browser = createBrowser();
// String content = open(browser,
// "<head><script></script></head>").getContent();
// Assert.assertEquals("<html><head><script></script></head><body></body></html>",
// content);
// }
//
// @Test
// public void testScriptDocumentWrite() throws Exception {
// MockBrowser browser = createBrowser();
// String content = open(browser,
// "<head><script>document.write('<p>');</script></head><body></body>").getContent();
// Assert.assertEquals("<html><head><script></script></head><body></body></html>",
// content);
// }
//
//
// protected static MockBrowser open(MockBrowser browser, String html) throws
// SAXException, ParserConfigurationException, IOException {
// browser.open(new ByteArrayInputStream(Bytes.getUtf8Bytes(html)), "/");
// return browser;
// }
//
// protected static MockBrowser createBrowser() {
// return new MockBrowser(8080);
// }
//
// }
