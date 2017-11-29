package com.chipsea.code.util;


import android.content.Context;

import com.chipsea.code.R;
import com.chipsea.mode.entity.StatEntity;
import com.chipsea.mode.entity.XHelpEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xulj on 2016/3/10.
 */
public class Constant {
    public static final long ONE_DAY_MS = 24 * 3600L * 1000 ;
    public static final int[] STAT_FINAL_DATA = {66,72,78,84,90,100} ;
    public static final int TOKEN_ERROR_CODE1 = 3516 ;
    public static final int TOKEN_ERROR_CODE2 = 3014 ;
    public static final int TOKEN_ERROR_CODE3 = 3015 ;
    public static final int TOKEN_ERROR_CODE4 = 3514 ;
    public static final String[] STAT_FINAL_DAY_TIME = {"00:00","04:00","08:00","12:00","16:00","20:00","24:00"} ;
    public static List<StatEntity> getDayPointData(){
        List<StatEntity> result = new ArrayList<>() ;
        result.add(new StatEntity(0,80));
        result.add(new StatEntity(4,85));
        result.add(new StatEntity(9,90));
        result.add(new StatEntity(12,88));
        result.add(new StatEntity(17,77));
        result.add(new StatEntity(20,75));
        result.add(new StatEntity(23,72));
        return result ;
    }
    public static List<XHelpEntity> getDayXHelpEntity(){
        List<XHelpEntity> result = new ArrayList<XHelpEntity>() ;
        result.add(new XHelpEntity(0,STAT_FINAL_DAY_TIME[0]));
        result.add(new XHelpEntity(3,STAT_FINAL_DAY_TIME[1]));
        result.add(new XHelpEntity(7,STAT_FINAL_DAY_TIME[2]));
        result.add(new XHelpEntity(11,STAT_FINAL_DAY_TIME[3]));
        result.add(new XHelpEntity(15,STAT_FINAL_DAY_TIME[4]));
        result.add(new XHelpEntity(19,STAT_FINAL_DAY_TIME[5]));
        result.add(new XHelpEntity(23,STAT_FINAL_DAY_TIME[6]));
        return result ;
    }

   public static List<String> getAddTimingType(Context context){
       List<String> result = new ArrayList<>() ;
       result.add(context.getResources().getString(R.string.timingExecutiveOne));
       result.add(context.getResources().getString(R.string.timingEveryday));
       result.add(context.getResources().getString(R.string.timingWorkday));
       result.add(context.getResources().getString(R.string.timingWeekend));
       result.add(context.getResources().getString(R.string.timingCustom));
       return result ;
   }
    public static List<String> getCountDownData(Context context){
        List<String> result = new ArrayList<>() ;
        result.add(context.getResources().getString(R.string.timingNotStart));
        result.add(context.getResources().getString(R.string.timingThirtyMinute));
        result.add(context.getResources().getString(R.string.timingFortyFiveMinute));
        result.add(context.getResources().getString(R.string.timingSixty));
        result.add(context.getResources().getString(R.string.timingCustomTime));
        return result ;
    }
    public static List<String> getWeekStrConstact(Context context){
        List<String> result = new ArrayList<>() ;
        result.add(context.getResources().getString(R.string.timingMonday));
        result.add(context.getResources().getString(R.string.timingTuesday));
        result.add(context.getResources().getString(R.string.timingWednesday));
        result.add(context.getResources().getString(R.string.timingThursday));
        result.add(context.getResources().getString(R.string.timingFriday));
        result.add(context.getResources().getString(R.string.timingSaturday));
        result.add(context.getResources().getString(R.string.timingWeekday));
        return result ;
    }
}
