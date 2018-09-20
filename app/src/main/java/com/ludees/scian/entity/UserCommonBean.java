package com.ludees.scian.entity;

import java.io.Serializable;

/**
 * 用户信息基本解析类
 * @author zhangp
 *
 */
public class UserCommonBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7830947193171218322L;
	private int status;
	private String msg;
	private UserInfo data;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public UserInfo getData() {
		return data;
	}
	public void setData(UserInfo data) {
		this.data = data;
	}
}
