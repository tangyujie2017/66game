package cn.game.api.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import com.google.gson.GsonBuilder;  
import com.google.gson.reflect.TypeToken; 
public class GameApiUtils {
	
	public static final String START_ARRAY = "[";  
    public static final String END_ARRAY = "]";  
	/**
	 * 
	 * 生成游戏批次号工具类
	 */
	public static String bacthNo() {
		Date nowTime = new Date();
		SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmss");
		return time.format(nowTime);
	}

	/**
	 * 根据requet获取ip地址.
	 *
	 * @param request
	 * @return
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	 @SuppressWarnings("unchecked")  
	    public static final <T> List<T> json2listT(String jsonStr, Class<T> tC) {  
	        //json字符串不能为空  
	        if(StringUtils.isBlank(jsonStr)) return null;  
	        //json字符串必须为数组节点类型  
	        if(!(jsonStr.startsWith(START_ARRAY) && jsonStr.endsWith(END_ARRAY))) return null;  
	        List<T> listT = null;  
	        try {  
	            //创建泛型对象  
	            T t =  tC.newInstance();  
	            //利用类加载加载泛型的具体类型  
	            Class<T> classT = (Class<T>) Class.forName(t.getClass().getName());  
	            List<Object> listObj = new ArrayList<Object>();  
	            //将数组节点中json字符串转换为object对象到Object的list集合  
	            listObj = new GsonBuilder().create().fromJson(jsonStr, new TypeToken<List<Object>>(){}.getType());  
	            //转换未成功  
	            if(listObj == null || listObj.isEmpty()) return null;  
	            listT = new ArrayList<T>();  
	            //将Object的list中的的每一个元素中的json字符串转换为泛型代表的类型加入泛型代表的list集合返回  
	            for (Object obj : listObj) {  
	                T perT = new GsonBuilder().create().fromJson(obj.toString(), classT);  
	                listT.add(perT);  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        return listT;  
	    }  
}
