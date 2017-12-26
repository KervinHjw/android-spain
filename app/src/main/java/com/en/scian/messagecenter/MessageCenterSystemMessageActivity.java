package com.en.scian.messagecenter;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.en.scian.BaseActivity;
import com.en.scian.ExampleApplication;
import com.en.scian.R;
import com.en.scian.entity.Page;
import com.en.scian.entity.ResponseCommon;
import com.en.scian.entity.SystemMessage;
import com.en.scian.entity.SystemMessageBean;
import com.en.scian.network.Urls;
import com.en.scian.util.SettingUtils;
import com.en.scian.util.ToastUtils;
import com.google.gson.Gson;
import com.socks.zlistview.widget.ZListView;
import com.socks.zlistview.widget.ZListView.IXListViewListener;

/**
 * 系统消息
 * 
 * @author jiyx
 * 
 */
public class MessageCenterSystemMessageActivity extends BaseActivity implements
		OnClickListener {
	private TextView title;
	private LinearLayout back;

	private ZListView listView;
	private MessageCenterSystemMessageAdapter messageCenterSystemMessageAdapter;

	private List<SystemMessage> list;
	private Gson gson;

	private static final int INIT_PAGE_NO = 1;// 初始化、下拉刷新页码
	private static final int PAGE_SIZE = 20;// 每次加载数据数量
	private int pageNo;// 已加载页数
	private int pageCount;// 已下载总数

	private Activity activity;
	private FinalHttp fh;

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
		list = new ArrayList<SystemMessage>();
		gson = new Gson();
		ExampleApplication.getInstance().addActivity(this);
		title = (TextView) findViewById(R.id.search_titleText);
		back = (LinearLayout) findViewById(R.id.search_leftLayout);

		listView = (ZListView) findViewById(R.id.listview);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		messageCenterSystemMessageAdapter = new MessageCenterSystemMessageAdapter(
				MessageCenterSystemMessageActivity.this, list);
		listView.setAdapter(messageCenterSystemMessageAdapter);

		// 获取Http工具类
		fh = new FinalHttp();
		fh.configTimeout(15 * 1000);
	}

	/**
	 * 设置初始化数据
	 */
	private void setData() {
		title.setText(getResources().getString(R.string.msg_list));

		getSystemMessageList(Urls.GET_SYSTEM_MESSAGE_LIST + "pageNo="
				+ INIT_PAGE_NO + "&pageSize=" + PAGE_SIZE + "&userId="
				+ SettingUtils.get(activity, "userId", ""));
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		back.setOnClickListener(this);
		listView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {// 下拉刷新
				getRefreshList(Urls.GET_SYSTEM_MESSAGE_LIST + "userId="
						+ SettingUtils.get(activity, "userId", "") + "&pageNo="
						+ INIT_PAGE_NO + "&pageSize=" + PAGE_SIZE);

			}

			@Override
			public void onLoadMore() {// 上拉加载
				if (pageCount > pageNo) {
					getLoadList(Urls.GET_SYSTEM_MESSAGE_LIST + "userId="
							+ SettingUtils.get(activity, "userId", "")
							+ "&pageNo=" + (pageNo + 1) + "&pageSize="
							+ PAGE_SIZE);
				} else {
					listView.stopLoadMore();
					prompt(getResources().getString(R.string.yijingshizuihouyiyele));
				}
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SystemMessage systemMessage = (SystemMessage) listView
						.getItemAtPosition(position);
				Intent intent = new Intent(
						MessageCenterSystemMessageActivity.this,
						MessageDetailActivity.class);
				intent.putExtra("messageId", systemMessage.getMessageId());
				startActivity(intent);
			}

		});

	}

	// SystemMessageBean
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
	 * 获取系统列表
	 * 
	 * @param string
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void getSystemMessageList(String url) {
		fh.get(url, new AjaxCallBack() {
			private ProgressDialog pd;

			@Override
			public void onStart() {
				super.onStart();
				pd = new ProgressDialog(MessageCenterSystemMessageActivity.this);
				pd.setCancelable(false);
				pd.setMessage(MessageCenterSystemMessageActivity.this.getResources().getString(R.string.zhengzaihuoquxitongxiaoxiliebiao));
				pd.show();
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				pd.dismiss();
				ToastUtils.TextToast(MessageCenterSystemMessageActivity.this,
						getResources().getString(R.string.fuwuqilianjieyichang));
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				pd.dismiss();
				String content = (String) t;
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					prompt(common.getMsg());
					listView.setPullLoadEnable(false);
					listView.setAdapter(new MessageCenterSystemMessageAdapter(
							MessageCenterSystemMessageActivity.this, list));
					return;
				}
				SystemMessageBean bean = gson.fromJson(content,
						SystemMessageBean.class);
				List<SystemMessage> data = bean.getData();
				updateUI(bean, data, true);
//				Page page = bean.getPage();
//				pageNo = page.getPageNo();
//				pageCount = page.getPageCount();
//				if (pageCount <= pageNo * PAGE_SIZE) {
//					listView.setPullLoadEnable(false);
//				}
//				if (data != null) {
//					list.addAll(data);
//					messageCenterSystemMessageAdapter = new MessageCenterSystemMessageAdapter(
//							MessageCenterSystemMessageActivity.this, list);
//					listView.setAdapter(messageCenterSystemMessageAdapter);
//				}
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
				listView.stopRefresh();
				ToastUtils.TextToast(MessageCenterSystemMessageActivity.this,
						getResources().getString(R.string.fuwuqilianjieyichang));
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String content = (String) t;
				listView.stopRefresh();
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					prompt(common.getMsg());
					return;
				}
				SystemMessageBean bean = gson.fromJson(content,
						SystemMessageBean.class);
				List<SystemMessage> data = bean.getData();
				updateUI(bean, data, true);
//				Page page = bean.getPage();
//				pageNo = page.getPageNo();
//				pageCount = page.getPageCount();
//				if (pageCount <= pageNo * PAGE_SIZE) {
//					listView.setPullLoadEnable(false);
//				}
//				if (data != null) {
//					// 1.先清理空数据
//					list.clear();
//					// 2.将新刷新的数据设置进去
//					list.addAll(data);
//					messageCenterSystemMessageAdapter.notifyDataSetChanged();
//				}
			}
		});
	}

	/**
	 * 上拉加载
	 * 
	 * @param string
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void getLoadList(String url) {
		fh.get(url, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				listView.stopLoadMore();
				ToastUtils.TextToast(MessageCenterSystemMessageActivity.this,
						getResources().getString(R.string.fuwuqilianjieyichang));
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String content = (String) t;
				listView.stopLoadMore();
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					prompt(common.getMsg());
					return;
				}
				SystemMessageBean bean = gson.fromJson(content,
						SystemMessageBean.class);
				List<SystemMessage> data = bean.getData();
				updateUI(bean, data, false);
//				Page page = bean.getPage();
//				pageNo = page.getPageNo();
//				pageCount = page.getPageCount();
//				if (pageCount <= pageNo * PAGE_SIZE) {
//					listView.setPullLoadEnable(false);
//				}
//				if (data != null) {
//					list.addAll(data);
//					messageCenterSystemMessageAdapter.notifyDataSetChanged();
//				}
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
	private void updateUI(SystemMessageBean bean, List<SystemMessage> data,
			boolean isRefresh) {
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
			messageCenterSystemMessageAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		title = null;
		back = null;
		listView = null;
		messageCenterSystemMessageAdapter = null;
		list = null;
		gson = null;
		activity = null;
		fh = null;

		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}
}
