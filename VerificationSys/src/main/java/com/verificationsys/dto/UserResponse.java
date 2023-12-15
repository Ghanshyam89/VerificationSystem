package com.verificationsys.dto;

import java.util.List;

public class UserResponse {
	private List<UserInfo> data;
	private PageInfo pageInfo;

	public UserResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserResponse(List<UserInfo> data, PageInfo pageInfo) {
		this.data = data;
		this.pageInfo = pageInfo;
	}

	public List<UserInfo> getData() {
		return data;
	}

	public void setData(List<UserInfo> data) {
		this.data = data;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}

}