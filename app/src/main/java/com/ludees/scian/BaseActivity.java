package com.ludees.scian;

import java.io.File;
import java.io.FileOutputStream;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.activity.AbActivity;
import com.ludees.scian.main.HomeActivity;
import com.ludees.scian.util.MyUtil;
import com.ludees.scian.util.SettingUtils;
import com.ludees.scian.view.CustomDialog;
import com.ludees.scian.view.CustomDialog.Builder;

/**
 * @author zhaoW
 * 
 */
public class BaseActivity extends AbActivity {

	ProgressDialog dialog;
	private ProgressDialog downloadDialog;
	// 手指上下滑动时的最小速度
	private static final int YSPEED_MIN = 1000;

	// 手指向右滑动时的最小距离
	private static final int XDISTANCE_MIN = 200;

	// 手指向上滑或下滑时的最小距离
	private static final int YDISTANCE_MIN = 100;

	// 记录手指按下时的横坐标。
	private float xDown;

	// 记录手指按下时的纵坐标。
	private float yDown;

	// 记录手指移动时的横坐标。
	private float xMove;

	// 记录手指移动时的纵坐标。
	private float yMove;

	// 用于计算手指滑动的速度。
	private VelocityTracker mVelocityTracker;
	// 是否能够滑动删除
	private boolean isHuaDong = true;
	// 是否是结果页
	private boolean isResult = false;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		AppManager.getAppManager().addActivity(this);
		super.onCreate(savedInstanceState);
		// 设置无标题
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	protected void onResume() {
		super.onResume();
	}

	protected void onPause() {
		super.onPause();
	}

