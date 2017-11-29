package com.chipsea.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chipsea.code.listener.EditDeviceCallback;
import com.chipsea.code.util.LogUtil;
import com.chipsea.code.util.PrefsUtil;
import com.chipsea.mode.entity.AccountInfo;
import com.chipsea.mode.entity.DeviceInfo;
import com.chipsea.ui.R;
import com.gizwits.gizwifisdk.api.GizWifiDevice;

import java.util.List;

import chipsea.wifiplug.lib.GosDeploy;

import static com.gizwits.gizwifisdk.enumration.GizDeviceSharingUserRole.GizDeviceSharingNormal;
import static com.gizwits.gizwifisdk.enumration.GizDeviceSharingUserRole.GizDeviceSharingOwner;
import static com.gizwits.gizwifisdk.enumration.GizDeviceSharingUserRole.GizDeviceSharingSpecial;

/**
 * Created by xulj on 2016/9/8.
 */
public class MyDeviceRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<GizWifiDevice> deviceInfos;
    private Context             context;
    private EditDeviceCallback  editDeviceCallback;
    private AccountInfo         mAccountInfo;
    private List<String>        mProductKeyList;

    public MyDeviceRecyclerAdapter(Context context, List<GizWifiDevice> deviceInfos, EditDeviceCallback
            editDeviceCallback) {
        this.context = context;
        this.deviceInfos = deviceInfos;
        this.editDeviceCallback = editDeviceCallback;
        this.mAccountInfo = PrefsUtil.getInstance(context).getAccountInfo();
        mProductKeyList = GosDeploy.setProductKeyList();
    }

    public void upDatas(List<GizWifiDevice> deviceInfos){
        this.deviceInfos = deviceInfos;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_device_recyclerview_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder tempHolder, final int position) {
        MyHolder            myHolder   = (MyHolder) tempHolder;
        final GizWifiDevice deviceInfo = deviceInfos.get(position);

        String deviceAlias = deviceInfo.getAlias();
        String devicePN    = deviceInfo.getProductName();
        if (TextUtils.isEmpty(deviceAlias)) {
            myHolder.deviceName.setText(devicePN);
        } else {
            myHolder.deviceName.setText(deviceAlias);
        }
        myHolder.deviceMac.setText("MAC:" + deviceInfo.getMacAddress());
        if (deviceInfo.getProductKey().equals(mProductKeyList.get(0))) {
            myHolder.deviceIcon.setImageResource(R.mipmap.device1);
        } else if (deviceInfo.getProductKey().equals(mProductKeyList.get(1))) {
            myHolder.deviceIcon.setImageResource(R.mipmap.device2);
        } else if (deviceInfo.getProductKey().equals(mProductKeyList.get(2))) {
            myHolder.deviceIcon.setImageResource(R.mipmap.device3);
        }
        myHolder.deviceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editDeviceCallback != null) {
                    editDeviceCallback.onEditDeviceName(deviceInfo);
                }
            }
        });
        myHolder.deleteDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editDeviceCallback != null) {
                    editDeviceCallback.deleteDevice(deviceInfo);
                }
            }
        });

        LogUtil.d("当前角色：" + deviceInfo.getSharingRole() + " " + GizDeviceSharingSpecial);
        if (deviceInfo.getSharingRole() == GizDeviceSharingOwner ||
                deviceInfo.getSharingRole() == GizDeviceSharingSpecial) {
            myHolder.managerBto.setVisibility(View.VISIBLE);
        }
        myHolder.managerBto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editDeviceCallback != null) {
                    editDeviceCallback.managerDevice(deviceInfo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceInfos.size();
    }


    static class MyHolder extends RecyclerView.ViewHolder {
        ImageView deviceIcon;
        TextView  deviceName;
        TextView  deviceMac;
        TextView  managerBto;
        TextView  restartMatching;
        TextView  sendCode;
        ImageView deleteDevice;

        public MyHolder(View itemView) {
            super(itemView);
            deviceIcon = (ImageView) itemView.findViewById(R.id.deviceIcon);
            deviceName = (TextView) itemView.findViewById(R.id.deviceName);
            deviceMac = (TextView) itemView.findViewById(R.id.deviceMac);
            managerBto = (TextView) itemView.findViewById(R.id.managerBto);
            restartMatching = (TextView) itemView.findViewById(R.id.restartMatching);
            sendCode = (TextView) itemView.findViewById(R.id.sendCode);
            deleteDevice = (ImageView) itemView.findViewById(R.id.deleteDevice);
        }
    }

}
