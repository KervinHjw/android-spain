package com.en.scian;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;

public class MyService extends Service{
	private int num;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@SuppressLint("NewApi") @Override
	public void onTrimMemory(int level) {
		// TODO Auto-generated method stub
		
		Log.e("AAAAAAAAAAAA--------------->>>>>>>>>>>", String.valueOf(level));
		if(level==TRIM_MEMORY_RUNNING_MODERATE ){
			num++;
			LineChart mchart = new LineChart(this);
			mchart.removeAllViews();
			mchart.clearValues();
			mchart.clear();
			
			Log.e("AAAAAAAAAAAA--------------->>>>>>>>>>>", String.valueOf(num));
			System.out.println("AAAAAAAAAAAA--------------->>>>>>>>>>>"+String.valueOf(num));
		}
		super.onTrimMemory(level);
	}

}
