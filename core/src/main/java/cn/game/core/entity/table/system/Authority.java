package cn.game.core.entity.table.system;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import cn.game.core.entity.BaseEntity;
import cn.game.core.service.detail.system.AuthorityDetails;

@Entity
@Table(name = "authority")
public class Authority extends BaseEntity {

	private static final long serialVersionUID = -2824880752071083208L;
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	// 权限名称
	@Column(length = 50)
	private String name;

	// 权限代码
	@Column(length = 50)
	private String code;

	// 描述
	@Column(length = 200)
	private String details;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Authority parent;

	@OrderBy(value = "sort ASC")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	private List<Authority> children;

	private Integer sort;

	public Authority() {

	}

	public Authority(Long id, String name, String code, String details, Integer sort, Authority parent) {
		this.id = id;
		this.name = name;
		this.code = code;
		this.details = details;
		this.sort = sort;
		this.parent = parent;
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

	public Authority getParent() {
		return parent;
	}

	public void setParent(Authority parent) {
		this.parent = parent;
	}

	public List<Authority> getChildren() {
		return children;
	}

	public void setChildren(List<Authority> children) {
		this.children = children;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public AuthorityDetails toDetails() {
		Long parentId = null;
		String parentName = null;
		if(parent != null){
			parentId = parent.getId();
			parentName = parent.getName();
		}
		return new AuthorityDetails(id,name,code,details,parentId,parentName,sort,Authority.toDetailsList(children));
	}
	public static List<AuthorityDetails> toDetailsList(List<Authority> authorityList){
		List<AuthorityDetails> detailsList = new ArrayList<>();
		if(authorityList != null){
			authorityList.stream().forEach(authority -> detailsList.add(authority.toDetails()));
		}
		return detailsList;
	}
	public static List<Authority> fromDetailsList(List<AuthorityDetails> detailsList){
		if (detailsList == null)
			return null;
		List<Authority> authList = new ArrayList<>(detailsList.size());
		detailsList.stream().forEach(d -> authList.add(fromDetails(d)));
		return authList;
	}
	public static Authority fromDetails(AuthorityDetails authorityDetails) {
		Authority parent = null;
		if(authorityDetails.getParentId() != null){
			parent = new Authority();
			parent.setId(authorityDetails.getParentId());
		}
		return new Authority(authorityDetails.getId(), authorityDetails.getName(), authorityDetails.getCode(),
				authorityDetails.getDetails(),authorityDetails.getSort(),parent);
	}
}
