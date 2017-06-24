package cn.game.core.service.detail.system;

import java.util.Date;
import java.util.List;

import cn.game.core.tools.CommonUtil;

public class CustomerDetails {


	private Long id;

	private String mobile;

	private String realName;

	private String login;

	private String password;

	private boolean locked;

	private String headPhoto;
    
	private String createTime;
	private String basePath;
	private String serverTime;
	private List<CustomerRoleDetails> roles;

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	private String roleStr;
	

	public CustomerDetails() {
		// 保留构造方法
	}

	public CustomerDetails(Long id) {
		this.id = id;
	}

	public CustomerDetails(Long id, String realName) {
		this.id = id;
		this.realName = realName;
	}

	public CustomerDetails(long id, String mobile, String realName, String login, String password, boolean locked,
			List<CustomerRoleDetails> roles, String headPhoto, Date createTime,String serverTime) {
		this.id = id;
		this.mobile = mobile;
		this.realName = realName;
		this.login = login;
		this.password = password;
		this.locked = locked;
		this.roles = roles;
		if (!roles.isEmpty()) {
			StringBuffer sBuffer = new StringBuffer();
			for (CustomerRoleDetails role : roles) {
				sBuffer.append(role.getDetails() + ",");
			}
			sBuffer.deleteCharAt(sBuffer.length() - 1);
			roleStr = sBuffer.toString();
		}
		this.headPhoto = headPhoto;
		this.createTime = CommonUtil.getNow(2, createTime);
		this.serverTime=serverTime;
	}

	

	public CustomerDetails(long id, String mobile, String realName, String login, String password, boolean locked,
			String headPhoto, Date createTime,String serverTime) {
		this.id = id;
		this.mobile = mobile;
		this.realName = realName;
		this.login = login;
		this.password = password;
		this.locked = locked;
		this.headPhoto = headPhoto;
		this.createTime = CommonUtil.getNow(2, createTime);
		this.serverTime=serverTime;
	}

	public CustomerDetails(long id, String mobile, String realName, String login, String password, boolean locked,
			List<CustomerRoleDetails> roles, String headPhoto,String serverTime) {
		this.id = id;
		this.mobile = mobile;
		this.realName = realName;
		this.login = login;
		this.password = password;
		this.locked = locked;
		this.roles = roles;
		this.serverTime=serverTime;
		if (!roles.isEmpty()) {
			StringBuffer sBuffer = new StringBuffer();
			for (CustomerRoleDetails role : roles) {
				sBuffer.append(role.getDetails() + ",");
			}
			sBuffer.deleteCharAt(sBuffer.length() - 1);
			roleStr = sBuffer.toString();
		}
		this.headPhoto = headPhoto;
	}

	public CustomerDetails(long id, String mobile, String realName, String login, boolean locked, List<CustomerRoleDetails> roles,String serverTime) {
		this.id = id;
		this.mobile = mobile;
		this.realName = realName;
		this.login = login;
		this.locked = locked;
		this.roles = roles;
		this.serverTime= serverTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public List<CustomerRoleDetails> getRoles() {
		return roles;
	}

	public void setRoles(List<CustomerRoleDetails> roles) {
		this.roles = roles;
	}

	public String getRoleStr() {
		return roleStr;
	}

	public void setRoleStr(String roleStr) {
		this.roleStr = roleStr;
	}

	public String getHeadPhoto() {
		return headPhoto;
	}

	public void setHeadPhoto(String headPhoto) {
		this.headPhoto = headPhoto;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getServerTime() {
		return serverTime;
	}

	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}

	

}
