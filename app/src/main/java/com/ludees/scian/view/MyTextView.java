package com.ludees.scian.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 修改字体
 * 
 */
public class MyTextView extends TextView {

	public MyTextView(Context context) {
		super(context);
	}

	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		changeTypeFace(context, attrs);
	}

	/**
	 * 改变字体类型
	 * 
	 * @param context
	 * @param attrs
	 */
	private void changeTypeFace(Context context, AttributeSet attrs) {
		if (attrs != null) {
			// TypedArray a = context.obtainStyledAttributes(attrs,
			// R.styleable.TextView_Typefaces);
			// tf = a.getInt(R.styleable.TextView_Typefaces_tf, tf);
			Typeface mtf = Typeface.createFromAsset(context.getAssets(),
					"fonts/Let's go Digital Regular.ttf");
			super.setTypeface(mtf);
		}
	}
}
