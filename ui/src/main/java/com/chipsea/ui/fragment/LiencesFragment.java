package com.chipsea.ui.fragment;

import android.os.Bundle;
import android.webkit.WebView;

import com.chipsea.ui.R;

/**
 * Created by lixun on 2016/7/1.
 */
public class LiencesFragment extends LazyFragment {
    private static final String TAG = "LiencesActivity";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentSub(R.layout.activity_lience,R.string.settingAgreement);
        WebView webview = (WebView) mParentView.findViewById(R.id.license);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("file:///android_asset/agreement.html");
    }
}
