package com.ludees.scian.xueya;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ludees.scian.BaseActivity;
import com.ludees.scian.ExampleApplication;
import com.ludees.scian.R;
import com.ludees.scian.adapter.MyPagerAdapter;

/**
 * 血压测量指导
 * 
 * @author zhangp
 * 
 */
public class XueYaCeLiangZhiDaoActivity extends BaseActivity implements
		OnClickListener, OnPageChangeListener {
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
	 * 底部进入按钮
	 */
	private Button xueya_in;
	/**
	 * ViewPager
	 */
	private ViewPager vp;
	private List<View> views;
	private MyPagerAdapter vpAdapter;
	private Bitmap btp;
	private View view;
	private ImageView img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xuetangcelaingzhidao);
		init();
		initViews();
		setIsHuaDong(false);
	}

	private void init() {
		ExampleApplication.getInstance().addActivity(this);
		search_leftLayout = (LinearLayout) this
				.findViewById(R.id.search_leftLayout);
		search_titleText = (TextView) this.findViewById(R.id.search_titleText);
		search_rightLayout = (LinearLayout) this
				.findViewById(R.id.search_rightLayout);
		search_right_txtView = (TextView) this
				.findViewById(R.id.search_right_txtView);
		xueya_in = (Button) this.findViewById(R.id.xuetang_in);
		search_titleText.setText(getResources().getString(R.string.xueyaceliangzhidaoactivity_xyclzd));
		search_right_txtView.setText(getResources().getString(R.string.xueyaceliangzhidaoactivity_sdsr));
		search_leftLayout.setOnClickListener(this);
		xueya_in.setOnClickListener(this);
		search_rightLayout.setOnClickListener(this);
		search_right_txtView.setVisibility(View.VISIBLE);
		vp = (ViewPager) this.findViewById(R.id.viewPager);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.search_leftLayout:
			goBack(this);
			break;
		case R.id.xuetang_in:
			Intent intent = new Intent(XueYaCeLiangZhiDaoActivity.this,
					XueYaCeLiangActivity.class);
			if(getIntent().getBooleanExtra("bluetooth",false)){
				intent.putExtra("bundle", getIntent().getParcelableExtra("bundle"));
				intent.putExtra("bluetooth", true);
				startActivity(intent);
			}else{
				String name = getIntent().getStringExtra("name");
				String address = getIntent().getStringExtra("addresss");
				boolean quxiao = getIntent().getBooleanExtra("quxiao", false);
				intent.putExtra("address", address);
				intent.putExtra("name", name);
				intent.putExtra("quxiao", quxiao);
				intent.putExtra("bundle", getIntent().getParcelableExtra("bundle"));
				startActivity(intent);
			}
			finish();
			break;
		case R.id.search_rightLayout:
			Intent shoudong = new Intent(this,
					XueYaCeLiangShouDongActivity.class);
			startActivity(shoudong);
			finish();
			break;
		default:
			break;
		}
	}

	@SuppressLint("InflateParams")
	private void initViews() {
		LayoutInflater inflater = LayoutInflater.from(this);

		views = new ArrayList<View>();
		for (int i = 0; i < 6; i++) {
			switch (i) {
			case 0:
				view = inflater.inflate(R.layout.vp_item1, null);
				img = (ImageView) view.findViewById(R.id.view_1);
				setBackground(img, R.drawable.celiangzhidao1);
				views.add(view);
				break;
			case 1:
				view = inflater.inflate(R.layout.vp_item2, null);
				img = (ImageView) view.findViewById(R.id.view_2);
				setBackground(img, R.drawable.celiangzhidao2);
				views.add(view);
				break;
			case 2:
				view = inflater.inflate(R.layout.vp_item3, null);
				img = (ImageView) view.findViewById(R.id.view_3);
				setBackground(img, R.drawable.celiangzhidao3);
				views.add(view);
				break;
			case 3:
				view = inflater.inflate(R.layout.vp_item4, null);
				img = (ImageView) view.findViewById(R.id.view_4);
				setBackground(img, R.drawable.celiangzhidao4);
				views.add(view);
				break;
			case 4:
				view = inflater.inflate(R.layout.vp_item5, null);
				img = (ImageView) view.findViewById(R.id.view_5);
				setBackground(img, R.drawable.celiangzhidao5);
				views.add(view);
				break;
			default:
				break;
			}

		}

		vpAdapter = new MyPagerAdapter(views);
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

	/**
	 * 设置背景图片
	 * 
	 * @param view
	 * @param id
	 */
	private void setBackground(ImageView view, int id) {
		InputStream is = this.getResources().openRawResource(id);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = 2;
		btp = BitmapFactory.decodeStream(is, null, options);
		view.setScaleType(ScaleType.FIT_XY);
		view.setImageBitmap(btp);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		btp.recycle();
		search_leftLayout = null;
		search_titleText = null;
		search_rightLayout = null;
		search_right_txtView = null;
		xueya_in = null;
		vp = null;
		views = null;
		vpAdapter = null;
		btp = null;
		view = null;
		img = null;
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();

	}
}
