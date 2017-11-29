package com.chipsea.code.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.chipsea.code.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import chipsea.wifiplug.lib.util.BytesUtil;

/**
 * Created by lixun on 2016/6/22.
 */
public class StandardUtil {
    private static StandardUtil instance;

    private Context context;

    public StandardUtil(Context context) {
        this.context = context;
    }

    public static StandardUtil getInstance(Context context) {
        if (instance == null) {
            instance = new StandardUtil(context);
        } else {
            instance.context = context;
        }

        return instance;
    }


    /**
     * 将循环设置转为中文描述(之后要考虑国际化)
     *
     * @return 中文描述
     */
    public String getLoopDescrption(boolean isLoop, byte loopSetting) {
        String sRet = "";

        if (isLoop) {
            if (loopSetting == (byte) 0xFF) {
                sRet = context.getString(R.string.timingEveryday);//"每天";
            } else if (loopSetting == (byte) 0xC1) {
                sRet = context.getString(R.string.timingWeekend);   //"周未";
            } else if (loopSetting == (byte) 0x3F) {
                sRet = context.getString(R.string.timingWorkday); //"工作日";
            } else {
                String sSet = BytesUtil.byteToBit(loopSetting);
                if (sSet.substring(6, 7).equals("1")) {
                    sRet = context.getString(R.string.timingMonday);  //"周一";
                }

                if (sSet.substring(5, 6).equals("1")) {
                    if (sRet.length() > 0) {
                        sRet = sRet + " " + context.getString(R.string.timingTuesday);
                    } else {
                        sRet = context.getString(R.string.timingTuesday);//"周二";
                    }
                }

                if (sSet.substring(4, 5).equals("1")) {
                    if (sRet.length() > 0) {
                        sRet = sRet + " " + context.getString(R.string.timingWednesday);
                    } else {
                        sRet = context.getString(R.string.timingWednesday);//"周三";
                    }
                }

                if (sSet.substring(3, 4).equals("1")) {
                    if (sRet.length() > 0) {
                        sRet = sRet + " " + context.getString(R.string.timingThursday);
                    } else {
                        sRet = context.getString(R.string.timingThursday);//"周四";
                    }
                }

                if (sSet.substring(2, 3).equals("1")) {
                    if (sRet.length() > 0) {
                        sRet = sRet + " " + context.getString(R.string.timingFriday);
                    } else {
                        sRet = context.getString(R.string.timingFriday);//"周五";
                    }
                }

                if (sSet.substring(1, 2).equals("1")) {
                    if (sRet.length() > 0) {
                        sRet = sRet + " " + context.getString(R.string.timingSaturday);
                    } else {
                        sRet = context.getString(R.string.timingSaturday);//"周六";
                    }
                }

                if (sSet.substring(0, 1).equals("1")) {
                    if (sRet.length() > 0) {
                        sRet = sRet + " " + context.getString(R.string.timingWeekday);
                    } else {
                        sRet = context.getString(R.string.timingWeekday);//"周天";
                    }
                }

            }
        } else {
            sRet = context.getString(R.string.timingExecutiveOne);//"执行一次";
        }

        return sRet;
    }

    /**
     * 手机号码是否合法
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^1\\d{10}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public void showToast(int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int resId, boolean isLong) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }

    public void showToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public boolean isNetworkConnected() {
        ConnectivityManager mConnMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo         mWifi    = mConnMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo         mMobile  = mConnMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean             flag     = false;
        if ((mWifi != null) && ((mWifi.isAvailable()) || (mMobile.isAvailable()))) {
            if ((mWifi.isConnected()) || (mMobile.isConnected())) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * Email是否合法
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        Pattern p = Pattern
                .compile("[A-Z0-9a-z._%+-]+@[A-Z0-9a-z._-]+\\.[A-Za-z]{2,4}");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 名字是否合法
     *
     * @param name
     * @return
     */
    public static boolean isNameOk(String name) {
        Pattern p = Pattern
                .compile("^[\u4E00-\u9FA5A-Za-z0-9_!\"#$%&'() *+,-/:;<>=?@\\^`{}|~！@￥……（）——。，‘’“”；：]+$");
        Matcher m = p.matcher(name);
        return m.matches();
    }

