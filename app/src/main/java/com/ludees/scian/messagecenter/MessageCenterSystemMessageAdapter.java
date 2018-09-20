package com.ludees.scian.messagecenter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ludees.scian.R;
import com.ludees.scian.entity.SystemMessage;

/**
 * 系统消息列表adapter
 * 
 * @author jiyx
 * 
 */
public class MessageCenterSystemMessageAdapter extends BaseAdapter {

	private Context mContext;
	private List<SystemMessage> list;
	private LayoutInflater inflater;

	public MessageCenterSystemMessageAdapter(Context context,
			List<SystemMessage> list) {
		this.mContext = context;
		this.list = list;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return list.size() > 0 ? list.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO
		convertView = inflater.inflate(
				R.layout.messagecenter_messagelist_item_2, null);
		TextView advice_date = (TextView) convertView
				.findViewById(R.id.messagecenter_messagelist_item_date);
		TextView advice_content = (TextView) convertView
				.findViewById(R.id.messagecenter_messagelist_item_content);
		SystemMessage systemMessage = list.get(position);
		advice_date.setText(systemMessage.getCreateTime());
		advice_content.setText(systemMessage.getMessageName());
		return convertView;
	}

}
