package com.ludees.scian.adapter;

import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ludees.scian.R;
import com.ludees.scian.entity.XueYaYearBase;

public class XueYaHistoryDataAdapter extends BaseAdapter {
	private Context mContext;
	private List<XueYaYearBase> list;

	/**
	 * 血压字体颜色
	 */
	private int[] xueyaTextColors = { R.color.blood_pressure_1,
			R.color.blood_pressure_2, R.color.blood_pressure_3,
			R.color.blood_pressure_4, R.color.blood_pressure_5,
			R.color.blood_pressure_6, };

	public XueYaHistoryDataAdapter(Context context, List<XueYaYearBase> list) {
		this.mContext = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		if (list != null) {
			return list.size();
		} else {
			return 0;
		}

	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View v, ViewGroup arg2) {
		// TODO
		XueYaHistoryDataViewHolder vh;

		if (v == null) {
			vh = new XueYaHistoryDataViewHolder();
			v = LayoutInflater.from(mContext).inflate(
					R.layout.xueya_history_data_item, null);
			vh.year = (TextView) v.findViewById(R.id.xueya_history_data_year);
			vh.time = (TextView) v.findViewById(R.id.xueya_data_time);
			vh.num = (TextView) v.findViewById(R.id.xueya_history_data_num);
			vh.maibo = (TextView) v.findViewById(R.id.xueya_history_data_maibo);
			vh.rl = (RelativeLayout) v.findViewById(R.id.time_rl);
			vh.cycle = (ImageView) v
					.findViewById(R.id.xueya_data_history_cycle);
			v.setTag(vh);
		} else {
			vh = (XueYaHistoryDataViewHolder) v.getTag();
		}
		XueYaYearBase bean = list.get(position);
		String year = bean.getYear();
		if (year == null || year.equals("") || year.equals("null")) {
			vh.year.setVisibility(View.GONE);
			vh.rl.setVisibility(View.VISIBLE);
			vh.cycle.setVisibility(View.VISIBLE);
			vh.time.setVisibility(View.VISIBLE);
			// vh.cycle.getBackground().setAlpha(130);
			int gaoya = bean.getBloodPressureClose();
			int diya = bean.getBloodPressureOpen();
			// 设置血压字体颜色
			int x = 0, y = 0;
			if (gaoya <= 120) {
				x = 0;
			} else if (gaoya > 120 && gaoya <= 130) {
				x = 1;
			} else if (gaoya > 130 && gaoya <= 140) {
				x = 2;
			} else if (gaoya > 140 && gaoya <= 160) {
				x = 3;
			} else if (gaoya > 160 && gaoya <= 180) {
				x = 4;
			} else if (gaoya > 180) {
				x = 5;
			}
			if (diya <= 80) {
				y = 0;
			} else if (diya > 80 && diya <= 85) {
				y = 1;
			} else if (diya > 85 && diya <= 90) {
				y = 2;
			} else if (diya > 90 && diya <= 100) {
				y = 3;
			} else if (diya > 100 && diya <= 110) {
				y = 4;
			} else if (gaoya > 180 && diya > 110) {
				y = 5;
			}
			vh.num.setTextColor(mContext.getResources().getColor(
					xueyaTextColors[x > y ? x : y]));
			vh.num.setText(gaoya + "/" + diya);
			vh.maibo.setText(String.valueOf(bean.getPulse()));
			String time = bean.getTime();
			if (!TextUtils.isEmpty(time)) {
				String[] split = time.split(mContext.getResources().getString(R.string.time_picker_date));
				if (split.length == 2) {
					vh.time.setText(split[0] + mContext.getResources().getString(R.string.time_picker_date) + "\n" + split[1]);
				} else {
					vh.time.setText(time);
				}
			} else {
				vh.time.setText("");
			}

		} else {
			Calendar c = Calendar.getInstance();// 首先要获取日历对象
			int mYear = c.get(Calendar.YEAR); // 获取当前年份
			if (mYear == Integer.valueOf(year)) {
				vh.year.setVisibility(View.GONE);
				vh.rl.setVisibility(View.GONE);
				vh.cycle.setVisibility(View.GONE);
				vh.time.setVisibility(View.GONE);
			} else {
				vh.year.setVisibility(View.VISIBLE);
				vh.rl.setVisibility(View.GONE);
				vh.cycle.setVisibility(View.GONE);
				vh.time.setVisibility(View.GONE);
				vh.year.setText(bean.getYear());
			}

		}

		return v;
	}

	public class XueYaHistoryDataViewHolder {
		TextView year;
		TextView time;
		TextView num;
		TextView maibo;
		RelativeLayout rl;
		ImageView cycle;
	}

}
