package cn.game.admin.controller.cmd;




import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import cn.game.admin.service.RoleSet;
import cn.game.core.service.detail.system.RoleDetails;
import cn.game.core.service.detail.system.UserDetails;

import java.util.*;

/**
 * Created by zzc on 16/11/2016.
 */
public class UserEditCmd {

	private Long id;

	@Pattern(regexp = "1\\d{10}", message = "手机号码：格式不正确")
	private String mobile;

	@Size(min = 2, max = 10, message = "真实姓名：字符长度在2-10之间")
	private String realName;

	@RoleSet
	private Collection<Long> roles;
	
	private boolean locked = false;

	public UserEditCmd() {
	}

	public UserEditCmd(Long id, String mobile, String realName, boolean locked, Collection<Long> roles) {
		this.id = id;
		this.mobile = mobile;
		this.realName = realName;
		this.roles = roles;
		this.locked = locked;
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

	public Collection<Long> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Long> roles) {
		this.roles = roles;
	}

	public UserDetails toDetails() {
		List<RoleDetails> tmpRoleDetailsSet = null;
		if (roles != null) {
			List<RoleDetails> roleDetailsSet = new ArrayList<>(roles.size());
			roles.stream().forEach(i -> roleDetailsSet.add(new RoleDetails(Long.valueOf(i))));
			tmpRoleDetailsSet = roleDetailsSet;
		}
		UserDetails details = new UserDetails(id, mobile, realName, mobile, locked, tmpRoleDetailsSet);
		return details;
	}
	
	public static UserEditCmd fromDetails(UserDetails details){
		List<Long> roleIds = null;
		List<RoleDetails> roleDetails = details.getRoles();
		if (roleDetails != null) {
			List<Long> tmpRoleIds = new ArrayList<>(details.getRoles().size());
			roleDetails.stream().forEach(r -> tmpRoleIds.add(r.getId()));
			roleIds = tmpRoleIds;
		}
	    return new UserEditCmd(details.getId(), details.getMobile(), details.getRealName(), details.isLocked(), roleIds);
	  }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

}
