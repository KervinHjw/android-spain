package com.ludees.scian.time;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ludees.scian.BaseActivity;
import com.ludees.scian.ExampleApplication;
import com.ludees.scian.R;
import com.ludees.scian.entity.RemindInfo;
import com.ludees.scian.entity.RemindInfoBean;
import com.ludees.scian.entity.ResponseCommon;
import com.ludees.scian.network.Urls;
import com.ludees.scian.util.SettingUtils;
import com.ludees.scian.util.ToastUtils;
import com.google.gson.Gson;

/**
 * 测量提醒
 * 
 * @author jiyx
 * 
 */
public class TimeCeliangActivity extends BaseActivity implements
		OnClickListener {
	private TextView title;
	private LinearLayout back;

	private LinearLayout search_rightLayout;
	private TextView search_right_txtView;
	private ListView time_alertset_zlv;
	private Gson gson;
	/**
	 * 我的时间列表
	 */
	private List<RemindInfo> list;
	private String userId;
	private TimeAdapter2 timeAdapter;
	private FinalHttp fh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_alertset);
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

		time_alertset_zlv = (ListView) findViewById(R.id.time_alertset_zlv);
	}

	/**
	 * 设置初始化数据
	 */
	private void setData() {
		title.setText(this.getResources().getString(R.string.tixingshezhi));
		search_right_txtView.setText(this.getResources().getString(R.string.xinzeng));
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		back.setOnClickListener(this);
		search_rightLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_leftLayout:// 返回
			finish();
			break;
		case R.id.search_rightLayout:// 新增
			if (list.size() >= 10) {
				prompt(this.getResources().getString(R.string.tixingshijianbunengchaoguoshitiao));
				return;
			} else {
				startActivity(new Intent(this, TimeSelectorActivity.class)
						.putExtra("num", 2));
				finish();
			}
			break;
		}

	}

	@Override
	protected void onStart() {
		list = new ArrayList<RemindInfo>();
		timeAdapter = new TimeAdapter2(TimeCeliangActivity.this, list);
		time_alertset_zlv.setAdapter(timeAdapter);
		userId = SettingUtils.get(TimeCeliangActivity.this, "userId", "");
		String URL = Urls.GET_REMIND_LIST
				+ "pageNo=0&pageSize=10&remindType=2&userId=" + userId;
		getData(URL);
		super.onStart();
	}

	/**
	 * 获取测量时间列表
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void getData(String URL) {
		fh.get(URL, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				ToastUtils.TextToast(TimeCeliangActivity.this, getResources().getString(R.string.fuwuqilianjieyichang));
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
				// list = new ArrayList<RemindInfo>();
				RemindInfoBean info = gson.fromJson(content,
						RemindInfoBean.class);
				List<RemindInfo> list2 = new ArrayList<RemindInfo>();
				list2 = info.getData();
				list.clear();
				list.addAll(list2);
				if (list.size() == 0) {
					prompt(TimeCeliangActivity.this.getResources().getString(R.string.zanwushuju));
				}
				timeAdapter.notifyDataSetChanged();
			}

		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		title = null;
		back = null;
		search_rightLayout = null;
		search_right_txtView = null;
		time_alertset_zlv = null;
		gson = null;
		list = null;
		userId = null;
		timeAdapter = null;
		fh = null;
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}
}
