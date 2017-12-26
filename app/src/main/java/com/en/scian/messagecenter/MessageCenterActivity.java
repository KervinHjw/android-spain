package com.en.scian.messagecenter;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.en.scian.BaseActivity;
import com.en.scian.ExampleApplication;
import com.en.scian.R;
import com.en.scian.dao.PushMessageDaoImpl;
import com.en.scian.entity.PushMessage;

/**
 * 消息中心
 * 
 * @author jiyx
 * 
 */
public class MessageCenterActivity extends BaseActivity implements
		OnClickListener {
	private TextView title;
	private LinearLayout back;

	private LinearLayout messagecenter_friend;
	private LinearLayout messagecenter_message;
	private LinearLayout messagecenter_systemmessage;

	private ImageView messagecenter_friend_red;
	private ImageView messagecenter_message_red;
	private ImageView messagecenter_systemmessage_red;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messagecenter);

		initViews();
		initListener();
		setData();
		setContext(this);
		setIsResult(true);
	}

	/**
	 * 初始化控件
	 * 
	 * @param view
	 */
	private void initViews() {
		ExampleApplication.getInstance().addActivity(this);
		title = (TextView) this.findViewById(R.id.search_titleText);
		back = (LinearLayout) this.findViewById(R.id.search_leftLayout);

		messagecenter_friend = (LinearLayout) this
				.findViewById(R.id.messagecenter_friend);
		messagecenter_message = (LinearLayout) this
				.findViewById(R.id.messagecenter_message);
		messagecenter_systemmessage = (LinearLayout) this
				.findViewById(R.id.messagecenter_systemmessage);

		messagecenter_friend_red = (ImageView) this
				.findViewById(R.id.messagecenter_red_1);
		messagecenter_message_red = (ImageView) this
				.findViewById(R.id.messagecenter_red_2);
		messagecenter_systemmessage_red = (ImageView) this
				.findViewById(R.id.messagecenter_red_3);
	}

	/**
	 * 设置初始化数据
	 */
	private void setData() {
		title.setText(this.getResources().getString(R.string.home_left_menu_message_text));
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		back.setOnClickListener(this);
		messagecenter_friend.setOnClickListener(this);
		messagecenter_message.setOnClickListener(this);
		messagecenter_systemmessage.setOnClickListener(this);
	}

	@Override
	public void onStart() {
		updataUI();
		super.onStart();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_leftLayout:
			finishPage();
			break;
		case R.id.messagecenter_friend:// 好友请求
			updateMessageReaded("4");
			startActivity(new Intent(this, MessageCenterFriendActivity.class));
			break;
		case R.id.messagecenter_message:// 留言消息
			updateMessageReaded("1");
			startActivity(new Intent(this, MessageCenterMessageActivity.class));
			break;
		case R.id.messagecenter_systemmessage:// 系统消息
			updateMessageReaded("3");
			startActivity(new Intent(this,
					MessageCenterSystemMessageActivity.class));
			break;
		}
	}

	/**
	 * 修改消息为已读状态
	 * 
	 * @param messageType
	 *            :留言消息(1)、系统消息(3)、好友请求(4)
	 */
	public void updateMessageReaded(String messageType) {
		PushMessageDaoImpl dao = new PushMessageDaoImpl(this);
		List<PushMessage> list = dao.find(new String[] { "messageTypeId",
				"pushType", "isRead" }, "pushType=?",
				new String[] { messageType }, null, null, null, null);
		PushMessage pushMessage = list.get(0);
		pushMessage.setPushType(Integer.parseInt(messageType));
		pushMessage.setIsRead(2);
		dao.update(pushMessage);
	}

	/**
	 * TODO 刷新消息红点
	 */
	public void updataUI() {
		PushMessageDaoImpl pushMessageDao = new PushMessageDaoImpl(this);
		List<PushMessage> list = pushMessageDao.find();
		for (int i = 0; i < list.size(); i++) {
			PushMessage pushMessage = list.get(i);
			// 推送类型：1.好友留言 2.定时提醒 3 系统消息 4 好友验证消息
			if (pushMessage.getPushType() == 1) {// TODO 好友留言
				if (pushMessage.getIsRead() == 1) {
					messagecenter_message_red.setVisibility(View.VISIBLE);
				} else {
					messagecenter_message_red.setVisibility(View.INVISIBLE);
				}
			} else if (pushMessage.getPushType() == 3) {// TODO 系统消息
				if (pushMessage.getIsRead() == 1) {
					messagecenter_systemmessage_red.setVisibility(View.VISIBLE);
				} else {
					messagecenter_systemmessage_red
							.setVisibility(View.INVISIBLE);
				}
			} else if (pushMessage.getPushType() == 4) { // TODO 好友验证消息
				if (pushMessage.getIsRead() == 1) {
					messagecenter_friend_red.setVisibility(View.VISIBLE);
				} else {
					messagecenter_friend_red.setVisibility(View.INVISIBLE);
				}
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finishPage();
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		title = null;
		back = null;
		messagecenter_friend = null;
		messagecenter_message = null;
		messagecenter_systemmessage = null;
		messagecenter_friend_red = null;
		messagecenter_message_red = null;
		messagecenter_systemmessage_red = null;
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}

	public void finishPage() {
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		finish();
	}
}
