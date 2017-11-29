package com.chipsea.code.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class VersionUtils {
	public static String getVersionName(Context context){
		 String versionName = "";
		    try {  
		        // ---get the package info---  
		        PackageManager pm = context.getPackageManager();
		        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
		        versionName = pi.versionName;  
		        if (versionName == null || versionName.length() <= 0) {  
		            return "";  
		        }  
		    } catch (Exception e) {
		        JLog.e("VersionInfo", "Exception"+ e);
		    }  
		    return versionName;  
	}
	public static String getVersionCode(Context context){
		 String versionCode = "";
		    try {  
		        // ---get the package info---  
		        PackageManager pm = context.getPackageManager();
		        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
		        versionCode = pi.versionCode +"";  
		        if (versionCode == null || versionCode.length() <= 0) {  
		            return "";  
		        }  
		    } catch (Exception e) {
				JLog.e("VersionInfo", "Exception"+ e);
		    }  
		    return versionCode;  
	}
}
