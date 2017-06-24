package cn.game.admin.controller.service.req;

/**
 * Created by zzc on 16/11/2016.
 */
public class ReadReq<T> {
  private T value;

  public ReadReq(T field) {
    this.value = field;
  }

  public T getValue() {
    return value;
  }

  public void setValue(T value) {
    this.value = value;
  }
}
