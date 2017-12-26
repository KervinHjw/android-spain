package com.en.scian.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

/**
 * 可设置渐变颜色的环形进度条
 * 
 * @author jiyx
 * 
 */
public class CustomAnnularBar extends View {

	/**
	 * 环型圈(背景)
	 */
	private Paint mPaintBackCircle;
	private int circelBackColor = Color.WHITE;

	/**
	 * 环型圈(前面)
	 */
	private Paint mPaintFrontCircle;
	private int circelFrontColor = 0xFF66C796;

	/**
	 * 环的宽度
	 */
	private float mStrokeWidth = 25;
	private float mRadius = 100;
	// private float mHalfStrokeWidth = mStrokeWidth / 1;
	// private float mX = 100 + mHalfStrokeWidth;
	// private float mY = 100 + mHalfStrokeWidth;
	private RectF mRectF;// 矩形

	/**
	 * 中间文字画笔
	 */
	private Paint mPaintText;
	private int mTextColor = 0xFF66C796;
	private int mTextSize = 50;

	/**
	 * 进度
	 */
	private int mProgress = 0;// 原始进度（==0）
	private int mTargetmProgress = 0;// 目标进度
	private int mMax = 100;// 最大值
	private int startAngle = -90;// 环形颜色绘制起始点（角度）

	/**
	 * 渐变
	 */
	private boolean isOpenGradient = false;// 开启渐变
	private int gradientStartColor = Color.RED;// 开始颜色
	private int gradientEndColor = Color.WHITE;// 结束颜色

	/**
	 * 控件尺寸
	 */
	private int mWidth;
	private int mHeight;

	public CustomAnnularBar(Context context) {
		super(context);
	}

	public CustomAnnularBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomAnnularBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		// 背景环画笔
		mPaintBackCircle = new Paint();
		mPaintBackCircle.setColor(circelBackColor);
		mPaintBackCircle.setAntiAlias(true);// 取消锯齿
		mPaintBackCircle.setStyle(Paint.Style.STROKE);
		mPaintBackCircle.setStrokeWidth(mStrokeWidth);

		// 前面环画笔
		mPaintFrontCircle = new Paint();
		mPaintFrontCircle.setColor(circelFrontColor);
		mPaintFrontCircle.setAntiAlias(true);// 取消锯齿
		mPaintFrontCircle.setStyle(Paint.Style.STROKE);
		mPaintFrontCircle.setStrokeWidth(mStrokeWidth);
		if (isOpenGradient) {
			Shader mShader = new SweepGradient(mWidth / 2, mHeight / 2,
					gradientStartColor, gradientEndColor);
			Matrix mMatrix = new Matrix();
			mMatrix.setRotate(startAngle, mWidth / 2, mHeight / 2);
			mShader.setLocalMatrix(mMatrix);
			mPaintFrontCircle.setShader(mShader);
		}

		// 中间文字画笔
		mPaintText = new Paint();
		mPaintText.setColor(mTextColor);
		mPaintText.setTextSize(mTextSize);
		mPaintText.setTextAlign(Paint.Align.CENTER);

		// // 前面扇形环
		// mRectF = new RectF(mHalfStrokeWidth, mHalfStrokeWidth, mRadius * 2
		// + mHalfStrokeWidth, mRadius * 2 + mHalfStrokeWidth);
	}

	private void initRect() {
		if (mRectF == null) {
			mRectF = new RectF();
			int viewSize = (int) (mRadius * 2);

			int left = (mWidth - viewSize) / 2;
			int top = (mHeight - viewSize) / 2;
			int right = left + viewSize;
			int bottom = top + viewSize;
			mRectF.set(left, top, right, bottom);
		}
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		// super.onDraw(canvas);
		init();// 代码中重新定义颜色等要在此初始化
		initRect();

		// 1.画背景环形
		canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mPaintBackCircle);

		// 2.画前面的环形
		// a.是否开启渐变
		// if (isOpenGradient) {
		// Shader mShader = new SweepGradient(mWidth / 2, mHeight / 2,
		// gradientStartColor, gradientEndColor);
		// Matrix mMatrix = new Matrix();
		// mMatrix.setRotate(startAngle, mWidth / 2, mHeight / 2);
		// mShader.setLocalMatrix(mMatrix);
		// mPaintFrontCircle.setShader(mShader);
		// }
		// b.计算旋转角度
		float angle = mProgress / (float) mMax * 360;
		canvas.drawArc(mRectF, startAngle, angle, false, mPaintFrontCircle);

		// 3.画中间显示文本
		canvas.drawText(mProgress + "%", mWidth / 2, mHeight / 2 + mTextSize
				/ 3, mPaintText);

		if (mProgress < mTargetmProgress) {
			mProgress += 2;
			invalidate();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		mWidth = getRealSize(widthMeasureSpec);
		mHeight = getRealSize(heightMeasureSpec);

		setMeasuredDimension(mWidth, mHeight);
	}

	public int getRealSize(int measureSpec) {
		int result = -1;
		int mode = MeasureSpec.getMode(measureSpec);
		int size = MeasureSpec.getSize(measureSpec);
		if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.UNSPECIFIED) {
			// 自己计算
			result = (int) (mRadius * 2 + mStrokeWidth * 2);
		} else {
			result = size;
		}
		return result;
	}

	/**
	 * TODO 设置背景环的颜色
	 * 
	 * @param circelBackColor
	 */
	public void setCircelBackColor(int circelBackColor) {
		this.circelBackColor = circelBackColor;
	}

	/**
	 * TODO 设置前面环的颜色
	 * 
	 * @param circelFrontColor
	 */
	public void setCircelFrontColor(int circelFrontColor) {
		this.circelFrontColor = circelFrontColor;
	}

	/**
	 * TODO 设置环的宽度
	 * 
	 * @param mStrokeWidth
	 */
	public void setmStrokeWidth(float mStrokeWidth) {
		this.mStrokeWidth = mStrokeWidth;
	}

	/**
	 * TODO 设置环的半径
	 * 
	 * @param mRadius
	 */
	public void setmRadius(float mRadius) {
		this.mRadius = mRadius;
	}

	/**
	 * TODO 设置字体颜色
	 * 
	 * @param mTextColor
	 */
	public void setmTextColor(int mTextColor) {
		this.mTextColor = mTextColor;
	}

	/**
	 * TODO 设置字体大小
	 * 
	 * @param mTextSize
	 */
	public void setmTextSize(int mTextSize) {
		this.mTextSize = mTextSize;
	}

	/**
	 * TODO 设置目标进度
	 * 
	 * @param mTargetmProgress
	 */
	public void setmTargetmProgress(int mTargetmProgress) {
		this.mTargetmProgress = mTargetmProgress;
	}

	/**
	 * TODO 设置开始角度
	 * 
	 * @param startAngle
	 */
	public void setStartAngle(int startAngle) {
		this.startAngle = startAngle;
	}

	/**
	 * TODO 设置是否开启渐变
	 * 
	 * @param isOpenGradient
	 */
	public void setOpenGradient(boolean isOpenGradient) {
		this.isOpenGradient = isOpenGradient;
	}

	/**
	 * TODO 渐变开始颜色
	 * 
	 * @param gradientStartColor
	 */
	public void setGradientStartColor(int gradientStartColor) {
		this.gradientStartColor = gradientStartColor;
	}

	/**
	 * TODO 渐变结束颜色
	 * 
	 * @param gradientEndColor
	 */
	public void setGradientEndColor(int gradientEndColor) {
		this.gradientEndColor = gradientEndColor;
	}

}
