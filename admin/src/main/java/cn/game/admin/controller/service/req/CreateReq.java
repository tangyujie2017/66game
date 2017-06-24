package cn.game.admin.controller.service.req;


/**
 * Created by zzc on 16/11/2016.
 */
public class CreateReq<T> {

  private T details;

  public CreateReq() {
    // 保留构造方法
  }

  public CreateReq(T details) {
    this.details = details;
  }

  public T getDetails() {
    return details;
  }

  public void setDetails(T details) {
    this.details = details;
  }
}
