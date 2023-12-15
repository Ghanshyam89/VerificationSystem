package com.verificationsys.dto;

public class PageInfo {
	private boolean hasNextPage;
	private boolean hasPreviousPage;
	private Long total;

	public PageInfo(boolean hasNextPage, boolean hasPreviousPage, Long total) {
		this.hasNextPage = hasNextPage;
		this.hasPreviousPage = hasPreviousPage;
		this.total = total;
	}

	public PageInfo() {
		// TODO Auto-generated constructor stub
	}

	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	public boolean isHasPreviousPage() {
		return hasPreviousPage;
	}

	public void setHasPreviousPage(boolean hasPreviousPage) {
		this.hasPreviousPage = hasPreviousPage;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

}
