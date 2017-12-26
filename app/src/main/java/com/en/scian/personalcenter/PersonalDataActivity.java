package com.en.scian.personalcenter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ab.image.AbImageLoader;
import com.en.scian.BaseActivity;
import com.en.scian.ExampleApplication;
import com.en.scian.R;
import com.en.scian.entity.PictureBean;
import com.en.scian.entity.ResponseCommon;
import com.en.scian.entity.UserCommonBean;
import com.en.scian.entity.UserInfo;
import com.en.scian.login.LoginActivity;
import com.en.scian.network.FilePath;
import com.en.scian.network.Urls;
import com.en.scian.util.DisplayUtils;
import com.en.scian.util.MyUtil;
import com.en.scian.util.SettingUtils;
import com.en.scian.util.ToastUtils;
import com.en.scian.view.CustomDialog;
import com.en.scian.view.CustomRuler;
import com.en.scian.view.CustomDialog.Builder;
import com.en.scian.view.CustomRuler.OnValueChangeListener;
import com.en.scian.widget.NumericWheelAdapter;
import com.en.scian.widget.OnWheelChangedListener;
import com.en.scian.widget.WheelView;
import com.google.gson.Gson;

/**
 * 个人资料
 * 
 * @author jiyx
 * 
 */
public class PersonalDataActivity extends BaseActivity implements
		OnClickListener, OnCheckedChangeListener {
	/* 头像文件 */
	private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";

	/* 请求识别码 */
	private static final int CODE_GALLERY_REQUEST = 0xa0;
	private static final int CODE_CAMERA_REQUEST = 0xa1;
	private static final int CODE_CUTTING_REQUEST = 0xa2;

	private TextView title;
	private LinearLayout back;
	private TextView save;

	private RelativeLayout personalcenter_personaldata_sethead1;
	private ImageView personalcenter_personaldata_sethead2;
	private RelativeLayout personalcenter_personaldata_setpwd;
	private LinearLayout person_layout;

	/**
	 * 时间控件
	 */
	private DateNumericAdapter monthAdapter, dayAdapter, yearAdapter;
	private WheelView year, month, day;
	private int mCurYear = 120, mCurMonth = 5, mCurDay = 14;

	// 缩略图文件路径
	File fileTemp;
	private Gson gson = new Gson();
	/**
	 * 图片的网络路径
	 */
	private String picture;
	/**
	 * 姓名
	 */
	private EditText person_name;
	/**
	 * 性别
	 */
	private ToggleButton person_sex;
	private int sex = 1;
	/**
	 * 生日
	 */
	private TextView person_time;
	/**
	 * 身高
	 */
	private TextView person_height;
	private CustomRuler person_cr_height;
	/**
	 * 体重
	 */
	private TextView person_weight;
	private CustomRuler person_cr_weight;
	private FinalHttp fh;


	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personalcenter_personaldata);
		initViews();
		initListener();
		setData();
		setContext(this);
		setIsResult(true);
		setIsHuaDong(false);
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		fh = new FinalHttp();
		fh.configTimeout(15 * 1000);
		gson = new Gson();

		ExampleApplication.getInstance().addActivity(this);
		title = (TextView) findViewById(R.id.search_titleText);
		back = (LinearLayout) findViewById(R.id.search_leftLayout);
		save = (TextView) findViewById(R.id.search_right_txtView);
		save.setVisibility(View.VISIBLE);

		personalcenter_personaldata_sethead1 = (RelativeLayout) findViewById(R.id.personalcenter_personaldata_sethead1);
		personalcenter_personaldata_sethead2 = (ImageView) findViewById(R.id.personalcenter_personaldata_sethead2);
		//personalcenter_personaldata_setpwd = (RelativeLayout) findViewById(R.id.personalcenter_personaldata_setpwd);

		person_layout = (LinearLayout) this.findViewById(R.id.person_layout);
		person_time = (TextView) this.findViewById(R.id.person_time);
		person_name = (EditText) this.findViewById(R.id.person_name);
		person_sex = (ToggleButton) this.findViewById(R.id.person_sex);

		person_height = (TextView) this.findViewById(R.id.person_height);
		person_weight = (TextView) this.findViewById(R.id.person_weight);
		person_cr_height = (CustomRuler) findViewById(R.id.person_cr_height);
		person_cr_weight = (CustomRuler) findViewById(R.id.person_cr_weight);
	}

	/**
	 * 设置初始化数据
	 */
	private void setData() {
		title.setText(getResources().getString(
				R.string.home_left_menu_personalinfo_text));
		save.setText(getResources().getString(R.string.personaldata_save));
		String userPic = SettingUtils.get(PersonalDataActivity.this, "userPic",
				"");
		picture = userPic;
		AbImageLoader loader = new AbImageLoader(PersonalDataActivity.this);
		loader.display(personalcenter_personaldata_sethead2, userPic);
		String realName = SettingUtils.get(PersonalDataActivity.this,
				"realName", "");
		person_name.setText(realName);
		String sex = SettingUtils.get(PersonalDataActivity.this, "sex", "");
		if (sex.equals("1")) {
			person_sex.setChecked(true);
		} else {
			person_sex.setChecked(false);
		}
		String birthday = SettingUtils.get(PersonalDataActivity.this,
				"birthday", "");
		String height = SettingUtils.get(PersonalDataActivity.this, "height",
				"");
		String weight = SettingUtils.get(PersonalDataActivity.this, "weight",
				"");
		person_time.setText(birthday);
		// 设置身高、体重刻度尺值
		if (!TextUtils.isEmpty(height)) {
			person_height.setText(height);
			person_cr_height.initViewParam(Float.valueOf(height), 300,
					CustomRuler.MOD_TYPE_ONE);
		} else {
			person_height.setText("0.0");
			person_cr_height.initViewParam(0f, 300, CustomRuler.MOD_TYPE_ONE);
		}
		if (!TextUtils.isEmpty(weight)) {
			person_weight.setText(weight);
			person_cr_weight.initViewParam(Float.valueOf(weight), 300,
					CustomRuler.MOD_TYPE_ONE);
		} else {
			person_weight.setText("0.0");
			person_cr_weight.initViewParam(0f, 300, CustomRuler.MOD_TYPE_ONE);
		}
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		back.setOnClickListener(this);
		save.setOnClickListener(this);
		person_layout.setOnClickListener(this);
		person_sex.setOnCheckedChangeListener(this);

		personalcenter_personaldata_sethead1.setOnClickListener(this);
		personalcenter_personaldata_sethead2.setOnClickListener(this);
		//personalcenter_personaldata_setpwd.setOnClickListener(this);

		person_cr_height.setValueChangeListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(float value) {
				person_height.setText(value / 10 + "");
			}
		});
		person_cr_weight.setValueChangeListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(float value) {
				person_weight.setText(value / 10 + "");
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_leftLayout:// 返回
			finishPage();
			break;
		case R.id.search_right_txtView:// 保存用户信息
			boolean flag = getYanZheng();
			if (flag) {
				String userId = SettingUtils.get(PersonalDataActivity.this,
						"userId", "");
				String URL = Urls.UPDATE_USERINFO;
				AjaxParams params = new AjaxParams();
				params.put("userId", userId);
				params.put("realName", person_name.getText().toString());
				params.put("userPic", picture);
				params.put("sex", String.valueOf(sex));
				params.put("birthday", person_time.getText().toString());
				params.put("height", person_height.getText().toString());
				params.put("weight", person_weight.getText().toString());
				dialog = new ProgressDialog(PersonalDataActivity.this);
				dialog.setCancelable(false);
				dialog.setMessage(PersonalDataActivity.this.getResources().getString(R.string.zhengzailianjieqingshaohou));
				dialog.show();
				updateUser(URL, params);
			}
			break;
		case R.id.personalcenter_personaldata_sethead1:// 设置头像1
			showSelectPicDialog();
			break;
		case R.id.personalcenter_personaldata_sethead2:// 设置头像2
			showSelectPicDialog();
			break;
		/*case R.id.personalcenter_personaldata_setpwd:// 修改密码
			startActivity(new Intent(this, UpdatePWDActivity.class));
			break;*/
		case R.id.person_layout:
			getTimePicker2();
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		// 用户没有进行有效的设置操作，返回
		if (resultCode == RESULT_CANCELED) {
			Toast.makeText(getApplication(), this.getResources().getString(R.string.ninquxiaolecaozuo), Toast.LENGTH_LONG)
					.show();
			return;
		}

		switch (requestCode) {
		case CODE_GALLERY_REQUEST:// 选图
			cropRawPhoto(intent.getData());
			break;

		case CODE_CAMERA_REQUEST:// 拍照
			if (hasSdcard()) {
				File tempFile = new File(
						Environment.getExternalStorageDirectory(),
						IMAGE_FILE_NAME);
				cropRawPhoto(Uri.fromFile(tempFile));
			} else {
				Toast.makeText(getApplication(), this.getResources().getString(R.string.meiyousdcard), Toast.LENGTH_LONG)
						.show();
			}
			break;

		case CODE_CUTTING_REQUEST:// 设置图片
			if (intent != null) {
				setImageToHeadView(intent);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, intent);
	}

	/**
	 * TODO 选择图片对话框
	 */
	private void showSelectPicDialog() {
		final AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.show();
		// 设置dialog布局
		Window window = dlg.getWindow();
		window.setContentView(R.layout.dialog_select_pic);
		// 设置dialog宽度
		WindowManager.LayoutParams lp = dlg.getWindow().getAttributes();
		lp.width = getWH("w") - DisplayUtils.dip2px(this, 20);// 定义宽度
		dlg.getWindow().setAttributes(lp);

		Button dialog_sethead__camera = (Button) window
				.findViewById(R.id.dialog_sethead__camera);
		Button dialog_sethead_selectpic = (Button) window
				.findViewById(R.id.dialog_sethead_selectpic);
		Button dialog_sethead_cancel = (Button) window
				.findViewById(R.id.dialog_sethead_cancel);
		// 拍照
		dialog_sethead__camera.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				choseHeadImageFromCameraCapture();
				dlg.cancel();
			}
		});
		// 从相册选择图片
		dialog_sethead_selectpic.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				choseHeadImageFromGallery();
				dlg.cancel();
			}
		});
		// 取消
		dialog_sethead_cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dlg.cancel();
			}
		});
	}

	/**
	 * 启动手机相机拍摄照片作为头像
	 */
	private void choseHeadImageFromCameraCapture() {
		Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 判断存储卡是否可用，存储照片文件
		if (hasSdcard()) {
			intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
					.fromFile(new File(Environment
							.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
			startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
		}
	}

	/**
	 * 从本地相册选取图片作为头像
	 */
	private void choseHeadImageFromGallery() {
		Intent intentFromGallery = new Intent();
		// 设置文件类型
		intentFromGallery.setType("image/*");
		intentFromGallery.setAction(Intent.ACTION_PICK);
		startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
	}

	/**
	 * 裁剪原始的图片
	 */
	public void cropRawPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");

		// 设置裁剪
		intent.putExtra("crop", "true");

		// aspectX , aspectY :宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// outputX , outputY : 裁剪图片宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);

		startActivityForResult(intent, CODE_CUTTING_REQUEST);
	}

	/**
	 * 提取保存裁剪之后的图片数据，并设置头像部分的View
	 */
	@SuppressLint("SimpleDateFormat")
	private void setImageToHeadView(Intent intent) {
		Bundle extras = intent.getExtras();

		MyUtil.createFile(FilePath.USER_ICON, PersonalDataActivity.this);
		/**
		 * 将图片存成本地文件
		 */
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");

			// 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上 传到服务器，QQ头像上传采用的方法跟这个类似
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
			byte[] b = stream.toByteArray(); // 将图片流以字符串形式存储下来

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			// 例如：cc_time=1291778220
			long lcc_time = System.currentTimeMillis();
			String currentTime = sdf.format(new Date(lcc_time));

			fileTemp = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath(), "/ludePic/" + currentTime + ".jpg");
			try {
				if (!fileTemp.exists()) {
					fileTemp.createNewFile();
				}
				FileOutputStream fileOutputStream = new FileOutputStream(
						fileTemp);
				fileOutputStream.write(b);
				fileOutputStream.flush();
				fileOutputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// TODO 上传头像
			upLoadPic(fileTemp);

			// if (extras != null) {
			// Bitmap photo = extras.getParcelable("data");
			// personalcenter_personaldata_sethead2.setImageBitmap(photo);
			// }

		}
	}

	/**
	 * 检查设备是否存在SDCard的工具方法
	 */
	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			// 有存储的SDCard
			return true;
		} else {
			return false;
		}
	}

	/**
	 * TODO 获取时间控件
	 */
	@SuppressLint("InflateParams")
	private void getTimePicker2() {
		View view = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.personalcenter_time_picker_year, null);
		year = (WheelView) view.findViewById(R.id.year);
		month = (WheelView) view.findViewById(R.id.month);
		day = (WheelView) view.findViewById(R.id.day);
		CustomDialog.Builder builder = new Builder(PersonalDataActivity.this);
		builder.isBottom(true, getWindowManager(), getWindow());
		builder.setContentView(view);
		builder.setTitle(this.getResources().getString(R.string.xuanqushijian));
		Calendar calendar = Calendar.getInstance();
		OnWheelChangedListener listener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateDays(year, month, day);
			}
		};
		int curYear = calendar.get(Calendar.YEAR);
		String birthday = person_time.getText().toString();
		if (birthday != null && birthday.contains("-")) {
			String str[] = birthday.split("-");
			mCurYear = 120 - (curYear - Integer.parseInt(str[0]));
			mCurMonth = Integer.parseInt(str[1]) - 1;
			mCurDay = Integer.parseInt(str[2]) - 1;
		}
		yearAdapter = new DateNumericAdapter(this, curYear - 120,
				curYear + 100, 100);
		year.setViewAdapter(yearAdapter);
		year.setCurrentItem(mCurYear);
		year.addChangingListener(listener);

		monthAdapter = new DateNumericAdapter(this, 1, 12, 5);
		month.setViewAdapter(monthAdapter);
		month.setCurrentItem(mCurMonth);
		month.addChangingListener(listener);

		// 设置日期
		updateDays(year, month, day);
		day.setCurrentItem(mCurDay);
		day.addChangingListener(listener);
		builder.setPositiveButton(this.getResources().getString(R.string.queding),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Calendar calendar = Calendar.getInstance();
						int years = calendar.get(Calendar.YEAR)
								- (120 - year.getCurrentItem());
						int m = month.getCurrentItem() + 1;
						int d = day.getCurrentItem() + 1;
						person_time.setText(years + "-"
								+ (m < 10 ? ("0" + m) : m) + "-"
								+ (d < 10 ? ("0" + d) : d));
						dialog.cancel();
					}
				});

		builder.create().show();

	}

	/**
	 * 图片上传
	 * 
	 * @param file
	 */
	private void upLoadPic(File file) {
		// 图片上传
		AjaxParams params = new AjaxParams();
		try {
			params.put("fileData", file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		FinalHttp fh = new FinalHttp();
		fh.post(Urls.UPLOAD_IMAGE, params, new AjaxCallBack<Object>() {
			@Override
			public void onSuccess(Object t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				ResponseCommon common = gson.fromJson((String) t,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					prompt(PersonalDataActivity.this.getResources().getString(R.string.tupianshangchuanshibai));
					return;
				}
				// TODO 将新的路径设置到SettingUtils中
				PictureBean bean = gson.fromJson((String) t, PictureBean.class);
				picture = bean.getPicture();
				SettingUtils.set(PersonalDataActivity.this, "userPic", picture);
				AbImageLoader loader = new AbImageLoader(
						PersonalDataActivity.this);
				loader.display(personalcenter_personaldata_sethead2, picture);
			}
		});
	}

	/**
	 * 保存验证
	 * 
	 * @return
	 */
	private boolean getYanZheng() {
		// 姓名验证
		if (person_name.getText().toString() == null
				|| person_name.getText().toString().equals("")) {
			prompt(this.getResources().getString(R.string.ninhaimeiyoutianxiexingming));
			return false;
		}
		// 生日验证
		if (person_time.getText().toString() == null
				|| person_time.getText().toString().equals(this.getResources().getString(R.string.personaldata_chose_birthday))) {
			prompt(this.getResources().getString(R.string.ninhaimeiyoutianxieshengri));
			return false;
		}
		// 身高验证
		if (person_height.getText().toString() == null
				|| person_height.getText().toString().equals("")) {
			prompt(this.getResources().getString(R.string.ninhaimeiyouxuanzeshengao));
			return false;
		}
		// 体重验证
		if (person_weight.getText().toString() == null
				|| person_weight.getText().toString().equals("")) {
			prompt(this.getResources().getString(R.string.ninhaimeiyouxuanzetizhong));
			return false;
		}
		return true;

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			sex = 1;
		} else {
			sex = 2;
		}
	}

	/**
	 * 用户资料更新
	 * 
	 * @param URL
	 * @param params
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void updateUser(String URL, AjaxParams params) {
		fh.post(URL, params, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				dialog.dismiss();
				ToastUtils.TextToast(
						PersonalDataActivity.this,
						PersonalDataActivity.this.getResources().getString(
								R.string.fuwuqilianjieyichang));
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				String content = (String) t;
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					prompt(common.getMsg());
					dialog.dismiss();
					return;
				}
				UserCommonBean bean = gson.fromJson(content,
						UserCommonBean.class);
				UserInfo info = bean.getData();
				int userID = info.getUserId();
				String realName = info.getRealName();
				int userSex = info.getSex();
				int userType2 = info.getUserType();
				String phone = info.getPhone();
				String birthday = info.getBirthday();
				float height = info.getHeight();
				float weight = info.getWeight();
				// 设置身高、体重刻度尺值
				person_cr_height.initViewParam(height, 300,
						CustomRuler.MOD_TYPE_ONE);
				person_cr_weight.initViewParam(weight, 300,
						CustomRuler.MOD_TYPE_ONE);
				person_height.setText(height + "");
				person_weight.setText(weight + "");

				SettingUtils.set(PersonalDataActivity.this, "userId",
						String.valueOf(userID));
				SettingUtils.set(PersonalDataActivity.this, "realName",
						realName);
				SettingUtils.set(PersonalDataActivity.this, "sex",
						String.valueOf(userSex));
				SettingUtils.set(PersonalDataActivity.this, "userType",
						String.valueOf(userType2));
				// TODO
				/*SettingUtils.set(
						PersonalDataActivity.this,
						"phone",
						String.valueOf(ExampleApplication.getInstance()
								.getUser().getPhone()));*/
				SettingUtils.set(PersonalDataActivity.this, "birthday",
						birthday);
				SettingUtils.set(PersonalDataActivity.this, "height", height
						+ "");
				SettingUtils.set(PersonalDataActivity.this, "weight", weight
						+ "");
				SettingUtils.set(PersonalDataActivity.this, "userPic", picture);
				SettingUtils.set(PersonalDataActivity.this, "isPush",
						info.getIsPush());
				dialog.dismiss();
				prompt(PersonalDataActivity.this.getResources().getString(R.string.yonghuziliaoxiugaichenggong));
				finishPage();
			}
		});
	}

	/**
	 * 数字轮适配器。突出当前值。 // TODO
	 */
	private class DateNumericAdapter extends NumericWheelAdapter {

		public DateNumericAdapter(Context context, int minValue, int maxValue,
				int current) {
			super(context, minValue, maxValue);
			setTextSize(24);
		}

		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			view.setTypeface(Typeface.SANS_SERIF);
		}

		public CharSequence getItemText(int index) {
			return super.getItemText(index);
		}
	}

	/**
	 * 获取时间控件时间 // TODO
	 * 
	 * @param year
	 * @param month
	 * @param day
	 */
	private void updateDays(WheelView year, WheelView month, WheelView day) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR,
				calendar.get(Calendar.YEAR) + year.getCurrentItem());
		calendar.set(Calendar.MONTH, month.getCurrentItem());

		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		dayAdapter = new DateNumericAdapter(this, 1, maxDays,
				calendar.get(Calendar.DAY_OF_MONTH) - 1);
		day.setViewAdapter(dayAdapter);
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
		save = null;
		personalcenter_personaldata_sethead1 = null;
		personalcenter_personaldata_sethead2 = null;
		//personalcenter_personaldata_setpwd = null;
		person_layout = null;

		monthAdapter = null;
		dayAdapter = null;
		yearAdapter = null;
		year = null;
		month = null;
		day = null;
		fileTemp = null;
		gson = null;
		picture = null;
		person_name = null;
		person_sex = null;
		person_time = null;

		person_height = null;
		person_cr_height = null;
		person_cr_weight = null;
		fh = null;
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}

	public void finishPage() {
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		setResult(999);
		finish();
	}
}
