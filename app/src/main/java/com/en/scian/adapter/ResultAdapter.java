package com.en.scian.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clj.fastble.data.ScanResult;
import com.en.scian.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/17.
 */

public class ResultAdapter extends BaseAdapter {
    private Context context;
    private List<ScanResult> resultList;
    private List<String> nameList;
    public ResultAdapter(Context context){
        this.context = context;
        resultList = new ArrayList<ScanResult>();
        nameList = new ArrayList<String>();
    }
    public void addResult(ScanResult result){
        resultList.add(result);
        nameList.add(result.getDevice().getName());
    }
    public List<String> getResultNameList(){
        return nameList;
    }
    public void clear (){
        resultList.clear();
    }
    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public ScanResult getItem(int position) {
        if (position > resultList.size())
            return null;
        return resultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ResultAdapter.ViewHolder holder;
        if (convertView != null) {
            holder = (ResultAdapter.ViewHolder) convertView.getTag();
        } else {
            convertView = View.inflate(context, R.layout.equipment_item, null);
            holder = new ResultAdapter.ViewHolder();
            holder.txt_name = (TextView) convertView.findViewById(R.id.equipment_item_name);
            convertView.setTag(holder);
        }

        ScanResult result = resultList.get(position);
        BluetoothDevice device = result.getDevice();
        String name = device.getName();
        holder.txt_name.setText(name);
        return convertView;
    }
    class ViewHolder {
        TextView txt_name;
    }
}
