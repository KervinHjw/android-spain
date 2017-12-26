package com.en.scian.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.en.scian.R;
import com.en.scian.login.LoginActivity;

/**
 * @Description:引导页适配器
 * @author zhaoW
 *
 */
public class WelcomeViewPagerAdapter extends PagerAdapter {

	private List<View> mViews;
	private Activity mActivity;

	private static final String SHAREDPREFERENCES_NAME = "first_pref";

	public WelcomeViewPagerAdapter(List<View> mViews, Activity mActivity) {
		this.mViews = mViews;
		this.mActivity = mActivity;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(mViews.get(arg1));
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	@Override
	public int getCount() {
		if (mViews != null) {
			return mViews.size();
		}
		return 0;
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(mViews.get(arg1), 0);

		if (arg1 == mViews.size() - 1) {
			View mStartWeiboImageButton = (View) arg0
					.findViewById(R.id.iv_start_weibo);
			mStartWeiboImageButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setGuided();
					goHome();
				}
			});
		}else if(arg1 ==0||arg1 == 1){
			View mStartWeiboImageButton2 = (View) arg0
					.findViewById(R.id.iv_tiaoguo);
			mStartWeiboImageButton2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setGuided();
					goHome();
				}
			});
		}
		return mViews.get(arg1);
	}

	/**
	 * 跳转到主界面
	 */
	private void goHome() {
		Intent intent = new Intent(mActivity, LoginActivity.class);
		mActivity.startActivity(intent);
		mActivity.finish();
	}

	/**
	 * 设置引导界面的存在有否的共享参数
	 */
	private void setGuided() {
		SharedPreferences preferences = mActivity.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("isFirstIn", false);
		editor.commit();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
	}

}