	/**
	 * 设置全屏
	 */
	public void setFullScreen() {
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/**
	 * @param requestStr
	 * @return 传值获取屏幕宽高
	 */
	public int getWH(String requestStr) {
		@SuppressWarnings("deprecation")
		int width = this.getWindowManager().getDefaultDisplay().getWidth();
		@SuppressWarnings("deprecation")
		int height = this.getWindowManager().getDefaultDisplay().getHeight();
		if (requestStr.equalsIgnoreCase("w")) {
			return width;
		}
		if (requestStr.equalsIgnoreCase("h")) {
			return height;
		}
		return 0;
	}

	/**
	 * 进行耗时阻塞操作时,需要调用改方法,显示等待效果
	 */
	public void showProgressDialog(String content, boolean isOutsideDismiss) {
		if (dialog == null) {
			dialog = new ProgressDialog(this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		}

		dialog.setCanceledOnTouchOutside(isOutsideDismiss);
		dialog.setMessage(content);
		if (!isFinishing() && !dialog.isShowing()) {
			dialog.show();
		}
	}

	/**
	 * 耗时阻塞操作结束时,需要调用改方法,关闭等待效果
	 */
	public void closeProgressDialog() {
		// if (loadingDialog != null || !isFinishing()) {
		// loadingDialog.dismiss();
		// loadingDialog = null;
		// if (dialog != null ) {
		// dialog.cancel();
		// dialog = null;
		if (loadingDialog != null) {
			loadingDialog.dismiss();
			loadingDialog = null;
			// if (!isFinishing()) {
			// loadingDialog.dismiss();
			// loadingDialog = null;
			// }

		}
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

	private void setDownloadDialog() {
		downloadDialog = new ProgressDialog(this);
		downloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		downloadDialog.setTitle("downloading...");
		downloadDialog.setIndeterminate(false);
		downloadDialog.setCancelable(false);
		downloadDialog.setCanceledOnTouchOutside(false);
	}

	/**
	 * 进行耗时阻塞操作时,需要调用改方法,显示等待效果
	 * 
	 * @author robin *
	 */
	public void showProgressDialog(String content) {
		// dialog = new ProgressDialog(this);
		// dialog.setMessage(content);
		// dialog.setCanceledOnTouchOutside(false);
		// // dialog.setCancelable(false);
		// // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		createLoadingDialog(this, content);
		if (!isFinishing()) {
			// dialog.show();
			loadingDialog.show();
		}
	}

	Dialog loadingDialog;

	public Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading_animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// 设置加载信息

		loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		return loadingDialog;

	}

	/**
	 * 设置跳转当前页
	 * 
	 * @param context
	 */
	public void setContext(Context context) {
		this.mContext = context;
	}

	/**
	 * 设置是否是结果页
	 * 
	 * @param result
	 */
	public void setIsResult(boolean result) {
		this.isResult = result;
	}

	//
	// /**
	// * @param mDataView1
	// * @param type 选择类型——楼号1，单元号2，门号3
	// */
	// public void initWheelData(View mDataView1,final TextView
	// textView,List<String>list) {
	// final AbWheelView mWheelView1 = (AbWheelView) mDataView1
	// .findViewById(R.id.wheelView1);
	//
	// mWheelView1.setAdapter(new AbStringWheelAdapter(list));
	//
	// // mWheelView1.setAdapter(new AbNumericWheelAdapter(40, 190));
	// // mWheelView1.setAdapter(new AbStringWheelAdapter(items));
	// // 可循环滚动
	// mWheelView1.setCyclic(true);
	// // 添加文字
	// // mWheelView1.setLabel(getResources().getString(R.string.data1_unit));
	// // 初始化时显示的数据
	// mWheelView1.setCurrentItem(3);
	// mWheelView1.setValueTextSize(35);
	// mWheelView1.setLabelTextSize(35);
	// mWheelView1.setLabelTextColor(0x80000000);
	// // mWheelView1.setCenterSelectDrawable(this.getResources().getDrawable(
	// // R.drawable.wheel_select));
	//
	// Button okBtn = (Button) mDataView1.findViewById(R.id.okBtn);
	// Button cancelBtn = (Button) mDataView1.findViewById(R.id.cancelBtn);
	// okBtn.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// AbDialogUtil.removeDialog(v.getContext());
	// int index = mWheelView1.getCurrentItem();
	// String val = mWheelView1.getAdapter().getItem(index);
	// textView.setText(val);
	// }
	//
	// });
	//
	// cancelBtn.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// AbDialogUtil.removeDialog(v.getContext());
	// }
	//
	// });
	// }

	public void GetandSaveCurrentImage() {
		// 1.构建Bitmap
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int w = display.getWidth();
		int h = display.getHeight();

		Bitmap Bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);

		// 2.获取屏幕
		View decorview = this.getWindow().getDecorView();
		decorview.setDrawingCacheEnabled(true);
		Bmp = decorview.getDrawingCache();

		// 3.保存Bitmap
		try {
			File path = new File(MyUtil.PATH);
			// 文件
			String filepath = MyUtil.PATH + "/screen1.png";
			System.out.println("*************" + MyUtil.PATH + "/screen1.png");
			File file = new File(filepath);
			if (!path.exists()) {
				path.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}

			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			if (null != fos) {
				Bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
				fos.flush();
				fos.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showToast(Context mContext, String msg) {
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}

	Dialog mDialog;

	/**
	 * 
	 * @Title: showComfirm
	 * @Description: TODO 两个按钮的对话框
	 * @param @param context
	 * @param @param msg
	 * @param @param cancleStr 取消按钮文字
	 * @param @param enterStr 确认按钮文字
	 * @param @param listener 确认按钮的事件
	 * @return void
	 * @throws
	 */
	public void showComfirm(Context context, String msg, String cancleStr,
			String enterStr, final IXDialog ll) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.xdg_confirm, null);
		// 获取相应控件
		TextView tv_msg = (TextView) v.findViewById(R.id.tv_msg);
		Button btn_cancle = (Button) v.findViewById(R.id.btn_cancle);
		Button btn_enter = (Button) v.findViewById(R.id.btn_enter);
		// 设置文字
		tv_msg.setText(msg);
		// 设置事件
		btn_cancle.setText(cancleStr);
		btn_enter.setText(enterStr);
		btn_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (null != mDialog && mDialog.isShowing())
					mDialog.dismiss();
			}
		});
		btn_enter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (null != mDialog && mDialog.isShowing())
					mDialog.dismiss();
				if (null != ll)
					ll.onXDialogEnter();
			}
		});
		// Init 创建Dialog
		mDialog = new Dialog(context, R.style.xdg_style);
		mDialog.setContentView(v, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));
		mDialog.setCancelable(true);
		mDialog.show();
		mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_HOME
						|| keyCode == KeyEvent.KEYCODE_BACK) {
					if (null != mDialog && mDialog.isShowing())
						mDialog.dismiss();
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * 
	 * @ClassName: IXDialog
	 * @Description: TODO IXDialog接口
	 * @author Lijc
	 * @date 2015-3-17 下午2:17:33
	 * 
	 */
	public interface IXDialog {
		/**
		 * 
		 * @Title: onXDialogEnter
		 * @Description: TODO
		 * @param
		 * @return void
		 * @throws
		 */
		public void onXDialogEnter();
	}

	/**
	 * 此功能暂未开放
	 * 
	 * @param context
	 */
	public void getNoMessage(Context context) {
		CustomDialog.Builder builder = new Builder(context);
		builder.setTitle(R.string.prompt);
		builder.setMessage(this.getResources().getString(
				R.string.cigongnengzanweikaifang));
		// 确定按钮点击事件处理
		builder.setPositiveButton(
				this.getResources().getString(R.string.confirm),
				new DialogInterface.OnClickListener() {

					@SuppressLint("NewApi")
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		createVelocityTracker(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDown = event.getRawX();
			yDown = event.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:

			xMove = event.getRawX();
			yMove = event.getRawY();
			// 滑动的距离
			int distanceX = (int) (xMove - xDown);
			int distanceY = (int) (yMove - yDown);
			// 获取顺时速度
			int ySpeed = getScrollVelocity();
			// 关闭Activity需满足以下条件：
			// 1.x轴滑动的距离>XDISTANCE_MIN
			// 2.y轴滑动的距离在YDISTANCE_MIN范围内
			// 3.y轴上（即上下滑动的速度）<XSPEED_MIN，如果大于，则认为用户意图是在上下滑动而非左滑结束Activity
			if (distanceX > XDISTANCE_MIN
					&& (distanceY < YDISTANCE_MIN && distanceY > -YDISTANCE_MIN)
					&& ySpeed < YSPEED_MIN) {
				if (isHuaDong) {
					if (isResult) {
						Intent intent = new Intent(mContext, HomeActivity.class);
						intent.putExtra("num", 0);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.push_right_in,
								R.anim.push_right_out);
					} else {
						finish();
					}
				} else {
				}
			}

			break;
		case MotionEvent.ACTION_UP:
			recycleVelocityTracker();
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(event);
	}

	/**
	 * 创建VelocityTracker对象，并将触摸界面的滑动事件加入到VelocityTracker当中。
	 * 
	 * @param event
	 * 
	 */
	private void createVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}

	/**
	 * 回收VelocityTracker对象。
	 */
	private void recycleVelocityTracker() {
		mVelocityTracker.recycle();
		mVelocityTracker = null;
	}

	/**
	 * 
	 * @return 滑动速度，以每秒钟移动了多少像素值为单位。
	 */
	private int getScrollVelocity() {
		mVelocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) mVelocityTracker.getYVelocity();
		return Math.abs(velocity);
	}

	/**
	 * 设置界面是否能够滑动删除
	 */
	public void setIsHuaDong(boolean flag) {
		this.isHuaDong = flag;
	}

	public void goBack(Context context) {
		SettingUtils.set(context, "myFriendData", "1");
		SettingUtils.set(context, "isFirstChart", "1");
		SettingUtils.set(context, "isFirstTrendChart", "1");
		SettingUtils.set(context, "isFirstFriendChart", "1");
		SettingUtils.set(context, "isFirstFriendData", "1");
		Intent intent = new Intent(context, HomeActivity.class);
		intent.putExtra("num", 0);
		context.startActivity(intent);
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		finish();
	}
}
