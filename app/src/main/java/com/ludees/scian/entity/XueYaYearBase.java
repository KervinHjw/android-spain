package com.ludees.scian.entity;

import java.io.Serializable;

/**
 * 血压年份数据
 * @author zhangp
 *
 */
public class XueYaYearBase implements Serializable{
	
	private String xueyaId;
	private String year;//测量年份
	private String time;//记录时间
	private int bloodPressureOpen;// 舒张压
	private int bloodPressureClose;// 收缩压
	private int pulse;// 脉搏
	private int bloodPressureId = 0;//血压数据id
	public String getXueyaId() {
		return xueyaId;
	}
	public void setXueyaId(String xueyaId) {
		this.xueyaId = xueyaId;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
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
	public int getBloodPressureId() {
		return bloodPressureId;
	}
	public void setBloodPressureId(int bloodPressureId) {
		this.bloodPressureId = bloodPressureId;
	}

}
