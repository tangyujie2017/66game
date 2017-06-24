package cn.game.admin.util;


import java.util.List;

public class ZtreeVo {

	private Long id;

	private Long pId;

	private String temp = "";

	private String name;

	// 是否打开节点
	private boolean open = false;

	// 是否是父节点
	private boolean isParent = false;

	// 复选框是否选择
	private boolean checked = false;

	// 是否展示复选框
	private boolean nocheck = false;

	// 设置节点的 checkbox / radio 是否禁用
	private boolean chkDisabled = false;

	// 类型
	private String type;

	private List children;

	// 排序
	private Integer sort;
	
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

	public Long getpId() {
		return pId;
	}

	public void setpId(Long pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}

	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean getNocheck() {
		return nocheck;
	}

	public void setNocheck(boolean nocheck) {
		this.nocheck = nocheck;
	}

	public boolean getChkDisabled() {
		return chkDisabled;
	}

	public void setChkDisabled(boolean chkDisabled) {
		this.chkDisabled = chkDisabled;
	}

	public List getChildren() {
		return children;
	}

	public void setChildren(List children) {
		this.children = children;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
