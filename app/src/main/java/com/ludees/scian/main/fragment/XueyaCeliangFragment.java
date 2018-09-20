package com.ludees.scian.main.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ludees.scian.R;
import com.ludees.scian.entity.ResponseCommon;
import com.ludees.scian.entity.XueyaShouye;
import com.ludees.scian.entity.XueyaShouyeBean;
import com.ludees.scian.network.Urls;
import com.ludees.scian.util.BlueToothUtil;
import com.ludees.scian.util.DisplayUtils;
import com.ludees.scian.util.SettingUtils;
import com.ludees.scian.view.CustomProgressBarView;
import com.ludees.scian.view.MyTextView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;

/**
 * 血压测量
 * 
 * @author jiyx
 * 
 */
public class XueyaCeliangFragment extends Fragment implements OnClickListener {

	private LineChart mChart1;
	private LineChart mChart2;

	private Button xueya_celiang;
	private BlueToothUtil util;

	private TextView home_xueya_celiangtime;
	private TextView home_xueya_shousuoya;
	private TextView home_xueya_shuzhangya;
	private MyTextView home_xueya_pulse;
	private CustomProgressBarView home_xueya_healthnumber_quan;
	private TextView home_xueya_shousuoya_average;
	private TextView home_xueya_shouzhangya_average;
	private TextView home_xueya_shousuoya_max;
	private TextView home_xueya_shouzhangya_max;

	private AbHttpUtil mAbHttpUtil;
	private Gson gson;

	private View view;

	private YAxis leftAxis;
	private YAxis leftAxis2;
	private List<Integer> closeList;
	private List<Integer> openList;

