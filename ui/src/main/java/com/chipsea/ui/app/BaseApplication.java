package com.chipsea.ui.app;

import android.app.Activity;
import android.app.Application;

import com.chipsea.code.util.AirUtlis;

import java.util.ArrayList;
import java.util.List;



public class BaseApplication extends Application {
    private static final String TAG = "BaseApplication";
	private List<Activity> activityList;


	public List<Activity> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<Activity> activityList) {
		this.activityList = activityList;
	}


	@Override
    public void onCreate() {    	     
		super.onCreate();
		//csACUtil.init(this);
		activityList = new ArrayList<Activity>();
		AirUtlis.writeDb(getApplicationContext());
	}
}
