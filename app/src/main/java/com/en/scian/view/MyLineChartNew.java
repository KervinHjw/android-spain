package com.en.scian.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MyLineChartNew extends View {
	// X轴画笔
	private Paint xPaint;
	private float xLineWidth = 2;
	private int xLineColor = Color.WHITE;
	private boolean isDrawXAxisLine = true;
	private boolean isDrawXMarkLine = true;
	private boolean isDrawXAxisValueText = true;

	// Y轴画笔
	private Paint yPaint;
	private float yLineWidth = 2;
	private int yLineColor = Color.WHITE;
	private boolean isDrawYAxisLine = true;
	private boolean isDrawYMarkLine = true;

	// 轴标签画笔
	private Paint textPaint;
	private int textPaintSize = 20;
	private int textPaintColor = Color.WHITE;

	// 值圆点
	private Paint valuePaint;
	private int valuePaintStrokeWidth = 3;
	private int valuePaintColor = Color.WHITE;
	private int valuePaintCircleRadius = 5;

	// 值圆点连接线
	private Paint valueLinePaint;
	private int valueLinePaintWidth = 3;
	private int valueLinePaintColor = Color.WHITE;

	// X 坐标间距（默认）
	private int XScaleDis = 130; // X的刻度长度
	private int XLength = 3 * XScaleDis; // X轴的长度

	// Y 坐标间距（默认）
	private int YScaleDis = 30; // Y的刻度长度
	private int YLength = 7 * YScaleDis; // Y轴的长度

	// 折线图原始坐标点
	private int XPoint = 50; // 原点的X坐标
	private int YPoint = (int) YLength; // 原点的Y坐标

	// 控件宽高
	private int width;
	private int height;

	public String[] XLabel; // X的刻度
	public String[] YLabel; // Y的刻度
	public String[] Data; // 数据
	public String Title; // 显示的标题

	public MyLineChartNew(Context context) {
		super(context);
	}

	public MyLineChartNew(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void SetInfo(String[] XLabels, String[] YLabels, String[] AllData,
			String strTitle) {
		XLabel = XLabels;
		YLabel = YLabels;
		Data = AllData;
		Title = strTitle;
	}

	/**
	 * 初始化画笔
	 */
	private void initPaint() {
		// X轴画笔
		xPaint = new Paint();
		xPaint.setStyle(Paint.Style.STROKE);
		xPaint.setAntiAlias(true);// 去锯齿
		xPaint.setColor(xLineColor);// 颜色
		xPaint.setStrokeWidth(xLineWidth);// 画笔粗细

		// Yzhou画笔
		yPaint = new Paint();
		yPaint.setStyle(Paint.Style.STROKE);
		yPaint.setAntiAlias(true);// 去锯齿
		yPaint.setColor(yLineColor);// 颜色
		yPaint.setStrokeWidth(yLineWidth);// 画笔粗细

		// 坐标轴标签
		textPaint = new Paint();
		// textPaint.setStyle(Paint.Style.STROKE);
		textPaint.setAntiAlias(true);// 去锯齿
		textPaint.setColor(textPaintColor);// 颜色
		textPaint.setTextSize(textPaintSize);// 字体大小

		// 值圆点
		valuePaint = new Paint();
		valuePaint.setStyle(Paint.Style.STROKE);
		valuePaint.setAntiAlias(true);// 去锯齿
		valuePaint.setColor(valuePaintColor);
		valuePaint.setStrokeWidth(valuePaintStrokeWidth);

		// 值连接线
		valueLinePaint = new Paint();
		valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
		valueLinePaint.setAntiAlias(true);// 去锯齿
		valueLinePaint.setColor(valueLinePaintColor);
		valueLinePaint.setStrokeWidth(valueLinePaintWidth);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		initPaint();
		XScaleDis = width / 3;
		XLength = XScaleDis * 3;

		YScaleDis = height / 7;
		YLength = 7 * YScaleDis;

		canvasXAxis(canvas);
		canvasYAxis(canvas);
	}

	/**
	 * TODO 画X轴轴线
	 * 
	 * @param canvas
	 */
	private void canvasXAxis(Canvas canvas) {
		if (isDrawXAxisLine) {
			canvas.drawLine(XPoint, height, XPoint + XLength, height, xPaint); // 轴线
			if (isDrawXMarkLine) {
				for (int i = 0; i * XScaleDis < XLength; i++) {
					canvas.drawLine(XPoint + i * XScaleDis, height, XPoint + i
							* XScaleDis, height - 5, xPaint); // 刻度
				}
			}
		}
		if (isDrawXAxisValueText) {
			if (XLabel != null && XLabel.length > 0) {
				for (int i = 0; i * XScaleDis < XLength; i++) {
					canvas.drawText(XLabel[i], XPoint + i * XScaleDis,
							height + 20, xPaint); // 文字
				}
			}
		}
		if (Data != null && Data.length > 0) {
			for (int i = 0; i < Data.length; i++) {
				// 数据值（// 保证有效数据）
				if (i > 0 && YCoord(Data[i - 1]) != -999
						&& YCoord(Data[i]) != -999) {
					canvas.drawLine(XPoint + (i - 1) * XScaleDis,
							YCoord(Data[i - 1]), XPoint + i * XScaleDis,
							YCoord(Data[i]), valueLinePaint);
				}
				canvas.drawCircle(XPoint + i * XScaleDis, YCoord(Data[i]),
						valuePaintCircleRadius, valuePaint);
			}
		}

	}

	/**
	 * TODO 画Y轴轴线
	 * 
	 * @param canvas
	 */
	private void canvasYAxis(Canvas canvas) {
		// 1.画轴线
		canvas.drawLine(XPoint, height - YLength - YScaleDis / 2, XPoint,
				height + YScaleDis / 2, yPaint);
		// 2.画轴线刻度
		if (isDrawYMarkLine) {
			for (int i = 0; i * YScaleDis < YLength; i++) {
				if (i % 2 == 0) {// 短刻度
					canvas.drawLine(XPoint, height - i * YScaleDis - YScaleDis
							/ 2, XPoint - 10, height - i * YScaleDis
							- YScaleDis / 2, yPaint); // 刻度
				} else {
					canvas.drawLine(XPoint, height - i * YScaleDis - YScaleDis
							/ 2, XPoint - 6, height - i * YScaleDis - YScaleDis
							/ 2, yPaint); // 刻度
				}
			}
		}
		// 文字
		if (YLabel != null && YLabel.length > 0) {
			for (int i = 0; i * YScaleDis < YLength; i++) {
				canvas.drawText(YLabel[i], 0, height - i * YScaleDis
						- YScaleDis / 2 + 5, textPaint);
			}
		}
	}

	// 计算绘制时的Y坐标，无数据时返回-999
	private int YCoord(String y0) {
		int y = 0;
		try {
			y = Integer.parseInt(y0);
			YScaleDis = height / 7;
			return height - YScaleDis / 2 - (y - 50) * YScaleDis / 25;
		} catch (Exception e) {
			// TODO: handle exception
			return y;
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = measureWidth(widthMeasureSpec);
		height = measureHeight(heightMeasureSpec);
		setMeasuredDimension(width, height);
	}

	private int measureWidth(int measureSpec) {
		int result = 0;
		int mode = MeasureSpec.getMode(measureSpec);
		int size = MeasureSpec.getSize(measureSpec);

		if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.UNSPECIFIED) {
			// 未定义，需自己计算
			result = (int) (XLength);
		} else {
			result = size;
		}
		return result;
	}

	private int measureHeight(int measureSpec) {
		int result = 0;
		int mode = MeasureSpec.getMode(measureSpec);
		int size = MeasureSpec.getSize(measureSpec);

		if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.UNSPECIFIED) {
			// 未定义，需自己计算
			result = (int) (YLength);
		} else {
			result = size;
		}
		return result;
	}

	public void setxLineWidth(float xLineWidth) {
		this.xLineWidth = xLineWidth;
	}

	public void setxLineColor(int xLineColor) {
		this.xLineColor = xLineColor;
	}

	public void setDrawXAxisLine(boolean isDrawXAxisLine) {
		this.isDrawXAxisLine = isDrawXAxisLine;
	}

	public void setDrawXMarkLine(boolean isDrawXMarkLine) {
		this.isDrawXMarkLine = isDrawXMarkLine;
	}

	public void setDrawXAxisValueText(boolean isDrawXAxisValueText) {
		this.isDrawXAxisValueText = isDrawXAxisValueText;
	}

	public void setyLineWidth(float yLineWidth) {
		this.yLineWidth = yLineWidth;
	}

	public void setyLineColor(int yLineColor) {
		this.yLineColor = yLineColor;
	}

	public void setDrawYAxisLine(boolean isDrawYAxisLine) {
		this.isDrawYAxisLine = isDrawYAxisLine;
	}

	public void setDrawYMarkLine(boolean isDrawYMarkLine) {
		this.isDrawYMarkLine = isDrawYMarkLine;
	}

	public void setTextPaintSize(int textPaintSize) {
		this.textPaintSize = textPaintSize;
	}

	public void setTextPaintColor(int textPaintColor) {
		this.textPaintColor = textPaintColor;
	}

	public void setXLabel(String[] xLabel) {
		XLabel = xLabel;
	}

	public void setYLabel(String[] yLabel) {
		YLabel = yLabel;
	}

	public void setData(String[] data) {
		Data = data;
		invalidate();
	}

	public void setYScaleDis(int yScaleDis) {
		YScaleDis = yScaleDis;
		invalidate();
	}

	public void setValuePaintColor(int valuePaintColor) {
		this.valuePaintColor = valuePaintColor;
	}

	public void setValueLinePaintColor(int valueLinePaintColor) {
		this.valueLinePaintColor = valueLinePaintColor;
	}

}
