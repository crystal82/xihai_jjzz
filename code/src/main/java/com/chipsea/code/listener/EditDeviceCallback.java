package com.chipsea.code.listener;

import com.gizwits.gizwifisdk.api.GizWifiDevice;

public interface EditDeviceCallback {
    /**
     * 编辑设备方法
     * @param deviceInfo
     */
    public void onEditDeviceName(GizWifiDevice deviceInfo);

    /**
     * 删除设备
     */
    public void deleteDevice(GizWifiDevice deviceInfo);

    public void bindDevice(GizWifiDevice deviceInfo);

    void sendCode(GizWifiDevice deviceInfo) ;
    void managerDevice(GizWifiDevice deviceInfo) ;
}
