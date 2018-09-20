package com.ludees.scian.entity;

/**
 * 我的好友留言
 * 
 * @author jiyx
 * 
 */
public class MyFriend_Message {
	private int friendsMessageId;// 留言消息ID
	private String friendsRealName;// 姓名
	private String messageContent;// 留言内容
	private String messageTime;// 留言时间

	public int getFriendsMessageId() {
		return friendsMessageId;
	}

	public void setFriendsMessageId(int friendsMessageId) {
		this.friendsMessageId = friendsMessageId;
	}

	public String getFriendsRealName() {
		return friendsRealName;
	}

	public void setFriendsRealName(String friendsRealName) {
		this.friendsRealName = friendsRealName;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public String getMessageTime() {
		return messageTime;
	}

	public void setMessageTime(String messageTime) {
		this.messageTime = messageTime;
	}

}
