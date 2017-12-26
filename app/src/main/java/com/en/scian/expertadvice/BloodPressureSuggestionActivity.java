package com.en.scian.expertadvice;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.en.scian.BaseActivity;
import com.en.scian.R;
import com.en.scian.network.Urls;

/**
 * 血压建议
 * 
 * @author jiyx
 * 
 */
public class BloodPressureSuggestionActivity extends BaseActivity implements
		OnClickListener {
	private TextView title;
	private LinearLayout back;

	private WebView webView;
	private ProgressBar pb;
	private ProgressDialog pd;

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
	@SuppressLint("SetJavaScriptEnabled")
	private void initViews() {
		title = (TextView) findViewById(R.id.search_titleText);
		back = (LinearLayout) findViewById(R.id.search_leftLayout);

		webView = (WebView) findViewById(R.id.webView);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);

		pb = (ProgressBar) findViewById(R.id.pb);
		pb.setMax(100);

		pd = new ProgressDialog(this);
		pd.setMessage(getResources().getString(R.string.zhengzaijiazaiqingshaohou));
	}

	/**
	 * 设置初始化数据
	 */
	private void setData() {
		title.setText(getResources().getString(R.string.dailyrecommendationactivity_wzxq));
		int articleId = getIntent().getIntExtra("articleId", 0);
		// WebView加载web资源
		webView.loadUrl(Urls.EXPERT_ADVICE_INFO + "articleId=" + articleId);
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

		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				pd.show();
				if (newProgress == 100) {
					pd.dismiss();
				}

				super.onProgressChanged(view, newProgress);
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
		super.onBackPressed();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		title = null;
		back = null;
		webView = null;
		pb = null;
		pd = null;
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}
}
