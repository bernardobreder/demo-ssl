package jtweb;

import html.HElement;

import java.io.IOException;
import java.util.Map;

import util.XmlNode;

public interface IAppServer {

  public XmlNode html() throws IOException;

  public HElement page(String url, Map<String, Object> header)
    throws IOException;

  public void close();

  public IAppServer start();

}
