package com.chipsea.code.listener;

import com.gizwits.gizwifisdk.api.GizWifiDevice;

/**
 * Created by lixun on 2016/6/21.
 */
public interface DeviceRecyclerviewCallback {
    /**
     * 成功后回调
     */
    public void onDeviceItem(GizWifiDevice gizWifiDevice);

    /**
     */
    public void addDevice();
}
