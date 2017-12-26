package com.en.scian;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import cn.jpush.android.api.JPushInterface;

import com.en.scian.entity.UserInfo;

public class ExampleApplication extends Application {

	public UserInfo user;
	private List<Activity> activityList = new LinkedList<Activity>();
	private static ExampleApplication instance;

	@Override
	public void onCreate() {
		super.onCreate();
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		instance = this;

		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_TIME_TICK);
		registerReceiver(new TimeChangeReceiver(), filter);

//		startService(new Intent(this, DaemonService.class));
	}

	/**
	 * 单例模式中获取唯一的ExampleApplication实例 (由于application在项目中其是本身已经是单例了)
	 * 
	 * @return
	 */
	public static ExampleApplication getInstance() {
		return instance;
	}

	/**
	 * TODO 添加Activity到容器中
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	/**
	 * TODO 遍历所有Activity并finish
	 */
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		// System.exit(0);
	}

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}
}
