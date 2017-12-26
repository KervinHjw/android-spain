package com.en.scian.messagecenter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.en.scian.BaseActivity;
import com.en.scian.R;
import com.en.scian.entity.FriendVerificationMessage;
import com.en.scian.entity.ResponseCommon;
import com.en.scian.network.Urls;
import com.en.scian.util.ToastUtils;
import com.google.gson.Gson;

/**
 * 好友列表adapter
 * 
 * @author jiyx
 * 
 */
public class MessageCenterFriendMessageAdapter extends BaseAdapter {

	private Context mContext;
	private List<FriendVerificationMessage> list;

	private AbHttpUtil mAbHttpUtil;
	Gson gson = new Gson();

	public MessageCenterFriendMessageAdapter(Context context,
			List<FriendVerificationMessage> list) {
		this.mContext = context;
		this.list = list;

		mAbHttpUtil = AbHttpUtil.getInstance(mContext);
		mAbHttpUtil.setTimeout(10000);
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

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.messagecenter_messagelist_item_0, null);
			holder.advice_date = (TextView) convertView
					.findViewById(R.id.messagecenter_messagelist_item_date);
			holder.advice_content = (TextView) convertView
					.findViewById(R.id.messagecenter_messagelist_item_content);
			holder.messagecenter_messagelist_item_bt = (Button) convertView
					.findViewById(R.id.messagecenter_messagelist_item_bt);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final FriendVerificationMessage common = list.get(position);
		holder.advice_date.setText(common.getCreateTime());
		holder.advice_content.setText(common.getRealName() + mContext.getResources().getString(R.string.qingqiutianjianiweihaoyou));
		holder.messagecenter_messagelist_item_bt
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						AbRequestParams params = new AbRequestParams();
						params.put("messageId", common.getMessageId() + "");
						agreeToAddFriend(Urls.AGREE_TO_ADD_FRIENDS, params,
								position);
					}

				});
		return convertView;
	}

	private class ViewHolder {
		TextView advice_date;
		TextView advice_content;
		Button messagecenter_messagelist_item_bt;
	}

	/**
	 * 同意添加好友
	 * 
	 * @param url
	 * @param params
	 * @param position
	 */
	private void agreeToAddFriend(String url, AbRequestParams params,
			final int position) {
		mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
			private ProgressDialog pd;

			// 开始执行前
			@Override
			public void onStart() {
				pd = new ProgressDialog(mContext);
				pd.setCancelable(false);
				pd.setMessage(mContext.getResources().getString(R.string.xinxizhengzaigengxin));
				pd.show();

			}

			// 完成后调用，失败，成功
			@Override
			public void onFinish() {
				// 移除进度框
				pd.dismiss();
			}

			// 失败，调用
			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				pd.dismiss();
				ToastUtils.TextToast(mContext, mContext.getResources().getString(R.string.wangluolianjieshibai));
			}

			// 获取数据成功会调用这里
			@Override
			public void onSuccess(int status, String content) {
				pd.dismiss();
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() == 0) {
					((BaseActivity) mContext).prompt(common.getMsg());
					return;
				} else if (common.getStatus() == 1) {
					((BaseActivity) mContext).prompt(common.getMsg());
					list.remove(position);
					notifyDataSetChanged();
				}
			}
		});

	}
}
