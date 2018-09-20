package com.ludees.scian.setting;

import java.util.HashMap;

import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ludees.scian.BaseActivity;
import com.ludees.scian.ExampleApplication;
import com.ludees.scian.R;
import com.ludees.scian.network.Urls;

/**
 * 使用说明
 * 
 * @author jiyx
 * 
 */
public class InstructionsActivity extends BaseActivity implements
		OnClickListener {
	private TextView title;
	private LinearLayout back;
	private WebView webView;
	private String xxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_instructions);
		initViews();
		initListener();
		setData();
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		ExampleApplication.getInstance().addActivity(this);
		title = (TextView) findViewById(R.id.search_titleText);
		back = (LinearLayout) findViewById(R.id.search_leftLayout);

		webView = (WebView) findViewById(R.id.webView);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);
		xxt = this.getResources().getString(R.string.xiaoxitou);
	}

	/**
	 * 设置初始化数据
	 */
	private void setData() {
		title.setText(getResources().getString(R.string.setting_instruction));
		HashMap<String, String> hashMap = new HashMap<String,String>();
		hashMap.put("lord-app-language", xxt);
		// WebView加载web资源
		webView.loadUrl(Urls.SHOW_USER_INFO);
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
				final AlertDialog.Builder builder = new AlertDialog.Builder(InstructionsActivity.this);
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
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		back.setOnClickListener(this);
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

	@Override
	public void onBackPressed() {
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		finish();
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		title = null;
		back = null;
		webView = null;
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}
}
