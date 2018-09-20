package com.ludees.scian.entity;

import java.io.Serializable;

/**
 * 血压测量结果初次过滤
 * @author zhangp
 *
 */
public class BloodPressureResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5579580527668279481L;
	private BloodPressureResultSecond data;
	private int status;
	private String msg;
	public BloodPressureResultSecond getData() {
		return data;
	}
	public void setData(BloodPressureResultSecond data) {
		this.data = data;
	}
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
}
