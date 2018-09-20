package com.ludees.scian.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 提醒设置封装
 * @author zhangp
 *
 */
public class RemindInfoBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 915799539915825664L;
	private int status;
	private String msg;
	private List<RemindInfo> data;
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
	public List<RemindInfo> getData() {
		return data;
	}
	public void setData(List<RemindInfo> data) {
		this.data = data;
	}

}
