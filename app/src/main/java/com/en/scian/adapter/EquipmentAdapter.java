package com.en.scian.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.en.scian.R;

/**
 * 设备数据适配器
 * 
 * @author zhangp
 * 
 */
public class EquipmentAdapter extends BaseAdapter {
	private Context mContext;
	private List<String> list;
	private EquipmentViewHolder vh;

	public EquipmentAdapter(Context context, List<String> list) {
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
		if (list != null) {
			return arg0;
		} else {
			return 0;
		}

	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View v, ViewGroup arg2) {
		// TODO 
		if (v == null) {
			vh = new EquipmentViewHolder();
			v = LayoutInflater.from(mContext).inflate(R.layout.equipment_item,
					null);
			vh.name = (TextView) v.findViewById(R.id.equipment_item_name);
			v.setTag(vh);
		} else {
			vh = (EquipmentViewHolder) v.getTag();
		}
		String name = list.get(position);
		vh.name.setText(name);
		return v;
	}

	public class EquipmentViewHolder {
		TextView name;
	}
}
