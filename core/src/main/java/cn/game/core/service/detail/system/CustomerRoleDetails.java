package cn.game.core.service.detail.system;

import java.util.List;

public class CustomerRoleDetails {

	
	  private Long id;
	  
	  private String name;
	  
	  private String details;
	
	  private List<CustomerAuthorityDetails> authoritys;
	  
	  private String authStr;

	  public CustomerRoleDetails() {
	  }
	  
	  public CustomerRoleDetails(Long id){
		  this.id = id;
	  }
	  
	  public CustomerRoleDetails(Long id,String name, String details){
		  this.id = id;
		  this.name = name;
		  this.details = details;
	  }

	  public CustomerRoleDetails(Long id, String name, String details,  List<CustomerAuthorityDetails> authoritys) {
	    this.id = id;
	    this.name = name;
	    this.details = details;
	    this.authoritys = authoritys;
	    if (authoritys != null && !authoritys.isEmpty()) {
			StringBuffer sBuffer = new StringBuffer();
			for (CustomerAuthorityDetails auth : authoritys) {
				sBuffer.append(auth.getName() + ",");
			}
			sBuffer.deleteCharAt(sBuffer.length() - 1);
			authStr = sBuffer.toString();
		}
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

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	

	public List<CustomerAuthorityDetails> getAuthoritys() {
		return authoritys;
	}

	public void setAuthoritys(List<CustomerAuthorityDetails> authoritys) {
		this.authoritys = authoritys;
	}

	public String getAuthStr() {
		return authStr;
	}

	public void setAuthStr(String authStr) {
		this.authStr = authStr;
	}

}
