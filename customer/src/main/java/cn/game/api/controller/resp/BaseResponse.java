package cn.game.api.controller.resp;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BaseResponse implements Serializable {
	/** */
	private static final long serialVersionUID = -4001746369042753079L;
	/** 其他业务错误code,以11xxx格式. */
	/** 成功. */
	public static int STATUS_SUCCESS = 10200;
	/** 错误请求. */
	public static int STATUS_BADREQUEST = 10400;
	/** 未授权. */
	public static int STATUS_UNAUTH = 10401;
	/** 无权限访问. */
	public static int STATUS_FORBIDDEN = 10403;
	/** 系统错误. */
	public static int STATUS_ERROR = 10500;
	/** 系统自定义错误. */
	public static int STATUS_SYSTEM = 10100;
	
	public static int BUSINESS_SYSTEM = 11200;

	/** 版本号. */
	private String ver;
	/** 设备id. */
	private String deviceid;
	/** 数据体. */
	private Object data;
	/** 时间戳. */
	private Long timestamp = System.currentTimeMillis();
	/** 响应状态码. */
	private int status;
	/** 错误信息. */
	private String msg;
	/** 签名信息 */
	private String sign;

	@JsonIgnore
	private transient ResponseEntity<BaseResponse> base;

	public BaseResponse() {
		// 无参构造
	}

	/**
	 * 产生一个新的成功BaseResponse
	 *
	 * @param data
	 *            数据
	 * @return
	 */
	public static BaseResponse newSuccess(Object data) {
		return new BaseResponse(data);
	}

	/**
	 * 产生一个新的成功BaseResponse
	 *
	 * @param data
	 *            数据
	 * @param ver
	 *            版本
	 * @return
	 */
	public static BaseResponse newSuccess(Object data, String ver) {
		return new BaseResponse(data, ver);
	}

	/**
	 * 产生一个新的失败BaseResponse
	 *
	 * @param status
	 *            BaseResponse.xx
	 * @param msg
	 *            错误消息.
	 * @return
	 */
	public static BaseResponse newFail(int status, String msg) {
		return new BaseResponse(status, msg);
	}

	public BaseResponse(Object data) {
		this.data = data;
		this.status = BaseResponse.STATUS_SUCCESS;
		base = new ResponseEntity<>(this, HttpStatus.OK);
	}

	public BaseResponse(Object data, String ver) {
		this.data = data;
		this.ver = ver;
		this.status = BaseResponse.STATUS_SUCCESS;
		base = new ResponseEntity<>(this, HttpStatus.OK);
	}

	public BaseResponse(int status, String msg) {
		this.status = status;
		this.msg = msg;
		//System.out.println(this);
		base = new ResponseEntity<>(this, HttpStatus.OK);
		//System.out.println(base);
	}

	public ResponseEntity<BaseResponse> build() {
		return base;
	}

	public static ResponseEntity<BaseResponse> success(Object data) {
		return new BaseResponse(data).build();
	}

	public static ResponseEntity<BaseResponse> systemError(String msg) {
		return new BaseResponse(BaseResponse.STATUS_SYSTEM, msg).build();
	}

	public static ResponseEntity<BaseResponse> error(String msg) {
		return new BaseResponse(BaseResponse.STATUS_ERROR, msg).build();
	}

	public static ResponseEntity<BaseResponse> badrequest(String msg) {
		return new BaseResponse(BaseResponse.STATUS_BADREQUEST, msg).build();
	}

	public static ResponseEntity<BaseResponse> forbidden(String msg) {
		return new BaseResponse(BaseResponse.STATUS_FORBIDDEN, msg).build();
	}

	public static ResponseEntity<BaseResponse> unauth(String msg) {
		return new BaseResponse(BaseResponse.STATUS_UNAUTH, msg).build();
	}
	public static ResponseEntity<BaseResponse> business(String msg) {
		return new BaseResponse(BaseResponse.BUSINESS_SYSTEM, msg).build();
	}

	public String getVer() {
		return ver;
	}

	public BaseResponse setVer(String ver) {
		this.ver = ver;
		return this;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public Object getData() {
		return data;
	}

	public BaseResponse setData(Object data) {
		this.data = data;
		return this;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public BaseResponse setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public int getStatus() {
		return status;
	}

	public BaseResponse setStatus(int status) {
		this.status = status;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public BaseResponse setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	/** @return the sign */
	public String getSign() {
		return sign;
	}

	/**
	 * @param sign
	 *            the sign to set
	 */
	public BaseResponse setSign(String sign) {
		this.sign = sign;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("BaseResponse [ver=%s, deviceid=%s, data=%s, timestamp=%s, status=%s, msg=%s, sign=%s]",
				ver, deviceid, data, timestamp, status, msg, sign);
	}
}
