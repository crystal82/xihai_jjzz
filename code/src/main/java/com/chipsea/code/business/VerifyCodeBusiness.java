package com.chipsea.code.business;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.chipsea.code.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lixun on 2016/6/22.
 */
public class VerifyCodeBusiness {

    private Activity mContext;
    private static Timer mTimer;
    private static final int TIME_SEND = 0;
    private static final int TIME_END = 1;
    private TextView mTextView;
    private Dialog mDialog;

    public VerifyCodeBusiness(Activity context, TextView textView) {
        mContext = context;
        mDialog=null;
        mTextView = textView;
        mTimer = new Timer();
    }

    public VerifyCodeBusiness(Dialog context, TextView textView) {
        mDialog = context;
        mContext=null;
        mTextView = textView;
        mTimer = new Timer();
    }

    @SuppressLint("HandlerLeak")
    Handler timeHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_SEND:
                    mTextView.setText(msg.arg1 + "");
                    break;
                case TIME_END:
                    mTextView.setText(R.string.loginGetVerif);
                    mTextView.setClickable(true);
                    break;
                default:
                    break;
            }
        };
    };

    public void cancelTask(){
        if(mTimer != null){
            mTimer.cancel();
        }
    }

    public void startTimerforDialog() {
        mTimer.schedule(new TimerTask() {
            int sum = 600;
            @Override
            public void run() {
                while (sum >= 0) {
                    try {
                        Thread.sleep(1000);
                        sum--;
                        Message message = new Message();
                        message.what = TIME_SEND;
                        message.arg1 = sum;
                        timeHandler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                timeHandler.sendEmptyMessage(TIME_END);
            }
        }, 100);
    }

    /**
     * 开始计时
     */
    public void startTimer() {
        mTimer.schedule(new TimerTask() {
            int sum = 60;
            @Override
            public void run() {
                while (sum >= 0 && !mContext.isFinishing()) {
                    try {
                        Thread.sleep(1000);
                        sum--;
                        Message message = new Message();
                        message.what = TIME_SEND;
                        message.arg1 = sum;
                        timeHandler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                timeHandler.sendEmptyMessage(TIME_END);
            }
        }, 100);
    }
}