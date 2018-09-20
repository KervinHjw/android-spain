package com.ludees.scian.entity;

import java.util.List;

/**
 * 历史血压初步
 * @author zhaok
 *
 */
public class HistoryBloodPressure {
	private String msg;
	private int status;
	private List<HistoryBlooePressureSecond> data;
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
	public List<HistoryBlooePressureSecond> getData() {
		return data;
	}
	public void setData(List<HistoryBlooePressureSecond> data) {
		this.data = data;
	}
}
