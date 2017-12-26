package com.en.scian.entity;

/**
 * 血压测量结果
 * 
 * @author jiyx
 * 
 */
public class BloodPressure {

	private int bloodPressureId;// 血压测量ID
	private int bloodPressureOpen;// 舒张压
	private int bloodPressureClose;// 收缩压
	private String measureTime;// 测量时间
	private int pulse;// 脉搏
	private int state;// 状态 1：正常 2：不正常
	private String measureResultDesc;// 测量结果说明
	private String worldRessultDesc;//世界评价

	public int getBloodPressureId() {
		return bloodPressureId;
	}

	public void setBloodPressureId(int bloodPressureId) {
		this.bloodPressureId = bloodPressureId;
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

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getMeasureResultDesc() {
		return measureResultDesc;
	}

	public void setMeasureResultDesc(String measureResultDesc) {
		this.measureResultDesc = measureResultDesc;
	}

	public String getWorldRessultDesc() {
		return worldRessultDesc;
	}

	public void setWorldRessultDesc(String worldRessultDesc) {
		this.worldRessultDesc = worldRessultDesc;
	}

}
