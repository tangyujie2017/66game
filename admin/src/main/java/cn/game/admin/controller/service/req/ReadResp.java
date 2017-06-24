package cn.game.admin.controller.service.req;

/**
 * Created by zzc on 16/11/2016.
 */
public class ReadResp<U,V> extends Resp {

  private U details;
  private V id;

  public ReadResp(int status, U details, V id) {
    super(status);
    this.details = details;
    this.id = id;
  }

  public U getDetails() {
    return details;
  }

  public void setDetails(U details) {
    this.details = details;
  }

  public V getId() {
    return id;
  }

  public void setId(V id) {
    this.id = id;
  }

}
