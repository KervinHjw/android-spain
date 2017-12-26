package com.en.scian.expertadvice;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.en.scian.R;
import com.en.scian.entity.ArticleInfo;
import com.en.scian.entity.ArticleInfoBean;
import com.en.scian.entity.Page;
import com.en.scian.entity.ResponseCommon;
import com.en.scian.network.Urls;
import com.en.scian.util.ToastUtils;
import com.google.gson.Gson;
import com.socks.zlistview.widget.ZListView;
import com.socks.zlistview.widget.ZListView.IXListViewListener;

public class XuetangFragment extends Fragment implements OnItemClickListener {

	private ZListView zListView;
	private ExpertAdviceAdaper adviceAdapter;
	private List<ArticleInfo> list;

	private static final int INIT_PAGE_NO = 1;// 初始化、下拉刷新页码
	private static final int PAGE_SIZE = 5;// 每次加载数据数量
	private int pageNo;// 已加载页数
	private int pageCount;// 已下载总数
	private View view;
	private FinalHttp fh;
	private boolean isFirstIn = true;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.expert_advice_layout, null);
		initViews(view);
		return view;
	}

	/**
	 * 初始化控件
	 * 
	 * @param view
	 */
	private void initViews(View view) {
		// TODO Auto-generated method stub
		zListView = (ZListView) view.findViewById(R.id.expert_advice_zlistview);
		zListView.setPullLoadEnable(true);
		zListView.setPullRefreshEnable(true);
		list = new ArrayList<ArticleInfo>();
		adviceAdapter = new ExpertAdviceAdaper(getActivity(), list);
		zListView.setAdapter(adviceAdapter);

		fh = new FinalHttp();
		fh.configTimeout(15 * 1000);

		initListener();
	}

//	@Override
//	public void onResume() {
//		super.onResume();
//	setData();
//	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isFirstIn & isVisibleToUser) {
			setData();
		}
		super.setUserVisibleHint(isVisibleToUser);
	}

	/**
	 * TODO 监听事件
	 */
	private void initListener() {
		zListView.setOnItemClickListener(this);
		zListView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {// 下拉刷新
				getRefreshList(Urls.EXPERT_ADVICE_LIST + "pageNo="
						+ INIT_PAGE_NO + "&pageSize=" + PAGE_SIZE + "&type=3");

			}

			@Override
			public void onLoadMore() {// 上拉加载
				if (pageCount > pageNo) {
					getLoadList(Urls.EXPERT_ADVICE_LIST + "pageNo="
							+ (pageNo + 1) + "&pageSize=" + PAGE_SIZE
							+ "&type=3");
				} else {
					zListView.stopLoadMore();
					ToastUtils.TextToast(getActivity(), getResources().getString(R.string.meiyougengduoshujule));
				}
			}
		});
	}

	/**
	 * 设置初始化数据
	 */
	private void setData() {
		getLunBoTuURL(Urls.EXPERT_ADVICE_LIST + "pageNo=" + INIT_PAGE_NO
				+ "&pageSize=" + PAGE_SIZE + "&type=3");
	}

	/**
	 * TODO 获取专家建议列表
	 * 
	 * @param ss
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void getLunBoTuURL(String url) {
		fh.get(url, new AjaxCallBack() {
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				zListView.setPullLoadEnable(false);
				zListView.setVisibility(View.INVISIBLE);
				ToastUtils.TextToast(getActivity(), getResources().getString(R.string.fuwuqilianjieyichang));
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String content = (String) t;
				ResponseCommon common = new Gson().fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					ToastUtils.TextToast(getActivity(), common.getMsg());
					zListView.setPullLoadEnable(false);
					zListView.setVisibility(View.INVISIBLE);
					return;
				}
				ArticleInfoBean infoBean = new Gson().fromJson(content,
						ArticleInfoBean.class);
				List<ArticleInfo> data = infoBean.getData();
				updateUI(infoBean, data, true);
			}

		});
	}

	/**
	 * TODO 下拉刷新
	 * 
	 * @param string
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getRefreshList(String url) {
		fh.get(url, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				zListView.stopRefresh();
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				zListView.stopRefresh();
				String content = (String) t;
				ResponseCommon common = new Gson().fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					ToastUtils.TextToast(getActivity(), common.getMsg());
					return;
				}
				ArticleInfoBean infoBean = new Gson().fromJson(content,
						ArticleInfoBean.class);
				List<ArticleInfo> data = infoBean.getData();
				updateUI(infoBean, data, true);
			}

		});
	}

	/**
	 * TODO 上拉加载
	 * 
	 * @param string
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getLoadList(String url) {
		fh.get(url, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				zListView.stopLoadMore();
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				zListView.stopLoadMore();
				String content = (String) t;
				ResponseCommon common = new Gson().fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					ToastUtils.TextToast(getActivity(), common.getMsg());
					return;
				}
				ArticleInfoBean infoBean = new Gson().fromJson(content,
						ArticleInfoBean.class);
				List<ArticleInfo> data = infoBean.getData();
				updateUI(infoBean, data, false);
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
	private void updateUI(ArticleInfoBean bean, List<ArticleInfo> data,
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
			adviceAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ArticleInfo info = (ArticleInfo) adviceAdapter.getItem(position - 1);
		Intent intent = new Intent(getActivity(),
				BloodSugarSuggestionActivity.class);
		intent.putExtra("articleId", info.getArticleId());
		startActivity(intent);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		zListView = null;
		adviceAdapter = null;
		System.gc();
	}
}
