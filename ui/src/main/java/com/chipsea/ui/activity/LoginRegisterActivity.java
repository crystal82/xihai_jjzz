package com.chipsea.ui.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.chipsea.code.business.ActivityBusiness;
import com.chipsea.code.util.JLog;
import com.chipsea.code.util.LogUtil;
import com.chipsea.code.util.PrefsUtil;
import com.chipsea.ui.R;
import com.chipsea.ui.fragment.FragmentLogin;
import com.chipsea.ui.fragment.FragmentRegister;
import com.chipsea.view.text.CustomTextView;
import com.chipsea.view.utils.CustomToast;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;


public class LoginRegisterActivity extends CommonActivity implements View.OnClickListener {
    private static final String TAG = "LoginRegisterActivity";
    private ImageView closeImg;
    CustomTextView login;
    CustomTextView register;
    ImageView      loginIndex;
    private FragmentLogin    mLoginFragment;
    private FragmentRegister mRegisterFragment;
    private PrefsUtil        mPrefsUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_rigister);
        ActivityBusiness.getInstance().addActivity(this);
        setStatusBarColor(R.color.white);
        hintTitleBar();
        mPrefsUtil = PrefsUtil.getInstance(this);
        closeImg = (ImageView) findViewById(R.id.closeImg);
        login = (CustomTextView) findViewById(R.id.lr_login);
        register = (CustomTextView) findViewById(R.id.lr_register);
        loginIndex = (ImageView) findViewById(R.id.loginTag);
        closeImg.setOnClickListener(this);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        mLoginFragment = new FragmentLogin();
        mRegisterFragment = new FragmentRegister();
        int type = getIntent().getIntExtra("type", 0);
        onFragment(type);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 每次返回activity都要注册一次sdk监听器，保证sdk状态能正确回调
        GizWifiSDK.sharedInstance().setListener(gizWifiSDKListener);
    }

    private void onFragment(int type) {
        FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();
        if (type == 0) {
            login.scaleLarger();
            register.scaleSmaller();
            login.setTextColor(getResources().getColor(R.color.main_color));
            register.setTextColor(getResources().getColor(R.color.gray_text));
            mTransaction.replace(R.id.fragmnet_ll, mLoginFragment).commit();
        } else {
            login.scaleSmaller();
            register.scaleLarger();
            login.setTextColor(getResources().getColor(R.color.gray_text));
            register.setTextColor(getResources().getColor(R.color.main_color));
            mTransaction.replace(R.id.fragmnet_ll, mRegisterFragment).commit();
        }
        startTagAnima(type);
    }

    public void startTagAnima(int type) {
        float curTranslationX = loginIndex.getTranslationX();
        JLog.e(TAG, "curTranslationX = " + curTranslationX);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        if (type == 0) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(loginIndex, "translationX", curTranslationX, 0);
            animator.start();
        } else {
            ObjectAnimator animator = ObjectAnimator.ofFloat(loginIndex, "translationX", curTranslationX, screenWidth
                    / 2);
            animator.start();
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
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
        CustomToast.showToast(this, getString(R.string.keyback), Toast.LENGTH_SHORT);
    }

    @Override
    public void onClick(View v) {
        if (v == login) {
            onFragment(0);
        } else if (v == register) {
            onFragment(1);
        } else if (v == closeImg) {
            super.onBackPressed();
        }
    }

    private GizWifiSDKListener gizWifiSDKListener = new GizWifiSDKListener() {

        /** 用于用户登录 */
        @Override
        public void didUserLogin(GizWifiErrorCode result, String uid, String token) {
            mLoginFragment.closeDialog();
            LogUtil.d("didUserLogin--------:" + result + "  uid:" + uid + "   token:" + token);
            if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
                // 登录失败
                showToast(toastError(result));
            } else {
                // 登录成功
                showToast(R.string.toast_login_successful);

                MainActivity.sAutoLogin = false;
                mLoginFragment.saveInfo(uid, token);
                startActivity(new Intent(LoginRegisterActivity.this, MainActivity.class));
                finish();
            }
        }

        /** 用于手机验证码 */
        @Override
        public void didRequestSendPhoneSMSCode(GizWifiErrorCode result, String token) {
            LogUtil.d("didUserLogin--------:" + result + "   token:" + token);
            progressDialog.cancel();
            if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
                showToast(R.string.USER_GET_CODE_ERROR);
            } else {
                showToast(R.string.USER_GET_CODE_SUCCESS);
                mRegisterFragment.verifyStart();
            }
        }

        /** 用于用户注册 */
        @Override
        public void didRegisterUser(GizWifiErrorCode result, String uid, String token) {
            LogUtil.d("didUserLogin--------:" + result + "  uid:" + uid + "   token:" + token);
            progressDialog.cancel();
            if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
                showToast(toastError(result));
            } else {
                showToast(R.string.registerSuccess);
                //TODO:登录
                if (mRegisterFragment != null) {
                    LogUtil.d(mRegisterFragment.mPhone + "    psd:" + mRegisterFragment.mPassword);
                    GizWifiSDK.sharedInstance().userLogin(mRegisterFragment.mPhone, mRegisterFragment.mPassword);
                }
            }
        }

        /** 用于重置密码 */
        @Override
        public void didChangeUserPassword(GizWifiErrorCode result) {
        }

        /** 用于解绑推送 */
        @Override
        public void didChannelIDUnBind(GizWifiErrorCode result) {
        }

        /** 用于设置云端服务环境 */
        @Override
        public void didGetCurrentCloudService(GizWifiErrorCode result,
                                              java.util.concurrent.ConcurrentHashMap<String, String> cloudServiceInfo) {
        }

        @Override
        public void didNotifyEvent(com.gizwits.gizwifisdk.enumration.GizEventType eventType, Object eventSource,
                                   GizWifiErrorCode eventID, String eventMessage) {
        }
    };
}
