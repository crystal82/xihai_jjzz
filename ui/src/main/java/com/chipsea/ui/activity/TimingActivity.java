package com.chipsea.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chipsea.code.business.ActivityBusiness;
import com.chipsea.code.listener.TimerListener;
import com.chipsea.code.util.HexStrUtils;
import com.chipsea.code.util.LogUtil;
import com.chipsea.mode.entity.TimerInfo;
import com.chipsea.ui.R;
import com.chipsea.ui.adapter.TimingListAdapter;
import com.chipsea.ui.utils.Constant;
import com.chipsea.ui.utils.Utils;
import com.chipsea.view.VerticalSwipeRefreshLayout;
import com.chipsea.view.dialog.LoadDialog;
import com.chipsea.view.text.CustomTextView;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.larksmart7618.sdk.communication.tools.commen.ToastTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.chipsea.ui.utils.Constant.TIMER_TYPE_ONE_FLAG;
import static com.chipsea.ui.utils.Constant.TIMER_TYPE_FLAG_EVERY_DAY;
import static com.chipsea.ui.utils.Constant.TIMER_TYPE_FLAG_WEEK_DAY;
import static com.chipsea.ui.utils.Constant.TIMER_TYPE_FLAG_WORKING_DAY;

/**
 * 作者：HWQ on 2017/11/10 10:07
 * 描述：
 */

