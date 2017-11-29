package com.uascent.jz.xinhai;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chipsea.code.util.JLog;
import com.chipsea.ui.activity.SplashActivity;
import com.chipsea.ui.photoUtils.ImageLoadingUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, SplashActivity.class));
        JLog.e("InitActivity", "InitActivity");
        //startActivity(new Intent(this,TestActivity.class)); test
        finish();
    }

}
