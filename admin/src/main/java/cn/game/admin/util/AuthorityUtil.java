package cn.game.admin.util;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 
 * @author liubin
 * @date 2017年1月17日
 * @reviewer
 */
public class AuthorityUtil {
	Logger logger=LoggerFactory.getLogger(AuthorityUtil.class);
	
	public static String getLoginUsername(){
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	/**
	 * 判断当前用户是否拥有某个权限
	 * @param authority
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Boolean hasAuthority(String authority,String authority2){
		Boolean result = false;
		List<GrantedAuthority> auths = (List<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		for(GrantedAuthority auth : auths){
			if(authority.equals(auth.getAuthority()) || authority2.equals(auth.getAuthority())){
				result = true;
				break;
			}
		}
		return result;
	}
	
}
