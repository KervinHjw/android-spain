package com.ludees.scian.entity;

/**
 * 推送类型
 * 
 * @author jiyx
 * 
 */
public class PushType {

	private String pushType;// 推送类型：1.好友留言 2.定时提醒 3 系统消息 4 好友验证消息

	public String getPushType() {
		return pushType;
	}

	public void setPushType(String pushType) {
		this.pushType = pushType;
	}

}
