package cn.game.admin.controller.service.req;

/**
 * Created by zzc on 16/11/2016.
 */
public class DeleteReq<T> {

  private T id;

  public DeleteReq(T id) {
    this.id = id;
  }

  public T getId() {
    return id;
  }

  public void setId(T id) {
    this.id = id;
  }
}
