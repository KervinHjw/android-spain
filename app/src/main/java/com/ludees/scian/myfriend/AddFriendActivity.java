package com.ludees.scian.myfriend;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ludees.scian.BaseActivity;
import com.ludees.scian.ExampleApplication;
import com.ludees.scian.R;
import com.ludees.scian.entity.FriendInfo;
import com.ludees.scian.entity.FriendInfoBean;
import com.ludees.scian.entity.ResponseCommon;
import com.ludees.scian.network.Urls;
import com.ludees.scian.util.SettingUtils;
import com.ludees.scian.util.ToastUtils;
import com.google.gson.Gson;
import com.zf.myzxing.CaptureActivity;

/**
 * 添加好友
 * 
 * @author jiyx
 * 
 */
public class AddFriendActivity extends BaseActivity implements OnClickListener {

	private static final int REQUEST_CODE_SCAN = 0x0010;

	private static final String DECODED_CONTENT_KEY = "codedContent";

	private TextView title;
	private LinearLayout back;

	private ImageView myfriend_add_search;
	private EditText myfriend_add_phone;
	private LinearLayout myfriend_add_scanner;
	private Button add_disanfang;

	private ListView myfriend_add_lv;
	private List<FriendInfo> list;

	private Gson gson;

	private FinalHttp fh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myfriend_add);
		initViews();
		initListener();
		setData();
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		list = new ArrayList<FriendInfo>();
		gson = new Gson();
		ExampleApplication.getInstance().addActivity(this);
		title = (TextView) findViewById(R.id.search_titleText);
		back = (LinearLayout) findViewById(R.id.search_leftLayout);

		myfriend_add_search = (ImageView) findViewById(R.id.myfriend_add_search);
		myfriend_add_phone = (EditText) findViewById(R.id.myfriend_add_phone);
		myfriend_add_scanner = (LinearLayout) findViewById(R.id.myfriend_add_scanner);

		myfriend_add_lv = (ListView) findViewById(R.id.myfriend_add_lv);
		add_disanfang = (Button) this.findViewById(R.id.add_disanfang);

		fh = new FinalHttp();
		fh.configTimeout(15 * 1000);
	}

	/**
	 * 设置初始化数据
	 */
	private void setData() {
		title.setText(this.getResources().getString(R.string.home_content_fragment_myfriend_add_friend));
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		back.setOnClickListener(this);
		myfriend_add_search.setOnClickListener(this);
		myfriend_add_scanner.setOnClickListener(this);
		add_disanfang.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_leftLayout:// 返回
			// startActivity(new Intent(this,
			// HomeActivity.class).putExtra("num",
			// 3));
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			finish();
			break;
		case R.id.myfriend_add_search:
			findFriend();
			break;
		case R.id.myfriend_add_scanner:
			startActivityForResult(new Intent(this, CaptureActivity.class),
					REQUEST_CODE_SCAN);
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			break;
		case R.id.add_disanfang:
			findFriend();
			break;
		}
	}

	/**
	 * 查找好友
	 */
	private void findFriend() {
		String friendPhone = myfriend_add_phone.getText().toString().trim();
		getLunBoTuURL(Urls.GET_FRIEND_INFO + "phone=" + friendPhone
				+ "&userId=" + SettingUtils.get(this, "userId", ""));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 扫描二维码/条码回传 RESULT_OK
		if (resultCode == REQUEST_CODE_SCAN) {
			if (data != null) {
				String content = data.getStringExtra(DECODED_CONTENT_KEY);
				// AbRequestParams params = new AbRequestParams();
				AjaxParams params = new AjaxParams();
				params.put("userId",
						SettingUtils.get(AddFriendActivity.this, "userId", ""));
				params.put("friendsId", content);
				// 发送添加好友验证
				sendFriendVerificationForURL(Urls.SEND_FRIENDS_VERIFICATION,
						params);
			}
		}
	}

	/**
	 * 扫一扫添加好友
	 * 
	 * @param url
	 * @param params
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void sendFriendVerificationForURL(String url, AjaxParams params) {
		fh.post(url, params, new AjaxCallBack() {
			private ProgressDialog pd;

			@Override
			public void onStart() {
				super.onStart();
				pd = new ProgressDialog(AddFriendActivity.this);
				pd.setCancelable(false);
				pd.setMessage(AddFriendActivity.this.getResources().getString(R.string.zhengzaifasonghaoyouyanzheng));
				pd.show();
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				pd.dismiss();
				prompt(AddFriendActivity.this.getResources().getString(R.string.tianjiahaoyoushibai));
				ToastUtils.TextToast(AddFriendActivity.this, getResources().getString(R.string.fuwuqilianjieyichang));
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				pd.dismiss();
				String content = (String) t;
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				Intent intent = new Intent(AddFriendActivity.this,
						AddFriendPromptActivity.class);
				intent.putExtra("msg", common.getMsg());
				if (common.getStatus() == 0) {// 好友验证发送失败
					intent.setFlags(0);
				} else if (common.getStatus() == 1) {// 好友验证发送成功
					intent.setFlags(1);
				}
				startActivity(intent);
				// overridePendingTransition(R.anim.push_right_in,
				// R.anim.push_right_out);
				finish();
			}

		});
	}

	/**
	 * 获取好友信息并插入当前listView
	 * 
	 * @param ss
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getLunBoTuURL(String url) {

		fh.get(url, new AjaxCallBack() {
			private ProgressDialog pd;

			@Override
			public void onStart() {
				super.onStart();
				pd = new ProgressDialog(AddFriendActivity.this);
				pd.setCancelable(false);
				pd.setMessage(AddFriendActivity.this.getResources().getString(R.string.zhengzaihuoquhaoyouxinxi));
				pd.show();
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				pd.dismiss();
				ToastUtils.TextToast(AddFriendActivity.this, getResources().getString(R.string.fuwuqilianjieyichang));
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				pd.dismiss();
				String content = (String) t;
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 3) {
					prompt(common.getMsg());
					return;
				}
				// 1.清空搜索框
				myfriend_add_phone.setText("");
				FriendInfoBean infoBean = gson.fromJson(content,
						FriendInfoBean.class);
				FriendInfo data = infoBean.getData();
				if (data != null) {
					list.add(data);
					AddFriendAdapter adapter = new AddFriendAdapter(
							AddFriendActivity.this, list);
					myfriend_add_lv.setAdapter(adapter);
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		title = null;
		back = null;
		myfriend_add_search = null;
		myfriend_add_phone = null;
		myfriend_add_scanner = null;
		add_disanfang = null;
		myfriend_add_lv = null;
		list = null;
		gson = null;
		fh = null;
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}
}
