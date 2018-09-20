package com.ludees.scian.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.util.EncodingUtils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Vibrator;
import android.view.WindowManager;
/**
 * 
* @ClassName: MyUtil 
* @Description: TODO
* @author zhaoW
* @date 2015骞?1鏈?5鏃? 涓婂崍12:04:04 
*
 */
public class MyUtil {

	/** 浣滅敤: SD鍗℃牴鐩綍 */
	public static final String PATH = Environment.getExternalStorageDirectory().getPath();
	/** 浣滅敤: 鏁版嵁鏂囦欢澶? */
	public static final String PATH_ICON = PATH + "/xiaoqu/img/";

	/**
	 * 鍔熻兘鎻忚堪: MD5鍔犲瘑
	 */
	public static String MD5(String inStr) {

		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return null;
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];

		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}

		String strMD5_32 = hexValue.toString();

		return strMD5_32.toUpperCase();
	}

	/** Get the STB MacAddress */
	public static String getMacAddress(Context c) {

		try {
			return loadFileAsString("/sys/class/net/eth0/address")
					.toUpperCase().substring(0, 17);
		} catch (IOException e) {
			e.printStackTrace();
			WifiManager wifi = (WifiManager) c
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();
			return info.getMacAddress();
		}
	}
/**
 * 
* @Title: loadFileAsString 
* @Description: TODO
* @param @param filePath
* @param @return
* @param @throws java.io.IOException
* @return String
* @throws
 */
	private static String loadFileAsString(String filePath)
			throws java.io.IOException {

		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
		}
		reader.close();
		return fileData.toString();
	}
/**
 * 
* @Title: writeFileSdcard 
* @Description: TODO
* @param @param fileName
* @param @param message
* @return void
* @throws
 */
	public static void writeFileSdcard(String fileName, String message) {

		try {

			FileOutputStream fout = new FileOutputStream(fileName);

			byte[] bytes = message.getBytes();

			fout.write(bytes);
			fout.close();

		}

		catch (Exception e) {

			e.printStackTrace();

		}

	}
/**
 * 
* @Title: writeFileSdcard 
* @Description: TODO
* @param @param fileName
* @param @param bytes
* @return void
* @throws
 */
	public static void writeFileSdcard(String fileName, byte[] bytes) {

		try {

			FileOutputStream fout = new FileOutputStream(fileName);

			fout.write(bytes);

			fout.close();

		}

		catch (Exception e) {

			e.printStackTrace();

		}

	}
