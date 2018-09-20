package com.ludees.scian.main.fragment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.ludees.scian.R;
import com.ludees.scian.entity.MyFriend;
import com.ludees.scian.entity.MyFriendBean;
import com.ludees.scian.entity.Page;
import com.ludees.scian.entity.ResponseCommon;
import com.ludees.scian.myfriend.AddFriendActivity;
import com.ludees.scian.myfriend.MyFriendAdapter;
import com.ludees.scian.myfriend.MyFriendLiuyanActivity;
import com.ludees.scian.network.Urls;
import com.ludees.scian.util.SettingUtils;
import com.ludees.scian.util.ToastUtils;
import com.google.gson.Gson;
import com.socks.zlistview.widget.ZListView;

/**
 * 我的好友
 * 
 * @author jiyx
 * 
 */
@SuppressLint("InflateParams")
public class MyFriendFragment extends Fragment implements OnClickListener {

	private ImageView myfriend_add_friend_iv;// 右上角添加好友
	private LinearLayout addLayout;// 数据为空时的添加好友布局
	private Button myfriend_add_friend_btn;

	private ZListView zListView;
	private List<MyFriend> list;
	private MyFriendAdapter myFriendAdapter;

	private static final int INIT_PAGE_NO = 1;// 初始化、下拉刷新页码
	private static final int PAGE_SIZE = 10;// 每次加载数据数量
	private int pageNo;// 已加载页数
	private int pageCount;// 已下载总数

