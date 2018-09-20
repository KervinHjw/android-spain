package com.ludees.scian.time;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ludees.scian.R;
import com.ludees.scian.entity.RemindInfo;
import com.ludees.scian.entity.RemindInfoBean;
import com.ludees.scian.entity.ResponseCommon;
import com.ludees.scian.network.Urls;
import com.ludees.scian.util.SettingUtils;
import com.ludees.scian.util.ToastUtils;
import com.google.gson.Gson;
import com.socks.zlistview.adapter.BaseSwipeAdapter;
import com.socks.zlistview.enums.DragEdge;
import com.socks.zlistview.enums.ShowMode;
import com.socks.zlistview.widget.ZSwipeItem;

public class TimeAdapter2 extends BaseSwipeAdapter {
	private Activity mContext;
	private List<RemindInfo> list;
	Gson gson = new Gson();
	private int stateChange = 1;
	private FinalHttp fh;

	public TimeAdapter2(Activity context, List<RemindInfo> list) {
		this.mContext = context;
		this.list = list;
		fh = new FinalHttp();
		fh.configTimeout(15 * 1000);
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
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getSwipeLayoutResourceId(int position) {
		return R.id.swipe_item;
	}

	@Override
	public View generateView(int position, ViewGroup parent) {
		return mContext.getLayoutInflater().inflate(R.layout.time_item, parent,
				false);
	}

	@Override
	public void fillValues(final int position, View convertView) {
		final ZSwipeItem swipeItem = (ZSwipeItem) convertView
				.findViewById(R.id.swipe_item);
		LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.ll);
		final ImageView time_item_img = (ImageView) convertView
				.findViewById(R.id.time_item_img);
		TextView time_item_reminder_time = (TextView) convertView
				.findViewById(R.id.time_item_reminder_time);
		final ToggleButton time_item_tbtn = (ToggleButton) convertView
				.findViewById(R.id.time_item_tbtn);

		swipeItem.setShowMode(ShowMode.PullOut);
		swipeItem.setDragEdge(DragEdge.Right);

		final RemindInfo common = list.get(position);
		int state = common.getState();
		if (state == 1) {
			time_item_tbtn.setChecked(true);
			time_item_img.setImageResource(R.drawable.time_item_timeon);
		} else {
			time_item_tbtn.setChecked(false);
			time_item_img.setImageResource(R.drawable.time_item_timeoff);
		}
		time_item_reminder_time.setText(common.getRemindHour() + ":"
				+ common.getRemindMinute());
		// 更新修改时间
		time_item_reminder_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showUpdateTimeDialog(position);
			}

		});
		// 提醒时间开关监听
		time_item_tbtn
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							time_item_img
									.setImageResource(R.drawable.time_item_timeon);
							stateChange = 1;
							time_item_tbtn.setChecked(true);
						} else {
							time_item_img
									.setImageResource(R.drawable.time_item_timeoff);
							stateChange = 2;
							time_item_tbtn.setChecked(false);
						}
						String STATE_CHANGE = Urls.SET_REMIND_INFO_STATE;
						AjaxParams params = new AjaxParams();
						params.put("id", String.valueOf(common.getId()));
						params.put("state", String.valueOf(stateChange));
						changeState(STATE_CHANGE, params);
					}
				});

		// 删除提醒监听
		ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				swipeItem.close();
				RemindInfo info = list.get(position);
				int id = info.getId();
				String URL = Urls.DEL_REMIND_INFO + "id=" + id;
				delRemind(URL, position);
				notifyDataSetChanged();

			}
		});
	}

	private void showUpdateTimeDialog(final int position) {
		RemindInfo common = list.get(position);
		Intent intent = new Intent(mContext, TimeSelectorActivity.class);
		intent.putExtra("time",
				common.getRemindHour() + ":" + common.getRemindMinute());
		intent.putExtra("hour", common.getRemindHour());
		intent.putExtra("minute", common.getRemindMinute());
		intent.putExtra("state", common.getState());
		intent.putExtra("position", position);
		intent.putExtra("id", common.getId());
		intent.putExtra("num", 4);
		mContext.startActivity(intent);
		mContext.finish();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void delRemind(String URL, final int position) {
		fh.get(URL, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				ToastUtils.TextToast(mContext, mContext.getResources().getString(R.string.fuwuqilianjieyichang));
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String content = (String) t;
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					Toast.makeText(mContext, common.getMsg(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(mContext, mContext.getResources().getString(R.string.shanchuchenggong), Toast.LENGTH_SHORT).show();
				list.remove(position);
				notifyDataSetChanged();
			}

		});
	}

	/**
	 * 获取时间列表
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void getData(String URL) {
		fh.get(URL, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				ToastUtils.TextToast(mContext, mContext.getResources().getString(R.string.fuwuqilianjieyichang));
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String content = (String) t;
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					Toast.makeText(mContext, common.getMsg(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				RemindInfoBean info = gson.fromJson(content,
						RemindInfoBean.class);
				List<RemindInfo> list2 = new ArrayList<RemindInfo>();
				list2 = info.getData();
				list.clear();
				list.addAll(list2);
				notifyDataSetChanged();
			}

		});
	}

	/**
	 * 更改时间状态
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void changeState(String URL, AjaxParams params) {
		fh.post(URL, params, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				ToastUtils.TextToast(mContext, mContext.getResources().getString(R.string.fuwuqilianjieyichang));
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String content = (String) t;
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					Toast.makeText(mContext, common.getMsg(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(mContext, mContext.getResources().getString(R.string.zhuangtaigenggaichenggong), Toast.LENGTH_SHORT).show();
				String userId = SettingUtils.get(mContext, "userId", "");
				String URL = Urls.GET_REMIND_LIST
						+ "pageNo=0&pageSize=10&remindType=2&userId=" + userId;
				getData(URL);

			}

		});
	}
}