/**
 * 
* @Title: readFileSdcard 
* @Description: TODO
* @param @param fileName
* @param @return
* @return String
* @throws
 */
	public static String readFileSdcard(String fileName) {

		String res = "";

		try {

			FileInputStream fin = new FileInputStream(fileName);

			int length = fin.available();

			byte[] buffer = new byte[length];

			fin.read(buffer);

			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();

		}

		catch (Exception e) {

			e.printStackTrace();

		}

		return res;

	}

	/**
	 * 鍔熻兘鎻忚堪: 鍒涘缓鏂囦欢澶?
	 */
	public static void createFile(String name, Context c) {

		if (!isHavaSDCard()) {
			return;
		}
		File dir = new File(name);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	/**
	 * 鍔熻兘鎻忚堪: 鍒犻櫎鏂囦欢鎴栨枃浠跺す
	 */
	public static void deleteFile(File file) {

		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFile(files[i]);
				}
			}
			file.delete();
		}
	}

	/**
	 * 鍔熻兘鎻忚堪: 鏄惁鏈塖D鍗?
	 */
	public static boolean isHavaSDCard() {

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		}

		return false;
	}

	/***************************************************************************
	 * 浣滅敤: 璇婚厤缃枃浠? 杩斿洖鍐呭
	 **************************************************************************/
	public static final String readPreferences(Context context1, String dataName) {

		String data = context1.getSharedPreferences("MYDATA",
				Context.MODE_PRIVATE).getString(dataName, null);

		return data;

	}

	/***************************************************************************
	 * 浣滅敤: 鍐欓厤缃枃浠?
	 **************************************************************************/
	public static final void writePreferences(Context context1,
			String dataName, String data) {

		SharedPreferences settings = context1.getSharedPreferences("MYDATA",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(dataName, data);
		editor.commit();

	}

	/**
	 * 鍒ゆ柇瀛楁暟涓嶅浜?200瀛?
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {

		Pattern p = Pattern.compile("/^.{1,200}$/");

		Matcher m = p.matcher(mobiles);

		return m.matches();

	}
/**
 * 
* @Title: loadImageFromUrl 
* @Description: TODO
* @param @param url
* @param @param path_name
* @param @return
* @return Drawable
* @throws
 */
	public static Drawable loadImageFromUrl(String url, String path_name) {

		URL m;
		InputStream i = null;
		try {
			m = new URL(url);
			i = (InputStream) m.getContent();

			File myFileTemp = new File(path_name);
			if (!myFileTemp.exists()) {

				FileOutputStream fos = new FileOutputStream(myFileTemp);

				byte[] b = new byte[1024];
				int aa;
				while ((aa = i.read(b)) != -1) {
					fos.write(b, 0, aa);
				}

				fos.close();
				i.close();
			}
			return Drawable.createFromPath(path_name);

		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
/**
 * 
* @Title: convertStreamToString 
* @Description: TODO
* @param @param is
* @param @return
* @return String
* @throws
 */
	public static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return sb.toString();

	}

	/**
	 * 鍔熻兘鎻忚堪: 闇囧姩
	 */
	public static void Vibrate(Context activity, long milliseconds) {

		Vibrator vib = (Vibrator) activity
				.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}

	/**
	 * 鍔熻兘浠嬬粛:鏍规嵁闂撮殧瀛楃灏嗕竴瀛楃涓插垏鎴愭暟缁? 杈撳叆鍙傛暟:瑕佸垏鐨勫瓧绗︿覆,闂撮殧瀛楃 杈撳嚭鍙傛暟:瀛楃涓叉暟缁?
	 **************************************************************************/
	public static final String[] split(String str, String pre) {

		if (str == null || str.length() < 1)
			return null;

		if (!str.contains(pre)) {
			String[] arr = new String[1];
			arr[0] = str;
			return arr;
		}

		Vector<String> veTmp = new Vector<String>();
		while (str.indexOf(pre) != -1) {// 鍙栧緱姣忎釜鏍囧織鍓嶉潰鐨勫瓧绗﹀唴瀹?
			veTmp.addElement(str.substring(0, str.indexOf(pre)));
			str = str.substring(str.indexOf(pre) + 1, str.length());
		}
		if (str.length() > 0) {// 鏈?鍚庝竴涓爣蹇楀悗杩樻湁瀛楃鍐呭鍒欎繚瀛?
			veTmp.addElement(str);
		}
		if (veTmp.size() == 0) {
			return null;
		}
		String arrTmp[] = new String[veTmp.size()];
		for (int i = 0; i < arrTmp.length; i++) {
			arrTmp[i] = (String) veTmp.elementAt(i);
		}
		veTmp = null;
		return arrTmp;
	}

	/**
	 * 鍔熻兘鎻忚堪: 鍒ゆ柇email鏍煎紡鏄惁姝ｇ‘
	 */
	public static boolean isEmail(String email) {

		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

		Pattern p = Pattern.compile(str);

		Matcher m = p.matcher(email);

		return m.matches();

	}
/**
 * 
* @Title: readBitMap 
* @Description: TODO
* @param @param context
* @param @param resId
* @param @return
* @return Bitmap
* @throws
 */
	public static Bitmap readBitMap(Context context, int resId) {

		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 鑾峰彇璧勬簮鍥剧墖
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}
/**
 * 
* @Title: daDianHua 
* @Description: TODO
* @param @param context
* @param @param mobile
* @return void
* @throws
 */
	public static void daDianHua(Context context, String mobile) {

		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:" + mobile));
		context.startActivity(intent);
	}
	/**
	 * 
	* @Title: daDianHua2 
	* @Description: TODO
	* @param @param context
	* @param @param mobile
	* @return void
	* @throws
	 */
	public static void daDianHua2(Context context, String mobile) {

		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_CALL);
		intent.setData(Uri.parse("tel:" + mobile));
		context.startActivity(intent);
	}
/**
 * 
* @Title: faDuanXin 
* @Description: TODO
* @param @param context
* @param @param mobile
* @return void
* @throws
 */
	public static void faDuanXin(Context context, String mobile) {

		Uri uri = Uri.parse("smsto:" + mobile);

		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);

		intent.putExtra("sms_body", "");

		context.startActivity(intent);
	}
/**
 * 
* @Title: faDuanXin 
* @Description: TODO
* @param @param context
* @param @param mobile
* @param @param msg
* @return void
* @throws
 */
	public static void faDuanXin(Context context, String mobile, String msg) {

		Uri uri = Uri.parse("smsto:" + mobile);

		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);

		intent.putExtra("sms_body", msg);

		context.startActivity(intent);
	}
	
	/**
	 * 鍒ゆ柇涓?涓澶囨槸鍚︽槸妯睆璁惧[AndroidPad].
	 * <p>
	 * whether a device is landscape device or not.
	 * 
	 * @param activity
	 * @return true 妯睆锛宖asle 绔栧睆
	 */
	public static boolean getLandscapeDevice(Context mContext) {
		int orientation = mContext.getResources().getConfiguration().orientation;
		WindowManager window = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		int displayRotation = window.getDefaultDisplay().getRotation();
//		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
//		screenResolution = dm.widthPixels + "x" + dm.heightPixels;
		return (orientation == Configuration.ORIENTATION_PORTRAIT && displayRotation % 2 != 0) || (orientation == Configuration.ORIENTATION_LANDSCAPE && displayRotation % 2 == 0);
	}

	/**
	 * 瀛楄妭娴佽浆鐮佹垚string
	 * @param in
	 * @return
	 */
	public static String getStrFromInputSteam(InputStream in){
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new InputStreamReader(in,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//鏈?濂藉湪灏嗗瓧鑺傛祦杞崲涓哄瓧绗︽祦鐨勬椂鍊? 杩涜杞爜
		StringBuffer buffer=new StringBuffer();
		String line = "";
		try {
			while((line=bf.readLine())!=null){
				buffer.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return buffer.toString();
	}
}
