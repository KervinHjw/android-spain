package com.en.scian.entity;

/**
 * 好友基本解析类
 * 
 * @author jiyx
 * 
 */
public class FriendInfoBean {

	private FriendInfo data;
	private String msg;
	private int status;

	public FriendInfo getData() {
		return data;
	}
	public void setData(FriendInfo data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
