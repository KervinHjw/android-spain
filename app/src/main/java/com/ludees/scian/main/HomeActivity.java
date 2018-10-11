package com.ludees.scian.main;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.image.AbImageLoader;
import com.ludees.scian.BaseActivity;
import com.ludees.scian.ExampleApplication;
import com.ludees.scian.R;
import com.ludees.scian.adapter.SheQuShengHuoPagerAdapter;
import com.ludees.scian.dao.PushMessageDaoImpl;
import com.ludees.scian.entity.PushMessage;
import com.ludees.scian.main.fragment.HistoryDataFragment;
import com.ludees.scian.main.fragment.XueyaCeliangFragment;
import com.ludees.scian.messagecenter.MessageCenterActivity;
import com.ludees.scian.mydevices.MyDevicesActivity;
import com.ludees.scian.personalcenter.PersonalDataActivity;
import com.ludees.scian.qrcode.QRCodeActivity;
import com.ludees.scian.setting.DataCleanManager;
import com.ludees.scian.setting.SettingActivity;
import com.ludees.scian.time.TimeActivity;
import com.ludees.scian.util.BlueToothUtil;
import com.ludees.scian.util.SettingUtils;
import com.ludees.scian.util.ToastUtils;
import com.ludees.scian.view.CircleImageView;
import com.ludees.scian.view.NoScrollViewPager;
import com.ludees.scian.view.SlidingMenu;

