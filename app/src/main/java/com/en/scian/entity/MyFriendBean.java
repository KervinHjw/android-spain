package com.en.scian.entity;

import java.util.List;

/**
 * 我的好友基本信息解析类
 * 
 * @author jiyx
 * 
 */
public class MyFriendBean {
	private List<MyFriend> data;
	private String msg;
	private Page page;
	private int status;

	public List<MyFriend> getData() {
		return data;
	}

	public void setData(List<MyFriend> data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
