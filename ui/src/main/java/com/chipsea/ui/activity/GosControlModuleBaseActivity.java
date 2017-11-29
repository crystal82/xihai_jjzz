package com.chipsea.ui.activity;

import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.chipsea.code.util.LogUtil;
import com.chipsea.ui.GosBaseActivity;
import com.chipsea.ui.R;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;

import java.text.DecimalFormat;
import java.util.concurrent.ConcurrentHashMap;

public class GosControlModuleBaseActivity extends CommonActivity {

    /*
     * ===========================================================
     * 以下key值对应开发者在云端定义的数据点标识名
     * ===========================================================
     */

	/*
     * ===========================================================
	 * 以下数值对应开发者在云端定义的可写数值型数据点增量值、数据点定义的分辨率、seekbar滚动条补偿值
	 * _ADDITION:数据点增量值
	 * _RATIO:数据点定义的分辨率
	 * _OFFSET:seekbar滚动条补偿值
	 * APP与设备定义的协议公式为：y（APP接收的值）=x（设备上报的值）* RATIO（分辨率）+ADDITION（增量值）
	 * 由于安卓的原生seekbar无法设置最小值，因此代码中增加了一个补偿量OFFSET
	 * 实际上公式中的：x（设备上报的值）=seekbar的值+补偿值
	 * ===========================================================
	 */

    /*
     * ===========================================================
     * 以下变量对应设备上报类型为布尔、数值、扩展数据点的数据存储
     * ===========================================================
     */
    // 数据点"开关机"对应的存储数据
    protected static boolean mSwitch; //开关
    protected static boolean mWakeUpSwitch; //光唤醒
    protected static    int    mLightInfo      = 1; //亮度
    public static final String KEY_SWITCH      = "switch";
    public static final String KEY_LIGHT       = "pwm_t";
    public static final String KEY_WAKE_SWITCH = "wake_sw";
    public static final String RSSI_LVL        = "rssi_lvl";
    public static final String CMD_1           = "13";
    public static final String CMD_2           = "14";

    protected static final String WIFI_HARDVER_KEY     = "wifiHardVersion";
    protected static final String WIFI_SOFTVER_KEY     = "wifiSoftVersion";
    protected static final String MCU_HARDVER_KEY      = "mcuHardVersion";
    protected static final String MCU_SOFTVER_KEY      = "mcuSoftVersion";
    protected static final String WIFI_FIRMWAREID_KEY  = "wifiFirmwareId";
    protected static final String WIFI_FIRMWAREVER_KEY = "wifiFirmwareVer";
    protected static final String PRODUCT_KEY          = "productKey";

    private Toast mToast;

    @SuppressWarnings("unchecked")
    protected void getDataFromReceiveDataMap(ConcurrentHashMap<String, Object> dataMap) {
        // 已定义的设备数据点，有布尔、数值和枚举型数据

        if (dataMap.get("data") != null) {
            ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) dataMap.get("data");
            for (String dataKey : map.keySet()) {
                if (dataKey.equals(KEY_SWITCH)) {
                    mSwitch = (Boolean) map.get(dataKey);
                    updateUI(mSwitch);
                } else if (dataKey.equals(KEY_LIGHT)) {
                    mLightInfo = (Integer) map.get(dataKey);
                } else if (dataKey.equals(RSSI_LVL)) {
                    int rssi_lvl = (int) map.get(dataKey);
                    LogUtil.e(dataKey + "--------rssi_lvl=" + rssi_lvl);
                    updateLvl(rssi_lvl);
                } else if (dataKey.equals(KEY_WAKE_SWITCH)) {
                    mWakeUpSwitch = (Boolean) map.get(dataKey);
                }
            }
        }


        StringBuilder sBuilder = new StringBuilder();

