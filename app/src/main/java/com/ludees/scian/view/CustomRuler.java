package com.ludees.scian.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.ludees.scian.R;


/**
 * 卷尺控件类
 * 
 * @author jiyx
 */
public class CustomRuler extends View {
	// 两长竖线间短线个数
	public static final int MOD_TYPE_HALF = 2;
	public static final int MOD_TYPE_ONE = 10;

	// 刻度线间隔距离
	private static final int ITEM_HALF_DIVIDER = 10;
	private static final int ITEM_ONE_DIVIDER = 2;

	// 刻度线尺寸
	private static final int ITEM_MAX_HEIGHT = 15;
	private static final int ITEM_MIN_HEIGHT = 8;

	// 刻度尺字体大小
	private static final int TEXT_SIZE = 10;

	private int ruler_StrokeWidth = 1;// 刻度尺线宽
	private String ruler_long_line_color = "#000000";// 刻度尺长线颜色
	private String ruler_short_line_color = "#565656";// 刻度尺短线颜色
	private String ruler_top_line_color = "#565656";// 刻度尺顶部线颜色

	//
	private int mLastX, mMove;
	private int mWidth;// 刻度尺布局宽
	private int mHeight;// 刻度尺布局高
	// TODO 画中心线参数
	private int indexWidth = 2;// 红线宽度
	private int indexTitleWidth = 10;// 顶部、底部线宽度
	private int indexTitleHight = 5;// 顶部、底部线高度
	// 中心线--》阴影线
	private int gap = 12;// 阴影线偏离距离
	private int shadow = 6;// 阴影线宽度
	private String color = "#66999999";// 阴影线颜色

	// TODO
	private Scroller mScroller;// 滚动操作(滚动的持续时间可以通过构造函数传递，并且可以指定滚动动作的持续的最长时间。)
	private int mMinVelocity;// 滚动最小速率
	private float mDensity; // 屏幕密度
	private VelocityTracker mVelocityTracker;
	private OnValueChangeListener mListener;

	private float mValue = 0f;// 初始值默认
	private int mMaxValue = 10 * MOD_TYPE_ONE;// 默认初始最大值
	private int mModType = MOD_TYPE_ONE;// 初始化刻度绘制默认（0.5）
	private int mLineDivider = ITEM_ONE_DIVIDER;//

	@SuppressWarnings("deprecation")
	public CustomRuler(Context context, AttributeSet attrs) {
		super(context, attrs);

		mScroller = new Scroller(getContext());
		mDensity = getContext().getResources().getDisplayMetrics().density;

		mMinVelocity = ViewConfiguration.get(getContext())
				.getScaledMinimumFlingVelocity();

		setBackgroundDrawable(createBackground());
	}

	private GradientDrawable createBackground() {
		float strokeWidth = 1 * mDensity; // 边框宽度
		float roundRadius = 6 * mDensity; // 圆角半径
		int strokeColor = Color.parseColor("#FF666666");// 边框颜色
		// int fillColor = Color.parseColor("#DFDFE0");// 内部填充颜色

		setPadding((int) strokeWidth, (int) strokeWidth, (int) strokeWidth, 0);

		int colors[] = { 0xFF999999, 0xFFFFFFFF, 0xFF999999 };//
		// 分别为开始颜色，中间夜色，结束颜色
		GradientDrawable bgDrawable = new GradientDrawable(
				GradientDrawable.Orientation.LEFT_RIGHT, colors);// 创建drawable
		bgDrawable.setColor(Color.WHITE);// 内部填充颜色
		bgDrawable.setCornerRadius(roundRadius);// 圆角大小
		bgDrawable.setStroke((int) strokeWidth, strokeColor);// 边框线
		// setBackgroundDrawable(gd);
		return bgDrawable;
	}

	/**
	 * 
	 * 考虑可扩展，但是时间紧迫，只可以支持两种类型效果图中两种类型
	 * 
	 * @param value
	 *            :初始值
	 * @param maxValue
	 *            :最大值
	 * @param model
	 *            :刻度盘精度：MOD_TYPE_HALF、MOD_TYPE_ONE
	 */
	public void initViewParam(float defaultValue, int maxValue, int model) {
		switch (model) {
		case MOD_TYPE_HALF:
			mModType = MOD_TYPE_HALF;
			mLineDivider = ITEM_HALF_DIVIDER;
			mValue = defaultValue * 2;
			mMaxValue = maxValue * 2;
			break;
		case MOD_TYPE_ONE:
			mModType = MOD_TYPE_ONE;
			mLineDivider = ITEM_ONE_DIVIDER;
			mValue = defaultValue * MOD_TYPE_ONE;
			mMaxValue = maxValue * MOD_TYPE_ONE;
			break;

		default:
			break;
		}
		invalidate();
		mLastX = 0;
		mMove = 0;
		notifyValueChange();
	}
	
	/**
	 * 设置用于接收结果的监听器
	 * 
	 * @param listener
	 */
	public void setValueChangeListener(OnValueChangeListener listener) {
		mListener = listener;
	}

