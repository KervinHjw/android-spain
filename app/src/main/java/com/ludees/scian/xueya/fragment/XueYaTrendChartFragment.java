package com.ludees.scian.xueya.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.ludees.scian.R;
import com.ludees.scian.myfriend.WebViewActivity;
import com.ludees.scian.network.Urls;
import com.ludees.scian.util.SettingUtils;

public class XueYaTrendChartFragment extends Fragment {
	private Context mContext;

	private String URL = "";
	private String URL2 = "";
	private String userId;
	private String bloodPressureId = "";
	/**
	 * 是否可见
	 */
	private ProgressDialog pd;
	private WebView main_webView;
	private WebView main_webView2;
	private RelativeLayout main_layout_friend_1;
	private RelativeLayout main_layout_friend_2;
	private View view;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.xueya_trend_chart_fragment, null);
		initView(view);
		userId = SettingUtils.get(mContext, "userId", "");
		getData();
		return view;
	}

	private void initView(View v) {
		mContext = getActivity();
		main_webView = (WebView) v.findViewById(R.id.celiang_webView);
		main_webView2 = (WebView) v.findViewById(R.id.celiang_webView2);
		main_layout_friend_1 = (RelativeLayout) v
				.findViewById(R.id.celiang_friend_1);
		main_layout_friend_2 = (RelativeLayout) v
				.findViewById(R.id.celiang_friend_2);

		userId = SettingUtils.get(mContext, "userId", "");
		pd = new ProgressDialog(getActivity());
		pd.setMessage(getResources().getString(R.string.zhengzaijiazaiqingshaohou));
	}

	@Override
	public void onDestroy() {
		// TODO
		super.onDestroy();
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
		if (pd != null) {
			pd = null;
		}
		view = null;
		System.gc();
	}

	@Override
	public void onStop() {
		// TODO
		super.onStop();
		if (main_webView != null) {
			main_webView.stopLoading();
		}
		if (main_webView2 != null) {
			main_webView2.stopLoading();
		}
		if (pd != null) {
			pd.dismiss();
		}
	}

	/**
	 * 获取数据
	 */
	public void getData() {
		userId = SettingUtils.get(mContext, "userId", "");
		bloodPressureId = String.valueOf(getActivity().getIntent().getIntExtra(
				"bloodPressureId", 0));
		if (bloodPressureId.equals("0")) {
			bloodPressureId = "";
		}
		URL = Urls.GET_DATA_CHART + "userId=" + userId + "&bloodPressureId="
				+ bloodPressureId;
		URL2 = Urls.GET_DATA_CHART_DOWN + "userId=" + userId
				+ "&bloodPressureId=" + bloodPressureId;
		getWeb1(URL);
		getWeb2(URL2);

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
		main_webView.setWebChromeClient(new WebChromeClient() {
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
				startActivity(new Intent(getActivity(), WebViewActivity.class)
						.putExtra("url", URL + "&flag=1"));
			}
		});
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		mContext = null;
		URL = null;
		URL2 = null;
		userId = null;
		bloodPressureId = null;
		pd = null;
		main_webView = null;
		main_webView2 = null;
		main_layout_friend_1 = null;
		main_layout_friend_2 = null;
		view = null;
		System.gc();
		super.onDestroyView();
	}

}
