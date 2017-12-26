package com.en.scian.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;

import com.ab.util.AbStrUtil;
import com.ab.view.ioc.AbIocView;
import com.en.scian.BaseActivity;
import com.en.scian.ExampleApplication;
import com.en.scian.R;
import com.en.scian.entity.DengLuInfoBean;
import com.en.scian.entity.ResponseCommon;
import com.en.scian.entity.UserCommonBean;
import com.en.scian.entity.UserInfo;
import com.en.scian.main.HomeActivity;
import com.en.scian.network.Urls;
import com.en.scian.util.Code;
import com.en.scian.util.SettingUtils;
import com.en.scian.view.CustomDialog;
import com.en.scian.view.CustomDialog.Builder;
import com.google.gson.Gson;

/**
 * @author zhaoW
 * 
 */
public class LoginActivity extends BaseActivity implements Callback,
		PlatformActionListener,OnClickListener{

	/**
	 * tab_title
	 */
	@AbIocView(id = R.id.search_titleText)
	private TextView titleText;
	/**
	 * tab_left
	 */
	@AbIocView(id = R.id.search_leftLayout)
	private LinearLayout leftLayout;
	@AbIocView(id = R.id.search_leftImg)
	private ImageView search_leftImg;
	/**
	 * tab_right
	 */
	@AbIocView(id = R.id.search_rightLayout, click = "btnClick")
	private LinearLayout rightLayout;

	/**
	 * tv_right
	 */
	@AbIocView(id = R.id.search_right_txtView)
	private TextView tv_right;
	/**
	 * 手机号
	 */
	@AbIocView(id = R.id.edt_phone)
	private EditText edt_phone;
	/**
	 * 验证码
	 */
	@AbIocView(id = R.id.edt_y_z_m)
	private EditText edt_y_z_m;
	/**
	 * 验证码获取按钮
	 */
	@AbIocView(id = R.id.get_y_z_m, click = "btnClick")
	private TextView get_y_z_m;
	/**
	 * 下一步
	 */
	@AbIocView(id = R.id.next, click = "btnClick")
	private Button next;
	
	//本地验证码
	@AbIocView(id = R.id.et_y_z_m)
	private EditText bd_y_z_m;

	//验证码图片
	@AbIocView(id = R.id.iv_showCode)
	private ImageView iv_showCode;
	
	//忘记密码
	@AbIocView(id = R.id.forget_passsword)
	private TextView forget_passsword;
	
	int usertype;

	Gson gson = new Gson();
	private FinalHttp fh;

	String sendTime;
	String verificationCode;
	private boolean isphone = false;
	/**
	 * 验证码是否正确
	 */
	private boolean isy_z_m = false;
	private boolean isBdy_z_m = false;
	@AbIocView(id = R.id.tv_phone)
	private TextView tv_phone;
	@AbIocView(id = R.id.tv_y_z_m)
	private TextView tv_y_z_m;
	private String userName;
	
	private ProgressDialog dialog;
	
	//邮箱正则校验
	//private String regex = "[a-zA-_0-9]+@[a-zA-Z_0-9]{2,8}(\\.[a-zA-z_0-9]{2,3})+";
	private String regex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
	private String miMa = "^[0-9a-zA-z]{6,12}$";//6~12位密码校验
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// 成功
				Toast.makeText(LoginActivity.this, LoginActivity.this.getResources().getString(R.string.chenggong), Toast.LENGTH_SHORT)
						.show();
				Platform platform = (Platform) msg.obj;
				userName = platform.getDb().getUserId();
				AjaxParams params = new AjaxParams();
				params.put("userName", platform.getDb().getUserId());
				thirdLogin(Urls.THIRD_LOGIN, params);
				break;
			case 2:
				// 失败
				Toast.makeText(LoginActivity.this, LoginActivity.this.getResources().getString(R.string.shibai), Toast.LENGTH_SHORT)
						.show();
				String expName = msg.obj.getClass().getSimpleName();
				if ("WechatClientNotExistException".equals(expName)
						|| "WechatTimelineNotSupportedException"
								.equals(expName)
						|| "WechatFavoriteNotSupportedException"
								.equals(expName)) {
					Toast.makeText(LoginActivity.this, LoginActivity.this.getResources().getString(R.string.qinganzhuangweixinkehuduan),
							Toast.LENGTH_SHORT).show();
				}
				break;
			case 3:
				// 取消
				Toast.makeText(LoginActivity.this, LoginActivity.this.getResources().getString(R.string.dialog_select_pic_cancel)+"····", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		};
	};
	private String code;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ShareSDK.initSDK(this);
		next.setClickable(false);
		get_y_z_m.setClickable(false);
		leftLayout.setVisibility(View.GONE);
		search_leftImg.setVisibility(View.GONE);
		initview();
		initData();
		fh = new FinalHttp();
		fh.addHeader("lord-app-language", "");
		fh.configTimeout(15 * 1000);
	}

	private void initview() {
		forget_passsword.setOnClickListener(this);
		iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
		iv_showCode.setOnClickListener(this);
		code = Code.getInstance().getCode();
		titleText.setText(this.getResources().getString(R.string.denglu));
		leftLayout.setVisibility(View.VISIBLE);
		tv_right.setVisibility(View.VISIBLE);
		tv_right.setText(R.string.mianfeizhuce);
		tv_right.setOnClickListener(this);
		// 电话输入监听
		edt_phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String phone = edt_phone.getText().toString();

				if (phone.matches(regex)) {
					isphone = true;
				} else {
					isphone = false;
				}
				if (isphone == true && isy_z_m == true && isBdy_z_m == true) {
					next.setBackgroundResource(R.drawable.button_bg);
					next.setClickable(true);
				} else {
					next.setBackgroundResource(R.drawable.button_bg_false);
					next.setClickable(false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		// 验证码输入监听
		edt_y_z_m.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String y_z_m = edt_y_z_m.getText().toString();

				if (y_z_m.matches(miMa)) {
					isy_z_m = true;
				} else {
					isy_z_m = false;
				}
				if (isphone == true && isy_z_m == true && isBdy_z_m == true) {
					next.setBackgroundResource(R.drawable.button_bg);
					next.setClickable(true);
				} else {
					next.setBackgroundResource(R.drawable.button_bg_false);
					next.setClickable(false);
				}
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		
		//本地图片验证码校验
		bd_y_z_m.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
				String str= bd_y_z_m.getText().toString();
				if(code.equalsIgnoreCase(str)){
					isBdy_z_m = true;
				}else{
					isBdy_z_m = false;
				}
				if (isphone == true && isy_z_m == true && isBdy_z_m == true) {
					next.setBackgroundResource(R.drawable.button_bg);
					next.setClickable(true);
				} else {
					next.setBackgroundResource(R.drawable.button_bg_false);
					next.setClickable(false);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	private void initData() {
		usertype = getIntent().getIntExtra("usertype", -1);
	}

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.search_leftLayout:
			finish();
			break;
		case R.id.get_y_z_m:
			if (!AbStrUtil.isMobileNo(edt_phone.getText().toString())) {
				prompt(this.getResources().getString(R.string.qingshuruchunshuzi));
				edt_phone.setText("");
				return;
			}
			String URL = Urls.SEND_MESSAGE + "phone="
					+ edt_phone.getText().toString() + "&type=1";
			getYanZhengMa(URL);

			break;
		case R.id.next:
			
			dialog = new ProgressDialog(LoginActivity.this);
			dialog.setCancelable(false);
			dialog.setMessage(LoginActivity.this.getResources().getString(R.string.zhengzailianjieqingshaohou));
			dialog.show();
			String LoginURL = Urls.PASSWORD;
			AjaxParams params = new AjaxParams();
			params.put("mail", edt_phone.getText().toString());
			params.put("pwd", edt_y_z_m.getText().toString());
			LoginUser(LoginURL, params);
			
			break;
		default:
			break;
		}
	}

	/**
	 * @author zhaow 倒计时的计时器
	 */
	class Timecount extends CountDownTimer {

		public Timecount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@SuppressLint("NewApi")
		@Override
		public void onTick(long millisUntilFinished) {
			get_y_z_m.setText(millisUntilFinished / 1000 + getResources().getString(R.string.miaohouchongshi));
			get_y_z_m.setBackgroundResource(R.color.gray);
			get_y_z_m.setEnabled(false);
			get_y_z_m.setClickable(false);
		}

		@Override
		public void onFinish() {
			get_y_z_m.setText(getResources().getString(R.string.huoquyanzhengma));
			get_y_z_m.setBackgroundResource(R.drawable.get_yan_zheng_ma);
			get_y_z_m.setEnabled(true);
			get_y_z_m.setClickable(true);
		}
	}

	/**
	 * 用户登录
	 * 
	 * @param URL
	 * @param params
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void LoginUser(String URL, AjaxParams params) {
		fh.post(URL, params, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				prompt(LoginActivity.this.getResources().getString(R.string.wangluolianjieyichang));
				dialog.dismiss();
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String content = (String) t;
				System.out.println(content);
				ResponseCommon bean = gson.fromJson(content, ResponseCommon.class);
				int status = bean.getStatus();
				if(status == 0){
					//无此用户，注册
					/*Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
					startActivity(intent);*/
					dialog.dismiss();
					prompt(LoginActivity.this.getResources().getString(R.string.qingzhuce));
				}else{
				UserCommonBean count = gson.fromJson(content,
						UserCommonBean.class);
				UserInfo info = count.getData();
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
				String usermail = info.getUsermail();
				String userPic = info.getUserPic();
				SettingUtils.set(LoginActivity.this, "userPic", userPic);
				SettingUtils.set(LoginActivity.this, "userId",
						String.valueOf(userID));
				SettingUtils.set(LoginActivity.this, "realName", realName);
				SettingUtils.set(LoginActivity.this, "sex",
						String.valueOf(userSex));
				SettingUtils.set(LoginActivity.this, "userType",
						String.valueOf(userType2));
				SettingUtils.set(LoginActivity.this, "phone", phone);
				SettingUtils.set(LoginActivity.this, "birthday", birthday);
				SettingUtils.set(LoginActivity.this, "height",
						String.valueOf(height));
				SettingUtils.set(LoginActivity.this, "weight",
						String.valueOf(weight));
				SettingUtils.set(LoginActivity.this, "loginNum", true);
				SettingUtils.set(LoginActivity.this, "isPush", info.getIsPush());
				SettingUtils.set(LoginActivity.this, "selectedNum", 0);
				SettingUtils.set(LoginActivity.this, "myData", "0");
				SettingUtils.set(LoginActivity.this, "isFirstIn", "1");
				SettingUtils.set(LoginActivity.this, "myFriendData", "1");
				SettingUtils.set(LoginActivity.this, "isFirstChart", "1");
				SettingUtils.set(LoginActivity.this, "isFirstTrendChart", "1");
				SettingUtils.set(LoginActivity.this, "isFirstFriendChart", "1");
				SettingUtils.set(LoginActivity.this, "isFirstFriendData", "1");
				SettingUtils.set(LoginActivity.this, "usermail", usermail);
				SettingUtils.set(LoginActivity.this, "pwd", edt_y_z_m.getText().toString());
				startActivity(new Intent(LoginActivity.this, HomeActivity.class));
				// TODO 别名与标签 API
				Set<String> set = new HashSet<String>();
				set.add("sysMessage");
				JPushInterface.setAliasAndTags(LoginActivity.this,
						info.getUserId() + "", set, null);
				dialog.dismiss();
				LoginActivity.this.finish();
				}
			}
		});
	}

	/**
	 * 获取验证码
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void getYanZhengMa(String URL) {
		fh.get(URL, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				prompt(LoginActivity.this.getResources().getString(R.string.fuwuqiwuxiangying));
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String content = (String) t;
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					prompt(common.getMsg());
					return;
				}
				// 点击倒数60秒
				new Timecount(60000, 1000).start();

				UserCommonBean bean = gson.fromJson(content,
						UserCommonBean.class);
				UserInfo info = (UserInfo) bean.getData();
				// verficationCode = Integer.valueOf(info.getVerifyCode());
				verificationCode = info.getVerificationCode();
				sendTime = info.getSendTime();
			}
		});
	}

	/**
	 * 判断验证码是否过期
	 */
	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	private void checkYanZhengMa(String URL, AjaxParams params) {
		fh.post(URL, params, new AjaxCallBack() {
			private ProgressDialog pd;

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				prompt(LoginActivity.this.getResources().getString(R.string.fuwuqiwuxiangying));
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String content = (String) t;
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					prompt(LoginActivity.this.getResources().getString(R.string.yanzhengmayiguoqi));
					return;
				}
				String LoginURL = Urls.LOGIN;
				AjaxParams params = new AjaxParams();
				params.put("phone", edt_phone.getText().toString());
				LoginUser(LoginURL, params);
			}
		});
	}

	@Override
	public void onCancel(Platform platform, int action) {
		Message msg = new Message();
		msg.what = 3;
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = platform;
		handler.sendMessage(msg);
	}

	@Override
	public void onComplete(Platform platform, int action,
			HashMap<String, Object> res) {
		// 获取资料
		platform.getDb().getUserName();// 获取用户名字
		platform.getDb().getUserIcon(); // 获取用户头像
		// 获取资料
		platform.getDb().getUserName();// 获取用户名字
		platform.getDb().getUserIcon(); // 获取用户头像
		platform.getDb().getUserId();
		Message msg = new Message();
		msg.what = 1;
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = platform;
		handler.sendMessage(msg);
	}

	@Override
	public void onError(Platform platform, int action, Throwable t) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = 2;
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = t;
		handler.sendMessage(msg);

		// 分享失败的统计
		ShareSDK.logDemoEvent(4, platform);
		Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.arg1) {
		case 1: {

		}
			break;
		case 2: {

		}
			break;
		case 3: {

		}
			break;
		}
		return false;
	}

	/**
	 * 第三方登录
	 * 
	 * @param URL
	 * @param params
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void thirdLogin(String URL, AjaxParams params) {
		fh.post(URL, params, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				prompt(LoginActivity.this.getResources().getString(R.string.fuwuqiwuxiangying));
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String content = (String) t;
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() == 0) {
					prompt(common.getMsg());
					CustomDialog.Builder builder = new Builder(
							LoginActivity.this);
					builder.setTitle(R.string.prompt);
					builder.setMessage(common.getMsg());
					// 确定按钮点击事件处理
					builder.setPositiveButton(LoginActivity.this.getResources().getString(R.string.confirm),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									Intent intent = new Intent(
											LoginActivity.this,
											RegisterActivity.class);
									intent.putExtra("isDiSanFang", true);
									intent.putExtra("userName", userName);
									startActivity(intent);
								}
							});
					builder.create().show();
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
				SettingUtils.set(LoginActivity.this, "userPic", userPic);
				SettingUtils.set(LoginActivity.this, "userId",
						String.valueOf(userID));
				SettingUtils.set(LoginActivity.this, "realName", realName);
				SettingUtils.set(LoginActivity.this, "sex", userSex);
				SettingUtils.set(LoginActivity.this, "userType", userType2);
				SettingUtils.set(LoginActivity.this, "phone", phone);
				SettingUtils.set(LoginActivity.this, "birthday", birthday);
				SettingUtils.set(LoginActivity.this, "height",
						String.valueOf(height));
				SettingUtils.set(LoginActivity.this, "weight",
						String.valueOf(weight));
				SettingUtils.set(LoginActivity.this, "loginNum", true);
				SettingUtils.set(LoginActivity.this, "isFirstChart", "1");
				SettingUtils.set(LoginActivity.this, "isPush", info.getIsPush());
				SettingUtils.set(LoginActivity.this, "isFirstIn", "1");
				SettingUtils.set(LoginActivity.this, "myFriendData", "1");
				SettingUtils.set(LoginActivity.this, "isFirstChart", "1");
				SettingUtils.set(LoginActivity.this, "isFirstTrendChart", "1");
				SettingUtils.set(LoginActivity.this, "isFirstFriendChart", "1");
				SettingUtils.set(LoginActivity.this, "isFirstFriendData", "1");
				// startActivity(new Intent(LoginActivity.this,
				// MainActivity.class));
				finish();

				// TODO 别名与标签 API
				Set<String> set = new HashSet<String>();
				set.add("sysMessage");
				JPushInterface.setAliasAndTags(LoginActivity.this,
						info.getUserId() + "", set, null);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_showCode:
			iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
			code = Code.getInstance().getCode();
			break;
			
		case R.id.forget_passsword:
			
			Intent intent = new Intent(LoginActivity.this,ForgetPwdActivity.class);
			startActivity(intent);
			
			break;
		case R.id.search_right_txtView:
			Intent intent2 = new Intent(LoginActivity.this,RegisterActivity.class);
			startActivity(intent2);
			break;
		default:
			break;
		}
	}
	
	/******************************** 双击退出 ***********************************/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			exitBy2Click(); // 调用双击退出函数
			break;
		}
		return false;
	}
	
	
	
	/** * 双击退出函数 */
	private static Boolean isExit = false;
	private ArrayList<Fragment> fragments;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, this.getResources().getString(R.string.zaianyicituichuchengxu), Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
		} else {
			Intent home = new Intent(Intent.ACTION_MAIN);
			home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			home.addCategory(Intent.CATEGORY_HOME);
			startActivity(home);
			System.exit(0);
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		edt_phone = null;
		edt_y_z_m = null;
	}
	
	/**
	 * 非阻塞提示方式
	 *//*
	public void prompt(String content) {
		Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
	}

	*//**
	 * 非阻塞提示方式
	 *//*
	public void prompt(int content) {
		Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
	}*/
}
