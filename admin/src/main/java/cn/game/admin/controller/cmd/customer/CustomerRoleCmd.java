package cn.game.admin.controller.cmd.customer;


import java.util.ArrayList;
import java.util.List;

import cn.game.core.service.detail.system.AuthorityDetails;
import cn.game.core.service.detail.system.CustomerAuthorityDetails;
import cn.game.core.service.detail.system.CustomerRoleDetails;
import cn.game.core.service.detail.system.RoleDetails;


/**
 * Created by zzc on 16/11/2016.
 */
public class CustomerRoleCmd {

	private Long id;
	private String name;
	private String details;
	private Boolean enable=true;
	
	
	private List<Long> authorityIds;

	public CustomerRoleCmd() {
	}

	public CustomerRoleCmd(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public CustomerRoleCmd(Long id, String name, String details,List<Long> authorityIds) {
		this.id = id;
		this.name = name;
		this.details = details;
		this.authorityIds = authorityIds;
	}
	
	public static CustomerRoleCmd fromDetails(CustomerRoleDetails roleDetails){
		List<Long> ids = null;
		List<CustomerAuthorityDetails> authoritys = roleDetails.getAuthoritys();
		if(authoritys != null){
			List<Long> tmpIds = new ArrayList<>();
			authoritys.stream().forEach(r -> tmpIds.add(r.getId()));
			ids = tmpIds;
		}
		
		return new CustomerRoleCmd(roleDetails.getId(), roleDetails.getName(), roleDetails.getDetails(), ids);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CustomerRoleDetails toDetails() {
		List<CustomerAuthorityDetails> authorityDetailsList = null;
		if(authorityIds != null){
			List<CustomerAuthorityDetails> authDetailsSet = new ArrayList<>();
			authorityIds.stream().forEach(i -> authDetailsSet.add(new CustomerAuthorityDetails(Long.valueOf(i))));
			authorityDetailsList = authDetailsSet;
		}
		return new CustomerRoleDetails(id, name, details, authorityDetailsList);
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	

	public List<Long> getAuthorityIds() {
		return authorityIds;
	}

	public void setAuthorityIds(List<Long> authorityIds) {
		this.authorityIds = authorityIds;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

}
