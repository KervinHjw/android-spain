package com.en.scian.entity;

/**
 * 好友验证消息类
 * 
 * @author jiyx
 * 
 */
public class FriendVerificationMessage {

	private String createTime;// 创建时间
	private int messageId;// 消息ID
	private String realName;// 名称
	private int friendsId;// 好友ID

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public int getFriendsId() {
		return friendsId;
	}

	public void setFriendsId(int friendsId) {
		this.friendsId = friendsId;
	}
}
