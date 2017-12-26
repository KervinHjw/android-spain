package com.en.scian.entity;

/**
 * 好友留言消息
 * 
 * @author jiyx
 * 
 */
public class MyFriend_liuyanMessage {
	private int friendsMessageId;// 好友留言Id
	private String friendsRealName;// 好友名称
	private String messageContent;// 留言内容
	private String messageTime;// 留言时间
	private int userId;// 留言用户ID

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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
