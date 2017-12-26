package com.en.scian.entity;

public class YanZhengBean {
	
	/*
	 * Kervin
	 * 
	 * 2016-5-19下午7:29:59
	 * 获取验证码
	 */
	
	private YanZhengInfo data;
	public YanZhengInfo getData() {
		return data;
	}

	public void setData(YanZhengInfo data) {
		this.data = data;
	}

	private String msg;
	private int status;

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
