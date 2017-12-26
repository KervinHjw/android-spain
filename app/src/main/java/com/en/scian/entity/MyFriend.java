package com.en.scian.entity;

import java.io.Serializable;

/**
 * 我的好友基本信息
 * 
 * @author jiyx
 * 
 */
public class MyFriend implements Serializable {
	private static final long serialVersionUID = 1L;
	private int bloodPressureClose;// 收缩压
	private int bloodPressureOpen;// 舒张压
	private String measureTime;// 测量时间
	private int bloodPressureDiffer;// 脉压差
	private int friendId;// 好友ID
	private String realName;// 名称
	private String userPic;// 用户图像
	private String phone;// 电话号码
	private int pulse;// 脉搏
	private int healthNumber;

	public int getBloodPressureClose() {
		return bloodPressureClose;
	}

	public void setBloodPressureClose(int bloodPressureClose) {
		this.bloodPressureClose = bloodPressureClose;
	}

	public int getBloodPressureOpen() {
		return bloodPressureOpen;
	}

	public void setBloodPressureOpen(int bloodPressureOpen) {
		this.bloodPressureOpen = bloodPressureOpen;
	}

	public String getMeasureTime() {
		return measureTime;
	}

	public void setMeasureTime(String measureTime) {
		this.measureTime = measureTime;
	}

	public int getBloodPressureDiffer() {
		return bloodPressureDiffer;
	}

	public void setBloodPressureDiffer(int bloodPressureDiffer) {
		this.bloodPressureDiffer = bloodPressureDiffer;
	}

	public int getFriendId() {
		return friendId;
	}

	public void setFriendId(int friendId) {
		this.friendId = friendId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getUserPic() {
		return userPic;
	}

	public void setUserPic(String userPic) {
		this.userPic = userPic;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getPulse() {
		return pulse;
	}

	public void setPulse(int pulse) {
		this.pulse = pulse;
	}

	public int getHealthNumber() {
		return healthNumber;
	}

	public void setHealthNumber(int healthNumber) {
		this.healthNumber = healthNumber;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
