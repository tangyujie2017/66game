package cn.tz.www.customer.view;

import java.util.List;

public class CustomerResource {
	private Long id;
	private String login;
	private String userName;
	private String password;
	 private List<Resource> resList;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<Resource> getResList() {
		return resList;
	}
	public void setResList(List<Resource> resList) {
		this.resList = resList;
	}

}
