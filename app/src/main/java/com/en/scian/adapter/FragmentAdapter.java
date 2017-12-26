package com.en.scian.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * fragment分页面适配器
 * @author zhangp
 *
 */
public class FragmentAdapter extends FragmentPagerAdapter{

	private ArrayList<Fragment> fragments;
	FragmentManager fm;
	public FragmentAdapter(FragmentManager fm,ArrayList<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
		this.fm = fm;
	}
	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}
	@Override
	public int getCount() {
		return fragments.size();
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO 
		super.destroyItem(container, position, object);
		container.removeView(fragments.get(position).getView());
	}

	@Override
	public Object instantiateItem(View container, int position) {
		// TODO 
		return fragments.get(position);
	}

}
