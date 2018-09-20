package com.ludees.scian.main.fragment;

import java.io.InputStream;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.ludees.scian.R;
import com.ludees.scian.adapter.SheQuShengHuoPagerAdapter;
import com.ludees.scian.expertadvice.RichangFragment;
import com.ludees.scian.expertadvice.XuetangFragment;
import com.ludees.scian.expertadvice.XueyaFragment;
import com.ludees.scian.view.NoScrollViewPager;

/**
 * 健康资讯
 * 
 * @author jiyx
 * 
 */
@SuppressLint("InflateParams")
public class HealthInfoFragment extends Fragment {
	private RadioGroup expert_advice_rg;
	private RadioButton home_expert_advice_rb_richang;
	private RadioButton home_expert_advice_rb_xueya;
	private RadioButton home_expert_advice_rb_xuetang;

	private FragmentManager fragmentManager;
	private boolean isFristIn = true;

	private int checkedNum = 0;
	private ImageView health_bg;
	private Bitmap btp;
	private NoScrollViewPager mTabPager;
	private SheQuShengHuoPagerAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_content_fragment_healthinfo,
				null);
		initViews(view);
		return view;
	}

	/**
	 * 初始化控件
	 * 
	 * @param view
	 */
	private void initViews(View view) {
		// TODO Auto-generated method stub
		expert_advice_rg = (RadioGroup) view
				.findViewById(R.id.home_expert_advice_rg);
		home_expert_advice_rb_richang = (RadioButton) view
				.findViewById(R.id.home_expert_advice_rb_richang);
		home_expert_advice_rb_xueya = (RadioButton) view
				.findViewById(R.id.home_expert_advice_rb_xueya);
		home_expert_advice_rb_xuetang = (RadioButton) view
				.findViewById(R.id.home_expert_advice_rb_xuetang);
		health_bg = (ImageView) view.findViewById(R.id.health_bg);
		InputStream is = this.getResources().openRawResource(
				R.drawable.xueya_history_data_bg);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = 2;
		btp = BitmapFactory.decodeStream(is, null, options);
		health_bg.setScaleType(ScaleType.FIT_XY);
		health_bg.setImageBitmap(btp);

		// 给ViewPager设置适配器
		final ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(new RichangFragment());
		fragments.add(new XueyaFragment());
		fragments.add(new XuetangFragment());
		fragmentManager = getChildFragmentManager();
		mTabPager = (NoScrollViewPager) view.findViewById(R.id.tabpager);
		adapter = new SheQuShengHuoPagerAdapter(fragmentManager, fragments);
		mTabPager.setAdapter(adapter);
		mTabPager.setOffscreenPageLimit(3);
		mTabPager.setNoScroll(true);

		initListener();
	}

	/**
	 * 监听事件
	 */
	private void initListener() {
		// TODO
		expert_advice_rg
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// FragmentTransaction transaction = fragmentManager
						// .beginTransaction();
						switch (checkedId) {
						case R.id.home_expert_advice_rb_richang:
							checkedNum = 0;
							// transaction.replace(R.id.fragment_container,
							// new RichangFragment());
							mTabPager.setCurrentItem(0, false);
							break;
						case R.id.home_expert_advice_rb_xueya:
							checkedNum = 1;
							// transaction.replace(R.id.fragment_container,
							// new XueyaFragment());
							mTabPager.setCurrentItem(1, false);
							break;
						case R.id.home_expert_advice_rb_xuetang:
							checkedNum = 2;
							// transaction.replace(R.id.fragment_container,
							// new XuetangFragment());
							mTabPager.setCurrentItem(2, false);
							break;
						default:
							mTabPager.setCurrentItem(0, false);
							break;
						}
						// transaction.addToBackStack(null);
						// transaction.commit();
					}
				});
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			int num = getActivity().getIntent().getIntExtra("expertadviceNum",
					0);
			if (num > 0) {
				checkedNum = num;
			}
			switch (checkedNum) {
			case 0:
				home_expert_advice_rb_richang.setChecked(true);
				if (isFristIn) {
					// FragmentTransaction transaction = fragmentManager
					// .beginTransaction();
					// transaction.replace(R.id.fragment_container,
					// new RichangFragment());
					// transaction.addToBackStack(null);
					// transaction.commit();
					mTabPager.setCurrentItem(0, false);
				}
				break;
			case 1:
				home_expert_advice_rb_xueya.setChecked(true);
				break;
			case 2:
				home_expert_advice_rb_xuetang.setChecked(true);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		if (isFristIn) {
			// FragmentTransaction transaction = getChildFragmentManager()
			// .beginTransaction();
			// transaction.add(R.id.fragment_container, new RichangFragment());
			// transaction.commit();
			mTabPager.setCurrentItem(0, false);
			isFristIn = false;
		}
	}

	@Override
	public void onDestroyView() {
		// TODO
		expert_advice_rg = null;
		fragmentManager = null;
		home_expert_advice_rb_richang = null;
		home_expert_advice_rb_xueya = null;
		home_expert_advice_rb_xuetang = null;
		health_bg = null;
		btp = null;
		mTabPager = null;
		adapter = null;
		System.gc();
		super.onDestroyView();
	}
}
