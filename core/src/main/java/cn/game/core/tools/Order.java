package cn.game.core.tools;

public class Order {
	private String field;
	private SortType sortType;
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public SortType getSortType() {
		return sortType;
	}
	public void setSortType(SortType sortType) {
		this.sortType = sortType;
	}
	
	public enum SortType{
		ASC,DESC
	}
}


