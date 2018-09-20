package com.ludees.scian.xueya;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.ludees.scian.ExampleApplication;
import com.ludees.scian.R;
import com.ludees.scian.adapter.SheQuShengHuoPagerAdapter;
import com.ludees.scian.util.BlueToothUtil;
import com.ludees.scian.util.ScreenShot_ldd;
import com.ludees.scian.util.SettingUtils;
import com.ludees.scian.view.NoScrollViewPager;
import com.ludees.scian.xueya.fragment.XueYaChartFragment;
import com.ludees.scian.xueya.fragment.XueYaDataFragment;
import com.ludees.scian.xueya.fragment.XueYaTrendChartFragment;

/**
 * 血压测量结果
 * 
 * @author zhangp
 * 
 */
public class XueYaCeLiangDataActivity extends FragmentActivity implements
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
	private RadioButton xueya_shuju;
	/**
	 * 图表
	 */
	private RadioButton xueya_tubiao;
	/**
	 * 趋势图
	 */
	private RadioButton xueya_qushitu;
	private NoScrollViewPager mTabPager;
	private SheQuShengHuoPagerAdapter adapter;
	private BlueToothUtil util;
	private boolean isceLiang = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_xueya_celiang_data);
		init();
		getViewPager();
	}

	private void init() {
		ExampleApplication.getInstance().addActivity(this);
		search_leftLayout = (LinearLayout) this
				.findViewById(R.id.search_leftLayout);
		search_titleText = (TextView) this.findViewById(R.id.search_titleText);
		search_rightLayout = (LinearLayout) this
				.findViewById(R.id.search_rightLayout);
		search_right_img = (ImageView) this.findViewById(R.id.search_right_img);
		search_rightLayout.setOnClickListener(this);
		search_leftLayout.setOnClickListener(this);
		search_titleText.setText(getResources().getString(R.string.xueyaceliangdataactivity_xycljs));
		search_right_img.setVisibility(View.GONE);
		search_right_img.setBackgroundResource(R.drawable.share);

		group = (RadioGroup) this.findViewById(R.id.task_selector_rg);
		xueya_shuju = (RadioButton) this.findViewById(R.id.xueya_shuju);
		xueya_tubiao = (RadioButton) this.findViewById(R.id.xueya_tubiao);
		xueya_qushitu = (RadioButton) this.findViewById(R.id.xueya_qushitu);
		isceLiang = getIntent().getBooleanExtra("celiang", false);
	}

	/**
	 * viewpager的初始化
	 */
	private void getViewPager() {
		group.setOnCheckedChangeListener(new MyOnCheckChangeListner());
		group.check(R.id.xueya_shuju);
		// one = new XueYaDataFragment();
		// if (first) {
		// FragmentTransaction transaction = getSupportFragmentManager()
		// .beginTransaction();
		// transaction.add(R.id.content, one);
		// transaction.commit();
		// first = false;
		// }
		final ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(new XueYaDataFragment());
		fragments.add(new XueYaChartFragment());
		fragments.add(new XueYaTrendChartFragment());

		mTabPager = (NoScrollViewPager) findViewById(R.id.tabpager);
		adapter = new SheQuShengHuoPagerAdapter(getSupportFragmentManager(),
				fragments);
		mTabPager.setAdapter(adapter);
		mTabPager.setOffscreenPageLimit(3);
		mTabPager.setCurrentItem(0);
//		mTabPager.setNoScroll(true);
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
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
				xueya_shuju.setChecked(true);
				xueya_tubiao.setChecked(false);
				xueya_qushitu.setChecked(false);
				break;
			case 1:
				xueya_shuju.setChecked(false);
				xueya_tubiao.setChecked(true);
				xueya_qushitu.setChecked(false);
				break;
			case 2:
				xueya_shuju.setChecked(false);
				xueya_tubiao.setChecked(false);
				xueya_qushitu.setChecked(true);
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
			// FragmentTransaction transaction = getSupportFragmentManager()
			// .beginTransaction();
			switch (checkedId) {
			case R.id.xueya_shuju:
				mTabPager.setCurrentItem(0, false);
				// if (one == null) {
				// one = new XueYaDataFragment();
				// }
				// transaction.replace(R.id.content, one);
				// transaction
				// .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				// transaction.commit();
				break;
			case R.id.xueya_tubiao:
				mTabPager.setCurrentItem(1, false);
				// if (two == null) {
				// two = new XueYaChartFragment();
				// }
				// transaction.replace(R.id.content, two);
				// transaction
				// .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				// transaction.commit();
				break;
			case R.id.xueya_qushitu:
				mTabPager.setCurrentItem(2, false);
				// if (three == null) {
				// three = new XueYaTrendChartFragment();
				// }
				// transaction.replace(R.id.content, three);
				// transaction
				// .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				// transaction.commit();
				break;
			default:
				break;
			}
		}

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 111){
			if(resultCode == -1){
				if(util == null){
					util = new BlueToothUtil(this);
				}
				util.setIsRefuse(true);
				util.setActivity(this);
				util.setBlueTooth();
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.search_leftLayout:
			SettingUtils.set(this, "isFirstIn", "1");
			if(isceLiang){
				SettingUtils.set(this, "myFriendData", "1");
				SettingUtils.set(this, "isFirstChart", "1");
				SettingUtils.set(this, "isFirstTrendChart", "1");
				SettingUtils.set(this, "isFirstFriendChart", "1");
				SettingUtils.set(this, "isFirstFriendData", "1");
			}
	
//			 startActivity(new Intent(XueYaCeLiangDataActivity.this,
//					 HomeActivity.class).putExtra("start", false));
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			finish();
			break;
		case R.id.search_rightLayout:
			//showShare();
			break;
		default:
			break;
		}
	}

	/*private void showShare() {
		String filePath = Environment.getExternalStorageDirectory() + "/DCIM/fenxiang.png";
		ScreenShot_ldd.shoot(this, new File(filePath));
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数w
		oks.setImagePath(filePath);// 确保SDcard下面存在此张图片
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setText("");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		oks.show(this);

	}*/

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			SettingUtils.set(this, "isFirstIn", "1");
			if(isceLiang){
				SettingUtils.set(this, "myFriendData", "1");
				SettingUtils.set(this, "isFirstChart", "1");
				SettingUtils.set(this, "isFirstTrendChart", "1");
				SettingUtils.set(this, "isFirstFriendChart", "1");
				SettingUtils.set(this, "isFirstFriendData", "1");
			}
			 overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
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
		super.onDestroy();
		util = null;
		setContentView(R.layout.xml_null);
		System.gc();
		
	}
}
