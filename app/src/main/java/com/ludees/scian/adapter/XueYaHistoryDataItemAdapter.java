package com.ludees.scian.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ludees.scian.R;
import com.ludees.scian.entity.XueYaTimeBase;

public class XueYaHistoryDataItemAdapter extends BaseAdapter {
	private Context mContext;
	private List<XueYaTimeBase> list;
	private XueYaHistoryItemViewHolder vh;
	/**
	 * 血压字体颜色
	 */
	private int[] xueyaTextColors = { R.color.blood_pressure_1,
			R.color.blood_pressure_2, R.color.blood_pressure_3,
			R.color.blood_pressure_4, R.color.blood_pressure_5,
			R.color.blood_pressure_6, };

	public XueYaHistoryDataItemAdapter(Context context, List<XueYaTimeBase> list) {
		this.mContext = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO
		if (list != null) {
			return list.size();
		} else {
			return 0;
		}

	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO
		if (convertView == null) {
			vh = new XueYaHistoryItemViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.xueya_history_data_item2, null);
			vh.time = (TextView) convertView.findViewById(R.id.xueya_data_time);
			vh.num = (TextView) convertView
					.findViewById(R.id.xueya_history_data_num);
			vh.maibo = (TextView) convertView
					.findViewById(R.id.xueya_history_data_maibo);
			convertView.setTag(vh);
		} else {
			vh = (XueYaHistoryItemViewHolder) convertView.getTag();
		}
		XueYaTimeBase base = list.get(position);
		vh.time.setText(base.getTime());
		vh.num.setText(base.getBloodPressureClose() + "/"
				+ base.getBloodPressureOpen());
		int gaoya = base.getBloodPressureClose();
		int diya = base.getBloodPressureOpen();
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
		vh.maibo.setText(String.valueOf(base.getPulse()));

		return convertView;
	}

	public class XueYaHistoryItemViewHolder {
		TextView time;
		TextView num;
		TextView maibo;
	}
}
