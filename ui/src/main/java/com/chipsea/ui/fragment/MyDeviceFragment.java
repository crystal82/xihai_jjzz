package com.chipsea.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chipsea.code.listener.EditDeviceCallback;
import com.chipsea.code.util.LogUtil;
import com.chipsea.code.util.PrefsUtil;
import com.chipsea.code.util.StandardUtil;
import com.chipsea.mode.entity.AccountInfo;
import com.chipsea.mode.entity.AirModel;
import com.chipsea.mode.entity.DeviceInfo;
import com.chipsea.ui.R;
import com.chipsea.ui.activity.DeviceListEvent;
import com.chipsea.ui.activity.MainActivity;
import com.chipsea.ui.activity.ManagerDeviceActivity;
import com.chipsea.ui.adapter.MyDeviceRecyclerAdapter;
import com.chipsea.ui.dialog.CommonDialog;
import com.chipsea.ui.dialog.NickNameDialog;
import com.chipsea.view.VerticalSwipeRefreshLayout;
import com.chipsea.view.utils.RecyclerItemDecoration;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import chipsea.wifiplug.lib.GosDeploy;


public class MyDeviceFragment extends LazyFragment implements View.OnClickListener, EditDeviceCallback,
        SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "SearchDeviceActivity";
    private RecyclerView            recyclerView;
    private MyDeviceRecyclerAdapter adapter;
    private List<GizWifiDevice>     deviceInfos;
    private StandardUtil            mStandardUtil;
    private AccountInfo             accountInfo;

    private List<String>      mProductKeyList;
    private ArrayList<String> mBoundMessage;
    private MainActivity      mActivity;
    private String            mUid;
    private String            mToken;
    private Handler mHandler = new Handler();
    private VerticalSwipeRefreshLayout swipeRefreshView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentSub(R.layout.fragment_my_device, R.string.settingDevice);
        LogUtil.d("---MyDeviceFragment--onActivityCreated-");
        mActivity = (MainActivity) getActivity();
        swipeRefreshView = (VerticalSwipeRefreshLayout) mParentView.findViewById(R.id.srl);
        swipeRefreshView.setOnRefreshListener(this);

        recyclerView = (RecyclerView) mParentView.findViewById(R.id.deviceRecyclerView);
        mStandardUtil = StandardUtil.getInstance(instance);
        accountInfo = PrefsUtil.getInstance(instance).getAccountInfo();
        deviceInfos = new ArrayList<>();
        adapter = new MyDeviceRecyclerAdapter(instance, deviceInfos, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new RecyclerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        initData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (deviceInfos == null || deviceInfos.size() == 0) {
            mHandler.post(queryDeviceList);
        } else {
            mHandler.postDelayed(queryDeviceList, 5 * 1000);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(queryDeviceList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(DeviceListEvent event) {
        LogUtil.d("------MyDevice onDataSynEvent------");
        if (mActivity.getDeviceList() != null && deviceInfos != null) {
            swipeRefreshView.setRefreshing(false);
            analyzeData(event.getDeviceList());
        }
    }


    private Runnable queryDeviceList = new Runnable() {
        @Override
        public void run() {
            LogUtil.d("queryDeviceList：" + MainActivity.sAutoLogin + "   " + (!TextUtils.isEmpty(mUid) && !TextUtils
                    .isEmpty(mToken)));
            if (MainActivity.sAutoLogin) {
                return;
            }
            //TODO:请求列表
            if (!TextUtils.isEmpty(mUid) && !TextUtils.isEmpty(mToken)) {
                GizWifiSDK.sharedInstance().getBoundDevices(mUid, mToken, mProductKeyList);
            }
        }
    };

    private void initData() {
        mBoundMessage = new ArrayList<>();
        mProductKeyList = GosDeploy.setProductKeyList();
        mUid = PrefsUtil.getStringValue(PrefsUtil.KEY_USER_UID, "");
        mToken = PrefsUtil.getStringValue(PrefsUtil.KEY_USER_TOKEN, "");
    }

    private void analyzeData(List<GizWifiDevice> deviceList) {
        LogUtil.d("---analyzeData---:" + deviceList.size());
        deviceInfos.clear();
        for (GizWifiDevice device : deviceList) {
            if (device.isBind()) {
                LogUtil.d("---analyzeData---:" + device.getProductKey() + "    did:" + device.getDid());
                deviceInfos.add(device);
            }
        }
        adapter.notifyDataSetChanged();
        //定时请求数据轮询
        mHandler.removeCallbacks(queryDeviceList);
        mHandler.postDelayed(queryDeviceList, 10 * 1000);
    }

    public void onEditDeviceName(GizWifiDevice deviceInfo) {
        onNickNameDialog(deviceInfo);
    }

    private NickNameDialog mNickNameDialog;

    private void onNickNameDialog(GizWifiDevice deviceInfo) {
        mDeviceInfo = deviceInfo;
        if (mNickNameDialog == null) {
            mNickNameDialog = new NickNameDialog(getActivity());
        }

        String deviceAlias = deviceInfo.getAlias();
        String devicePN    = deviceInfo.getProductName();
        if (TextUtils.isEmpty(deviceAlias)) {
            mNickNameDialog.setText(devicePN);
        } else {
            mNickNameDialog.setText(deviceAlias);
        }
        mNickNameDialog.showDialog();
        mNickNameDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nickName = mNickNameDialog.getText().toString();
                mDeviceInfo.setCustomInfo("", nickName);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private CommonDialog  commonDialog;
    private GizWifiDevice mDeviceInfo;

    @Override
    public void deleteDevice(GizWifiDevice deviceInfo) {
        LogUtil.d("-----deleteDevice:" + deviceInfo.getProductKey());
        mDeviceInfo = deviceInfo;
        if (commonDialog == null) {
            commonDialog = new CommonDialog(instance, getString(R.string.myDeviceDeleteHint), getString(R.string.sure));
            commonDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GizWifiSDK.sharedInstance().unbindDevice(mUid, mToken, mDeviceInfo.getDid());
                    deviceInfos.remove(mDeviceInfo);
                    adapter.upDatas(deviceInfos);
                    adapter.notifyDataSetChanged();
                    LogUtil.d("-----执行删除----:" + deviceInfos.size() + "   did:" + mDeviceInfo.getDid());
                }
            });
        }
        commonDialog.showDialog();
    }

    @Override
    public void bindDevice(GizWifiDevice deviceInfo) {
        //Intent intent = new Intent(instance, AirMatchActivity.class);
        //intent.putExtra("currDeviceInfo", deviceInfo);
        //startCommonActivity(intent);
    }

    @Override
    public void sendCode(final GizWifiDevice deviceInfo) {
    }

    @Override
    public void managerDevice(GizWifiDevice deviceInfo) {
        //设备管理，二维码
        Intent intent = new Intent(instance, ManagerDeviceActivity.class);
        intent.putExtra("deviceInfo", deviceInfo);
        intent.putExtra("productname", deviceInfo.getProductName());
        intent.putExtra("did", deviceInfo.getDid());
        startCommonActivity(intent);
    }

    @Override
    public void onRefresh() {
        if (!swipeRefreshView.isRefreshing())
            swipeRefreshView.setRefreshing(true);
        mHandler.removeCallbacks(queryDeviceList);
        mHandler.post(queryDeviceList);
    }
}
