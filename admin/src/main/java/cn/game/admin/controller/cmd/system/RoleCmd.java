package cn.game.admin.controller.cmd.system;


import java.util.ArrayList;
import java.util.List;

import cn.game.core.service.detail.system.AuthorityDetails;
import cn.game.core.service.detail.system.RoleDetails;


/**
 * Created by zzc on 16/11/2016.
 */
public class RoleCmd {

	private Long id;
	private String name;
	private String details;
	
	private Boolean enable=true;
	
	private List<Long> authorityIds;

	public RoleCmd() {
	}

	public RoleCmd(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public RoleCmd(Long id, String name, String details,List<Long> authorityIds) {
		this.id = id;
		this.name = name;
		this.details = details;
		this.authorityIds = authorityIds;
	}
	
	public static RoleCmd fromDetails(RoleDetails roleDetails){
		List<Long> ids = null;
		List<AuthorityDetails> authoritys = roleDetails.getAuthoritys();
		if(authoritys != null){
			List<Long> tmpIds = new ArrayList<>();
			authoritys.stream().forEach(r -> tmpIds.add(r.getId()));
			ids = tmpIds;
		}
		
		return new RoleCmd(roleDetails.getId(), roleDetails.getName(), roleDetails.getDetails(), ids);
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

	public RoleDetails toDetails() {
		List<AuthorityDetails> authorityDetailsList = null;
		if(authorityIds != null){
			List<AuthorityDetails> authDetailsSet = new ArrayList<>();
			authorityIds.stream().forEach(i -> authDetailsSet.add(new AuthorityDetails(Long.valueOf(i))));
			authorityDetailsList = authDetailsSet;
		}
		return new RoleDetails(id, name, details, authorityDetailsList);
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
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
	

}
