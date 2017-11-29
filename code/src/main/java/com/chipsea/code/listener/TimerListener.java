package com.chipsea.code.listener;

import android.view.View;

import com.chipsea.mode.entity.TimerInfo;
import com.gizwits.gizwifisdk.api.GizDeviceScheduler;

/**
 * 作者：HWQ on 2017/11/10 10:32
 * 描述：
 */

public interface TimerListener {
    public void onRemove(TimerInfo timerInfo);
    public void onAdd();
    public void switchOnChanged(TimerInfo timerInfo) ;
}
