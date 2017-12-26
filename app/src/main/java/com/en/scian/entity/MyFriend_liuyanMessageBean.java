package com.en.scian.entity;

import java.util.List;
/**
 *  好友留言消息解析类
 * @author jiyx
 *
 */
public class MyFriend_liuyanMessageBean {

	private List<MyFriend_liuyanMessage> data;
	private String msg;
	private Page page;
	private int status;

	public List<MyFriend_liuyanMessage> getData() {
		return data;
	}

	public void setData(List<MyFriend_liuyanMessage> data) {
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
