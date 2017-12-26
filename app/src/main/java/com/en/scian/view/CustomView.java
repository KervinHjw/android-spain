package com.en.scian.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义进度条
 * 
 * @author jiyx
 * 
 */
public class CustomView extends View {

	// 背景圆
	private Paint mPaintBackCircle;
	private float mBackStrokeCircleWidth = 10;
	private int mBackCircleColor = Color.WHITE;

	// 前面的圆
	private Paint mPaintFrontCircle;
	private float mFrontStrokeCircleWidth = 20;
	private int mFrontCircleColor = 0xFF66C796;

	// 前面圆上的圆点
	private Paint mPaintWhiteCircle;
	private float mWhiteCircleRadius = 15;
	private int mWhiteCircleColor = Color.WHITE;

	// 中心文字
	private Paint mPaintText;
	private int mTextColor = 0xFF66C796;
	private int mTextSize = 80;
	private String mTextContent = "0";

	// 中心文字
	private Paint mPaintText2;
	private int mTextColor2 = 0xFF66C796;
	private int mTextSize2 = 100;
	private String mTextContent2;

	// 圆半径
	private float mRadius = 200;

	private RectF mRectF;

	private int mProgress = 0;
	private int mTargetProgress = 70;
	private int mMax = 300;

	// 控件宽高
	private int mWidth;
	private int mHeight;
	// 是否显示单个数据
	private boolean flag = false;
	private int down;

	public CustomView(Context context) {
		super(context);
		init();
	}

	public CustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void setDown(int down) {
		this.down = down;
	}

	private void init() {
		// 背景圆
		mPaintBackCircle = new Paint();
		mPaintBackCircle.setColor(mBackCircleColor);// 画笔颜色
		mPaintBackCircle.setAntiAlias(true);// 去锯齿
		mPaintBackCircle.setStyle(Paint.Style.STROKE);
		mPaintBackCircle.setStrokeWidth(mBackStrokeCircleWidth);

		// 前面的圆
		mPaintFrontCircle = new Paint();
		mPaintFrontCircle.setColor(mFrontCircleColor);// 画笔颜色
		mPaintFrontCircle.setAntiAlias(true);// 去锯齿
		mPaintFrontCircle.setStyle(Paint.Style.STROKE);
		mPaintFrontCircle.setStrokeWidth(mFrontStrokeCircleWidth);

		// 前面圆上的圆点
		mPaintWhiteCircle = new Paint();
		mPaintWhiteCircle.setColor(mWhiteCircleColor);// 画笔颜色
		mPaintWhiteCircle.setAntiAlias(true);// 去锯齿
		mPaintWhiteCircle.setStyle(Paint.Style.FILL);

		// 中心文字
		mPaintText = new Paint();
		mPaintText.setColor(mTextColor);// 画笔颜色
		mPaintText.setAntiAlias(true);// 去锯齿
		mPaintText.setTextSize(mTextSize);
		mPaintText.setTextAlign(Paint.Align.CENTER);// 文字居中

		// 中心文字
		mPaintText2 = new Paint();
		mPaintText2.setColor(mTextColor);// 画笔颜色
		mPaintText2.setAntiAlias(true);// 去锯齿
		mPaintText2.setTextSize(60);
		mPaintText2.setTextAlign(Paint.Align.CENTER);// 文字居中

		// mRectF = new RectF(mhalfStrokeWidth, mhalfStrokeWidth, mRadius * 2
		// + mhalfStrokeWidth, mRadius * 2 + mhalfStrokeWidth);

	}

	/**
	 * 初始化扇形（矩形）
	 */
	private void initRect() {
		if (mRectF == null) {
			mRectF = new RectF();
			int viewSize = (int) (mRadius * 2);
			int left = (mWidth - viewSize) / 2;
			int top = (mHeight - viewSize) / 2;
			int right = left + viewSize;
			int bottom = top + viewSize;
			mRectF = new RectF(left, top, right, bottom);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		init();
		initRect();

		float angle = mProgress / (float) mMax * 360;
		// 1、背景圆
		canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mPaintBackCircle);
		// 2、前面圆
		canvas.drawArc(mRectF, -90, angle, false, mPaintFrontCircle);
		// 3.前面圆头部圆点
		canvas.drawCircle(
				mWidth
						* 0.5f
						- (float) (mRadius * Math.cos(Math
								.toRadians(angle + 90))),
				mHeight
						* 0.5f
						- (float) (mRadius * Math.sin(Math
								.toRadians(angle + 90))), mWhiteCircleRadius,
				mPaintWhiteCircle);

		// 4、中心文字
		if (flag) {
			canvas.drawText(mTextContent, mWidth * 0.5f, mHeight * 0.5f
					- mTextSize * 0.5f + 20, mPaintText);
		} else {
			canvas.drawText(mTextContent + "/" + down, mWidth * 0.5f, mHeight
					* 0.5f - mTextSize * 0.5f + 20, mPaintText);
		}

		canvas.drawText(mTextContent2, mWidth * 0.5f, mHeight * 0.5f
				+ mTextSize * 0.5f + 20, mPaintText2);

		// if (mProgress < mTargetProgress) {
		// mProgress += 2;
		// invalidate();
		// }
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mWidth = getRealSize(widthMeasureSpec);
		mHeight = getRealSize(heightMeasureSpec);
		setMeasuredDimension(mWidth, mHeight);
	}

