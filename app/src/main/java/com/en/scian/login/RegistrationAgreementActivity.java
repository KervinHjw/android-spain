package com.en.scian.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ab.view.ioc.AbIocView;
import com.en.scian.BaseActivity;
import com.en.scian.ExampleApplication;
import com.en.scian.R;
import com.en.scian.network.Urls;

/**
 * 注册协议的Activity
 * 
 * @author zhangp
 * 
 */
public class RegistrationAgreementActivity extends BaseActivity {
	/**
	 * tab_title
	 */
	@AbIocView(id = R.id.search_titleText)
	private TextView titleText;
	/**
	 * tab_left
	 */
	@AbIocView(id = R.id.search_leftLayout, click = "btnClick")
	private LinearLayout leftLayout;
	/**
	 * tab_right
	 */
	@AbIocView(id = R.id.search_rightLayout, click = "btnClick")
	private LinearLayout rightLayout;
	/**
	 * tv_right
	 */
	@AbIocView(id = R.id.search_right_txtView)
	private TextView tv_right;
	@AbIocView(id = R.id.ri_next, click = "btnClick")
	public Button ri_next;
	private String phone = "";
	private String verificationCode = "";
	private TextView re_text;
	private WebView webView;
	private ProgressBar pb;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration_agreement);
		ExampleApplication.getInstance().addActivity(this);
		// 获取Http工具类
		initview();
		setData();
	}

	//初始化数据
	private void setData() {
		webView.loadUrl("http://192.168.2.28:8080/lude/staticPage/registrationAgreement.html");
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

	private void initview() {
		// TODO Auto-generated method stub
		titleText.setText(this.getResources().getString(R.string.zhucexieyi));
		leftLayout.setVisibility(View.VISIBLE);
		rightLayout.setVisibility(View.VISIBLE);
		tv_right.setVisibility(View.VISIBLE);
		tv_right.setTextSize(14);
		tv_right.setText(this.getResources().getString(R.string.butongyi));
		if (getIntent().getStringExtra("phone") != null
				&& !getIntent().getStringExtra("phone").equals("null")
				&& !getIntent().getStringExtra("phone").equals("")) {
			phone = getIntent().getStringExtra("phone");
		}
		if (getIntent().getStringExtra("verificationCode") != null
				&& !getIntent().getStringExtra("verificationCode").equals(
						"null")
				&& !getIntent().getStringExtra("verificationCode").equals("")) {
			verificationCode = getIntent().getStringExtra("verificationCode");
		}
		ri_next = (Button) this.findViewById(R.id.ri_next);
		//re_text = (TextView) this.findViewById(R.id.re_text);
		//re_text.setText(R.string.re_text);
		
		webView = (WebView) findViewById(R.id.webView);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);

		pb = (ProgressBar) findViewById(R.id.pb);
		pb.setMax(100);

		pd = new ProgressDialog(this);
		pd.setMessage(getResources().getString(R.string.zhengzaijiazaiqingshaohou));
		
	}

	public void btnClick(View v) {
		switch (v.getId()) {
		case R.id.search_leftLayout:
			finish();
			break;
		case R.id.ri_next:
			Intent intent = new Intent();
			intent.putExtra("TongYi", true);
			setResult(1, intent);
			finish();
			break;
		case R.id.search_rightLayout:// 不同意
			Intent intent1 = new Intent();
			intent1.putExtra("TongYi", false);
			setResult(2, intent1);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			startActivity(new Intent(RegistrationAgreementActivity.this,
					RegisterActivity.class).putExtra("isTongYi", false)
					.putExtra("phone", phone)
					.putExtra("verificationCode", verificationCode));
			finish();
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		webView = null;
		pb = null;
		pd = null;
		System.gc();
		super.onDestroy();
	}
}
