package com.en.scian.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 血压二次解析
 * @author zhangp
 *
 */
public class XueYaSecondBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7893423669149419813L;
	private String startTime;
	private String endTime;
	private BloodPressure bloodPressure;
	private List<Integer> differList;
	private List<Integer> pressureCloseList;
	private List<Integer> pressureOpenList;
	private List<String> dateTimeList;
	private List<BloodPressure> wanshangList;
	private List<BloodPressure> zhongwuList;
	private List<BloodPressure> quantianList;
	private List<BloodPressure> zaoshangList;
	private List<BloodPressure> xueyaList;
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public BloodPressure getBloodPressure() {
		return bloodPressure;
	}
	public void setBloodPressure(BloodPressure bloodPressure) {
		this.bloodPressure = bloodPressure;
	}
	public List<BloodPressure> getWanshangList() {
		return wanshangList;
	}
	public void setWanshangList(List<BloodPressure> wanshangList) {
		this.wanshangList = wanshangList;
	}
	public List<BloodPressure> getZhongwuList() {
		return zhongwuList;
	}
	public void setZhongwuList(List<BloodPressure> zhongwuList) {
		this.zhongwuList = zhongwuList;
	}
	public List<BloodPressure> getQuantianList() {
		return quantianList;
	}
	public void setQuantianList(List<BloodPressure> quantianList) {
		this.quantianList = quantianList;
	}
	public List<BloodPressure> getZaoshangList() {
		return zaoshangList;
	}
	public void setZaoshangList(List<BloodPressure> zaoshangList) {
		this.zaoshangList = zaoshangList;
	}
	public List<BloodPressure> getXueyaList() {
		return xueyaList;
	}
	public void setXueyaList(List<BloodPressure> xueyaList) {
		this.xueyaList = xueyaList;
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