@SuppressLint("NewApi")
public class HomeActivity extends BaseActivity implements OnClickListener,
		OnTouchListener {

	public final int PAGE_RECODE_ONE = 0xa1;
	public final int PAGE_RECODE_TWO = 0xa2;
	public final int PAGE_RECODE_THREE = 0xa3;
	public final static int PAGE_RECODE_FOUR = 0xa4;

	public static SlidingMenu menu;

	// 底部菜单
	private RadioGroup mTabButtonGroup;
	private ImageView home_xueya_time;
	private ImageView home_content_xueya_open_slidingmenu;
	private RadioButton home_content_rb_0;
	private RadioButton home_content_rb_1;
	//private RadioButton home_content_rb_2;
	//private RadioButton home_content_rb_3;
	/**
	 * 个人资料
	 */
	private LinearLayout home_left_layout_personalinfo;
	private ImageView home_left_layout_personalinfo_left_img;
	/**
	 * 消息中心
	 */
	private LinearLayout home_left_layout_message;
	private ImageView home_left_layout_message_left_img;
	/**
	 * 我的设备
	 */
	private LinearLayout home_left_layout_mydevices;
	private ImageView home_left_layout_mydevices_left_img;
	/**
	 * 二维码
	 */
	private LinearLayout home_left_layout_myqrcode;
	private ImageView home_left_layout_myqrcode_left_img;
	/**
	 * 设置
	 */
	private LinearLayout home_left_layout_setting;
	private ImageView home_left_layout_setting_left_img;
	/**
	 * 左侧菜单头部
	 */
	private CircleImageView menu_left_user_head_pic;
	private TextView menu_left_user_name;
	private TextView menu_left_user_phone;
	private static ImageView menu_left_message_hongdian;
	private RelativeLayout toptitle;
	private boolean isFristIn = true;
	private int checkedNum = 0;
	int num = 0;
	private NoScrollViewPager mTabPager;
	private static ImageView home_content_xueya_open_hongdian;
	private SheQuShengHuoPagerAdapter adapter;

	private boolean start = true;
	PushMessageDaoImpl pushMessageDao = null;
	private ImageView left_bg;
	private Bitmap btp;
	private BlueToothUtil util;
	private LinearLayout home_left_layout_help;
	private ImageView home_left_layout_help_img;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);// 主视图

		initSlidingMenu();
		initViews();

		ExampleApplication.getInstance().addActivity(this);
		setIsHuaDong(false);

	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		mTabButtonGroup = (RadioGroup) findViewById(R.id.home_content_rg);
		home_content_rb_0 = (RadioButton) findViewById(R.id.home_content_rb_0);
		home_content_rb_1 = (RadioButton) findViewById(R.id.home_content_rb_1);
		//home_content_rb_2 = (RadioButton) findViewById(R.id.home_content_rb_2);
		//home_content_rb_3 = (RadioButton) findViewById(R.id.home_content_rb_3);
		mTabButtonGroup.check(R.id.home_content_rb_0);

		toptitle = (RelativeLayout) findViewById(R.id.toptitle);
		home_content_xueya_open_slidingmenu = (ImageView) findViewById(R.id.home_content_xueya_open_slidingmenu);
		home_xueya_time = (ImageView) findViewById(R.id.home_xueya_time);
		home_content_xueya_open_hongdian = (ImageView) findViewById(R.id.home_content_xueya_open_hongdian);

		// 给ViewPager设置适配器
		home_content_xueya_open_slidingmenu.setOnClickListener(this);
		home_xueya_time.setOnClickListener(this);

		fragments = new ArrayList<Fragment>();
		fragments.add(new XueyaCeliangFragment());
		fragments.add(new HistoryDataFragment());
		//fragments.add(new HealthInfoFragment());
		//fragments.add(new MyFriendFragment());
		mTabPager = (NoScrollViewPager) this.findViewById(R.id.tabpager);
		adapter = new SheQuShengHuoPagerAdapter(getSupportFragmentManager(),
				fragments);
		mTabPager.setAdapter(adapter);
		mTabPager.setOffscreenPageLimit(3);
		mTabPager.setNoScroll(true);
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mTabButtonGroup
				.setOnCheckedChangeListener(new MyOnCheckChangeListner());

		menu = (SlidingMenu) this.findViewById(R.id.id_menu);
		pushMessageDao = new PushMessageDaoImpl(HomeActivity.this);

	}

	/**
	 * 初始化侧滑菜单
	 */
	private void initSlidingMenu() {
		left_bg = (ImageView) findViewById(R.id.left_bg);
		InputStream is = this.getResources().openRawResource(
				R.drawable.home_left_title_bg);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = 2;
		btp = BitmapFactory.decodeStream(is, null, options);
		left_bg.setScaleType(ScaleType.FIT_XY);
		left_bg.setImageBitmap(btp);

		menu_left_user_head_pic = (CircleImageView) findViewById(R.id.home_menu_left_user_head_pic);
		menu_left_user_head_pic.setBorderColor(0x00000000);

		menu_left_user_name = (TextView) findViewById(R.id.home_menu_left_user_name);
		menu_left_user_phone = (TextView) findViewById(R.id.home_menu_left_user_phone);
		menu_left_message_hongdian = (ImageView) findViewById(R.id.home_left_message_hongdian);
		// 个人资料
		home_left_layout_personalinfo = (LinearLayout) findViewById(R.id.home_left_layout_personalinfo);
		home_left_layout_personalinfo_left_img = (ImageView) findViewById(R.id.home_left_layout_personalinfo_left_img);
		home_left_layout_personalinfo.setOnTouchListener(this);
		home_left_layout_personalinfo.setOnClickListener(this);
		// 消息中心
		home_left_layout_message = (LinearLayout) findViewById(R.id.home_left_layout_message);
		home_left_layout_message_left_img = (ImageView) findViewById(R.id.home_left_layout_message_left_img);
		home_left_layout_message.setOnTouchListener(this);
		home_left_layout_message.setOnClickListener(this);
		// 我的设备
		home_left_layout_mydevices = (LinearLayout) findViewById(R.id.home_left_layout_mydevices);
		home_left_layout_mydevices_left_img = (ImageView) findViewById(R.id.home_left_layout_mydevices_left_img);
		home_left_layout_mydevices.setOnTouchListener(this);
		home_left_layout_mydevices.setOnClickListener(this);
		// 二维码
		home_left_layout_myqrcode = (LinearLayout) findViewById(R.id.home_left_layout_myqrcode);
		home_left_layout_myqrcode_left_img = (ImageView) findViewById(R.id.home_left_layout_myqrcode_left_img);
		home_left_layout_myqrcode.setOnTouchListener(this);
		home_left_layout_myqrcode.setOnClickListener(this);
		// 设置
		home_left_layout_setting = (LinearLayout) findViewById(R.id.home_left_layout_setting);
		home_left_layout_setting_left_img = (ImageView) findViewById(R.id.home_left_layout_setting_left_img);
		home_left_layout_setting.setOnTouchListener(this);
		home_left_layout_setting.setOnClickListener(this);
		//帮助
		home_left_layout_help = (LinearLayout) findViewById(R.id.home_left_layout_help);
		home_left_layout_help_img = (ImageView) findViewById(R.id.home_left_help_img);
		home_left_layout_help.setOnTouchListener(this);
		home_left_layout_help.setOnClickListener(this);

		// 设置数据
		String userPic = SettingUtils.get(this, "userPic", "");
		String realName = SettingUtils.get(this, "realName", "");
		String phone = SettingUtils.get(this, "phone", "");
		AbImageLoader loader = new AbImageLoader(this);
		if (!TextUtils.isEmpty(userPic)) {
			loader.display(menu_left_user_head_pic, userPic);
		}
		if (!TextUtils.isEmpty(realName)) {
			menu_left_user_name.setText(realName);
		} else {
			menu_left_user_name.setText("-");
		}
		if (!TextUtils.isEmpty(phone)) {
			menu_left_user_phone.setText(phone);
		} else {
			menu_left_user_phone.setText("-");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_xueya_time:// 定时提醒设置
			startActivity(new Intent(this, TimeActivity.class));
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.home_content_xueya_open_slidingmenu:// 侧滑菜单
			menu.toggle();
			home_content_xueya_open_hongdian.setVisibility(View.INVISIBLE);
			break;
		case R.id.home_left_layout_personalinfo:// 个人资料
			startActivityForResult(
					new Intent(this, PersonalDataActivity.class), 999);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.home_left_layout_message:// 消息中心
			startActivity(new Intent(this, MessageCenterActivity.class));
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.home_left_layout_mydevices:// 我的设备
			startActivity(new Intent(this, MyDevicesActivity.class));
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.home_left_help_img://帮助
			overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
			break;
		case R.id.home_left_layout_myqrcode:// 二维码
			String userId = SettingUtils.get(this, "userId", "");
			if (!TextUtils.isEmpty(userId)) {
				startActivity(new Intent(this, QRCodeActivity.class).putExtra(
						"QR", userId));
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			} else {
				ToastUtils.TextToast(this, this.getResources().getString(R.string.yonghuxinxiweikong));
			}
			break;
			case R.id.home_left_layout_setting:// 设置
				startActivity(new Intent(this, SettingActivity.class));
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 刷新消息中心红点
		// PushMessageDaoImpl pushMessageDao = new PushMessageDaoImpl(this);
		List<PushMessage> list = pushMessageDao.find();
		if (list.size() == 4) {
			if (list.get(0).getIsRead() == 1 /* || list.get(1).getIsRead() == 1 */
					|| list.get(2).getIsRead() == 1
					|| list.get(3).getIsRead() == 1) {
				//menu_left_message_hongdian.setVisibility(View.VISIBLE);
				home_content_xueya_open_hongdian.setVisibility(View.VISIBLE);
			} else {
				//menu_left_message_hongdian.setVisibility(View.INVISIBLE);
				home_content_xueya_open_hongdian.setVisibility(View.INVISIBLE);
			}
		}
		start = getIntent().getBooleanExtra("start", true);
		if (start) {

		} else {
			home_content_rb_1.setChecked(true);
			start = false;
		}

		num = getIntent().getIntExtra("num", 0);
		if (num > 0) {
			checkedNum = num;
		}
		switch (checkedNum) {
		case 0:
			home_content_rb_0.setChecked(true);
			if (isFristIn) {
				mTabPager.setCurrentItem(0, false);
			}
			break;
		case 1:
			home_content_rb_1.setChecked(true);
			break;
		/*case 2:
			home_content_rb_2.setChecked(true);
			break;
		case 3:
			home_content_rb_3.setChecked(true);
			break;*/
		default:
			break;
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt("checkedNum", checkedNum);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 刷新侧滑菜单数据
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 999) {
			// 设置数据
			String userPic = SettingUtils.get(this, "userPic", "");
			String realName = SettingUtils.get(this, "realName", "");
			String phone = SettingUtils.get(this, "phone", "");
			AbImageLoader loader = new AbImageLoader(this);
			if (!TextUtils.isEmpty(userPic)) {
				loader.display(menu_left_user_head_pic, userPic);
			}
			if (!TextUtils.isEmpty(realName)) {
				menu_left_user_name.setText(realName);
			} else {
				menu_left_user_name.setText("-");
			}
			if (!TextUtils.isEmpty(phone)) {
				menu_left_user_phone.setText(phone);
			} else {
				menu_left_user_phone.setText("-");
			}
		} else if (requestCode == 111) {
			if (resultCode == -1) {
				if (util == null) {
					util = new BlueToothUtil(this);
				}
				util.setActivity(this);
				util.setBlueTooth();
			}
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

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO 触摸事件监听
		if (v == home_left_layout_personalinfo) {// 个人资料
			if (event.getAction() == MotionEvent.ACTION_UP) {
				home_left_layout_personalinfo_left_img
						.setVisibility(View.INVISIBLE);
			} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
				home_left_layout_personalinfo_left_img
						.setVisibility(View.VISIBLE);
			}
		} else if (v == home_left_layout_message) {// 消息中心
			if (event.getAction() == MotionEvent.ACTION_UP) {
				home_left_layout_message_left_img.setVisibility(View.INVISIBLE);
			}
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				home_left_layout_message_left_img.setVisibility(View.VISIBLE);
			}
		} else if (v == home_left_layout_mydevices) {// 我的设备
			if (event.getAction() == MotionEvent.ACTION_UP) {
				home_left_layout_mydevices_left_img
						.setVisibility(View.INVISIBLE);
			} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
				home_left_layout_mydevices_left_img.setVisibility(View.VISIBLE);
			}
		} else if(v == home_left_layout_help){//帮助
			if(event.getAction() == MotionEvent.ACTION_UP){
				home_left_layout_help_img.setVisibility(View.INVISIBLE);
			}else if(event.getAction() == MotionEvent.ACTION_DOWN){
				home_left_layout_help_img.setVisibility(View.VISIBLE);
			}
		} else if (v == home_left_layout_myqrcode) {// 二维码
			if (event.getAction() == MotionEvent.ACTION_UP) {
				home_left_layout_myqrcode_left_img
						.setVisibility(View.INVISIBLE);
			} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
				home_left_layout_myqrcode_left_img.setVisibility(View.VISIBLE);
			}
		} else if (v == home_left_layout_setting) {// 设置
			if (event.getAction() == MotionEvent.ACTION_UP) {
				home_left_layout_setting_left_img.setVisibility(View.INVISIBLE);
			} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
				home_left_layout_setting_left_img.setVisibility(View.VISIBLE);
			}
		}
		return false;
	}

	/**
	 * TODO 内部广播，用来刷新在有新消息推送时刷新UI消息红点
	 * 
	 * @author jiyx
	 * 
	 */
	public static class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if ("com.aaa".equals(intent.getAction())) {
				if (isForeground(context, "com.example.ludehealthnew.main.HomeActivity")) {
					//menu_left_message_hongdian.setVisibility(View.VISIBLE);
					home_content_xueya_open_hongdian
							.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	/**
	 * viewpager选择监听事件
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@SuppressLint("ResourceAsColor")
		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				home_content_rb_0.setChecked(true);
				home_content_rb_1.setChecked(false);
				//home_content_rb_2.setChecked(false);
				//home_content_rb_3.setChecked(false);
				toptitle.setVisibility(View.VISIBLE);
				menu.setScroll(true);
				// 刷新消息中心红点
				// PushMessageDaoImpl pushMessageDao = new PushMessageDaoImpl(
				// HomeActivity.this);
				List<PushMessage> list = pushMessageDao.find();
				if (list.size() == 4) {
					if (list.get(0).getIsRead() == 1 /*
													 * ||
													 * list.get(1).getIsRead()
													 * == 1
													 */
							|| list.get(2).getIsRead() == 1
							|| list.get(3).getIsRead() == 1) {
						//menu_left_message_hongdian.setVisibility(View.VISIBLE);
						home_content_xueya_open_hongdian
								.setVisibility(View.VISIBLE);
					} else {
						/*menu_left_message_hongdian
								.setVisibility(View.INVISIBLE);*/
						home_content_xueya_open_hongdian
								.setVisibility(View.INVISIBLE);
					}
				}
				break;
			case 1:
				home_content_rb_0.setChecked(false);
				home_content_rb_1.setChecked(true);
				//home_content_rb_2.setChecked(false);
				//home_content_rb_3.setChecked(false);
				toptitle.setVisibility(View.GONE);
				menu.setScroll(false);
				break;
			case 2:
				home_content_rb_0.setChecked(false);
				home_content_rb_1.setChecked(false);
				//home_content_rb_2.setChecked(true);
				//home_content_rb_3.setChecked(false);
				toptitle.setVisibility(View.GONE);
				menu.setScroll(false);
				break;
			case 3:
				home_content_rb_0.setChecked(false);
				home_content_rb_1.setChecked(false);
				/*home_content_rb_2.setChecked(false);
				home_content_rb_3.setChecked(true);*/
				toptitle.setVisibility(View.GONE);
				menu.setScroll(false);
				break;

			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	public class MyOnCheckChangeListner implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch (checkedId) {
			case R.id.home_content_rb_0:
				mTabPager.setCurrentItem(0, false);
				checkedNum = 0;
				break;
			case R.id.home_content_rb_1:
				mTabPager.setCurrentItem(1, false);
				checkedNum = 1;
				break;
			/*case R.id.home_content_rb_2:
				mTabPager.setCurrentItem(2, false);
				checkedNum = 2;
				break;
			case R.id.home_content_rb_3:
				mTabPager.setCurrentItem(3, false);
				checkedNum = 3;
				break;*/
			default:
				break;
			}
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * 判断某个界面是否在前台
	 * 
	 * @param context
	 * @param className
	 *            某个界面名称
	 */
	private static boolean isForeground(Context context, String className) {
		if (context == null || TextUtils.isEmpty(className)) {
			return false;
		}

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(1);
		if (list != null && list.size() > 0) {
			ComponentName cpn = list.get(0).topActivity;
			if (className.equals(cpn.getClassName())) {
				return true;
			}
		}

		return false;
	}

	@Override
	protected void onDestroy() {
		// TODO 销毁控件
		menu = null;
		util = null;

		mTabButtonGroup = null;
		home_xueya_time = null;
		home_content_xueya_open_slidingmenu = null;
		home_content_rb_0 = null;
		home_content_rb_1 = null;
		//home_content_rb_2 = null;
		//home_content_rb_3 = null;
		home_left_layout_personalinfo = null;
		home_left_layout_personalinfo_left_img = null;
		home_left_layout_message = null;
		home_left_layout_message_left_img = null;
		home_left_layout_setting = null;
		home_left_layout_setting_left_img = null;
		menu_left_user_head_pic = null;
		menu_left_user_name = null;
		menu_left_user_phone = null;
		//menu_left_message_hongdian = null;
		toptitle = null;
		mTabPager = null;
		home_content_xueya_open_hongdian = null;
		btp = null;
		adapter = null;
		if (fragments != null) {
			fragments.clear();
		}
		DataCleanManager.cleanInternalCache(this);
		DataCleanManager.cleanExternalCache(this);
		DataCleanManager.cleanFiles(this);
		System.gc();
		super.onDestroy();
	}

}
