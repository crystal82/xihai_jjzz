package com.chipsea.ui.activity;

import android.os.Bundle;
import android.webkit.WebView;

import com.chipsea.code.business.ActivityBusiness;
import com.chipsea.ui.R;

/**
 * Created by lixun on 2016/7/1.
 */
public class LiencesActivity extends CommonActivity {
    private static final String TAG = "LiencesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentSub(R.layout.activity_lience,R.string.settingAgreement);
        ActivityBusiness.getInstance().addActivity(this);
        WebView webview = (WebView) findViewById(R.id.license);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("file:///android_asset/agreement.html");
    }
}
