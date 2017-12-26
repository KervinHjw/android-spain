package com.en.scian.dao;

import android.content.Context;

import com.en.scian.entity.PushMessage;
import com.tgb.lk.ahibernate.dao.impl.BaseDaoImpl;

public class PushMessageDaoImpl extends BaseDaoImpl<PushMessage> {

	public PushMessageDaoImpl(Context context) {
		super(new DBHelper(context));
	}

}
