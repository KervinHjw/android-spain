package com.en.scian.myfriend;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.en.scian.BaseActivity;
import com.en.scian.ExampleApplication;
import com.en.scian.R;
import com.en.scian.adapter.SheQuShengHuoPagerAdapter;
import com.en.scian.myfriend.fragment.XueYaHistoryChartFragment;
import com.en.scian.myfriend.fragment.XueYaHistoryDataFragment;
import com.en.scian.view.NoScrollViewPager;
import com.en.scian.xueya.ScreeningDataActivity;

/**
 * 好友历史数据
 * 
 * @author jiyx
 * 
 */
public class MyFriendHistoryDataActivity extends BaseActivity implements
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
	private ImageView search_right_img;
	/**
	 * 主要的view
	 */
	private RadioGroup group;
	/**
	 * 数据
	 */
	private RadioButton xueya_history_shuju;
	/**
	 * 趋势图
	 */
	private RadioButton xueya_history_qushitu;

	private NoScrollViewPager content;
	private SheQuShengHuoPagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_content_fragment_history2);
		init();
		getViewPager();
		setIsHuaDong(false);
	}

	private void init() {
		ExampleApplication.getInstance().addActivity(this);
		search_leftLayout = (LinearLayout) this
				.findViewById(R.id.search_leftLayout);
		search_leftLayout.setOnClickListener(this);
		search_rightLayout = (LinearLayout) this
				.findViewById(R.id.search_rightLayout);
		search_titleText = (TextView) this.findViewById(R.id.search_titleText);
		search_right_img = (ImageView) this.findViewById(R.id.search_right_img);
		search_titleText.setText(this.getResources().getString(R.string.haoyoulishixueyashuju));
		search_rightLayout.setOnClickListener(this);
		search_right_img.setVisibility(View.VISIBLE);
		search_right_img.setBackgroundResource(R.drawable.shaixuan);

		group = (RadioGroup) this.findViewById(R.id.task_selector_rg);
		xueya_history_shuju = (RadioButton) this
				.findViewById(R.id.xueya_history_shuju);
		xueya_history_qushitu = (RadioButton) this
				.findViewById(R.id.xueya_history_qushitu);

	}

	/**
	 * viewpager的初始化
	 */
	private void getViewPager() {
		group.setOnCheckedChangeListener(new MyOnCheckChangeListner());
		xueya_history_shuju.setChecked(true);
		final ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(new XueYaHistoryDataFragment());
		fragments.add(new XueYaHistoryChartFragment());
		content = (NoScrollViewPager) findViewById(R.id.content);
		adapter = new SheQuShengHuoPagerAdapter(getSupportFragmentManager(),
				fragments);
		content.setAdapter(adapter);
		content.setCurrentItem(0);
		content.setNoScroll(false);
		content.setOnPageChangeListener(new MyOnPageChangeListener());

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.search_leftLayout:
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			finish();
			break;
		case R.id.search_rightLayout:
			Intent intent = new Intent(this, ScreeningDataActivity.class);
			intent.putExtra("isFriend", true);
			intent.putExtra("myFriendId",
					getIntent().getStringExtra("myFriendId"));
			intent.putExtra("bundle", getIntent().getBundleExtra("bundle"));
			startActivity(intent);
			break;
		default:
			break;
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
				xueya_history_shuju.setChecked(true);
				break;
			case 1:
				xueya_history_qushitu.setChecked(true);
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
			case R.id.xueya_history_shuju:
				content.setCurrentItem(0, false);
				break;
			case R.id.xueya_history_qushitu:
				content.setCurrentItem(1, false);
				break;

			default:
				break;
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		search_leftLayout = null;
		search_titleText = null;
		search_rightLayout = null;
		search_right_img = null;
		group = null;
		xueya_history_shuju = null;
		xueya_history_qushitu = null;
		content = null;
		adapter = null;
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}
}
