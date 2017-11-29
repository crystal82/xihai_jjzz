package com.chipsea.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.chipsea.code.business.ActivityBusiness;
import com.chipsea.code.util.LogUtil;
import com.chipsea.code.util.NetUtils;
import com.chipsea.ui.R;
import com.chipsea.view.PasswordEditText;
import com.chipsea.view.text.CustomTextView;


public class WLANConnectActivity extends CommonActivity implements View.OnClickListener {
    private static final String TAG = "WLANConnectActivity";

    private ViewHolder mViewHolder;
    private String     mProductKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentSub(R.layout.activity_wlan_connect, R.string.settingConnectWLAN);
        ActivityBusiness.getInstance().addActivity(this);
        mProductKey = getIntent().getStringExtra("productKey");
        mViewHolder = new ViewHolder();
        mViewHolder.wlanSSID = (CustomTextView) findViewById(R.id.conn_wlan_ssid);
        mViewHolder.wlanPwd = (EditText) findViewById(R.id.conn_wlan_pwd);
        mViewHolder.connWLAN = (CustomTextView) findViewById(R.id.conn_wlan);
        mViewHolder.cbLaws = (CheckBox) findViewById(R.id.cbLaws);
        mViewHolder.connWLAN.setOnClickListener(this);

        mViewHolder.wlanPwd.setInputType(0x81);
        mViewHolder.cbLaws.setChecked(true);

        initEvent();
    }

    private void initEvent() {
        mViewHolder.cbLaws.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String psw = mViewHolder.wlanPwd.getText().toString();
                if (isChecked) {
                    mViewHolder.wlanPwd.setInputType(0x81);
                } else {
                    mViewHolder.wlanPwd.setInputType(0x90);
                }
                mViewHolder.wlanPwd.setSelection(psw.length());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewHolder.wlanSSID.setText(NetUtils.getCurentWifiSSID(this));
        LogUtil.d("WLANConnectActivity:" + NetUtils.getCurentWifiSSID(this));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == mViewHolder.connWLAN) {
            String wlanPassword   = mViewHolder.wlanPwd.getText().toString();
            if (!NetUtils.isWifiConnected(this)) {
                showToast(R.string.please_connect_wifi);
                return;
            }
            String curentWifiSSID = NetUtils.getCurentWifiSSID(this);
            Intent intent = new Intent(this, SearchDeviceActivity.class);
            intent.putExtra("productKey", mProductKey);
            intent.putExtra(SearchDeviceActivity.WLAN_SSID, curentWifiSSID);
            intent.putExtra(SearchDeviceActivity.WLAN_PWD, wlanPassword);
            startCommonActivity(intent);
        }
    }

    private class ViewHolder {
        CustomTextView wlanSSID, connWLAN;
        EditText wlanPwd;
        CheckBox cbLaws;
    }
}
