package com.en.scian.setting;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.en.scian.BaseActivity;
import com.en.scian.ExampleApplication;
import com.en.scian.R;
import com.en.scian.entity.ResponseCommon;
import com.en.scian.login.LoginActivity;
import com.en.scian.network.Urls;
import com.en.scian.util.SettingUtils;
import com.en.scian.util.ToastUtils;
import com.en.scian.view.CustomDialog;
import com.en.scian.view.CustomDialog.Builder;
import com.google.gson.Gson;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

/**
 * 设置
 * 
 * @author jiyx
 * 
 */
public class SettingActivity extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener {

	private TextView title;
	private LinearLayout back;
	private RelativeLayout setting_instructions;
	private RelativeLayout setting_cache;
	private RelativeLayout setting_about;
	private Button setting_exit;

	//private RelativeLayout setting_version_update;

	private ToggleButton setting_tb_push;

	private Activity activity;
	private Gson gson;

	private int isPush;
	private FinalHttp fh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
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
		activity = this;
		gson = new Gson();
		ExampleApplication.getInstance().addActivity(this);
		title = (TextView) findViewById(R.id.search_titleText);
		back = (LinearLayout) findViewById(R.id.search_leftLayout);

		setting_instructions = (RelativeLayout) findViewById(R.id.setting_instructions);
		setting_cache = (RelativeLayout) findViewById(R.id.setting_cache);
		setting_about = (RelativeLayout) findViewById(R.id.setting_about);
		//setting_version_update = (RelativeLayout) findViewById(R.id.setting_version_update);
		setting_exit = (Button) findViewById(R.id.setting_exit);

		//setting_tb_push = (ToggleButton) findViewById(R.id.setting_tb_push);

