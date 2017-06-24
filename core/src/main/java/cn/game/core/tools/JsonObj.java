package cn.game.core.tools;

public class JsonObj {
	
	private boolean success;
	
	private String message;
	
	private Object result;
	
	public static JsonObj newSuccessJsonObj(String message){
		JsonObj jsonObj = new JsonObj();
		jsonObj.setSuccess(true);
		jsonObj.setMessage(message);
		return jsonObj;
	}
	
	public static JsonObj newSuccessJsonObj(String message,Object resultObject){
		JsonObj jsonObj = new JsonObj();
		jsonObj.setSuccess(true);
		jsonObj.setMessage(message);
		jsonObj.setResult(resultObject);
		return jsonObj;
	}
	
	public static JsonObj newErrorJsonObj(String message){
		JsonObj jsonObj = new JsonObj();
		jsonObj.setSuccess(false);
		jsonObj.setMessage(message);
		return jsonObj;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	

}
