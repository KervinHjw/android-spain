package com.ludees.scian.login;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ludees.scian.ExampleApplication;
import com.ludees.scian.R;
import com.ludees.scian.entity.ResponseCommon;
import com.ludees.scian.entity.UserCommonBean;
import com.ludees.scian.entity.UserInfo;
import com.ludees.scian.main.HomeActivity;
import com.ludees.scian.network.Urls;
import com.ludees.scian.util.SettingUtils;
import com.ludees.scian.util.ToastUtils;
import com.ludees.scian.view.CustomDialog;
import com.ludees.scian.view.CustomRuler;
import com.ludees.scian.view.CustomDialog.Builder;
import com.ludees.scian.view.CustomRuler.OnValueChangeListener;
import com.ludees.scian.widget.NumericWheelAdapter;
import com.ludees.scian.widget.OnWheelChangedListener;
import com.ludees.scian.widget.WheelView;
import com.google.gson.Gson;

/**
 * 个人信息注册
 * 
 * @author jiyx
 * 
 */
public class RegisterPersonalDataActivity extends Activity implements
		OnClickListener, OnCheckedChangeListener {
	private TextView title;
	private LinearLayout back;

	private Button register_btn_save;

	private LinearLayout register_delete;

	private TextView register_delete_tv;
	private RelativeLayout register_layout;
	/**
	 * 时间控件
	 */
	private DateNumericAdapter monthAdapter, dayAdapter, yearAdapter;
	private WheelView year, month, day;
	private int mCurYear = 120, mCurMonth = 5, mCurDay = 14;
	/**
	 * 我的身高
	 */
	private TextView ri_my_height;
	private CustomRuler ri_cr_height;
	/**
	 * 我的体重
	 */
	private TextView ri_my_weight;
	private CustomRuler ri_cr_weight;
	/**
	 * 性别
	 */
	private int sex = 1;
	private ToggleButton ri_sex;
	/**
	 * 我的姓名
	 */
	private EditText ri_name;
	/**
	 * 我的生日
	 */
	private TextView register_birthday;

	Gson gson = new Gson();
	private FinalHttp fh;
	/**
	 * 我的手机号
	 */
	private String phone;
	/**
	 * 注册类型
	 */
	private static final String USERTYPE = "3";
	/**
	 * 第三方登录标识
	 */
	private String userName;
	private ProgressDialog dialog;
	private String pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_personal_data);
		ExampleApplication.getInstance().addActivity(this);
		initViews();
		initListener();
		setData();

		ExampleApplication.getInstance().addActivity(this);
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		fh = new FinalHttp();
		fh.configTimeout(15 * 1000);

		title = (TextView) findViewById(R.id.search_titleText);
		back = (LinearLayout) findViewById(R.id.search_leftLayout);

		register_delete = (LinearLayout) findViewById(R.id.search_rightLayout);
		register_delete_tv = (TextView) findViewById(R.id.search_right_txtView);
		// register_delete_tv.setVisibility(View.VISIBLE);
		register_btn_save = (Button) findViewById(R.id.register_btn_save);
		register_birthday = (TextView) this
				.findViewById(R.id.register_birthday);
		register_layout = (RelativeLayout) this
				.findViewById(R.id.register_layout);

		ri_name = (EditText) this.findViewById(R.id.ri_name);
		ri_sex = (ToggleButton) this.findViewById(R.id.ri_sex);
		ri_sex.setOnCheckedChangeListener(this);
		/*userType = getIntent().getStringExtra("userType");
		// isDiSanFang = getIntent().getBooleanExtra("isDiSanFang", false);
		if (userType.equals("1")) {
			phone = getIntent().getStringExtra("phone");
		} else {
			userName = getIntent().getStringExtra("userName");
			phone = getIntent().getStringExtra("phone");
		}*/

		ri_my_height = (TextView) this.findViewById(R.id.ri_my_height);
		ri_my_weight = (TextView) this.findViewById(R.id.ri_my_weight);
		ri_cr_height = (CustomRuler) findViewById(R.id.ri_cr_height);
		ri_cr_weight = (CustomRuler) findViewById(R.id.ri_cr_weight);
	}

	/**
	 * 设置初始化数据
	 */
	@SuppressLint("SimpleDateFormat")
	private void setData() {
		title.setText(this.getResources().getString(R.string.gerenxinxi));
		register_delete_tv.setText(this.getResources().getString(R.string.time_item_delete));
		register_birthday.setText(new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date()));
		// 设置身高、体重刻度尺值
		ri_cr_height.initViewParam(170f, 300, CustomRuler.MOD_TYPE_ONE);
		ri_cr_weight.initViewParam(48.5f, 300, CustomRuler.MOD_TYPE_ONE);
		ri_my_height.setText(170 + "");
		ri_my_weight.setText(48.5 + "");
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		back.setOnClickListener(this);
		register_layout.setOnClickListener(this);
		register_btn_save.setOnClickListener(this);
		register_delete.setOnClickListener(this);
		ri_cr_height.setValueChangeListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(float value) {
				ri_my_height.setText(value / 10 + "");
			}
		});
		ri_cr_weight.setValueChangeListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(float value) {
				ri_my_weight.setText(value / 10 + "");
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_leftLayout:// 返回
			finish();
			break;
		case R.id.register_btn_save:// 保存注册信息
			boolean flag = getYanZheng();
			if (flag) {
				dialog = new ProgressDialog(RegisterPersonalDataActivity.this);
				dialog.setCancelable(false);
				dialog.setMessage(RegisterPersonalDataActivity.this.getResources().getString(R.string.zhengzailianjieqingshaohou));
				dialog.show();
				String usermail = getIntent().getStringExtra("usermail");
				pwd = getIntent().getStringExtra("pwd");
				String verifyCode = getIntent().getStringExtra("verifyCode");
				String URL = Urls.USER_REGISTER;
				AjaxParams params = new AjaxParams();
				params.put("usermail", usermail);
				params.put("pwd", pwd);
				params.put("verifyCode", verifyCode);
				params.put("userType", USERTYPE);
				//params.put("phone", "");
				/*if (userType.equals("1")) {
				} else {
					params.put("phone", phone);
					params.put("userName", userName);
				}*/
				params.put("realName", ri_name.getText().toString());
				params.put("sex", String.valueOf(sex));
				params.put("birthday", register_birthday.getText().toString());
				params.put("height", ri_my_height.getText().toString());
				params.put("weight", ri_my_weight.getText().toString());
				registerUser(URL, params);
			}
			break;
		case R.id.search_rightLayout:// 删除注册信息
			Toast.makeText(this, this.getResources().getString(R.string.shanchuzhucexinxi), Toast.LENGTH_SHORT).show();
			break;
		case R.id.register_layout:
			getTimePicker2();
			break;
		}

	}

	/**
	 * 获取时间控件
	 */
	@SuppressLint("InflateParams")
	private void getTimePicker2() {
		// TODO
		View view = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.personalcenter_time_picker_year, null);
		year = (WheelView) view.findViewById(R.id.year);
		month = (WheelView) view.findViewById(R.id.month);
		day = (WheelView) view.findViewById(R.id.day);
		CustomDialog.Builder builder = new Builder(
				RegisterPersonalDataActivity.this);
		builder.isBottom(true, getWindowManager(), getWindow());
		builder.setContentView(view);
		builder.setTitle(this.getResources().getString(R.string.xuanqushijian));
		Calendar calendar = Calendar.getInstance();
		OnWheelChangedListener listener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateDays(year, month, day);
			}
		};
		int curYear = calendar.get(Calendar.YEAR);
		String birthday = register_birthday.getText().toString();
		if (birthday != null && birthday.contains("-")) {
			String str[] = birthday.split("-");
			mCurYear = 120 - (curYear - Integer.parseInt(str[0]));
			mCurMonth = Integer.parseInt(str[1]) - 1;
			mCurDay = Integer.parseInt(str[2]) - 1;
		}
		yearAdapter = new DateNumericAdapter(this, curYear - 120,
				curYear + 100, 100);
		year.setViewAdapter(yearAdapter);
		year.setCurrentItem(mCurYear);
		year.addChangingListener(listener);

		monthAdapter = new DateNumericAdapter(this, 1, 12, 5);
		month.setViewAdapter(monthAdapter);
		month.setCurrentItem(mCurMonth);
		month.addChangingListener(listener);

		// 设置日期
		updateDays(year, month, day);
		day.setCurrentItem(mCurDay);
		day.addChangingListener(listener);
		builder.setPositiveButton(this.getResources().getString(R.string.queding),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Calendar calendar = Calendar.getInstance();
						int years = calendar.get(Calendar.YEAR)
								- (120 - year.getCurrentItem());
						int m = month.getCurrentItem() + 1;
						int d = day.getCurrentItem() + 1;
						register_birthday.setText(years + "-"
								+ (m < 10 ? ("0" + m) : m) + "-"
								+ (d < 10 ? ("0" + d) : d));
						dialog.cancel();
					}
				});

		builder.create().show();

	}

	public static int dip2pixel(Context paramContext, float paramFloat) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				paramFloat, paramContext.getResources().getDisplayMetrics());
	}

	/**
	 * 非阻塞提示方式
	 */
	public void prompt(String content) {
		Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 非阻塞提示方式
	 */
	public void prompt(int content) {
		Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
	}
	
	private boolean getYanZheng() {
		// 姓名验证
		if (ri_name.getText().toString() == null
				|| ri_name.getText().toString().equals("")) {
			prompt(this.getResources().getString(R.string.ninhaimeiyoutianxiexingming));
			return false;
		}
		// 生日验证
		if (register_birthday.getText().toString() == null
				|| register_birthday.getText().toString().equals(this.getResources().getString(R.string.qingxuanzechushengriqi))) {
			prompt(this.getResources().getString(R.string.ninhaimeiyoutianxieshengri));
			return false;
		}
		// 身高验证
		if (ri_my_height.getText().toString() == null
				|| ri_my_height.getText().toString().equals("")) {
			prompt(this.getResources().getString(R.string.ninhaimeiyouxuanzeshengao));
			return false;
		}
		// 体重验证
		if (ri_my_weight.getText().toString() == null
				|| ri_my_weight.getText().toString().equals("")) {
			prompt(this.getResources().getString(R.string.ninhaimeiyouxuanzetizhong));
			return false;
		}
		return true;

	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		if (arg1) {
			sex = 1;
		} else {
			sex = 2;
		}
	}

	/**
	 * 用户注册
	 * 
	 * @param URL
	 * @param params
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void registerUser(String URL, AjaxParams params) {
		fh.post(URL, params, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				dialog.dismiss();
				ToastUtils.TextToast(getApplicationContext(), RegisterPersonalDataActivity.this.getResources().getString(R.string.wangluolianjieshibai));
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String content = (String) t;
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					dialog.dismiss();
					prompt(common.getMsg());
					return;
				}
				UserCommonBean bean = gson.fromJson(content,
						UserCommonBean.class);
				UserInfo info = bean.getData();
				//
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
				SettingUtils.set(RegisterPersonalDataActivity.this, "userId",
						String.valueOf(userID));
				SettingUtils.set(RegisterPersonalDataActivity.this, "realName",
						realName);
				SettingUtils.set(RegisterPersonalDataActivity.this, "sex",
						String.valueOf(userSex));
				SettingUtils.set(RegisterPersonalDataActivity.this, "userType",
						String.valueOf(userType2));
				SettingUtils.set(RegisterPersonalDataActivity.this, "phone",
						phone);
				SettingUtils.set(RegisterPersonalDataActivity.this, "birthday",
						birthday);
				SettingUtils.set(RegisterPersonalDataActivity.this, "height",
						String.valueOf(height));
				SettingUtils.set(RegisterPersonalDataActivity.this, "weight",
						String.valueOf(weight));
				SettingUtils.set(RegisterPersonalDataActivity.this, "usermali",
						usermail);
				SettingUtils.set(RegisterPersonalDataActivity.this, "pwd",
						pwd);
				prompt(RegisterPersonalDataActivity.this.getResources().getString(R.string.zhucechenggong));

				dialog.dismiss();
				startActivity(new Intent(RegisterPersonalDataActivity.this,
						HomeActivity.class));
				// TODO 遍历所有Activity并finish
				ExampleApplication.getInstance().exit();
			}
		});
	}

	/**
	 * 数字轮适配器。突出当前值。
	 */
	private class DateNumericAdapter extends NumericWheelAdapter {

		public DateNumericAdapter(Context context, int minValue, int maxValue,
				int current) {
			super(context, minValue, maxValue);
			setTextSize(24);
		}

		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			view.setTypeface(Typeface.SANS_SERIF);
		}

		public CharSequence getItemText(int index) {
			return super.getItemText(index);
		}
	}

	/**
	 * 获取时间控件时间 TODO
	 * 
	 * @param year
	 * @param month
	 * @param day
	 */
	private void updateDays(WheelView year, WheelView month, WheelView day) {

		Calendar calendar = Calendar.getInstance();
		// 设置选择的年份
		calendar.set(Calendar.YEAR,
				calendar.get(Calendar.YEAR) + year.getCurrentItem());
		// 设置选择的月份
		calendar.set(Calendar.MONTH, month.getCurrentItem());
		// 获取设置月份的天数
		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		dayAdapter = new DateNumericAdapter(this, 1, maxDays,
				calendar.get(Calendar.DAY_OF_MONTH) - 1);
		day.setViewAdapter(dayAdapter);
	}
}
