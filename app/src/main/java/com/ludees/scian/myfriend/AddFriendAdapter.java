package com.ludees.scian.myfriend;

import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.image.AbImageLoader;
import com.ludees.scian.BaseActivity;
import com.ludees.scian.R;
import com.ludees.scian.entity.FriendInfo;
import com.ludees.scian.entity.ResponseCommon;
import com.ludees.scian.network.Urls;
import com.ludees.scian.util.SettingUtils;
import com.ludees.scian.util.ToastUtils;
import com.google.gson.Gson;

public class AddFriendAdapter extends BaseAdapter {

	private Context mContext;
	private List<FriendInfo> list;

	// private AbHttpUtil mAbHttpUtil;
	Gson gson = new Gson();
	private AbImageLoader imageLoader;
	private FinalHttp fh;

	public AddFriendAdapter(Context context, List<FriendInfo> list) {
		this.mContext = context;
		this.list = list;

		imageLoader = new AbImageLoader(mContext);

		fh = new FinalHttp();
		fh.configTimeout(15 * 1000);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint({ "InflateParams" })
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.myfriend_add_item, null);
			holder.friend_head = (ImageView) convertView
					.findViewById(R.id.myfriend_add_item_head);
			holder.friend_name = (TextView) convertView
					.findViewById(R.id.myfriend_add_item_name);
			holder.friend_content = (TextView) convertView
					.findViewById(R.id.myfriend_add_item_content);
			holder.friend_addBtn = (Button) convertView
					.findViewById(R.id.myfriend_add_item_addBtn);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final FriendInfo info = list.get(position);
		if (!TextUtils.isEmpty(info.getUserPic())) {
			imageLoader.display(holder.friend_head, info.getUserPic());
		} else {
			holder.friend_head
					.setImageResource(R.drawable.personalcenter_head_square_bg);
		}
		holder.friend_name.setText(info.getRealName());
		holder.friend_content.setText(mContext.getResources().getString(R.string.haoyouxinxijieshao));
		holder.friend_addBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AjaxParams params = new AjaxParams();
				params.put("userId", SettingUtils.get(mContext, "userId", ""));
				params.put("friendsId", info.getUserId() + "");
				sendFriendVerificationForURL(Urls.SEND_FRIENDS_VERIFICATION,
						params, position);
			}
		});
		return convertView;
	}

	/**
	 * 发送好友验证
	 * 
	 * @param url
	 * @param params
	 * @param position
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void sendFriendVerificationForURL(String url,
			AjaxParams params, final int position) {
		fh.post(url,params, new AjaxCallBack() {
			private ProgressDialog pd;

			@Override
			public void onStart() {
				super.onStart();
				pd = new ProgressDialog(mContext);
				pd.setCancelable(false);
				pd.setMessage(mContext.getResources().getString(R.string.zhengzaifasonghaoyouyanzheng));
				pd.show();
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				pd.dismiss();
				ToastUtils.TextToast(mContext, mContext.getResources().getString(R.string.fuwuqilianjieyichang));
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				pd.dismiss();
				String content = (String) t;
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() == 0) {
					((BaseActivity) mContext).prompt(common.getMsg());
					return;
				} else if (common.getStatus() == 1) {
					// 好友验证发送成功
					((BaseActivity) mContext).prompt(common.getMsg());
					list.remove(position);
					notifyDataSetChanged();
				}
			}
		});
	}

	private class ViewHolder {
		ImageView friend_head;
		TextView friend_name;
		TextView friend_content;
		Button friend_addBtn;
	}
}
