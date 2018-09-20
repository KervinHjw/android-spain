package com.ludees.scian.entity;

import java.util.List;

/**
 * 血压首页数据
 * 
 * @author jiyx
 * 
 */
public class XueyaShouye {

	private int bloodPressureClose;// 收缩压
	private int bloodPressureCloseAvg;// 收缩压平均值
	private List<Integer> bloodPressureCloseList;// 近三天收缩压集合
	private int bloodPressureCloseMax;// 收缩压最高值
	private int bloodPressureOpen;// 舒张压
	private int bloodPressureOpenAvg;// 舒张压平均值
	private List<Integer> bloodPressureOpenList;// 近三天舒张压集合
	private int bloodPressureOpenMax;// 舒张压最高值
	private int healthNumber;// 健康指数
	private String measureTime;// 测量时间
	private int pulse;// 心率

	public int getBloodPressureClose() {
		return bloodPressureClose;
	}

	public void setBloodPressureClose(int bloodPressureClose) {
		this.bloodPressureClose = bloodPressureClose;
	}

	public int getBloodPressureCloseAvg() {
		return bloodPressureCloseAvg;
	}

	public void setBloodPressureCloseAvg(int bloodPressureCloseAvg) {
		this.bloodPressureCloseAvg = bloodPressureCloseAvg;
	}

	public List<Integer> getBloodPressureCloseList() {
		return bloodPressureCloseList;
	}

	public void setBloodPressureCloseList(List<Integer> bloodPressureCloseList) {
		this.bloodPressureCloseList = bloodPressureCloseList;
	}

	public int getBloodPressureCloseMax() {
		return bloodPressureCloseMax;
	}

	public void setBloodPressureCloseMax(int bloodPressureCloseMax) {
		this.bloodPressureCloseMax = bloodPressureCloseMax;
	}

	public int getBloodPressureOpen() {
		return bloodPressureOpen;
	}

	public void setBloodPressureOpen(int bloodPressureOpen) {
		this.bloodPressureOpen = bloodPressureOpen;
	}

	public int getBloodPressureOpenAvg() {
		return bloodPressureOpenAvg;
	}

	public void setBloodPressureOpenAvg(int bloodPressureOpenAvg) {
		this.bloodPressureOpenAvg = bloodPressureOpenAvg;
	}

	public List<Integer> getBloodPressureOpenList() {
		return bloodPressureOpenList;
	}

	public void setBloodPressureOpenList(List<Integer> bloodPressureOpenList) {
		this.bloodPressureOpenList = bloodPressureOpenList;
	}

	public int getBloodPressureOpenMax() {
		return bloodPressureOpenMax;
	}

	public void setBloodPressureOpenMax(int bloodPressureOpenMax) {
		this.bloodPressureOpenMax = bloodPressureOpenMax;
	}

	public int getHealthNumber() {
		return healthNumber;
	}

	public void setHealthNumber(int healthNumber) {
		this.healthNumber = healthNumber;
	}

	public String getMeasureTime() {
		return measureTime;
	}

	public void setMeasureTime(String measureTime) {
		this.measureTime = measureTime;
	}

	public int getPulse() {
		return pulse;
	}

	public void setPulse(int pulse) {
		this.pulse = pulse;
	}

}
