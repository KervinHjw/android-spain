package com.ludees.scian.entity;

import java.util.List;

/**
 * 历史血压详情
 * @author zhangp
 *
 */
public class HistoryBlooePressureSecond {
	private List<BloodPressure> bloodPressureList;
	private String yearTime;
	public List<BloodPressure> getBloodPressureList() {
		return bloodPressureList;
	}
	public void setBloodPressureList(List<BloodPressure> bloodPressureList) {
		this.bloodPressureList = bloodPressureList;
	}
	public String getYearTime() {
		return yearTime;
	}
	public void setYearTime(String yearTime) {
		this.yearTime = yearTime;
	}
}
