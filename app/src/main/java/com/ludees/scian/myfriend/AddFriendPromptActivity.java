package com.ludees.scian.myfriend;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ludees.scian.BaseActivity;
import com.ludees.scian.ExampleApplication;
import com.ludees.scian.R;

/**
 * 添加好友成功与否友好提示
 * 
 * @author jiyx
 * 
 */
public class AddFriendPromptActivity extends BaseActivity implements
		OnClickListener {

	private TextView title;
	private LinearLayout back;
	private ImageView myfriend_add_prompt_img;
	private TextView myfriend_add_prompt_text;
	private Button myfriend_add_exit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myfriend_add_prompt);
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

		myfriend_add_prompt_img = (ImageView) findViewById(R.id.myfriend_add_prompt_img);
		myfriend_add_prompt_text = (TextView) findViewById(R.id.myfriend_add_prompt_text);
		myfriend_add_exit = (Button) findViewById(R.id.myfriend_add_exit);
	}

	/**
	 * 设置初始化数据
	 */
	private void setData() {
		title.setText(getResources().getString(R.string.home_content_fragment_myfriend_add_friend));

		String msg = getIntent().getStringExtra("msg");
		myfriend_add_prompt_text.setText(msg);
		int flags = getIntent().getFlags();
		if (flags == 0) {
			myfriend_add_prompt_img
					.setImageResource(R.drawable.myfriend_add_friend_failure);
			// myfriend_add_prompt_text.setText(msg);
		} else if (flags == 1) {
			myfriend_add_prompt_img
					.setImageResource(R.drawable.myfriend_add_friend_success);
		}

	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		back.setOnClickListener(this);
		myfriend_add_exit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_leftLayout:// 返回
			// startActivity(new Intent(this, AddFriendActivity.class));
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			finish();
			break;
		case R.id.myfriend_add_exit:// 关闭页面
			finish();
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		title = null;
		back = null;
		myfriend_add_prompt_img = null;
		myfriend_add_prompt_text = null;
		myfriend_add_exit = null;
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}

}
