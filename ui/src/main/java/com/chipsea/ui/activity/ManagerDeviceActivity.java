package com.chipsea.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chipsea.code.business.ActivityBusiness;
import com.chipsea.code.listener.ManagerRoleCallback;
import com.chipsea.code.util.LogUtil;
import com.chipsea.code.util.PrefsUtil;
import com.chipsea.mode.entity.AccountInfo;
import com.chipsea.mode.entity.DeviceInfo;
import com.chipsea.ui.R;
import com.chipsea.ui.adapter.ManagerDeviceRecyclerAdapter;
import com.chipsea.ui.dialog.CommonDialog;
import com.chipsea.view.utils.RecyclerItemDecoration;
import com.gizwits.gizwifisdk.api.GizDeviceSharing;
import com.gizwits.gizwifisdk.api.GizUserInfo;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizDeviceSharingWay;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizDeviceSharingListener;

import java.util.List;

import chipsea.wifiplug.lib.model.GosConstant;

public class ManagerDeviceActivity extends CommonActivity implements ManagerRoleCallback {
    private static final String TAG = "LiencesActivity";
    private RecyclerView                 recyclerView;
    private ManagerDeviceRecyclerAdapter adapter;
    private List<AccountInfo>            accountInfos;
    private GizWifiDevice                deviceInfo;
    private String                       productname;
    private String                       did;
    private String                       mUid;
    private String                       mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentSub(R.layout.fragment_my_device, R.string.myDeviceManageDevice);
        Intent tent = getIntent();
        productname = tent.getStringExtra("productname");
        did = tent.getStringExtra("did");
        deviceInfo = tent.getParcelableExtra("deviceInfo");

        mUid = PrefsUtil.getStringValue(PrefsUtil.KEY_USER_UID, "");
        mToken = PrefsUtil.getStringValue(PrefsUtil.KEY_USER_TOKEN, "");

        ActivityBusiness.getInstance().addActivity(this);
        recyclerView = (RecyclerView) findViewById(R.id.deviceRecyclerView);
        adapter = new ManagerDeviceRecyclerAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new RecyclerItemDecoration(this, LinearLayoutManager.VERTICAL));

        loadCode();
        loadInfos();
    }

    //加载当前分享列表
    private void loadInfos() {
        GizDeviceSharing.getBindingUsers(mToken, did);
    }

    //加载二维码
    private void loadCode() {
        progressDialog.show();
        GizDeviceSharing.sharingDevice(
                PrefsUtil.getStringValue(PrefsUtil.KEY_USER_TOKEN, ""),
                did,
                GizDeviceSharingWay.GizDeviceSharingByQRCode,
                null, null);
    }

    @Override
    public void deleteInfo(final GizUserInfo info) {
        CommonDialog commonDialog = new CommonDialog(this, getString(R.string.myShareDeleteHint), getString(R.string.sure));
        commonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GizDeviceSharing.unbindUser(mToken, did, info.getUid());
                progressDialog.show();
            }
        });
        commonDialog.showDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GizDeviceSharing.setListener(listener);
    }

    GizDeviceSharingListener listener = new GizDeviceSharingListener() {
        @Override
        public void didSharingDevice(GizWifiErrorCode result, String deviceID,
                                     int sharingID, Bitmap QRCodeImage) {
            super.didSharingDevice(result, deviceID, sharingID, QRCodeImage);
            LogUtil.d("----共享----：" + result.ordinal());
            progressDialog.dismiss();
            if (QRCodeImage != null) {
                adapter.setBitmap(QRCodeImage);
            } else {
                int errorcode = result.ordinal();
                if (8041 <= errorcode && errorcode <= 8050 || errorcode == 8308) {
                    showToast(R.string.twosharedtimeout);
                    //bottomtext.setVisibility(View.GONE);
                } else {
                    showToast(R.string.sharedfailed);
                    //bottomtext.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void didGetBindingUsers(GizWifiErrorCode result, String deviceID, List<GizUserInfo> bindUsers) {
            super.didGetBindingUsers(result, deviceID, bindUsers);
            GosConstant.mybindUsers = bindUsers;
            adapter.setInfos(bindUsers);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void didUnbindUser(GizWifiErrorCode result, String deviceID, String guestUID) {
            // TODO Auto-generated method stub
            super.didUnbindUser(result, deviceID, guestUID);
            progressDialog.dismiss();
            GizDeviceSharing.getBindingUsers(mToken, did);
            if (result.ordinal() != 0) {
                Toast.makeText(ManagerDeviceActivity.this, toastError(result), Toast.LENGTH_SHORT).show();
            }
        }
    };
}
