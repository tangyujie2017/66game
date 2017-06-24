package cn.game.admin.controller.cmd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import cn.game.admin.service.CustomerRoleSet;
import cn.game.core.service.detail.system.CustomerDetails;
import cn.game.core.service.detail.system.CustomerRoleDetails;

public class CustomerEditCmd {


	private Long id;

	@Pattern(regexp = "1\\d{10}", message = "手机号码：格式不正确")
	private String mobile;

	@Size(min = 2, max = 10, message = "真实姓名：字符长度在2-10之间")
	private String realName;

	@CustomerRoleSet
	private Collection<Long> roles;
	
	private boolean locked = false;
    private  String serverTime;
	public CustomerEditCmd() {
	}

	public CustomerEditCmd(Long id, String mobile, String realName, boolean locked, Collection<Long> roles,String serverTime) {
		this.id = id;
		this.mobile = mobile;
		this.realName = realName;
		this.roles = roles;
		this.locked = locked;
		this. serverTime= serverTime;
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

	public String getServerTime() {
		return serverTime;
	}

	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}

	public CustomerDetails toDetails() {
		List<CustomerRoleDetails> tmpRoleDetailsSet = null;
		if (roles != null) {
			List<CustomerRoleDetails> roleDetailsSet = new ArrayList<>(roles.size());
			roles.stream().forEach(i -> roleDetailsSet.add(new CustomerRoleDetails(Long.valueOf(i))));
			tmpRoleDetailsSet = roleDetailsSet;
		}
		CustomerDetails details = new CustomerDetails(id, mobile, realName, mobile, locked, tmpRoleDetailsSet,serverTime);
		return details;
	}
	
	public static CustomerEditCmd fromDetails(CustomerDetails details){
		List<Long> roleIds = null;
		List<CustomerRoleDetails> roleDetails = details.getRoles();
		if (roleDetails != null) {
			List<Long> tmpRoleIds = new ArrayList<>(details.getRoles().size());
			roleDetails.stream().forEach(r -> tmpRoleIds.add(r.getId()));
			roleIds = tmpRoleIds;
		}
	    return new CustomerEditCmd(details.getId(), details.getMobile(), details.getRealName(), details.isLocked(), roleIds,details.getServerTime());
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
