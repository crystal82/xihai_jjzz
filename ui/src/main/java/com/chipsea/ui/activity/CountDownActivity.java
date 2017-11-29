package com.chipsea.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chipsea.code.business.ActivityBusiness;
import com.chipsea.code.listener.SetTimingCallback;
import com.chipsea.code.util.Constant;
import com.chipsea.code.util.HexStrUtils;
import com.chipsea.code.util.LogUtil;
import com.chipsea.mode.entity.TimerInfo;
import com.chipsea.ui.R;
import com.chipsea.ui.adapter.AddTimingAdapter;
import com.chipsea.ui.dialog.SetTimingDialog;
import com.chipsea.ui.utils.Utils;
import com.chipsea.view.ShowPercentView;
import com.chipsea.view.dialog.LoadDialog;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.larksmart7618.sdk.communication.tools.commen.ToastTools;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;


public class CountDownActivity extends GosControlModuleBaseActivity implements AdapterView.OnItemClickListener,
        SetTimingCallback {
    private static final String TAG = "CountDownActivity";
    private View             headView;
    private ShowPercentView  myShowPercentView;
    private TimerInfo        mTimerInfo;
    private AddTimingAdapter adapter;
    private ListView         listView;
    private SetTimingDialog  timingDialog;
    private int mCurSelectMinutes = 0;

    GizWifiDevice mDevice;
    private LoadDialog mLoadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentSub(R.layout.activity_set_timing, R.string.timingCountDonw);
        setRightText(R.string.timingSave);
        ActivityBusiness.getInstance().addActivity(this);

        listView = (ListView) findViewById(R.id.listview);
        headView = LayoutInflater.from(this).inflate(R.layout.count_down_top_layout, null);
        myShowPercentView = (ShowPercentView) headView.findViewById(R.id.myShowPercentView);
        adapter = new AddTimingAdapter(this, Constant.getCountDownData(this));
        listView.addHeaderView(headView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        mDevice = (GizWifiDevice) getIntent().getParcelableExtra(ControlSocketActivity.DEVICE);
        mTimerInfo = (TimerInfo) getIntent().getSerializableExtra("timer");
        mDevice.setListener(gizWifiDeviceListener);
        if (mTimerInfo == null) {
            myShowPercentView.setMin(0);
            mCurSelectMinutes = 0;
            timeClick(5, false);
            mTimerInfo = new TimerInfo();
            mTimerInfo.setTimerId(com.chipsea.ui.utils.Constant.COUNTDOWN_ID);
            mTimerInfo.setTimerZone(Utils.getCurrentTimeZone());
            mTimerInfo.setAction(com.chipsea.ui.utils.Constant.ACTION_ADD_OR_EDIT);
            mTimerInfo.setTimerAction((byte) (mTimerInfo.getTimerAction() & com.chipsea.ui.utils.Constant.TIMER_ACTION_CLOSE));
            mTimerInfo.setWeekFlag(com.chipsea.ui.utils.Constant.TIMER_TYPE_ONE_FLAG);
        } else {
            myShowPercentView.setMin(Integer.parseInt(HexStrUtils.bytesToHexString(mTimerInfo.getDeltaTime()), 16) /
                                             60);
            mCurSelectMinutes = Integer.parseInt(HexStrUtils.bytesToHexString(mTimerInfo.getDeltaTime()), 16) / 60;
            timeClick(5, false);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mDevice.setListener(null);
    }

    private void timeClick(int position, boolean isPop) {
        if (position > 0) {
            switch (position) {
                case 1:
                    mCurSelectMinutes = 0;
                    myShowPercentView.setMin(0);
                    break;
                case 2:
                    mCurSelectMinutes = 30;
                    myShowPercentView.setMin(30);
                    break;
                case 3:
                    mCurSelectMinutes = 45;
                    myShowPercentView.setMin(45);
                    break;
                case 4:
                    mCurSelectMinutes = 60;
                    myShowPercentView.setMin(60);
                    break;
            }
            int index = position - 1;
            adapter.setSelectIndex(index);
            adapter.notifyDataSetChanged();
        }
        if (position == adapter.getCount() && isPop) {
            showCustomDialog();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        timeClick(position, true);
    }

    private void showCustomDialog() {
        if (timingDialog == null) {
            timingDialog = new SetTimingDialog(this, mCurSelectMinutes / 60, mCurSelectMinutes % 60, getString(R.string.timingEditTime), this,false);
        }
        timeResult(mCurSelectMinutes / 60, mCurSelectMinutes % 60);
        timingDialog.showAtLocation(headView, Gravity.TOP, 0, 0);
    }

    @Override
    public void timeResult(int hourValue, int minValue) {
        mCurSelectMinutes = hourValue * 60 + minValue;
        myShowPercentView.setMin(mCurSelectMinutes);
    }

    @Override
    protected void onRightTextClick(View v) {
        super.onRightTextClick(v);
        if (mLoadDialog == null) {
            mLoadDialog = LoadDialog.getShowDialog(CountDownActivity.this);
        }
        mLoadDialog.show();
        String tim = Integer.toHexString(mCurSelectMinutes * 60);
        int    l   = tim.length();
        for (int i = 0; i < (8 - l); i++) {
            tim = "0" + tim;
        }
        mTimerInfo.setDeltaTime(HexStrUtils.hexStringToBytes(tim));
        if (mCurSelectMinutes == 0) {
            saveTimer(mTimerInfo, true);
        } else saveTimer(mTimerInfo, false);


    }

    public void saveTimer(TimerInfo timerInfo, boolean isDel) {
        byte[] data = new byte[13];
        data[5] = timerInfo.getTimerId();
        data[6] = timerInfo.getTimerZone();
        data[7] = timerInfo.getTimerAction();
        data[8] = timerInfo.getWeekFlag();
        System.arraycopy(timerInfo.getDeltaTime(), 0, data, 9, 4);
        data[0] = com.chipsea.ui.utils.Constant.TIMER_CMD_1;
        data[1] = com.chipsea.ui.utils.Constant.VERSION;
        data[2] = com.chipsea.ui.utils.Constant.WRITE;
        data[3] = com.chipsea.ui.utils.Constant.DEVST;
        if (isDel) {
            data[4] = com.chipsea.ui.utils.Constant.ACTION_DEL;
            com.chipsea.ui.utils.Constant.sTimerInfo = null;
        } else {
            data[4] = com.chipsea.ui.utils.Constant.ACTION_ADD_OR_EDIT;
        }
        LogUtil.e(TAG, Arrays.toString(data));
        sendCommand(com.chipsea.ui.utils.Constant.DPID, data);
    }



    private void sendCommand(String key, Object value) {
        ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<String, Object>();
        hashMap.put(key, value);
        mDevice.write(hashMap, 10101);
        Log.e("TAG=", "下发数据=" + hashMap.toString());
        handler.postDelayed(runnable, 8000);
    }


    @Override
    protected void didReceiveData(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, Object>
            dataMap, int sn) {
        super.didReceiveData(result, device, dataMap, sn);
        if (sn == 10101) {
            handler.removeCallbacks(runnable);
            handler.sendEmptyMessage(0x01);
        }
    }


    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x01) {
                mLoadDialog.dismiss();
                ToastTools.short_Toast(CountDownActivity.this, getString(R.string.add_time_success));
                Intent intent = new Intent();
                intent.putExtra("minutes", "" + mCurSelectMinutes);
                setResult(12, intent);
                ActivityBusiness.getInstance().finishActivity(CountDownActivity.this);
            }
            if (msg.what == 0x02) {
                mLoadDialog.dismiss();
                ToastTools.short_Toast(CountDownActivity.this, getString(R.string.add_time_error));
            }
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.sendEmptyMessage(0x02);
        }
    };
}