	/**
	 * 获取当前刻度值
	 * 
	 * @return
	 */
	public float getValue() {
		return mValue;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		mWidth = getWidth();
		mHeight = getHeight();
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// TODO 画刻度尺背景、刻度、中心线
		// // 1.画刻度尺背景
		// drawWheel(canvas);
		// 2.画刻度线
		drawScaleLine(canvas);
		// 3.画中心线
		drawMiddleLine(canvas);

	}

	/**
	 * 画刻度尺背景
	 * 
	 * @param canvas
	 */
	private void drawWheel(Canvas canvas) {
		Drawable wheel = getResources().getDrawable(R.drawable.image_bg);
		wheel.setBounds(0, 0, getWidth(), getHeight());
		wheel.draw(canvas);
	}

	/**
	 * 画刻度线(从中间往两边开始画刻度线)
	 * 
	 * @param canvas
	 */
	private void drawScaleLine(Canvas canvas) {
		canvas.save();
		// 1 .画刻度尺顶部线
		Paint ovalPaint = new Paint();
		ovalPaint.setColor(Color.parseColor(ruler_top_line_color));
		ovalPaint.setStrokeWidth(mDensity * ruler_StrokeWidth);
		canvas.drawLine(0, mDensity * 5, mWidth, mDensity * 5, ovalPaint);

		// 2.画笔设置（线宽、颜色）
		Paint linePaint = new Paint();
		linePaint.setStrokeWidth(mDensity * ruler_StrokeWidth);
		linePaint.setColor(Color.parseColor(ruler_long_line_color));

		Paint linePaint2 = new Paint();
		linePaint2.setStrokeWidth(mDensity * ruler_StrokeWidth);
		linePaint2.setColor(Color.parseColor(ruler_short_line_color));

		// 3.文本画笔设置
		TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(Color.parseColor(ruler_short_line_color));
		textPaint.setTextSize(TEXT_SIZE * mDensity);

		// 4.画线
		int width = mWidth, drawCount = 0;
		float xPosition = 0, textWidth = Layout.getDesiredWidth("0", textPaint);

		for (int i = 0; drawCount <= 4 * width; i++) {
			int numSize = String.valueOf(mValue + i).length();
			// 向右绘制
			xPosition = (width / 2 - mMove) + i * mLineDivider * mDensity;
			if (xPosition + getPaddingRight() < mWidth) {
				if ((mValue + i) % mModType == 0) {// 画长线
					canvas.drawLine(xPosition, 5 * mDensity, xPosition,
							(ITEM_MAX_HEIGHT + 5) * mDensity, linePaint);

					if (mValue + i <= mMaxValue) {
						switch (mModType) {
						case MOD_TYPE_HALF:
							canvas.drawText(
									String.valueOf((mValue - i) / 2).substring(
											0,
											String.valueOf((mValue - i) / 2)
													.length() - 2),
									countLeftStart(mValue + i, xPosition,
											textWidth), mDensity
											* (ITEM_MAX_HEIGHT + 25), textPaint);
							break;
						case MOD_TYPE_ONE:
							if (((mValue + i) / 10) % 5 == 0) {
								canvas.drawText(
										String.valueOf((mValue + i) / 10)
												.substring(
														0,
														String.valueOf(
																(mValue + i) / 10)
																.length() - 2),
										xPosition - (textWidth * numSize / 5),
										mDensity * (ITEM_MAX_HEIGHT + 25),
										textPaint);
							}
							break;
						default:
							break;
						}
					}
				} else {
					canvas.drawLine(xPosition, 5 * mDensity, xPosition,
							(ITEM_MIN_HEIGHT + 5) * mDensity, linePaint2);
				}
			}
			// 向左绘制
			xPosition = (width / 2 - mMove) - i * mLineDivider * mDensity;
			if (xPosition > getPaddingLeft()) {
				if ((mValue - i) % mModType == 0) {// 画长线
					canvas.drawLine(xPosition, 5 * mDensity, xPosition,
							(ITEM_MAX_HEIGHT + 5) * mDensity, linePaint);

					if (mValue - i >= 0) {
						switch (mModType) {
						case MOD_TYPE_HALF:
							canvas.drawText(
									String.valueOf((mValue - i) / 2).substring(
											0,
											String.valueOf((mValue - i) / 2)
													.length() - 2),
									countLeftStart(mValue + i, xPosition,
											textWidth), mDensity
											* (ITEM_MAX_HEIGHT + 25), textPaint);
							break;
						case MOD_TYPE_ONE:
							if (((mValue - i) / 10) % 5 == 0) {
								canvas.drawText(
										String.valueOf((mValue - i) / 10)
												.substring(
														0,
														String.valueOf(
																(mValue - i) / 10)
																.length() - 2),
										xPosition - (textWidth * numSize / 5),
										mDensity * (ITEM_MAX_HEIGHT + 25),
										textPaint);
							}
							break;

						default:
							break;
						}
					}
				} else {
					canvas.drawLine(xPosition, 5 * mDensity, xPosition,
							(ITEM_MIN_HEIGHT + 5) * mDensity, linePaint2);
				}
			}

			drawCount += 2 * mLineDivider * mDensity;
		}

		canvas.restore();
	}

