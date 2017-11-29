package com.chipsea.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chipsea.code.business.ActivityBusiness;
import com.chipsea.code.util.HexStrUtils;
import com.chipsea.code.util.LogUtil;
import com.chipsea.code.util.StandardUtil;
import com.chipsea.mode.entity.StatDetailEntity;
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


public class ControlSocketActivity extends GosControlModuleBaseActivity implements View.OnClickListener, View
        .OnTouchListener {
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
    List<String> attrsLvl    = new ArrayList<>();

    private class ViewHolder {
        ImageView backImag, airControl;
        ImageButton swichBto, timingBto, statBto;
        LinearLayout   statLayout;
        RelativeLayout bgLayout;
        ImageView      rippleImage;
        ImageView      swichImage;
        LinearLayout   openLayout;
        CustomTextView swichStatusText;
        CustomTextView swichPowerConsumptionText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_control);
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
        //LogUtil.i("Apptest", mDevice.getDid() + "");
        attrsSwitch.add(KEY_SWITCH);
        attrsLvl.add(RSSI_LVL);
    }

    /**
     * Description:页面加载后弹出等待框，等待设备可被控制状态回调，如果一直不可被控，等待一段时间后自动退出界面
     */
    private void getStatusOfDevice() {
        // 设备是否可控
        if (isDeviceCanBeControlled()) {
            // 可控则查询当前设备状态
            mDevice.getDeviceStatus(attrsSwitch);
            mDevice.getDeviceStatus(attrsLvl);
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
        mViewHolder.backImag = (ImageView) findViewById(R.id.backImag);
        mViewHolder.airControl = (ImageView) findViewById(R.id.airControl);
        mViewHolder.swichBto = (ImageButton) findViewById(R.id.swichBto);
        mViewHolder.timingBto = (ImageButton) findViewById(R.id.timingBto);
        mViewHolder.statBto = (ImageButton) findViewById(R.id.statBto);
        mViewHolder.statLayout = (LinearLayout) findViewById(R.id.statLayout);
        mViewHolder.openLayout = (LinearLayout) findViewById(R.id.openLayout);
        mViewHolder.swichPowerConsumptionText = (CustomTextView) findViewById(R.id.swichPowerConsumptionText);
        mViewHolder.swichStatusText = (CustomTextView) findViewById(R.id.swichStatusText);
        mViewHolder.backImag.setOnClickListener(ControlSocketActivity.this);
        mViewHolder.swichBto.setOnClickListener(ControlSocketActivity.this);
        mViewHolder.timingBto.setOnClickListener(ControlSocketActivity.this);
        mViewHolder.statBto.setOnClickListener(ControlSocketActivity.this);

        mViewHolder.bgLayout = (RelativeLayout) findViewById(R.id.bgLayout);
        mViewHolder.rippleImage = (ImageView) findViewById(R.id.rippleImage);
        mViewHolder.swichImage = (ImageView) findViewById(R.id.swichImage);

        mViewHolder.swichImage.setOnTouchListener(this);
        mViewHolder.swichBto.setEnabled(true);
        mViewHolder.timingBto.setEnabled(true);
    }

    private void updateSamplingUI(final StatDetailEntity detail) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mViewHolder.swichPowerConsumptionText.setText(getString(R.string.statisticsPower) + detail
                        .instantaneousPower + "W");
            }
        });
    }

    private boolean issend = true;

    private void openOrCloseSwitch() {
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

        int sn = 5;

        ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<String, Object>();
        hashMap.put(key, value);
        mDevice.write(hashMap, sn);
        LogUtil.i("TAG", "下发命令：" + hashMap.toString());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == mViewHolder.backImag) {
            finishSelf();
        } else if (v == mViewHolder.swichBto) {
            openOrCloseSwitch();
        } else if (v == mViewHolder.timingBto) {
            Intent intent = new Intent(ControlSocketActivity.this, TimingActivity.class);
            intent.putExtra(DEVICE, mDevice);
            startCommonActivity(intent);
        }
        //else if (v == mViewHolder.statBto) {
        //    Intent intent = new Intent(ControlSocketActivity.this, StatActivity.class);
        //    intent.putExtra("currDeviceInfo", mCurDevice);
        //    startCommonActivity(intent);
        //} else if (v == mViewHolder.airControl) {
        //    Intent startIntent = new Intent(this, AirControlActivity.class);
        //    startIntent.putExtra("currDeviceInfo", mCurDevice);
        //    startCommonActivity(startIntent);
        //}
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                swichDownAnim(false);
            }
            case MotionEvent.ACTION_MOVE: {
                break;
            }
            case MotionEvent.ACTION_UP: {
                swichUpAnim();
                openOrCloseSwitch();
                break;
            }
        }
        return true;
    }

    public void swichDownAnim(boolean showUpAnim) {
        mViewHolder.rippleImage.setAlpha(0f);
        final ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(mViewHolder.swichImage, "scaleY", 1f, 0.8f);
        final ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(mViewHolder.swichImage, "scaleX", 1f, 0.8f);
        AnimatorSet          animSet    = new AnimatorSet();
        animSet.play(scaleYAnim).with(scaleXAnim);
        animSet.setDuration(100);
        animSet.start();
        if (showUpAnim) {
            swichUpAnim();
        }
    }

    public void rippleAnim() {
        mViewHolder.rippleImage.setAlpha(1f);
        ObjectAnimator    rippleScaleXAnim = ObjectAnimator.ofFloat(mViewHolder.rippleImage, "scaleX", 1f, 1.2f);
        ObjectAnimator    rippleScaleYAnim = ObjectAnimator.ofFloat(mViewHolder.rippleImage, "scaleY", 1f, 1.2f);
        ObjectAnimator    rippleAlphaAnim  = ObjectAnimator.ofFloat(mViewHolder.rippleImage, "alpha", 1f, 0f);
        final AnimatorSet animSet1         = new AnimatorSet();
        animSet1.play(rippleScaleYAnim).with(rippleAlphaAnim).with(rippleScaleXAnim);
        animSet1.setDuration(800);
        animSet1.start();
        animSet1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                swichDownAnim(true);
            }
        });
    }

    public void swichUpAnim() {
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(mViewHolder.swichImage, "scaleY", 0.8f, 1f);
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(mViewHolder.swichImage, "scaleX", 0.8f, 1f);
        AnimatorSet    animSet    = new AnimatorSet();
        animSet.play(scaleYAnim).with(scaleXAnim);
        animSet.setDuration(100);
        animSet.start();
    }

    //TODO:更新UI
    public void refreshBackgroundByStatus() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSwitch) {
                    setStatusBarColor(R.color.main_color);
                    mViewHolder.bgLayout.setBackgroundResource(R.mipmap.swich_on_bg);
                    mViewHolder.swichImage.setImageResource(R.mipmap.swich_on_icon);
                    mViewHolder.openLayout.setVisibility(View.VISIBLE);
                    mViewHolder.swichStatusText.setText(getResources().getString(R.string.mainSwitchOn));
                } else {
                    setStatusBarColor(R.color.main_swich_off_bg);
                    mViewHolder.bgLayout.setBackgroundResource(R.mipmap.swich_off_bg);
                    mViewHolder.swichImage.setImageResource(R.mipmap.swich_off_icon);
                    mViewHolder.openLayout.setVisibility(View.INVISIBLE);
                    mViewHolder.swichStatusText.setText(getResources().getString(R.string.mainSwitchOff));
                }
            }
        });
    }


    protected GizWifiDeviceListener gizWifiDeviceListener = new GizWifiDeviceListener() {

        /** 用于设备订阅 */
        public void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
        }

        /** 用于获取设备状态 */
        public void didReceiveData(GizWifiErrorCode result, GizWifiDevice device,
                                   java.util.concurrent.ConcurrentHashMap<String, Object> dataMap, int sn) {
            super.didReceiveData(result, device, dataMap, sn);
            LogUtil.e("liang", "接收到数据" + dataMap + " sn:" + sn);
            if ((result == GizWifiErrorCode.GIZ_SDK_SUCCESS) && dataMap.get("data") != null) {
                getDataFromReceiveDataMap(dataMap);
                LogUtil.d("mSwitch:" + mSwitch);
                refreshBackgroundByStatus();
            }
            if ((result == GizWifiErrorCode.GIZ_SDK_SUCCESS) && dataMap.get("binary") != null) {
                byte[] binary = (byte[]) dataMap.get("binary");
                String data   = HexStrUtils.bytesToHexString(binary);
                String cmd    = data.substring(0, 2);
                String flag   = data.substring(2, 4);
                LogUtil.e(cmd + "////" + flag);
                if (cmd.equals(CMD_1)) {
                    String sw = data.substring(4, 6);

                    mSwitch = sw.equals("01");
                    refreshBackgroundByStatus();

                    final int lvl = Integer.parseInt(data.substring(6, 8), 16);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateLvl(lvl);
                        }
                    });
                }
            }
        }

        /** 用于设备硬件信息 */
        public void didGetHardwareInfo(GizWifiErrorCode result, GizWifiDevice device,
                                       java.util.concurrent.ConcurrentHashMap<String, String> hardwareInfo) {
        }

        /** 用于修改设备信息 */
        public void didSetCustomInfo(GizWifiErrorCode result, GizWifiDevice device) {
        }

        /** 用于设备状态变化 */
        public void didUpdateNetStatus(GizWifiDevice device, GizWifiDeviceNetStatus netStatus) {
            LogUtil.e("接收到数据didUpdateNetStatus：" + netStatus);
            if (netStatus == GizWifiDeviceNetStatus.GizDeviceControlled) {
                mHandler.removeCallbacks(mRunnable);
                progressDialog.cancel();
            } else {
                //showToast(getString(R.string.disconnect));
                finish();
            }
        }
    };

}
