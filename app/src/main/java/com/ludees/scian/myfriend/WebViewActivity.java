package com.ludees.scian.myfriend;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ludees.scian.BaseActivity;
import com.ludees.scian.R;

public class WebViewActivity extends BaseActivity {
	private WebView large_web;
	private String URL;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		setIsHuaDong(false);
		pd = new ProgressDialog(this);
		pd.setMessage(getResources().getString(R.string.zhengzaijiazaiqingshaohou));
		URL = getIntent().getStringExtra("url");
		large_web = (WebView) findViewById(R.id.large_web);
		WebSettings webSettings = large_web.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);
		large_web.loadUrl(URL);
		large_web.setWebChromeClient(new WebChromeClient() {
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		large_web = null;
		URL = null;
		pd = null;
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}
}
