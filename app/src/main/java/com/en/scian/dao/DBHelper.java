package com.en.scian.dao;

import android.content.Context;

import com.en.scian.entity.PushMessage;
import com.tgb.lk.ahibernate.util.MyDBHelper;

public class DBHelper extends MyDBHelper {
	private static final String DBNAME = "pushmessage.db";// 数据库名
	private static final int DBVERSION = 1;
	private static final Class<?>[] clazz = { PushMessage.class };// 要初始化的表

	public DBHelper(Context context) {
		super(context, DBNAME, null, DBVERSION, clazz);
	}
}
