package com.chipsea.ui.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chipsea.code.util.LogUtil;
import com.chipsea.code.util.PrefsUtil;
import com.chipsea.code.util.StandardUtil;
import com.chipsea.ui.R;
import com.chipsea.view.dialog.BaseDialog;
import com.chipsea.view.text.CustomTextView;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;

import chipsea.wifiplug.lib.GosDeploy;


public class DeviceConnectDialog extends BaseDialog implements View.OnClickListener {
    private Context        _context;
    private ImageView      animImg;
    private ImageView      connectStateTag;
    private CustomTextView connectStateText;
    private CustomTextView bottomText;
    private StandardUtil   mStandardUtil;
    private PrefsUtil      mPrefsUtil;
    private LinearLayout   noLayout, nameLayout;
    private CustomTextView deviceNo;
    private EditText       deviceName;
    public  long           subDominId;
    public  GizWifiDevice  deviceInfo;


    public  String mac;
    public  String did;
    public  String productKey;
    public  String productSecret;
    private String mUid;
    private String mToken;

    public DeviceConnectDialog(Context context) {
        super(context);
        _context = context;
        mStandardUtil = StandardUtil.getInstance(context);
        mPrefsUtil = PrefsUtil.getInstance(context);
        mUid = PrefsUtil.getStringValue(PrefsUtil.KEY_USER_UID, "");
        mToken = PrefsUtil.getStringValue(PrefsUtil.KEY_USER_TOKEN, "");


        View vv = LayoutInflater.from(context).inflate(
                R.layout.dialog_device_connect, null);
        addView(vv);
        animImg = (ImageView) vv.findViewById(R.id.animImg);
        Animation an = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        an.setInterpolator(new LinearInterpolator());//不停顿
        an.setRepeatCount(Integer.MAX_VALUE);//重复次数
        an.setDuration(1000);
        animImg.startAnimation(an);
        connectStateTag = (ImageView) vv.findViewById(R.id.connectStateTag);
        connectStateText = (CustomTextView) vv.findViewById(R.id.connectStateText);
        noLayout = (LinearLayout) vv.findViewById(R.id.noLayout);
        nameLayout = (LinearLayout) vv.findViewById(R.id.nameLayout);
        deviceNo = (CustomTextView) vv.findViewById(R.id.deviceNo);
        deviceName = (EditText) vv.findViewById(R.id.deviceName);
        bottomText = (CustomTextView) vv.findViewById(R.id.bottomText);
        bottomText.setOnClickListener(this);
        bottomText.setEnabled(false);
        dialog.setCanceledOnTouchOutside(false);


    }

    @Override
    public void showDialog() {
        super.showDialog();
        bindDevice();
    }

    private void bindDevice() {
        LogUtil.d("---开始绑定---:mUid" + mUid + "  mToken:" + mToken + "  mac:" + mac + "  productKey:" + productKey);
        GizWifiSDK.sharedInstance().
                bindRemoteDevice(mUid, mToken, mac, productKey, GosDeploy.getProductSecret(productKey));
        GizWifiSDK.sharedInstance().
                bindRemoteDevice(mUid, mToken, mac, productKey, GosDeploy.getProductSecret(productKey));
        GizWifiSDK.sharedInstance().
                bindRemoteDevice(mUid, mToken, mac, productKey, GosDeploy.getProductSecret(productKey));
    }

    public void onBindSuccess(GizWifiDevice deviceInfo) {
        //mPrefsUtil.setDeviceInfo(deviceInfo);
        this.deviceInfo = deviceInfo;
        handler.sendEmptyMessage(1);
    }

    public void onBindError() {
        DeviceConnectDialog.this.dismiss();
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            animImg.clearAnimation();
            animImg.setVisibility(View.GONE);
            connectStateTag.setVisibility(View.VISIBLE);
            noLayout.setVisibility(View.VISIBLE);
            nameLayout.setVisibility(View.VISIBLE);
            deviceNo.setText(deviceInfo.getMacAddress());
            String deviceAlias = deviceInfo.getAlias();
            String devicePN    = deviceInfo.getProductName();
            if (TextUtils.isEmpty(deviceAlias)) {
                deviceName.setText(devicePN);
            } else {
                deviceName.setText(deviceAlias);
            }
            connectStateText.setText(R.string.settingConnectSucceed);
            bottomText.setText(R.string.settingWelcomeUser);
            bottomText.setEnabled(true);
        }
    };

    @Override
    public void onClick(View v) {
        //改名
        deviceInfo.setCustomInfo("", deviceName.getText().toString());
        l.onClick(null);
    }
}
