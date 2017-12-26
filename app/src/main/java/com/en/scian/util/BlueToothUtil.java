package com.en.scian.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;

import com.en.scian.R;
import com.en.scian.xueya.EquipmentXueTangActivity;
import com.en.scian.xueya.XueYaCeLiangZhiDaoActivity;

/**
 * 蓝牙帮助类
 * 
 * @author zhangp
 * 
 */
@SuppressLint("NewApi")
public class BlueToothUtil{
	/**
	 * 获取蓝牙adapter
	 */
	private BluetoothAdapter bluetoothAdapter;
	/**
	 * 蓝牙数据列表
	 */
	private List<BluetoothDevice> device;
	private List<BluetoothDevice> deviceSuccess = new ArrayList<BluetoothDevice>();
	BluetoothSocket socket;
	private Context context;
	/**
	 * 蓝牙数据列表
	 */
	private List<String> name = new ArrayList<String>();
	private ProgressDialog dialog;
	private boolean success = false;
	private boolean isRefuse = false;
	private Activity activity;
	private boolean b = false;
	
	public BlueToothUtil(Context context) {
		this.context = context;
	}
	
	/**
	 * 判断蓝牙是否开启
	 */
	public void setBlueTooth() {
		// 检查设备是否支持蓝牙
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!bluetoothAdapter.isEnabled()) {
			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			// 设置蓝牙可见性，最多300秒
			intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			activity.startActivityForResult(intent, 111);
//			context.startActivity(intent);
			
		} else {
			//getDevice();
			if(isRefuse){
				activity.finish();
			}
			Intent intent = new Intent(context, EquipmentXueTangActivity.class);
			intent.putExtra("quxiao", b);
			intent.putExtra("isXueTang", false);
			context.startActivity(intent);
		}
	}

	/**
	 * 获取到已适配的蓝牙设备
	 */
	public void getDevice() {
		dialog = new ProgressDialog(context);
		dialog.setCancelable(false);
		dialog.setMessage(context.getResources().getString(R.string.zhengzailianjieqingshaohou));
		//dialog.show();
		device = new ArrayList<BluetoothDevice>();
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		Set<BluetoothDevice> devices = adapter.getBondedDevices();
		for (int i = 0; i < devices.size(); i++) {
			BluetoothDevice device2 = (BluetoothDevice) devices.iterator()
					.next();
			if (device2 != null) {
				if (device2.getName() != null && !device2.getName().equals("")
						&& !device2.getName().equals("null")) {
					if (device2.getName().length() > 6
							&& "Dual".equals(device2.getName().substring(0, 4))) {
						if (!device.contains(device2)) {
							device.add(device2);
						}

					}
				}
			}
		}
		setData();
	

	}
	
	/**
	 * TODO 历史设备成功连接
	 */
	private void success(BluetoothDevice device) {
		final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
		UUID uuid = UUID.fromString(SPP_UUID);
		try {
		BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
		if(socket.isConnected()){
		Intent intent = new Intent(context, XueYaCeLiangZhiDaoActivity.class);
		intent.putExtra("addresss", device.getAddress());
		intent.putExtra("name", device.getName());
		Bundle bundle = new Bundle();
		bundle.putParcelable("device", device);
		intent.putExtra("bundle", bundle);
		context.startActivity(intent);
		}else{
			System.out.println("*********"+isRefuse);
			System.out.println("*********"+b);
			if(isRefuse){
				activity.finish();
			}
			Intent intent = new Intent(context, EquipmentXueTangActivity.class);
			intent.putExtra("quxiao", b);
			intent.putExtra("isXueTang", false);
			context.startActivity(intent);
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

//	/**
//	 * TODO 蓝牙搜索的倒计时的计时器
//	 */
//	class Timecount extends CountDownTimer {
//
//		public Timecount(long millisInFuture, long countDownInterval) {
//			super(millisInFuture, countDownInterval);
//		}
//
//		@Override
//		public void onTick(long millisUntilFinished) {
//
//		}
//
//		@Override
//		public void onFinish() {
//			if (deviceSuccess.size() > 0) {
//				success(deviceSuccess.get(0));
//
//			} else {
//				Intent intent = new Intent(context,
//						EquipmentXueTangActivity.class);
//				intent.putExtra("isXueTang", false);
//				context.startActivity(intent);
//			}
//
//		}
//	}
	
	
	/**
	 * 接收器 当搜索蓝牙设备完成时调用
	 */
	private BroadcastReceiver fundReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			/* 从intent中取得搜索结果数据 */
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device2 = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if(!TextUtils.isEmpty(device2.getName())&&device2.getName().length()>6){
					// 如果查找到的设备符合要连接的设备，处理
					if (!TextUtils.isEmpty(device2.getName())
							&& "Dual".equals(device2.getName().substring(0, 4))) {
						// 搜索蓝牙设备的过程占用资源比较多，一旦找到需要连接的设备后需要及时关闭搜索
						// 将搜索到的设备添加到适配器列表中
						if (!deviceSuccess.contains(device2)) {
							deviceSuccess.add(device2);

						}

					}
				}
				
			}
		}
	};
	
	private void setData() {
		// TODO 注册蓝牙搜索返回接收器
		// 设置广播信息过滤
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		// 注册广播接收器，接收并处理搜索结果
		context.registerReceiver(fundReceiver, intentFilter);
		// 寻找蓝牙设备，android会将查找到的设备以广播形式发出去(该函数时异步的，调用后立即返回，返回值表示搜索是否成功开始,搜索处理通常包括一个12秒钟的查询扫描)
		boolean discovery = bluetoothAdapter.startDiscovery();
		if (discovery) {
			// 点击倒数12秒
			new Timecount2(3 * 1000, 1000).start();
		}
	}
	
	/**
	 * TODO 蓝牙搜索的倒计时的计时器
	 */
	class Timecount2 extends CountDownTimer {

		public Timecount2(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {

		}

		@Override
		public void onFinish() {
//			if(fundReceiver!=null){
////				// 注销关闭广播
//				unregisterReceiver(fundReceiver);
//			}
			// 关闭搜索
			dialog.dismiss();
			bluetoothAdapter.cancelDiscovery();
			if (device == null || device.size() <= 0) {
				Intent intent = new Intent(context, EquipmentXueTangActivity.class);
				intent.putExtra("isXueTang", false);
				context.startActivity(intent);
			} else {
				for (int i = 0; i < device.size(); i++) {
					final BluetoothDevice blueToothDevice = device.get(i);
					for(int j=0;j<deviceSuccess.size();j++){
						if(deviceSuccess.get(j).getName().equals(blueToothDevice.getName())){
							success(deviceSuccess.get(j));
							success = true;
							break;
						}
					}
//					if(deviceSuccess.contains(blueToothDevice)){
//						success(blueToothDevice);
//						success = true;
//						break;
//					}

				}
				if(success){
					
				}else{
					Intent intent = new Intent(context, EquipmentXueTangActivity.class);
					intent.putExtra("isXueTang", false);
					context.startActivity(intent);
					if(isRefuse){
						activity.finish();
					}
				}
				
			}

		}
	}
	
	public void setIsRefuse(boolean isRefuse){
		this.isRefuse = isRefuse;
	}
	
	public void setActivity(Activity activity){
		this.activity = activity;
	}

	public void setBoolean(boolean b) {
		this.b = b;
	}
	
	public boolean getZzhi() {
		return isRefuse;
	}
}
