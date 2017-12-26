package com.en.scian.login;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.view.ioc.AbIocView;
import com.en.scian.BaseActivity;
import com.en.scian.R;
import com.en.scian.entity.YanZhengBean;
import com.en.scian.network.Urls;
import com.en.scian.view.TimeButton;
import com.google.gson.Gson;


public class ForgetPwdActivity extends BaseActivity implements OnClickListener{
	
	private EditText mail;
	//邮箱正则校验
	private String regex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
	private String miMa = "^[0-9a-zA-z]{6,12}$";//6~12位密码校验
	private String code = "^[a-z0-9A-Z]{5}$";
	
	@AbIocView(id = R.id.send_mail)
	private TimeButton send_mail;
	@AbIocView(id = R.id.y_z_m)
	private EditText yzm;
	@AbIocView(id = R.id.xinmima)
	private EditText pwd;
	@AbIocView(id = R.id.xinmima2)
	private EditText pwd2;
	@AbIocView(id = R.id.pwd_btn_update)
	private Button wancheng;
	/**
	 * tab_title
	 */
	@AbIocView(id = R.id.search_titleText)
	private TextView titleText;
	/**
	 * tab_left
	 */
	@AbIocView(id = R.id.search_leftLayout)
	private LinearLayout leftLayout;
	
	private boolean isMail = false;//邮箱是否正确
	private boolean isPwd = false;//密码是否正确
	private boolean isPwd2 = false;//密码是否正确
	private boolean isYzm = false;//验证码
	private FinalHttp finalHttp;
	Gson gson = new Gson();
	private String key;
	private ProgressDialog dialog;
	private String mMail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_pwd);
		send_mail.setBackgroundResource(R.drawable.button_bg_false);
		send_mail.setClickable(false);
		initView();
		initData();
		wancheng.setClickable(false);
	}

	private void initView() {
		titleText.setText(this.getResources().getString(R.string.zhaohuimima));
		mail = (EditText) findViewById(R.id.edt_forger_mail);
		send_mail.setOnClickListener(this);
		leftLayout.setOnClickListener(this);
		send_mail.setTextAfter(this.getResources().getString(R.string.miaohoufasong)).setTextBefore(this.getResources().getString(R.string.forget_mali)).setLenght(60 * 1000);
	}

	private void initData() {
		finalHttp = new FinalHttp();
		finalHttp.configTimeout(15 * 1000);
		wancheng.setOnClickListener(this);
		yzm.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(yzm.getText().toString().matches(code)){
					isYzm = true;
				}else{
					isYzm = false;
				}
				if (isMail == true && isPwd == true && isPwd2 == true && isYzm == true) {
					wancheng.setBackgroundResource(R.drawable.button_bg);
					wancheng.setClickable(true);
				} else {
					wancheng.setBackgroundResource(R.drawable.button_bg_false);
					wancheng.setClickable(false);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		mail.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(mail.getText().toString().matches(regex)){
					isMail = true;
					send_mail.setBackgroundResource(R.drawable.button_bg);
					send_mail.setClickable(true);
				}else{
					isMail = false;
					send_mail.setBackgroundResource(R.drawable.button_bg_false);
					send_mail.setClickable(false);
				}
				if (isMail == true && isPwd == true && isPwd2 == true && isYzm == true) {
					wancheng.setBackgroundResource(R.drawable.button_bg);
					wancheng.setClickable(true);
				} else {
					wancheng.setBackgroundResource(R.drawable.button_bg_false);
					wancheng.setClickable(false);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		pwd.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(pwd.getText().toString().matches(miMa)){
					isPwd = true;
				}else{
					isPwd = false;
				}
				if (isMail == true && isPwd == true && isPwd2 == true && isYzm == true) {
					wancheng.setBackgroundResource(R.drawable.button_bg);
					wancheng.setClickable(true);
				} else {
					wancheng.setBackgroundResource(R.drawable.button_bg_false);
					wancheng.setClickable(false);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		pwd2.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(pwd2.getText().toString().equals(pwd.getText().toString())){
					isPwd2 = true;
				}else{
					isPwd2 = false;
				}
				if (isMail == true && isPwd == true && isPwd2 == true && isYzm == true) {
					wancheng.setBackgroundResource(R.drawable.button_bg);
					wancheng.setClickable(true);
				} else {
					wancheng.setBackgroundResource(R.drawable.button_bg_false);
					wancheng.setClickable(false);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send_mail:
			if(isMail){
				mMail = mail.getText().toString();
				String url = Urls.SEND_MAIL;
				AjaxParams params = new AjaxParams();
				params.put("mail", mMail);
				sendMail(url, params);
			}else{
				prompt(ForgetPwdActivity.this.getResources().getString(R.string.shuruyouwu));
			}
			break;
		case R.id.pwd_btn_update:
			if(!pwd.getText().toString().equals(pwd2.getText().toString())){
				prompt(ForgetPwdActivity.this.getResources().getString(R.string.liangcimimashuruzhengque));
				return;
			}
			dialog = new ProgressDialog(ForgetPwdActivity.this);
			dialog.setCancelable(false);
			dialog.setMessage(ForgetPwdActivity.this.getResources().getString(R.string.zhengzailianjieqingshaohou));
			dialog.show();
			if(!yzm.getText().toString().equals(key)){
				dialog.dismiss();
				prompt(ForgetPwdActivity.this.getResources().getString(R.string.qqryzmzq));
				return;
			}
			if(mMail!=null){
				
				if(!mMail.equals(mail.getText().toString())){
					dialog.dismiss();
					prompt(ForgetPwdActivity.this.getResources().getString(R.string.qqryzmzq));
					return;
				}
			}
				String url = Urls.UPDATE_MAIL;
				String pwsd = pwd.getText().toString();
				AjaxParams params = new AjaxParams();
				params.put("usermail", mMail);
				params.put("pwd", pwsd);
				updatePwd(url,params);
			/*}else{
				prompt(ForgetPwdActivity.this.getResources().getString(R.string.shuruyanzhengmayouwu));
			}*/
			break;
		case R.id.search_leftLayout:
			finish();
			break;
		default:
			break;
		}
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void sendMail(String URL, AjaxParams params){
		finalHttp.post(URL, params, new AjaxCallBack(){


			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				prompt(strMsg);
			}
			
			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String content = (String)t;
				YanZhengBean bean = gson.fromJson(content, YanZhengBean.class);
				int i = bean.getStatus();
				if(i == 0){
					prompt(ForgetPwdActivity.this.getResources().getString(R.string.yonghubucunzai));
					return ;
				}
				key = bean.getData().getKey();
				prompt(ForgetPwdActivity.this.getResources().getString(R.string.youjianyifasong));
				
			}
		});
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updatePwd(String URL, AjaxParams params){
		finalHttp.post(URL, params, new AjaxCallBack(){
			
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				dialog.dismiss();
				prompt(ForgetPwdActivity.this.getResources().getString(R.string.wangluolianjieyichang));
			}
			
			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String content = (String)t;
				System.out.println(content);
				dialog.dismiss();
				prompt(ForgetPwdActivity.this.getResources().getString(R.string.xiugaimimachenggong));
				finish();
			}
		});
	}
	
	
	
	/**
	 * 非阻塞提示方式
	 */
	public void prompt(String content) {
		Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 非阻塞提示方式
	 */
	public void prompt(int content) {
		Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		key = null;
		dialog = null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
