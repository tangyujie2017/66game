package cn.game.api.service.login;

public interface GameLoginService {
	public Object login(String userName,String userPwd);
	public Object login(Long userid);
	public Object userRegist(String userName,String userPwd);
	public Object weChatLogin(String weChatId);
	public Object  userLoginLog();

}