	private Activity activity;
	private Gson gson;
	private boolean isFirstIn = true;
	private ProgressDialog pd;
	private FinalHttp fh;
	private ImageView friend_bg;
	private Bitmap btp;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_content_fragment_myfriend,
				null);
		initViews(view);
		return view;
	}

	private void initViews(View view) {
		activity = getActivity();
		myfriend_add_friend_iv = (ImageView) view
				.findViewById(R.id.myfriend_add_friend_iv);

		addLayout = (LinearLayout) view.findViewById(R.id.myfriend_add_Layout);
		myfriend_add_friend_btn = (Button) view
				.findViewById(R.id.myfriend_add_friend_btn);
		friend_bg = (ImageView) view.findViewById(R.id.friend_bg);
		InputStream is = this.getResources().openRawResource(
				R.drawable.xueya_history_data_bg);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = 2;
		btp = BitmapFactory.decodeStream(is, null, options);
		friend_bg.setImageBitmap(btp);
		friend_bg.setScaleType(ScaleType.FIT_XY);
		zListView = (ZListView) view.findViewById(R.id.myfriend_zlistview);
		zListView.setPullRefreshEnable(true);
		zListView.setPullLoadEnable(true);
		list = new ArrayList<MyFriend>();
		myFriendAdapter = new MyFriendAdapter(getActivity(), list);
		zListView.setAdapter(myFriendAdapter);

		// 获取Http工具类
		fh = new FinalHttp();
		fh.configTimeout(15 * 1000);
		gson = new Gson();

		initListener();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {

			if (isFirstIn) {
				isFirstIn = false;
				String userID2 = SettingUtils.get(getActivity(), "userId", "");
				if (list.size() > 0)
					list.clear();
				// 获取好友列表数据
				getFriendsList(Urls.GET_FRIENDS_LIST + "userId=" + userID2
						+ "&pageNo=" + INIT_PAGE_NO + "&pageSize=" + PAGE_SIZE);
			}
		} else {
			if (zListView != null) {
				zListView.setClickable(false);
			}
		}

	}

	/**
	 * TODO点击监听事件
	 */
	private void initListener() {
		myfriend_add_friend_iv.setOnClickListener(this);
		myfriend_add_friend_btn.setOnClickListener(this);

		zListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				Bundle bundle = new Bundle();
				MyFriend myFriend = list.get(position - 1);
				bundle.putSerializable("myFriend", myFriend);
				startActivity(new Intent(getActivity(),
						MyFriendLiuyanActivity.class)
						.putExtra("bundle", bundle));
			}
		});
		// 设置回调函数
		zListView.setXListViewListener(new ZListView.IXListViewListener() {

			@Override
			public void onRefresh() {// 下拉刷新
				getRefreshList(Urls.GET_FRIENDS_LIST + "userId="
						+ SettingUtils.get(activity, "userId", "") + "&pageNo="
						+ INIT_PAGE_NO + "&pageSize=" + PAGE_SIZE);

			}

			@Override
			public void onLoadMore() {// 上拉加载
				if (pageCount > pageNo) {
					getLoadList(Urls.GET_FRIENDS_LIST + "userId="
							+ SettingUtils.get(activity, "userId", "")
							+ "&pageNo=" + (pageNo + 1) + "&pageSize="
							+ PAGE_SIZE);
				} else {
					zListView.stopLoadMore();
					ToastUtils.TextToast(activity, getResources().getString(R.string.meiyougengduoshujule));
				}

			}
		});
	}

	@Override
	public void onClick(View view) {
		// TODO 点击事件
		if (view == myfriend_add_friend_iv // 右上角添加好友
				|| view == myfriend_add_friend_btn) {// 好友数据为空时的添加功能
			getActivity().startActivity(
					new Intent(getActivity(), AddFriendActivity.class));
		}
	}

	/**
	 * TODO 获取好友列表
	 * 
	 * @param string
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void getFriendsList(String url) {
		fh.get(url, new AjaxCallBack() {

			@Override
			public void onStart() {
				super.onStart();
				pd = new ProgressDialog(getActivity());
				pd.setMessage(getResources().getString(R.string.zhengzaijiazaiqingshaohou));
				pd.show();
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				pd.dismiss();
				addLayout.setVisibility(View.VISIBLE);
				ToastUtils.TextToast(getActivity(), getResources().getString(R.string.fuwuqilianjieyichang));
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
					addLayout.setVisibility(View.VISIBLE);
					zListView.setPullLoadEnable(false);
					return;
				}
				MyFriendBean bean = gson.fromJson(content, MyFriendBean.class);
				List<MyFriend> data = bean.getData();
				updateUI(bean, data, true);
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
				zListView.stopRefresh();
				ToastUtils.TextToast(getActivity(), getResources().getString(R.string.fuwuqilianjieyichang));
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String content = (String) t;
				zListView.stopRefresh();
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					ToastUtils.TextToast(activity, common.getMsg());
					return;
				}
				MyFriendBean bean = gson.fromJson(content, MyFriendBean.class);
				List<MyFriend> data = bean.getData();
				updateUI(bean, data, true);
			}
		});

	}

	/**
	 * TODO 上拉加载
	 * 
	 * @param string
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void getLoadList(String url) {
		fh.get(url, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				zListView.stopLoadMore();
				ToastUtils.TextToast(getActivity(), getResources().getString(R.string.fuwuqilianjieyichang));
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String content = (String) t;
				zListView.stopLoadMore();
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					// ToastUtils.TextToast(activity, common.getMsg());
					return;
				}
				MyFriendBean bean = gson.fromJson(content, MyFriendBean.class);
				List<MyFriend> data = bean.getData();
				updateUI(bean, data, false);
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
	private void updateUI(MyFriendBean bean, List<MyFriend> data,
			boolean isRefresh) {
		Page page = bean.getPage();
		pageNo = page.getPageNo();
		pageCount = page.getPageCount();
		if (pageCount > pageNo) {
			zListView.setPullLoadEnable(true);
		} else {
			zListView.setPullLoadEnable(false);
		}
		if (data != null) {
			if (isRefresh) {
				list.clear();
			}
			list.addAll(data);
			data.clear();
			myFriendAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onDestroyView() {
		// TODO 清理对象
		myfriend_add_friend_iv = null;
		addLayout = null;
		myfriend_add_friend_btn = null;
		zListView = null;
		if (list != null) {
			list.clear();
			list = null;
		}
		myFriendAdapter = null;
		activity = null;
		gson = null;
		pd = null;
		fh = null;
		System.gc();
		super.onDestroyView();
	}

}
