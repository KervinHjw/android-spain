package com.en.scian.util;

public class IsLoadByPwd {

	private static IsLoadByPwd isLoadByPwd;
	
	public static boolean ISLOAD;
	
	private IsLoadByPwd() {
		super();
	}

	public static synchronized IsLoadByPwd getIsLoadByPwd() {
		if (isLoadByPwd == null) {
			isLoadByPwd = new IsLoadByPwd();
		}
		return isLoadByPwd;
	}
	
	public void setisload(boolean b) {
		ISLOAD=b;
	}
	public boolean getisload() {
		return ISLOAD;
	}
}
