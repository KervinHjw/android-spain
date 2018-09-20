package com.ludees.scian.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;
/**
 * 解决scrollView和其他滑动控件冲突
 * @author jiyx
 *
 */
public class CoustomScrollView extends ScrollView {

	private float xDistance;
	private float yDistance;
	private float xStart;
	private float yStart;
	private float xEnd;
	private float yEnd;

	public CoustomScrollView(Context context) {
		super(context);
	}

	public CoustomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDistance = yDistance = 0f;
			xStart = ev.getX();
			yStart = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			xEnd = ev.getX();
			yEnd = ev.getY();
			break;
		default:
			break;
		}
		xDistance = Math.abs(xEnd - xStart);
		yDistance = Math.abs(yEnd - yStart);
		if (xDistance > yDistance)
			return false;
		return super.onInterceptTouchEvent(ev);
	}
}
