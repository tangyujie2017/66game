package cn.game.api.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GameApiUtils {
	/**
	 * 
	 * 生成批次号工具类
	 */
	public static String bacthNo() {
		Date nowTime = new Date();
		SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmss");
		return time.format(nowTime);
	}

}
