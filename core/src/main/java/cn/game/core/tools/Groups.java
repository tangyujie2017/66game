package cn.game.core.tools;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.springframework.util.StringUtils;

import cn.game.core.tools.PropertyFilter.MatchType;

public class Groups {

	private List<Group> groupList = new LinkedList<Group>();

	private MatchType childrelation = null;

	private Groups childGroups = null;

	private List<Groups> childGroups2;

	private boolean alias = false;

	private String orderby = "";

	private String groupby = "";

	private String[] orderbys;

	private boolean[] orders;

	private boolean order = false;

	public Groups() {
	}

	public Groups(Groups groups) {
		if (groupList == null) {
			groupList = new ArrayList<Group>();
		}
		groupList.addAll(groups.groupList);
	}

	public Groups(String fieldName, Object obj) {
		if (groupList == null) {
			groupList = new ArrayList<Group>();
		}
		Group group = new Group(fieldName, obj);
		groupList.add(group);
	}

	public Groups(Group group) {
		if (groupList == null) {
			groupList = new ArrayList<Group>();
		}
		if (group != null)
			groupList.add(group);
	}

	public Groups(List<Group> groupList) {
		if (groupList == null) {
			groupList = new ArrayList<Group>();
		}
		this.groupList = groupList;
	}

	public void filterOrders(String orderby, String dir) {
		if (orderby != null && !"".equals(orderby)) {
			this.setOrderby(orderby);
			boolean order = true;
			if ("desc".equals(dir)) {
				order = false;
			}
			this.setOrder(order);
		}
	}

	public void RemoveChild() {
		childGroups = null;
	}

	public void RemoveAll() {
		childGroups = null;
		groupList = null;
		childGroups2 = null;
		this.setOrderby(null);
		this.setOrderbys(null);
	}

	public void RemoveOders(Groups groups) {
		if (groups.getOrderby() != null && !groups.getOrderby().equals("")) {
			groups.setOrderby(null);
		}
		if (groups.getOrderbys() != null && groups.getOrderbys().length > 0) {
			groups.setOrderbys(null);
		}
	}

	public void transferGroupsOrders(Groups groups, Groups newGroups) {
		if (groups.getOrderby() != null && !groups.getOrderby().equals("")) {
			newGroups.setOrderby(groups.getOrderby());
			newGroups.setOrder(groups.isOrder());
		}
		if (groups.getOrderbys() != null && groups.getOrderbys().length > 0) {
			newGroups.setOrderbys(groups.getOrderbys());
			newGroups.setOrders(groups.getOrders());
		}
	}

	public void Add(String fieldName, Object obj) {
		if (groupList == null) {
			groupList = new ArrayList<Group>();
		}
		Group group = new Group(fieldName, obj);
		groupList.add(group);
	}

	public void Add(String fieldName, Object obj, MatchType matchType, MatchType relation) {
		if (groupList == null) {
			groupList = new ArrayList<Group>();
		}
		Group group = new Group(fieldName, obj, matchType, relation);
		groupList.add(group);
	}
	
	public void Add(String propertyName, Object propertyValue1, Object propertyValue2, MatchType matchType) {
		if (groupList == null) {
			groupList = new ArrayList<Group>();
		}
		Group group = new Group(propertyName, propertyValue1, propertyValue2, matchType);
		groupList.add(group);
	}
	
	

	public void Add(String fieldName, Object obj, MatchType matchType) {
		if (groupList == null) {
			groupList = new ArrayList<Group>();
		}
		Group group = new Group(fieldName, obj, matchType);
		groupList.add(group);
	}

	public void Add(String fieldName, MatchType matchType, MatchType relation) {
		if (groupList == null) {
			groupList = new ArrayList<Group>();
		}
		Group group = new Group(fieldName, matchType, relation);
		groupList.add(group);
	}

	public void Add(Group group) {
		if (groupList == null) {
			groupList = new ArrayList<Group>();
		}
		groupList.add(group);
	}

	public void Add(Groups groups) {
		if (groupList == null) {
			groupList = new ArrayList<Group>();
		}
		groupList.addAll(groups.getGroupList());
	}

	public void Add(List<Group> groups) {
		if (groupList == null) {
			groupList = new ArrayList<Group>();
		}
		groupList.addAll(groups);
	}

	public void Remove(Group group) {
		if (groupList != null && !groupList.isEmpty() && group != null)
			groupList.remove(group);
	}

	public void Remove(int index) {
		if (!groupList.isEmpty() && index < groupList.size())
			groupList.remove(index);

	}

	public Group findByName(String name) {
		if (null != groupList && !groupList.isEmpty() && null != name) {
			for (Group g : groupList) {
				if (g.getPropertyName().equals(name.trim()))
					return g;
			}
		}

		return null;
	}

	public void RemoveByName(String name) {
		if (!groupList.isEmpty() && null != name) {
			List<Group> groups = new ArrayList<Group>();
			for (Group group : groupList) {
				if (group.getPropertyName().equals(name.trim())) {
					groups.add(group);

				}
			}
			for (Group group : groups) {
				this.Remove(group);
			}
		}
	}

	public List<Group> getGroupList() {
		if (groupList == null) {
			groupList = new ArrayList<Group>();
		}
		return groupList;
	}

	public void setGroupList(List<Group> groupList) {
		this.groupList = groupList;
	}

	public Groups getChildGroups() {
		return childGroups;
	}

	public void setChildGroups(Groups childGroups) {
		this.childGroups = childGroups;
	}

	public boolean isAlias() {
		return alias;
	}

	public void setAlias(boolean alias) {
		this.alias = alias;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public boolean isOrder() {
		return order;
	}

	public void setOrder(boolean order) {
		this.order = order;
	}

	public String[] getOrderbys() {
		return orderbys;
	}

	public void setOrderbys(String[] orderbys) {
		this.orderbys = orderbys;
	}

	public boolean[] getOrders() {
		return orders;
	}

	public void setOrders(boolean[] orders) {
		this.orders = orders;
	}

	public MatchType getChildrelation() {
		return childrelation;
	}

	public void setChildrelation(MatchType childrelation) {
		this.childrelation = childrelation;
	}

	public List<Groups> getChildGroups2() {
		if (childGroups2 == null) {
			childGroups2 = new LinkedList<Groups>();
		}
		return childGroups2;
	}

	public void setChildGroups2(List<Groups> childGroups2) {
		this.childGroups2 = childGroups2;
	}

	public String getVal(String name) {

		for (Group g : getGroupList()) {
			if (g.getMatchType() != MatchType.IN && g.getMatchType() != MatchType.NOTIN
					&& name.equals(g.getPropertyName()) && !StringUtils.isEmpty((String) g.getPropertyValue1())) {
				return g.getPropertyValue1().toString();
			}
		}
		return null;
	}

	public Object findListValue(String name) {
		for (Group g : getGroupList()) {
			if (g.getMatchType() == MatchType.IN || g.getMatchType() == MatchType.NOTIN) {
				return g.getPropertyValue1();
			}
		}
		return name;
	}

	public String getGroupby() {
		return groupby;
	}

	public void setGroupby(String groupby) {
		this.groupby = groupby;
	}

}
