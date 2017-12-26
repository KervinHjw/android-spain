package com.en.scian;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.en.scian.dao.PushMessageDaoImpl;
import com.en.scian.entity.PushMessage;
import com.en.scian.entity.PushType;
import com.en.scian.entity.UserInfo;
import com.en.scian.util.SystemUtils;
import com.google.gson.Gson;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	Gson gson = new Gson();

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		// 接收到推送下来的通知
		if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Intent intent1 = new Intent();
			intent1.setAction("com.aaa");
			// context.sendBroadcast(intent1);

			// 更新小红点
			updateRedPoint(context, bundle, intent1);
//			ExampleApplication app = (ExampleApplication) context
//					.getApplicationContext();
//			UserInfo user = app.getUser();
//			if (SystemUtils.isAppAlive(context, context.getPackageName())
//					&& user != null) {
//				// 更新小红点
//				updateRedPoint(context, bundle, intent1);
//			} else {
//				Intent launchIntent = context.getPackageManager()
//						.getLaunchIntentForPackage(context.getPackageName());
//				launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//				context.startActivity(launchIntent);
//				updateRedPoint(context, bundle, intent1); // 更新小红点
//			}

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
			ExampleApplication app = (ExampleApplication) context
					.getApplicationContext();
			UserInfo user = app.getUser();
			if (SystemUtils.isAppAlive(context, context.getPackageName())
					&& user != null) {
				// 如果存活的话，就直接启动DetailActivity，但要考虑一种情况，就是app的进程虽然仍然在
				// 但Task栈已经空了，比如用户点击Back键退出应用，但进程还没有被系统回收，如果直接启动
				// DetailActivity,再按Back键就不会返回MainActivity了。所以在启动
				// DetailActivity前，要先启动MainActivity。
				Log.i("MyReceiver", "the app process is alive");
				// Intent mainIntent = new Intent(context, MainActivity.class);
				// 将MainAtivity的launchMode设置成SingleTask,
				// 或者在下面flag中加上Intent.FLAG_CLEAR_TOP,
				// 如果Task栈中有MainActivity的实例，就会把它移到栈顶，把在它之上的Activity都清理出栈，
				// 如果Task栈不存在MainActivity实例，则在栈顶创建
				// mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// Intent detailIntent = new Intent(context,
				// DetailActivity.class);
				// detailIntent.putExtras(bundle);
				// detailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				// | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// context.startActivity(mainIntent);
				// Intent[] intents = { mainIntent, detailIntent };
				// context.startActivities(intents);
			} else {
				// 如果app进程已经被杀死，先重新启动app，将DetailActivity的启动参数传入Intent中，参数经过
				// SplashActivity传入MainActivity，此时app的初始化已经完成，在MainActivity中就可以根据传入
				// //参数跳转到DetailActivity中去了
				Log.i("myReceiver", "the app process is dead");
				Intent launchIntent = context.getPackageManager()
						.getLaunchIntentForPackage(context.getPackageName());
				launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				// Bundle bundle = intent.getExtras();
				// launchIntent.putExtra(Constants.EXTRA_BUNDLE, bundle);
				context.startActivity(launchIntent);

			}
		} else if ("android.intent.action.BOOT_COMPLETED".equals(intent
				.getAction())//
				|| "android.net.conn.CONNECTIVITY_CHANGE".equals(intent
						.getAction())//
				|| "android.intent.action.TIME_TICK".equals(intent.getAction())//
		) {
			JPushInterface.resumePush(context);
		}

	}

	/**
	 * 更新小红点
	 * 
	 * @param context
	 * @param bundle
	 * @param intent1
	 */
	private void updateRedPoint(Context context, Bundle bundle, Intent intent1) {
		String pushTypeStr = bundle.getString(JPushInterface.EXTRA_EXTRA);
		PushType pushType = gson.fromJson(pushTypeStr, PushType.class);
		if (pushType != null) {
			String pt = pushType.getPushType();
			if (!"2".equals(pt)) {
				PushMessageDaoImpl dao = new PushMessageDaoImpl(context);
				List<PushMessage> list = dao.find(new String[] {
						"messageTypeId", "pushType", "isRead" }, "pushType=?",
						new String[] { pt }, null, null, null, null);
				if (list.size() > 0) {
					PushMessage pushMessage = list.get(0);
					pushMessage.setPushType(Integer.parseInt(pt));
					pushMessage.setIsRead(1);
					dao.update(pushMessage);
					// 如果应用存活着，发送广播
					if (SystemUtils.isAppAlive(context,
							context.getPackageName())) {
						context.sendBroadcast(intent1);
					}
				}
			}
		}
	}

}
