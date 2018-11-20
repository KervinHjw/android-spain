package com.en.scian.xueya.fragment;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.en.scian.R;
import com.en.scian.entity.BloodPressureResult;
import com.en.scian.entity.ResponseCommon;
import com.en.scian.network.Urls;
import com.en.scian.util.DisplayUtils;
import com.en.scian.util.SettingUtils;
import com.google.gson.Gson;

/**
 * 血压测量结果图表
 * 
 * @author zhangp
 * 
 */
public class XueYaChartFragment extends Fragment {
	private Context mContext;
	/**
	 * 高压
	 */
	private ImageView xueya_chart_right;
	/**
	 * 低压
	 */
	private ImageView xueya_chart_bottom;
	/**
	 * 实际值
	 */
	private ImageView xueya_chart_center;
	/**
	 * 健康建议
	 */
	private TextView xueya_chart_suggestion;
	/**
	 * 网络获取
	 */
	private Gson gson;
	private String URL = "";
	private String userId;
	private ProgressDialog pd;
	private int bloodPressureId;
	private String str;
	private boolean isFirstIn = true;
	private int x = 0, y = 0;
	private FinalHttp fh;
	private View view;
	private ImageView imageView;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		fh = new FinalHttp();
		fh.configTimeout(15000);
		mContext = getActivity();
		view = inflater.inflate(R.layout.xueya_chart_fragment, null);
		initView(view);

