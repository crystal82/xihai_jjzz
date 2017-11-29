package com.chipsea.ui.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chipsea.code.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 作者：HWQ on 2017/11/10 10:20
 * 描述：
 */

public class MyUtils {

    public static String fromatTExt(String data) {
        StringBuilder text = new StringBuilder();
        text.append(data.replace(" ", ""));
        int length = text.length();
        int sum    = (length % 2 == 0) ? (length / 2) - 1 : (length / 2);
        for (int offset = 2, index = 0; index < sum; offset += 3, index++) {
            text.insert(offset, ",");
        }
        return text.toString();
    }

    public static String getFormatDateTime(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    //校验密码，大小字母，数值，
    public static boolean checkPsd(String psd) {
        LogUtil.d("----密码---:" + psd);
        return !TextUtils.isEmpty(psd)
                && psd.length() >= 6
                && psd.length() <= 18
                && psd.matches(".*[a-z]{1,}.*")
                && psd.matches(".*[A-Z]{1,}.*")
                && psd.matches(".*\\d{1,}.*");
    }

    public abstract static class DialogEvent {
        public void onDataSet(View layout, AlertDialog dialog) {
        }

        public void onSureClick() {
        }

        public void onCancelClick() {
        }
    }

    //显示dialog
    public static AlertDialog showDialog(Activity activity, int layoutId, int viewGroupId, DialogEvent dialogAble) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(layoutId,
                                       (ViewGroup) activity.findViewById(viewGroupId));
        AlertDialog dialog = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_LIGHT).show();
        dialogAble.onDataSet(layout, dialog);//设置数据
        dialog.getWindow().setContentView(layout);
        return dialog;
    }

    public static void changeAppLanguage(Context context, String lan) {
        Locale         myLocale = new Locale(lan);
        Resources      res      = context.getResources();
        DisplayMetrics dm       = res.getDisplayMetrics();
        Configuration  conf     = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
}
