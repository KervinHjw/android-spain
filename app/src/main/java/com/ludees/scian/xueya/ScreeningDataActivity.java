package com.ludees.scian.xueya;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ludees.scian.BaseActivity;
import com.ludees.scian.ExampleApplication;
import com.ludees.scian.R;
import com.ludees.scian.util.SettingUtils;

/**
 * 筛选数据
 * 
 * @author zhangp
 * 
 */
public class ScreeningDataActivity extends BaseActivity implements
		OnClickListener {
	/**
	 * 头部左边按钮
	 */
	private LinearLayout search_leftLayout;
	/**
	 * 头部中心标题
	 */
	private TextView search_titleText;
	// private ImageView search_titleImage;
	/**
	 * 近一周
	 */
	private RelativeLayout screening_layout_1;
	private TextView screening_tv_1;
	private ImageView screening_img_1;
	/**
	 * 近2周
	 */
	private RelativeLayout screening_layout_2;
	private TextView screening_tv_2;
	private ImageView screening_img_2;
	/**
	 * 近一月
	 */
	private RelativeLayout screening_layout_3;
	private TextView screening_tv_3;
	private ImageView screening_img_3;
	/**
	 * 近2月
	 */
	private RelativeLayout screening_layout_4;
	private TextView screening_tv_4;
	private ImageView screening_img_4;
	/**
	 * 下一步
	 */
	private Button screening_next;
	private boolean isFriend = false;
	private int num = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screening_data);
		init();

	}

	private void init() {
		ExampleApplication.getInstance().addActivity(this);
		search_leftLayout = (LinearLayout) this
				.findViewById(R.id.search_leftLayout);
		search_titleText = (TextView) this.findViewById(R.id.search_titleText);
		search_leftLayout.setOnClickListener(this);
		search_titleText.setText(getResources().getString(R.string.screeningdataactivity_sxsj));

		screening_layout_1 = (RelativeLayout) this
				.findViewById(R.id.screening_layout_1);
		screening_tv_1 = (TextView) this.findViewById(R.id.screening_tv_1);
		screening_img_1 = (ImageView) this.findViewById(R.id.screening_img_1);
		screening_layout_1.setOnClickListener(this);
		// 设置字体变大效果
		screening_tv_1.setTextSize(24);

		screening_layout_2 = (RelativeLayout) this
				.findViewById(R.id.screening_layout_2);
		screening_tv_2 = (TextView) this.findViewById(R.id.screening_tv_2);
		screening_img_2 = (ImageView) this.findViewById(R.id.screening_img_2);
		screening_layout_2.setOnClickListener(this);

		screening_layout_3 = (RelativeLayout) this
				.findViewById(R.id.screening_layout_3);
		screening_tv_3 = (TextView) this.findViewById(R.id.screening_tv_3);
		screening_img_3 = (ImageView) this.findViewById(R.id.screening_img_3);
		screening_layout_3.setOnClickListener(this);

		screening_layout_4 = (RelativeLayout) this
				.findViewById(R.id.screening_layout_4);
		screening_tv_4 = (TextView) this.findViewById(R.id.screening_tv_4);
		screening_img_4 = (ImageView) this.findViewById(R.id.screening_img_4);
		screening_layout_4.setOnClickListener(this);

		screening_next = (Button) this.findViewById(R.id.screening_next);
		screening_next.setOnClickListener(this);

		isFriend = getIntent().getBooleanExtra("isFriend", false);
		if (isFriend) {
			int j = Integer
					.valueOf(SettingUtils.get(this, "myFriendData", "0"));
			if (j == 1 || j == 0) {
				selectedNum1();
			} else if (j == 2) {
				selectedNum2();
			} else if (j == 3) {
				selectedNum3();
			} else if (j == 4) {
				selectedNum4();
			}
		} else {
			int i = SettingUtils.get(this, "selectedNum", 0);
			if (i == 1 || i == 0) {
				selectedNum1();
			} else if (i == 2) {
				selectedNum2();
			} else if (i == 3) {
				selectedNum3();
			} else if (i == 4) {
				selectedNum4();
			}
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.search_leftLayout:
			// if(isFriend){
			// Intent intent = new
			// Intent(this,MyFriendHistoryDataActivity.class);
			// intent.putExtra("myFriendId",
			// getIntent().getStringExtra("myFriendId"));
			// intent.putExtra("bundle", getIntent().getBundleExtra("bundle"));
			// startActivity(intent);
			// }
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			finish();
			break;
		case R.id.screening_layout_1:
			selectedNum1();
			break;
		case R.id.screening_layout_2:
			selectedNum2();
			break;
		case R.id.screening_layout_3:
			selectedNum3();
			break;
		case R.id.screening_layout_4:
			selectedNum4();
			break;
		case R.id.screening_next:
			if (isFriend) {
				// Intent intent = new
				// Intent(this,MyFriendHistoryDataActivity.class);
				SettingUtils.set(ScreeningDataActivity.this, "myFriendData",
						String.valueOf(num));
				SettingUtils.set(ScreeningDataActivity.this,
						"isFirstFriendChart", "1");
				SettingUtils.set(ScreeningDataActivity.this,
						"isFirstFriendData", "1");
				// intent.putExtra("myFriendId",
				// getIntent().getStringExtra("myFriendId"));
				// intent.putExtra("bundle",
				// getIntent().getBundleExtra("bundle"));
				// intent.putExtra("num", num);
				// startActivity(intent);
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
				finish();
			} else {
				SettingUtils.set(ScreeningDataActivity.this, "myData",
						String.valueOf(num));
				SettingUtils.set(ScreeningDataActivity.this, "isFirstChart",
						"1");
				SettingUtils.set(ScreeningDataActivity.this,
						"isFirstTrendChart", "1");
				// Intent intent = new Intent(ScreeningDataActivity.this,
				// HomeActivity.class);
				// intent.putExtra("type", num);
				// intent.putExtra("num", 1);
				// startActivity(intent);
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
				finish();
			}

			// Intent intent2 = new Intent(ScreeningDataActivity.this,
			// XueYaHistoryDataActivity.class);
			// intent2.putExtra("num", num);
			// startActivity(intent2);
			// }
			break;
		default:
			break;
		}
	}

	/**
	 * 选中一周
	 */
	private void selectedNum1() {
		screening_tv_1.setTextColor(this.getResources().getColor(
				R.color.main_color));
		screening_img_1.setVisibility(View.VISIBLE);
		screening_layout_1.setBackgroundResource(R.drawable.choosing_on_bar);

		screening_tv_2
				.setTextColor(this.getResources().getColor(R.color.black));
		screening_img_2.setVisibility(View.GONE);
		screening_layout_2.setBackgroundResource(R.drawable.choosing_off);

		screening_tv_3
				.setTextColor(this.getResources().getColor(R.color.black));
		screening_img_3.setVisibility(View.GONE);
		screening_layout_3.setBackgroundResource(R.drawable.choosing_off);

		screening_tv_4
				.setTextColor(this.getResources().getColor(R.color.black));
		screening_img_4.setVisibility(View.GONE);
		screening_layout_4.setBackgroundResource(R.drawable.choosing_off);

		// 设置字体变化
		screening_tv_1.setTextSize(24);
		screening_tv_2.setTextSize(18);
		screening_tv_3.setTextSize(18);
		screening_tv_4.setTextSize(18);

		num = 1;
		SettingUtils.set(this, "selectedNum", 1);
	}

	/**
	 * 选中两周
	 */
	private void selectedNum2() {
		screening_tv_1
				.setTextColor(this.getResources().getColor(R.color.black));
		screening_img_1.setVisibility(View.GONE);
		screening_layout_1.setBackgroundResource(R.drawable.choosing_off);

		screening_tv_2.setTextColor(this.getResources().getColor(
				R.color.main_color));
		screening_img_2.setVisibility(View.VISIBLE);
		screening_layout_2.setBackgroundResource(R.drawable.choosing_on_bar);

		screening_tv_3
				.setTextColor(this.getResources().getColor(R.color.black));
		screening_img_3.setVisibility(View.GONE);
		screening_layout_3.setBackgroundResource(R.drawable.choosing_off);

		screening_tv_4
				.setTextColor(this.getResources().getColor(R.color.black));
		screening_img_4.setVisibility(View.GONE);
		screening_layout_4.setBackgroundResource(R.drawable.choosing_off);

		// 设置字体变化
		screening_tv_1.setTextSize(18);
		screening_tv_2.setTextSize(24);
		screening_tv_3.setTextSize(18);
		screening_tv_4.setTextSize(18);
		num = 2;
		SettingUtils.set(this, "selectedNum", 2);
	}

	/**
	 * 选中一月
	 */
	private void selectedNum3() {
		screening_tv_1
				.setTextColor(this.getResources().getColor(R.color.black));
		screening_img_1.setVisibility(View.GONE);
		screening_layout_1.setBackgroundResource(R.drawable.choosing_off);

		screening_tv_2
				.setTextColor(this.getResources().getColor(R.color.black));
		screening_img_2.setVisibility(View.GONE);
		screening_layout_2.setBackgroundResource(R.drawable.choosing_off);

		screening_tv_3.setTextColor(this.getResources().getColor(
				R.color.main_color));
		screening_img_3.setVisibility(View.VISIBLE);
		screening_layout_3.setBackgroundResource(R.drawable.choosing_on_bar);

		screening_tv_4
				.setTextColor(this.getResources().getColor(R.color.black));
		screening_img_4.setVisibility(View.GONE);
		screening_layout_4.setBackgroundResource(R.drawable.choosing_off);

		// 设置字体变化
		screening_tv_1.setTextSize(18);
		screening_tv_2.setTextSize(18);
		screening_tv_3.setTextSize(24);
		screening_tv_4.setTextSize(18);

		num = 3;
		SettingUtils.set(this, "selectedNum", 3);
	}

	/**
	 * 选中两月
	 */
	private void selectedNum4() {
		screening_tv_1
				.setTextColor(this.getResources().getColor(R.color.black));
		screening_img_1.setVisibility(View.GONE);
		screening_layout_1.setBackgroundResource(R.drawable.choosing_off);

		screening_tv_2
				.setTextColor(this.getResources().getColor(R.color.black));
		screening_img_2.setVisibility(View.GONE);
		screening_layout_2.setBackgroundResource(R.drawable.choosing_off);

		screening_tv_3
				.setTextColor(this.getResources().getColor(R.color.black));
		screening_img_3.setVisibility(View.GONE);
		screening_layout_3.setBackgroundResource(R.drawable.choosing_off);

		screening_tv_4.setTextColor(this.getResources().getColor(
				R.color.main_color));
		screening_img_4.setVisibility(View.VISIBLE);
		screening_layout_4.setBackgroundResource(R.drawable.choosing_on_bar);

		// 设置字体变化
		screening_tv_1.setTextSize(18);
		screening_tv_2.setTextSize(18);
		screening_tv_3.setTextSize(18);
		screening_tv_4.setTextSize(24);
		num = 4;
		SettingUtils.set(this, "selectedNum", 4);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			// if(isFriend){
			// Intent intent = new
			// Intent(this,MyFriendHistoryDataActivity.class);
			// intent.putExtra("myFriendId",
			// getIntent().getStringExtra("myFriendId"));
			// intent.putExtra("bundle", getIntent().getBundleExtra("bundle"));
			// startActivity(intent);
			// }
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			finish();
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		search_leftLayout = null;
		search_titleText = null;
		screening_layout_1 = null;
		screening_tv_1 = null;
		screening_img_1 = null;
		screening_layout_2 = null;
		screening_tv_2 = null;
		screening_img_2 = null;
		screening_layout_3 = null;
		screening_tv_3 = null;
		screening_img_3 = null;
		screening_layout_4 = null;
		screening_tv_4 = null;
		screening_img_4 = null;
		screening_next = null;
		setContentView(R.layout.xml_null);
		System.gc();
	}
}
