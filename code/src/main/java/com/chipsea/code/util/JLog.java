package com.chipsea.code.util;

import android.util.Log;

/**
 * 打印类，控制打包发布时应用日志的输出
 * Created by Administrator on 13-10-11.
 */
public class JLog {

    private static final boolean DEBUG = true;

    public static void d(String tag, String msg){
        if (DEBUG) Log.d(tag,msg);
    }

    public  static void e(String tag, String msg){
        if (DEBUG) Log.e(tag,msg);
    }

    public static void v(String tag, String msg){
        if (DEBUG) Log.v(tag,msg);
    }


    public static void w(String tag, String msg) {
        if (DEBUG)
            Log.w(tag, msg);
    }

    public static String getStackTraceString(Exception e) {
        return Log.getStackTraceString(e);
    }

    public static void w(String tag, String msg, Exception e) {
        if (DEBUG)
            Log.w(tag, msg,e);
    }


}
