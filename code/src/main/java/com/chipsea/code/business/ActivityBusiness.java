package com.chipsea.code.business;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.chipsea.code.util.LogUtil;

import java.util.Stack;

/**
 * Created by lixun on 2016/6/22.
 */
public class ActivityBusiness { // Activity栈
    private static Stack<Activity> activityStack;
    // 单例模式
    private static ActivityBusiness instance;

    private ActivityBusiness() {
    }

    /**
     * 单一实例
     */
    public static ActivityBusiness getInstance() {
        if (instance == null) {
            instance = new ActivityBusiness();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
        LogUtil.e("ActivityBusiness","addActivity:" + activityStack.size());
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
            LogUtil.e("ActivityBusiness","finishActivity:" + activityStack.size());
        }
    }

    public boolean finishActivityEx(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            LogUtil.e("ActivityBusiness","finishActivityEx:" + activityStack.size());
            if(activityStack.size()==0){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }

    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if(activityStack == null )
            return;
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
        LogUtil.e("ActivityBusiness","finishAllActivity:" + activityStack.size());
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }
}