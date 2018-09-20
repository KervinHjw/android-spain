package com.ludees.scian.xueya.fragment;

import java.io.InputStream;

import org.apache.http.Header;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
import com.ludees.scian.R;
import com.ludees.scian.entity.BloodPressureResult;
import com.ludees.scian.entity.ResponseCommon;
import com.ludees.scian.network.Urls;
import com.ludees.scian.util.BlueToothUtil;
import com.ludees.scian.util.SettingUtils;
import com.ludees.scian.xueya.XunYiActivity;
import com.google.gson.Gson;

/**
 * 血压测量数据
 * 
 * @author zhangp
 * 
 */
@SuppressLint("HandlerLeak")
public class XueYaDataFragment extends Fragment implements OnClickListener {
	private Context mContext;
	private LinearLayout xueya_data_view;
	private TranslateAnimation translateAnimation;
	/**
	 * 建议框
	 */
	private RelativeLayout xueya_data_rl;
	private TextView xueya_data_suggestion;
	private String str = "";
	View view;
	/**
	 * 网络获取
	 */
	private AbHttpUtil mAbHttpUtil = null;
	private Gson gson;
	private String URL = "";
	private String URL1 = "";
	private String userId;
	private ProgressDialog pd;
	private int bloodPressureId;
	private TextView xueya_data_xueya;
	private TextView xueya_data_maibo;
	private Button xueya_data_restart;
	private BlueToothUtil util;

