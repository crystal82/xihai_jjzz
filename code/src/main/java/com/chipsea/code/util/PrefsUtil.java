package com.chipsea.code.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.chipsea.mode.entity.AccountInfo;
import com.chipsea.mode.entity.AirModel;
import com.chipsea.mode.entity.DeviceInfo;
import com.gizwits.gizwifisdk.api.GizWifiDevice;


/**
 * Created by hfei on 2015/10/28.
 */
public class PrefsUtil {

    public static final String TAG = "PrefsUtil";
    private static PrefsUtil instance; // 当前实体类
    public static final String                   SP_NAME = "CSWIFIPLUG";
    public static       SharedPreferences        sprefer = null;
    public static       SharedPreferences.Editor sp_edit = null;
    private Context context;

    /**
     * 析构函数
     */
    public PrefsUtil(Context context) {
        this.context = context;
        sprefer = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp_edit = sprefer.edit();
    }

    /**
     * 得到当前实体类
     */
    public static PrefsUtil getInstance(Context context) {
        if (instance == null) {
            instance = new PrefsUtil(context);
        }
        return instance;
    }

    public static final String USER_IMG_PATH = "user_img_path";

    public static void setUserImgPath(String uid) {
        LogUtil.d("---setUserImgPath----:" + uid);
        setStringValue(USER_IMG_PATH, uid);
    }

    public static String getUserImgPath() {
        return getStringValue(USER_IMG_PATH, "");
    }


    public static final String KEY_USER_NAME  = "login_phone_name";
    public static final String KEY_USER_PSD   = "login_phone_psd";
    public static final String KEY_USER_UID   = "login_user_uid";
    public static final String KEY_USER_TOKEN = "login_user_token";

    public static final String KEY_USER_PHONE = "login_phone_name";
    public static final String KEY_USER_QQ    = "login_qq_name";
    public static final String KEY_USER_WEXIN = "login_wexin_name";
    public static final String KEY_USER_SINA  = "login_sina_name";

    public void setAccountInfo(String userName, String password, String uid, String token) {
        setStringValue(KEY_USER_NAME, userName);
        setStringValue(KEY_USER_PSD, password);
        setStringValue(KEY_USER_UID, uid);
        setStringValue(KEY_USER_TOKEN, token);
    }

    public static void seAutoLoginInfo(String uid, String token) {
        setStringValue(KEY_USER_UID, uid);
        setStringValue(KEY_USER_TOKEN, token);
    }

    public AccountInfo getAccountInfo() {
        AccountInfo info = new AccountInfo();
        info.setPassword(getStringValue(KEY_USER_PSD, ""));
        info.setToken(getStringValue(KEY_USER_TOKEN, ""));
        info.setUid(getStringValue(KEY_USER_UID, ""));
        info.setUserName(getStringValue(KEY_USER_NAME, ""));
        return info;
    }

    public void logoutToClean() {
        setStringValue(KEY_USER_NAME, "");
        setStringValue(KEY_USER_PSD, "");
        setStringValue(KEY_USER_UID, "");
        setStringValue(KEY_USER_TOKEN, "");
    }

    public String getId() {
        return "";
    }


    private static final String KEY_DEVICE_MAC         = "device_mac";
    private static final String KEY_DEVICE_PRODUCT_KEY = "key_device_product_key";
    private static final String KEY_DEVICE_NAME        = "device_name";
    private static final String KEY_DEVICE_NIKE_NAME   = "device_nike_name";
    private static final String KEY_DEVICE_STATUS      = "device_status";
    private static final String KEY_DEVICE_DID         = "device_did";

    public void setDeviceInfo(GizWifiDevice info) {
        //if (info == null)
        //    return;

        //setStringValue(KEY_DEVICE_MAC, info.getMacAddress());
        //setStringValue(KEY_DEVICE_PRODUCT_KEY, info.physicalDeviceId);
        //setStringValue(KEY_DEVICE_NAME, info.name);
        //setLongValue(KEY_DEVICE_NIKE_NAME, info.owner);
        //setIntValue(KEY_DEVICE_STATUS, info.status);
        //setLongValue(KEY_DEVICE_DID, info.subDominId);
    }

    public DeviceInfo getDeviceInfo() {
        DeviceInfo info = new DeviceInfo();
        return info;
        //String physicalId = getStringValue(KEY_DEVICE_PRODUCT_KEY, "");
        //if (physicalId.length() == 0) {
        //    return null;
        //} else {
        //    info.physicalDeviceId = physicalId;
        //    info.deviceId = getStringValue(KEY_DEVICE_MAC, "");
        //    info.name = getStringValue(KEY_DEVICE_NAME, "");
        //    info.owner = getLongValue(KEY_DEVICE_NIKE_NAME, 0);
        //    info.status = getIntValue(KEY_DEVICE_STATUS, 0);
        //    info.subDominId = getLongValue(KEY_DEVICE_DID, 0);
        //    return info;
        //}
    }

    /**
     * 获得键值对
     */
    public static String getStringValue(String key, String def_value) {
        if (sprefer != null) {
            return sprefer.getString(key, def_value);
        }
        return def_value;
    }

    public static Long getLongValue(String key, long def_value) {
        if (sprefer != null) {
            return sprefer.getLong(key, def_value);
        }
        return def_value;
    }

    public static int getIntValue(String key, int def_value) {
        if (sprefer != null) {
            return sprefer.getInt(key, def_value);
        }
        return def_value;
    }

    /**
     * 设置键值対
     */
    public static void setStringValue(String key, String value) {
        if (sp_edit != null) {
            sp_edit.putString(key, value);
            sp_edit.commit();
        }
    }

    public static void setLongValue(String key, long value) {
        if (sp_edit != null) {
            sp_edit.putLong(key, value);
            sp_edit.commit();
        }
    }

    public static void setIntValue(String key, int value) {
        if (sp_edit != null) {
            sp_edit.putInt(key, value);
            sp_edit.commit();
        }
    }

    /**
     * 移除键值対
     */
    public void clearValue(String key) {
        if (sp_edit != null) {
            sp_edit.remove(key);
            sp_edit.commit();
        }
    }

    public void saveAirModel(String physicalDeviceId, AirModel model) {
        //String value = JsonMapper.toJson(model);
        //setStringValue(physicalDeviceId, value);
    }

    public AirModel getAirModel(String physicalDeviceId) {
        //AirModel airModel = JsonMapper.fromJson(getStringValue(physicalDeviceId, ""), AirModel.class);

        return new AirModel();
    }

}
