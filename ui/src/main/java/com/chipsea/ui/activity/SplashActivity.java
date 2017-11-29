package com.chipsea.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;

import com.chipsea.code.util.PrefsUtil;
import com.chipsea.code.util.StandardUtil;
import com.chipsea.mode.entity.AccountInfo;
import com.chipsea.ui.GosBaseActivity;
import com.chipsea.ui.R;
import com.chipsea.ui.photoUtils.ImageLoadingUtils;

import java.io.File;


/**
 * class desc: 启动画面 (1)判断是否是首次加载应用--采取读取SharedPreferences的方法
 * (2)是，则进入GuideActivity；否，则进入MainActivity (3)3s后执行(2)操作
 */
public class SplashActivity extends GosBaseActivity {
    private PrefsUtil    mPrefsUtil;
    private StandardUtil mStandardUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        mPrefsUtil = PrefsUtil.getInstance(this);
        mStandardUtil = StandardUtil.getInstance(this);
        init();
    }

    protected void init() {
        File file = new File(ImageLoadingUtils.IMAGE_SAVE_CACHE);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(getSDPath() + "/picture");
        if (!file2.exists()) {
            file2.mkdirs();
        }


        if (!mStandardUtil.isNetworkConnected()) {
            mStandardUtil.showToast(R.string.loginNoNetworkTip, true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
            return;
        }

        PrefsUtil   instance    = PrefsUtil.getInstance(this);
        AccountInfo accountInfo = instance.getAccountInfo();
        if (TextUtils.isEmpty(accountInfo.getUserName()) || TextUtils.isEmpty(accountInfo.getPassword())) {
            goLoginActivity();
        } else {
            goMainActivity();
        }
    }

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

    private void goMainActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.sAutoLogin = true;
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
        }, 1500);
    }

    private void goLoginActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginRegisterActivity.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
        }, 2000);

    }
}
