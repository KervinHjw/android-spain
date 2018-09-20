package com.ludees.scian.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * 侧滑删除
 * 
 * @author jiyx
 * 
 */
public class SlidingDelete extends HorizontalScrollView {

	/** 屏幕宽度 **/
	private int screenWidth;

	/** 内容宽度 **/
	private int contentLayoutWidth;

	/** 删除按钮宽度 **/
	private int deleteLayoutWidth;

	/** 内容布局 **/
	private ViewGroup layout_content;
	/** 删除布局 **/
	private ViewGroup layout_delete;

	public SlidingDelete(Context context) {
		this(context, null, 0);
	}

	public SlidingDelete(Context context, AttributeSet attrs) {

		this(context, attrs, 0);
	}

	public SlidingDelete(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		if (!once) {
			LinearLayout wrapper = (LinearLayout) getChildAt(0);

			layout_content = (ViewGroup) wrapper.getChildAt(0);
			layout_delete = (ViewGroup) wrapper.getChildAt(1);

			layout_content.getLayoutParams().width = screenWidth;
			layout_delete.getLayoutParams().width = screenWidth / 3;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	private boolean once;

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			// 将删除布局隐藏
			this.scrollTo(0, 0);
			once = true;
		}
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent ev) {
	//
	// switch (ev.getAction()) {
	// case MotionEvent.ACTION_UP:
	//
	// //getScrollX，始终是x轴0到目前左上角的距离
	// int scrollX = getScrollX();
	//
	// //如果距离大于屏幕宽度的四分之一，显示删除布局
	// if(scrollX>(screenWidth/4)){
	// smoothScrollTo((screenWidth/3), 0);
	// }else{
	// smoothScrollTo(0, 0);
	// }
	// return true;
	// }
	//
	// return super.onTouchEvent(ev);
	// }

}
