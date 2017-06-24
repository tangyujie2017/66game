package cn.game.api.util;

public class PageParamProduct {
	private int pageSize=10;
	private int pageIndex=1;
	private SearchJsonProduct search;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public SearchJsonProduct getSearch() {
		return search;
	}

	public void setSearch(SearchJsonProduct search) {
		this.search = search;
	}

	
}
