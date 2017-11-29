package com.chipsea.configuration;

/**
 * Created by lixun on 2016/6/21.
 */
public class Config {
    public static final String TAG="WIFIPLUG";

    public static final String MAJORDOAMIN = "chipsea";
    public static final long MAJORDOMAINID = 889;

    public static final String UDSSERVICENAME="WifiPlugUDS";
    public static final int UDSSERVICEVERSION=1;

    public static final String DEFAULTSUBDOMAIN="5155";

    //插座类型定义
    //智能插座--后面数字为SubDomainId
    public static final long SMART_SOCKET = 5793;
    //带计量智能插座
    public static final long SUPER_SMART_SOCKET= 892 ;
    //空调伴侣
    public static final long AC_PARTNER = 5145 ;
    //带计量空调伴侣
    public static final long SUPER_AC_PARTNER = 5144;
    //带按键空调伴侣(无计量)
    public static final long AC_PARTNER_WITHKEY = 5275 ;
    //设备默认名称
    public static final String DEF_DEVICE_NAME="Timmer";
    //帐号默认名称
    public static final String DEF_ACCOUNT_NAME="ALLO";


    public static String getSubDomainById(long subDomainId){
        String sRet="";
        if(subDomainId==892){
            sRet="wifiplug2016051301";
        }else if(subDomainId==5145){
            sRet="wifiplug2017020503";
        }else if(subDomainId==5144){
            sRet="wifiplug2017020502";
        }else if(subDomainId==5793){
            sRet="5155";
        }else if(subDomainId==5275){
            sRet="wifiplug2017031601";
        }
        return sRet;
    }

    //当前App运行模式 0--TEST 1--生产环境
    public static final int APP_MODE=0;
    //是否开启第三方登陆
    public static final boolean THIRDPART_LOGIN=false;
    
    //区域定义 0--中国 1--南亚  2-华东  3--北美 4-中欧
    public static final int APP_REGIONAL=0;

    /**
     * @Description 使用手机还是邮箱作为登陆认证
     */
    public static final  boolean USE_PHONE_FOR_LOGIN=true;

}
