package cn.game.admin.controller.service.req;

/**
 * Created by zzc on 16/11/2016.
 */
public class Resp {

  public static final int SUCCESS = 0;
  public static final int FAIL = 1;

  private int status;

  public Resp(int status) {
    this.status = status;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public static final Resp success(){
    return new Resp(SUCCESS);
  }

  public static final Resp fail(){
    return new Resp(FAIL);
  }

}
