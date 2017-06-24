package cn.game.admin.util;



import cn.game.core.table.User;

public class NewsVo {
	private Long id;

	private Integer status;
	
	private String title;
	private String declareContext;
	private String context;
	private String createUser;
	 private String createTime;
	 private Integer viewTimes;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDeclareContext() {
		return declareContext;
	}
	public void setDeclareContext(String declareContext) {
		this.declareContext = declareContext;
	}
	public String getContext() {
		return context;
	}
	
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Integer getViewTimes() {
		return viewTimes;
	}
	public void setViewTimes(Integer viewTimes) {
		this.viewTimes = viewTimes;
	}
	 
}
