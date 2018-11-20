package com.en.scian.mydevices;

import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.en.scian.R;
import com.en.scian.R.color;
import com.en.scian.entity.MyEquipment;
import com.en.scian.entity.ResponseCommon;
import com.en.scian.network.Urls;
import com.en.scian.util.ToastUtils;
import com.google.gson.Gson;

/**
 * 我的设备list适配器
 * 
 * @author jiyx
 * 
 */
public class MyDevicesAdapter extends BaseAdapter {

	private Context mContext;
	private List<MyEquipment> list;

	Gson gson = new Gson();
	private FinalHttp fh;

	public MyDevicesAdapter(Context context, List<MyEquipment> list) {
		this.mContext = context;
		this.list = list;
		fh = new FinalHttp();
		fh.configTimeout(15 * 1000);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.personalcenter_mydevices_item, null);
			holder.mydevice_img = (ImageView) convertView
					.findViewById(R.id.personalcenter_mydevices_img);
			holder.mydevice_name = (TextView) convertView
					.findViewById(R.id.personalcenter_mydevices_tv_name);
			holder.mydevice_delete = (Button) convertView
					.findViewById(R.id.personalcenter_mydevices_btn_delete);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mydevice_img.setBackgroundResource(color.mydevices_left_img2);
		final MyEquipment myEquipment = list.get(position);
		int type = myEquipment.getEquipmentType();
		holder.mydevice_name.setText(myEquipment.getEquipmentNo());
		holder.mydevice_delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteMyDevice(Urls.MY_DEVICES_DELETE + "userEquipmentId="
						+ myEquipment.getUserEquipmentId(), position);
			}

		});
		return convertView;
	}

	private class ViewHolder {
		ImageView mydevice_img;
		TextView mydevice_name;
		Button mydevice_delete;
	}

	/**
	 * 删除我的设备
	 * 
	 * @param url
	 * @param position
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void deleteMyDevice(String url, final int position) {
		fh.get(url, new AjaxCallBack() {
			private ProgressDialog pd;

			// 开始执行前
			@Override
			public void onStart() {
				pd = new ProgressDialog(mContext);
				pd.setCancelable(false);
				pd.setMessage(mContext.getResources().getString(R.string.zhengzaifasonghaoyouyanzheng));
				pd.show();
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				pd.dismiss();
				ToastUtils.TextToast(mContext, mContext.getResources().getString(R.string.fuwuqilianjieyichang));
			}

			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				pd.dismiss();
				String content = (String) t;
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() == 0) {
					ToastUtils.TextToast(mContext, common.getMsg());
					return;
				} else if (common.getStatus() == 1) {
					// 设备删除成功
					ToastUtils.TextToast(mContext, common.getMsg());
					list.remove(position);
					notifyDataSetChanged();
				}
			}

		});
	}
}
