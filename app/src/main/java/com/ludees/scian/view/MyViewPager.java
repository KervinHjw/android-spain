package com.ludees.scian.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

/**
 * 禁止滑动的viewpager
 * @author zhangp
 *
 */
public class MyViewPager extends ViewPager{

	public MyViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
            // TODO Auto-generated method stub
            return true;
    }
	
	
	
}
