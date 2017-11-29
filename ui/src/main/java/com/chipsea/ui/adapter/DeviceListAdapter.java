package com.chipsea.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chipsea.code.util.LogUtil;
import com.chipsea.mode.entity.DeviceBind;
import com.chipsea.ui.R;
import com.chipsea.view.ScanningView;
import com.chipsea.view.text.CustomTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import chipsea.wifiplug.lib.GosDeploy;

/**
 * Created by hfei on 2015/10/19.
 */
public class DeviceListAdapter extends BaseAdapter {
    private       Context          context;
    public        List<DeviceBind> smartDevices;
    public        ScanningView     scanningView;
    private final List<String>     mProductKeyList;

    public DeviceListAdapter(Context context) {
        this.context = context;
        smartDevices = new ArrayList<DeviceBind>();
        mProductKeyList = GosDeploy.setProductKeyList();//DeviceListAdapter
    }


    @Override
    public int getCount() {
        return smartDevices.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (position == 0) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.device_list_top_layout, parent, false);
            }
            scanningView = (ScanningView) convertView.findViewById(R.id.scanView);
        } else {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.setting_device_list_item, parent, false);
                holder.deviceName = (CustomTextView) convertView.findViewById(R.id.deviceName);
                holder.deviceNo = (CustomTextView) convertView.findViewById(R.id.deviceNo);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            LogUtil.d(Arrays.toString(mProductKeyList.toArray()) + "    productKey:" + smartDevices.get(position - 1)
                    .productKey);
            String macAddress = smartDevices.get(position - 1).mac;
            if (mProductKeyList.get(0).equals(smartDevices.get(position - 1).productKey)) {
                holder.deviceName.setText(context.getString(R.string.device1_socket));
            } else if (mProductKeyList.get(1).equals(smartDevices.get(position - 1).productKey)) {
                holder.deviceName.setText(context.getString(R.string.device2_light));
            } else if (mProductKeyList.get(2).equals(smartDevices.get(position - 1).productKey)) {
                holder.deviceName.setText(context.getString(R.string.device3_code));
            } else {
                holder.deviceName.setText(context.getString(R.string.device_unknown));
            }
            holder.deviceNo.setText(context.getString(R.string.myDevicePhysicAddress) + macAddress);
            convertView.setTag(holder);
        }
        return convertView;
    }

    private class ViewHolder {
        CustomTextView deviceName;
        CustomTextView deviceNo;
    }

}
