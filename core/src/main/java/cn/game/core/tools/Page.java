package cn.game.core.tools;

import java.util.List;

@SuppressWarnings("rawtypes")
public class Page<T> {

	public Page() {
	}

	public Page(int pageSize, int currentPage) {
		this.pageSize = pageSize;
		this.currentPage = currentPage;
	}

	private List items;

	//总记录数
	private int totalCount;

	//总共页数
	private int totalPageCount;

	//每页记录个数
	private int pageSize;

	//当前页数
	private int currentPage;

	//查询索引位置
	private int fromIndex = -1;

	public List getItems() {
		return items;
	}

	public void setItems(List items) {
		this.items = items;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalPageCount() {
		totalPageCount = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
		return totalPageCount;
	}

	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getFromIndex() {
		return fromIndex;
	}

	public void setFromIndex(int fromIndex) {
		this.fromIndex = fromIndex;
	}

}
