package com.ludees.scian.entity;

import java.util.List;

/**
 * 好友验证消息解析类
 * 
 * @author jiyx
 * 
 */
public class FriendVerificationMessageBean {

	private List<FriendVerificationMessage> data;
	private String msg;
	private Page page;
	private int status;

	public List<FriendVerificationMessage> getData() {
		return data;
	}

	public void setData(List<FriendVerificationMessage> data) {
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