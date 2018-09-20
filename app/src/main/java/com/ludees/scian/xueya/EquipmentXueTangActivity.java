package com.ludees.scian.xueya;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clj.fastble.BluetoothService;
import com.clj.fastble.data.ScanResult;
import com.clj.fastble.iscian.IscianService;
import com.ludees.scian.BaseActivity;
import com.ludees.scian.ExampleApplication;
import com.ludees.scian.R;
import com.ludees.scian.adapter.ResultAdapter;
import com.ludees.scian.view.PullDownElasticImp;
import com.ludees.scian.view.PullDownScrollView;
import com.ludees.scian.view.PullDownScrollView.RefreshListener;

/**
 * 血糖、血压设备检测
 * 
 * @author zhangp
 * 
 */
@SuppressLint("NewApi")
public class EquipmentXueTangActivity extends BaseActivity implements
		OnClickListener, RefreshListener {
	/**
	 * 头部左边按钮
	 */
	private LinearLayout search_leftLayout;
	/**
	 * 头部中心标题
	 */
	private TextView search_titleText;
	/**
	 * 头部右边按钮
	 */
	private LinearLayout search_rightLayout;
	private TextView search_right_txtView;
	/**
	 * 等待栏
	 */
	private RelativeLayout equipment_relayout1;
	/**
	 * 判断是血糖还是血压设备
	 */
	private boolean isXueTang;
	private boolean quxiao;

	private RelativeLayout equipment_xuetang_view;
	private TextView xuetang_tv;
	private PullDownScrollView equipment_scroll;
	private ListView equipment_xueya_list;
	/**
	 * 获取蓝牙adapter
	 */
	private BluetoothAdapter bluetoothAdapter;
	/**
	 * 蓝牙数据列表
	 */
	private List<BluetoothDevice> device;
	private ResultAdapter adapter;
	private List<String> name;
	/**
	 * 广播是否注册
	 */
	private boolean isRegister = false;

	private IscianService mBluetoothService;
	private ProgressDialog progressDialog;
	private BluetoothDevice mDevice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO onCreate
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_equipment_xuetang);
		init();
		setData();

	}
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					mBluetoothService.closeConnect();
					if (mBluetoothService != null) {
						mBluetoothService.cancelScan();
					}
					if (mBluetoothService != null) {
						unbindService();
					}
					break;
			}
			super.handleMessage(msg);
		}
	};
	private void init() {
		name = new ArrayList<String>();
		ExampleApplication.getInstance().addActivity(this);
		search_leftLayout = (LinearLayout) this
				.findViewById(R.id.search_leftLayout);
		search_titleText = (TextView) this.findViewById(R.id.search_titleText);
		search_right_txtView = (TextView) this
				.findViewById(R.id.search_right_txtView);
		search_rightLayout = (LinearLayout) this
				.findViewById(R.id.search_rightLayout);
		search_rightLayout.setVisibility(View.VISIBLE);
		search_right_txtView.setVisibility(View.VISIBLE);
		search_leftLayout.setOnClickListener(this);
		search_titleText.setText(this.getResources().getString(R.string.equipmentxuetangactivity_sssb));
		search_right_txtView.setText(this.getResources().getString(R.string.equipmentxuetangactivity_sdsr));
		search_rightLayout.setOnClickListener(this);

		isXueTang = getIntent().getBooleanExtra("isXueTang", false);
		quxiao = getIntent().getBooleanExtra("quxiao", false);

		equipment_scroll = (PullDownScrollView) this
				.findViewById(R.id.equipment_scroll);
		equipment_xuetang_view = (RelativeLayout) this
				.findViewById(R.id.equipment_xuetang_view);
		equipment_xueya_list = (ListView) this
				.findViewById(R.id.equipment_xueya_list);
		equipment_relayout1 = (RelativeLayout) this
				.findViewById(R.id.equipment_relayout1);
		xuetang_tv = (TextView) this.findViewById(R.id.xuetang_tv);

		equipment_scroll.setRefreshTips(this.getResources().getString(R.string.chazhaoshebei), this.getResources().getString(R.string.chazhaowancheng), this.getResources().getString(R.string.zhengzaichazhaoshebeiqingshaohou));
		equipment_scroll.setPullDownElastic(new PullDownElasticImp(this));
		equipment_scroll.setRefreshListener(this);
		equipment_scroll.setVisibility(View.VISIBLE);
		equipment_xuetang_view.setVisibility(View.VISIBLE);
		// 检查设备是否支持蓝牙
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		// 打开蓝牙
		if (!bluetoothAdapter.isEnabled()) {
			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			// 设置蓝牙可见性，最多300秒
			intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			this.startActivity(intent);
		}
		progressDialog = new ProgressDialog(this);
		progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				// Cancel task.
				if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN) {
					handler.sendEmptyMessageDelayed(0,1000);
				}
				return false;
			}
		});
		equipment_xueya_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mDevice = adapter.getItem(position).getDevice();
				if("Dual-SPP".equals(adapter.getItem(position).getDevice().getName())){
					success(adapter.getItem(position).getDevice());
				}else{
					if (mBluetoothService != null) {
						mBluetoothService.cancelScan();
						mBluetoothService.connectDevice(adapter.getItem(position));
						adapter.clear();
						adapter.notifyDataSetChanged();
					}
				}
				/*BluetoothDevice blueToothDevice = device.get(position);
				Method createBondMethod;
				try {
					createBondMethod = BluetoothDevice.class
							.getMethod("createBond");
					Method createBondMethod1 = BluetoothDevice.class
							.getMethod("createBond");
					try {
						createBondMethod1.invoke(device);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				success(blueToothDevice);*/
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.search_leftLayout:
			finish();
			break;
		case R.id.search_rightLayout:
			if (isXueTang) {
				// Intent intent = new Intent(EquipmentXueTangActivity.this,
				// XueTangCeLiangShouDongActivity.class);
				// startActivity(intent);
			} else {
				Intent intent = new Intent(EquipmentXueTangActivity.this,
						XueYaCeLiangShouDongActivity.class);
				startActivity(intent);
				finish();
			}
			break;
		default:
			break;
		}
	}

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
				if (!TextUtils.isEmpty(device2.getName())
						&& device2.getName().length() > 6) {
					// 如果查找到的设备符合要连接的设备，处理
					if (!TextUtils.isEmpty(device2.getName())
							&& "Dual".equals(device2.getName().substring(0, 4))) {
						// 搜索蓝牙设备的过程占用资源比较多，一旦找到需要连接的设备后需要及时关闭搜索
						// 将搜索到的设备添加到适配器列表中
						if (!adapter.getResultNameList().contains(device2.getName())) {
							adapter.addResult(new ScanResult(device2,0,null,0));
							adapter.notifyDataSetChanged();
							isRegister = true;
						}

					}
				}

			}
		}
	};
	private static boolean isFinish = true;
	private void setData() {
		device = new ArrayList<BluetoothDevice>();
		adapter = new ResultAdapter(EquipmentXueTangActivity.this);
		equipment_xueya_list.setAdapter(adapter);
		// TODO 注册蓝牙搜索返回接收器
		// 设置广播信息过滤
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		// 注册广播接收器，接收并处理搜索结果
		registerReceiver(fundReceiver, intentFilter);
		// 寻找蓝牙设备，android会将查找到的设备以广播形式发出去(该函数时异步的，调用后立即返回，返回值表示搜索是否成功开始,搜索处理通常包括一个12秒钟的查询扫描)
		//boolean discovery = bluetoothAdapter.startDiscovery();
		checkPermissions();
		new Timecount(6 * 1000, 1000).start();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if(isFinish){
					mBluetoothService.cancelMeasure();
					bluetoothAdapter.startDiscovery();
				}
			}
		}, 5000);
	}

	private void bindService() {
		Intent bindIntent = new Intent(this, IscianService.class);
		this.bindService(bindIntent, mFhrSCon, Context.BIND_AUTO_CREATE);
	}

	private void unbindService() {
		this.unbindService(mFhrSCon);
	}

	/**
	 * ble扫描服务
	 */
	private ServiceConnection mFhrSCon = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBluetoothService = ((IscianService.BluetoothBinder) service).getService();
			mBluetoothService.setScanCallback(callback);
			mBluetoothService.scanDevice();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mBluetoothService = null;
		}
	};
	/**
	 * ble连接回调
	 */
	private BluetoothService.Callback callback = new BluetoothService.Callback(){

		@Override
		public void onStartScan() {
			adapter.clear();
			adapter.notifyDataSetChanged();
		}

		@Override
		public void onScanning(ScanResult scanResult) {
			if(!TextUtils.isEmpty(scanResult.getDevice().getName())){
				adapter.addResult(scanResult);
				adapter.notifyDataSetChanged();
			}
		}

		@Override
		public void onScanComplete() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onConnecting() {
			progressDialog.show();
		}

		@Override
		public void onConnectFail() {
			progressDialog.dismiss();
		}

		@Override
		public void onDisConnected() {
			progressDialog.show();
			adapter.clear();
			adapter.notifyDataSetChanged();
		}

		@Override
		public void onServicesDiscovered() {
			isFinish = false;
			progressDialog.dismiss();
			Intent in = new Intent(EquipmentXueTangActivity.this,XueYaCeLiangZhiDaoActivity.class);
			Bundle bundle = new Bundle();
			bundle.putParcelable("device", mDevice);
			in.putExtra("bundle", bundle);
			in.putExtra("bluetooth", true);
			startActivity(in);
			//finish();
		}

	};

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case 12:
				if (grantResults.length > 0) {
					for (int i = 0; i < grantResults.length; i++) {
						if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
							onPermissionGranted(permissions[i]);
						}
					}
				}
				break;
		}
	}
	/**
	 * 权限获取
	 * @param permission
	 */
	private void onPermissionGranted(String permission) {
		switch (permission) {
			case Manifest.permission.ACCESS_FINE_LOCATION:
				if (mBluetoothService == null) {
					bindService();
				} else {
					mBluetoothService.scanDevice();
				}
				break;
		}
	}
	/**
	 * 检查权限
	 */
	private void checkPermissions() {
		String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
		List<String> permissionDeniedList = new ArrayList<>();
		for (String permission : permissions) {
			int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
			if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
				onPermissionGranted(permission);
			} else {
				permissionDeniedList.add(permission);
			}
		}
		if (!permissionDeniedList.isEmpty()) {
			String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
			ActivityCompat.requestPermissions(this, deniedPermissions, 12);
		}
	}

	@Override
	public void onRefresh(PullDownScrollView view) {
		// // TODO 下拉刷新
		device.clear();
		name.clear();
		adapter.notifyDataSetChanged();
		// 设置广播信息过滤
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		// 注册广播接收器，接收并处理搜索结果
		registerReceiver(fundReceiver, intentFilter);
		// 寻找蓝牙设备，android会将查找到的设备以广播形式发出去(该函数时异步的，调用后立即返回，返回值表示搜索是否成功开始,搜索处理通常包括一个12秒钟的查询扫描)
		/*boolean discovery = bluetoothAdapter.startDiscovery();
		if (discovery) {
			// 点击倒数12秒
			new Timecount(2 * 1000, 1000).start();
		}*/
		checkPermissions();
		new Timecount(8 * 1000, 1000).start();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if(isFinish){
					mBluetoothService.cancelMeasure();
					bluetoothAdapter.startDiscovery();
				}
			}
		}, 5000);
	}

	/**
	 * TODO 蓝牙搜索的倒计时的计时器
	 */
	class Timecount extends CountDownTimer {

		public Timecount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {

		}

		@Override
		public void onFinish() {
			// if(fundReceiver!=null){
			// // // 注销关闭广播
			// unregisterReceiver(fundReceiver);
			// }
			/*// 关闭搜索
			if (bluetoothAdapter != null) {
				bluetoothAdapter.cancelDiscovery();
			}*/
			// 关闭头部搜索进度圆圈
			if (equipment_relayout1 != null) {
				equipment_relayout1.setVisibility(View.GONE);
			}
			if (equipment_scroll != null) {
				equipment_scroll.finishRefresh(EquipmentXueTangActivity.this.getResources().getString(R.string.chazhaowancheng));
			}
			if (xuetang_tv != null) {
				if (name != null && name.size() == 0) {
					xuetang_tv.setText(EquipmentXueTangActivity.this.getResources().getString(R.string.equipmentxuetangactivity_wssdsb));
				} else {

					xuetang_tv.setText(EquipmentXueTangActivity.this.getResources().getString(R.string.equipmentxuetangactivity_lianjie));
				}
			}

		}
	}

	/**
	 * TODO 连接设备
	 */
	private void success(final BluetoothDevice device) {
		Intent intent = new Intent(EquipmentXueTangActivity.this,
				XueYaCeLiangZhiDaoActivity.class);
		intent.putExtra("addresss", device.getAddress());
		intent.putExtra("name", device.getName());
		intent.putExtra("quxiao", quxiao);
		Bundle bundle = new Bundle();
		bundle.putParcelable("device", device);
		intent.putExtra("bundle", bundle);
		startActivity(intent);
		/*Intent in = new Intent(EquipmentXueTangActivity.this,XueYaCeLiangActivity.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable("device", mDevice);
		in.putExtra("bundle", bundle);
		in.putExtra("bluetooth", false);
		startActivity(in);*/
		finish();
	}

	@Override
	protected void onDestroy() {
		isFinish = false;
		if (mBluetoothService != null)
			unbindService();
		search_leftLayout = null;
		search_titleText = null;
		search_rightLayout = null;
		search_right_txtView = null;
		equipment_relayout1 = null;
		equipment_xuetang_view = null;
		xuetang_tv = null;
		equipment_scroll = null;
		equipment_xueya_list = null;
		bluetoothAdapter = null;
		device = null;
		adapter = null;
		name = null;
		setContentView(R.layout.xml_null);
		System.gc();
		super.onDestroy();
	}

}
