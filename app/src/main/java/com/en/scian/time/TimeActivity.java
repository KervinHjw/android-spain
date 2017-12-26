package com.en.scian.time;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.en.scian.BaseActivity;
import com.en.scian.ExampleApplication;
import com.en.scian.R;

/**
 * 时间提醒
 * 
 * @author jiyx
 * 
 */
public class TimeActivity extends BaseActivity implements OnClickListener {
	private TextView title;
	private LinearLayout back;
	private RelativeLayout time_chiyao;
	private RelativeLayout time_celiang;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time);
		initViews();
		initListener();
		setData();
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		ExampleApplication.getInstance().addActivity(this);
		title = (TextView) findViewById(R.id.search_titleText);
		back = (LinearLayout) findViewById(R.id.search_leftLayout);
		time_chiyao = (RelativeLayout) findViewById(R.id.time_chiyao);
		time_celiang = (RelativeLayout) findViewById(R.id.time_celiang);
	}

	/**
	 * 设置初始化数据
	 */
	private void setData() {
		title.setText(this.getResources().getString(R.string.timeactivity_settime_remind));
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		back.setOnClickListener(this);
		time_chiyao.setOnClickListener(this);
		time_celiang.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_leftLayout:// 返回
			finish();
			break;
		case R.id.time_chiyao:// 返回
			startActivity(new Intent(this, TimeChiyaoActivity.class));
			break;
		case R.id.time_celiang:// 返回
			startActivity(new Intent(this, TimeCeliangActivity.class));
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		title = null;
		back = null;
		time_chiyao = null;
		time_celiang = null;
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}
}
