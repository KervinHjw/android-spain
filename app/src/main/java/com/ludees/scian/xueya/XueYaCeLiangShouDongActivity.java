package com.ludees.scian.xueya;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ludees.scian.BaseActivity;
import com.ludees.scian.ExampleApplication;
import com.ludees.scian.R;
import com.ludees.scian.entity.ResponseCommon;
import com.ludees.scian.main.HomeActivity;
import com.ludees.scian.network.Urls;
import com.ludees.scian.util.SettingUtils;
import com.ludees.scian.view.CustomDialog;
import com.ludees.scian.view.CustomDialog.Builder;
import com.ludees.scian.widget.NumericWheelAdapter;
import com.ludees.scian.widget.OnWheelChangedListener;
import com.ludees.scian.widget.WheelView;
import com.google.gson.Gson;

/**
 * 血压手动测量
 * 
 * @author zhangp
 * 
 */
public class XueYaCeLiangShouDongActivity extends BaseActivity implements
		OnClickListener {
	/**
	 * 头部左边按钮
	 */
	private LinearLayout search_leftLayout;
	/**
	 * 头部中心标题
	 */
	private TextView search_titleText;
	/**
	 * 头部右边按钮
	 */
	private LinearLayout search_rightLayout;
	private TextView search_right_txtView;
	/**
	 * 收缩压
	 */
	private EditText xueya_shoudong_shousuoya;
	/**
	 * 舒张压
	 */
	private EditText xueya_shoudong_shuzhangya;
	/**
	 * 脉搏
	 */
	private EditText xueya_shoudong_maibo;
	/**
	 * 测量时间
	 */
	private RelativeLayout xueya_shoudong_layout5;
	private TextView xueya_shoudong_time;
	/**
	 * 时间控件
	 */
	private DateNumericAdapter yearAdapter, monthAdapter, dayAdapter,
			hourAdapter, minuteAdapter;
	private WheelView year, month, day, hour, minute;
	private int mCurYear = 120, mCurMonth = 5, mCurDay = 14, mCurHour = 12,
			mCurMinute = 30;

	private Gson gson;
	private FinalHttp fh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xueya_celiang_shoudong);
		init();
		setTimeData();

	}

	private void init() {
		fh = new FinalHttp();
		fh.configTimeout(15000);
		gson = new Gson();
		ExampleApplication.getInstance().addActivity(this);
		search_leftLayout = (LinearLayout) this
				.findViewById(R.id.search_leftLayout);
		search_titleText = (TextView) this.findViewById(R.id.search_titleText);
		search_rightLayout = (LinearLayout) this
				.findViewById(R.id.search_rightLayout);
		search_right_txtView = (TextView) this
				.findViewById(R.id.search_right_txtView);
		search_right_txtView.setVisibility(View.VISIBLE);
		search_right_txtView.setText(getResources().getString(R.string.personaldata_save));
		search_titleText.setText(getResources().getString(R.string.xueyaceliangzhidaoactivity_sdsr));
		search_rightLayout.setOnClickListener(this);
		search_leftLayout.setOnClickListener(this);

		xueya_shoudong_shousuoya = (EditText) this
				.findViewById(R.id.xueya_shoudong_shousuoya);
		xueya_shoudong_shuzhangya = (EditText) this
				.findViewById(R.id.xueya_shoudong_shuzhangya);
		xueya_shoudong_maibo = (EditText) this
				.findViewById(R.id.xueya_shoudong_maibo);
		xueya_shoudong_layout5 = (RelativeLayout) this
				.findViewById(R.id.xueya_shoudong_layout5);
		xueya_shoudong_time = (TextView) this
				.findViewById(R.id.xueya_shoudong_time);
		xueya_shoudong_layout5.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO 点击事件处理
		switch (v.getId()) {
		case R.id.search_leftLayout:
			SettingUtils.set(XueYaCeLiangShouDongActivity.this, "myFriendData",
					"1");
			SettingUtils.set(XueYaCeLiangShouDongActivity.this, "isFirstChart",
					"1");
			SettingUtils.set(XueYaCeLiangShouDongActivity.this,
					"isFirstTrendChart", "1");
			SettingUtils.set(XueYaCeLiangShouDongActivity.this,
					"isFirstFriendChart", "1");
			SettingUtils.set(XueYaCeLiangShouDongActivity.this,
					"isFirstFriendData", "1");
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			finish();
			break;
		case R.id.search_rightLayout:
			if (isSuccess()) {
				String URL = Urls.SAVE_BLOOD_PRESSURE;
				AjaxParams abRequestParams = new AjaxParams();
				abRequestParams.put("userId", SettingUtils.get(
						XueYaCeLiangShouDongActivity.this, "userId", ""));
				abRequestParams.put("bloodPressureOpen",
						xueya_shoudong_shuzhangya.getText().toString());
				abRequestParams.put("bloodPressureClose",
						xueya_shoudong_shousuoya.getText().toString());
				abRequestParams.put("pulse", xueya_shoudong_maibo.getText()
						.toString());
				abRequestParams.put("measureTime", xueya_shoudong_time
						.getText().toString());
				abRequestParams.put("type", "2");
				abRequestParams.put("equipmentNo", "");
				setData(URL, abRequestParams);
			}

			break;
		case R.id.xueya_shoudong_layout5:
			getTimePicker();
			break;
		default:
			break;
		}
	}

	/**
	 * 设置初始时间
	 */
	@SuppressLint("SimpleDateFormat")
	private void setTimeData() {
		// TODO 初始化时间
		String newTime = new SimpleDateFormat("yyyy-MM-dd HH:mm")
				.format(new Date());
		xueya_shoudong_time.setText(newTime);
	}

	/**
	 * TODO 显示时间弹出框
	 */
	@SuppressLint("InflateParams")
	private void getTimePicker() {
		View view = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.time_picker, null);
		year = (WheelView) view.findViewById(R.id.year);
		month = (WheelView) view.findViewById(R.id.month);
		day = (WheelView) view.findViewById(R.id.day);
		hour = (WheelView) view.findViewById(R.id.hour);
		minute = (WheelView) view.findViewById(R.id.minute);
		// 设置初始显示时间
		Calendar calendar = Calendar.getInstance();
		OnWheelChangedListener listener = new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateDays(year, month, day);
			}
		};
		int curYear = calendar.get(Calendar.YEAR);
		String celiang_time = xueya_shoudong_time.getText().toString();
		if (celiang_time != null && celiang_time.contains(" ")) {
			String[] split = celiang_time.split(" ");
			if (split.length == 2) {
				if (split[0].contains("-")) {
					String str[] = split[0].split("-");
					mCurYear = 120 - (curYear - Integer.parseInt(str[0]));
					mCurMonth = Integer.parseInt(str[1]) - 1;
					mCurDay = Integer.parseInt(str[2]) - 1;
				}
				if (split[1].contains(":")) {
					String str[] = split[1].split(":");
					mCurHour = Integer.parseInt(str[0]);
					mCurMinute = Integer.parseInt(str[1]);
				}
			}
		}
		// 设置年份
		yearAdapter = new DateNumericAdapter(this, curYear - 120,
				curYear + 100, 100);
		year.setViewAdapter(yearAdapter);
		year.setCurrentItem(mCurYear);
		year.addChangingListener(listener);
		// 设置月份
		monthAdapter = new DateNumericAdapter(this, 1, 12, 5);
		month.setViewAdapter(monthAdapter);
		month.setCurrentItem(mCurMonth);
		month.addChangingListener(listener);
		// 设置日期
		updateDays(year, month, day);
		day.setCurrentItem(mCurDay);
		day.addChangingListener(listener);
		// 设置小时
		hourAdapter = new DateNumericAdapter(this, 0, 23, 5);
		hour.setViewAdapter(hourAdapter);
		hour.setCurrentItem(mCurHour);
		hour.addChangingListener(listener);
		// 设置分钟
		minuteAdapter = new DateNumericAdapter(this, 0, 59, 5);
		minute.setViewAdapter(minuteAdapter);
		minute.setCurrentItem(mCurMinute);
		minute.addChangingListener(listener);

		// 显示对话框
		CustomDialog.Builder builder = new Builder(
				XueYaCeLiangShouDongActivity.this);
		builder.isBottom(true, getWindowManager(), getWindow());
		builder.setContentView(view);
		builder.setTitle(this.getResources().getString(R.string.xuanqushijian));
		builder.setPositiveButton(this.getResources().getString(R.string.queding),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						Calendar calendar = Calendar.getInstance();
						int years = calendar.get(Calendar.YEAR)
								- (120 - year.getCurrentItem());
						int m = month.getCurrentItem() + 1;
						int d = day.getCurrentItem() + 1;
						int h = hour.getCurrentItem();
						int m2 = minute.getCurrentItem();
						xueya_shoudong_time.setText(years + "-"
								+ (m < 10 ? ("0" + m) : m) + "-"
								+ (d < 10 ? ("0" + d) : d) + " "
								+ (h < 10 ? ("0" + h) : h) + ":"
								+ (m2 < 10 ? ("0" + m2) : m2));

						dialog.cancel();
					}
				});

		builder.create().show();
	}

	/**
	 * 血压测量增添数据
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setData(String URL, AjaxParams params) {
		fh.post(URL, params, new AjaxCallBack() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				prompt(strMsg);
			}

			@Override
			public void onSuccess(Object t) {
				String content = (String) t;
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					prompt(common.getMsg());
					return;
				}
				prompt(common.getMsg());
				SettingUtils.set(XueYaCeLiangShouDongActivity.this,
						"myFriendData", "1");
				SettingUtils.set(XueYaCeLiangShouDongActivity.this,
						"isFirstChart", "1");
				SettingUtils.set(XueYaCeLiangShouDongActivity.this,
						"isFirstTrendChart", "1");
				SettingUtils.set(XueYaCeLiangShouDongActivity.this,
						"isFirstFriendChart", "1");
				SettingUtils.set(XueYaCeLiangShouDongActivity.this,
						"isFirstFriendData", "1");
				Intent intent = new Intent(XueYaCeLiangShouDongActivity.this,
						XueYaCeLiangDataActivity.class);
				intent.putExtra("isFirst", false);
				finish();

			}
		});
	}

	private boolean isSuccess() {
		// 收缩压
		if (xueya_shoudong_shousuoya.getText().toString() == null
				|| xueya_shoudong_shousuoya.getText().toString().equals("")
				|| xueya_shoudong_shousuoya.getText().toString().equals("null")) {
			prompt(this.getResources().getString(R.string.ninhaimeiyoutianxieshousuoya));
			return false;
		}
		// 舒张压
		if (xueya_shoudong_shuzhangya.getText().toString() == null
				|| xueya_shoudong_shuzhangya.getText().toString().equals("")
				|| xueya_shoudong_shuzhangya.getText().toString()
						.equals("null")) {
			prompt(this.getResources().getString(R.string.ninhaimeiyoutianxieshuzhangya));
			return false;
		}
		// 脉搏
		if (xueya_shoudong_maibo.getText().toString() == null
				|| xueya_shoudong_maibo.getText().toString().equals("")
				|| xueya_shoudong_maibo.getText().toString().equals("null")) {
			prompt(this.getResources().getString(R.string.ninhaimeiyoutianxiemaibo));
			return false;
		}
		// 测量时间
		if (xueya_shoudong_time.getText().toString() == null
				|| xueya_shoudong_time.getText().toString().equals("")
				|| xueya_shoudong_time.getText().toString().equals("null")) {
			prompt(this.getResources().getString(R.string.ninhaimeiyoutianxieceliangshijian));
			return false;
		}
		if (Integer.valueOf(xueya_shoudong_shousuoya.getText().toString()) < 10
				|| Integer.valueOf(xueya_shoudong_shousuoya.getText()
						.toString()) > 200) {
			prompt(this.getResources().getString(R.string.qingshurushidaoliangbaineizhengquedeshuzi));
			return false;
		}
		if (Integer.valueOf(xueya_shoudong_shuzhangya.getText().toString()) < 10
				|| Integer.valueOf(xueya_shoudong_shuzhangya.getText()
						.toString()) > 200) {
			prompt(this.getResources().getString(R.string.qingshurushidaoliangbaineizhengquedeshuzi));
			return false;
		}
		if (Integer.valueOf(xueya_shoudong_maibo.getText().toString()) < 10
				|| Integer.valueOf(xueya_shoudong_maibo.getText().toString()) > 200) {
			prompt(this.getResources().getString(R.string.qingshurushidaoliangbaineizhengquedeshuzi));
			return false;
		}
		if (Integer.valueOf(xueya_shoudong_shousuoya.getText().toString()) < Integer
				.valueOf(xueya_shoudong_shuzhangya.getText().toString())) {
			prompt(this.getResources().getString(R.string.shousuoyabunengxiaoyushuzhangya));
			return false;
		}
		return true;
	}

	/**
	 * 数字轮适配器。突出当前值。 // TODO
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
	 * TODO 获取日期时间控件时间
	 * 
	 * @param year
	 * @param month
	 * @param day
	 */
	private void updateDays(WheelView year, WheelView month, WheelView day) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR,
				calendar.get(Calendar.YEAR) + year.getCurrentItem());
		calendar.set(Calendar.MONTH, month.getCurrentItem());

		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		dayAdapter = new DateNumericAdapter(this, 1, maxDays,
				calendar.get(Calendar.DAY_OF_MONTH) - 1);
		day.setViewAdapter(dayAdapter);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			SettingUtils.set(XueYaCeLiangShouDongActivity.this, "myFriendData",
					"1");
			SettingUtils.set(XueYaCeLiangShouDongActivity.this, "isFirstChart",
					"1");
			SettingUtils.set(XueYaCeLiangShouDongActivity.this,
					"isFirstTrendChart", "1");
			SettingUtils.set(XueYaCeLiangShouDongActivity.this,
					"isFirstFriendChart", "1");
			SettingUtils.set(XueYaCeLiangShouDongActivity.this,
					"isFirstFriendData", "1");
			Intent intent = new Intent(XueYaCeLiangShouDongActivity.this,
					HomeActivity.class);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		search_leftLayout = null;
		search_titleText = null;
		search_rightLayout = null;
		search_right_txtView = null;
		xueya_shoudong_shousuoya = null;
		xueya_shoudong_shuzhangya = null;
		xueya_shoudong_maibo = null;
		xueya_shoudong_layout5 = null;
		xueya_shoudong_time = null;
		yearAdapter = null;
		monthAdapter = null;
		dayAdapter = null;
		hourAdapter = null;
		minuteAdapter = null;
		year = null;
		month = null;
		day = null;
		hour = null;
		minute = null;
		gson = null;
		fh = null;
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}
}
