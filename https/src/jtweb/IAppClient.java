package jtweb;

public interface IAppClient {

  public void close();

  public IAppClient requireTitle(String string);

  public IAppClient start();

  public IAppClient open(String url);

}
