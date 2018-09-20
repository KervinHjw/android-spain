package com.ludees.scian.entity;

import java.io.Serializable;

/**
 * 图片返回路径
 * @author zhangp
 *
 */
public class PictureBean implements Serializable{

	private int status;
	//图片的网络路径
	private String picture;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
}
