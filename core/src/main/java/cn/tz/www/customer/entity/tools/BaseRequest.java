
package cn.tz.www.customer.entity.tools;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 公共请求数据体.
 *
 * @author Wang.ch
 */
public class BaseRequest<T extends Serializable> implements Serializable {
  /** */
  private static final long serialVersionUID = -7771183107123560896L;
  /** 数据体. */
  @NotNull @Valid private T data;

  public BaseRequest() {}

  public BaseRequest(T data) {
    this.data = data;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "BaseRequest [data=" + data + "," + super.toString() + "]";
  }
}
