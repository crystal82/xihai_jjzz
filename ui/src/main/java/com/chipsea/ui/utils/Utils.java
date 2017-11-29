package com.chipsea.ui.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.chipsea.code.util.HexStrUtils;
import com.chipsea.mode.entity.TimerInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 作者：HWQ on 2017/11/13 09:23
 * 描述：
 */

public class Utils {

    public static byte getCurrentTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        int z = createGmtOffsetString(true, true, tz.getRawOffset());
        return (byte) z;
    }

    public static int createGmtOffsetString(boolean includeGmt,
                                            boolean includeMinuteSeparator, int offsetMillis) {
        int offsetMinutes = offsetMillis / 60000;
        char sign = '+';
        if (offsetMinutes < 0) {
            sign = '-';
            offsetMinutes = -offsetMinutes;
        }
       /* StringBuilder builder = new StringBuilder(9);
        if (includeGmt) {
            builder.append("GMT");
        }
        builder.append(sign);*/
        //appendNumber(builder, 2, offsetMinutes / 60);
        Log.e("TAG", "1-" + offsetMinutes / 60);
        /*if (includeMinuteSeparator) {
            builder.append(':');
        }
        appendNumber(builder, 2, offsetMinutes % 60);
        Log.e("TAG","2-"+offsetMinutes / 60);*/
        if ((offsetMinutes / 60) < 10)
            return offsetMinutes / 60;
        else
            return offsetMinutes / 60;
    }

    private static void appendNumber(StringBuilder builder, int count, int value) {
        String string = Integer.toString(value);
        for (int i = 0; i < count - string.length(); i++) {
            builder.append('0');
        }
        builder.append(string);
        Log.e("TAG", "3-" + string);
    }


    public static byte getID() {

        List<String> idList = new ArrayList<>();
        for (TimerInfo t : Constant.list) {
            idList.add("" + t.getTimerId());
        }
        byte id = 0x01;
        for (int i = 1; i < 126; i++) {
            id = (byte) i;
            if (!idList.contains("" + id)) {
                break;
            }
        }
        Log.e("TAG", "----id=" + id);
        return id;
    }


    public static int getR(int hour, int min,int n) {
        Calendar cal = Calendar.getInstance();
        int newHour = cal.get(Calendar.HOUR_OF_DAY);
        int newMin = cal.get(Calendar.MINUTE);
        int newSec = cal.get(Calendar.SECOND);
        int delta = (hour - newHour) * 3600 + (min - newMin) * 60 - newSec+n*24*3600;
        return delta;
    }

    /**
     * 得到与当前时间的相对时间  若为负数则加一天
     *
     * @return
     */
    public static long getRelativeTime(String setTimer) {
        Date nowTime = new Date(System.currentTimeMillis());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        //Date setDate=format.parse("18:00");
        String tempTimer = format.format(nowTime).substring(0, 10) + " " + setTimer;
        //Log.d("testTimeUtil", nowTime.getTime() + "");
        try {
            long delay = (format.parse(tempTimer).getTime() - nowTime.getTime()) / 1000;
            if (delay <= 0)
                delay = 24 * 60 * 60 + delay;
            //Log.e("TAG", "delay=" + delay);
            return delay;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;

    }

    /**
     * 得到与当前时间的相对时间
     *
     * @return
     */
    public static long getRelativeTime2(String setTimer, int n) {
        Date nowTime = new Date(System.currentTimeMillis());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        //Date setDate=format.parse("18:00");
        String tempTimer = format.format(nowTime).substring(0, 10) + " " + setTimer;
        try {
            long delay = (format.parse(tempTimer).getTime() - nowTime.getTime()) / 1000;
            if (delay <= 0) {
                delay = 24 * 60 * 60 + delay;
                if (n != 1)
                    delay = delay + (n - 1) * 24 * 3600;
            } else
                delay = delay + n * 24 * 3600;
            Log.e("TAG", "delay=" + delay);
            return delay;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;

    }

    public static long getRelative(String setTimer) {
        Date nowTime = new Date(System.currentTimeMillis());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        //Date setDate=format.parse("18:00");
        String tempTimer = format.format(nowTime).substring(0, 10) + " " + setTimer;
        Log.d("testTimeUtil", nowTime.getTime() + "");
        try {
            long delay = (format.parse(tempTimer).getTime() - nowTime.getTime()) / 1000;
            if (delay <= 0) {
                return -1;
            }
            return delay;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;

    }

    /**
     * 十进制转换为十六进制
     *
     * @return String 十六进制字符串
     * @throws
     * @Title: convert10To16
     * @Description:
     * @param：[source:十进制数字]
     * @user： wangzg
     * @Date：2014-9-5
     */
    public static String convert10To16(int source) {
        String result = Integer.toHexString(source);
        return result;
    }

    public static void main(String[] args) {
        int relativeTime = (int) Utils.getRelativeTime(17 + ":" + 50);
        String s = Utils.convert10To16(relativeTime);
        byte[] bytes = HexStrUtils.hexStringToBytes(s);
        String time = Utils.stampToDate(System.currentTimeMillis() + Long.parseLong(HexStrUtils.bytesToHexString
                (bytes), 16) * 1000);
        System.out.println("----" + time + "----");
    }

    /**
     * 将16进制 转换成10进制
     *
     * @param str
     * @return
     */
    public static String print10(String str) {

        StringBuffer buff = new StringBuffer();
        String array[] = str.split(" ");
        for (int i = 0; i < array.length; i++) {
            int num = Integer.parseInt(array[i], 16);
            buff.append(String.valueOf((char) num));
        }
        return buff.toString();
    }


    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(long s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");

        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(long s, String fm) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fm);

        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static long date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            String ll = String.valueOf(sdf.parse(date_str).getTime());
            return Long.parseLong(ll);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 不满8位前面补0
     *
     * @param tim
     * @return
     */
    @NonNull
    public static String getString(String tim) {
        int l = tim.length();
        for (int i = 0; i < (8 - l); i++) {
            tim = "0" + tim;
        }
        return tim;
    }


}
