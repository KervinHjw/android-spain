package com.ludees.scian.login;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.view.ioc.AbIocView;
import com.ludees.scian.BaseActivity;
import com.ludees.scian.ExampleApplication;
import com.ludees.scian.R;
import com.ludees.scian.entity.ResponseCommon;
import com.ludees.scian.network.Urls;
import com.ludees.scian.util.Code;
import com.google.gson.Gson;

/**
 * 手机验证码注册的Activity
 * 
 * @author zhangp
 * 
 */
public class RegisterActivity extends BaseActivity implements OnClickListener {
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
	/**
	 * tab_right
	 */
	@AbIocView(id = R.id.search_rightLayout)
	private LinearLayout rightLayout;
	/**
	 * 手机号
	 */
	@AbIocView(id = R.id.ri_edt_phone)
	private EditText edt_phone;
	/**
	 * 验证码
	 */
	@AbIocView(id = R.id.ri_edt_y_z_m)
	private EditText edt_y_z_m;
	/**
	 * 注册
	 */
	@AbIocView(id = R.id.ri_next)
	private Button next;
	/**
	 * 记住密码选框
	 */
	@AbIocView(id = R.id.read_message)
	private CheckBox rememberPassword;
	
	@AbIocView(id = R.id.et_y_z_m)
	private EditText bd_y_z_m;
	
	/**
	 * 注册协议
	 */
	@AbIocView(id = R.id.register_message)
	private TextView register_message;

	int usertype;

	Gson gson = new Gson();
	private FinalHttp fh;

	String sendTime;
	/**
	 * 手机号码提示
	 */
	@AbIocView(id = R.id.ri_tv_phone)
	private TextView ri_tv_phone;
	/**
	 * 手机验证码
	 */
	@AbIocView(id = R.id.ri_tv_y_z_m)
	private TextView ri_tv_y_z_m;
	private boolean isphone = false;
	/**
	 * 验证码是否正确
	 */
	private boolean isy_z_m = false;

	/**
	 * 手机验证码
	 */
	@SuppressWarnings("unused")
	private int verficationCode = 0;

