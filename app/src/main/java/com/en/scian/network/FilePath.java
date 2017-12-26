package com.en.scian.network;

import android.os.Environment;


/**
 * 文件路径
 * @author zhangp
 *
 */
public class FilePath {
	
	public static final String BASEPATH = Environment.getExternalStorageDirectory()+"";//根目录

	/** 添加设备 */
	public static final String USER_ICON = BASEPATH
			+ "/ludePic/";
	/** 分享 */
	public static final String USER_FENXIANG = BASEPATH
			+ "/DCIM/all.png";
	
	
}
