package com.chipsea.code.util;

import android.content.Context;
import android.content.SharedPreferences;


import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * 配置文件操作类
 * Created by Administrator on 13-10-11.
 */
public class ConfigFileUtils {
    public static final  String CONFIGNAME = "chipsea_config" ;
    private static  final String tag = "ConfigFileUtils";

    /**
     *
     * @param context
     * @param fileName 文件名
     * @param key 存储数据对应的键
     * @param value 存储数据对应的值
     */
    public static  void save(Context context, String fileName, String key, String value){
    	if(context == null){
    		return  ;
    	}
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName,0);
        sharedPreferences.edit().putString(key,value).commit();
    }
    /**
     *  保存配置信息
     * @param context
     * @param fileName 文件名
     * @param map 需要存储的数据以键值数据格式
     */
    public static void save(Context context, String fileName, Map<String ,Object> map){

        Set set = map.entrySet();
        Iterator interator = set.iterator();
        while (interator.hasNext()){
            Map.Entry  entry =(Map.Entry)interator.next();
            JLog.v(tag,"Key:"+entry.getKey()+"Value:"+entry.getValue());
            save(context,fileName,entry.getKey().toString(),entry.getValue().toString());
        }
    }

    /**
     * 读取配置文件对应的值
     * @param context
     * @param fileName  配置文件名
     * @param key 需要读取的值对应的Key
     * @return  读取到的值
     */
    public  static String getConfigInfo(Context context, String fileName, String key){
    	if(context == null){
    		return "" ;
    	}
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName,0);
        String result = "";
        if(sharedPreferences!=null){
        	result = sharedPreferences.getString(key,"");
        }
        return result;
    }

    /**
     * 清除对应key字段
     */
    public  static  void clearConfigInfo(Context context, String fileName){
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName,0);
        sharedPreferences.edit().clear().commit() ;
    }

    /**
     *加密存储 字符串
     * @param context
     * @param key 存储数据对应的键
     * @param value 存储数据对应的值
     */
    public static  void saveSecure(Context context, String key, String value){
        SecurePreferences sharedPreferences = new SecurePreferences(context);
        sharedPreferences.edit().putString(key,value).commit();
    }
    /**
     * 读取加密数据字符串
     * @param context
     * @param key 需要读取的值对应的Key
     * @return  读取到的值
     */
    public  static String readSecureString(Context context, String key, String defaultValue){
    	SecurePreferences sharedPreferences = new SecurePreferences(context);
        String result = sharedPreferences.getString(key,defaultValue);
        return result;
    }
    /**
     *加密存储boolean值
     * @param context
     * @param key 存储数据对应的键
     * @param value 存储数据对应的值
     */
    public static  void saveSecureBoolean(Context context, String key, boolean value){
        SecurePreferences sharedPreferences = new SecurePreferences(context);
        sharedPreferences.edit().putBoolean(key,value).commit();
    }
    /**
     * 读取加密的boolean值
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    
    public static boolean readSecureBoolean(Context context, String key, boolean defaultValue){
    	SecurePreferences sharedPreferences = new SecurePreferences(context);
        boolean result = sharedPreferences.getBoolean(key, defaultValue);
        return result;
    }
    
}
