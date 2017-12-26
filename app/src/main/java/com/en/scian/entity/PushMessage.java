package com.en.scian.entity;

import com.tgb.lk.ahibernate.annotation.Column;
import com.tgb.lk.ahibernate.annotation.Id;
import com.tgb.lk.ahibernate.annotation.Table;

@Table(name = "push_message")
public class PushMessage {
	@Id
	@Column(name = "messageTypeId")
	private int messageTypeId;
	@Column(name = "pushType")
	private int pushType;// 推送类型：1.好友留言 2.定时提醒 3 系统消息 4 好友验证消息
	@Column(name = "isRead")
	private int isRead;// 1.未读；2.已读

	public int getMessageTypeId() {
		return messageTypeId;
	}

	public void setMessageTypeId(int messageTypeId) {
		this.messageTypeId = messageTypeId;
	}

	public int getPushType() {
		return pushType;
	}

	public void setPushType(int pushType) {
		this.pushType = pushType;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	@Override
	public String toString() {
		return "PushMessage [messageTypeId=" + messageTypeId + ", pushType="
				+ pushType + ", isRead=" + isRead + "]";
	}

}
