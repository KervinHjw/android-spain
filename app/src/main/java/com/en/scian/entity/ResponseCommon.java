package com.en.scian.entity;

import java.io.Serializable;

public class ResponseCommon implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int status;
	private String msg;
	private Object data;

	public ResponseCommon() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResponseCommon(String msg) {
		super();
		this.msg = msg;
	}

	public ResponseCommon(int status, String msg) {
		super();
		this.status = status;
		this.msg = msg;
	}

	public ResponseCommon(String msg, Object data) {
		super();
		this.msg = msg;
		this.data = data;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
