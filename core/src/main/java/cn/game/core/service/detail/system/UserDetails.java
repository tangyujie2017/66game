package cn.game.core.service.detail.system;

import java.util.Date;
import java.util.List;

import cn.game.core.tools.CommonUtil;


/** Created by zzc on 16/11/2016. */
public class UserDetails {

	private Long id;

	private String mobile;

	private String realName;

	private String login;

	private String password;

	private boolean locked;

	private String headPhoto;
    
	private String createTime;
	private String basePath;
	private List<RoleDetails> roles;

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	private String roleStr;
	/** 用户信息 */
	private UserProfileDetails profile;

	public UserDetails() {
		// 保留构造方法
	}

	public UserDetails(Long id) {
		this.id = id;
	}

	public UserDetails(Long id, String realName) {
		this.id = id;
		this.realName = realName;
	}

	public UserDetails(long id, String mobile, String realName, String login, String password, boolean locked,
			List<RoleDetails> roles, String headPhoto, Date createTime) {
		this.id = id;
		this.mobile = mobile;
		this.realName = realName;
		this.login = login;
		this.password = password;
		this.locked = locked;
		this.roles = roles;
		if (!roles.isEmpty()) {
			StringBuffer sBuffer = new StringBuffer();
			for (RoleDetails role : roles) {
				sBuffer.append(role.getDetails() + ",");
			}
			sBuffer.deleteCharAt(sBuffer.length() - 1);
			roleStr = sBuffer.toString();
		}
		this.headPhoto = headPhoto;
		this.createTime = CommonUtil.getNow(2, createTime);
	}

	public UserDetails(long id, String mobile, String realName, String login, String password, boolean locked,
			List<RoleDetails> roles, String headPhoto, Date createTime, UserProfileDetails profile) {
		this.id = id;
		this.mobile = mobile;
		this.realName = realName;
		this.login = login;
		this.password = password;
		this.locked = locked;
		this.roles = roles;
		if (!roles.isEmpty()) {
			StringBuffer sBuffer = new StringBuffer();
			for (RoleDetails role : roles) {
				sBuffer.append(role.getDetails() + ",");
			}
			sBuffer.deleteCharAt(sBuffer.length() - 1);
			roleStr = sBuffer.toString();
		}
		this.headPhoto = headPhoto;
		this.createTime = CommonUtil.getNow(2, createTime);
		this.profile = profile;
	}

	public UserDetails(long id, String mobile, String realName, String login, String password, boolean locked,
			String headPhoto, Date createTime) {
		this.id = id;
		this.mobile = mobile;
		this.realName = realName;
		this.login = login;
		this.password = password;
		this.locked = locked;
		this.headPhoto = headPhoto;
		this.createTime = CommonUtil.getNow(2, createTime);
	}

	public UserDetails(long id, String mobile, String realName, String login, String password, boolean locked,
			List<RoleDetails> roles, String headPhoto) {
		this.id = id;
		this.mobile = mobile;
		this.realName = realName;
		this.login = login;
		this.password = password;
		this.locked = locked;
		this.roles = roles;
		if (!roles.isEmpty()) {
			StringBuffer sBuffer = new StringBuffer();
			for (RoleDetails role : roles) {
				sBuffer.append(role.getDetails() + ",");
			}
			sBuffer.deleteCharAt(sBuffer.length() - 1);
			roleStr = sBuffer.toString();
		}
		this.headPhoto = headPhoto;
	}

	public UserDetails(long id, String mobile, String realName, String login, boolean locked, List<RoleDetails> roles) {
		this.id = id;
		this.mobile = mobile;
		this.realName = realName;
		this.login = login;
		this.locked = locked;
		this.roles = roles;
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

	public List<RoleDetails> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleDetails> roles) {
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

	/** @return the profile */
	public UserProfileDetails getProfile() {
		return profile;
	}

	/**
	 * @param profile
	 *            the profile to set
	 */
	public void setProfile(UserProfileDetails profile) {
		this.profile = profile;
	}
}
