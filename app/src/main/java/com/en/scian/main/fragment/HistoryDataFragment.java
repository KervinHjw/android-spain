package com.en.scian.main.fragment;

import java.io.InputStream;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.en.scian.R;
import com.en.scian.adapter.SheQuShengHuoPagerAdapter;
import com.en.scian.view.NoScrollViewPager;
import com.en.scian.view.SlidingMenu;
import com.en.scian.xueya.ScreeningDataActivity;

/**
 * 历史数据
 * 
 * @author jiyx
 * 
 */
public class HistoryDataFragment extends Fragment implements OnClickListener {
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

	private SheQuShengHuoPagerAdapter adapter;
	private NoScrollViewPager mTabPager;
	private SlidingMenu menu;
	private ImageView histroy_bg;
	private Bitmap btp;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_content_fragment_history,
				null);
		init(view);
		getViewPager(view);
		return view;
	}

	private void init(View view){
		search_leftLayout = (LinearLayout) view
				.findViewById(R.id.search_leftLayout);
		search_titleText = (TextView) view.findViewById(R.id.search_titleText);
		search_rightLayout = (LinearLayout) view
				.findViewById(R.id.search_rightLayout);
		search_right_img = (ImageView) view.findViewById(R.id.search_right_img);
		search_rightLayout.setOnClickListener(this);
		search_titleText.setText(this.getResources().getString(R.string.lishixueyashuju));
		search_right_img.setVisibility(View.VISIBLE);
		search_right_img.setBackgroundResource(R.drawable.shaixuan);
		search_leftLayout.setVisibility(View.GONE);
		menu = (SlidingMenu) getActivity().findViewById(R.id.id_menu);
		group = (RadioGroup) view.findViewById(R.id.task_selector_rg);
		xueya_history_shuju = (RadioButton) view
				.findViewById(R.id.history_xueya_history_shuju);
		xueya_history_qushitu = (RadioButton) view
				.findViewById(R.id.xueya_history_qushitu);
		histroy_bg = (ImageView) view.findViewById(R.id.histroy_bg);
		InputStream is = this.getResources().openRawResource(
				R.drawable.xueya_history_data_bg);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = 2;
		btp = BitmapFactory.decodeStream(is, null, options);
		histroy_bg.setScaleType(ScaleType.FIT_XY);
		histroy_bg.setImageBitmap(btp);
	}

	/**
	 * viewpager的初始化
	 * 
	 * @param view
	 */
	private void getViewPager(View view) {
		group.setOnCheckedChangeListener(new MyOnCheckChangeListner());
		// group.check(R.id.history_xueya_history_shuju);
		xueya_history_shuju.setChecked(true);

		// one = new XueYaHistoryDataFragment();
		// if (first) {
		// FragmentTransaction transaction = getChildFragmentManager()
		// .beginTransaction();
		// transaction.add(R.id.content, one);
		// transaction.commit();
		// first = false;
		// }

		final ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(new XueYaHistoryDataFragment());
		fragments.add(new XueYaHistoryChartFragment());

		mTabPager = (NoScrollViewPager) view
				.findViewById(R.id.history_tabpager);
		adapter = new SheQuShengHuoPagerAdapter(getChildFragmentManager(),
				fragments);
		mTabPager.setAdapter(adapter);
		mTabPager.setCurrentItem(0);
		mTabPager.setNoScroll(true);
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.search_rightLayout:
			Intent intent = new Intent(getActivity(),
					ScreeningDataActivity.class);
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
				// menu.setScroll(false);
				break;
			case 1:
				xueya_history_qushitu.setChecked(true);
				// menu.setScroll(false);
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

		@SuppressWarnings("static-access")
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.history_xueya_history_shuju:
				mTabPager.setCurrentItem(0, false);

				menu.isOpen = true;
				menu.closeMenuNosmooth();
				menu.setScroll(false);
				break;
			case R.id.xueya_history_qushitu:
				mTabPager.setCurrentItem(1, false);

				break;

			default:
				break;
			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO
		search_leftLayout = null;
		search_titleText = null;
		search_rightLayout = null;
		search_right_img = null;
		group = null;
		xueya_history_shuju = null;
		xueya_history_qushitu = null;
		System.gc();
		super.onDestroy();
	}
}
