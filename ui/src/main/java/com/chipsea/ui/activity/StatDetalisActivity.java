package com.chipsea.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import com.chipsea.code.business.ActivityBusiness;
import com.chipsea.code.listener.WIFICallback;
import com.chipsea.code.util.LogUtil;
import com.chipsea.code.util.TrendUtils;
import com.chipsea.mode.entity.DeviceInfo;
import com.chipsea.mode.entity.StatDetailEntity;
import com.chipsea.ui.R;
import com.chipsea.ui.adapter.StatDetalisListAdapter;

import java.util.ArrayList;
import java.util.List;

import chipsea.wifiplug.lib.model.RunningStatus;


public class StatDetalisActivity extends CommonActivity{
    private static final String TAG = "NewMainActivity";
    private ArrayList<StatDetailEntity> statEntities ;
    private ListView listView ;
    private StatDetalisListAdapter adapter;
    private DeviceInfo mDeviceInfo;
    private Handler handler = new Handler();


    @Override
    protected void onResume() {
        super.onResume();
        handler.post(intevalQueryDevice);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(intevalQueryDevice);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentSub(R.layout.activity_stat_detalis,R.string.statisticsPowerDetails);
        ActivityBusiness.getInstance().addActivity(this);
        mDeviceInfo = getIntent().getParcelableExtra("currDeviceInfo") ;
        adapter = new StatDetalisListAdapter(this) ;
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
    }

    private void updateSamplingUI(final StatDetailEntity detail){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.add(detail);
            }
        });
    }


    private Runnable intevalQueryDevice=new Runnable() {
        @Override
        public void run() {
            TrendUtils.queryRunningState(mDeviceInfo.physicalDeviceId,mDeviceInfo.subDominId, new WIFICallback<List<RunningStatus>>() {
                @Override
                public void onSuccess(List<RunningStatus> data) {
                    if(data!=null){
                        if(data.size()>0){
                            RunningStatus status=data.get(0);
                            StatDetailEntity detail=TrendUtils.ConvertRuningStatu2StateDetail(status);
                            updateSamplingUI(detail);
                        }
                    }
                    handler.postDelayed(intevalQueryDevice,StatActivity.samplingInterval);
                }

                @Override
                public void onFailure(String msg, int code) {
                    LogUtil.e(TAG,"Err[" + code + "]:" + msg);
                    handler.postDelayed(intevalQueryDevice,StatActivity.samplingInterval);
                }
            });
        }
    };

}
