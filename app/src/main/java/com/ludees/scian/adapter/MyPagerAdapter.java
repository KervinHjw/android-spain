package com.ludees.scian.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * viewPager的适配器
 * 
 * @author zhangp
 * 
 */
public class MyPagerAdapter extends PagerAdapter {

	private List<View> mViews;

	public MyPagerAdapter(List<View> mViews) {
		this.mViews = mViews;
	}

	@Override
	public int getCount() {
		if (mViews != null) {
			return mViews.size();
		}
		return 0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(mViews.get(position));

	}

	/**
	 * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
	 */
	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(mViews.get(position), 0);
		return mViews.get(position);
	}

}
