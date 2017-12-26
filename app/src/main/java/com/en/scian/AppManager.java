package com.en.scian;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

/**
 * 
 * @ClassName: AppManager
 * @Description: TODO
 * @author zhaoW
 * 
 */
public class AppManager {
	private static Stack<Activity> activityStack;
	private static AppManager instance;

	private AppManager() {
	}

	/**
	 * 
	 * @Title: getAppManager
	 * @Description: TODO
	 * @param @return
	 * @return AppManager
	 * @throws
	 */
	public static synchronized AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	/**
	 * 
	 * @Title: addActivity
	 * @Description: TODO
	 * @param @param activity
	 * @return void
	 * @throws
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 
	 * @Title: currentActivity
	 * @Description: TODO
	 * @param @return
	 * @return Activity
	 * @throws
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 
	 * @Title: finishActivity
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 
	 * @Title: finishActivity
	 * @Description: TODO
	 * @param @param activity
	 * @return void
	 * @throws
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 
	 * @Title: finishActivity
	 * @Description: TODO
	 * @param @param cls
	 * @return void
	 * @throws
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 
	 * @Title: finishAllActivity
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 
	 * @Title: getCount
	 * @Description: TODO
	 * @param @return
	 * @return Integer
	 * @throws
	 */
	public Integer getCount() {
		int count = 0;
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		count = activityStack.size();
		return count;
	}

	/**
	 * 
	 * @Title: AppExit
	 * @Description: TODO
	 * @param @param context
	 * @return void
	 * @throws
	 */
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {
		}
	}
}