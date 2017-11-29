package com.chipsea.ui.utils;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;

import com.chipsea.ui.R;


/**
 * Created by Administrator on 2017/3/16.
 */

public class PointLayoutUtils {
    private LinearLayout pointLayout ;
    private int pointIndex  ;
    private boolean isStop  ;
    private Handler pointHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            refreshPoint() ;
        }
    } ;
    public void startAnim(){
        pointIndex = -1 ;
        isStop = false ;
        new Thread(runnable).start();
    }
    public void stopAnio(){
        this.isStop = true ;
    }
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (!isStop){
                try {
                    Thread.sleep(300);
                    pointIndex++ ;
                    pointHandler.sendEmptyMessage(-1) ;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    public void refreshPoint(){
        int count = pointLayout.getChildCount() ;
        for (int i = 0; i < count ; i++) {
            View child = pointLayout.getChildAt(i) ;
            if(i == pointIndex % 4){
                child.setBackgroundResource(R.drawable.clean_round_box_on);
            }else {
                child.setBackgroundResource(R.drawable.clean_round_box_off);
            }
        }
    }
    public PointLayoutUtils(LinearLayout pointLayout){
        this.pointLayout = pointLayout ;
    }
}
