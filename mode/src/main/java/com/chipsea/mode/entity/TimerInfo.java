package com.chipsea.mode.entity;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * 作者：HWQ on 2017/11/13 09:12
 * 描述：
 */

public class TimerInfo implements Serializable, Comparable<TimerInfo> {
    private byte timerId;
    private byte timerZone;
    private byte action;
    private byte timerAction = 3;//00000011
    private byte    weekFlag;
    private byte[]  deltaTime;
    private String  time;
    private boolean isUseTime; //定时器有效
    private boolean isOpenTime; //开关
    private boolean isWakeUpTime; //唤醒

    public boolean isUseTime() {
        return (timerAction & 2) == 2;  //10 第二位
    }

    public void setUseTime(boolean useTime) {
        isUseTime = useTime;
        if (useTime) {//10
            timerAction = (byte) (timerAction | 2);
        } else {
            timerAction = (byte) (timerAction & 253);
        }
    }

    public boolean isOpenTime() {
        return (timerAction & 1) == 1;  //01 第二位

    }

    public void setOpenTime(boolean openTime) {
        isOpenTime = openTime;
    }

    public boolean isWakeUpTime() {
        return (timerAction & 4) == 4;  //10 第二位
    }

    public void setWakeUpTime(boolean wakeUpTime) {
        isWakeUpTime = wakeUpTime;
    }

    public byte getTimerId() {
        return timerId;
    }

    public void setTimerId(byte timerId) {
        this.timerId = timerId;
    }

    public byte getTimerZone() {
        return timerZone;
    }

    public void setTimerZone(byte timerZone) {
        this.timerZone = timerZone;
    }

    public byte getAction() {
        return action;
    }

    public void setAction(byte action) {
        this.action = action;
    }

    public byte getTimerAction() {
        return timerAction;
    }

    public void setTimerAction(byte timerAction) {
        this.timerAction = timerAction;
    }

    public byte getWeekFlag() {
        return weekFlag;
    }

    public void setWeekFlag(byte weekFlag) {
        this.weekFlag = weekFlag;
    }

    public byte[] getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(byte[] deltaTime) {
        this.deltaTime = deltaTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String toString() {

        return "timerId=" + timerId
                + " timerZone=" + timerZone
                + " timerAction=" + timerAction
                + " weekFlag=" + weekFlag
                + " deltaTime="
                + Arrays.toString(deltaTime)
                + " time=" + time;
    }

    @Override
    public int compareTo(TimerInfo another) {
        return this.time.compareTo(another.getTime());
    }
}

