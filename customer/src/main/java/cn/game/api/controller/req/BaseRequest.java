package cn.game.api.controller.req;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class BaseRequest<T extends Serializable> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -554013973302309987L;
	@NotNull
	@Valid
	private T data;

	public BaseRequest() {
	}

	public BaseRequest(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "BaseRequest [data=" + data + "," + super.toString() + "]";
	}
}
