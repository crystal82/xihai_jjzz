package com.chipsea.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chipsea.code.business.ActivityBusiness;
import com.chipsea.code.util.LogUtil;
import com.chipsea.code.util.PrefsUtil;
import com.chipsea.mode.entity.DeviceBind;
import com.chipsea.ui.R;
import com.chipsea.ui.adapter.DeviceListAdapter;
import com.chipsea.ui.dialog.DeviceConnectDialog;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiConfigureMode;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.enumration.GizWifiGAgentType;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;

import java.util.ArrayList;
import java.util.List;

import chipsea.wifiplug.lib.GosDeploy;


public class SearchDeviceActivity extends CommonActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "SearchDeviceActivity";
    private ListView          listView;
    private DeviceListAdapter adapter;
    public static String WLAN_SSID = "device_conn_ssid";
    public static String WLAN_PWD  = "device_conn_pwd";
    private String                       mWlanSSID;
    private String                       mWlanPwd;
    private DeviceConnectDialog          mDialog;
    private String                       mProductKey;
    private ArrayList<GizWifiGAgentType> mModeList;
    private PrefsUtil                    mSpInstance;
    private String                       mUid;
    private String                       mToken;
    private boolean isDestroy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentSub(R.layout.activity_search_device, R.string.myDeviceFind);
        ActivityBusiness.getInstance().addActivity(this);
        isDestroy = false;
        mWlanSSID = getIntent().getStringExtra(WLAN_SSID);
        mWlanPwd = getIntent().getStringExtra(WLAN_PWD);
        listView = (ListView) findViewById(R.id.listview);
        mProductKey = getIntent().getStringExtra("productKey");
        mSpInstance = PrefsUtil.getInstance(this);
        mUid = PrefsUtil.getStringValue(PrefsUtil.KEY_USER_UID, "");
        mToken = PrefsUtil.getStringValue(PrefsUtil.KEY_USER_TOKEN, "");
        LogUtil.d("---mProductKey:---" + mProductKey + "  mUid:" + mUid + "  mToken:" + mToken
                          + "    SSID:" + mWlanSSID + "   Pwd:" + mWlanPwd);

        adapter = new DeviceListAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        //使用汉枫配网
        mModeList = new ArrayList<GizWifiGAgentType>();
        mModeList.add(GizWifiGAgentType.GizGAgentHF);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                findDevice();
            }
        }, 500);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 每次返回activity都要注册一次sdk监听器，保证sdk状态能正确回调
        GizWifiSDK.sharedInstance().setListener(gizWifiSDKListener);
    }

    private void findDevice() {
        if (adapter.scanningView != null) {
            adapter.scanningView.start();
        }
        LogUtil.d("SearchDeviceActivity----开始配网----findDevice");
        GizWifiSDK.sharedInstance().setDeviceOnboarding(
                mWlanSSID, mWlanPwd,
                GizWifiConfigureMode.GizWifiAirLink,
                null, 60, mModeList);
    }

    @Override
    public void onOtherClick(View v) {
        Intent intent = null;
        intent = new Intent(this, MainActivity.class);
        startCommonActivity(intent);
        ActivityBusiness.getInstance().finishAllActivity();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            if (mDialog == null) {
                mDialog = new DeviceConnectDialog(this);
            }
            mDialog.setOnClickListener(this);
            mDialog.mac = adapter.smartDevices.get(position - 1).mac;
            mDialog.did = adapter.smartDevices.get(position - 1).did;
            mDialog.productKey = adapter.smartDevices.get(position - 1).productKey;
            isStartBind = true;
            bindNum = 0;
            mDialog.showDialog();

            //倒计时30秒绑定倒计时
            mHandler.postDelayed(mBindRunnable, 40 * 1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d("-------search-----isDestroy:" + isDestroy);
        isDestroy = true;
        mHandler.removeCallbacks(mBindRunnable);
        if (mDialog != null) {
            mDialog.dismiss();
        }
        //GizWifiSDK.sharedInstance().setListener(null);
    }

    boolean isStartBind = false;
    int     bindNum     = 0;
    String MAC;
    Handler  mHandler      = new Handler();
    Runnable mBindRunnable = new Runnable() {
        @Override
        public void run() {
            showToast(R.string.settingBindError);
            finish();
        }
    };
    public GizWifiSDKListener gizWifiSDKListener = new GizWifiSDKListener() {

        /** 设备配置成功 */
        public void didSetDeviceOnboarding(GizWifiErrorCode result, GizWifiDevice device) {
            LogUtil.e("SearchDeviceActivity--配网成功,显示成功设备------:" + bindNum);
            if (isDestroy) return;
            if (GizWifiErrorCode.GIZ_SDK_DEVICE_CONFIG_IS_RUNNING == result) {
                return;
            }
            if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                MAC = device.getMacAddress();
                LogUtil.e("TAG", "SearchDeviceActivity----配网成功,显示成功设备-----------MAC:" + MAC);
                adapter.scanningView.stop();
                adapter.smartDevices.add(new DeviceBind(device.getMacAddress(),
                                                        device.getDid(),
                                                        device.getProductKey(),
                                                        GosDeploy.getProductSecret(device.getProductKey())));
                adapter.notifyDataSetChanged();
            } else {
                LogUtil.e("TAG", "SearchDeviceActivity-----配网失败-----");
                isStartBind = false;
                adapter.scanningView.stop();
                showToast(getString(R.string.set_net_error));
                finish();
            }
        }

        /** 用于设备列表,判断是否绑定成功，连续三次才为成功。 */
        public void didDiscovered(GizWifiErrorCode result, java.util.List<GizWifiDevice> deviceList) {
            LogUtil.d("SearchDeviceActivity----------didDiscovered:" + bindNum);
            if (bindNum >= 4 || isDestroy) {
                return;
            }
            if (!TextUtils.isEmpty(MAC)) {
                for (GizWifiDevice device : deviceList) {
                    //校验绑定状态
                    if (checkBindState(device)) return;
                }
            }
        }

        /** 用于设备绑定 */
        public void didBindDevice(GizWifiErrorCode result, java.lang.String did) {
            LogUtil.d("SearchDeviceActivity----------didBindDevice:" + bindNum + "  " + result);
            if (isDestroy) return;

            if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                if (bindNum <= 3) {
                    List<String> ProductKeyList = GosDeploy.setProductKeyList();
                    GizWifiSDK.sharedInstance().getBoundDevices(mUid, mToken, ProductKeyList);
                }
            } else {
                if (isStartBind) {
                    GizWifiSDK.sharedInstance().
                            bindRemoteDevice(mUid, mToken, MAC,
                                             mProductKey,
                                             GosDeploy.getProductSecret(mProductKey));
                }
            }
        }
    };

    private boolean checkBindState(GizWifiDevice device) {
        if (device.getMacAddress().equals(MAC)) {
            if (!device.isBind()) {
                bindNum = 0;
                if (isStartBind) {
                    GizWifiSDK.sharedInstance().
                            bindRemoteDevice(mUid, mToken, MAC,
                                             mProductKey,
                                             GosDeploy.getProductSecret(mProductKey));
                }
                return true;
            }
            LogUtil.e("TAG", "------bindNum=" + bindNum);
            if (bindNum >= 3) {
                bindNum++;
                //绑定成功，移除runnable
                mHandler.removeCallbacks(mBindRunnable);
                mDialog.onBindSuccess(device);
                //finish();
                LogUtil.e("TAG", "--success---" + device.getMacAddress());
                return true;
            } else if (bindNum < 3) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                bindNum++;
                List<String> ProductKeyList = GosDeploy.setProductKeyList();
                GizWifiSDK.sharedInstance().getBoundDevices(mUid, mToken, ProductKeyList);
            }
        }
        return false;
    }


}
