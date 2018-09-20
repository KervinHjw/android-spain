package com.ludees.scian.qrcode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ludees.scian.BaseActivity;
import com.ludees.scian.ExampleApplication;
import com.ludees.scian.R;
import com.ludees.scian.util.DisplayUtils;
import com.ludees.scian.util.SettingUtils;
import com.google.zxing.WriterException;
import com.zf.myzxing.camera.EncodingHandler;

/**
 * 我的二维码
 * 
 * @author jiyx
 * 
 */
public class QRCodeActivity extends BaseActivity implements OnClickListener {
	private TextView title;
	private LinearLayout back;

	private ImageView personalcenter_qr_img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personalcenter_qrcode);
		initViews();
		initListener();
		setData();
		setContext(this);
		setIsResult(true);
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		ExampleApplication.getInstance().addActivity(this);
		title = (TextView) findViewById(R.id.search_titleText);
		back = (LinearLayout) findViewById(R.id.search_leftLayout);

		personalcenter_qr_img = (ImageView) findViewById(R.id.personalcenter_qr_img);
	}

	/**
	 * 设置初始化数据
	 */
	private void setData() {
		String qrString = getIntent().getStringExtra("QR");
		String userId = SettingUtils.get(QRCodeActivity.this, "userId", "");
		if (qrString.equals(userId)) {
			title.setText(this.getResources().getString(R.string.wdewm));
		} else {
			title.setText(this.getResources().getString(R.string.myfriend_add_scan));
		}

		try {
			Bitmap createQRCode = EncodingHandler.createQRCode(qrString, 320);
			personalcenter_qr_img.setImageBitmap(createQRCode);
			LayoutParams layoutParams = personalcenter_qr_img.getLayoutParams();
			layoutParams.width = DisplayUtils.dip2px(this, 160);
			layoutParams.height = DisplayUtils.dip2px(this, 160);
			personalcenter_qr_img.setLayoutParams(layoutParams);
		} catch (WriterException e) {
			e.printStackTrace();
		}

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
			finishPage();
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finishPage();
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO 
		title = null;
		back = null;
		personalcenter_qr_img = null;
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}
	public void finishPage() {
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		finish();
	}
}
