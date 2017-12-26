package com.en.scian.time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.en.scian.BaseActivity;
import com.en.scian.ExampleApplication;
import com.en.scian.R;
import com.en.scian.entity.ResponseCommon;
import com.en.scian.network.Urls;
import com.en.scian.util.SettingUtils;
import com.en.scian.util.ToastUtils;
import com.en.scian.view.PickerView;
import com.google.gson.Gson;

/**
 * 新建提醒
 * 
 * @author jiyx
 * 
 */
public class TimeSelectorActivity extends BaseActivity implements
		OnClickListener {
	private TextView title;
	private LinearLayout back;

	private LinearLayout search_rightLayout;
	private TextView search_right_txtView;

	private PickerView hour_pv;
	private PickerView minute_pv;
	private Button time_btn_delete;

	private String updateTime;
	private int num = 0;
	private String userId;

	private Gson gson;

	private FinalHttp fh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_timeselector);
		initViews();
		initListener();
		setData();
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		fh = new FinalHttp();
		fh.configTimeout(15 * 1000);
		gson = new Gson();
		ExampleApplication.getInstance().addActivity(this);
		title = (TextView) findViewById(R.id.search_titleText);
		back = (LinearLayout) findViewById(R.id.search_leftLayout);

		search_rightLayout = (LinearLayout) findViewById(R.id.search_rightLayout);
		search_right_txtView = (TextView) findViewById(R.id.search_right_txtView);
		search_right_txtView.setVisibility(View.VISIBLE);

		hour_pv = (PickerView) findViewById(R.id.hour_pv);
		minute_pv = (PickerView) findViewById(R.id.minute_pv);

		time_btn_delete = (Button) findViewById(R.id.time_btn_delete);
		num = getIntent().getIntExtra("num", 0);
		userId = SettingUtils.get(TimeSelectorActivity.this, "userId", "");

	}

	/**
	 * 设置初始化数据
	 */
	private void setData() {
		title.setText(this.getResources().getString(R.string.xinjiantixing));
		search_right_txtView.setText(this.getResources().getString(R.string.personaldata_save));
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minuts = calendar.get(Calendar.MINUTE);
		updateTime = getIntent().getStringExtra("time");
		if (!TextUtils.isEmpty(updateTime)) {
			// time_btn_delete.setVisibility(View.VISIBLE);
			title.setText(this.getResources().getString(R.string.bianjitixing));
		}

		// 设置小时数据
		List<String> data = new ArrayList<String>();
		for (int i = 0; i < 24; i++) {
			if (i < 10) {
				data.add("0" + i);
			} else {
				data.add("" + i);
			}
		}

		// 设置分钟数据
		List<String> seconds = new ArrayList<String>();
		for (int i = 0; i < 60; i++) {
			seconds.add(i < 10 ? "0" + i : "" + i);
		}

		switch (num) {
		case 1:
			minute_pv.setTime(minuts);
			hour_pv.setTime(hour);
			break;
		case 2:
			minute_pv.setTime(minuts);
			hour_pv.setTime(hour);
			break;
		case 3:
			int hourChange = Integer
					.valueOf(getIntent().getStringExtra("hour"));
			int minuteChange = Integer.valueOf(getIntent().getStringExtra(
					"minute"));
			minute_pv.setTime(minuteChange);
			hour_pv.setTime(hourChange);
			break;
		case 4:
			int hourChange2 = Integer.valueOf(getIntent()
					.getStringExtra("hour"));
			int minuteChange2 = Integer.valueOf(getIntent().getStringExtra(
					"minute"));
			minute_pv.setTime(minuteChange2);
			hour_pv.setTime(hourChange2);
			break;
		default:
			break;
		}

		hour_pv.setData(data);
		minute_pv.setData(seconds);

	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		back.setOnClickListener(this);
		search_rightLayout.setOnClickListener(this);
		time_btn_delete.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_leftLayout:// 返回
			// startActivity(new Intent(this, TimeChiyaoActivity.class));
			finish();
			break;
		case R.id.search_rightLayout:// 保存新建提醒时间
			// TODO
			switch (num) {
			// 吃药新增
			case 1:
				String URL = Urls.ADD_REMIND_INFO;
				AjaxParams params = new AjaxParams();
				params.put("remindHour", hour_pv.getTime());
				params.put("remindMinute", minute_pv.getTime());
				params.put("remindType", "1");
				params.put("state", "1");
				params.put("userId", userId);
				setTime(URL, params);
				break;
			// 测量新增
			case 2:
				String URL2 = Urls.ADD_REMIND_INFO;
				AjaxParams params2 = new AjaxParams();
				params2.put("remindHour", hour_pv.getTime());
				params2.put("remindMinute", minute_pv.getTime());
				params2.put("remindType", "2");
				params2.put("state", "1");
				params2.put("userId", userId);
				setTime(URL2, params2);
				break;
			// 吃药修改
			case 3:
				String URL_UPDATE = Urls.UPDATE_REMIND_INFO;
				AjaxParams params3 = new AjaxParams();
				params3.put("id",
						String.valueOf(getIntent().getIntExtra("id", 0)));
				params3.put("remindHour", hour_pv.getTime());
				params3.put("remindMinute", minute_pv.getTime());
				params3.put("remindType", "1");
				int state = getIntent().getIntExtra("state", 0);
				params3.put("state", String.valueOf(state));
				params3.put("userId", userId);
				changeTime(URL_UPDATE, params3);
				break;
			// 测量修改
			case 4:
				String URL_UPDATE2 = Urls.UPDATE_REMIND_INFO;
				AjaxParams params4 = new AjaxParams();
				params4.put("id",
						String.valueOf(getIntent().getIntExtra("id", 0)));
				params4.put("remindHour", hour_pv.getTime());
				params4.put("remindMinute", minute_pv.getTime());
				params4.put("remindType", "2");
				int state2 = getIntent().getIntExtra("state", 0);
				params4.put("state", String.valueOf(state2));
				params4.put("userId", userId);
				changeTime(URL_UPDATE2, params4);
				break;
			default:
				break;
			}

			break;
		case R.id.time_btn_delete:// 删除提醒时间
			finish();
			break;
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setTime(String URL, AjaxParams params) {
		fh.post(URL, params, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				ToastUtils.TextToast(TimeSelectorActivity.this, getResources().getString(R.string.fuwuqilianjieyichang));
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
				switch (num) {
				case 1:
					startActivity(new Intent(TimeSelectorActivity.this,
							TimeChiyaoActivity.class));
					break;
				case 2:
					startActivity(new Intent(TimeSelectorActivity.this,
							TimeCeliangActivity.class));
					break;
				default:
					break;
				}
				prompt(TimeSelectorActivity.this.getResources().getString(R.string.xinzengtixingshezhichenggong));
				finish();
			}

		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void changeTime(String URL, AjaxParams params) {
		fh.post(URL, params, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				ToastUtils.TextToast(TimeSelectorActivity.this, getResources().getString(R.string.fuwuqilianjieyichang));
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
				switch (num) {
				case 3:
					startActivity(new Intent(TimeSelectorActivity.this,
							TimeChiyaoActivity.class));
					break;
				case 4:
					startActivity(new Intent(TimeSelectorActivity.this,
							TimeCeliangActivity.class));
					break;
				default:
					break;
				}
				prompt(TimeSelectorActivity.this.getResources().getString(R.string.shijiangengxinchenggong));
				finish();
			}

		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			switch (num) {
			case 1:
				startActivity(new Intent(TimeSelectorActivity.this,
						TimeChiyaoActivity.class));
				break;
			case 2:
				startActivity(new Intent(TimeSelectorActivity.this,
						TimeCeliangActivity.class));
				break;
			case 3:
				startActivity(new Intent(TimeSelectorActivity.this,
						TimeChiyaoActivity.class));
				break;
			case 4:
				startActivity(new Intent(TimeSelectorActivity.this,
						TimeCeliangActivity.class));
				break;

			default:
				break;
			}
			break;

		default:
			break;

		}
		finish();
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		title = null;
		back = null;
		search_rightLayout = null;
		search_right_txtView = null;
		hour_pv = null;
		minute_pv = null;
		time_btn_delete = null;
		updateTime = null;
		userId = null;
		gson = null;
		fh = null;
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}
}
