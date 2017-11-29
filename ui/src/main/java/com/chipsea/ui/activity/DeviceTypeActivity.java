package com.chipsea.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.chipsea.ui.R;

import java.util.List;

import chipsea.wifiplug.lib.GosDeploy;


public class DeviceTypeActivity extends CommonActivity {

    private List<String> mProductKeyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentSub(R.layout.activity_device_type, R.string.settingAddDevice);
        mProductKeyList = GosDeploy.setProductKeyList();

        LinearLayout ll_chaz = (LinearLayout) findViewById(R.id.ll_chaz);
        LinearLayout ll_lamp = (LinearLayout) findViewById(R.id.ll_lamp);
        LinearLayout ll_cube = (LinearLayout) findViewById(R.id.ll_cube);
        ll_chaz.setOnClickListener(this);
        ll_lamp.setOnClickListener(this);
        ll_cube.setOnClickListener(this);
    }

    @Override
    public void onOtherClick(View view) {
        String productKey = "";
        if (view.getId() == R.id.ll_chaz) {
            productKey = mProductKeyList.get(0);
        } else if (view.getId() == R.id.ll_lamp) {
            productKey = mProductKeyList.get(1);
        } else if (view.getId() == R.id.ll_cube) {
            productKey = mProductKeyList.get(2);
        }
        Intent intent = new Intent(DeviceTypeActivity.this, AddDeviceActivity.class);
        intent.putExtra("productKey", productKey);
        startActivity(intent);
        finish();
    }
}
