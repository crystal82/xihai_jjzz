package com.chipsea.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chipsea.code.business.ActivityBusiness;
import com.chipsea.code.listener.WIFIVoidCallback;
import com.chipsea.code.util.PrefsUtil;
import com.chipsea.code.util.StandardUtil;
import com.chipsea.mode.entity.DeviceInfo;
import com.chipsea.ui.R;
import com.chipsea.ui.dialog.NickNameDialog;
import com.chipsea.view.dialog.LoadDialog;
import com.chipsea.view.text.CustomTextView;


public class DeviceConnectSuccessActivity extends CommonActivity implements View.OnClickListener{
    private static final String TAG = "SearchDeviceActivity";
    private ViewHolder mViewHolder;
    private PrefsUtil mPrefsUtil;
    private StandardUtil mStandardUtil;
    private String physicalDeviceId;
    private NickNameDialog mNickNameDialog;
    private LoadDialog mLoadDialog;
    private DeviceInfo mDeviceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentSub(R.layout.activity_device_connect_success,R.string.settingDevice);
        ActivityBusiness.getInstance().addActivity(this);
        mPrefsUtil=PrefsUtil.getInstance(this);
        mStandardUtil=StandardUtil.getInstance(this);
        mViewHolder=new ViewHolder();
        mViewHolder.deviceName=(CustomTextView)findViewById(R.id.my_device_name);
        mViewHolder.deviceName.setOnClickListener(this);
        mViewHolder.deviceMac=(CustomTextView)findViewById(R.id.my_device_mac);
        mViewHolder.removeDevice=(CustomTextView)findViewById(R.id.my_device_remove);
        mViewHolder.removeDevice.setOnClickListener(this);

        mDeviceInfo= mPrefsUtil.getDeviceInfo();
        String deviceName="";
        if(mDeviceInfo.name!=null){
            deviceName=mDeviceInfo.name;
        }
        mViewHolder.deviceName.setText(this.getString(R.string.myDeviceAliName) + deviceName);
        physicalDeviceId=mDeviceInfo.physicalDeviceId;
        mViewHolder.deviceMac.setText(this.getString(R.string.myDevicePhysicAddress) + mDeviceInfo.physicalDeviceId);

    }

    private class ViewHolder {
        CustomTextView deviceName,deviceMac,removeDevice;
    }

    private void onNickNameDialog() {
        if (mNickNameDialog == null) {
            mNickNameDialog = new NickNameDialog(this);
        }
        String deviceName="";
        if(mDeviceInfo.name!=null){
            deviceName=mDeviceInfo.name;
        }
        mNickNameDialog.setText(deviceName);
        mNickNameDialog.showDialog();
        mNickNameDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLoadDialog==null){
                    mLoadDialog=LoadDialog.getShowDialog(DeviceConnectSuccessActivity.this);
                }
                mLoadDialog.show();
                final String nickName=mNickNameDialog.getText().toString();
               //DeviceUtils.changeDeviceName(mDeviceInfo.deviceId,mDeviceInfo.subDominId,nickName,new WIFIVoidCallback(){

               //    @Override
               //    public void onSuccess() {
               //        mLoadDialog.dismiss();
               //        mDeviceInfo.name=nickName;
               //        mPrefsUtil.setDeviceInfo(mDeviceInfo);
               //        mViewHolder.deviceName.setText(getString(R.string.myDeviceAliName) + nickName);
               //    }

               //    @Override
               //    public void onFailure(String msg, int code) {
               //        mLoadDialog.dismiss();
               //        showToast(mStandardUtil.getMessage(code,msg));
               //    }
               //});

            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == mViewHolder.removeDevice){
          //DeviceUtils.csUnbindDeviceNoForce(physicalDeviceId,mDeviceInfo.subDominId, new WIFIVoidCallback() {
          //    @Override
          //    public void onSuccess() {
          //        startCommonActivity(new Intent(DeviceConnectSuccessActivity.this,AddDeviceActivity.class));
          //        ActivityBusiness.getInstance().finishAllActivity();
          //    }

          //    @Override
          //    public void onFailure(String msg, int code) {
          //        showToast(mStandardUtil.getMessage(code,msg));
          //    }
          //});
        }else if(v==mViewHolder.deviceName){
            onNickNameDialog();
        }
    }
}
