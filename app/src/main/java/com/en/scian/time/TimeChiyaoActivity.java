package com.en.scian.time;

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

import com.en.scian.BaseActivity;
import com.en.scian.ExampleApplication;
import com.en.scian.R;
import com.en.scian.entity.RemindInfo;
import com.en.scian.entity.RemindInfoBean;
import com.en.scian.entity.ResponseCommon;
import com.en.scian.network.Urls;
import com.en.scian.util.SettingUtils;
import com.en.scian.util.ToastUtils;
import com.google.gson.Gson;

/**
 * 吃药提醒
 * 
 * @author jiyx
 * 
 */
public class TimeChiyaoActivity extends BaseActivity implements OnClickListener {
	private TextView title;
	private LinearLayout back;

	private LinearLayout search_rightLayout;
	private TextView search_right_txtView;
	private ListView time_alertset_zlv;

	private TimeAdapter timeAdapter;
	/**
	 * 我的数据
	 */
	private Gson gson;
	/**
	 * 我的时间列表
	 */
	private List<RemindInfo> list;
	private String userId;
	private FinalHttp fh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_alertset);
		initViews();
		setData();
		initListener();
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
			} else {
				startActivity(new Intent(this, TimeSelectorActivity.class)
						.putExtra("num", 1));
				finish();
			}

			break;
		}

	}

	/**
	 * 获取吃药时间列表
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getData(String URL) {
		fh.get(URL, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				ToastUtils.TextToast(TimeChiyaoActivity.this, getResources().getString(R.string.fuwuqilianjieyichang));
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
					prompt(TimeChiyaoActivity.this.getResources().getString(R.string.zanwushuju));
				}
				timeAdapter.notifyDataSetChanged();
			}

		});
	}

	@Override
	protected void onStart() {
		// TODO
		list = new ArrayList<RemindInfo>();
		timeAdapter = new TimeAdapter(TimeChiyaoActivity.this, list);
		time_alertset_zlv.setAdapter(timeAdapter);
		userId = SettingUtils.get(TimeChiyaoActivity.this, "userId", "");
		String URL = Urls.GET_REMIND_LIST
				+ "pageNo=0&pageSize=10&remindType=1&userId=" + userId;
		getData(URL);
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		title = null;
		back = null;
		search_rightLayout = null;
		search_right_txtView = null;
		time_alertset_zlv = null;
		timeAdapter = null;
		gson = null;
		list = null;
		userId = null;
		fh = null;
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}
}
