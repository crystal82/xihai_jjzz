package com.chipsea.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chipsea.code.listener.DeviceRecyclerviewCallback;
import com.chipsea.code.util.LogUtil;
import com.chipsea.code.util.NetUtils;
import com.chipsea.code.util.PrefsUtil;
import com.chipsea.ui.R;
import com.chipsea.ui.activity.ControlLightActivity;
import com.chipsea.ui.activity.DeviceListEvent;
import com.chipsea.ui.activity.DeviceTypeActivity;
import com.chipsea.ui.activity.MainActivity;
import com.chipsea.ui.activity.ControlSocketActivity;
import com.chipsea.ui.adapter.DeviceRecyclerAdapter;
import com.chipsea.ui.dialog.BottomItemDialog;
import com.chipsea.view.VerticalSwipeRefreshLayout;
import com.chipsea.view.utils.RecyclerItemDecoration;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import chipsea.wifiplug.lib.GosDeploy;
import zxing.CaptureActivity;

import static android.app.Activity.RESULT_OK;


public class DeviceListFragment extends LazyFragment implements DeviceRecyclerviewCallback, SwipeRefreshLayout
        .OnRefreshListener {
    private ViewHolder            mViewHolder;
    private DeviceRecyclerAdapter adapter;
    private List<Object>          dates;
    private Handler mHandler       = new Handler();
    private long    refreshInteval = 15000;
    private BottomItemDialog           bottomItemDialog;
    private List<String>               mProductKeyList;
    private ArrayList<String>          mBoundMessage;
    private MainActivity               mActivity;
    private String                     mUid;
    private String                     mToken;
    private VerticalSwipeRefreshLayout swipeRefreshView;

    @Override
    public void onRefresh() {
        if (!swipeRefreshView.isRefreshing())
            swipeRefreshView.setRefreshing(true);
        mHandler.removeCallbacks(queryDeviceList);
        mHandler.post(queryDeviceList);
    }

    private class ViewHolder {
        RecyclerView deviceRecyclerView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentSub(R.layout.fragment_device_list, R.string.mainDeviceList);
        mActivity = (MainActivity) getActivity();
        initData();
        dates = new ArrayList<>();
        mViewHolder = new ViewHolder();
        swipeRefreshView = (VerticalSwipeRefreshLayout) mParentView.findViewById(R.id.srl);
        swipeRefreshView.setOnRefreshListener(this);

        mViewHolder.deviceRecyclerView = (RecyclerView) mParentView.findViewById(R.id.deviceRecyclerView);
        adapter = new DeviceRecyclerAdapter(getActivity(), this, mProductKeyList);
        adapter.setDatas(dates);
        mViewHolder.deviceRecyclerView.setAdapter(adapter);
        mViewHolder.deviceRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mViewHolder.deviceRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager
                .VERTICAL, false));
        mViewHolder.deviceRecyclerView.addItemDecoration(new RecyclerItemDecoration(getActivity(),
                                                                                    LinearLayoutManager.VERTICAL));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(DeviceListEvent event) {
        LogUtil.d("------MyDevice onDataSynEvent------");
        swipeRefreshView.setRefreshing(false);
        onDataBack(event.getDeviceList());
        //定时请求数据轮询
        mHandler.removeCallbacks(queryDeviceList);
        mHandler.postDelayed(queryDeviceList, 10 * 1000);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dates == null || dates.size() == 0) {
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

    private void initData() {
        mBoundMessage = new ArrayList<>();
        mProductKeyList = GosDeploy.setProductKeyList();
        mUid = PrefsUtil.getStringValue(PrefsUtil.KEY_USER_UID, "");
        mToken = PrefsUtil.getStringValue(PrefsUtil.KEY_USER_TOKEN, "");
    }

    private void onDataBack(List<GizWifiDevice> deviceList) {
        LogUtil.d("---onDataBack---:" + deviceList.size());
        if (this.isAdded()) {
            dates.clear();
            if (deviceList != null) {
                List<GizWifiDevice> device1 = new ArrayList<>();
                List<GizWifiDevice> device2 = new ArrayList<>();
                List<GizWifiDevice> device3 = new ArrayList<>();
                for (GizWifiDevice gizWifiDevice : deviceList) {
                    if (!gizWifiDevice.isBind()) {
                        continue;
                    }
                    if (gizWifiDevice.getProductKey().equals(mProductKeyList.get(0))) {
                        device1.add(gizWifiDevice);
                    } else if (gizWifiDevice.getProductKey().equals(mProductKeyList.get(1))) {
                        device2.add(gizWifiDevice);
                    } else if (gizWifiDevice.getProductKey().equals(mProductKeyList.get(2))) {
                        device3.add(gizWifiDevice);
                    }
                }

                if (device1.size() > 0) {
                    dates.add("PowerCube|Smart Home|");
                    dates.addAll(device1);
                }
                if (device2.size() > 0) {
                    dates.add("LightCube|Smart Home|");
                    dates.addAll(device2);
                }
                if (device3.size() > 0) {
                    dates.add("SmartCube|Timer|");
                    dates.addAll(device3);
                }
            }
        }
        dates.add("add");
        adapter.setDatas(dates);
    }

    private Runnable queryDeviceList = new Runnable() {
        @Override
        public void run() {
            LogUtil.d("queryDeviceList：" + MainActivity.sAutoLogin + "   " + (!mUid.isEmpty() && !mToken.isEmpty()));
            if (MainActivity.sAutoLogin) {
                return;
            }
            //TODO:请求列表
            if (!mUid.isEmpty() && !mToken.isEmpty()) {
                GizWifiSDK.sharedInstance().getBoundDevices(mUid, mToken, mProductKeyList);
            }
        }
    };

    @Override
    public void onDeviceItem(GizWifiDevice gizWifiDevice) {
        if (gizWifiDevice.isOnline()) {
            goToControlPage(gizWifiDevice);
        } else {
            showToast(getString(R.string.DEVICE_STATE_OUTLINE));
        }
    }

    @Override
    public void addDevice() {
        //显示，添加二维码
        if (bottomItemDialog == null) {
            bottomItemDialog = new BottomItemDialog(instance,
                                                    getResources().getString(R.string.add_device_tip1),
                                                    getResources().getString(R.string.add_device_tip2));
            bottomItemDialog.setOnClickListener(DeviceListFragment.this);
        }
        bottomItemDialog.show();
    }

    //订阅，进入控制
    private void goToControlPage(GizWifiDevice gizWifiDevice) {
        showLoadDialog();
        mViewHolder.deviceRecyclerView.setEnabled(false);
        mViewHolder.deviceRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewHolder.deviceRecyclerView.setEnabled(true);
            }
        }, 3000);
        gizWifiDevice.setListener(gizWifiDeviceListener);
        String productKey = gizWifiDevice.getProductKey();

        if (mProductKeyList.contains(productKey)) {
            gizWifiDevice.setSubscribe(GosDeploy.getProductSecret(productKey), true);
        } else {
            gizWifiDevice.setSubscribe(true);
        }
    }

    private Intent getIntentByProductKey(String product) {
        if (product.equals(mProductKeyList.get(1))) {
            return new Intent(mActivity, ControlLightActivity.class);
        } else {
            return new Intent(mActivity, ControlSocketActivity.class);
        }
    }

    /**
     * 设备订阅回调
     */
    protected GizWifiDeviceListener gizWifiDeviceListener = new GizWifiDeviceListener() {
        // 用于设备订阅
        public void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
            // TODO 控制页面跳转
            Log.e("liang", "接收到数据didSetSubscribe:" + result);
            dismissLoadDialog();
            if (GizWifiErrorCode.GIZ_SDK_SUCCESS == result) {
                Intent intent = getIntentByProductKey(device.getProductKey());
                Bundle bundle = new Bundle();
                bundle.putParcelable("GizWifiDevice", device);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            } else {
                if (device.isBind()) {
                    mActivity.showToast(mActivity.toastError(result));
                }
            }
        }
    };

    @Override
    protected void onOtherClick(View v) {
        super.onOtherClick(v);
        if (v.getId() == R.id.item1) {
            //添加设备  AddDeviceActivity
            if (!NetUtils.isWifiConnected(getActivity())) {
                showToast(R.string.please_connect_wifi);
                return;
            }
            Intent intent = new Intent(getActivity(), DeviceTypeActivity.class);
            getActivity().startActivity(intent);
        } else if (v.getId() == R.id.item2) {
            //二维码
            Intent intent = new Intent(getActivity(), CaptureActivity.class);
            startActivityForResult(intent, 100);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
        }
    }
}