		fh = new FinalHttp();
		fh.configTimeout(15 * 1000);

	}

	/**
	 * 设置初始化数据
	 */
	private void setData() {
		title.setText(getResources().getString(R.string.home_left_menu_setting_text));
		// UserInfo user = app.getUser();
		// if (user != null) {
		// if (user.getIsPush() == 1) {
		// isPush = 1;
		// setting_tb_push.setChecked(true);
		// } else if (user.getIsPush() == 2) {
		// isPush = 2;
		// setting_tb_push.setChecked(false);
		// }
		// }
		int i = SettingUtils.get(this, "isPush", 1);
		/*if (i == 1) {
			setting_tb_push.setChecked(true);
		} else if (i == 2) {
			setting_tb_push.setChecked(false);
		}*/
		SettingUtils.set(this, "isPush", i);
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		back.setOnClickListener(this);

		setting_instructions.setOnClickListener(this);
		setting_cache.setOnClickListener(this);
		setting_about.setOnClickListener(this);
		//setting_version_update.setOnClickListener(this);
		//setting_tb_push.setOnCheckedChangeListener(this);

		setting_exit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_leftLayout:// 返回
			finishPage();
			break;
		case R.id.setting_instructions:// 使用说明
			startActivity(new Intent(this, InstructionsActivity.class));
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.setting_cache:// 清楚缓存
			showDeleteCache();
			break;
		case R.id.setting_about:// 关于鹿得
			startActivity(new Intent(this, AboutActivity.class));
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.setting_exit:// 退出登录
			showExitDialog();
			break;
		/*case R.id.setting_version_update:// 检测
			checkVersion();
			break;*/
		}

	}

	/**
	 * 退出登录对话框
	 */
	private void showExitDialog() {
		CustomDialog.Builder builder = new Builder(SettingActivity.this);
		builder.setTitle(this.getResources().getString(R.string.setting_logout));
		builder.setMessage(this.getResources().getString(R.string.shifoutuichu));
		// 确定按钮点击事件处理
		builder.setPositiveButton(this.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				startActivity(new Intent(SettingActivity.this,
						LoginActivity.class));

				// 关闭所有已打开activity
				ExampleApplication.getInstance().exit();
				// 清除本地share和数据库
				// DataCleanManager.cleanSharedPreference(activity);
				SettingUtils.set(SettingActivity.this, "userPic", "");
				SettingUtils.set(SettingActivity.this, "userId", "");
				SettingUtils.set(SettingActivity.this, "usermail", "");
				SettingUtils.set(SettingActivity.this, "pwd", "");
				DataCleanManager.cleanDatabases(activity);

			}
		});
		builder.setNegativeButton(this.getResources().getString(R.string.dialog_select_pic_cancel), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	/**
	 * 版本检测
	 */
	private void checkVersion() {
		/** 开始调用自动更新函数 **/
		// 从服务器获取更新信息
		UmengUpdateAgent.forceUpdate(activity);
		// 是否在只在wifi下提示更新，默认为true
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		// 是否自动弹出更新对话框
		UmengUpdateAgent.setUpdateAutoPopup(true);
		// 下载新版本过程事件监听，可设为null
		UmengUpdateAgent.setDownloadListener(null);
		// 用户点击更新对话框按钮的回调事件，直接null
		UmengUpdateAgent.setDialogListener(null);
		// 从服务器获取更新信息的回调函数
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus,
					UpdateResponse updateInfo) {
				switch (updateStatus) {
				case 0:// 有更新
					UmengUpdateAgent.showUpdateDialog(SettingActivity.this,
							updateInfo);
					break;
				case 1: // 无更新
					ToastUtils.TextToast(SettingActivity.this, SettingActivity.this.getResources().getString(R.string.dangqianyishizuixinban));
					break;
				case 2:// 如果设置为wifi下更新且wifi无法打开时调用
					break;
				case 3:// 连接超时
					ToastUtils.TextToast(SettingActivity.this, SettingActivity.this.getResources().getString(R.string.lianjiechaoshiqingshaohouchongshi));
					break;
				}
			}
		});

	}

	/**
	 * 清楚缓存
	 */
	private void showDeleteCache() {

		new AsyncTask<Void, Void, Boolean>() {

			private ProgressDialog pd;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pd = new ProgressDialog(activity);
				pd.setCancelable(false);
				pd.setMessage(activity.getResources().getString(R.string.zhengzaiqingchuhuancun));
				pd.show();
			}

			@Override
			protected Boolean doInBackground(Void... params) {

				DataCleanManager.cleanInternalCache(activity);
				try {
					Thread.sleep(2 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				if (result = true) {
					pd.dismiss();
					Toast.makeText(activity, activity.getResources().getString(R.string.qingchuhuancunchenggong), Toast.LENGTH_SHORT)
							.show();
				}
			}
		}.execute();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO
		String url = Urls.UPDATE_USERINFO;
		AjaxParams params = new AjaxParams();
		params.put("userId", SettingUtils.get(this, "userId", ""));
		if (isChecked) {
			params.put("isPush", "1");
			isPush = 1;
		} else {
			params.put("isPush", "2");
			isPush = 2;
		}
		updatePushState(url, params);

	}

	/**
	 * 修改推送状态
	 * 
	 * @param url
	 * @param params
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void updatePushState(String url, final AjaxParams params) {
		fh.post(url, params, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				ToastUtils.TextToast(SettingActivity.this, SettingActivity.this.getResources().getString(R.string.fuwuqilianjieyichang));
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String content = (String) t;
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					prompt(common.getMsg());
					return;
				} else {
					SettingUtils.set(SettingActivity.this, "isPush", isPush);
					// 1.显示修改成功提醒
					// prompt(common.getMsg());
					// // 2.修改用户全局变了的isPush值
					// UserInfo user = app.getUser();
					// if (user != null) {
					// user.setIsPush(isPush);
					// app.setUser(user);
					// }
				}
			}

		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
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
		setting_instructions = null;
		setting_cache = null;
		setting_about = null;
		setting_exit = null;
		//setting_version_update = null;
		//setting_tb_push = null;
		activity = null;
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
