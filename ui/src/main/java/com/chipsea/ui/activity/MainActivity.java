package com.chipsea.ui.activity;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.chipsea.code.business.ActivityBusiness;
import com.chipsea.code.util.LogUtil;
import com.chipsea.code.util.PrefsUtil;
import com.chipsea.mode.entity.AccountInfo;
import com.chipsea.ui.R;
import com.chipsea.ui.fragment.AccountFragment;
import com.chipsea.ui.fragment.DeviceListFragment;
import com.chipsea.ui.fragment.MyDeviceFragment;
import com.gizwits.gizwifisdk.api.GizDeviceSharing;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizDeviceSharingListener;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import chipsea.wifiplug.lib.GosDeploy;
import zxing.CaptureActivity;

import static com.chipsea.ui.R.id.radioGroup;

public class MainActivity extends CommonActivity implements DrawerLayout.DrawerListener, RadioGroup
        .OnCheckedChangeListener {
    private ViewHolder mViewHolder;
    private PrefsUtil  mPrefsUtil;
    private int        index;
    public static        boolean sAutoLogin                = false;
    private static final int     REQUEST_CODE_SETTING      = 100;
    private static final int     REQUEST_ZXINGCODE_SETTING = 200;
    private       DeviceListFragment  mDeviceListFragment;
    private       AccountFragment     mAccountFragment;
    private       MyDeviceFragment    mMyDeviceFragment;
    private       List<GizWifiDevice> mDeviceList;
    public static List<String>        boundMessage;

    public List<GizWifiDevice> getDeviceList() {
        return mDeviceList;
    }

    private class ViewHolder {
        private DrawerLayout mDrawerLayout;
        private RadioGroup   radioGroup;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityBusiness.getInstance().addActivity(this);
        boundMessage = new ArrayList<String>();

        mPrefsUtil = PrefsUtil.getInstance(this);
        mDeviceList = new ArrayList<>();
        initView();
        if (sAutoLogin) {
            progressDialog.show();
            AccountInfo mAccountInfo = mPrefsUtil.getAccountInfo();
            GizWifiSDK.sharedInstance().userLogin(mAccountInfo.getUserName(), mAccountInfo.getPassword());
        }

        AndPermission.with(this)
                .requestCode(REQUEST_CODE_SETTING)
                .permission(Manifest.permission.ACCESS_FINE_LOCATION).rationale(new RationaleListener() {

            @Override
            public void showRequestPermissionRationale(int arg0, Rationale arg1) {
                AndPermission.rationaleDialog(MainActivity.this, arg1).show();
            }
        }).send();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GizWifiSDK.sharedInstance().setListener(gizWifiSDKListener);
        GizDeviceSharing.setListener(mGizDeviceSharingListener);

        LogUtil.e("-----------UpdateUI--------------------");
        if (boundMessage.size() != 0) {
            String uid   = PrefsUtil.getStringValue(PrefsUtil.KEY_USER_UID, "");
            String token = PrefsUtil.getStringValue(PrefsUtil.KEY_USER_TOKEN, "");
            progressDialog.show();
            if (boundMessage.size() == 2) {
                GizWifiSDK.sharedInstance().bindDevice(uid, token, boundMessage.get(0), boundMessage.get(1), null);
            } else if (boundMessage.size() == 1) {
                GizWifiSDK.sharedInstance().bindDeviceByQRCode(uid, token, boundMessage.get(0));
            } else if (boundMessage.size() == 3) {
                GizDeviceSharing.checkDeviceSharingInfoByQRCode(token, boundMessage.get(2));
            } else {
                LogUtil.e("Apptest", "ListSize:" + boundMessage.size());
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        boundMessage.clear();
    }

    GizDeviceSharingListener mGizDeviceSharingListener = new GizDeviceSharingListener() {

        @Override
        public void didCheckDeviceSharingInfoByQRCode(GizWifiErrorCode result, String userName, String productName,
                                                      String deviceAlias, String expiredAt) {
            super.didCheckDeviceSharingInfoByQRCode(result, userName, productName, deviceAlias, expiredAt);
            LogUtil.d("----didCheckDeviceSharingInfoByQRCode----");
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            int errorcode = result.ordinal();

            if (8041 <= errorcode && errorcode <= 8050 || errorcode == 8308) {
                showToast(getResources().getString(R.string.sorry));
            } else if (errorcode != 0) {
                showToast(getResources().getString(R.string.verysorry));
            } else {
                Intent tent = new Intent(MainActivity.this, GosZxingDeviceSharingActivity.class);

                tent.putExtra("userName", userName);
                tent.putExtra("productName", productName);
                tent.putExtra("deviceAlias", deviceAlias);
                tent.putExtra("expiredAt", expiredAt);
                tent.putExtra("code", boundMessage.get(2));

                startActivity(tent);
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mViewHolder = new ViewHolder();
        mViewHolder.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        mViewHolder.mDrawerLayout.setDrawerListener(this);
        mViewHolder.radioGroup = (RadioGroup) findViewById(radioGroup);
        mViewHolder.radioGroup.setOnCheckedChangeListener(this);
        switchContent(0);
    }

    /**
     * 跳转视图
     *
     * @param index
     */
    private void switchContent(int index) {
        this.index = index;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        closeMenu();
        switch (index) {
            case 0:
                if (mDeviceListFragment == null) {
                    mDeviceListFragment = new DeviceListFragment();
                }
                transaction.replace(R.id.fragment_layout, mDeviceListFragment);
                break;
            case 1:
                if (mAccountFragment == null) {
                    mAccountFragment = new AccountFragment();
                }
                transaction.replace(R.id.fragment_layout, new AccountFragment());
                break;
            case 2:
                if (mMyDeviceFragment == null) {
                    mMyDeviceFragment = new MyDeviceFragment();
                }
                transaction.replace(R.id.fragment_layout, new MyDeviceFragment());
                break;
        }
        transaction.commit();
    }


    /**
     * 关闭菜单栏
     */
    public void closeMenu() {
        if (mViewHolder.mDrawerLayout != null && mViewHolder.mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mViewHolder.mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
            });
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.firstRb) {
            switchContent(0);
        } else if (checkedId == R.id.accountRb) {
            switchContent(1);
        } else if (checkedId == R.id.deviceRb) {
            switchContent(2);
        }
        mViewHolder.radioGroup.check(0);
    }

    /**
     * 打开菜单栏
     */
    public void openMenu() {
        if (mViewHolder.mDrawerLayout != null) {
            mViewHolder.mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }


    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (mViewHolder.mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            closeMenu();
            return;
        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
        if (index == 0) {
            show(getString(R.string.keyback));
        } else {
            if (!mViewHolder.mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mViewHolder.mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        }
    }

    public void show(Object paramObject) {
        Toast.makeText(this, paramObject.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSucceed(int requestCode, List<String> grantPermissions) {
        super.onSucceed(requestCode, grantPermissions);
        switch (requestCode) {
            case REQUEST_CODE_SETTING:
                break;
            case REQUEST_ZXINGCODE_SETTING:
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailed(int requestCode, List<String> deniedPermissions) {
        super.onFailed(requestCode, deniedPermissions);
        {
            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, deniedPermissions)) {
                // 第一种：用默认的提示语。
                AndPermission.defaultSettingDialog(MainActivity.this, REQUEST_CODE_SETTING).show();
            }

        }
    }

    //回调方法
    private GizWifiSDKListener gizWifiSDKListener = new GizWifiSDKListener() {

        /** 用于设备列表 */
        public void didDiscovered(GizWifiErrorCode result, java.util.List<GizWifiDevice> deviceList) {
            //没登录不设置数据
            progressDialog.cancel();
            LogUtil.d(result + "---didDiscoveredSuccess:---" + deviceList.size());
            if (GizWifiErrorCode.GIZ_SDK_SUCCESS == result) {
                mDeviceList = deviceList;
                EventBus.getDefault().post(new DeviceListEvent(result, deviceList));
            }
        }

        /** 用于用户匿名登录 */
        public void didUserLogin(GizWifiErrorCode result, java.lang.String uid, java.lang.String token) {
            LogUtil.d("didUserLogin--------:" + result + "  uid:" + uid + "   token:" + token);
            if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
                // 登录失败
                progressDialog.cancel();
                MainActivity.sAutoLogin = true;
                showToast(toastError(result));
                Intent intent = new Intent(MainActivity.this, LoginRegisterActivity.class);
                startActivity(intent);
                ActivityBusiness.getInstance().finishAllActivity();
            } else {
                // 登录成功
                showToast(R.string.toast_login_successful);
                PrefsUtil.seAutoLoginInfo(uid, token);
                GizWifiSDK.sharedInstance().getBoundDevices(uid, token,
                                                            GosDeploy.setProductKeyList());
                MainActivity.sAutoLogin = false;
            }
        }

        /**密码修改*/
        public void didChangeUserPassword(final GizWifiErrorCode result) {
            LogUtil.d("=====didChangeUserPassword=====:" + result);
            if (result.getResult() == 0) {
                showToast(getResources().getString(R.string.passsuccess));
                mAccountFragment.onRePsdSuccess();
            } else {
                if (result.getResult() == 9020) {
                    showToast(getResources().getString(R.string.oldpasserror));
                } else {
                    showToast(getResources().getString(R.string.passerror));
                }
            }
        }
    };
}