        // 已定义的设备报警数据点，设备发生报警后该字段有内容，没有发生报警则没内容
        if (dataMap.get("alerts") != null) {
            ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) dataMap.get("alerts");
            for (String alertsKey : map.keySet()) {
                if ((Boolean) map.get(alertsKey)) {
                    sBuilder.append(getString(R.string.alarm) + alertsKey + "=true" + "\n");
                }
            }
        }

        // 已定义的设备故障数据点，设备发生故障后该字段有内容，没有发生故障则没内容
        if (dataMap.get("faults") != null) {
            ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) dataMap.get("faults");
            for (String faultsKey : map.keySet()) {
                if ((Boolean) map.get(faultsKey)) {
                    sBuilder.append(getString(R.string.breakdown) + faultsKey + "=true" + "\n");
                }
            }
        }

        if (sBuilder.length() > 0) {
            sBuilder.insert(0, getString(R.string.alarm_or_breakdown) + "\n");
            myToast(sBuilder.toString().trim());
        }

        // 透传数据，无数据点定义，适合开发者自行定义协议自行解析
        if (dataMap.get("binary") != null) {
            byte[] binary = (byte[]) dataMap.get("binary");
        }
    }

    protected GizWifiDeviceListener gizWifiDeviceListener = new GizWifiDeviceListener() {

        /** 用于设备订阅 */
        public void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
            GosControlModuleBaseActivity.this.didSetSubscribe(result, device, isSubscribed);
        }

        ;

        /** 用于获取设备状态 */
        public void didReceiveData(GizWifiErrorCode result, GizWifiDevice device,
                                   java.util.concurrent.ConcurrentHashMap<String, Object> dataMap, int sn) {
            GosControlModuleBaseActivity.this.didReceiveData(result, device, dataMap, sn);
        }

        ;

        /** 用于设备硬件信息 */
        public void didGetHardwareInfo(GizWifiErrorCode result, GizWifiDevice device,
                                       java.util.concurrent.ConcurrentHashMap<String, String> hardwareInfo) {
            GosControlModuleBaseActivity.this.didGetHardwareInfo(result, device, hardwareInfo);
        }

        ;

        /** 用于修改设备信息 */
        public void didSetCustomInfo(GizWifiErrorCode result, GizWifiDevice device) {
            GosControlModuleBaseActivity.this.didSetCustomInfo(result, device);
        }

        ;

        /** 用于设备状态变化 */
        public void didUpdateNetStatus(GizWifiDevice device, GizWifiDeviceNetStatus netStatus) {
            GosControlModuleBaseActivity.this.didUpdateNetStatus(device, netStatus);
        }

        ;

    };

    /**
     * 设备订阅回调
     *
     * @param result       错误码
     * @param device       被订阅设备
     * @param isSubscribed 订阅状态
     */
    protected void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
    }

    /**
     * 设备状态回调
     *
     * @param result  错误码
     * @param device  当前设备
     * @param dataMap 当前设备状态
     * @param sn      命令序号
     */
    protected void didReceiveData(GizWifiErrorCode result, GizWifiDevice device,
                                  java.util.concurrent.ConcurrentHashMap<String, Object> dataMap, int sn) {
    }

    /**
     * 设备硬件信息回调
     *
     * @param result       错误码
     * @param device       当前设备
     * @param hardwareInfo 当前设备硬件信息
     */
    protected void didGetHardwareInfo(GizWifiErrorCode result, GizWifiDevice device,
                                      java.util.concurrent.ConcurrentHashMap<String, String> hardwareInfo) {
    }

    /**
     * 修改设备信息回调
     *
     * @param result 错误码
     * @param device 当前设备
     */
    protected void didSetCustomInfo(GizWifiErrorCode result, GizWifiDevice device) {
    }

    /**
     * 设备状态变化回调
     */
    protected void didUpdateNetStatus(GizWifiDevice device, GizWifiDeviceNetStatus netStatus) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void myToast(String string) {
        if (mToast != null) {
            mToast.setText(string);
        } else {
            mToast = Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    protected void hideKeyBoard(View v) {
        // 隐藏键盘
        ((InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).
                hideSoftInputFromWindow(v.getWindowToken(),
                                        InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * Description:显示格式化数值，保留对应分辨率的小数个数，比如传入参数（20.3656，0.01），将返回20.37
     *
     * @param date 传入的数值
     * @return
     */
    protected String formatValue(double date, Object scale) {
        if (scale instanceof Double) {
            DecimalFormat df = new DecimalFormat(scale.toString());
            return df.format(date);
        }
        return Math.round(date) + "";
    }

    public void updateUI(boolean b) {

    }

    public void updateLvl(int lvl) {

    }

}