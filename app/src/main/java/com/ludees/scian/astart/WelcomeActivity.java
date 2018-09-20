package com.ludees.scian.astart;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import cn.jpush.android.api.JPushInterface;

import com.ludees.scian.BaseActivity;
import com.ludees.scian.ExampleApplication;
import com.ludees.scian.R;
import com.ludees.scian.dao.PushMessageDaoImpl;
import com.ludees.scian.entity.PushMessage;
import com.ludees.scian.entity.ResponseCommon;
import com.ludees.scian.entity.UserCommonBean;
import com.ludees.scian.entity.UserInfo;
import com.ludees.scian.login.LoginActivity;
import com.ludees.scian.main.HomeActivity;
import com.ludees.scian.network.Urls;
import com.ludees.scian.util.SettingUtils;
import com.google.gson.Gson;

/**
 * @author zhaoW 欢迎页
 */
public class WelcomeActivity extends BaseActivity {
	boolean isFirstIn = false;

	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	private static final long SPLASH_DELAY_MILLIS = 3000;

	private static final String SHAREDPREFERENCES_NAME = "first_pref";

	// private static final String TAG = "WelcomeActivity";
	// private AbHttpUtil mAbHttpUtil = null;
	Gson gson = new Gson();
	private FinalHttp fh;
	private ImageView start_bg;
	private Bitmap btp;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				String LoginURL = Urls.PASSWORD;
				AjaxParams params = new AjaxParams();
				String usermail = SettingUtils.get(WelcomeActivity.this, "usermail",
						"");
				String pwd = SettingUtils.get(WelcomeActivity.this, "pwd",
						"");
				if ((!usermail.equals(""))&&(!pwd.equals(""))) {
					params.put("mail", usermail);
					params.put("pwd", pwd);
					LoginUser(LoginURL, params);
				} else {
					goHome();
				}

				break;
			case GO_GUIDE:
				goGuide();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		// 获取Http工具类
		// mAbHttpUtil = AbHttpUtil.getInstance(this);
		// mAbHttpUtil.setTimeout(10000);
		fh = new FinalHttp();
		fh.configTimeout(15 * 1000);

