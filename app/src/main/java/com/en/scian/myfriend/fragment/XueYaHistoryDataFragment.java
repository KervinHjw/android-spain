package com.en.scian.myfriend.fragment;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.en.scian.R;
import com.en.scian.adapter.XueYaHistoryDataAdapter;
import com.en.scian.entity.BloodPressure;
import com.en.scian.entity.HistoryBloodPressure;
import com.en.scian.entity.HistoryBlooePressureSecond;
import com.en.scian.entity.ResponseCommon;
import com.en.scian.entity.XueYaYearBase;
import com.en.scian.network.Urls;
import com.en.scian.util.SettingUtils;
import com.en.scian.util.ToastUtils;
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
	private Gson gson;
	private String URL = "";
	private String userId;
	private int num = 1;
	private FinalHttp fh;
	private String isFirstChart = "";

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO
		mContext = getActivity();

		fh = new FinalHttp();
		fh.configTimeout(15 * 1000);

		View view = inflater.inflate(R.layout.xueya_history_data, null);
		initView(view);
		return view;

	}

	private void initView(View view) {
		listview = (ListView) view
				.findViewById(R.id.xueya_history_data_listview);

		list = new ArrayList<XueYaYearBase>();
		gson = new Gson();
		adapter = new XueYaHistoryDataAdapter(getActivity(), list);
		listview.setAdapter(adapter);
	}

	private void setData() {
		userId = getActivity().getIntent().getStringExtra("myFriendId");
		num = Integer.valueOf(SettingUtils.get(getActivity(), "myFriendData",
				"0"));
		URL = Urls.GET_BLOOD_PRESSURE_HISTORY + "userId=" + userId + "&type="
				+ num + "&pageNo=0&pageSize=10";
		switch (num) {
		case 0:
			URL = Urls.GET_BLOOD_PRESSURE_HISTORY + "userId=" + userId
					+ "&type=" + 1 + "&pageNo=0&pageSize=10";
			setData(URL, true);
			break;
		case 1:
			URL = Urls.GET_BLOOD_PRESSURE_HISTORY + "userId=" + userId
					+ "&type=" + 1 + "&pageNo=0&pageSize=10";
			setData(URL, true);
			break;
		case 2:
			URL = Urls.GET_BLOOD_PRESSURE_HISTORY + "userId=" + userId
					+ "&type=" + 2 + "&pageNo=0&pageSize=10";
			setData(URL, true);
			break;
		case 3:
			URL = Urls.GET_BLOOD_PRESSURE_HISTORY + "userId=" + userId
					+ "&type=" + 3 + "&pageNo=0&pageSize=10";
			setData(URL, true);
			break;
		case 4:
			URL = Urls.GET_BLOOD_PRESSURE_HISTORY + "userId=" + userId
					+ "&type=" + 4 + "&pageNo=0&pageSize=10";
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
				SettingUtils.set(getActivity(), "isFirstFriendData", "2");
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

						}
					}
					if (b) {
						list.clear();
					}
					list.addAll(list2);
					adapter.notifyDataSetChanged();
				}
			}
		});
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isFirstChart = SettingUtils
				.get(getActivity(), "isFirstFriendData", "1");
		if (isFirstChart.equals("1")) {
			setData();
		}

	}

	@Override
	public void onDestroyView() {
		// TODO 清理对象数据
		mContext = null;
		list = null;
		adapter = null;
		listview = null;
		gson = null;
		URL = null;
		userId = null;
		fh = null;
		isFirstChart = null;
		super.onDestroyView();

	}
}
