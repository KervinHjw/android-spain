package com.en.scian.astart;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.ab.http.AbHttpUtil;
import com.en.scian.BaseActivity;
import com.en.scian.R;
import com.en.scian.adapter.WelcomeViewPagerAdapter;

/**
 * @Description:引导页
 * @author zhaoW
 * 
 */
public class GuideActivity extends BaseActivity implements OnPageChangeListener {

	private ViewPager vp;
	private WelcomeViewPagerAdapter vpAdapter;
	private List<View> views;
	ArrayList<String> list = new ArrayList<String>();

	private AbHttpUtil mAbHttpUtil = null;
//	private static final String TAG = "GuideActivity";

	// @Override
	// protected void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	// setContentView(R.layout.activity_guide);
	// mAbHttpUtil = AbHttpUtil.getInstance(this);
	// mAbHttpUtil.setTimeout(10000);
	// list.add("http://p0.so.qhimg.com/bdr/288__/t01553cd077c39f570f.jpg");
	// list.add("http://p3.so.qhimg.com/bdr/_240_/t0115bed124c96462bd.jpg");
	// list.add("http://p4.so.qhimg.com/bdr/_240_/t0191c65c01b9ac0814.jpg");
	// list.add("http://p3.so.qhimg.com/bdr/288__/t01439c37412a4b7f28.jpg");
	// initViews();
	// }
	//
	// private void initViews() {
	// LayoutInflater inflater = LayoutInflater.from(this);
	// AbImageLoader mAbImageLoader = AbImageLoader.newInstance(this);
	//
	// views = new ArrayList<View>();
	// for (int i = 0; i < list.size(); i++) {
	// if (i == list.size() - 1) {
	// View view = inflater.inflate(R.layout.guide_last, null);
	// ImageView imageView = (ImageView) view
	// .findViewById(R.id.view_last);
	// mAbImageLoader.display(imageView, list.get(i));
	// views.add(view);
	// } else {
	// View view = inflater.inflate(R.layout.guide_no_last, null);
	// ImageView imageView = (ImageView) view.findViewById(R.id.view);
	// mAbImageLoader.display(imageView, list.get(i));
	// views.add(view);
	// }
	// }
	//
	// vpAdapter = new WelcomeViewPagerAdapter(views, this);
	//
	// vp = (ViewPager) findViewById(R.id.viewpager);
	// vp.setAdapter(vpAdapter);
	// vp.setOnPageChangeListener(this);
	// }
	//
	// @Override
	// public void onPageScrollStateChanged(int arg0) {
	// }
	//
	// @Override
	// public void onPageScrolled(int arg0, float arg1, int arg2) {
	// }
	//
	// @Override
	// public void onPageSelected(int arg0) {
	// }
	//
	// /**
	// * @param stringurl
	// * 登陆网络请求
	// */
	// private void getYinDaoYe(String stringurl) {
	// mAbHttpUtil.get(stringurl, new AbStringHttpResponseListener() {
	//
	// // 获取数据成功会调用这里
	// @Override
	// public void onSuccess(int statusCode, String content) {
	// Log.d(TAG, "onSuccess");
	// list.add("http://p0.so.qhimg.com/bdr/288__/t01553cd077c39f570f.jpg");
	// list.add("http://p3.so.qhimg.com/bdr/_240_/t0115bed124c96462bd.jpg");
	// list.add("http://p4.so.qhimg.com/bdr/_240_/t0191c65c01b9ac0814.jpg");
	// list.add("http://p3.so.qhimg.com/bdr/288__/t01439c37412a4b7f28.jpg");
	//
	// if (list.size()==0) {
	// //直接跳转到下一界面
	// startActivity(new Intent(GuideActivity.this, MainActivity.class));
	// finish();
	// }
	// initViews();
	// }
	//
	// // 失败，调用
	// @Override
	// public void onFailure(int statusCode, String content,
	// Throwable error) {
	// Log.d(TAG, "onFailure");
	// }
	//
	// // 开始执行前
	// @Override
	// public void onStart() {
	// Log.d(TAG, "onStart");
	// }
	//
	// // 完成后调用，失败，成功
	// @Override
	// public void onFinish() {
	// Log.d(TAG, "onFinish");
	// };
	// });
	// }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		mAbHttpUtil = AbHttpUtil.getInstance(this);
		mAbHttpUtil.setTimeout(10000);
		setIsHuaDong(false);
		initViews();
	}

	@SuppressLint("InflateParams")
	private void initViews() {
		LayoutInflater inflater = LayoutInflater.from(this);

		views = new ArrayList<View>();
		for (int i = 0; i < 3; i++) {
			if (i == 2) {
				View view = inflater.inflate(R.layout.guide_last, null);
				ImageView imageView = (ImageView) view
						.findViewById(R.id.view_last);
				imageView.setImageResource(R.drawable.lead3);
				views.add(view);
			} else if (i == 0) {
				View view = inflater.inflate(R.layout.guide_no_last, null);
				ImageView imageView = (ImageView) view.findViewById(R.id.view);
				imageView.setImageResource(R.drawable.lead1);
				views.add(view);
			} else if (i == 1) {
				View view = inflater.inflate(R.layout.guide_no_last, null);
				ImageView imageView = (ImageView) view.findViewById(R.id.view);
				imageView.setImageResource(R.drawable.lead2);

				views.add(view);
			}
		}

		vpAdapter = new WelcomeViewPagerAdapter(views, this);

		vp = (ViewPager) findViewById(R.id.viewpager);
		vp.setAdapter(vpAdapter);
		vp.setOnPageChangeListener(this);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
	}

}