	/**
	 * 是否是第三方登录
	 */
	private boolean isDiSanFang = false;
	private String userName = "";
	/**
	 * 验证协议
	 */
	@AbIocView(id = R.id.re_layout)
	private LinearLayout re_layout;
	private boolean isTongYi = true;
	@AbIocView(id = R.id.iv_showCode)
	private ImageView iv_showCode;
	private String code;
	private boolean isBdy_z_m = false;//本地验证码
	//邮箱正则校验
	private String regex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
	private String miMa = "^[0-9a-zA-z]{6,12}$";//6~12位密码校验
	private boolean b;
	private String phone;
	private String verificationCode;
	private ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		ExampleApplication.getInstance().addActivity(this);
		initview();
		next.setClickable(false);
		ExampleApplication.getInstance().addActivity(this);
	}

	private void initview() {
		// TODO
		fh = new FinalHttp();
		fh.configTimeout(15 * 1000);
		
		//图片验证码
		iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
		iv_showCode.setOnClickListener(this);
		code = Code.getInstance().getCode();
		register_message.setOnClickListener(this);
		
		/*isTongYi = getIntent().getBooleanExtra("TongYi", true);
		String phone = getIntent().getStringExtra("phone");
		String verificationCode = getIntent()
				.getStringExtra("verificationCode");*/
		
		/*if (verificationCode != null && !verificationCode.equals("null")
				&& !verificationCode.equals("")) {
			edt_y_z_m.setText(verificationCode);
		}
		if (phone != null && !phone.equals("null")
				&& !phone.equals("")) {
			edt_phone.setText(phone);
		}
		
		if (isTongYi) {
			rememberPassword.setChecked(true);
		} else {
			rememberPassword.setChecked(false);
		}*/
		if (isDiSanFang) {
			titleText.setText(this.getResources().getString(R.string.wanshanxinxi));
			re_layout.setVisibility(View.GONE);
			userName = getIntent().getStringExtra("userName");
		} else {
			titleText.setText(this.getResources().getString(R.string.zhuce));
			re_layout.setVisibility(View.VISIBLE);
		}

		leftLayout.setVisibility(View.VISIBLE);
		leftLayout.setOnClickListener(this);
		rightLayout.setVisibility(View.GONE);
		
		next.setOnClickListener(this);
		// 邮箱输入监听
		edt_phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String phone = edt_phone.getText().toString();

				ri_tv_phone.setTextColor(Color.BLACK);
				if (phone.matches(regex)) {
					isphone = true;
				} else {
					isphone = false;
				}
				if (isphone == true && isy_z_m == true && isBdy_z_m == true) {
					next.setBackgroundResource(R.drawable.button_bg);
					next.setClickable(true);
				} else {
					next.setBackgroundResource(R.drawable.button_bg_false);
					next.setClickable(false);
				}
				
			}


			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		// 验证码输入监听
		edt_y_z_m.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String y_z_m = edt_y_z_m.getText().toString();
				if (y_z_m.matches(miMa)) {
					isy_z_m = true;
				} else {
					isy_z_m = false;
				}
				if (isphone == true && isy_z_m == true && isBdy_z_m == true) {
					next.setBackgroundResource(R.drawable.button_bg);
					next.setClickable(true);
				} else {
					next.setBackgroundResource(R.drawable.button_bg_false);
					next.setClickable(false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		//本地图片验证码校验
				bd_y_z_m.addTextChangedListener(new TextWatcher() {
					

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						// TODO Auto-generated method stub
						
						String str= bd_y_z_m.getText().toString();
						if(code.equalsIgnoreCase(str)){
							isBdy_z_m  = true;
						}else{
							isBdy_z_m = false;
						}
						if (isphone == true && isy_z_m == true && isBdy_z_m == true) {
							next.setBackgroundResource(R.drawable.button_bg);
							next.setClickable(true);
						} else {
							next.setBackgroundResource(R.drawable.button_bg_false);
							next.setClickable(false);
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
/*
	isTongYi = getIntent().getBooleanExtra("TongYi", true);
	String phone = getIntent().getStringExtra("phone");
	String verificationCode = getIntent()
			.getStringExtra("verificationCode");*/
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//isTongYi = data.getBooleanExtra("TongYi", true);
		switch (resultCode) {
		case 1:
			
			isTongYi = data.getExtras().getBoolean("TongYi");
			if (isTongYi) {
				rememberPassword.setChecked(true);
			} else {
				rememberPassword.setChecked(false);
			}
			break;
		case 2:
			
			isTongYi = data.getExtras().getBoolean("TongYi");
			if (isTongYi) {
				rememberPassword.setChecked(true);
			} else {
				rememberPassword.setChecked(false);
			}
			break;
	
		default:
			break;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_showCode:
			iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
			code = Code.getInstance().getCode();
			break;
		case R.id.search_leftLayout:
			finish();
			break;
		
		case R.id.ri_next:
			// 3.判断注册协议已阅读是否勾选
			boolean flag = rememberPassword.isChecked();
			if (flag == false) {
				prompt(this.getResources().getString(R.string.qingyueduzhucexieyi));
				return;
			}
			dialog = new ProgressDialog(RegisterActivity.this);
			dialog.setCancelable(false);
			dialog.setMessage(RegisterActivity.this.getResources().getString(R.string.zhengzailianjieqingshaohou));
			dialog.show();
			String url = Urls.YZ_MAIL;
			AjaxParams params = new AjaxParams();
			params.put("usermail", edt_phone.getText().toString());
				fh.post(url, params,new AjaxCallBack() {
					@Override
					public void onFailure(Throwable t, int errorNo, String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						prompt(RegisterActivity.this.getResources().getString(R.string.wangluolianjieshibai));
					}
					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						String content = (String) t;
						System.out.println(content);
						ResponseCommon bean = gson.fromJson(content, ResponseCommon.class);
						int status = bean.getStatus();
						if(status == 0){
							dialog.dismiss();
							//用户存在
							prompt(RegisterActivity.this.getResources().getString(R.string.yonghucunzai));
							
						}else{
							Intent intent = new Intent(RegisterActivity.this,RegisterPersonalDataActivity.class);
							intent.putExtra("usermail", edt_phone.getText().toString());
							intent.putExtra("pwd", edt_y_z_m.getText().toString());
							intent.putExtra("verifyCode", bd_y_z_m.getText().toString());
							startActivity(intent);
							dialog.dismiss();
						}
					}
				});
				
				break;
			
		case R.id.register_message:
			startActivityForResult((new Intent(RegisterActivity.this,
					RegistrationAgreementActivity.class).putExtra("phone",
					edt_phone.getText().toString()).putExtra(
					"verificationCode", edt_y_z_m.getText().toString())),0);
			break;
		default:
			break;
		}
		
	}

}
