package com.ludees.scian.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ludees.scian.R;


public class CustomDialog extends Dialog {
	private CustomDialog dialog;
	public CustomDialog(Context context) {
		super(context);
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context; //上下文对象
		private String title; //对话框标题
		private String message; //对话框内容
		private String confirm_btnText; //按钮名称“确定”
		private String cancel_btnText; //按钮名称“取消”
		private View contentView; //对话框中间加载的其他布局界面
		private boolean flag = false;
		private boolean isFullScreen = false;
		private WindowManager manager;
		private Window window2;
		/*按钮坚挺事件*/
		private DialogInterface.OnClickListener confirm_btnClickListener; 
		private DialogInterface.OnClickListener cancel_btnClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		/*设置对话框信息*/
		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}

		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		/**
		 * 设置对话框界面
		 * @param v View
		 * @return
		 */
		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		public Builder setPositiveButton(int confirm_btnText,
				DialogInterface.OnClickListener listener) {
			this.confirm_btnText = (String) context
					.getText(confirm_btnText);
			this.confirm_btnClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String confirm_btnText,
				DialogInterface.OnClickListener listener) {
			this.confirm_btnText = confirm_btnText;
			this.confirm_btnClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int cancel_btnText,
				DialogInterface.OnClickListener listener) {
			this.cancel_btnText = (String) context
					.getText(cancel_btnText);
			this.cancel_btnClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String cancel_btnText,
				DialogInterface.OnClickListener listener) {
			this.cancel_btnText = cancel_btnText;
			this.cancel_btnClickListener = listener;
			return this;
		}
		/**
		 * 是否从底部弹出
		 * @param flag
		 * @return
		 */
		public boolean isBottom(boolean flag,WindowManager manager,Window window){
			this.flag = flag;
			this.manager = manager;
			this.window2 = window;
			return this.flag;
			
		}
		
		/**
		 * 是否是全屏显示
		 * @param flag
		 */
		private void isFullScreen(boolean flag,WindowManager manager){
			this.isFullScreen = flag;
			this.manager = manager;
			
		}
		
		public CustomDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final CustomDialog dialog = new CustomDialog(context, R.style.mystyle);
		
 			View layout = inflater.inflate(R.layout.customdialog, null);
			if(flag){
				Window window = dialog.getWindow();
				window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置 
				window.setWindowAnimations(R.style.AnimBottom);  //添加动画  
				//设置全屏
				Display d = manager.getDefaultDisplay(); // 获取屏幕宽、高用
				WindowManager.LayoutParams p = window2.getAttributes(); // 获取对话框当前的参数值
				p.height = (int) ( d.getHeight()); // 高度设置为屏幕的0.6
				p.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.95
//				window.setAttributes(p);
//				dialog.getWindow().setLayout(p.width, p.height);
				//设置dialog的大小
				layout.setMinimumWidth(p.width);
				
			}
			
			LinearLayout line = (LinearLayout) layout.findViewById(R.id.line);
			dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			((TextView) layout.findViewById(R.id.title)).setText(title);
			((TextView) layout.findViewById(R.id.title)).getPaint().setFakeBoldText(true);;
			// set the confirm button
			if (confirm_btnText != null&&!confirm_btnText.equals("")) {
				((Button) layout.findViewById(R.id.confirm_btn))
						.setText(confirm_btnText);
				if (confirm_btnClickListener != null) {
					((Button) layout.findViewById(R.id.confirm_btn))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									confirm_btnClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.confirm_btn).setVisibility(
						View.GONE);
				line.setVisibility(View.GONE);
			}
			// set the cancel button
			if (cancel_btnText != null&&!cancel_btnText.equals("")) {
				((Button) layout.findViewById(R.id.cancel_btn))
						.setText(cancel_btnText);
				if (cancel_btnClickListener != null) {
					((Button) layout.findViewById(R.id.cancel_btn))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									cancel_btnClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.cancel_btn).setVisibility(
						View.GONE);
				line.setVisibility(View.GONE);
			}
			// set the content message
			if (message != null) {
				((TextView) layout.findViewById(R.id.message)).setText(message);
			} else if (contentView != null) {
				((RelativeLayout) layout.findViewById(R.id.layout_message))
						.removeAllViews();
				((RelativeLayout) layout.findViewById(R.id.layout_message)).addView(
						contentView, new LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT));
			}
			dialog.setContentView(layout);
			return dialog;
		}

	}
}
