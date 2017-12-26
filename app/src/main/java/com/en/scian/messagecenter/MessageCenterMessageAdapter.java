package com.en.scian.messagecenter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.en.scian.R;
import com.en.scian.entity.MyFriend_Message;

/**
 * 留言消息列表adapter
 * 
 * @author jiyx
 * 
 */
public class MessageCenterMessageAdapter extends BaseAdapter {

	private Context mContext;
	private List<MyFriend_Message> list;

	public MessageCenterMessageAdapter(Context context,
			List<MyFriend_Message> list) {
		this.mContext = context;
		this.list = list;
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
		convertView = LayoutInflater.from(mContext).inflate(
				R.layout.messagecenter_messagelist_item_1, null);
		TextView advice_date = (TextView) convertView
				.findViewById(R.id.messagecenter_messagelist_item_date);
		TextView advice_content = (TextView) convertView
				.findViewById(R.id.messagecenter_messagelist_item_content);
		MyFriend_Message myFriend_Message = list.get(position);
		advice_date.setText(myFriend_Message.getFriendsRealName() + mContext.getResources().getString(R.string.zly, myFriend_Message.getMessageTime()));
		advice_content.setText(myFriend_Message.getMessageContent());
		return convertView;
	}

}
