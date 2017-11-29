package com.chipsea.ui.activity;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chipsea.code.business.ActivityBusiness;
import com.chipsea.code.util.LogUtil;
import com.chipsea.code.util.StandardUtil;
import com.chipsea.ui.R;
import com.chipsea.view.text.CustomTextView;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import chipsea.wifiplug.lib.GosDeploy;


public class ControlLightActivity extends GosControlModuleBaseActivity implements View.OnClickListener, View
        .OnTouchListener, SeekBar.OnSeekBarChangeListener {
    private ViewHolder   mViewHolder;
    private StandardUtil mStandardUti;
    private Handler mHandler       = new Handler();
    private Handler intevalHandler = new Handler();
    private long    refreshInteval = 15000;
    private GizWifiDevice mDevice;
    private Vibrator      mVibrator;  //声明一个振动器对象
    public static final String DEVICE = "device";

    // 数据点"开关机"对应的标识名

    List<String> attrsSwitch = new ArrayList<>();
    List<String> attrsPwm    = new ArrayList<>();

    private class ViewHolder {
        ImageView      backImag;
        TextView       tv_light_num;
        SeekBar        sb_light;
        ImageView      timingBto;
        ImageView      light_swichBto;
        RelativeLayout bgLayout;
        ImageView      swichImage;
        CustomTextView swichStatusText;
        CustomTextView swichPowerConsumptionText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_control);
        hintTitleBar();
        setStatusBarColor(R.color.main_swich_off_bg);
        mStandardUti = StandardUtil.getInstance(this);
        ActivityBusiness.getInstance().addActivity(this);

        this.initView();
        initDevice();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDevice.setListener(gizWifiDeviceListener);
        getStatusOfDevice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDevice.setSubscribe(GosDeploy.getProductSecret(mDevice.getProductKey()), false);
        mDevice.setListener(null);
    }

    private void initDevice() {
        mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
        Intent intent = getIntent();
        mDevice = intent.getParcelableExtra("GizWifiDevice");
        attrsSwitch.add(KEY_SWITCH);
        attrsPwm.add(KEY_LIGHT);
    }

    /**
     * Description:页面加载后弹出等待框，等待设备可被控制状态回调，如果一直不可被控，等待一段时间后自动退出界面
     */
    private void getStatusOfDevice() {
        // 设备是否可控
        if (isDeviceCanBeControlled()) {
            // 可控则查询当前设备状态
            mDevice.getDeviceStatus(attrsSwitch);
            mDevice.getDeviceStatus(attrsPwm);
        } else {
            // 显示等待栏
            progressDialog.show();

            if (mDevice.isLAN()) {
                // 小循环10s未连接上设备自动退出
                mHandler.postDelayed(mRunnable, 10000);
            } else {
                // 大循环20s未连接上设备自动退出
                mHandler.postDelayed(mRunnable, 20000);
            }
        }
    }

    private Runnable mRunnable = new Runnable() {
        public void run() {
            if (isDeviceCanBeControlled()) {
                progressDialog.cancel();
            } else {
                progressDialog.cancel();
                showToast(getString(R.string.equipment_no_response));
                finish();
            }
        }
    };

    private boolean isDeviceCanBeControlled() {
        return mDevice.getNetStatus() == GizWifiDeviceNetStatus.GizDeviceControlled;
    }

    private void initView() {
        mViewHolder = new ViewHolder();
        mViewHolder.tv_light_num = (TextView) findViewById(R.id.tv_light_num);
        mViewHolder.sb_light = (SeekBar) findViewById(R.id.sb_light);
        mViewHolder.sb_light.setOnSeekBarChangeListener(ControlLightActivity.this);
        mViewHolder.backImag = (ImageView) findViewById(R.id.backImag);
        mViewHolder.light_swichBto = (ImageView) findViewById(R.id.light_swichBto);
        mViewHolder.light_swichBto.setOnClickListener(this);
        mViewHolder.timingBto = (ImageView) findViewById(R.id.timingBto);
        mViewHolder.swichPowerConsumptionText = (CustomTextView) findViewById(R.id.swichPowerConsumptionText);
        mViewHolder.swichStatusText = (CustomTextView) findViewById(R.id.swichStatusText);
        mViewHolder.backImag.setOnClickListener(ControlLightActivity.this);
        mViewHolder.timingBto.setOnClickListener(ControlLightActivity.this);

        mViewHolder.bgLayout = (RelativeLayout) findViewById(R.id.bgLayout);
        mViewHolder.swichImage = (ImageView) findViewById(R.id.swichImage);

        mViewHolder.swichImage.setOnTouchListener(this);
        mViewHolder.timingBto.setEnabled(true);
    }

    private boolean issend        = true;
    private int     mSentLightNum = -1;

    private void doSendLightInfo(int progress) {
        LogUtil.d("doSendLightInfo---:" + mSentLightNum + "   " + mViewHolder.sb_light.getProgress());
        if (mSentLightNum != (progress + 1)) {
            mSentLightNum = progress + 1;
            mViewHolder.tv_light_num.setText(mSentLightNum + "");
            mHandler.removeCallbacks(reSetLight);
            mHandler.postDelayed(reSetLight, 2000);
            sendCommand(KEY_LIGHT, progress + 1);
        }
    }

    //发送光唤醒命令
    private void doSendWakeUp() {
        if (issend) {
            issend = false;
            mWakeUpSwitch = !mWakeUpSwitch;
            sendCommand(KEY_WAKE_SWITCH, mWakeUpSwitch);
            mHandler.removeCallbacks(reSetIssend);
            mHandler.postDelayed(reSetIssend, 200);
            mVibrator.vibrate(new long[]{100, 100}, -1);
            updateSunWakeUpUi();
        }
    }

    private void doOpenOrCloseSwitch() {
        if (issend) {
            issend = false;
            mSwitch = !mSwitch;
            sendCommand(KEY_SWITCH, mSwitch);
            mHandler.removeCallbacks(reSetIssend);
            mHandler.postDelayed(reSetIssend, 200);
            mVibrator.vibrate(new long[]{100, 100}, -1);
            refreshBackgroundByStatus(); //马上跟新UI
        }
    }

    Runnable reSetLight = new Runnable() {
        @Override
        public void run() {
            mSentLightNum = -1;
        }
    };

    Runnable reSetIssend = new Runnable() {
        @Override
        public void run() {
            issend = true;
        }
    };

    /**
     * 发送指令,下发单个数据点的命令可以用这个方法
     * 下发多个数据点命令不能用这个方法多次调用，一次性多次调用这个方法会导致模组无法正确接收消息，参考方法内注释。
     *
     * @param key   数据点对应的标识名
     * @param value 需要改变的值
     */
    private void sendCommand(String key, Object value) {
        if (value == null) {
            return;
        }
        int                               sn      = 5;
        ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<String, Object>();
        hashMap.put(key, value);
        if (mDevice == null) {
            showToast(getString(R.string.equipment_no_response));
            finish();
        } else {
            mDevice.write(hashMap, sn);
        }
        LogUtil.i("TAG", "下发命令：" + hashMap.toString());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == mViewHolder.backImag) {
            finishSelf();
        } else if (v == mViewHolder.timingBto) {
            Intent intent = new Intent(ControlLightActivity.this, TimingActivity.class);
            intent.putExtra(DEVICE, mDevice);
            startCommonActivity(intent);
        } else if (v == mViewHolder.light_swichBto) {
            doSendWakeUp();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
            }
            case MotionEvent.ACTION_MOVE: {
                break;
            }
            case MotionEvent.ACTION_UP: {
                doOpenOrCloseSwitch();
                break;
            }
        }
        return true;
    }

    //TODO:更新UI
    public void refreshBackgroundByStatus() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSwitch) {
                    setStatusBarColor(R.color.main_color);
                    //mViewHolder.bgLayout.setBackgroundResource(R.mipmap.swich_on_bg);
                    mViewHolder.swichImage.setImageResource(R.mipmap.control_light_on);
                    mViewHolder.swichStatusText.setText(getResources().getString(R.string.mainSwitchOn));
                    mViewHolder.sb_light.setEnabled(true);
                } else {
                    setStatusBarColor(R.color.main_swich_off_bg);
                    //mViewHolder.bgLayout.setBackgroundResource(R.mipmap.swich_off_bg);
                    mViewHolder.swichImage.setImageResource(R.mipmap.control_light_off);
                    mViewHolder.swichStatusText.setText(getResources().getString(R.string.mainSwitchOff));
                    mViewHolder.sb_light.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        doSendLightInfo(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    protected GizWifiDeviceListener gizWifiDeviceListener = new GizWifiDeviceListener() {

        /** 用于设备订阅 */
        public void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
        }

        /** 用于获取设备状态 */
        public void didReceiveData(GizWifiErrorCode result, GizWifiDevice device,
                                   ConcurrentHashMap<String, Object> dataMap, int sn) {
            super.didReceiveData(result, device, dataMap, sn);
            LogUtil.e("liang", "接收到数据" + dataMap.get("data") + " sn:" + sn + "  binary:" + dataMap.get("binary"));
            if ((result == GizWifiErrorCode.GIZ_SDK_SUCCESS) && dataMap.get("data") != null) {
                getDataFromReceiveDataMap(dataMap);
                LogUtil.d("mSwitch:" + mSwitch);

                //updateSunWakeUpUi();
                updateLightNumUi();
                refreshBackgroundByStatus();
            }
        }

        /** 用于设备硬件信息 */
        public void didGetHardwareInfo(GizWifiErrorCode result, GizWifiDevice device,
                                       ConcurrentHashMap<String, String> hardwareInfo) {
        }

        /** 用于修改设备信息 */
        public void didSetCustomInfo(GizWifiErrorCode result, GizWifiDevice device) {
        }

        /** 用于设备状态变化 */
        public void didUpdateNetStatus(GizWifiDevice device, GizWifiDeviceNetStatus netStatus) {
            LogUtil.e("接收到数据didUpdateNetStatus：" + netStatus);
            if (netStatus == GizWifiDeviceNetStatus.GizDeviceControlled) {
                mHandler.removeCallbacks(mRunnable);
                mDevice.getDeviceStatus(attrsSwitch);
                mDevice.getDeviceStatus(attrsPwm);
                progressDialog.cancel();
            } else {
                //showToast(getString(R.string.disconnect));
                finish();
            }
        }
    };

    //TODO:暂时不使用，唤醒总开关
    private void updateSunWakeUpUi() {
        mViewHolder.light_swichBto.setImageResource(
                mWakeUpSwitch ? R.mipmap.wake_up_on : R.mipmap.wake_up_off);
    }


    private void updateLightNumUi() {
        mViewHolder.tv_light_num.setText(mLightInfo + "");
        if (mSentLightNum != mLightInfo) {
            mSentLightNum = mLightInfo;
            mViewHolder.sb_light.setProgress(mLightInfo - 1);
        }
    }

}
