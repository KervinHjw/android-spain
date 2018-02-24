package com.en.scian.xueya;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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

import com.clj.fastble.exception.BleException;
import com.clj.fastble.iscian.IscianService;
import com.clj.fastble.iscian.bean.MeasureResult;
import com.clj.fastble.iscian.bean.MeasuringData;
import com.en.scian.BaseActivity;
import com.en.scian.ExampleApplication;
import com.en.scian.R;
import com.en.scian.entity.ResponseCommon;
import com.en.scian.main.HomeActivity;
import com.en.scian.network.Urls;
import com.en.scian.util.SettingUtils;
import com.en.scian.view.CustomDialog;
import com.en.scian.view.CustomView;
import com.en.scian.view.CustomDialog.Builder;
import com.google.gson.Gson;

/**
 * 血压测量
 * 
 * @author zhangp
 * 
 */
public class XueYaCeLiangActivity extends BaseActivity implements
		OnClickListener, IscianService.BleMessageCallback{
	/**
	 * 头部左边按钮
	 */
	private LinearLayout search_leftLayout;
	/**
	 * 头部中心标题
	 */
	private TextView search_titleText;
	/**
	 * 测量进度条
	 */
	private CustomView xueya_celiang_roundProgressBar;
	// 低压位
	private int down = -1;
	// 脉搏
	private int maibo = 0;
	private int getNew = 0;
	private BluetoothDevice device = null;
	private List<String> num;
	/**
	 * 设备是否连接成功
	 */
	@SuppressWarnings("unused")
	private boolean isSuccess = false;
	private List<byte[]> listNum;
	private Gson gson;
	private boolean isContinue = true;
	private BluetoothSocket socket;

	private Button xueya_celiang_jieshu;
	private Button xueya_celiang_chakan;
	private int percent = 0;
	private TranslateAnimation translateAnimation;
	private RelativeLayout xueya_celiang_rl;
	private LinearLayout xueya_celiang_view;
	private TextView xueya_celiang_suggestion;
	private Thread dialogAndTextThread;
	private String strA = "";
	private FinalHttp fh;
	private Thread th;
	private Thread closeTh;
	private ImageView celiang_bg;
	private Bitmap btp;

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		private byte i;

		@SuppressLint("SimpleDateFormat")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:// TODO 设置显示点击查看按钮并上传数据
				String URL = Urls.SAVE_BLOOD_PRESSURE;
				AjaxParams abRequestParams = new AjaxParams();
				abRequestParams.put("userId", SettingUtils.get(
						XueYaCeLiangActivity.this, "userId", ""));
				/*if (down < 0) {
					down = Math.abs(down);
				}
				if (getNew < 0) {
					getNew = Math.abs(getNew);
				}
				if (maibo < 0) {
					maibo = Math.abs(maibo);
				}*/
				abRequestParams.put("bloodPressureOpen", String.valueOf(down));
				abRequestParams.put("bloodPressureClose",
						String.valueOf(getNew));
				abRequestParams.put("pulse", String.valueOf(maibo));
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd    HH:mm:ss");
				Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
				String str = formatter.format(curDate);
				abRequestParams.put("measureTime", str);
				abRequestParams.put("type", "1");
				String[] name = device.getAddress().split(":");
				String name2 = "";
				for (int i = 0; i < name.length; i++) {
					name2 += name[i];
				}
				abRequestParams.put("equipmentNo", name2);
				setData(URL, abRequestParams);
				break;
			// 测量成功
			case 1:
				mBluetoothService.cancelMeasure();
				mBluetoothService.closeConnect();
				int[] result = (int[]) msg.obj;
				getNew = result[1];
				maibo = result[0];
				down = result[2];
				xueya_celiang_roundProgressBar.setFlag(false);
				xueya_celiang_roundProgressBar.setTextContent(String
						.valueOf(getNew));
				xueya_celiang_roundProgressBar.setDown(down);
				xueya_celiang_roundProgressBar.setProgress(getNew);
				handler.sendEmptyMessage(0);
				break;
			// 测量失败
			case 2:
				getNoMessage();
				break;
			// 设备超时
			case 3:
				getTimeOut(XueYaCeLiangActivity.this.getResources().getString(R.string.shebeilianjieyichangqingchongxinlianjie));
				break;
			// 充不上气
			case 4:
				getTimeOut(XueYaCeLiangActivity.this.getResources().getString(R.string.chongbushangqi));
				break;
			// 测量中发生错误
			case 5:
				getTimeOut(XueYaCeLiangActivity.this.getResources().getString(R.string.celiangzhongfashengcuowu));
				break;
			// 血压计低电量
			case 6:
				getTimeOut(XueYaCeLiangActivity.this.getResources().getString(R.string.xueyajididianliang));
				break;
			case 7:
				getTimeOut(XueYaCeLiangActivity.this.getResources().getString(R.string.quxiaocaozuo));
				break;
			// ble测量中
			case 8:
				String measuring = (String) msg.obj;
				int numPosition = 0;
				numPosition = Integer.valueOf(measuring);

				xueya_celiang_roundProgressBar.setFlag(true);
				xueya_celiang_roundProgressBar.setTextContent(String
						.valueOf(numPosition));
				xueya_celiang_roundProgressBar.setProgress(numPosition);
				break;
			case 9:
				int n = msg.arg1;
				xueya_celiang_suggestion.setText(strA.substring(0, (n + 1)));
				break;
			case 10:
				finish();
				new TimeManager(3000, 1000).start();
				break;
			case 11:
				String erro = (String) msg.obj;
				if(erro.equals("错误信息：设备充不上气")){
					getTimeOut(XueYaCeLiangActivity.this.getResources().getString(R.string.shebeiyichang00));
				}else if(erro.equals("错误信息：血压计电量过低")){
					getTimeOut(XueYaCeLiangActivity.this.getResources().getString(R.string.shebeiyichang02));
				}else if(erro.equals("错误信息：测量中发生错误，请正确测量")){
					getTimeOut(XueYaCeLiangActivity.this.getResources().getString(R.string.shebeiyichang01));
				}
				break;
			case 12://经典蓝牙测量中数据
				byte[] result2 = (byte[]) msg.obj;
				int num = 0;
				/*numPosition = result2[5];
				if (numPosition > 255) {
					numPosition = (((byte)numPosition)&0xff)+1+0xff;
				}*/
				num = (result2[5]&0xff)|(result2[6]<<8&0xff00);
				xueya_celiang_roundProgressBar.setFlag(true);
				xueya_celiang_roundProgressBar.setTextContent(String
						.valueOf(num));
				xueya_celiang_roundProgressBar.setProgress(num);
				break;
			case 13://经典蓝牙测量结果
				byte[] res = (byte[]) msg.obj;
				getNew = (res[5]&0xff) + 30;
				maibo = res[4]&0xff;

				int i = res[6];
				if(((i & 0XFF)>0)||(i & 0XFF)<256){
					down = (i & 0XFF) + 30;
				}else{
					down = (((byte)i) & 0XFF)+1+0xff + 30;
				}
				xueya_celiang_roundProgressBar.setFlag(false);
				xueya_celiang_roundProgressBar.setTextContent(String
						.valueOf(getNew));
				xueya_celiang_roundProgressBar.setDown(down);
				xueya_celiang_roundProgressBar.setProgress(getNew);
				handler.sendEmptyMessage(0);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	private boolean quxiao;
	private IscianService mBluetoothService;
	private boolean isBle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO onCreate
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xueya_celiang);
		Bundle bundle = getIntent().getParcelableExtra("bundle");
		device = bundle.getParcelable("device");
		isBle = getIntent().getBooleanExtra("bluetooth",false);
		// 获取Http工具类
		init();
		bindService();
		setIsHuaDong(false);
		getSuggesstion();
		th = new Thread(new Runnable() {

			@Override
			public void run() {
				if (isContinue) {
					try {
						connect(device);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		if(isBle){
			bindService();
		}else{
			th.start();
		}
	}
	private void bindService() {
		Intent bindIntent = new Intent(this, IscianService.class);
		this.bindService(bindIntent, mFhrSCon, Context.BIND_AUTO_CREATE);
	}

	private void unbindService() {
		this.unbindService(mFhrSCon);
	}

	private ServiceConnection mFhrSCon = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBluetoothService = ((IscianService.BluetoothBinder) service).getService();
			mBluetoothService.setBleMessageCallback(XueYaCeLiangActivity.this);
			mBluetoothService.startMeasure();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mBluetoothService = null;
		}
	};
	/**
	 * TODO 初始化
	 */
	private void init() {
		fh = new FinalHttp();
		fh.configTimeout(15000);
		num = new ArrayList<String>();
		listNum = new ArrayList<byte[]>();
		gson = new Gson();
		ExampleApplication.getInstance().addActivity(this);
		search_leftLayout = (LinearLayout) this
				.findViewById(R.id.search_leftLayout);
		search_titleText = (TextView) this.findViewById(R.id.search_titleText);
		search_leftLayout.setOnClickListener(this);
		search_titleText.setText(getResources().getString(R.string.home_content_rb_0));
		xueya_celiang_roundProgressBar = (CustomView) this
				.findViewById(R.id.xueya_celiang_roundProgressBar);
		xueya_celiang_roundProgressBar.setTextColor(getResources().getColor(
				R.color.white));
		xueya_celiang_roundProgressBar.setTextColor2(getResources().getColor(
				R.color.white));

		xueya_celiang_roundProgressBar.setOnClickListener(this);
		xueya_celiang_rl = (RelativeLayout) this
				.findViewById(R.id.xueya_celiang_rl);
		xueya_celiang_view = (LinearLayout) this
				.findViewById(R.id.xueya_celiang_view);
		xueya_celiang_suggestion = (TextView) this
				.findViewById(R.id.xueya_celiang_suggestion);
		celiang_bg = (ImageView) this.findViewById(R.id.celiang_bg);
		InputStream is = this.getResources().openRawResource(
				R.drawable.xueya_history_data_bg);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = 2;
		btp = BitmapFactory.decodeStream(is, null, options);
		celiang_bg.setScaleType(ScaleType.FIT_XY);
		celiang_bg.setImageBitmap(btp);

		xueya_celiang_jieshu = (Button) findViewById(R.id.xueya_celiang_jieshu);
		xueya_celiang_jieshu.setOnClickListener(this);
		xueya_celiang_chakan = (Button) findViewById(R.id.xueya_celiang_chakan);
		xueya_celiang_chakan.setOnClickListener(this);
		xueya_celiang_roundProgressBar.setTextContent2("mmHg");
		strA = this.getResources().getString(R.string.xueyajizhengzaiceliang);
	}

	@Override
	public void onClick(View v) {
		// TODO 点击事件监听
		switch (v.getId()) {
		case R.id.search_leftLayout:
			if(isBle){
				mBluetoothService.cancelMeasure();
				mBluetoothService.closeConnect();
			}else{
				closeTh = new Thread(new Runnable() {

					@Override
					public void run() {
						closeSocket2();
					}
				});
				closeTh.start();
			}
			finish();
			break;
		case R.id.xueya_celiang_roundProgressBar:
			break;
		case R.id.xueya_celiang_jieshu:// 测量中途结束
			if(isBle){
				mBluetoothService.cancelMeasure();
				mBluetoothService.closeConnect();
				finish();
			}else{
				closeTh = new Thread(new Runnable() {

					@Override
					public void run() {
						closeSocket2();
					}
				});
				closeTh.start();
			}
			break;
		case R.id.xueya_celiang_chakan:// 测量完点击查看
			Intent intent = new Intent(XueYaCeLiangActivity.this,
					XueYaCeLiangDataActivity.class);
			intent.putExtra("num", 1);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		if(isBle){
			mBluetoothService.cancelMeasure();
			mBluetoothService.closeConnect();
		}else{
			closeTh = new Thread(new Runnable() {

				@Override
				public void run() {
					closeSocket2();
				}
			});
			closeTh.start();
		}
	}

	/**
	 * TODO 连接设备
	 * 
	 * @param device
	 * @throws IOException
	 */
	@SuppressLint("NewApi")
	private void connect(BluetoothDevice device) throws IOException {
		// 固定的UUID
		final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
		UUID uuid = UUID.fromString(SPP_UUID);
		socket = device.createRfcommSocketToServiceRecord(uuid);
		if (!socket.isConnected()) {
			socket.connect();
		}
		if (socket.isConnected()) {
			isSuccess = true;
		} else {
			isSuccess = false;
			socket.connect();
		}
		quxiao = getIntent().getBooleanExtra("quxiao", false);
		System.out.println("***********重新測量**********"+quxiao);
		if(quxiao){
			quxiao = false;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					OutputStream os ; 
					try {
						os = socket.getOutputStream();
							byte[] a = { (byte) 0xff, (byte) 0xff, 0x05, 0x04,
									(byte) 0xfa };
							os.write(a, 0, a.length);
						os.write(a, 0, a.length);
						       

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		Thread th = new Thread(new Runnable() {

			@Override
			public void run() {
				// 发送启动命令
				OutputStream os;
				try {
					os = socket.getOutputStream();
					byte[] b = { (byte) 0xff, (byte) 0xff, 0x05, 0x01,
							(byte) 0xfa };
					os.write(b, 0, b.length);
					

				} catch (IOException e) {
					e.printStackTrace();
				}
				InputStream is;
				try {
					is = socket.getInputStream();
					// 定义一个数据，来接收数据
					byte[] buffer = new byte[255];
					// 定义int型变量，代表字节数
					int bytes = 0;
					if (is == null) {
						prompt(XueYaCeLiangActivity.this.getResources().getString(R.string.duqushujushibai));
						return;
					}
					// 执行循环
					while (true) {
						try {
							if (socket != null) {
								// 如果读取的字节数大于0，表示有数据存在
								bytes = is.read(buffer);
								System.out.println("AAAAAAA--------->>>>>>>>>>"
										+ bytes);
								if (bytes > 0 && bytes == 73) {
									// 定义新的字符数据，长度为读取的数据的长度
									byte[] byte_data = new byte[bytes];
									// 通过循环将buffer里面的数据复制到byte_data数组中
									for (int i = 0; i < bytes; i++) {
										byte_data[i] = buffer[i];
									}
									// 将byte_data数组转换成字符串
									listNum.add(byte_data);
									if (listNum != null) {
										byte[] result = listNum.get(0);
										if (result[2] == 73 && result[3] == 3) {
											// 测量返回成功关闭流
											try {
												is.close();
											} catch (IOException ae) {
												ae.printStackTrace();
											}
											socket.close();
											// dialog.cancel();
											Message msg = handler
													.obtainMessage();
											msg.what = 13;
											msg.obj = result;
											isContinue = false;
											handler.sendMessage(msg);
											break;
										}
									}
								} else if (bytes > 0 && bytes == 6) {
									// 定义新的字符数据，长度为读取的数据的长度
									byte[] byte_data = new byte[bytes];
									// 通过循环将buffer里面的数据复制到byte_data数组中
									for (int i = 0; i < bytes; i++) {
										byte_data[i] = buffer[i];
									}
									String abc = "";
									for (int j = 0; j < byte_data.length; j++) {
										abc += byte_data[j];
									}
									System.out
											.println("CCCCCCCCCCCCCCCCCCCCC----------->>>>>"
													+ abc);
									if (byte_data[2] == 6 && byte_data[3] == 7) {
										try {
											is.close();
										} catch (IOException ae) {
											ae.printStackTrace();
										}
										socket.close();
										// dialog.cancel();
										if (byte_data[4] == 0) {

											handler.sendEmptyMessage(4);
										} else if (byte_data[4] == 1) {
											handler.sendEmptyMessage(5);
										} else if (byte_data[4] == 2) {
											handler.sendEmptyMessage(6);
										} else {
											handler.sendEmptyMessage(2);
										}
										break;
									}
									// else{
									// try {
									// is.close();
									// } catch (IOException ae) {
									// ae.printStackTrace();
									// }
									// socket.close();
									// handler.sendEmptyMessage(3);
									// }
								} else if (bytes > 0 && bytes == 10) {
									// 定义新的字符数据，长度为读取的数据的长度
									byte[] byte_data = new byte[bytes];
									// 通过循环将buffer里面的数据复制到byte_data数组中
									for (int i = 0; i < bytes; i++) {
										byte_data[i] = buffer[i];
									}

									Message msg = handler.obtainMessage();
									msg.what = 12;
									msg.obj = byte_data;
									isContinue = false;
									handler.sendMessage(msg);

								} else if (bytes > 0 && bytes == 5) {
									// 定义新的字符数据，长度为读取的数据的长度
									byte[] byte_data = new byte[bytes];
									// 通过循环将buffer里面的数据复制到byte_data数组中
									for (int i = 0; i < bytes; i++) {
										byte_data[i] = buffer[i];
									}
									if (byte_data[3] == 4) {
										try {
											is.close();
										} catch (IOException ae) {
											ae.printStackTrace();
										}
										socket.close();
										handler.sendEmptyMessage(7);

									}
								}

							}
						} catch (IOException a) {
							try {
								is.close();
							} catch (IOException ae) {
								ae.printStackTrace();
							}
							break;
						}
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		th.start();
	}

	/***
	 * TODO 合并字节数组
	 * 
	 * @param a
	 * @return
	 */
	public static byte[] mergeArray(byte[]... a) {
		// 合并完之后数组的总长度
		int index = 0;
		int sum = 0;
		for (int i = 0; i < a.length; i++) {
			sum = sum + a[i].length;
		}
		byte[] result = new byte[sum];
		for (int i = 0; i < a.length; i++) {
			int lengthOne = a[i].length;
			if (lengthOne == 0) {
				continue;
			}
			// 拷贝数组
			System.arraycopy(a[i], 0, result, index, lengthOne);
			index = index + lengthOne;
		}
		return result;
	}

	/**
	 * TODO 血压测量增添数据
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setData(String URL, AjaxParams params) {

		fh.post(URL, params, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				xueya_celiang_jieshu.setVisibility(View.GONE);
				xueya_celiang_chakan.setVisibility(View.VISIBLE);
//				Toast.makeText(XueYaCeLiangActivity.this, strMsg,
//						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(Object t) {
				String content = (String) t;
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() != 1) {
					prompt(common.getMsg());
					return;
				}
				// 显示点击查看按钮
				xueya_celiang_jieshu.setVisibility(View.GONE);
				xueya_celiang_chakan.setVisibility(View.VISIBLE);
				Intent intent = new Intent(XueYaCeLiangActivity.this,
						XueYaCeLiangDataActivity.class);
				intent.putExtra("num", 1);
				intent.putExtra("isFirst", false);
				intent.putExtra("celiang", true);
				startActivity(intent);
				finish();
			}
		});
	}

	/**
	 * TODO 此功能暂未开放
	 * 
	 * @param context
	 */
	public void getNoMessage() {
		CustomDialog.Builder builder = new Builder(this);
		builder.setTitle(R.string.prompt);
		builder.setMessage(this.getResources().getString(R.string.shebeilianjieyichang));
		// 确定按钮点击事件处理
		builder.setPositiveButton(this.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				startActivity(new Intent(XueYaCeLiangActivity.this,
						HomeActivity.class));
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
				finish();
			}
		});
		builder.create().show();
	}

	/**
	 * TODO 设备超时
	 * 
	 * @param context
	 */
	public void getTimeOut(String message) {
		CustomDialog.Builder builder = new Builder(this);
		builder.setTitle(R.string.prompt);
		builder.setMessage(message);
		// 确定按钮点击事件处理
		builder.setPositiveButton(this.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// startActivity(new Intent(XueYaCeLiangActivity.this,
				// HomeActivity.class));
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
				finish();
			}
		});
		builder.create().show();
	}

	// /**
	// * TODO 结束测量，并跳转首页
	// */
	// private void closeSocket() {
	// try {
	// if (socket != null||socket.getInputStream()!=null) {
	// socket.getInputStream().close();
	// socket.close();
	// startActivity(new Intent(XueYaCeLiangActivity.this,
	// HomeActivity.class));
	// finish();
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// }

	/**
	 * TODO 结束测量，并跳转首页
	 */
	@SuppressLint("NewApi")
	private void closeSocket2() {
		if (socket != null) {
			if (!socket.isConnected()) {
				try {
					socket.connect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (socket.isConnected()) {
				isSuccess = true;
			} else {
				isSuccess = false;
			}

			Thread th = new Thread() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					// 发送启动命令
					OutputStream os;
					try {
						os = socket.getOutputStream();
						byte[] b = { (byte) 0xff, (byte) 0xff, 0x05, 0x04,
								(byte) 0xf7 };
						os.write(b, 0, b.length);
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (handler != null) {
						handler.sendEmptyMessage(10);
					}

				}
			};
			th.start();
		}
	}

	/**
	 * TODO 结束测量，并跳转首页
	 */
	@SuppressLint("NewApi")
	private void getManager(BluetoothDevice device) {
		// if (!socket.isConnected()) {
		// try {
		// socket.connect();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// if (socket.isConnected()) {
		// isSuccess = true;
		// } else {
		// isSuccess = false;
		// }

		Thread th = new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				// 发送启动命令
				OutputStream os;
				try {
					os = socket.getOutputStream();
					// 获取机器电量
					byte[] b2 = { (byte) 0xff, (byte) 0xff, 0x05, 0x06,
							(byte) 0xf5 };
					os.write(b2, 0, b2.length);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		th.start();
	}

	private void getSuggesstion() {
		xueya_celiang_view.setVisibility(View.VISIBLE);
		translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				-1f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f);
		translateAnimation.setRepeatCount(0);
		translateAnimation.setDuration(2000);
		xueya_celiang_view.startAnimation(translateAnimation);
		translateAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				xueya_celiang_rl.setVisibility(View.INVISIBLE);
				xueya_celiang_suggestion.setText("");
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
		xueya_celiang_rl.setVisibility(View.VISIBLE);
		Animation aa = new AlphaAnimation(0f, 1.0f);
		aa.setDuration(1000);
		xueya_celiang_rl.startAnimation(aa);
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
						for (int i = 0; !TextUtils.isEmpty(strA)
								&& i < strA.length(); i++) {
							Message msg = handler.obtainMessage();
							msg.what = 9;
							msg.arg1 = i;
							try {
								if (handler != null) {
									handler.sendMessage(msg);
									Thread.sleep(100);
									if (!TextUtils.isEmpty(strA)) {
										if (i == strA.length() - 1)
											dialogAndTextThread = null;
									}
								}
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

						}
					}
				});
				dialogAndTextThread.start();
			}
		});
	}

	@Override
	public void onNotifyDataChanged(byte[] data) {

	}

	private static int j = 0;
	@Override
	public void onMeasuringDataUpdate(MeasuringData data) {
		Message msg = handler.obtainMessage();
		msg.obj = data.toString();
		if (data.toString().contains("错误信息")) {
			if(j==0){
				++j;
				msg.what = 11;
				handler.sendMessage(msg);
			}
		} else {
			msg.what = 8;
			handler.sendMessage(msg);
		}
	}
	private static int i = 0;
	@Override
	public void onMeasureResultUpdate(MeasureResult data) {
        Log.i("ss","********************************"+i);
		if(i==0){
			++i;
			Message msg = handler.obtainMessage();
			msg.what = 1;
			msg.obj = data.getData();
			handler.sendMessage(msg);
		}
	}

	@Override
	public void onFailure(BleException exception) {

	}

	@Override
	public void onCancel() {

	}

	/**
	 * @author zhaow 倒计时的计时器
	 */
	class Timecount extends CountDownTimer {

		public Timecount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@SuppressLint("NewApi")
		@Override
		public void onTick(long millisUntilFinished) {

		}

		@Override
		public void onFinish() {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						getManager(device);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	/**
	 * @author zhaow 倒计时的计时器
	 */
	class TimeManager extends CountDownTimer {

		public TimeManager(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@SuppressLint("NewApi")
		@Override
		public void onTick(long millisUntilFinished) {

		}

		@Override
		public void onFinish() {
			try {
				if (socket != null) {
					socket.close();
				}
				socket = null;
				closeTh = null;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		i = 0;
		if (mBluetoothService != null){
			unbindService();
			mBluetoothService.cancelMeasure();
			mBluetoothService.closeConnect();
		}
		super.onDestroy();
		dialogAndTextThread = null;
		celiang_bg = null;
		th = null;
		search_leftLayout = null;
		search_titleText = null;
		xueya_celiang_roundProgressBar = null;
		device = null;
		num = null;
		listNum = null;
		gson = null;
		xueya_celiang_jieshu = null;
		xueya_celiang_chakan = null;
		translateAnimation = null;
		xueya_celiang_rl = null;
		xueya_celiang_view = null;
		xueya_celiang_suggestion = null;
		dialogAndTextThread = null;
		strA = null;
		fh = null;
		th = null;
		handler = null;
		btp.recycle();
		setContentView(R.layout.xml_null);
		System.gc();
	}
}
