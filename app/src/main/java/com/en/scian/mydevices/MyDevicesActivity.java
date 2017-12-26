package com.en.scian.mydevices;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.en.scian.BaseActivity;
import com.en.scian.ExampleApplication;
import com.en.scian.R;
import com.en.scian.entity.MyEquipment;
import com.en.scian.entity.MyEquipmentBean;
import com.en.scian.entity.ResponseCommon;
import com.en.scian.network.Urls;
import com.en.scian.util.SettingUtils;
import com.en.scian.util.ToastUtils;
import com.google.gson.Gson;

/**
 * 我的设备
 * 
 * @author jiyx
 * 
 */
public class MyDevicesActivity extends BaseActivity implements OnClickListener {
	private TextView title;
	private LinearLayout back;
	private ListView mydevices_lv;
	private List<MyEquipment> list;
	private Gson gson;
	private FinalHttp fh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personalcenter_mydevices);
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
		list = new ArrayList<MyEquipment>();
		gson = new Gson();
		ExampleApplication.getInstance().addActivity(this);
		title = (TextView) findViewById(R.id.search_titleText);
		back = (LinearLayout) findViewById(R.id.search_leftLayout);

		mydevices_lv = (ListView) findViewById(R.id.personalcenter_mydevices_lv);

		fh = new FinalHttp();
		fh.configTimeout(15 * 1000);
	}

	/**
	 * 设置初始化数据
	 */
	private void setData() {
		title.setText(getResources().getString(R.string.home_left_menu_mydevices_text));

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				getFMyEquementList(Urls.MY_DEVICES_LIST
						+ "userId="
						+ SettingUtils
								.get(MyDevicesActivity.this, "userId", "")
						+ "&pageNo=0&pageSize=100");
			}
		}, 1 * 1000);

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

	/**
	 * 获取我的设备列表
	 * 
	 * @param url
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getFMyEquementList(String url) {

		fh.get(url, new AjaxCallBack() {
			private ProgressDialog pd;

			@Override
			public void onStart() {
				super.onStart();
				pd = new ProgressDialog(MyDevicesActivity.this);
				pd.setCancelable(false);
				pd.setMessage(MyDevicesActivity.this.getResources().getString(R.string.zhengzaihuoquwodeshebeiliebiao));
				pd.show();
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				pd.dismiss();
				ToastUtils.TextToast(MyDevicesActivity.this, getResources().getString(R.string.fuwuqilianjieyichang));
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				pd.dismiss();
				String content = (String) t;
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					prompt(common.getMsg());
					mydevices_lv.setAdapter(new MyDevicesAdapter(
							MyDevicesActivity.this, list));
					return;
				}
				MyEquipmentBean bean = gson.fromJson(content,
						MyEquipmentBean.class);
				List<MyEquipment> data = bean.getData();
				if (data != null) {
					list.addAll(data);
					mydevices_lv.setAdapter(new MyDevicesAdapter(
							MyDevicesActivity.this, list));
				}

			}

		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		title = null;
		back = null;
		mydevices_lv = null;
		list = null;
		gson = null;
		fh = null;
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}

	public void finishPage() {
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		finish();
	}
}
