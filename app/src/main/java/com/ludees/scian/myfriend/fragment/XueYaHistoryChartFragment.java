package com.ludees.scian.myfriend.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.ludees.scian.R;
import com.ludees.scian.myfriend.WebViewActivity;
import com.ludees.scian.network.Urls;
import com.ludees.scian.setting.InstructionsActivity;
import com.ludees.scian.util.SettingUtils;

/**
 * 血压历史数据趋势图
 * 
 * @author zhaok
 * 
 */
public class XueYaHistoryChartFragment extends Fragment {

	private String userId;
	private WebView webView;
	private WebView webView2;
	private int num = 0;
	private RelativeLayout layout_friend_1;
	private RelativeLayout layout_friend_2;
	private ProgressDialog pd;
	private String URL;
	private String URL2;
	private String myFriendId;
	private String isFirstChart = "";
	private boolean isFirstIn = true;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO
		View view = inflater.inflate(R.layout.myfriend_history_chart, null);
		initView(view);
		return view;
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView(View v) {
		layout_friend_1 = (RelativeLayout) v.findViewById(R.id.layout_friend_1);
		layout_friend_2 = (RelativeLayout) v.findViewById(R.id.layout_friend_2);
		webView = (WebView) v.findViewById(R.id.webView);
		webView2 = (WebView) v.findViewById(R.id.webView2);
		myFriendId = getActivity().getIntent().getStringExtra("myFriendId");

		userId = getActivity().getIntent().getStringExtra("myFriendId");
		pd = new ProgressDialog(getActivity());
		pd.setMessage(getResources().getString(R.string.zhengzaijiazaiqingshaohou));
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub13991204591
		super.onResume();
		// userId = SettingUtils.get(getActivity(), "userId", "");
		if (!isFirstIn) {
			isFirstChart = SettingUtils.get(getActivity(),
					"isFirstFriendChart", "1");
			if (isFirstChart.equals("1")) {
				getData();
			}
		}

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		if (isFirstIn && isVisibleToUser) {
			isFirstChart = SettingUtils.get(getActivity(),
					"isFirstFriendChart", "1");
			if (isFirstChart.equals("1")) {
				getData();
			}
			isFirstIn = false;
		}
		super.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (webView != null) {
			webView.destroy();
			webView.destroyDrawingCache();
			webView = null;
		}
		if (webView2 != null) {
			webView2.destroy();
			webView2.destroyDrawingCache();
			webView2 = null;
		}
		if (pd != null) {
			pd = null;
		}
	}

	/**
	 * 获取数据
	 */
	public void getData() {
		// num = getActivity().getIntent().getIntExtra("type", 0);
		SettingUtils.set(getActivity(), "isFirstFriendChart", "2");
		num = Integer.valueOf(SettingUtils.get(getActivity(), "myFriendData",
				"0"));
		switch (num) {
		case 0:
			URL = Urls.GET_HISTORY_CHART_DATA + "userId=" + myFriendId
					+ "&type=1";
			URL2 = Urls.GET_HISTORY_CHART_DATA_DOWN + "userId=" + myFriendId
					+ "&type=1";
			getWeb1(URL);
			getWeb2(URL2);
			break;
		case 1:
			URL = Urls.GET_HISTORY_CHART_DATA + "userId=" + myFriendId
					+ "&type=1";
			URL2 = Urls.GET_HISTORY_CHART_DATA_DOWN + "userId=" + myFriendId
					+ "&type=1";
			getWeb1(URL);
			getWeb2(URL2);
			break;
		case 2:
			URL = Urls.GET_HISTORY_CHART_DATA + "userId=" + myFriendId
					+ "&type=2";
			URL2 = Urls.GET_HISTORY_CHART_DATA_DOWN + "userId=" + myFriendId
					+ "&type=2";
			getWeb1(URL);
			getWeb2(URL2);
			break;
		case 3:
			URL = Urls.GET_HISTORY_CHART_DATA + "userId=" + myFriendId
					+ "&type=3";
			URL2 = Urls.GET_HISTORY_CHART_DATA_DOWN + "userId=" + myFriendId
					+ "&type=3";
			getWeb1(URL);
			getWeb2(URL2);
			break;
		case 4:
			URL = Urls.GET_HISTORY_CHART_DATA + "userId=" + myFriendId
					+ "&type=4";
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
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);

		// WebView加载web资源
		webView.loadUrl(URL + "&flag=0");
		// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		webView.setWebViewClient(new WebViewClient() {
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
				final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
		layout_friend_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), WebViewActivity.class)
						.putExtra("url", URL + "&flag=1"));
			}
		});
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					pd.dismiss();
				}

				super.onProgressChanged(view, newProgress);
			}
		});
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void getWeb2(final String URL) {
		WebSettings webSettings = webView2.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);

		// WebView加载web资源
		webView2.loadUrl(URL + "&flag=0");
		// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		webView2.setWebViewClient(new WebViewClient() {
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
				final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
		layout_friend_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), WebViewActivity.class)
						.putExtra("url", URL + "&flag=1"));
			}
		});
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		userId = null;
		webView = null;
		webView2 = null;
		layout_friend_1 = null;
		layout_friend_2 = null;
		pd = null;
		URL = null;
		URL2 = null;
		myFriendId = null;
		isFirstChart = null;
		super.onDestroyView();
	}

}
