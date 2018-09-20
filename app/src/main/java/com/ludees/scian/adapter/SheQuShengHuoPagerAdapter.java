package com.ludees.scian.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class SheQuShengHuoPagerAdapter extends FragmentStatePagerAdapter {

	private ArrayList<Fragment> fragments;
	FragmentManager fm;

	public SheQuShengHuoPagerAdapter(FragmentManager fm,
			ArrayList<Fragment> fragments) {
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
		container.removeView(fragments.get(position).getView());
	}

	@Override
	public Object instantiateItem(View container, int position) {
		// TODO
		Fragment fragment = fragments.get(position);
		if (!fragment.isAdded()) { // 如果fragment还没有added
			android.support.v4.app.FragmentTransaction ft = fm
					.beginTransaction();
			ft.add(fragment, fragment.getClass().getSimpleName());
			ft.commit();
			/**
			 * 在用FragmentTransaction.commit()方法提交FragmentTransaction对象后
			 * 会在进程的主线程中，用异步的方式来执行。 如果想要立即执行这个等待中的操作，就要调用这个方法（只能在主线程中调用）。
			 * 要注意的是，所有的回调和相关的行为都会在这个调用中被执行完成，因此要仔细确认这个方法的调用位置。
			 */
			fm.executePendingTransactions();
		}

		if (fragment.getView().getParent() == null) {
			((ViewGroup) container).addView(fragment.getView()); //
			// 为viewpager增加布局
		}

		return fragment.getView();
	}

}
