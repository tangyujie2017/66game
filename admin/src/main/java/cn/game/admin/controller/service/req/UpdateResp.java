package cn.game.admin.controller.service.req;

/**
 * Created by zzc on 16/11/2016.
 */
public class UpdateResp<U> extends Resp {

  private U details;

  public UpdateResp(int status, U details) {
    super(status);
    this.details = details;
  }

  public U getDetails() {
    return details;
  }

  public void setDetails(U details) {
    this.details = details;
  }

}
