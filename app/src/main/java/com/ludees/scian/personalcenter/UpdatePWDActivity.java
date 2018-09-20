package com.ludees.scian.personalcenter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ludees.scian.BaseActivity;
import com.ludees.scian.ExampleApplication;
import com.ludees.scian.R;
import com.ludees.scian.view.CustomDialog;
import com.ludees.scian.view.CustomDialog.Builder;

/**
 * 修改密码
 * 
 * @author jiyx
 * 
 */
public class UpdatePWDActivity extends BaseActivity implements OnClickListener {
	private TextView title;
	private LinearLayout back;
	private Button pwd_btn_update;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personalcenter_pwd);
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

		pwd_btn_update = (Button) findViewById(R.id.pwd_btn_update);
	}

	/**
	 * 设置初始化数据
	 */
	private void setData() {
		title.setText(getResources().getString(R.string.personaldata_modifypwd));
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		back.setOnClickListener(this);
		pwd_btn_update.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_leftLayout:// 返回
			finish();
			break;
		case R.id.pwd_btn_update:// 返回
			CustomDialog.Builder builder = new Builder(this);
			builder.setTitle(this.getResources().getString(R.string.personaldata_modifypwd));
			builder.setMessage(this.getResources().getString(R.string.ninduimimajinxinglexiugai));
			builder.setPositiveButton(this.getResources().getString(R.string.confirm),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Toast.makeText(UpdatePWDActivity.this, UpdatePWDActivity.this.getResources().getString(R.string.mimaxiugaichenggong),
									Toast.LENGTH_SHORT).show();

						}
					});
			builder.setNegativeButton(this.getResources().getString(R.string.dialog_select_pic_cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();

						}
					});
			builder.create().show();
			break;
		}

	}
}