	private boolean isFirstIn = true;
	private FinalHttp fh;
	private XueyaShouye data;
	/**
	 * 获取蓝牙adapter
	 */
	private BluetoothAdapter bluetoothAdapter;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.home_content_fragment_xueyaceliang,
				null);

		// initViews(view);

		return view;
	}

	private void initViews(View view) {
		fh = new FinalHttp();
		fh.configTimeout(15 * 1000);
		xueya_celiang = (Button) view.findViewById(R.id.xueya_celiang);
		initChart1(view);
		initChart2(view);
		home_xueya_celiangtime = (TextView) view
				.findViewById(R.id.home_xueya_celiangtime);
		home_xueya_shousuoya = (TextView) view
				.findViewById(R.id.home_xueya_shousuoya);
		home_xueya_shuzhangya = (TextView) view
				.findViewById(R.id.home_xueya_shuzhangya);
		home_xueya_pulse = (MyTextView) view
				.findViewById(R.id.home_xueya_pulse);
		home_xueya_healthnumber_quan = (CustomProgressBarView) view
				.findViewById(R.id.home_xueya_healthnumber_quan);
		home_xueya_healthnumber_quan.setRadius(DisplayUtils.dip2px(
				getActivity(), 45));
		home_xueya_healthnumber_quan.setBackStrokeCircleWidth(DisplayUtils
				.dip2px(getActivity(), 5));
		home_xueya_healthnumber_quan.setFrontStrokeCircleWidth(DisplayUtils
				.dip2px(getActivity(), 5));
		home_xueya_healthnumber_quan.setBackCircleColor(0x22FFFFFF);
		home_xueya_healthnumber_quan.setTextSize(DisplayUtils.dip2px(
				getActivity(), 32));

		// home_xueya_healthnumber = (TextView) view
		// .findViewById(R.id.home_xueya_healthnumber);
		home_xueya_shousuoya_average = (TextView) view
				.findViewById(R.id.home_xueya_shousuoya_average);
		home_xueya_shouzhangya_average = (TextView) view
				.findViewById(R.id.home_xueya_shouzhangya_average);
		home_xueya_shousuoya_max = (TextView) view
				.findViewById(R.id.home_xueya_shousuoya_max);
		home_xueya_shouzhangya_max = (TextView) view
				.findViewById(R.id.home_xueya_shouzhangya_max);

		// 获取Http工具类
		mAbHttpUtil = AbHttpUtil.getInstance(getActivity());
		mAbHttpUtil.setTimeout(10000);
		gson = new Gson();

		initListener();
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		// TODO Auto-generated method stub
		xueya_celiang.setOnClickListener(this);
	}

	/**
	 * TODO 初始化折线图1
	 * 
	 * @param view
	 */
	private void initChart1(View view) {
		mChart1 = (LineChart) view.findViewById(R.id.xueya_chart1);
		// 设置趋势图的描述
		mChart1.setDescription("");
		mChart1.setNoDataTextDescription("");
		mChart1.setNoDataText("");
		mChart1.setViewPortOffsets(DisplayUtils.dip2px(getActivity(), 30),
				DisplayUtils.dip2px(getActivity(), 5),
				DisplayUtils.dip2px(getActivity(), 10),
				DisplayUtils.dip2px(getActivity(), 5));

		// 隐藏右边 的坐标轴
		mChart1.getAxisRight().setEnabled(false);
		// 设置值高了显示
		mChart1.setHighlightEnabled(true);
		// 设置可以通过手势操作
		mChart1.setTouchEnabled(true);
		mChart1.setDragDecelerationFrictionCoef(0.99f);
		// 启用缩放和拖动
		mChart1.setDragEnabled(true);
		mChart1.setScaleEnabled(true);
		mChart1.setDrawGridBackground(true);
		// mChart.setHighlightPerDragEnabled(true);
		// 如果禁用，缩放可以在X和Y轴分别做
		mChart1.setPinchZoom(false);
		mChart1.setScaleXEnabled(false);
		mChart1.setScaleYEnabled(false);
		// 设置可选背景色
		mChart1.setBackgroundColor(0x00000000);
		mChart1.setGridBackgroundColor(0x00000000);

		// 设置比例图标示，就是那个一组y的value的
		Legend l = mChart1.getLegend();
		l.setEnabled(false);

		// TODO X轴属性设置
		XAxis xAxis = mChart1.getXAxis();
		// 设置避免第一个和最后一个坐标值被裁切掉
		// xAxis.setAvoidFirstLastClipping(true);
		// 设置X轴字体和颜色
		xAxis.setTextSize(10f);
		xAxis.setTextColor(Color.BLACK);
		// 设置画X轴网格线
		xAxis.setGridColor(Color.LTGRAY);
		xAxis.setGridLineWidth(1.0f);
		xAxis.setDrawGridLines(false);
		// 设置画X轴轴线
		xAxis.setDrawAxisLine(false);
		// 设置X轴显示坐标的间隔大小
		xAxis.setLabelsToSkip(0);
		xAxis.setPosition(XAxisPosition.BOTTOM);// 让x轴在下面
		xAxis.setAxisLineColor(Color.LTGRAY);
		xAxis.setAxisLineWidth(1.0f);

		leftAxis = mChart1.getAxisLeft();
		// 设置Y轴字体和颜色
		leftAxis.setTextColor(0x88FFFFFF);// Y轴字体颜色
		leftAxis.setAxisMinValue(200);
		leftAxis.setAxisMinValue(20);
		leftAxis.setLabelCount(5, false);
		leftAxis.setDrawGridLines(false);// 显示Y轴网格线
		leftAxis.setDrawAxisLine(true);// 显示Y轴
		leftAxis.setAxisLineColor(0x66FFFFFF);// Y轴颜色
		leftAxis.setAxisLineWidth(1.0f);// Y轴线宽
		leftAxis.setStartAtZero(false);
	}

	/**
	 * TODO 初始化折线图2
	 * 
	 * @param view
	 */
	private void initChart2(View view) {
		mChart2 = (LineChart) view.findViewById(R.id.xueya_chart2);
		// 设置趋势图的描述
		mChart2.setDescription("");
		mChart2.setNoDataTextDescription("");
		mChart2.setNoDataText("");
		mChart2.setViewPortOffsets(DisplayUtils.dip2px(getActivity(), 30),
				DisplayUtils.dip2px(getActivity(), 5),
				DisplayUtils.dip2px(getActivity(), 10),
				DisplayUtils.dip2px(getActivity(), 5));

		// 隐藏右边 的坐标轴
		mChart2.getAxisRight().setEnabled(false);
		// 设置值高了显示
		mChart2.setHighlightEnabled(true);
		// 设置可以通过手势操作
		mChart2.setTouchEnabled(true);
		mChart2.setDragDecelerationFrictionCoef(0.99f);
		// 启用缩放和拖动
		mChart2.setDragEnabled(true);
		mChart2.setScaleEnabled(true);
		mChart2.setDrawGridBackground(true);
		// mChart.setHighlightPerDragEnabled(true);
		// 如果禁用，缩放可以在X和Y轴分别做
		mChart2.setPinchZoom(false);
		mChart2.setScaleXEnabled(false);
		mChart2.setScaleYEnabled(false);
		// 设置可选背景色
		mChart2.setBackgroundColor(0x00000000);
		mChart2.setGridBackgroundColor(0x00000000);

		// 设置比例图标示，就是那个一组y的value的
		Legend l = mChart2.getLegend();
		l.setEnabled(false);

		// TODO X轴属性设置
		XAxis xAxis = mChart2.getXAxis();
		// 设置避免第一个和最后一个坐标值被裁切掉
		// xAxis.setAvoidFirstLastClipping(true);
		// 设置X轴字体和颜色
		xAxis.setTextSize(10f);
		xAxis.setTextColor(Color.BLACK);
		// 设置画X轴网格线
		xAxis.setGridColor(Color.LTGRAY);
		xAxis.setGridLineWidth(1.0f);
		xAxis.setDrawGridLines(false);
		// 设置画X轴轴线
		xAxis.setDrawAxisLine(false);
		// 设置X轴显示坐标的间隔大小
		xAxis.setLabelsToSkip(0);
		xAxis.setPosition(XAxisPosition.BOTTOM);// 让x轴在下面
		xAxis.setAxisLineColor(Color.LTGRAY);
		xAxis.setAxisLineWidth(1.0f);

		leftAxis2 = mChart2.getAxisLeft();
		// 设置Y轴字体和颜色
		leftAxis2.setTextColor(0x88FFFFFF);// Y轴字体颜色
		leftAxis2.setAxisMinValue(200);
		leftAxis2.setAxisMinValue(20);
		leftAxis2.setLabelCount(5, false);
		leftAxis2.setDrawGridLines(false);// 显示Y轴网格线
		leftAxis2.setDrawAxisLine(true);// 显示Y轴
		leftAxis2.setAxisLineColor(0x66FFFFFF);// Y轴颜色
		leftAxis2.setAxisLineWidth(1.0f);// Y轴线宽
		leftAxis2.setStartAtZero(false);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initViews(view);
		isFirstIn = false;
		getData(Urls.GET_MAIN + "userId="
				+ SettingUtils.get(getActivity(), "userId", ""));
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isFirstIn == false) {
			if (isVisibleToUser) {
				getData(Urls.GET_MAIN + "userId="
						+ SettingUtils.get(getActivity(), "userId", ""));
			}
		}
	}

	/**
	 * TODO 获取首页数据
	 * 
	 * @param url
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void getData(String url) {
		fh.get(url, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String content = (String) t;
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				ArrayList<String> x = new ArrayList<String>();
				ArrayList<Entry> y1 = new ArrayList<Entry>();
				if (common.getStatus() != 1) {
					home_xueya_healthnumber_quan.setProgress(0);
					// ToastUtils.TextToast(getActivity(), common.getMsg());
					for (int i = 0; i < 3; i++) {
						x.add(" ");
					}
					y1.add(new Entry(50, 0));
					y1.add(new Entry(100, 1));
					y1.add(new Entry(150, 2));
					setDataToChart(x, y1);
					setDataToChart2(x, y1);
					home_xueya_healthnumber_quan.setVisibility(View.INVISIBLE);
					return;
				}

				XueyaShouyeBean bean = gson.fromJson(content,
						XueyaShouyeBean.class);
				data = bean.getData();
				if (data != null) {
					home_xueya_celiangtime.setText(data.getMeasureTime());
					home_xueya_shousuoya.setText(data.getBloodPressureClose()
							+ "");
					home_xueya_shuzhangya.setText(data.getBloodPressureOpen()
							+ "");
					home_xueya_pulse.setText(data.getPulse() + "");
					int healthNumber = data.getHealthNumber();
					if (healthNumber < 35) {
						home_xueya_healthnumber_quan
								.setFrontCircleColor(getResources().getColor(
										R.color.blood_pressure_6));
						home_xueya_healthnumber_quan
								.setTextColor(getResources().getColor(
										R.color.blood_pressure_6));
					} else if (healthNumber >= 35 && healthNumber < 50) {
						home_xueya_healthnumber_quan
								.setFrontCircleColor(getResources().getColor(
										R.color.blood_pressure_5));
						home_xueya_healthnumber_quan
								.setTextColor(getResources().getColor(
										R.color.blood_pressure_5));
					} else if (healthNumber >= 50 && healthNumber < 65) {
						home_xueya_healthnumber_quan
								.setFrontCircleColor(getResources().getColor(
										R.color.blood_pressure_4));
						home_xueya_healthnumber_quan
								.setTextColor(getResources().getColor(
										R.color.blood_pressure_4));
					} else if (healthNumber >= 65 && healthNumber < 73) {
						home_xueya_healthnumber_quan
								.setFrontCircleColor(getResources().getColor(
										R.color.blood_pressure_3));
						home_xueya_healthnumber_quan
								.setTextColor(getResources().getColor(
										R.color.blood_pressure_3));
					} else if (healthNumber >= 73 && healthNumber < 80) {
						home_xueya_healthnumber_quan
								.setFrontCircleColor(getResources().getColor(
										R.color.blood_pressure_2));
						home_xueya_healthnumber_quan
								.setTextColor(getResources().getColor(
										R.color.blood_pressure_2));
					} else if (healthNumber >= 80) {
						home_xueya_healthnumber_quan
								.setFrontCircleColor(getResources().getColor(
										R.color.blood_pressure_1));
						home_xueya_healthnumber_quan
								.setTextColor(getResources().getColor(
										R.color.blood_pressure_1));
					}
					home_xueya_healthnumber_quan.setProgress(0);
					home_xueya_healthnumber_quan
							.setTargetProgress(healthNumber);
					home_xueya_shousuoya_average.setText(data
							.getBloodPressureCloseAvg() + "");
					home_xueya_shouzhangya_average.setText(data
							.getBloodPressureOpenAvg() + "");
					home_xueya_shousuoya_max.setText(data
							.getBloodPressureCloseMax() + "");
					home_xueya_shouzhangya_max.setText(data
							.getBloodPressureOpenMax() + "");

					for (int i = 0; i < 3; i++) {
						x.add(" ");
					}
					closeList = data.getBloodPressureCloseList();

					if (closeList != null && closeList.size() > 0) {
						for (int i = 0; i < closeList.size(); i++) {
							// 收缩压
							y1.add(new Entry(closeList.get(i), i));
						}
						setDataToChart(x, y1);
					} else {
						y1.add(new Entry(50, 0));
						y1.add(new Entry(100, 1));
						y1.add(new Entry(150, 2));
						setDataToChart(x, y1);
					}

					openList = data.getBloodPressureOpenList();
					ArrayList<Entry> y2 = new ArrayList<Entry>();
					if (openList != null && openList.size() > 0) {
						if (openList != null && openList.size() > 0) {
							for (int i = 0; i < openList.size(); i++) {
								// 收缩压
								y2.add(new Entry(openList.get(i), i));
							}
							setDataToChart2(x, y2);
						}
					} else {
						setDataToChart2(x, y2);
					}
				}
			}

		});
	}

	/**
	 * TODO 折线图数据设置1
	 * 
	 * @param y1
	 */
	private void setDataToChart(ArrayList<String> x, ArrayList<Entry> y1) {
		LineDataSet set = new LineDataSet(y1, getString(R.string.xueya_celiang_shoudong_ssy));
		if (closeList == null || closeList.size() <= 0) {
			set.setCircleSize(0.1f);
			set.setColor(Color.parseColor("#00207BDA"));
			set.setCircleColor(Color.parseColor("#00207BDA"));
		} else {
			set.setCircleSize(4f);
			set.setColor(0x66FFFFFF);
			set.setCircleColor(0x88FFFFFF);
		}

		set.setHighlightEnabled(false);
		set.setLineWidth(2f);
		set.enableDashedLine(1f, 0f, 0f);
		// 设置K线数值圆点颜色和大小
		set.setDrawCircleHole(false);

		ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
		dataSets.add(set);

		// 用数据集创建数据对象
		LineData data = new LineData(x, dataSets);
		data.setDrawValues(false);

		// 将数据设置到折线图中
		if (mChart1 != null) {
			if (closeList != null && closeList.size() >= 2) {
				leftAxis.setAxisMaxValue((int) (Collections.max(closeList) + 20));
			} else {
				leftAxis.setAxisMinValue(50);
				leftAxis.setAxisMaxValue(160);
			}
			mChart1.setData(data);
			mChart1.setScaleMinima(x.size() / 7f, 1f);
			mChart1.invalidate();
		}
	}

	/**
	 * TODO 折线图数据设置1
	 * 
	 * @param x
	 * 
	 * @param y1
	 */
	private void setDataToChart2(ArrayList<String> x, ArrayList<Entry> y1) {
		LineDataSet set = new LineDataSet(y1, getString(R.string.xueya_celiang_shoudong_ssy));
		if (closeList == null || closeList.size() <= 0) {
			set.setCircleSize(0.1f);
			set.setColor(Color.parseColor("#00207BDA"));
			set.setCircleColor(Color.parseColor("#00207BDA"));
		} else {
			set.setCircleSize(4f);
			set.setColor(0x66FFFFFF);
			set.setCircleColor(0x88FFFFFF);
		}
		set.setHighlightEnabled(false);
		set.setLineWidth(2f);
		set.enableDashedLine(1f, 0f, 0f);
		// 设置K线数值圆点颜色和大小
		set.setDrawCircleHole(false);

		ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
		dataSets.add(set);

		// 用数据集创建数据对象
		LineData data = new LineData(x, dataSets);
		data.setDrawValues(false);

		// 将数据设置到折线图中
		if (mChart2 != null) {
			if (openList != null && openList.size() >= 2) {
				leftAxis2
						.setAxisMaxValue((int) (Collections.max(openList) + 20));
			} else {
				leftAxis2.setAxisMinValue(50);
				leftAxis2.setAxisMaxValue(160);
			}
			mChart2.setData(data);
			mChart2.setScaleMinima(x.size() / 7f, 1f);
			mChart2.invalidate();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.xueya_celiang:
			getBlueTooth();
			break;

		}

	}

	@Override
	public void onDestroyView() {
		mChart1 = null;
		mChart2 = null;
		xueya_celiang = null;
		util = null;
		home_xueya_celiangtime = null;
		home_xueya_shousuoya = null;
		home_xueya_shuzhangya = null;
		home_xueya_pulse = null;
		home_xueya_healthnumber_quan = null;
		home_xueya_shousuoya_average = null;
		home_xueya_shouzhangya_average = null;
		home_xueya_shousuoya_max = null;
		home_xueya_shouzhangya_max = null;
		mAbHttpUtil = null;
		gson = null;
		view = null;
		leftAxis = null;
		leftAxis2 = null;
		fh = null;
		data = null;
		bluetoothAdapter = null;
		if (closeList != null) {
			closeList.clear();
			closeList = null;
		}
		if (openList != null) {
			openList.clear();
			openList = null;
		}

		System.gc();
		super.onDestroyView();
	}

	private void getBlueTooth() {
		// 检查设备是否支持蓝牙
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!bluetoothAdapter.isEnabled()) {
			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			// 设置蓝牙可见性，最多300秒
			intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			getActivity().startActivityForResult(intent, 111);
			// context.startActivity(intent);

		} else {
			if (util == null) {
				util = new BlueToothUtil(getActivity());
			}
			util.setActivity(getActivity());
			util.setBlueTooth();
		}
	}
}
