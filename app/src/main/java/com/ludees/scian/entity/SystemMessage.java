package com.ludees.scian.entity;

/**
 * 系统消息
 * 
 * @author jiyx
 * 
 */
public class SystemMessage {

	private int messageId;// 系统消息ID
	private String messageName;// 消息名称
	private int isRead;// 是否读（1:已读 2：未读）
	private String createTime;// 创建时间

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getMessageName() {
		return messageName;
	}

	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