    public String getMessage(int code, String msg) {
        String sRet = msg;
        switch (code) {
            case 1993:
                sRet = context.getString(R.string.err_1993);
                break;
            case 1999:
                sRet = context.getString(R.string.err_1999);
                break;
            case 3000:
                sRet = context.getString(R.string.err_3000);
                break;
            case 3001:
                sRet = context.getString(R.string.err_3001);
                break;
            case 3002:
                sRet = context.getString(R.string.err_3002);
                break;
            case 3003:
                sRet = context.getString(R.string.err_3003);
                break;
            case 3004:
                sRet = context.getString(R.string.err_3004);
                break;
            case 3005:
                sRet = context.getString(R.string.err_3005);
                break;
            case 3006:
                sRet = context.getString(R.string.err_3006);
                break;
            case 3007:
                sRet = context.getString(R.string.err_3007);
                break;
            case 3008:
                sRet = context.getString(R.string.err_3008);
                break;
            case 3009:
                sRet = context.getString(R.string.err_3009);
                break;
            case 3010:
                sRet = context.getString(R.string.err_3010);
                break;
            case 3011:
                sRet = context.getString(R.string.err_3011);
                break;
            case 3012:
                sRet = context.getString(R.string.err_3012);
                break;
            case 3013:
                sRet = context.getString(R.string.err_3013);
                break;
            case 3014:
                sRet = context.getString(R.string.err_3014);
                break;
            case 3015:
                sRet = context.getString(R.string.err_3015);
                break;
            case 3501:
                sRet = context.getString(R.string.err_3501);
                break;
            case 3502:
                sRet = context.getString(R.string.err_3502);
                break;
            case 3503:
                sRet = context.getString(R.string.err_3503);
                break;
            case 3504:
                sRet = context.getString(R.string.err_3504);
                break;
            case 3505:
                sRet = context.getString(R.string.err_3505);
                break;
            case 3506:
                sRet = context.getString(R.string.err_3506);
                break;
            case 3507:
                sRet = context.getString(R.string.err_3507);
                break;
            case 3508:
                sRet = context.getString(R.string.err_3508);
                break;
            case 3509:
                sRet = context.getString(R.string.err_3509);
                break;
            case 3510:
                sRet = context.getString(R.string.err_3510);
                break;
            case 3511:
                sRet = context.getString(R.string.err_3511);
                break;
            case 3512:
                sRet = context.getString(R.string.err_3512);
                break;
            case 3514:
                sRet = context.getString(R.string.err_3514);
                break;
            case 3515:
                sRet = context.getString(R.string.err_3515);
                break;
            case 3516:
                sRet = context.getString(R.string.err_3516);
                break;
            case 3517:
                sRet = context.getString(R.string.err_3517);
                break;
            case 3518:
                sRet = context.getString(R.string.err_3518);
                break;
            case 3601:
                sRet = context.getString(R.string.err_3601);
                break;
            case 3602:
                sRet = context.getString(R.string.err_3602);
                break;
            case 3603:
                sRet = context.getString(R.string.err_3603);
                break;
            case 3604:
                sRet = context.getString(R.string.err_3604);
                break;
            case 3605:
                sRet = context.getString(R.string.err_3605);
                break;
            case 3606:
                sRet = context.getString(R.string.err_3606);
                break;
            case 3801:
                sRet = context.getString(R.string.err_3801);
                break;
            case 3802:
                sRet = context.getString(R.string.err_3802);
                break;
            case 3803:
                sRet = context.getString(R.string.err_3803);
                break;
            case 3804:
                sRet = context.getString(R.string.err_3804);
                break;
            case 3805:
                sRet = context.getString(R.string.err_3805);
                break;
            case 3806:
                sRet = context.getString(R.string.err_3806);
                break;
            case 3807:
                sRet = context.getString(R.string.err_3807);
                break;
            case 3808:
                sRet = context.getString(R.string.err_3808);
                break;
            case 3809:
                sRet = context.getString(R.string.err_3809);
                break;
            case 3811:
                sRet = context.getString(R.string.err_3811);
                break;
            case 3812:
                sRet = context.getString(R.string.err_3812);
                break;
            case 3813:
                sRet = context.getString(R.string.err_3813);
                break;
            case 3814:
                sRet = context.getString(R.string.err_3814);
                break;
            case 3815:
                sRet = context.getString(R.string.err_3815);
                break;
            case 3816:
                sRet = context.getString(R.string.err_3816);
                break;
            case 3817:
                sRet = context.getString(R.string.err_3817);
                break;
            case 3818:
                sRet = context.getString(R.string.err_3818);
                break;
            case 3819:
                sRet = context.getString(R.string.err_3819);
                break;
            case 3820:
                sRet = context.getString(R.string.err_3820);
                break;
            case 3821:
                sRet = context.getString(R.string.err_3821);
                break;
            case 3822:
                sRet = context.getString(R.string.err_3822);
                break;
            default:
                if (msg != null) {
                    sRet = context.getString(R.string.err_nodefine) + "[" + code + "]" + msg;
                } else {
                    sRet = context.getString(R.string.err_nodefine) + "[" + code + "]";
                }

                break;

        }
        return sRet;
    }
}
