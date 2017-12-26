package com.en.scian.entity;

import java.io.Serializable;

/**
 * 血压基本历史数据
 * @author zhaok
 *
 */
public class XueYaTimeBase implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6295231837990839636L;
	private String time;//记录时间
	private int bloodPressureOpen;// 舒张压
	private int bloodPressureClose;// 收缩压
	private int pulse;// 脉搏
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getBloodPressureOpen() {
		return bloodPressureOpen;
	}
	public void setBloodPressureOpen(int bloodPressureOpen) {
		this.bloodPressureOpen = bloodPressureOpen;
	}
	public int getBloodPressureClose() {
		return bloodPressureClose;
	}
	public void setBloodPressureClose(int bloodPressureClose) {
		this.bloodPressureClose = bloodPressureClose;
	}
	public int getPulse() {
		return pulse;
	}
	public void setPulse(int pulse) {
		this.pulse = pulse;
	}

}
