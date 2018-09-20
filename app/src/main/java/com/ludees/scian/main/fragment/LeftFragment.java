package com.ludees.scian.main.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.image.AbImageLoader;
import com.ludees.scian.R;
import com.ludees.scian.dao.PushMessageDaoImpl;
import com.ludees.scian.entity.PushMessage;
import com.ludees.scian.messagecenter.MessageCenterActivity;
import com.ludees.scian.mydevices.MyDevicesActivity;
import com.ludees.scian.personalcenter.PersonalDataActivity;
import com.ludees.scian.qrcode.QRCodeActivity;
import com.ludees.scian.setting.SettingActivity;
import com.ludees.scian.util.SettingUtils;
import com.ludees.scian.util.ToastUtils;
import com.ludees.scian.view.CircleImageView;

/**
 * 侧拉栏
 * 
 * @author zhangp
 * 
 */
@SuppressLint("InflateParams")
public class LeftFragment extends Fragment implements OnClickListener,
		OnTouchListener {
	private Context mContext;
	/**
	 * 个人资料
	 */
	private LinearLayout home_left_layout_personalinfo;
	private ImageView home_left_layout_personalinfo_left_img;
	/**
	 * 消息中心
	 */
	private LinearLayout home_left_layout_message;
	private ImageView home_left_layout_message_left_img;
	/**
	 * 我的设备
	 */
	private LinearLayout home_left_layout_mydevices;
	private ImageView home_left_layout_mydevices_left_img;
	/**
	 * 二维码
	 */
	private LinearLayout home_left_layout_myqrcode;
	private ImageView home_left_layout_myqrcode_left_img;
	/**
	 * 设置
	 */
	private LinearLayout home_left_layout_setting;
	private ImageView home_left_layout_setting_left_img;
	/**
	 * 左侧菜单头部
	 */
	private CircleImageView menu_left_user_head_pic;
	private TextView menu_left_user_name;
	private TextView menu_left_user_phone;
	private static ImageView menu_left_message_hongdian;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext = getActivity();
		View v = inflater.inflate(R.layout.home_left_menu, null);
		initView(v);
		return v;
	}

	private void initView(View v) {
		menu_left_user_head_pic = (CircleImageView) v
				.findViewById(R.id.home_menu_left_user_head_pic);
		menu_left_user_head_pic.setBorderColor(0x00000000);
		menu_left_user_name = (TextView) v
				.findViewById(R.id.home_menu_left_user_name);
		menu_left_user_phone = (TextView) v
				.findViewById(R.id.home_menu_left_user_phone);
		/*menu_left_message_hongdian = (ImageView) v
				.findViewById(R.id.home_left_message_hongdian);*/
		// 个人资料
		home_left_layout_personalinfo = (LinearLayout) v
				.findViewById(R.id.home_left_layout_personalinfo);
		home_left_layout_personalinfo_left_img = (ImageView) v
				.findViewById(R.id.home_left_layout_personalinfo_left_img);
		home_left_layout_personalinfo.setOnTouchListener(this);
		home_left_layout_personalinfo.setOnClickListener(this);
		// 消息中心
		/*home_left_layout_message = (LinearLayout) v
				.findViewById(R.id.home_left_layout_message);
		home_left_layout_message_left_img = (ImageView) v
				.findViewById(R.id.home_left_layout_message_left_img);*/
		home_left_layout_message.setOnTouchListener(this);
		home_left_layout_message.setOnClickListener(this);
		// 我的设备
		/*home_left_layout_mydevices = (LinearLayout) v
				.findViewById(R.id.home_left_layout_mydevices);
		home_left_layout_mydevices_left_img = (ImageView) v
				.findViewById(R.id.home_left_layout_mydevices_left_img);*/
		home_left_layout_mydevices.setOnTouchListener(this);
		home_left_layout_mydevices.setOnClickListener(this);
		// 二维码
		/*home_left_layout_myqrcode = (LinearLayout) v
				.findViewById(R.id.home_left_layout_myqrcode);
		home_left_layout_myqrcode_left_img = (ImageView) v
				.findViewById(R.id.home_left_layout_myqrcode_left_img);*/
		home_left_layout_myqrcode.setOnTouchListener(this);
		home_left_layout_myqrcode.setOnClickListener(this);
		// 设置
		home_left_layout_setting = (LinearLayout) v
				.findViewById(R.id.home_left_layout_setting);
		home_left_layout_setting_left_img = (ImageView) v
				.findViewById(R.id.home_left_layout_setting_left_img);
		home_left_layout_setting.setOnTouchListener(this);
		home_left_layout_setting.setOnClickListener(this);

		// 设置数据
		String userPic = SettingUtils.get(mContext, "userPic", "");
		String realName = SettingUtils.get(mContext, "realName", "");
		String phone = SettingUtils.get(mContext, "phone", "");
		AbImageLoader loader = new AbImageLoader(mContext);
		if (!TextUtils.isEmpty(userPic)) {
			loader.display(menu_left_user_head_pic, userPic);
		}
		if (!TextUtils.isEmpty(realName)) {
			menu_left_user_name.setText(realName);
		} else {
			menu_left_user_name.setText("-");
		}
		if (!TextUtils.isEmpty(phone)) {
			menu_left_user_phone.setText(phone);
		} else {
			menu_left_user_phone.setText("-");
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// TODO 点击事件监听
		if (v == home_left_layout_personalinfo) {// 个人资料
			startActivity(new Intent(mContext, PersonalDataActivity.class));
			getActivity().overridePendingTransition(
					android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);

		} else if (v == home_left_layout_message) {// 消息中心
			startActivity(new Intent(mContext, MessageCenterActivity.class));
			getActivity().overridePendingTransition(
					android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
		} else if (v == home_left_layout_mydevices) {// 我的设备
			startActivity(new Intent(mContext, MyDevicesActivity.class));
			getActivity().overridePendingTransition(
					android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
		} else if (v == home_left_layout_myqrcode) {// 二维码
			String userId = SettingUtils.get(mContext, "userId", "");
			if (!TextUtils.isEmpty(userId)) {
				startActivity(new Intent(mContext, QRCodeActivity.class)
						.putExtra("QR", userId));
				getActivity().overridePendingTransition(
						android.R.anim.slide_in_left,
						android.R.anim.slide_out_right);
			} else {
				ToastUtils.TextToast(mContext, mContext.getResources().getString(R.string.yonghuxinxiweikong));
			}
		} else if (v == home_left_layout_setting) {// 设置
			startActivity(new Intent(mContext, SettingActivity.class));
			getActivity().overridePendingTransition(
					android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
		}

	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO 触摸事件监听
		if (v == home_left_layout_personalinfo) {// 个人资料
			if (event.getAction() == MotionEvent.ACTION_UP) {
				home_left_layout_personalinfo_left_img
						.setVisibility(View.INVISIBLE);
			} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
				home_left_layout_personalinfo_left_img
						.setVisibility(View.VISIBLE);
			}
		} else if (v == home_left_layout_message) {// 消息中心
			if (event.getAction() == MotionEvent.ACTION_UP) {
				home_left_layout_message_left_img.setVisibility(View.INVISIBLE);
			}
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				home_left_layout_message_left_img.setVisibility(View.VISIBLE);
			}
		} else if (v == home_left_layout_mydevices) {// 我的设备
			if (event.getAction() == MotionEvent.ACTION_UP) {
				home_left_layout_mydevices_left_img
						.setVisibility(View.INVISIBLE);
			} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
				home_left_layout_mydevices_left_img.setVisibility(View.VISIBLE);
			}
		} else if (v == home_left_layout_myqrcode) {// 二维码
			if (event.getAction() == MotionEvent.ACTION_UP) {
				home_left_layout_myqrcode_left_img
						.setVisibility(View.INVISIBLE);
			} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
				home_left_layout_myqrcode_left_img.setVisibility(View.VISIBLE);
			}
		} else if (v == home_left_layout_setting) {// 设置
			if (event.getAction() == MotionEvent.ACTION_UP) {
				home_left_layout_setting_left_img.setVisibility(View.INVISIBLE);
			} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
				home_left_layout_setting_left_img.setVisibility(View.VISIBLE);
			}
		}
		return false;
	}

	/**
	 * TODO 内部广播，用来刷新在有新消息推送时刷新UI消息红点
	 * 
	 * @author jiyx
	 * 
	 */
	public static class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if ("com.aaa".equals(intent.getAction())) {
				menu_left_message_hongdian.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 刷新消息中心红点
		PushMessageDaoImpl pushMessageDao = new PushMessageDaoImpl(
				getActivity());
		List<PushMessage> list = pushMessageDao.find();
		if (list.size() == 4) {
			if (list.get(0).getIsRead() == 1 /* || list.get(1).getIsRead() == 1 */
					|| list.get(2).getIsRead() == 1
					|| list.get(3).getIsRead() == 1) {
				menu_left_message_hongdian.setVisibility(View.VISIBLE);
			} else {
				menu_left_message_hongdian.setVisibility(View.INVISIBLE);
			}
		}
	}
}