	private Thread dialogAndTextThread;
	private TextView xueya_data_diya;
	private int gaoya = 0;
	private int diya = 0;
	private int maibo = 0;
	private Thread th;
	private RelativeLayout xunyi;
	private FinalHttp fh;
	private ImageView xueya_data_bg;
	private Bitmap btp;
	/**
	 * 获取蓝牙adapter
	 */
	private BluetoothAdapter bluetoothAdapter;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				int n = msg.arg1;
				if (xueya_data_suggestion != null) {
					xueya_data_suggestion.setText(str.substring(0, (n + 1)));
				}
				break;
			case 1:
				if (xueya_data_xueya != null) {
					xueya_data_xueya.setText(String.valueOf(gaoya));
				}
				if (xueya_data_diya != null) {
					xueya_data_diya.setText(String.valueOf(diya));
				}
				if (xueya_data_maibo != null) {
					xueya_data_maibo.setText(String.valueOf(maibo));
				}
				break;
			default:
				break;
			}
		}
	};
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		fh = new FinalHttp();
		fh.configTimeout(15000);
		mContext = getActivity();
		gson = new Gson();
		// 获取Http工具类
		mAbHttpUtil = AbHttpUtil.getInstance(getActivity());
		mAbHttpUtil.setTimeout(10000);
		view = inflater.inflate(R.layout.xueya_data_fragment, null);
		initView(view);
		userId = SettingUtils.get(getActivity(), "userId", "");
		bloodPressureId = getActivity().getIntent().getIntExtra(
				"bloodPressureId", 0);
		if (bloodPressureId != 0) {
			URL = Urls.GET_BLOOD_PRESSURE + "userId=" + userId
					+ "&bloodPressureId=" + String.valueOf(bloodPressureId)+ "&language="+getString(R.string.language);
		} else {
			URL = Urls.GET_BLOOD_PRESSURE + "userId=" + userId+"&language="+getString(R.string.language);
		}
		return view;
	}

	private void initView(View view) {
		xueya_data_view = (LinearLayout) view
				.findViewById(R.id.xueya_data_view);
		xueya_data_rl = (RelativeLayout) view.findViewById(R.id.xueya_data_rl2);
		xueya_data_suggestion = (TextView) view
				.findViewById(R.id.xueya_data_suggestion);
		xueya_data_xueya = (TextView) view.findViewById(R.id.xueya_data_xueya);
		xueya_data_maibo = (TextView) view.findViewById(R.id.xueya_data_maibo);
		xueya_data_restart = (Button) view
				.findViewById(R.id.xueya_data_restart);
		xueya_data_diya = (TextView) view.findViewById(R.id.xueya_data_diya);
		xunyi = (RelativeLayout) view.findViewById(R.id.xunyi);
		xueya_data_bg = (ImageView) view.findViewById(R.id.xueya_data_bg);
		InputStream is = getActivity().getResources().openRawResource(
				R.drawable.xueya_result_bg);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = 2;
		btp = BitmapFactory.decodeStream(is, null, options);
		xueya_data_bg.setScaleType(ScaleType.FIT_XY);
		xueya_data_bg.setImageBitmap(btp);
		xunyi.setOnClickListener(this);
		xueya_data_restart.setOnClickListener(this);
		userId = SettingUtils.get(mContext, "userId", "");

		bloodPressureId = getActivity().getIntent().getIntExtra(
				"bloodPressureId", 0);
	}

	private Header[] headers;
	
	/**
	 * TODO 联网获取数据
	 * 
	 * @param URL
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setData(String URL) {
		//fh.addHeader("language", "English");
		fh.get(URL, new AjaxCallBack() {
			private int max;
			private int i;

			@Override
			public void onSuccess(Object t) {
				// flag = false;
				String content = (String) t;
				SettingUtils.set(getActivity(), "isFirstIn", "2");
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() == 0) {
					Toast.makeText(mContext, common.getMsg(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				final BloodPressureResult result = gson.fromJson(content,
						BloodPressureResult.class);
				str = result.getData().getBloodPressure().getMeasureResultDesc();
				// xueya_data_xueya.setText(String.valueOf(result.getData()
				// .getBloodPressure().getBloodPressureClose()));
				// xueya_data_diya.setText(String.valueOf(result.getData()
				// .getBloodPressure().getBloodPressureOpen()));
				// xueya_data_maibo.setText(String.valueOf(result.getData()
				// .getBloodPressure().getPulse()));
				// getSuggesstion();
				
				max = getMax(result.getData().getBloodPressure().getBloodPressureOpen(), 
						result.getData().getBloodPressure().getBloodPressureClose(), 
						result.getData().getBloodPressure().getPulse());
				i = 0;
				
				th = new Thread(new Runnable() {

					public void run() {
						
						while (i<max) {
							if(gaoya < result.getData().getBloodPressure().
									getBloodPressureClose()){
								gaoya++;
							}
							if (diya < result.getData().getBloodPressure()
									.getBloodPressureOpen()) {
								diya++;
							}
							if (maibo < result.getData().getBloodPressure()
									.getPulse()) {
								maibo++;
							}
							if (handler != null) {
								handler.sendEmptyMessage(1);
								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							} else {
								return;
							}

						}

					}
				});
				th.start();

			}
		});
	}

	//取最大值
	public int getMax(int a,int b,int c){
		if(a>b&&a>c){
			return a;
		}else if(c>a&&c>b){
			return c;
		}else{
			return b;
		}
	}
	
	
	
	
	@Override
	public void onResume() {
		super.onResume();
		String num = SettingUtils.get(getActivity(), "isFirstIn", "1");
		if (num.equals("1")) {
			pd = new ProgressDialog(getActivity());
			pd.setMessage(getResources().getString(R.string.zhengzaijiazaiqingshaohou));
			pd.show();
			setData(URL);
			getSuggesstion();
		} else {
			xueya_data_xueya.setText(String.valueOf(gaoya));
			xueya_data_diya.setText(String.valueOf(diya));
			xueya_data_maibo.setText(String.valueOf(maibo));
			xueya_data_view.setVisibility(View.VISIBLE);
			xueya_data_rl.setVisibility(View.VISIBLE);
			xueya_data_suggestion.setText(str);
		}

	}

	@SuppressWarnings("static-access")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.xueya_data_restart:
			SettingUtils.set(getActivity(), "isFirstIn", "1");
			getBlueTooth();
			break;
		case R.id.xunyi:
			/*String phone = SettingUtils.get(getActivity(), "phone", "");
			TelephonyManager tm = (TelephonyManager) getActivity()
					.getSystemService(getActivity().TELEPHONY_SERVICE);
			String blood = String.valueOf(bloodPressureId);
			String app_data = tm.getDeviceId();
			String XunYi = Urls.XUNYI_LOGIN + "telephoe=" + phone
					+ "&bloodPressureId=" + blood + "&app_data=" + app_data;
			getXunYi(XunYi);*/
			break;
		default:
			break;
		}
	}

	private void getSuggesstion() {
		xueya_data_view.setVisibility(View.VISIBLE);
		translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				-1f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f);
		translateAnimation.setRepeatCount(0);
		translateAnimation.setDuration(2000);
		xueya_data_view.startAnimation(translateAnimation);
		translateAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				xueya_data_rl.setVisibility(View.INVISIBLE);
				xueya_data_suggestion.setText("");
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				shouDialogAndText();
			}
		});
	}

	/**
	 * 显示提示对话框和文本
	 */
	private void shouDialogAndText() {
		xueya_data_rl.setVisibility(View.VISIBLE);
		Animation aa = new AlphaAnimation(0f, 1.0f);
		aa.setDuration(1000);
		xueya_data_rl.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				dialogAndTextThread = new Thread(new Runnable() {

					@Override
					public void run() {
						for (int i = 0; !TextUtils.isEmpty(str)
								&& i < str.length(); i++) {
							Message msg = handler.obtainMessage();
							msg.what = 0;
							msg.arg1 = i;
							try {
								if (handler != null) {
									handler.sendMessage(msg);
									Thread.sleep(100);
									if (!TextUtils.isEmpty(str)) {
										if (i == str.length() - 1) {
											dialogAndTextThread = null;
										}
									}
								}
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				});
				dialogAndTextThread.start();
				pd.dismiss();
			}
		});
	}

	/**
	 * 血压数据获取
	 */
	private void getXunYi(String URL1) {
		mAbHttpUtil.get(URL1, new AbStringHttpResponseListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onFinish() {

			}

			@Override
			public void onFailure(int arg0, String arg1, Throwable arg2) {

			}

			@Override
			public void onSuccess(int status, String content) {
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					Toast.makeText(getActivity(), common.getMsg(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent = new Intent(getActivity(), XunYiActivity.class);
				intent.putExtra("url", common.getMsg());
				startActivity(intent);
			}
		});
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		mContext = null;
		xueya_data_view = null;
		translateAnimation = null;
		bluetoothAdapter = null;
		xueya_data_rl = null;
		xueya_data_suggestion = null;
		str = null;
		view = null;
		mAbHttpUtil = null;
		gson = null;
		URL = null;
		userId = null;
		pd = null;
		bloodPressureId = 0;
		xueya_data_xueya = null;
		xueya_data_maibo = null;
		xueya_data_restart = null;
		util = null;
		xueya_data_diya = null;
		gaoya = 0;
		diya = 0;
		maibo = 0;
		xunyi = null;
		fh = null;
		xueya_data_bg = null;
		btp = null;
		handler = null;
		dialogAndTextThread = null;
		th = null;
		System.gc();
		super.onDestroyView();
	}
	
	
	private void getBlueTooth(){
		// 检查设备是否支持蓝牙
				bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
				if (!bluetoothAdapter.isEnabled()) {
					Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					// 设置蓝牙可见性，最多300秒
					intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
					getActivity().startActivityForResult(intent, 111);
				} else {
					if(util == null){
						util = new BlueToothUtil(getActivity());
					}
					util.setIsRefuse(true);
					util.setBoolean(true);
					System.out.println("*********改變后的值"+util.getZzhi());
					util.setActivity(getActivity());
					util.setBlueTooth();
				}
	}

}
