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
import com.en.scian.entity.MyFriend_liuyanMessage;
import com.en.scian.util.SettingUtils;

/**
 * 留言消息列表adapter
 * 
 * @author jiyx
 * 
 */
public class MyFriendMessageDetailAdapter extends BaseAdapter {

	private Context mContext;
	private List<MyFriend_liuyanMessage> list;

	public MyFriendMessageDetailAdapter(Context context,
			List<MyFriend_liuyanMessage> list) {
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
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.myfriend_info_list_item, null);
			holder.myfriend_item_list_item_person = (TextView) convertView
					.findViewById(R.id.myfriend_item_list_item_person);
			holder.myfriend_item_list_item_content = (TextView) convertView
					.findViewById(R.id.myfriend_item_list_item_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MyFriend_liuyanMessage message = list.get(position);
		if (message.getFriendsRealName().equals(
				SettingUtils.get(mContext, "realName", ""))) {
			holder.myfriend_item_list_item_person.setText(mContext.getResources().getString(R.string.wzly, message.getMessageTime()));
		} else {
			holder.myfriend_item_list_item_person.setText(message
					.getFriendsRealName()
					+ mContext.getResources().getString(R.string.zly, message.getMessageTime()));
		}
		holder.myfriend_item_list_item_content.setText(message
				.getMessageContent());
		return convertView;
	}

	private class ViewHolder {
		TextView myfriend_item_list_item_person;
		TextView myfriend_item_list_item_content;
	}

}
