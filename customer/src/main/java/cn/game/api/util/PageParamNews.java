package cn.game.api.util;

public class PageParamNews {
	private int pageSize=10;
	private int pageIndex=1;
	private SearchJsonNews search;

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

	public SearchJsonNews getSearch() {
		return search;
	}

	public void setSearch(SearchJsonNews search) {
		this.search = search;
	}

	
}