	/**
	 * 计算宽高比例
	 * 
	 * @param measureSpec
	 * @return
	 */
	public int getRealSize(int measureSpec) {
		int result = -1;
		int mode = MeasureSpec.getMode(measureSpec);
		int size = MeasureSpec.getSize(measureSpec);

		if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.UNSPECIFIED) {
			// 未定义，需自己计算
			result = (int) (mRadius * 2 + mFrontStrokeCircleWidth + mWhiteCircleRadius);
		} else {
			result = size;
		}
		return result;
	}

	/**
	 * 背景圆环的宽度
	 * 
	 * @param mBackStrokeCircleWidth
	 */
	public void setBackStrokeCircleWidth(float mBackStrokeCircleWidth) {
		this.mBackStrokeCircleWidth = mBackStrokeCircleWidth;
	}

	/**
	 * 背景圆环的颜色
	 * 
	 * @param mBackCircleColor
	 */
	public void setBackCircleColor(int mBackCircleColor) {
		this.mBackCircleColor = mBackCircleColor;
	}

	/**
	 * 前面圆环的宽度
	 * 
	 * @param mFrontStrokeCircleWidth
	 */
	public void setFrontStrokeCircleWidth(float mFrontStrokeCircleWidth) {
		this.mFrontStrokeCircleWidth = mFrontStrokeCircleWidth;
	}

	/**
	 * 前面圆环的颜色
	 * 
	 * @param mFrontCircleColor
	 */
	public void setFrontCircleColor(int mFrontCircleColor) {
		this.mFrontCircleColor = mFrontCircleColor;
	}

	/**
	 * 前面圆环上圆点的半径
	 * 
	 * @param mWhiteCircleRadius
	 */
	public void setWhiteCircleRadius(float mWhiteCircleRadius) {
		this.mWhiteCircleRadius = mWhiteCircleRadius;
	}

	/**
	 * 前面圆环上圆点的颜色
	 * 
	 * @param mWhiteCircleColor
	 */
	public void setWhiteCircleColor(int mWhiteCircleColor) {
		this.mWhiteCircleColor = mWhiteCircleColor;
	}

	/**
	 * 圆环中间字体的颜色
	 * 
	 * @param mTextColor
	 */
	public void setTextColor(int mTextColor) {
		this.mTextColor = mTextColor;
	}

	/**
	 * 圆环中间字体的大小
	 * 
	 * @param mTextSize
	 */
	public void setTextSize(int mTextSize) {
		this.mTextSize = mTextSize;
	}

	/**
	 * 圆环中间字体的内容
	 * 
	 * @param mTextContent
	 */

	public void setTextContent(String mTextContent) {
		this.mTextContent = mTextContent;
	}

	/**
	 * 圆环中间字体2的颜色
	 * 
	 * @param mTextColor
	 */
	public void setTextColor2(int mTextColor2) {
		this.mTextColor2 = mTextColor2;
	}

	/**
	 * 圆环中间字体2的大小
	 * 
	 * @param mTextContent
	 */
	public void setTextSize2(int mTextSize2) {
		this.mTextSize2 = mTextSize2;
	}

	/**
	 * 圆环中间字体2的内容
	 * 
	 * @param mTextContent
	 */
	public void setTextContent2(String mTextContent2) {
		this.mTextContent2 = mTextContent2;
	}

	/**
	 * 圆环的半径
	 * 
	 * @param mRadius
	 */
	public void setRadius(float mRadius) {
		this.mRadius = mRadius;
	}

	/**
	 * 圆环的进度
	 * 
	 * @param mProgress
	 */
	public void setProgress(int mProgress) {
		this.mProgress = mProgress;
		invalidate();
	}
}
