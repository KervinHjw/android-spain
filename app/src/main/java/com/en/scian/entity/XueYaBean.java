package com.en.scian.entity;

/**
 * 血压一次解析
 * @author zhangp
 *
 */
public class XueYaBean {
	private int status;
	private String msg;
	private XueYaSecondBean data;
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
	public XueYaSecondBean getData() {
		return data;
	}
	public void setData(XueYaSecondBean data) {
		this.data = data;
	}
}