	/**
	 * 计算没有数字显示位置的辅助方法
	 * 
	 * @param value
	 * @param xPosition
	 * @param textWidth
	 * @return
	 */
	private float countLeftStart(float value, float xPosition, float textWidth) {
		float xp = 0f;
		if (value < 20) {
			xp = xPosition - (textWidth * 1 / 2);
		} else {
			xp = xPosition - (textWidth * 2 / 2);
		}
		return xp;
	}

	/**
	 * 画中间的红色指示线、阴影等。指示线两端简单的用了两个矩形代替
	 * 
	 * @param canvas
	 */
	private void drawMiddleLine(Canvas canvas) {
		canvas.save();
		// 1.中心线-->竖直
		Paint redPaint = new Paint();
		redPaint.setStrokeWidth(mDensity * ruler_StrokeWidth);
		redPaint.setColor(Color.RED);
		canvas.drawLine(mWidth / 2, 5 * mDensity, mWidth / 2,
				(ITEM_MAX_HEIGHT + 12) * mDensity, redPaint);
		// canvas.drawLine(mWidth / 2, 0, mWidth / 2, mHeight * 2 / 3,
		// redPaint);

		// // 2.中心线-->顶部、底部横线
		// Paint ovalPaint = new Paint();
		// ovalPaint.setColor(Color.RED);
		// ovalPaint.setStrokeWidth(indexTitleWidth);
		// canvas.drawLine(mWidth / 2, 0, mWidth / 2, indexTitleHight,
		// ovalPaint);
		// canvas.drawLine(mWidth / 2, mHeight - indexTitleHight, mWidth / 2,
		// mHeight, ovalPaint);

		// // 3.中心线阴影
		// Paint shadowPaint = new Paint();
		// shadowPaint.setStrokeWidth(shadow);
		// shadowPaint.setColor(Color.parseColor(color));
		// canvas.drawLine(mWidth / 2 + gap, 0, mWidth / 2 + gap, mHeight,
		// shadowPaint);

		canvas.restore();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int xPosition = (int) event.getX();

		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);

		switch (action) {
		case MotionEvent.ACTION_DOWN:

			mScroller.forceFinished(true);

			mLastX = xPosition;
			mMove = 0;
			break;
		case MotionEvent.ACTION_MOVE:
			mMove += (mLastX - xPosition);
			changeMoveAndValue();
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			countMoveEnd();
			countVelocityTracker(event);
			return false;
			// break;
		default:
			break;
		}

		mLastX = xPosition;
		return true;
	}

	private void countVelocityTracker(MotionEvent event) {
		mVelocityTracker.computeCurrentVelocity(1000);
		float xVelocity = mVelocityTracker.getXVelocity();
		if (Math.abs(xVelocity) > mMinVelocity) {
			mScroller.fling(0, 0, (int) xVelocity, 0, Integer.MIN_VALUE,
					Integer.MAX_VALUE, 0, 0);
		}
	}

	/**
	 * 移动和值改变
	 */
	private void changeMoveAndValue() {
		int tValue = (int) (mMove / (mLineDivider * mDensity));
		if (Math.abs(tValue) > 0) {
			mValue += tValue;
			mMove -= tValue * mLineDivider * mDensity;
			if (mValue <= 0 || mValue > mMaxValue) {
				mValue = mValue <= 0 ? 0 : mMaxValue;
				mMove = 0;
				mScroller.forceFinished(true);
			}
			notifyValueChange();
		}
		postInvalidate();
	}

	private void countMoveEnd() {
		int roundMove = Math.round(mMove / (mLineDivider * mDensity));
		mValue = mValue + roundMove;
		mValue = mValue <= 0 ? 0 : mValue;
		mValue = mValue > mMaxValue ? mMaxValue : mValue;

		mLastX = 0;
		mMove = 0;

		notifyValueChange();
		postInvalidate();
	}

	/**
	 * 刷新
	 */
	private void notifyValueChange() {
		if (null != mListener) {
			if (mModType == MOD_TYPE_ONE) {
				mListener.onValueChange(mValue);
			}
			if (mModType == MOD_TYPE_HALF) {
				mListener.onValueChange(mValue / 2f);
			}
		}
	}

	/**
	 * 滚动计算
	 */
	@Override
	public void computeScroll() {
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			if (mScroller.getCurrX() == mScroller.getFinalX()) { // over
				countMoveEnd();
			} else {
				int xPosition = mScroller.getCurrX();
				mMove += (mLastX - xPosition);
				changeMoveAndValue();
				mLastX = xPosition;
			}
		}
	}

	/**
	 * TODO 值变化接口
	 * 
	 * @author jiyx
	 * 
	 */
	public interface OnValueChangeListener {

		public void onValueChange(float value);
	}
}
