package com.ludees.scian.entity;

/**
 * 提醒设置
 * 
 * @author jiyx
 * 
 */
public class RemindInfo {

	private int id;// 提醒设置ID
	private String remindHour;// 提醒小时
	private String remindMinute;// 提醒分钟
	private int remindType;// 提醒类型(1:吃药提醒 2：测量提醒)
	private int state;// 状态(1：提醒 2：不提醒)
	private int userId;// 用户ID
	private String createTime;// 创建时间

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRemindHour() {
		return remindHour;
	}

	public void setRemindHour(String remindHour) {
		this.remindHour = remindHour;
	}

	public String getRemindMinute() {
		return remindMinute;
	}

	public void setRemindMinute(String remindMinute) {
		this.remindMinute = remindMinute;
	}

	public int getRemindType() {
		return remindType;
	}

	public void setRemindType(int remindType) {
		this.remindType = remindType;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
