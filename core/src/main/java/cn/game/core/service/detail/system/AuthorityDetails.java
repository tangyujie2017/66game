package cn.game.core.service.detail.system;

import java.util.List;

public class AuthorityDetails {
	
	private Long id;
	
	private String name;
	
	private String code;
	
	private String details;

	private Long parentId;
	
	private String parentName;
	
	private List<AuthorityDetails> children;
	
	private Integer sort;
	
	public AuthorityDetails(){
		
	}

	public AuthorityDetails(Long id){
		this.id = id;
	}
	
	public AuthorityDetails(Long id,String name,String code,String details, Long parentId,String parentName,Integer sort,List<AuthorityDetails> children){
		this.id = id;
		this.name = name;
		this.code = code;
		this.details = details;
		this.parentId = parentId;
		this.parentName = parentName;
		this.sort = sort;
		this.children = children;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public List<AuthorityDetails> getChildren() {
		return children;
	}

	public void setChildren(List<AuthorityDetails> children) {
		this.children = children;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

}
