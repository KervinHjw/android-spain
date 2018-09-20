package com.ludees.scian.myfriend;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.image.AbImageLoader;
import com.ludees.scian.BaseActivity;
import com.ludees.scian.ExampleApplication;
import com.ludees.scian.R;
import com.ludees.scian.adapter.MyFriendMessageDetailAdapter;
import com.ludees.scian.entity.MyFriend;
import com.ludees.scian.entity.MyFriend_liuyanMessage;
import com.ludees.scian.entity.MyFriend_liuyanMessageBean;
import com.ludees.scian.entity.Page;
import com.ludees.scian.entity.ResponseCommon;
import com.ludees.scian.network.Urls;
import com.ludees.scian.util.SettingUtils;
import com.ludees.scian.util.ToastUtils;
import com.ludees.scian.view.ListViewForScrollView;
import com.ludees.scian.view.PullDownElasticImp;
import com.ludees.scian.view.PullDownScrollView;
import com.ludees.scian.view.RoundImageView;
import com.ludees.scian.view.PullDownScrollView.RefreshListener;
import com.google.gson.Gson;

public class MyFriendLiuyanActivity extends BaseActivity implements
		OnClickListener, RefreshListener {

	private ImageView myfriend_info_back;
	private RoundImageView myfriend_info_head;
	private TextView myfriend_info_title_name;

	private ListViewForScrollView zListView;
	private List<MyFriend_liuyanMessage> list;
	private MyFriendMessageDetailAdapter myFriendMessageDetailAdapter;

	private Gson gson;

	private static final int INIT_PAGE_NO = 1;// 初始化、下拉刷新页码
	private static final int PAGE_SIZE = 20;// 每次加载数据数量
	private int pageNo;// 已加载页数
	private int pageCount;// 已下载总数

	private Activity activity;
	private MyFriend myFriend;
	private EditText myfriend_info_liuyan_content;
	private TextView myfriend_info_liuyan_send;
	private TextView myfriend_info_name;
	private TextView myfriend_info_phoneNumber;
	private ImageView myfriend_info_call;
	private ImageView myfriend_info_progress_quan;
	private TextView myfriend_info_progress_healthNumber;
	private TextView myfriend_info_celiangshijian;
	private TextView myfriend_info_xueya;
	private TextView myfriend_info_pulse;
	private TextView myfriend_info_bloodPressureDiffer;
	private LinearLayout myfriend_info_history;
	private PullDownScrollView equipment_scroll2;
	private FinalHttp fh;
	private String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myfriend_friendinfo);
		activity = MyFriendLiuyanActivity.this;

		initView();
		setData();
		initListener();
	}

	/**
	 * TODO 初始化控件
	 */
	private void initView() {
		list = new ArrayList<MyFriend_liuyanMessage>();
		gson = new Gson();
		ExampleApplication.getInstance().addActivity(this);
		userId = SettingUtils.get(MyFriendLiuyanActivity.this, "userId", "");
		myfriend_info_back = (ImageView) findViewById(R.id.myfriend_info_back);
		myfriend_info_title_name = (TextView) findViewById(R.id.myfriend_info_title_name);
		equipment_scroll2 = (PullDownScrollView) findViewById(R.id.equipment_scroll2);

		myfriend_info_head = (RoundImageView) findViewById(R.id.myfriend_info_head);
		myfriend_info_head.setType(1);

		zListView = (ListViewForScrollView) findViewById(R.id.myfriend_info_lv);

		myfriend_info_liuyan_content = (EditText) findViewById(R.id.myfriend_info_liuyan_content);
		myfriend_info_liuyan_send = (TextView) findViewById(R.id.myfriend_info_liuyan_send);
		myfriend_info_name = (TextView) findViewById(R.id.myfriend_info_name);
		myfriend_info_phoneNumber = (TextView) findViewById(R.id.myfriend_info_phoneNumber);
		myfriend_info_call = (ImageView) findViewById(R.id.myfriend_info_call);
		myfriend_info_progress_quan = (ImageView) findViewById(R.id.myfriend_info_progress_quan);
		myfriend_info_progress_healthNumber = (TextView) findViewById(R.id.myfriend_info_progress_healthNumber);
		myfriend_info_celiangshijian = (TextView) findViewById(R.id.myfriend_info_celiangshijian);
		myfriend_info_xueya = (TextView) findViewById(R.id.myfriend_info_xueya);
		myfriend_info_pulse = (TextView) findViewById(R.id.myfriend_info_pulse);
		myfriend_info_bloodPressureDiffer = (TextView) findViewById(R.id.myfriend_info_bloodPressureDiffer);

		myfriend_info_history = (LinearLayout) findViewById(R.id.myfriend_info_history);

		fh = new FinalHttp();
		fh.configTimeout(15 * 1000);

		equipment_scroll2.setRefreshTips(this.getResources().getString(R.string.chazhaohaoyou), this.getResources().getString(R.string.chazhaowancheng), this.getResources().getString(R.string.zhengzaichazhaohaoyou));
		equipment_scroll2.setPullDownElastic(new PullDownElasticImp(this));
		equipment_scroll2.setRefreshListener(this);
		equipment_scroll2.setVisibility(View.VISIBLE);
	}

	/**
	 * TODO 初始化事件监听
	 */
	private void initListener() {
		myfriend_info_back.setOnClickListener(this);
		myfriend_info_call.setOnClickListener(this);
		myfriend_info_liuyan_send.setOnClickListener(this);
		myfriend_info_history.setOnClickListener(this);
		zListView.setClickable(false);
	}

	/**
	 * 设置初始化数据
	 */
	private void setData() {
		Bundle bundle = getIntent().getBundleExtra("bundle");
		myFriend = (MyFriend) bundle.get("myFriend");

		myfriend_info_title_name.setText(myFriend.getRealName());
		AbImageLoader imageLoader = AbImageLoader.newInstance(activity);
		if (!TextUtils.isEmpty(myFriend.getUserPic())) {
			imageLoader.display(myfriend_info_head, myFriend.getUserPic());
		} else {
			myfriend_info_head
					.setImageResource(R.drawable.normal_user__circle_head);
		}
		myfriend_info_name.setText(myFriend.getRealName());
		myfriend_info_phoneNumber.setText(myFriend.getPhone());
		// TODO 健康指示圈
		int healthNumber = myFriend.getHealthNumber();
		if (healthNumber < 35) {
			myfriend_info_progress_quan
					.setBackgroundResource(R.drawable.home_myfriend_quan_6);
			myfriend_info_progress_healthNumber.setTextColor(getResources()
					.getColor(R.color.blood_pressure_6));
		} else if (healthNumber >= 35 && healthNumber < 50) {
			myfriend_info_progress_quan
					.setBackgroundResource(R.drawable.home_myfriend_quan_5);
			myfriend_info_progress_healthNumber.setTextColor(getResources()
					.getColor(R.color.blood_pressure_5));
		} else if (healthNumber >= 50 && healthNumber < 65) {
			myfriend_info_progress_quan
					.setBackgroundResource(R.drawable.home_myfriend_quan_4);
			myfriend_info_progress_healthNumber.setTextColor(getResources()
					.getColor(R.color.blood_pressure_4));
		} else if (healthNumber >= 65 && healthNumber < 73) {
			myfriend_info_progress_quan
					.setBackgroundResource(R.drawable.home_myfriend_quan_3);
			myfriend_info_progress_healthNumber.setTextColor(getResources()
					.getColor(R.color.blood_pressure_3));
		} else if (healthNumber >= 73 && healthNumber < 80) {
			myfriend_info_progress_quan
					.setBackgroundResource(R.drawable.home_myfriend_quan_2);
			myfriend_info_progress_healthNumber.setTextColor(getResources()
					.getColor(R.color.blood_pressure_2));
		} else if (healthNumber >= 80) {
			myfriend_info_progress_quan
					.setBackgroundResource(R.drawable.home_myfriend_quan_1);
			myfriend_info_progress_healthNumber.setTextColor(getResources()
					.getColor(R.color.blood_pressure_1));
		}
		myfriend_info_progress_healthNumber.setText(healthNumber + "%");
		myfriend_info_celiangshijian.setText(myFriend.getMeasureTime());
		myfriend_info_xueya.setText(myFriend.getBloodPressureClose() + "/"
				+ myFriend.getBloodPressureOpen());
		myfriend_info_pulse.setText(myFriend.getPulse() + "");
		myfriend_info_bloodPressureDiffer.setText(myFriend
				.getBloodPressureDiffer() + "");

		getFriendsMessageList(Urls.FRIENDS_LIUYAN_DATA + "friendId="
				+ myFriend.getFriendId() + "&pageNo=" + INIT_PAGE_NO
				+ "&userId=" + userId + "&pageSize=" + PAGE_SIZE);

	}

	@Override
	public void onClick(View v) {
		// TODO 点击事件监听
		if (v == myfriend_info_back) {// 返回键
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			finish();
		} else if (v == myfriend_info_call) {// 打电话
			String phone = myFriend.getPhone();
			if (!TextUtils.isEmpty(phone)) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ phone));
				startActivity(intent);
			} else {
				ToastUtils.TextToast(this, this.getResources().getString(R.string.haoyouxinxidianhuahaomaweikong));
			}
		} else if (v == myfriend_info_liuyan_send) {// 发送留言
			String sendContent = myfriend_info_liuyan_content.getText()
					.toString().trim();
			if (!TextUtils.isEmpty(sendContent)) {
				// 发送留言
				sendLiuyan(sendContent);
			} else {
				ToastUtils.TextToast(activity, activity.getResources().getString(R.string.qingtianxieliuyanneirong));
			}
		} else if (v == myfriend_info_history) {// 好友历史数据
			SettingUtils.set(MyFriendLiuyanActivity.this, "isFirstFriendChart",
					"1");
			SettingUtils.set(MyFriendLiuyanActivity.this, "isFirstFriendData",
					"1");
			Bundle bundle = new Bundle();
			bundle.putSerializable("myFriend", myFriend);
			Intent intent = new Intent(this, MyFriendHistoryDataActivity.class);
			intent.putExtra("myFriendId", myFriend.getFriendId() + "");
			intent.putExtra("bundle", bundle);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			startActivity(intent);
			// finish();
		}

	}

	/**
	 * 获取留言列表
	 * 
	 * @param ss
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getFriendsMessageList(String url) {
		fh.get(url, new AjaxCallBack() {
			private ProgressDialog pd;

			@Override
			public void onStart() {
				super.onStart();
				pd = new ProgressDialog(MyFriendLiuyanActivity.this);
				pd.setCancelable(false);
				pd.setMessage(MyFriendLiuyanActivity.this.getResources().getString(R.string.zhengzaihuoquhaoyouliuyanliebiao));
				pd.show();
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				pd.dismiss();
				ToastUtils.TextToast(MyFriendLiuyanActivity.this, getResources().getString(R.string.fuwuqilianjieyichang));
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				pd.dismiss();
				String content = (String) t;
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					ToastUtils.TextToast(activity, common.getMsg());
					return;
				}
				MyFriend_liuyanMessageBean infoBean = gson.fromJson(content,
						MyFriend_liuyanMessageBean.class);
				List<MyFriend_liuyanMessage> data = infoBean.getData();
				Page page = infoBean.getPage();
				pageNo = page.getPageNo();
				pageCount = page.getPageCount();
				if (pageCount <= pageNo * PAGE_SIZE) {

				}
				if (data != null && data.size() > 0) {
					list.addAll(data);
					if (myFriendMessageDetailAdapter == null) {
						myFriendMessageDetailAdapter = new MyFriendMessageDetailAdapter(
								activity, list);
						zListView.setAdapter(myFriendMessageDetailAdapter);
					} else {
						myFriendMessageDetailAdapter.notifyDataSetChanged();
					}
					zListView.setSelection(1);
				}
			}
		});
	}

	/**
	 * 下拉刷新
	 * 
	 * @param string
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void getRefreshList(String url) {
		fh.get(url, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				ToastUtils.TextToast(MyFriendLiuyanActivity.this, getResources().getString(R.string.fuwuqilianjieyichang));
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String content = (String) t;
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					ToastUtils.TextToast(activity, common.getMsg());
					return;
				}
				MyFriend_liuyanMessageBean infoBean = gson.fromJson(content,
						MyFriend_liuyanMessageBean.class);
				List<MyFriend_liuyanMessage> data = infoBean.getData();
				Page page = infoBean.getPage();
				pageNo = page.getPageNo();
				pageCount = page.getPageCount();

				if (data != null) {
					// 1.先清理空数据
					list.clear();
					// 2.将新刷新的数据设置进去
					list.addAll(data);
					myFriendMessageDetailAdapter.notifyDataSetChanged();
					equipment_scroll2.finishRefresh(activity.getResources().getString(R.string.chazhaowancheng));
				}
			}
		});
	}

	/**
	 * 上拉加载
	 * 
	 * @param string
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	private void getLoadList(String url) {
		fh.get(url, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				ToastUtils.TextToast(MyFriendLiuyanActivity.this, getResources().getString(R.string.fuwuqilianjieyichang));
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String content = (String) t;
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					ToastUtils.TextToast(activity, common.getMsg());
					return;
				}
				MyFriend_liuyanMessageBean infoBean = gson.fromJson(content,
						MyFriend_liuyanMessageBean.class);
				List<MyFriend_liuyanMessage> data = infoBean.getData();
				Page page = infoBean.getPage();
				pageNo = page.getPageNo();
				pageCount = page.getPageCount();
				if (pageCount <= pageNo * PAGE_SIZE) {
				}
				if (data != null) {
					list.addAll(data);
					myFriendMessageDetailAdapter.notifyDataSetChanged();
				}
			}
		});
	}

	/**
	 * 发送留言
	 * 
	 * @param sendContent
	 */
	private void sendLiuyan(String sendContent) {
		// TODO Auto-generated method stub
		AjaxParams params = new AjaxParams();
		params.put("userId", SettingUtils.get(this, "userId", ""));
		params.put("messageContent", sendContent);
		params.put("myFriendsId", myFriend.getFriendId() + "");
		sendFriendMessage(Urls.FRIENDS_MESSAGE_EDIT, params);
	}

	/**
	 * 发送好友留言
	 * 
	 * @param url
	 * @param params
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void sendFriendMessage(String url, AjaxParams params) {
		fh.post(url, params, new AjaxCallBack() {
			private ProgressDialog pd;

			@Override
			public void onStart() {
				super.onStart();
				pd = new ProgressDialog(activity);
				pd.setCancelable(false);
				pd.setMessage(activity.getResources().getString(R.string.zhengzaifasongliuyanqingshaodeng));
				pd.show();
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				pd.dismiss();
				prompt(activity.getResources().getString(R.string.fasongliuyanxiaoxishibai));
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				pd.dismiss();
				String content = (String) t;
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				prompt(common.getMsg());
				if (common.getStatus() == 1) {
					myfriend_info_liuyan_content.setText("");
					if (list.size() > 0) {
						list.clear();
					}
					myFriendMessageDetailAdapter.notifyDataSetChanged();
					zListView.setVisibility(View.VISIBLE);
					getFriendsMessageList(Urls.FRIENDS_LIUYAN_DATA
							+ "friendId=" + myFriend.getFriendId() + "&userId="
							+ userId + "&pageNo=" + INIT_PAGE_NO + "&pageSize="
							+ PAGE_SIZE);
				}

			}
		});
	}

	@Override
	public void onRefresh(PullDownScrollView view) {
		// TODO
		getRefreshList(Urls.FRIENDS_LIUYAN_DATA + "friendId="
				+ myFriend.getFriendId() + "&userId=" + userId + "&pageNo="
				+ INIT_PAGE_NO + "&pageSize=" + PAGE_SIZE + "&type=1");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		myfriend_info_back = null;
		myfriend_info_head = null;
		myfriend_info_title_name = null;
		zListView = null;
		list = null;
		myFriendMessageDetailAdapter = null;
		gson = null;
		activity = null;
		myFriend = null;
		myfriend_info_liuyan_content = null;
		myfriend_info_liuyan_send = null;
		myfriend_info_name = null;
		myfriend_info_phoneNumber = null;
		myfriend_info_call = null;
		myfriend_info_progress_quan = null;
		myfriend_info_progress_healthNumber = null;
		myfriend_info_celiangshijian = null;
		myfriend_info_xueya = null;
		myfriend_info_pulse = null;
		myfriend_info_bloodPressureDiffer = null;
		myfriend_info_history = null;
		equipment_scroll2 = null;
		fh = null;
		userId = null;
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}
}