public class TimingActivity extends GosControlModuleBaseActivity implements AdapterView.OnItemClickListener,
        TimerListener, SwipeRefreshLayout.OnRefreshListener {
    String TAG = "TimingActivity";
    private View headView;
    private LinearLayout countDownLayout;
    private CustomTextView countDownTitle;
    private ListView listView;
    private GizWifiDevice mDevice = null;
    private TimingListAdapter adapter;
    private List<TimerInfo> mTimeList = new ArrayList<>();
    private Handler handler = new Handler();
    private boolean isgetdata = false;
    private VerticalSwipeRefreshLayout swipeRefreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentSub(R.layout.activity_timing, R.string.timingSetting);
        setRightText(R.string.timingAdd);
        ActivityBusiness.getInstance().addActivity(this);
        mDevice = (GizWifiDevice) getIntent().getParcelableExtra(ControlSocketActivity.DEVICE);

        headView = LayoutInflater.from(this).inflate(R.layout.timing_list_countdown_layout, null, false);
        countDownTitle = (CustomTextView) headView.findViewById(R.id.countDownTitle);
        countDownLayout = (LinearLayout) headView.findViewById(R.id.countDownLayout);
        countDownLayout.setOnClickListener(this);

        swipeRefreshView = (VerticalSwipeRefreshLayout) findViewById(R.id.srl);
        swipeRefreshView.setOnRefreshListener(this);

        listView = (ListView) findViewById(R.id.listview);
        adapter = new TimingListAdapter(this, mTimeList);
        adapter.setTimerListener(this);
        listView.addHeaderView(headView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        getTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDevice.setListener(gizWifiDeviceListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 获取定时列表
     */
    public void getTimers() {
        isgetdata = false;
        List<String> attrs = new ArrayList<>();
        attrs.add(Constant.DPID);
        mDevice.getDeviceStatus(attrs);
        swipeRefreshView.setRefreshing(true);
        handler.postDelayed(runnable, 8000);//设置超时 8s
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isgetdata) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshView.setRefreshing(false);
                        ToastTools.short_Toast(TimingActivity.this, getString(R.string.timer_error));
                        isgetdata = true;
                    }
                });
            }
        }
    };

    private void sendCommand(String key, Object value) {
        ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<String, Object>();
        hashMap.put(key, value);
        mDevice.write(hashMap, Constant.SEND_SN);
        Log.e(TAG, "下发数据=" + hashMap.toString());

    }

    //binary=[19,64,6, 1, 19,0, 2,       1, 8, 1,127,0,1,67,-77,   71,8, 0, 31,0,1,76,28]
    //data:13 40 06 01 13 00 02       01 08 01 7f 000143b3      47 08 00 1f 00014c1c
    @Override
    public void didReceiveData(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, Object>
            dataMap, int sn) {
        getDataFromReceiveDataMap(dataMap);
        LogUtil.e(TAG, "sn=" + sn + "收到数据：" + dataMap.toString());
        // 透传数据
        if (dataMap.get("binary") != null) {
            byte[] binary = (byte[]) dataMap.get("binary");
            LogUtil.e(Arrays.toString(binary));
            if (binary[0] == Constant.CMD_1 || binary[0] == Constant.CMD_3) {
                onAnalysisData(binary);
            }
        }
    }

    boolean haveDownTime = false;

    public void onAnalysisData(byte[] data) {
        haveDownTime = false;
        byte timer_cmd = data[2];
        LogUtil.e(TAG, "timer_cmd=" + timer_cmd);
        if (timer_cmd == Constant.TIMER_CMD_2) {
            int num = data[6];//定时器个数
            int l = num * 8;//定时器数据长度
            LogUtil.e(TAG, "num" + num + ">>l=" + l);
            if (num < 1) {
                mTimeList.clear();
                updateTimerList(mTimeList);
                return;
            }
            List<TimerInfo> list = new ArrayList<>();
            TimerInfo timer;
            for (int i = 7; i < 7 + l; i += 8) {
                byte[] tm = new byte[8];
                System.arraycopy(data, i, tm, 0, 8);
                LogUtil.e(Arrays.toString(tm));
                timer = new TimerInfo();
                timer.setTimerId(tm[0]);//11
                timer.setTimerZone(tm[1]);//01
                timer.setTimerAction(tm[2]);//01
                timer.setWeekFlag(tm[3]);//00
                byte[] d = new byte[4];
                System.arraycopy(tm, 4, d, 0, 4);//时间差
                timer.setDeltaTime(d);
                long deltaTime = Long.parseLong(HexStrUtils.bytesToHexString(d), 16) * 1000;
                long longTime = System.currentTimeMillis() + deltaTime + 15000;//加15s
                String time = Utils.stampToDate(longTime);
                timer.setTime(time);
                if (timer.getTimerId() == Constant.COUNTDOWN_ID) {
                    Constant.sTimerInfo = timer;
                    haveDownTime = true;
                } else {
                    list.add(timer);
                }
                LogUtil.e(TAG, timer.toString());
            }
            updateTimerList(list);
        }
    }

    //定时器排序
    public void sortTimerInfo(List<TimerInfo> timeLists) {
        ArrayList<TimerInfo> typeOne = new ArrayList<TimerInfo>();
        ArrayList<TimerInfo> typeEvery = new ArrayList<TimerInfo>();
        ArrayList<TimerInfo> typeWorking = new ArrayList<TimerInfo>();
        ArrayList<TimerInfo> typeWeek = new ArrayList<TimerInfo>();
        ArrayList<TimerInfo> typeDefined = new ArrayList<TimerInfo>();
        for (TimerInfo timerInfo : timeLists) {
            switch (timerInfo.getWeekFlag()) {
                case TIMER_TYPE_ONE_FLAG:
                    typeOne.add(timerInfo);
                    break;
                case TIMER_TYPE_FLAG_EVERY_DAY:
                    typeEvery.add(timerInfo);
                    break;
                case TIMER_TYPE_FLAG_WORKING_DAY:
                    typeWorking.add(timerInfo);
                    break;
                case TIMER_TYPE_FLAG_WEEK_DAY:
                    typeWeek.add(timerInfo);
                    break;
                default:
                    typeDefined.add(timerInfo);
            }
        }
        mTimeList.clear();
        Collections.sort(typeOne);
        Collections.sort(typeEvery);
        Collections.sort(typeWorking);
        Collections.sort(typeWeek);
        Collections.sort(typeDefined);
        mTimeList.addAll(typeOne);
        mTimeList.addAll(typeEvery);
        mTimeList.addAll(typeWorking);
        mTimeList.addAll(typeWeek);
        mTimeList.addAll(typeDefined);
        adapter.notifyDataSetChanged();
    }

    public void updateTimerList(List<TimerInfo> timeLists) {
        //isgetdata = true;
        //handler.removeCallbacks(runnable);
        //sortTimerInfo(timeLists);
        isgetdata = true;
        handler.removeCallbacks(runnable);
        mTimeList.clear();
        mTimeList.addAll(timeLists);
        adapter.notifyDataSetChanged();

        //倒计时文本
        if (Constant.sTimerInfo != null && haveDownTime) {
            int d = Integer.parseInt(HexStrUtils.bytesToHexString(Constant.sTimerInfo.getDeltaTime()), 16);
            if (d / 60 == 0) {
                countDownTitle.setText(d == 0 ? getString(R.string.timingAdd) : d % 60 + getString(R.string.timingAfterClose_1));
            } else {
                countDownTitle.setText(Integer.parseInt(HexStrUtils.bytesToHexString(Constant.sTimerInfo.getDeltaTime
                        ()), 16) / 60 + getString(R.string.timingAfterClose));
            }
        } else {
            countDownTitle.setText(getString(R.string.timingAdd));
        }
        Constant.list.clear();
        Constant.list.addAll(mTimeList);
        swipeRefreshView.setRefreshing(false);
    }

    @Override
    protected void onRightTextClick(View v) {
        super.onRightTextClick(v);
        callAddTimerActivity();
    }

    private void callAddTimerActivity() {
        if (mTimeList.size() >= 14) {
            ToastTools.short_Toast(TimingActivity.this, getString(R.string.max_set_time));
            return;
        }
        Intent intent = new Intent(TimingActivity.this, AddTimingActivity.class);
        intent.putExtra("isedit", false);
        intent.putExtra(Constant.DEVICE, mDevice);
        startActivityForResult(intent, 0);
        closeDelItem();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == 0 && resultCode == 11) {//添加定时器后返回
                getTimers();
            }
            if (requestCode == 1 && resultCode == 12) {//添加倒计时后返回
                getTimers();
                String min = data.getStringExtra("minutes");
                if (TextUtils.isEmpty(min) || min.equals("0")) {
                    countDownTitle.setText(getString(R.string.timingAdd));
                } else {
                    countDownTitle.setText(min + getString(R.string.timingAfterClose));
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LogUtil.e("" + position);
        Intent intent = new Intent(TimingActivity.this, AddTimingActivity.class);
        intent.putExtra(Constant.DEVICE, mDevice);
        intent.putExtra("isedit", true);
        intent.putExtra("timer", mTimeList.get(position - 1));
        startActivityForResult(intent, 0);
        closeDelItem();
        LogUtil.e("" + mTimeList.get(position - 1).toString());
    }

    @Override
    public void onRemove(TimerInfo timerInfo) {
        byte[] data = new byte[13];
        data[5] = timerInfo.getTimerId();
        data[6] = timerInfo.getTimerZone();
        data[7] = timerInfo.getTimerAction();
        data[8] = timerInfo.getWeekFlag();
        System.arraycopy(timerInfo.getDeltaTime(), 0, data, 9, 4);
        data[0] = Constant.TIMER_CMD_1;
        data[1] = Constant.VERSION;
        data[2] = Constant.WRITE;
        data[3] = Constant.DEVST;
        data[4] = Constant.ACTION_DEL;
        LogUtil.e(TAG, Arrays.toString(data));
        sendCommand(Constant.DPID, data);
    }

    @Override
    public void onAdd() {
        callAddTimerActivity();
    }

    @Override
    public void switchOnChanged(TimerInfo timerInfo) {

        byte[] data = new byte[13];
        data[5] = timerInfo.getTimerId();
        data[6] = timerInfo.getTimerZone();
        data[7] = timerInfo.getTimerAction();
        data[8] = timerInfo.getWeekFlag();
        System.arraycopy(timerInfo.getDeltaTime(), 0, data, 9, 4);
        data[0] = Constant.TIMER_CMD_1;
        data[1] = Constant.VERSION;
        data[2] = Constant.WRITE;
        data[3] = Constant.DEVST;
        data[4] = Constant.ACTION_ADD_OR_EDIT;
        LogUtil.e(TAG, Arrays.toString(data));
        sendCommand(Constant.DPID, data);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.countDownLayout) {
            Intent intent = new Intent(TimingActivity.this, CountDownActivity.class);
            intent.putExtra(Constant.DEVICE, mDevice);
            intent.putExtra("timer", Constant.sTimerInfo);
            startActivityForResult(intent, 1);
            closeDelItem();
        }
    }

    @Override
    public void onRefresh() {
        if (!swipeRefreshView.isRefreshing())
            swipeRefreshView.setRefreshing(true);
        getTimers();
    }


    private void closeDelItem() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.closeOpenedSwipeItemLayoutWithAnim();
            }
        },500);
    }
}
