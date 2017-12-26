package com.en.scian.messagecenter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
import com.en.scian.BaseActivity;
import com.en.scian.ExampleApplication;
import com.en.scian.R;
import com.en.scian.entity.MyFriend_Message;
import com.en.scian.entity.MyFriend_MessageBean;
import com.en.scian.entity.Page;
import com.en.scian.entity.ResponseCommon;
import com.en.scian.network.Urls;
import com.en.scian.util.SettingUtils;
import com.en.scian.util.ToastUtils;
import com.google.gson.Gson;
import com.socks.zlistview.widget.ZListView;
import com.socks.zlistview.widget.ZListView.IXListViewListener;

/**
 * 留言消息
 * 
 * @author jiyx
 * 
 */
public class MessageCenterMessageActivity extends BaseActivity implements
		OnClickListener {
	private TextView title;
	private LinearLayout back;

	private ZListView listView;
	private MessageCenterMessageAdapter messageCenterMessageAdapter;

	private List<MyFriend_Message> list;
	private AbHttpUtil mAbHttpUtil;
	private Gson gson;

	private static final int INIT_PAGE_NO = 1;// 初始化、下拉刷新页码
	private static final int PAGE_SIZE = 20;// 每次加载数据数量
	private int pageNo;// 已加载页数
	private int pageCount;// 已下载总数

	private Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messagecenter_message_list);
		initViews();
		initListener();
		setData();
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		activity = this;
		list = new ArrayList<MyFriend_Message>();
		gson = new Gson();
		ExampleApplication.getInstance().addActivity(this);
		title = (TextView) findViewById(R.id.search_titleText);
		back = (LinearLayout) findViewById(R.id.search_leftLayout);

		listView = (ZListView) findViewById(R.id.listview);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		messageCenterMessageAdapter = new MessageCenterMessageAdapter(
				MessageCenterMessageActivity.this, list);
		listView.setAdapter(messageCenterMessageAdapter);

		// 获取Http工具类
		mAbHttpUtil = AbHttpUtil.getInstance(this);
		mAbHttpUtil.setTimeout(10000);
	}

	/**
	 * 设置初始化数据
	 */
	private void setData() {
		title.setText(this.getResources().getString(R.string.msg_list));
		getMessageList(Urls.FRIENDS_MESSAGE_LIST + "userId="
				+ SettingUtils.get(this, "userId", "") + "&pageNo="
				+ INIT_PAGE_NO + "&pageSize=" + PAGE_SIZE);
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		back.setOnClickListener(this);
		listView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {// 下拉刷新
				getRefreshList(Urls.FRIENDS_MESSAGE_LIST + "userId="
						+ SettingUtils.get(activity, "userId", "") + "&pageNo="
						+ INIT_PAGE_NO + "&pageSize=" + PAGE_SIZE);

			}

			@Override
			public void onLoadMore() {// 上拉加载
				if (pageCount > pageNo) {
					getLoadList(Urls.FRIENDS_MESSAGE_LIST + "userId="
							+ SettingUtils.get(activity, "userId", "")
							+ "&pageNo=" + (pageNo + 1) + "&pageSize="
							+ PAGE_SIZE);
				} else {
					listView.stopLoadMore();
					prompt(getResources().getString(
							R.string.yijingshizuihouyiyele));
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_leftLayout:// 返回
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			finish();
			break;
		}
	}

	/**
	 * 获取好友留言列表
	 * 
	 * @param string
	 */
	private void getMessageList(String url) {
		mAbHttpUtil.get(url, new AbStringHttpResponseListener() {
			private ProgressDialog pd;

			// 开始执行前
			@Override
			public void onStart() {
				pd = new ProgressDialog(MessageCenterMessageActivity.this);
				pd.setCancelable(false);
				pd.setMessage(MessageCenterMessageActivity.this.getResources().getString(R.string.zhengzaihuoquhaoyouliuyanliebiao));
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
				ToastUtils.TextToast(getApplicationContext(), MessageCenterMessageActivity.this.getResources().getString(R.string.wangluolianjieshibai));
			}

			// 获取数据成功会调用这里
			@Override
			public void onSuccess(int status, String content) {
				// TODO Auto-generated method stub
				pd.dismiss();
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					prompt(common.getMsg());
					listView.setPullLoadEnable(false);
					listView.setAdapter(new MessageCenterMessageAdapter(
							MessageCenterMessageActivity.this, list));
					return;
				}
				MyFriend_MessageBean bean = gson.fromJson(content,
						MyFriend_MessageBean.class);
				List<MyFriend_Message> data = bean.getData();
				updateUI(bean, data, true);
				// Page page = bean.getPage();
				// pageNo = page.getPageNo();
				// pageCount = page.getPageCount();
				// if (pageCount <= pageNo * PAGE_SIZE) {
				// listView.setPullLoadEnable(false);
				// }
				// if (data != null) {
				// list.addAll(data);
				// messageCenterMessageAdapter = new
				// MessageCenterMessageAdapter(
				// MessageCenterMessageActivity.this, list);
				// listView.setAdapter(messageCenterMessageAdapter);
				// }
			}
		});
	}

	/**
	 * 下拉刷新
	 * 
	 * @param string
	 */
	private void getRefreshList(String url) {
		mAbHttpUtil.get(url, new AbStringHttpResponseListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onFinish() {
				listView.stopRefresh();
			}

			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				listView.stopRefresh();
				ToastUtils.TextToast(getApplicationContext(), MessageCenterMessageActivity.this.getResources().getString(R.string.wangluolianjieshibai));
			}

			// 获取数据成功会调用这里
			@Override
			public void onSuccess(int status, String content) {
				// TODO Auto-generated method stub
				listView.stopRefresh();
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					prompt(common.getMsg());
					return;
				}
				MyFriend_MessageBean bean = gson.fromJson(content,
						MyFriend_MessageBean.class);
				List<MyFriend_Message> data = bean.getData();
				updateUI(bean, data, true);
				// Page page = bean.getPage();
				// pageNo = page.getPageNo();
				// pageCount = page.getPageCount();
				// if (pageCount <= pageNo * PAGE_SIZE) {
				// listView.setPullLoadEnable(false);
				// }
				// if (data != null) {
				// // 1.先清理空数据
				// list.clear();
				// // 2.将新刷新的数据设置进去
				// list.addAll(data);
				// messageCenterMessageAdapter.notifyDataSetChanged();
				// }
			}
		});
	}

	/**
	 * 上拉加载
	 * 
	 * @param string
	 */
	private void getLoadList(String url) {
		mAbHttpUtil.get(url, new AbStringHttpResponseListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onFinish() {
				listView.stopLoadMore();
			}

			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				listView.stopLoadMore();
				ToastUtils.TextToast(getApplicationContext(), MessageCenterMessageActivity.this.getResources().getString(R.string.wangluolianjieshibai));
			}

			// 获取数据成功会调用这里
			@Override
			public void onSuccess(int status, String content) {
				// TODO Auto-generated method stub
				listView.stopLoadMore();
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					prompt(common.getMsg());
					return;
				}
				MyFriend_MessageBean bean = gson.fromJson(content,
						MyFriend_MessageBean.class);
				List<MyFriend_Message> data = bean.getData();
				updateUI(bean, data, false);
				// Page page = bean.getPage();
				// pageNo = page.getPageNo();
				// pageCount = page.getPageCount();
				// if (pageCount <= pageNo * PAGE_SIZE) {
				// listView.setPullLoadEnable(false);
				// }
				// if (data != null) {
				// list.addAll(data);
				// messageCenterMessageAdapter.notifyDataSetChanged();
				// }
			}
		});
	}

	/**
	 * 更新UI界面
	 * 
	 * @param bean
	 * @param data
	 * @param isRefresh
	 *            是否下拉刷新
	 */
	private void updateUI(MyFriend_MessageBean bean,
			List<MyFriend_Message> data, boolean isRefresh) {
		Page page = bean.getPage();
		pageNo = page.getPageNo();
		pageCount = page.getPageCount();
		if (pageCount > pageNo) {
			listView.setPullLoadEnable(true);
		} else {
			listView.setPullLoadEnable(false);
		}
		if (data != null) {
			if (isRefresh) {
				list.clear();
			}
			list.addAll(data);
			data.clear();
			messageCenterMessageAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		title = null;
		back = null;
		listView = null;
		messageCenterMessageAdapter = null;
		list = null;
		mAbHttpUtil = null;
		gson = null;
		activity = null;
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}
}
