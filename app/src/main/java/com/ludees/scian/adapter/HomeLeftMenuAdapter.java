package com.ludees.scian.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ludees.scian.R;

public class HomeLeftMenuAdapter extends BaseAdapter {
	int[] left_menu_imgs = { R.drawable.home_left_menu_personalinfo,
			R.drawable.home_left_menu_message,
			R.drawable.home_left_menu_mydevices,
			R.drawable.home_left_menu_myqrcode,
			R.drawable.home_left_menu_setting };
	int[] left_menu_texts = { R.string.home_left_menu_personalinfo_text,
			R.string.home_left_menu_message_text,
			R.string.home_left_menu_mydevices_text,
			R.string.home_left_menu_myqrcode_text,
			R.string.home_left_menu_setting_text, };
	private Context mContext;

	public HomeLeftMenuAdapter(Context context) {
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return left_menu_imgs.length;
	}

	@Override
	public Object getItem(int position) {
		return left_menu_imgs[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.home_left_menu_lv_item, null);
			holder.leftImg = (ImageView) convertView
					.findViewById(R.id.home_left_menu_lv_item_left_img);
			holder.middleImg = (ImageView) convertView
					.findViewById(R.id.home_left_menu_lv_item_middle_img);
			holder.rightText = (TextView) convertView
					.findViewById(R.id.home_left_menu_lv_item_right_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.middleImg.setImageResource(left_menu_imgs[position]);
		holder.rightText.setText(mContext.getResources().getString(
				left_menu_texts[position]));
		return convertView;
	}

	private class ViewHolder {
		ImageView leftImg;
		ImageView middleImg;
		TextView rightText;
	}
}
