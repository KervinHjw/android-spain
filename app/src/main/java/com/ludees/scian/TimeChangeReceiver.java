package com.ludees.scian;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import cn.jpush.android.api.JPushInterface;

public class TimeChangeReceiver extends BroadcastReceiver {
	boolean isServiceRunning = false;

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_TIME_TICK) || action.equals("com.bbb")) {
			boolean pushStopped = JPushInterface.isPushStopped(context);
			if (pushStopped) {
				JPushInterface.resumePush(context);
				// 如果app进程已经被杀死，先重新启动app
				Intent launchIntent = context.getPackageManager()
						.getLaunchIntentForPackage(context.getPackageName());
				launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				context.startActivity(launchIntent);
			}
		}
	}

}
