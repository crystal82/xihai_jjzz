package com.chipsea.ui.activity;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;

import java.util.List;

/**
 * 作者：HWQ on 2017/11/10 18:04
 * 描述：
 */

public class DeviceListEvent {
    private List<GizWifiDevice> deviceList;
    private GizWifiErrorCode    result;

    public DeviceListEvent(GizWifiErrorCode result, List<GizWifiDevice> deviceList) {
        this.deviceList = deviceList;
        this.result = result;
    }

    public List<GizWifiDevice> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<GizWifiDevice> deviceList) {
        this.deviceList = deviceList;
    }

    public GizWifiErrorCode getResult() {
        return result;
    }

    public void setResult(GizWifiErrorCode result) {
        this.result = result;
    }
}
