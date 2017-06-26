package cn.game.api.exception;
public class BizException extends RuntimeException {

	private static final long serialVersionUID = 3638887403429291292L;
	/**
	 * 提示
	 */
	public static final int WARNING_CODE = 1000;
	/**
	 * 错误
	 */
	public static final int ERROR_CODE = 1001;
	/**
     * 状态码
     */
    private final int code;
    public BizException(String msg) {
        super(msg);
        this.code = BizException.WARNING_CODE;
    }

    public BizException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