		if (isFirstIn) {
			pd = new ProgressDialog(getActivity());
			pd.setMessage(getResources().getString(R.string.zhengzaijiazaiqingshaohou));
			userId = SettingUtils.get(getActivity(), "userId", "");
			bloodPressureId = getActivity().getIntent().getIntExtra(
					"bloodPressureId", 0);
			if (bloodPressureId != 0) {
				URL = Urls.GET_BLOOD_PRESSURE + "userId=" + userId
						+ "&bloodPressureId=" + String.valueOf(bloodPressureId)+ "&language=English";
			} else {
				URL = Urls.GET_BLOOD_PRESSURE + "userId=" + userId+ "&language=English";
			}
			setData(URL);
			getSuggesstion();
		} else {
			xueya_chart_view.setVisibility(View.VISIBLE);
			xueya_chart_rl.setVisibility(View.VISIBLE);
			xueya_chart_suggestion.setText(str);
			getDongTaiTu(x, y, 0);
		}
		return view;
	}

	private void initView(View view) {
		gson = new Gson();
		xueya_chart_right = (ImageView) view
				.findViewById(R.id.xueya_chart_right);
		xueya_chart_bottom = (ImageView) view
				.findViewById(R.id.xueya_chart_bottom);
		xueya_chart_center = (ImageView) view
				.findViewById(R.id.xueya_chart_center);
		xueya_chart_suggestion = (TextView) view
				.findViewById(R.id.xueya_chart_suggestion);
		userId = SettingUtils.get(mContext, "userId", "");
		bloodPressureId = getActivity().getIntent().getIntExtra(
				"bloodPressureId", 0);
		imageView = (ImageView) view.findViewById(R.id.color_img);
		if(!SettingUtils.get(mContext,"kpa",false)){
			imageView.setBackgroundResource(R.drawable.xueya_colour710);
		}else{
			imageView.setBackgroundResource(R.drawable.xueya_colour720);
		}
		xueya_chart_view = (LinearLayout) view
				.findViewById(R.id.xueya_chart_view);
		xueya_chart_rl = (RelativeLayout) view
				.findViewById(R.id.xueya_chart_rl);
	}

	/**
	 * 获取标准图
	 */
	private void getDongTaiTu(int x, int y, int durationTime) {
		// 高压平移
		TranslateAnimation tla = new TranslateAnimation(0, 0, 0, y);
		// 设置动画效果
		tla.setDuration(durationTime);
		tla.setRepeatCount(0);
		tla.setFillAfter(true);
		xueya_chart_right.startAnimation(tla);
		// 低压平移
		TranslateAnimation tla2 = new TranslateAnimation(0, x, 0, 0);
		// 设置动画效果
		tla2.setDuration(durationTime);
		tla2.setRepeatCount(0);
		tla2.setFillAfter(true);
		xueya_chart_bottom.startAnimation(tla2);

		TranslateAnimation tla3 = new TranslateAnimation(0, x, 0, y);
		// 设置动画效果
		tla3.setDuration(durationTime);
		tla3.setRepeatCount(0);
		tla3.setFillAfter(true);
		xueya_chart_center.startAnimation(tla3);
	}

	/**
	 * TODO 联网获取数据
	 * 
	 * @param URL
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setData(String URL) {
		fh.get(URL, new AjaxCallBack() {
			@Override
			public void onSuccess(Object t) {
				String content = (String) t;
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() == 0) {
					Toast.makeText(mContext, common.getMsg(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				BloodPressureResult result = gson.fromJson(content,
						BloodPressureResult.class);
				str = result.getData().getBloodPressure().getWorldRessultDesc();
				// xueya_chart_suggestion.setText(str);
				int gaoya = result.getData().getBloodPressure()
						.getBloodPressureClose();
				int diya = result.getData().getBloodPressure()
						.getBloodPressureOpen();
				// 设置血压字体颜色

				if (gaoya <= 120) {
					x = 0;
				} else if (gaoya > 120 && gaoya <= 130) {
					x = 1;
				} else if (gaoya > 130 && gaoya <= 140) {
					x = 2;
				} else if (gaoya > 140 && gaoya <= 160) {
					x = 3;
				} else if (gaoya > 160 && gaoya <= 180) {
					x = 4;
				} else if (gaoya > 180) {
					x = 5;
				}
				if (diya <= 80) {
					y = 0;
				} else if (diya > 80 && diya <= 85) {
					y = 1;
				} else if (diya > 85 && diya <= 90) {
					y = 2;
				} else if (diya > 90 && diya <= 100) {
					y = 3;
				} else if (diya > 100 && diya <= 110) {
					y = 4;
				} else if (gaoya > 180 && diya > 110) {
					y = 5;
				}
				if (diya >= 0 && diya <= 80) {
					x = Integer.valueOf(diya
							* DisplayUtils.dip2px(getActivity(), 54) / 80);
				} else if (diya > 80 && diya <= 90) {
					x = DisplayUtils.dip2px(getActivity(), 54)
							+ Integer.valueOf((diya - 80)
									* DisplayUtils.dip2px(getActivity(), 82)
									/ 10);
				} else if (diya > 90 && diya <= 110) {
					x = DisplayUtils.dip2px(getActivity(), 136)
							+ Integer.valueOf((diya - 90)
									* DisplayUtils.dip2px(getActivity(), 82)
									/ 20);
				} else if (diya > 110) {
					x = DisplayUtils.dip2px(getActivity(), 240);
				}
				if (gaoya >= 0 && gaoya <= 120) {
					y = -Integer.valueOf(gaoya
							* DisplayUtils.dip2px(getActivity(), 54) / 120);
				} else if (gaoya > 120 && gaoya <= 140) {
					y = -DisplayUtils.dip2px(getActivity(), 54)
							- Integer.valueOf((gaoya - 120)
									* DisplayUtils.dip2px(getActivity(), 82)
									/ 20);
				} else if (gaoya > 140 && gaoya <= 180) {
					y = -DisplayUtils.dip2px(getActivity(), 136)
							- Integer.valueOf((gaoya - 140)
									* DisplayUtils.dip2px(getActivity(), 82)
									/ 40);
				} else if (gaoya > 180) {
					y = -DisplayUtils.dip2px(getActivity(), 240);
				}
				if (isFirstIn) {
					getDongTaiTu(x, y, 1000);
					/*if(!SettingUtils.get(mContext,"kpa",false)){
						getDongTaiTu(x, y, 1000);
					}else{
						getDongTaiTu((int)(x/7.5), (int)(y/7.5),1000);
					}*/
				}
			}
		});
	}

	private Thread dialogAndTextThread;
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				int n = msg.arg1;
				if (xueya_chart_suggestion != null) {
					xueya_chart_suggestion.setText(str.substring(0, (n + 1)));
				}
				break;

			default:
				break;
			}
		}
	};
	private LinearLayout xueya_chart_view;
	private RelativeLayout xueya_chart_rl;

	private void getSuggesstion() {
		xueya_chart_view.setVisibility(View.VISIBLE);
		TranslateAnimation translateAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		translateAnimation.setRepeatCount(0);
		translateAnimation.setDuration(2000);
		xueya_chart_view.startAnimation(translateAnimation);
		translateAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				xueya_chart_rl.setVisibility(View.INVISIBLE);
				xueya_chart_suggestion.setText("");
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
		xueya_chart_rl.setVisibility(View.VISIBLE);
		Animation aa = new AlphaAnimation(0f, 1.0f);
		aa.setDuration(1000);
		xueya_chart_rl.startAnimation(aa);
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
						if (!TextUtils.isEmpty(str)) {
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
					}
				});
				dialogAndTextThread.start();
			}
		});
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		view = null;
		mContext = null;
		xueya_chart_right = null;
		xueya_chart_bottom = null;
		xueya_chart_center = null;
		xueya_chart_suggestion = null;
		gson = null;
		URL = null;
		userId = null;
		pd = null;
		str = null;
		fh = null;
		System.gc();
		super.onDestroy();
	}
}
