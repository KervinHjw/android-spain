package com.en.scian.messagecenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.en.scian.BaseActivity;
import com.en.scian.ExampleApplication;
import com.en.scian.R;
import com.en.scian.network.Urls;

/**
 * 消息详情页面
 * 
 * @author jiyx
 * 
 */
public class MessageDetailActivity extends BaseActivity implements
		OnClickListener {
	private TextView title;
	private LinearLayout back;

	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messagecenter_message_detail);
		initViews();
		initListener();
		setData();
	}

	/**
	 * 初始化控件
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void initViews() {
		ExampleApplication.getInstance().addActivity(this);
		title = (TextView) findViewById(R.id.search_titleText);
		back = (LinearLayout) findViewById(R.id.search_leftLayout);

		webView = (WebView) findViewById(R.id.webView);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);
	}

	/**
	 * 设置初始化数据
	 */
	private void setData() {
		title.setText(getResources().getString(R.string.msg_detail));

		int messageId = getIntent().getIntExtra("messageId", 0);
		// WebView加载web资源
		webView.loadUrl(Urls.GET_SYSTEM_MESSAGE_DETAIL + "messageId="
				+ messageId);
		// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
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
			startActivity(new Intent(this,
					MessageCenterSystemMessageActivity.class));
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			finish();
			break;
		}
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
