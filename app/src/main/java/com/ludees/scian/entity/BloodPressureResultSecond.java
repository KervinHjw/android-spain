package com.ludees.scian.entity;

import java.util.List;

/**
 * 血压测量结果
 * @author zhangp
 *
 */
public class BloodPressureResultSecond {
	private BloodPressure bloodPressure;
	private List<Integer> differList;
	private List<Integer> pressureCloseList;
	private List<Integer> pressureOpenList;
	private List<String> dateTimeList;
	public BloodPressure getBloodPressure() {
		return bloodPressure;
	}
	public void setBloodPressure(BloodPressure bloodPressure) {
		this.bloodPressure = bloodPressure;
	}
	public List<Integer> getDifferList() {
		return differList;
	}
	public void setDifferList(List<Integer> differList) {
		this.differList = differList;
	}
	public List<Integer> getPressureCloseList() {
		return pressureCloseList;
	}
	public void setPressureCloseList(List<Integer> pressureCloseList) {
		this.pressureCloseList = pressureCloseList;
	}
	public List<Integer> getPressureOpenList() {
		return pressureOpenList;
	}
	public void setPressureOpenList(List<Integer> pressureOpenList) {
		this.pressureOpenList = pressureOpenList;
	}
	public List<String> getDateTimeList() {
		return dateTimeList;
	}
	public void setDateTimeList(List<String> dateTimeList) {
		this.dateTimeList = dateTimeList;
	}

}