		init();
		initDB();
		// Intent intent = new Intent(WelcomeActivity.this, MyService.class);
		// startService(intent);

	}

	private void init() {
		start_bg = (ImageView) this.findViewById(R.id.start_bg);
//		InputStream is = this.getResources().openRawResource(R.drawable.qidongye);
//		BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = false;
//		options.inSampleSize = 2;
//		btp =BitmapFactory.decodeStream(is,null,options);
//		start_bg.setImageBitmap(btp);
		SharedPreferences preferences = getSharedPreferences(
				SHAREDPREFERENCES_NAME, MODE_PRIVATE);

		isFirstIn = preferences.getBoolean("isFirstIn", true);

		if (!isFirstIn) {
			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
		} else {
			mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		setContentView(R.layout.xml_null);
	}
	
	/**
	 * 初始化数据库
	 */
	private void initDB() {
		PushMessageDaoImpl pushMessageDao = new PushMessageDaoImpl(this);
		List<PushMessage> list = pushMessageDao.find();
		if (list.size() == 0) {
			PushMessage message1 = new PushMessage();
			message1.setPushType(1);// 好友留言
			message1.setIsRead(2);// 已读
			long message1Id = pushMessageDao.insert(message1);
			Log.i("MainActivity", "message1Id==" + message1Id);

			PushMessage message2 = new PushMessage();
			message2.setPushType(2);// 定时提醒
			message2.setIsRead(2);// 已读
			long message2Id = pushMessageDao.insert(message2);
			Log.i("MainActivity", "message2Id==" + message2Id);

			PushMessage message3 = new PushMessage();
			message3.setPushType(3);// 系统消息
			message3.setIsRead(2);// 已读
			long message3Id = pushMessageDao.insert(message3);
			Log.i("MainActivity", "message3Id==" + message3Id);

			PushMessage message4 = new PushMessage();
			message4.setPushType(4);// 好友验证消息
			message4.setIsRead(2);// 已读
			long message4Id = pushMessageDao.insert(message4);
			Log.i("MainActivity", "message4Id==" + message4Id);
		}
	}

	private void goHome() {
		Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
		WelcomeActivity.this.startActivity(intent);
		WelcomeActivity.this.finish();
	}

	private void goGuide() {
		Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
		WelcomeActivity.this.startActivity(intent);
		WelcomeActivity.this.finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		JPushInterface.onPause(this);
	}

	/**
	 * 用户登录
	 * 
	 * @param URL
	 * @param params
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void LoginUser(String URL, AjaxParams params) {
		fh.post(URL, params, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				//prompt(getResources().getString(R.string.ninhaimeiyoudengluo));
				goHome();
				goMain();
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String content = (String) t;
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() == 0) {
					goMain();
					return;
				}
				UserCommonBean bean = gson.fromJson(content,
						UserCommonBean.class);
				UserInfo info = bean.getData();
				// TODO 将用户信息设置到全局变量
				ExampleApplication app = (ExampleApplication) getApplication();
				app.setUser(info);

				int userID = info.getUserId();
				String realName = info.getRealName();
				int userSex = info.getSex();
				int userType2 = info.getUserType();
				String phone = info.getPhone();
				String birthday = info.getBirthday();
				float height = info.getHeight();
				float weight = info.getWeight();
				String userPic = info.getUserPic();
				SettingUtils.set(WelcomeActivity.this, "userPic", userPic);
				SettingUtils.set(WelcomeActivity.this, "userId",
						String.valueOf(userID));
				SettingUtils.set(WelcomeActivity.this, "realName", realName);
				SettingUtils.set(WelcomeActivity.this, "sex",
						String.valueOf(userSex));
				SettingUtils.set(WelcomeActivity.this, "userType",
						String.valueOf(userType2));
				SettingUtils.set(WelcomeActivity.this, "phone", phone);
				SettingUtils.set(WelcomeActivity.this, "birthday", birthday);
				SettingUtils.set(WelcomeActivity.this, "height",
						String.valueOf(height));
				SettingUtils.set(WelcomeActivity.this, "weight",
						String.valueOf(weight));
				SettingUtils.set(WelcomeActivity.this, "loginNum", true);
//				SettingUtils.set(WelcomeActivity.this, "selectedNum", 0);
//				SettingUtils.set(WelcomeActivity.this, "myData", "0");
				SettingUtils.set(WelcomeActivity.this, "isFirstChart", "1");
				SettingUtils.set(WelcomeActivity.this, "isFirstIn", "1");
//				SettingUtils.set(WelcomeActivity.this, "myFriendData", "1");
				SettingUtils.set(WelcomeActivity.this, "isFirstChart", "1");
				SettingUtils
						.set(WelcomeActivity.this, "isFirstTrendChart", "1");
				SettingUtils.set(WelcomeActivity.this, "isFirstFriendChart",
						"1");
				SettingUtils
						.set(WelcomeActivity.this, "isFirstFriendData", "1");
				startActivity(new Intent(WelcomeActivity.this,
						HomeActivity.class));
				finish();

				// TODO 别名与标签 API
				Set<String> set = new HashSet<String>();
				set.add("sysMessage");
				JPushInterface.setAliasAndTags(WelcomeActivity.this,
						info.getUserId() + "", set, null);
			}
		});
		// mAbHttpUtil.post(URL, params, new AbStringHttpResponseListener() {
		//
		// @Override
		// public void onStart() {
		//
		// }
		//
		// @Override
		// public void onFinish() {
		//
		// }
		//
		// @Override
		// public void onFailure(int arg0, String arg1, Throwable arg2) {
		// goHome();
		// }
		//
		// @Override
		// public void onSuccess(int statusCode, String content) {
		// ResponseCommon common = gson.fromJson(content,
		// ResponseCommon.class);
		// if (common.getStatus() != 1) {
		// prompt(common.getMsg());
		// goHome();
		// return;
		// }
		// UserCommonBean bean = gson.fromJson(content,
		// UserCommonBean.class);
		// UserInfo info = bean.getData();
		// // TODO 将用户信息设置到全局变量
		// ExampleApplication app = (ExampleApplication) getApplication();
		// app.setUser(info);
		//
		// int userID = info.getUserId();
		// String realName = info.getRealName();
		// int userSex = info.getSex();
		// int userType2 = info.getUserType();
		// String phone = info.getPhone();
		// String birthday = info.getBirthday();
		// float height = info.getHeight();
		// float weight = info.getWeight();
		// String userPic = info.getUserPic();
		// SettingUtils.set(WelcomeActivity.this, "userPic", userPic);
		// SettingUtils.set(WelcomeActivity.this, "userId",
		// String.valueOf(userID));
		// SettingUtils.set(WelcomeActivity.this, "realName", realName);
		// SettingUtils.set(WelcomeActivity.this, "sex",
		// String.valueOf(userSex));
		// SettingUtils.set(WelcomeActivity.this, "userType",
		// String.valueOf(userType2));
		// SettingUtils.set(WelcomeActivity.this, "phone", phone);
		// SettingUtils.set(WelcomeActivity.this, "birthday", birthday);
		// SettingUtils.set(WelcomeActivity.this, "height",
		// String.valueOf(height));
		// SettingUtils.set(WelcomeActivity.this, "weight",
		// String.valueOf(weight));
		// SettingUtils.set(WelcomeActivity.this, "loginNum", true);
		// SettingUtils.set(WelcomeActivity.this, "selectedNum", 0);
		// SettingUtils.set(WelcomeActivity.this, "myData", "0");
		// SettingUtils.set(WelcomeActivity.this, "isFirstChart", "1");
		// SettingUtils.set(WelcomeActivity.this, "isFirstIn", "1");
		// SettingUtils.set(WelcomeActivity.this, "myFriendData", "1");
		// SettingUtils.set(WelcomeActivity.this, "isFirstChart", "1");
		// SettingUtils
		// .set(WelcomeActivity.this, "isFirstTrendChart", "1");
		// SettingUtils.set(WelcomeActivity.this, "isFirstFriendChart",
		// "1");
		// SettingUtils
		// .set(WelcomeActivity.this, "isFirstFriendData", "1");
		// startActivity(new Intent(WelcomeActivity.this,
		// HomeActivity.class));
		// finish();
		//
		// // TODO 别名与标签 API
		// Set<String> set = new HashSet<String>();
		// set.add("sysMessage");
		// JPushInterface.setAliasAndTags(WelcomeActivity.this,
		// info.getUserId() + "", set, null);
		// }
		// });
	}
	
	private void goMain(){
		SettingUtils.set(WelcomeActivity.this, "loginNum", true);
		SettingUtils.set(WelcomeActivity.this, "selectedNum", 0);
		SettingUtils.set(WelcomeActivity.this, "myData", "0");
		SettingUtils.set(WelcomeActivity.this, "isFirstChart", "1");
		SettingUtils.set(WelcomeActivity.this, "isFirstIn", "1");
		SettingUtils.set(WelcomeActivity.this, "myFriendData", "1");
		SettingUtils.set(WelcomeActivity.this, "isFirstChart", "1");
		SettingUtils
				.set(WelcomeActivity.this, "isFirstTrendChart", "1");
		SettingUtils.set(WelcomeActivity.this, "isFirstFriendChart",
				"1");
		SettingUtils
				.set(WelcomeActivity.this, "isFirstFriendData", "1");
		startActivity(new Intent(WelcomeActivity.this,
				HomeActivity.class));
		finish();
	}
}