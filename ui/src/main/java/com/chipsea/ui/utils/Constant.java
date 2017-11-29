package com.chipsea.ui.utils;

import android.content.Context;

import com.chipsea.mode.entity.TimerInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：HWQ on 2017/11/13 09:17
 * 描述：
 */

public class Constant {
    public static final String DPID     = "timer";
    public static final String RSSI_LVL = "rssi_lvl";

    public static final byte   CMD_1 = 19;//13H 主动请求
    public static final byte   CMD_3 = 20;//设备上报
    public static final String CMD_2 = "14";

    public static final byte   TIMER_CMD_1 = 05;//05H
    public static final byte   TIMER_CMD_2 = 06;//06H
    public static final String TIMER_FLAG  = "04";
    public static final String FLAG        = "07";

    public static final byte VERSION  = 01;//01H
    public static final byte RESERVED = 00;//00H

    public static final byte READ  = 02;
    public static final byte WRITE = 01;
    public static final byte DEVST = 01;

    public static final byte TIMER_ANDROID_LIGHT = (byte) 07;
    public static final byte TIMER_ACTION_OPEN   = (byte) 01;
    public static final byte TIMER_ACTION_CLOSE  = (byte) 06;

    public static final byte TIMER_LIGHT_OPEN  = (byte) 04;
    public static final byte TIMER_LIGHT_CLOSE = (byte) 03;

    public static final byte TIMER_TYPE_ONE_FLAG         = 00; //一次性
    public static final byte TIMER_TYPE_FLAG_EVERY_DAY   = 127; //每天
    public static final byte TIMER_TYPE_FLAG_WORKING_DAY = 31;//工作日
    public static final byte TIMER_TYPE_FLAG_WEEK_DAY    = 96;//周末

    public static final byte ACTION_ADD_OR_EDIT = 00;
    public static final byte ACTION_DEL         = 01;

    public static final byte COUNTDOWN_ID = -2;// FE H

    public static final int    SEND_SN = 5;
    public static final String DEVICE  = "device";


    public static List<TimerInfo> list = new ArrayList<>();
    public static TimerInfo sTimerInfo;

    public static List<String> getAddTimingType(Context context) {
        List<String> result = new ArrayList<>();
        result.add(context.getResources().getString(com.chipsea.code.R.string.timingExecutiveOne));
        result.add(context.getResources().getString(com.chipsea.code.R.string.timingEveryday));
        result.add(context.getResources().getString(com.chipsea.code.R.string.timingWorkday));
        result.add(context.getResources().getString(com.chipsea.code.R.string.timingWeekend));
        result.add(context.getResources().getString(com.chipsea.code.R.string.timingCustom));
        return result;
    }

    public static List<String> getWeekStrConstact(Context context) {
        List<String> result = new ArrayList<>();
        result.add(context.getResources().getString(com.chipsea.code.R.string.timingMonday));
        result.add(context.getResources().getString(com.chipsea.code.R.string.timingTuesday));
        result.add(context.getResources().getString(com.chipsea.code.R.string.timingWednesday));
        result.add(context.getResources().getString(com.chipsea.code.R.string.timingThursday));
        result.add(context.getResources().getString(com.chipsea.code.R.string.timingFriday));
        result.add(context.getResources().getString(com.chipsea.code.R.string.timingSaturday));
        result.add(context.getResources().getString(com.chipsea.code.R.string.timingWeekday));
        return result;
    }

}
