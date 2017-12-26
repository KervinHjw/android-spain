package com.en.scian.myfriend;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.en.scian.R;
import com.en.scian.entity.MyFriend;
import com.en.scian.util.DisplayUtils;
import com.en.scian.view.CustomProgressBarView;
import com.en.scian.view.RoundImageView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MyFriendAdapter extends BaseAdapter {
	private Context mContext;
	private List<MyFriend> list;
	private ImageLoader imageLoader;
	/**
	 * 血压字体颜色
	 */
	private int[] xueyaTextColors = { R.color.blood_pressure_1,
			R.color.blood_pressure_2, R.color.blood_pressure_3,
			R.color.blood_pressure_4, R.color.blood_pressure_5,
			R.color.blood_pressure_6, };

	public MyFriendAdapter(Context context, List<MyFriend> list) {
		this.mContext = context;
		this.list = list;
		imageLoader = ImageLoader.getInstance();
		@SuppressWarnings("deprecation")
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				mContext).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		imageLoader.init(config);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO
		ViewHolder holder = null;
		MyFriend myFriend = list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.myfriend_item, null);
			holder.myfriend_item_head = (RoundImageView) convertView
					.findViewById(R.id.myfriend_item_head);
			holder.myfriend_item_head.setType(1);
			holder.myfriend_item_name = (TextView) convertView
					.findViewById(R.id.myfriend_item_name);
			holder.myfriend_item_healthNumber_quan = (CustomProgressBarView) convertView
					.findViewById(R.id.myfriend_item_healthNumber_quan);
			// holder.myfriend_item_healthNumber = (TextView) convertView
			// .findViewById(R.id.myfriend_item_healthNumber);
			holder.myfriend_item_healthNumber_quan.setRadius(DisplayUtils
					.dip2px(mContext, 40));
			holder.myfriend_item_healthNumber_quan
					.setBackStrokeCircleWidth(DisplayUtils.dip2px(mContext, 5));
			holder.myfriend_item_healthNumber_quan
					.setFrontStrokeCircleWidth(DisplayUtils.dip2px(mContext, 5));
			holder.myfriend_item_healthNumber_quan
					.setBackCircleColor(0x22AAAAAA);
			holder.myfriend_item_healthNumber_quan.setTextSize(DisplayUtils
					.dip2px(mContext, 32));

			holder.myfriend_item_xueya = (TextView) convertView
					.findViewById(R.id.myfriend_item_xueya);
			holder.myfriend_item_pulse = (TextView) convertView
					.findViewById(R.id.myfriend_item_pulse);
			holder.myfriend_item_bloodPressureDiffer = (TextView) convertView
					.findViewById(R.id.myfriend_item_bloodPressureDiffer);
			holder.myfriend_item_celiangshijian = (TextView) convertView
					.findViewById(R.id.myfriend_item_celiangshijian);
			convertView.setTag(holder);
			holder.myfriend_item_head.setTag(myFriend);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		myFriend = list.get(position);
		holder.myfriend_item_head.setTag(myFriend.getUserPic());
		holder.myfriend_item_head
				.setBackgroundResource(R.drawable.normal_user__circle_head);
		if (holder.myfriend_item_head.getTag() != null
				&& holder.myfriend_item_head.getTag().equals(
						myFriend.getUserPic())) {
			imageLoader.displayImage(myFriend.getUserPic(),
					holder.myfriend_item_head);
		}

		holder.myfriend_item_name.setText(myFriend.getRealName());

		int healthNumber = myFriend.getHealthNumber();
		if (healthNumber < 35) {
			if (healthNumber == 0) {// 设置背景圈颜色
				holder.myfriend_item_healthNumber_quan
						.setBackCircleColor(mContext.getResources().getColor(
								R.color.blood_pressure_6));
			} else {// 设置前面圈的颜色
				holder.myfriend_item_healthNumber_quan
						.setFrontCircleColor(mContext.getResources().getColor(
								R.color.blood_pressure_6));
			}
			holder.myfriend_item_healthNumber_quan.setTextColor(mContext
					.getResources().getColor(R.color.blood_pressure_6));
		} else if (healthNumber >= 35 && healthNumber < 50) {
			holder.myfriend_item_healthNumber_quan.setFrontCircleColor(mContext
					.getResources().getColor(R.color.blood_pressure_5));
			holder.myfriend_item_healthNumber_quan.setTextColor(mContext
					.getResources().getColor(R.color.blood_pressure_5));
		} else if (healthNumber >= 50 && healthNumber < 65) {
			holder.myfriend_item_healthNumber_quan.setFrontCircleColor(mContext
					.getResources().getColor(R.color.blood_pressure_4));
			holder.myfriend_item_healthNumber_quan.setTextColor(mContext
					.getResources().getColor(R.color.blood_pressure_4));
		} else if (healthNumber >= 65 && healthNumber < 73) {
			holder.myfriend_item_healthNumber_quan.setFrontCircleColor(mContext
					.getResources().getColor(R.color.blood_pressure_3));
			holder.myfriend_item_healthNumber_quan.setTextColor(mContext
					.getResources().getColor(R.color.blood_pressure_3));
		} else if (healthNumber >= 73 && healthNumber < 80) {
			holder.myfriend_item_healthNumber_quan.setFrontCircleColor(mContext
					.getResources().getColor(R.color.blood_pressure_2));
			holder.myfriend_item_healthNumber_quan.setTextColor(mContext
					.getResources().getColor(R.color.blood_pressure_2));
		} else if (healthNumber >= 80) {
			holder.myfriend_item_healthNumber_quan.setFrontCircleColor(mContext
					.getResources().getColor(R.color.blood_pressure_1));
			holder.myfriend_item_healthNumber_quan.setTextColor(mContext
					.getResources().getColor(R.color.blood_pressure_1));
		}
		holder.myfriend_item_healthNumber_quan.setProgress(healthNumber);
		// holder.myfriend_item_healthNumber_quan.setTargetProgress(healthNumber);

		int gaoya = myFriend.getBloodPressureClose();
		int diya = myFriend.getBloodPressureOpen();
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
		holder.myfriend_item_xueya.setTextColor(mContext.getResources()
				.getColor(xueyaTextColors[x > y ? x : y]));
		holder.myfriend_item_xueya.setText(gaoya + "/" + diya);
		holder.myfriend_item_pulse.setText(myFriend.getPulse() + "");
		holder.myfriend_item_bloodPressureDiffer.setText(myFriend
				.getBloodPressureDiffer() + "");
		holder.myfriend_item_celiangshijian.setText(myFriend.getMeasureTime());
		return convertView;
	}

	private class ViewHolder {
		RoundImageView myfriend_item_head;
		TextView myfriend_item_name;
		CustomProgressBarView myfriend_item_healthNumber_quan;
		// TextView myfriend_item_healthNumber;
		TextView myfriend_item_xueya;
		TextView myfriend_item_pulse;
		TextView myfriend_item_bloodPressureDiffer;
		TextView myfriend_item_celiangshijian;
	}
}
