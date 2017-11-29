package com.chipsea.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chipsea.code.business.ActivityBusiness;
import com.chipsea.code.listener.SetTimingCallback;
import com.chipsea.ui.R;
import com.chipsea.ui.adapter.AddTimingAdapter;
import com.chipsea.ui.dialog.SetTimingDialog;
import com.chipsea.view.text.CustomTextView;

import java.util.List;

import chipsea.wifiplug.lib.GosDeploy;


public class AddDeviceActivity extends CommonActivity {
    private static final String TAG = "AddDeviceActivity";
    private CustomTextView resetSucc;
    private CustomTextView mTv_device_des;
    private ImageView      mIv_device_bg;
    private String         mProductKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentSub(R.layout.activity_add_device, R.string.settingAddDevice);
        ActivityBusiness.getInstance().addActivity(this);
        mProductKey = getIntent().getStringExtra("productKey");

        resetSucc = (CustomTextView) findViewById(R.id.reset_success);
        resetSucc.setOnClickListener(this);
        mTv_device_des = (CustomTextView) findViewById(R.id.tv_device_des);
        mIv_device_bg = (ImageView) findViewById(R.id.iv_device_bg);

        initView();
    }

    private void initView() {
        List<String> productKeyList = GosDeploy.setProductKeyList();
        if (mProductKey.equals(productKeyList.get(0))) {
            mTv_device_des.setText(R.string.settingAddDeviceHint1);
            mIv_device_bg.setImageResource(R.mipmap.setting_add_device1_bg);
        } else if (mProductKey.equals(productKeyList.get(1))) {
            mTv_device_des.setText(R.string.settingAddDeviceHint2);
            mIv_device_bg.setImageResource(R.mipmap.setting_add_device2_bg);
        } else if (mProductKey.equals(productKeyList.get(2))) {
            mTv_device_des.setText(R.string.settingAddDeviceHint3);
            mIv_device_bg.setImageResource(R.mipmap.setting_add_device3_bg);
        }
    }

    @Override
    protected void onOtherClick(View v) {
        if (v == resetSucc) {
            Intent intent = new Intent(this, WLANConnectActivity.class);
            intent.putExtra("productKey", mProductKey);
            startCommonActivity(intent);
        }
    }
}
