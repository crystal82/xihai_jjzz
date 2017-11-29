package com.chipsea.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chipsea.code.business.ActivityBusiness;
import com.chipsea.code.listener.WIFICallback;
import com.chipsea.code.util.Constant;
import com.chipsea.code.util.LogUtil;
import com.chipsea.code.util.StandardUtil;
import com.chipsea.code.util.TimeUtil;
import com.chipsea.code.util.TrendUtils;
import com.chipsea.mode.entity.DeviceInfo;
import com.chipsea.mode.entity.StatDetailEntity;
import com.chipsea.mode.entity.StatEntity;
import com.chipsea.mode.entity.XHelpEntity;
import com.chipsea.ui.R;
import com.chipsea.ui.dialog.ShareDialog;
import com.chipsea.ui.utils.ShareItemClickListenerImp;
import com.chipsea.view.StatTrendView;
import com.chipsea.view.text.CustomTextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import chipsea.wifiplug.lib.model.RunningStatus;


public class StatActivity extends CommonActivity implements RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "StatActivity";
    public static final int DAY_SECTION = 24 ;
    public static final int WEEK_SECTION = 7 ;
    public static final int MONTH_SECTION = 28 ;
    public int currSection = DAY_SECTION ;
    private CustomTextView timeText ;
    private LinearLayout consumptionLayout ;
    private TextView userElecSum ;
    private TextView sectionTag ;
    private TextView powerConsumption ;
    private ImageView upTime,nextTime;
    private RadioGroup radioRg ;
    private StatTrendView trendView ;
    private ArrayList<StatEntity> statEntities ;
    private List<XHelpEntity> xHelpEntities ;
    private long startTs;
    private int curDayIndex;
    private Handler handler = new Handler();
    public static int samplingInterval=5000;
    private StandardUtil mStandardUtil;
    private DeviceInfo mDeviceInfo;
    private ShareItemClickListenerImp shareItemClickListenerImp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentSub(R.layout.activity_new_stat,0,R.string.mainStatistics,R.mipmap.white_share_icon);
        ActivityBusiness.getInstance().addActivity(this);
        mDeviceInfo = getIntent().getParcelableExtra("currDeviceInfo") ;
        mStandardUtil=StandardUtil.getInstance(this);
        statEntities = new ArrayList<>() ;
        xHelpEntities = new ArrayList<>() ;
        this.initView();
        refreshData();
    }

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

    private void updateSamplingUI(final StatDetailEntity detail){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                powerConsumption.setText("" + detail.instantaneousPower + "W");
            }
        });
    }

    @Override
    protected void onRightClick() {
        super.onRightClick();
        if(shareItemClickListenerImp == null){
            shareItemClickListenerImp = new ShareItemClickListenerImp(this) ;
        }
        ShareDialog.show(this).addItemOnclickListner(shareItemClickListenerImp);
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

    private void initView() {
        currSection = DAY_SECTION ;
        curDayIndex=0;
        upTime=(ImageView)findViewById(R.id.upTimeBto);
        nextTime=(ImageView)findViewById(R.id.nextTimeBto);
        timeText = (CustomTextView) findViewById(R.id.timeText);
        radioRg = (RadioGroup) findViewById(R.id.radioRg);
        trendView = (StatTrendView) findViewById(R.id.statTrendView);
        consumptionLayout = (LinearLayout) findViewById(R.id.consumptionLayout);
        userElecSum = (TextView) findViewById(R.id.userElecSum);
        sectionTag = (TextView) findViewById(R.id.sectionTag);
        powerConsumption=(TextView)findViewById(R.id.statTrendCurPwConsumption);
        radioRg.setOnCheckedChangeListener(this);
        consumptionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatActivity.this,StatDetalisActivity.class) ;
                intent.putExtra("currDeviceInfo" ,mDeviceInfo) ;
                startActivity(intent);
            }
        });


        upTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDate(true);
                refreshData();
            }
        });

        nextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDate(false);
                refreshData();
            }
        });



    }

    private void changeDate(boolean isUp){
        if(isUp){
            if(currSection==DAY_SECTION){
                curDayIndex+=1;
            }else{
                curDayIndex=curDayIndex+currSection;
            }
        }else{
            if(curDayIndex==0) return;

            if(currSection==DAY_SECTION){
                curDayIndex-=1;
            }else{
                curDayIndex=curDayIndex-currSection;
            }
        }
    }

    private void refreshData() {
        List<Long> lstTs;
        if(currSection==DAY_SECTION){
            lstTs=TrendUtils.getStartEndTimesForDays(curDayIndex,1);
        }else{
            lstTs=TrendUtils.getStartEndTimesForDays(curDayIndex,currSection);
        }
        startTs=lstTs.get(0);

        TrendUtils.getTrendStatEntitysForXSetion(mDeviceInfo.physicalDeviceId, mDeviceInfo.subDominId,lstTs.get(0), lstTs.get(1), currSection, new WIFICallback<List<StatEntity>>() {
            @Override
            public void onSuccess(List<StatEntity> data) {
                showData(data);
            }

            @Override
            public void onFailure(String msg, int code) {
                mStandardUtil.showToast(mStandardUtil.getMessage(code,msg));
            }
        });

    }

    public long getTimestamp(long startTs, int day){
        return startTs + day * Constant.ONE_DAY_MS ;
    }

    private float[] getYAxisText(List<StatEntity> data){
        float yMax=0;
        float[] fRet=new float[]{0x0};
        if(data.size()>0) {
            for (StatEntity entity : data) {
                if (entity.getValue() > yMax) {
                    yMax = entity.getValue();
                }
            }
            if (yMax > 0) {
                if(currSection == DAY_SECTION){
                    yMax=yMax+0.2f;
                }else{
                    yMax=yMax+1;
                }
                float fAvg=0;
                float iCount=5.0f;
                if(data.size()<6){
                    iCount=data.size();
                }
                fAvg=yMax / iCount;
                fRet=new float[data.size()+1];
                fRet[0]=0;
                for(int i=1;i<fRet.length;i++){
                    float f1=(i * fAvg);
                    BigDecimal format=new BigDecimal(f1);
                    fRet[i]=format.setScale(3,BigDecimal.ROUND_HALF_UP).floatValue();
                }
            }
        }

        return fRet;
    }


    private void showData(List<StatEntity> data){
        if(currSection == DAY_SECTION){
            timeText.setText(TimeUtil.parseTimes(startTs,TimeUtil.TIME_FORMAT3));
            sectionTag.setText(getString(R.string.statDayUserElec));
        }else if(currSection == WEEK_SECTION){
            timeText.setText(TimeUtil.parseTimes(startTs,TimeUtil.TIME_FORMAT3) + "~" + TimeUtil.parseTimes(getTimestamp(startTs,currSection -1),TimeUtil.TIME_FORMAT3));
            sectionTag.setText(getString(R.string.statWeekUserElec));
        }else if(currSection == MONTH_SECTION){
            timeText.setText(TimeUtil.parseTimes(startTs,TimeUtil.TIME_FORMAT3) + "~" + TimeUtil.parseTimes(getTimestamp(startTs,currSection -1),TimeUtil.TIME_FORMAT3));
            sectionTag.setText(getString(R.string.statMonthUserElec));
        }
        userElecSum.setText(getUserElecNumber(data) + "度");
        xHelpEntities.clear();
        statEntities.clear();
        xHelpEntities.addAll(TrendUtils.getTrendXhelpEntityForXSetion(startTs,currSection)) ;
        statEntities.addAll(data) ;
        trendView.setxSesion(currSection);
        trendView.setValueYAxis(getYAxisText(data));
        trendView.setxHelpEntities(xHelpEntities);
        trendView.setStatEntities(statEntities);
        trendView.invalidate();
    }

    private float getUserElecNumber(List<StatEntity> data) {
        float result = 0 ;
        if(data != null && data.size() != 0){
            for (StatEntity e:data) {
                result += e.getValue();
            }
        }
        BigDecimal format=new BigDecimal(result);
        result = format.setScale(3,BigDecimal.ROUND_HALF_UP).floatValue() ;
        return result;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId == R.id.dayBto){
            currSection = DAY_SECTION ;
        }else if(checkedId == R.id.weekBto){
            currSection = WEEK_SECTION ;
        }else if(checkedId == R.id.monthBto){
            currSection = MONTH_SECTION ;
        }
        curDayIndex=0;
        refreshData();
    }
}
