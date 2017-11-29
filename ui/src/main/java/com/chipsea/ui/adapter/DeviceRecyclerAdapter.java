package com.chipsea.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chipsea.code.listener.DeviceRecyclerviewCallback;
import com.chipsea.ui.R;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;

import java.util.ArrayList;
import java.util.List;

public class DeviceRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int          TYPE1 = 1;
    private static final int          TYPE2 = 2;
    private static final int          TYPE3 = 3;
    private              List<Object> datas = new ArrayList<>();
    private Context                    context;
    private DeviceRecyclerviewCallback callback;
    private List<String>               mProductKeyList;

    public void setDatas(List<Object> datas) {
        if (datas != null) {
            this.datas.clear();
            this.datas.addAll(datas);
        }
        notifyDataSetChanged();
    }

    public DeviceRecyclerAdapter(Context context, DeviceRecyclerviewCallback callback, List<String> productKeyList) {
        this.context = context;
        this.callback = callback;
        this.mProductKeyList = productKeyList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE1) {
            View view = LayoutInflater.from(context).inflate(R.layout.device_item_top, parent, false);
            return new TopHolder(view);
        } else if (viewType == TYPE3) {
            View view = LayoutInflater.from(context).inflate(R.layout.device_item_bottom, parent, false);
            return new BottomHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.device_item, parent, false);
            return new MiddleHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == datas.size() - 1) {
            return TYPE3;
        } else if (datas.get(position) instanceof String) {
            return TYPE1;
        } else {
            return TYPE2;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder tempHolder, final int position) {
        if (tempHolder instanceof TopHolder) {
            TopHolder topHolder = (TopHolder) tempHolder;
            topHolder.typeName.setText(datas.get(position) + "");
        } else if (tempHolder instanceof MiddleHolder) {
            MiddleHolder        middleHolder = (MiddleHolder) tempHolder;
            final GizWifiDevice deviceInfo   = (GizWifiDevice) datas.get(position);

            if (deviceInfo.isBind()) {
                String deviceAlias = deviceInfo.getAlias();
                String devicePN    = deviceInfo.getProductName();
                middleHolder.elecNumber.setText(deviceInfo.getMacAddress());
                if(deviceInfo.isLAN()){
                    middleHolder.tvNet.setText(context.getString(R.string.lan));
                }else{
                    middleHolder.tvNet.setText(context.getString(R.string.no_lan));
                }
                if (TextUtils.isEmpty(deviceAlias)) {
                    middleHolder.deviceName.setText(devicePN);
                } else {
                    middleHolder.deviceName.setText(deviceAlias);
                }
                if (deviceInfo.getProductKey().equals(mProductKeyList.get(0))) {
                    middleHolder.typeIcon.setImageResource(R.mipmap.device1);
                } else if (deviceInfo.getProductKey().equals(mProductKeyList.get(1))) {
                    middleHolder.typeIcon.setImageResource(R.mipmap.device2);
                } else if (deviceInfo.getProductKey().equals(mProductKeyList.get(2))) {
                    middleHolder.typeIcon.setImageResource(R.mipmap.device3);
                }

                if (deviceInfo.getNetStatus() == GizWifiDeviceNetStatus.GizDeviceOnline
                        || deviceInfo.getNetStatus() == GizWifiDeviceNetStatus.GizDeviceControlled) {
                    middleHolder.stateText.setBackgroundResource(R.drawable.await_solid_conrners);
                    middleHolder.stateText.setText(context.getString(R.string.mainOnline));
                } else {
                    middleHolder.stateText.setBackgroundResource(R.drawable.offline_solid_conrners);
                    middleHolder.stateText.setText(context.getString(R.string.mainOffineing));
                }
                middleHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (callback != null) {
                            callback.onDeviceItem(deviceInfo);
                        }
                    }
                });
            }
        } else {
            BottomHolder bottomHolder = (BottomHolder) tempHolder;
            bottomHolder.addDeviceBto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.addDevice();
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    static class TopHolder extends RecyclerView.ViewHolder {
        TextView typeName;

        public TopHolder(View itemView) {
            super(itemView);
            typeName = (TextView) itemView.findViewById(R.id.typeName);
        }
    }

    static class MiddleHolder extends RecyclerView.ViewHolder {
        LinearLayout itemLayout;
        ImageView    typeIcon;
        TextView     deviceName;
        TextView     elecNumber;
        TextView     tvNet;
        TextView     stateText;

        public MiddleHolder(View itemView) {
            super(itemView);
            itemLayout = (LinearLayout) itemView.findViewById(R.id.itemLayout);
            typeIcon = (ImageView) itemView.findViewById(R.id.typeIcon);
            deviceName = (TextView) itemView.findViewById(R.id.deviceName);
            elecNumber = (TextView) itemView.findViewById(R.id.elecNumber);
            tvNet = (TextView) itemView.findViewById(R.id.tv_net);
            stateText = (TextView) itemView.findViewById(R.id.stateText);
        }
    }

    static class BottomHolder extends RecyclerView.ViewHolder {
        TextView addDeviceBto;

        public BottomHolder(View itemView) {
            super(itemView);
            addDeviceBto = (TextView) itemView.findViewById(R.id.addDeviceBto);
        }
    }
}
