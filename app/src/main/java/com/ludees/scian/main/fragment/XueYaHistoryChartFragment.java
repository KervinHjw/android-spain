package com.ludees.scian.main.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.ludees.scian.R;
import com.ludees.scian.myfriend.WebViewActivity;
import com.ludees.scian.network.Urls;
import com.ludees.scian.util.SettingUtils;

/**
 * 血压历史数据趋势图
 * 
 * @author zhaok
 * 
 */
public class XueYaHistoryChartFragment extends Fragment {
	private Context mContext;
	/**
	 * k线图
	 */
	private int num = 0;
	/**
	 * 网络获取
	 */
	private String URL = "";
	private String URL2 = "";
	private String userId;

	private WebView main_webView;
	private WebView main_webView2;
	private RelativeLayout main_layout_friend_1;
	private RelativeLayout main_layout_friend_2;
	private String isFirstChart = "";

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.xueya_history_chart, null);
		initView(view);
		userId = SettingUtils.get(mContext, "userId", "");
		return view;
	}

	private void initView(View v) {
		mContext = getActivity();
		userId = SettingUtils.get(mContext, "userId", "");
		main_webView = (WebView) v.findViewById(R.id.main_webView);
		main_webView2 = (WebView) v.findViewById(R.id.main_webView2);
		main_layout_friend_1 = (RelativeLayout) v
				.findViewById(R.id.main_layout_friend_1);
		main_layout_friend_2 = (RelativeLayout) v
				.findViewById(R.id.main_layout_friend_2);
	}

	@Override
	public void onResume() {
		super.onResume();
			getData();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (main_webView != null) {
			main_webView.destroy();
			main_webView.destroyDrawingCache();
			main_webView = null;
		}
		if (main_webView2 != null) {
			main_webView2.destroy();
			main_webView2.destroyDrawingCache();
			main_webView2 = null;
		}
		mContext = null;
		URL = null;
		URL2 = null;
		userId = null;
		main_layout_friend_1 = null;
		main_layout_friend_2 = null;
		isFirstChart = null;
	}

	/**
	 * 获取数据
	 */
	public void getData() {
		// num = getActivity().getIntent().getIntExtra("type", 0);
		num = Integer.valueOf(SettingUtils.get(getActivity(), "myData", "0"));
		SettingUtils.set(getActivity(), "isFirstTrendChart", "2");
		switch (num) {
		case 0:
			URL = Urls.GET_HISTORY_CHART_DATA + "userId=" + userId + "&type=1";
			URL2 = Urls.GET_HISTORY_CHART_DATA_DOWN + "userId=" + userId
					+ "&type=1";
			getWeb1(URL);
			getWeb2(URL2);
			break;
		case 1:
			URL = Urls.GET_HISTORY_CHART_DATA + "userId=" + userId + "&type=1";
			URL2 = Urls.GET_HISTORY_CHART_DATA_DOWN + "userId=" + userId
					+ "&type=1";
			getWeb1(URL);
			getWeb2(URL2);
			break;
		case 2:
			URL = Urls.GET_HISTORY_CHART_DATA + "userId=" + userId + "&type=2";
			URL2 = Urls.GET_HISTORY_CHART_DATA_DOWN + "userId=" + userId
					+ "&type=2";
			getWeb1(URL);
			getWeb2(URL2);
			break;
		case 3:
			URL = Urls.GET_HISTORY_CHART_DATA + "userId=" + userId + "&type=3";
			URL2 = Urls.GET_HISTORY_CHART_DATA_DOWN + "userId=" + userId
					+ "&type=3";
			getWeb1(URL);
			getWeb2(URL2);
			break;
		case 4:
			URL = Urls.GET_HISTORY_CHART_DATA + "userId=" + userId + "&type=4";
			URL2 = Urls.GET_HISTORY_CHART_DATA_DOWN + "userId=" + userId
					+ "&type=4";
			getWeb1(URL);
			getWeb2(URL2);
			break;
		default:
			break;
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void getWeb1(final String URL) {
		WebSettings webSettings = main_webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);

		// WebView加载web资源
		main_webView.loadUrl(URL + "&flag=0");
		// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		main_webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
			@Override
			public void onReceivedSslError(WebView view,final SslErrorHandler handler,
										   SslError error) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				String message = "SSL Certificate error.";
				switch (error.getPrimaryError()) {
					case SslError.SSL_UNTRUSTED:
						message = "The certificate authority is not trusted.";
						break;
					case SslError.SSL_EXPIRED:
						message = "The certificate has expired.";
						break;
					case SslError.SSL_IDMISMATCH:
						message = "The certificate Hostname mismatch.";
						break;
					case SslError.SSL_NOTYETVALID:
						message = "The certificate is not yet valid.";
						break;
					case SslError.SSL_DATE_INVALID:
						message = "The date of the certificate is invalid";
						break;
					case SslError.SSL_INVALID:
					default:
						message = "A generic error occurred";
						break;
				}
				message += " Do you want to continue anyway?";

				builder.setTitle("SSL Certificate Error");
				builder.setMessage(message);

				builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						handler.cancel();
					}
				});
				builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						handler.cancel();
					}
				});
				final AlertDialog dialog = builder.create();
				dialog.show();
			}
		});
		main_layout_friend_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), WebViewActivity.class)
						.putExtra("url", URL + "&flag=1"));
			}
		});
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void getWeb2(final String URL) {
		WebSettings webSettings = main_webView2.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);

		// WebView加载web资源
		main_webView2.loadUrl(URL + "&flag=0");
		// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		main_webView2.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
			@Override
			public void onReceivedSslError(WebView view,final SslErrorHandler handler,
										   SslError error) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				String message = "SSL Certificate error.";
				switch (error.getPrimaryError()) {
					case SslError.SSL_UNTRUSTED:
						message = "The certificate authority is not trusted.";
						break;
					case SslError.SSL_EXPIRED:
						message = "The certificate has expired.";
						break;
					case SslError.SSL_IDMISMATCH:
						message = "The certificate Hostname mismatch.";
						break;
					case SslError.SSL_NOTYETVALID:
						message = "The certificate is not yet valid.";
						break;
					case SslError.SSL_DATE_INVALID:
						message = "The date of the certificate is invalid";
						break;
					case SslError.SSL_INVALID:
					default:
						message = "A generic error occurred";
						break;
				}
				message += " Do you want to continue anyway?";

				builder.setTitle("SSL Certificate Error");
				builder.setMessage(message);

				builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						handler.cancel();
					}
				});
				builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						handler.cancel();
					}
				});
				final AlertDialog dialog = builder.create();
				dialog.show();
			}
		});
		main_layout_friend_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), WebViewActivity.class)
						.putExtra("url", URL + "&flag=1"));
			}
		});
	}

}
