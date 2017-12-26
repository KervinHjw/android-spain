package com.en.scian.entity;

import java.util.List;

/**
 * 我的好友留言解析类
 * 
 * @author jiyx
 * 
 */
public class MyFriend_MessageBean {
	private List<MyFriend_Message> data;
	private String msg;
	private Page page;
	private int status;

	public List<MyFriend_Message> getData() {
		return data;
	}

	public void setData(List<MyFriend_Message> data) {
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
