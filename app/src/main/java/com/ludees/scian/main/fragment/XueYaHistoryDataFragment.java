package com.ludees.scian.main.fragment;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.ludees.scian.R;
import com.ludees.scian.adapter.XueYaHistoryDataAdapter;
import com.ludees.scian.entity.BloodPressure;
import com.ludees.scian.entity.HistoryBloodPressure;
import com.ludees.scian.entity.HistoryBlooePressureSecond;
import com.ludees.scian.entity.ResponseCommon;
import com.ludees.scian.entity.XueYaYearBase;
import com.ludees.scian.network.Urls;
import com.ludees.scian.util.SettingUtils;
import com.ludees.scian.util.ToastUtils;
import com.ludees.scian.xueya.XueYaCeLiangDataActivity;
import com.google.gson.Gson;

/**
 * 血压历史数据
 * 
 * @author zhangp
 * 
 */
public class XueYaHistoryDataFragment extends Fragment {
	private Context mContext;
	private List<XueYaYearBase> list;
	private XueYaHistoryDataAdapter adapter;
	private ListView listview;
	private int num = 0;
	private Gson gson;
	private String URL = "";
	private String userId;

	private FinalHttp fh;
	private String isFirstChart = "";

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext = getActivity();

		View view = inflater.inflate(R.layout.xueya_history_data, null);
		initView(view);
		return view;

	}

	private void initView(View view) {
		fh = new FinalHttp();
		fh.configTimeout(15 * 1000);
		list = new ArrayList<XueYaYearBase>();
		gson = new Gson();
		listview = (ListView) view
				.findViewById(R.id.xueya_history_data_listview);
		adapter = new XueYaHistoryDataAdapter(getActivity(), list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				XueYaYearBase base = list.get(arg2);
				int bloodPressureId = base.getBloodPressureId();

				if (bloodPressureId != 0) {
					Intent intent = new Intent(getActivity(),
							XueYaCeLiangDataActivity.class);
					intent.putExtra("bloodPressureId", bloodPressureId);
					getActivity().startActivity(intent);
				}
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
			setData();
	}

	private void setData() {
		userId = SettingUtils.get(mContext, "userId", "");
		SettingUtils.set(getActivity(), "isFirstChart", "2");
		num = Integer.valueOf(SettingUtils.get(getActivity(), "myData", "1"));
		
		switch (num) {
		case 0:
			URL = Urls.GET_BLOOD_PRESSURE_HISTORY + "userId=" + userId
					+ "&type=1";
			setData(URL, true);
			break;
		case 1:
			URL = Urls.GET_BLOOD_PRESSURE_HISTORY + "userId=" + userId
					+ "&type=1";
			setData(URL, true);
			break;
		case 2:
			URL = Urls.GET_BLOOD_PRESSURE_HISTORY + "userId=" + userId
					+ "&type=2";
			setData(URL, true);
			break;
		case 3:
			URL = Urls.GET_BLOOD_PRESSURE_HISTORY + "userId=" + userId
					+ "&type=3";
			setData(URL, true);
			break;
		case 4:
			URL = Urls.GET_BLOOD_PRESSURE_HISTORY + "userId=" + userId
					+ "&type=4";
			setData(URL, true);
			break;
		default:
			break;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setData(String URL, final boolean b) {

		fh.get(URL, new AjaxCallBack() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				ToastUtils.TextToast(getActivity(), getResources().getString(R.string.fuwuqilianjieyichang));
			}

			@Override
			public void onSuccess(Object t) {
				// TODO
				super.onSuccess(t);
				String content = (String) t;
				ResponseCommon common = gson.fromJson(content,
						ResponseCommon.class);
				if (common.getStatus() == 0) {
					Toast.makeText(mContext, common.getMsg(),
							Toast.LENGTH_SHORT).show();
					return;
				}

				List<XueYaYearBase> list2 = new ArrayList<XueYaYearBase>();
				HistoryBloodPressure pressure = new HistoryBloodPressure();
				pressure = gson.fromJson(content, HistoryBloodPressure.class);
				if (pressure != null) {
					List<HistoryBlooePressureSecond> data = pressure.getData();
					if (data != null && data.size() > 0) {
						for (int i = 0; i < data.size(); i++) {
							HistoryBlooePressureSecond second = pressure
									.getData().get(i);
							String time = second.getYearTime();
							XueYaYearBase baseTime = new XueYaYearBase();
							baseTime.setYear(time);
							list2.add(baseTime);
							List<BloodPressure> bloodList = new ArrayList<BloodPressure>();
							bloodList = second.getBloodPressureList();
							for (int j = 0; j < bloodList.size(); j++) {
								BloodPressure blood = bloodList.get(j);
								XueYaYearBase base = new XueYaYearBase();
								base.setBloodPressureClose(blood
										.getBloodPressureClose());
								base.setBloodPressureOpen(blood
										.getBloodPressureOpen());
								base.setBloodPressureId(blood
										.getBloodPressureId());
								base.setPulse(blood.getPulse());
								base.setTime(blood.getMeasureTime());
								list2.add(base);
							}
							data.clear();
						}
					}
					if (b) {
						list.clear();
					}

					list.addAll(list2);
				}
				adapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		mContext = null;
		if (list != null) {
			list.clear();
			list = null;
		}
		adapter = null;
		listview = null;
		gson = null;
		URL = null;
		userId = null;
		fh = null;
		isFirstChart = null;
		System.gc();
		super.onDestroyView();
	}
}
