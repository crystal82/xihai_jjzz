package com.chipsea.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chipsea.code.business.ActivityBusiness;
import com.chipsea.code.listener.SetTimingCallback;
import com.chipsea.code.listener.WeekSelectChangeListener;
import com.chipsea.code.util.HexStrUtils;
import com.chipsea.code.util.LogUtil;
import com.chipsea.mode.entity.TimerInfo;
import com.chipsea.ui.R;
import com.chipsea.ui.adapter.AddTimingAdapter;
import com.chipsea.ui.dialog.SelectWeekDialog;
import com.chipsea.ui.dialog.SetTimingDialog;
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
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import chipsea.wifiplug.lib.GosDeploy;

/**
 * 作者：HWQ on 2017/11/10 11:07
 * 描述：
 */

public class AddTimingActivity extends GosControlModuleBaseActivity implements View.OnClickListener,
        SetTimingCallback, AdapterView.OnItemClickListener, WeekSelectChangeListener {


    private byte    REPEAT = Constant.TIMER_TYPE_FLAG_EVERY_DAY;
    private boolean ISEDIT = false;

    private View           headView;
    private LinearLayout   lightLl;
    private CustomTextView openBto, closeBto, lightOpenBtn, lightCloseBtn;
    private LinearLayout     timingLayout;
    private CustomTextView   timeText;
    private AddTimingAdapter adapter;
    private ListView         listView;
    private SetTimingDialog  timingDialog;
    private int checkIndex = 1;

    private int mCurHour, mCurMinute;
    private String hourStr, minStr;
    private GizWifiDevice mDevice = null;
    private SelectWeekDialog customDialog;

    public TimerInfo mTimerInfo;
    public String[]     week      = {"0", "0", "0", "0", "0", "0", "0"};
    public List<String> week_list = new ArrayList<>(7);
    List<String> list = new ArrayList<>();
    private LoadDialog mLoadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRightText(R.string.timingSave);
        setContentSub(R.layout.activity_set_timing, R.string.settingAddDevice);
        ActivityBusiness.getInstance().addActivity(this);
        mDevice = (GizWifiDevice) getIntent().getParcelableExtra(ControlSocketActivity.DEVICE);
        initView();
        initData();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mDevice.setListener(null);
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listview);
        headView = LayoutInflater.from(this).inflate(R.layout.add_timing_top_layout, null);
        openBto = (CustomTextView) headView.findViewById(R.id.openBto);
        closeBto = (CustomTextView) headView.findViewById(R.id.closeBto);
        lightLl = (LinearLayout) headView.findViewById(R.id.light_ll);
        lightOpenBtn = (CustomTextView) headView.findViewById(R.id.lightOpenBtn);
        lightCloseBtn = (CustomTextView) headView.findViewById(R.id.lightCloseBtn);
        timingLayout = (LinearLayout) headView.findViewById(R.id.timingLayout);
        timeText = (CustomTextView) headView.findViewById(R.id.timeText);
        if (mDevice.getProductKey().equals(GosDeploy.setProductKeyList().get(1))) {
            lightLl.setVisibility(View.VISIBLE);
        }
        adapter = new AddTimingAdapter(this, Constant.getAddTimingType(this));
        adapter.setSelectIndex(checkIndex);
        listView.addHeaderView(headView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        timingLayout.setOnClickListener(this);
        openBto.setOnClickListener(this);
        closeBto.setOnClickListener(this);
        lightOpenBtn.setOnClickListener(this);
        lightCloseBtn.setOnClickListener(this);
    }

    private void initData() {
        mDevice.setListener(gizWifiDeviceListener);
        if (getIntent().getBooleanExtra("isedit", false)) {
            ISEDIT = true;
            mTimerInfo = (TimerInfo) getIntent().getSerializableExtra("timer");
            String[] ttt = mTimerInfo.getTime().substring(11, 16).split(":");
            timeResult(Integer.parseInt(ttt[0]), Integer.parseInt(ttt[1]));
            setOpenCloseStyle(!mTimerInfo.isOpenTime());
            setLightOpenCloseStyle(!mTimerInfo.isWakeUpTime());

            if (mTimerInfo.getWeekFlag() == Constant.TIMER_TYPE_ONE_FLAG) {
                adapter.setSelectIndex(0);
                REPEAT = Constant.TIMER_TYPE_ONE_FLAG;
                initWeeks();
            } else if (mTimerInfo.getWeekFlag() == Constant.TIMER_TYPE_FLAG_EVERY_DAY) {
                adapter.setSelectIndex(1);
                REPEAT = Constant.TIMER_TYPE_FLAG_EVERY_DAY;
                initWeeks();
            } else if (mTimerInfo.getWeekFlag() == Constant.TIMER_TYPE_FLAG_WORKING_DAY) {
                adapter.setSelectIndex(2);
                REPEAT = Constant.TIMER_TYPE_FLAG_WORKING_DAY;
                initWeeks();
            } else if (mTimerInfo.getWeekFlag() == Constant.TIMER_TYPE_FLAG_WEEK_DAY) {
                adapter.setSelectIndex(3);
                REPEAT = Constant.TIMER_TYPE_FLAG_WEEK_DAY;
                initWeeks();
            } else {
                adapter.setSelectIndex(4);
                REPEAT = mTimerInfo.getWeekFlag();
                initWeeks();
            }
        } else {
            Calendar calendar = Calendar.getInstance();
            mCurHour = calendar.get(Calendar.HOUR_OF_DAY);
            mCurMinute = (calendar.get(Calendar.MINUTE));
            timeResult(mCurHour, mCurMinute);
            mTimerInfo = new TimerInfo();
            mTimerInfo.setTimerId(Utils.getID());
            mTimerInfo.setTimerZone(Utils.getCurrentTimeZone());
            mTimerInfo.setAction(Constant.ACTION_ADD_OR_EDIT);
            if (mDevice.getProductKey().equals(GosDeploy.setProductKeyList().get(1))) {
                mTimerInfo.setTimerAction(Constant.TIMER_ANDROID_LIGHT);
            }
            mTimerInfo.setWeekFlag(REPEAT);
            initWeeks();
        }
    }

    private void initWeeks() {
        String weeks = Integer.toBinaryString(REPEAT + 256);
        LogUtil.e("<<<<=" + weeks);
        weeks = new StringBuilder(weeks).reverse().toString();
        LogUtil.e("<<<<=" + weeks);
        week_list.clear();
        for (int i = 0; i < 7; i++) {
            week[i] = weeks.substring(i, i + 1);
            if (week[i].equals("1")) {
                week_list.add("1");
            } else {
                week_list.add("0");
            }
        }
    }

    private void setOpenCloseStyle(boolean isClose) {
        if (isClose) {
            openBto.setBackgroundResource(R.color.transparent);
            openBto.setTextColor(getResources().getColor(R.color.main_color));
            closeBto.setBackgroundResource(R.drawable.checkcolor_right_corners_50);
            closeBto.setTextColor(getResources().getColor(R.color.white));
        } else {
            openBto.setBackgroundResource(R.drawable.checkcolor_left_corners_50);
            openBto.setTextColor(getResources().getColor(R.color.white));
            closeBto.setBackgroundResource(R.color.transparent);
            closeBto.setTextColor(getResources().getColor(R.color.main_color));
        }
    }

    private void setLightOpenCloseStyle(boolean isClose) {
        if (isClose) {
            lightOpenBtn.setBackgroundResource(R.color.transparent);
            lightOpenBtn.setTextColor(getResources().getColor(R.color.main_color));
            lightCloseBtn.setBackgroundResource(R.drawable.checkcolor_right_corners_50);
            lightCloseBtn.setTextColor(getResources().getColor(R.color.white));
        } else {
            lightOpenBtn.setBackgroundResource(R.drawable.checkcolor_left_corners_50);
            lightOpenBtn.setTextColor(getResources().getColor(R.color.white));
            lightCloseBtn.setBackgroundResource(R.color.transparent);
            lightCloseBtn.setTextColor(getResources().getColor(R.color.main_color));
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LogUtil.e("position=" + position);
        if (position > 0) {
            checkIndex = position - 1;
            adapter.notifyDataSetChanged();
            switch (checkIndex) {
                case 0: //执行一次
                    REPEAT = Constant.TIMER_TYPE_ONE_FLAG;
                    mTimerInfo.setWeekFlag(Constant.TIMER_TYPE_ONE_FLAG);//每天 11111110
                    adapter.setSelectIndex(checkIndex);
                    initWeeks();
                    break;
                case 1: //每天
                    REPEAT = Constant.TIMER_TYPE_FLAG_EVERY_DAY;
                    mTimerInfo.setWeekFlag(Constant.TIMER_TYPE_FLAG_EVERY_DAY);
                    adapter.setSelectIndex(checkIndex);
                    initWeeks();
                    break;
                case 2: //工作日
                    REPEAT = Constant.TIMER_TYPE_FLAG_WORKING_DAY;
                    mTimerInfo.setWeekFlag(Constant.TIMER_TYPE_FLAG_WORKING_DAY);
                    adapter.setSelectIndex(checkIndex);
                    initWeeks();
                    break;
                case 3: //周未
                    REPEAT = Constant.TIMER_TYPE_FLAG_WEEK_DAY;
                    mTimerInfo.setWeekFlag(Constant.TIMER_TYPE_FLAG_WEEK_DAY);
                    adapter.setSelectIndex(checkIndex);
                    initWeeks();
                    break;
                case 4: //自定义
                    showCustomDialog();
                    break;
            }
        }
    }

    private void showCustomDialog() {
        list.clear();
        list.addAll(week_list);
        if (customDialog == null) {
            customDialog = new SelectWeekDialog(this, list);
        }
        customDialog.adapterNotifyDataSetChanged();
        customDialog.setWeekChangeListener(this);
        customDialog.showAtLocation(timingLayout, Gravity.TOP, 0, 0);
    }

    @Override
    public void timeResult(int hourValue, int minValue) {
        mCurHour = hourValue;
        mCurMinute = minValue;
        hourStr = mCurHour < 10 ? "0" + mCurHour : mCurHour + "";
        minStr = mCurMinute < 10 ? "0" + mCurMinute : mCurMinute + "";
        timeText.setText(hourStr + ":" + minStr);
    }

    @Override
    public void onChanged(List<String> week) {
        LogUtil.e("TAG", "size=" + week.size());
        week_list.clear();
        week_list.addAll(week);
        LogUtil.e("TAG", "size=" + week_list.size());
        String str = "";
        for (String w : week) {
            str = str + w;
        }
        str = new StringBuilder(str).reverse().toString();
        LogUtil.e("TAG", "str=" + str);
        REPEAT = (byte) Integer.parseInt(str, 2);
        LogUtil.e("TAG", "REPEAT=" + REPEAT);
        if (REPEAT == Constant.TIMER_TYPE_ONE_FLAG) {
            mTimerInfo.setWeekFlag(Constant.TIMER_TYPE_ONE_FLAG);
            adapter.setSelectIndex(0);
        } else if (REPEAT == Constant.TIMER_TYPE_FLAG_EVERY_DAY) {
            mTimerInfo.setWeekFlag(Constant.TIMER_TYPE_FLAG_EVERY_DAY);
            adapter.setSelectIndex(1);
        } else if (REPEAT == Constant.TIMER_TYPE_FLAG_WORKING_DAY) {
            mTimerInfo.setWeekFlag(Constant.TIMER_TYPE_FLAG_WORKING_DAY);//工作日
            adapter.setSelectIndex(2);
        } else if (REPEAT == Constant.TIMER_TYPE_FLAG_WEEK_DAY) {
            mTimerInfo.setWeekFlag(Constant.TIMER_TYPE_FLAG_WEEK_DAY);//周末
            adapter.setSelectIndex(3);
        } else {
            mTimerInfo.setWeekFlag(REPEAT);
            adapter.setSelectIndex(4);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        LogUtil.e("TAG", "T=" + mTimerInfo.getTimerAction());
        if (v == timingLayout) {
            if (timingDialog == null) {
                timingDialog = new SetTimingDialog(this, mCurHour, mCurMinute, getResources().getString(R.string.timingEditTime), this, true);
            }
            timingDialog.showAtLocation(timingLayout, Gravity.TOP, 0, 0);
        } else if (v == openBto) {
            setOpenCloseStyle(false);
            mTimerInfo.setTimerAction((byte) (mTimerInfo.getTimerAction() | Constant.TIMER_ACTION_OPEN)); //开启
        } else if (v == closeBto) {
            setOpenCloseStyle(true);
            mTimerInfo.setTimerAction((byte) (mTimerInfo.getTimerAction() & Constant.TIMER_ACTION_CLOSE)); //关闭
        } else if (v == lightOpenBtn) {
            setLightOpenCloseStyle(false);
            mTimerInfo.setTimerAction((byte) (mTimerInfo.getTimerAction() | Constant.TIMER_LIGHT_OPEN)); //开启
        } else if (v == lightCloseBtn) {
            setLightOpenCloseStyle(true);
            mTimerInfo.setTimerAction((byte) (mTimerInfo.getTimerAction() & Constant.TIMER_LIGHT_CLOSE)); //关闭
        }
        LogUtil.e("TAG", "TimerAction=" + mTimerInfo.getTimerAction());
    }

    @Override
    protected void onRightTextClick(View v) {
        super.onRightTextClick(v);
        if (mLoadDialog == null) {
            mLoadDialog = LoadDialog.getShowDialog(AddTimingActivity.this);
        }
        mLoadDialog.show();
        addOrEditTimer();
    }

    //
    public void addOrEditTimer() {
        if (REPEAT == Constant.TIMER_TYPE_ONE_FLAG || REPEAT == Constant.TIMER_TYPE_FLAG_EVERY_DAY) {
            String tim = Utils.convert10To16((int) Utils.getRelativeTime(hourStr + ":" + minStr));
            tim = Utils.getString(tim);
            Log.e("TAG", "tim=" + tim);
            mTimerInfo.setDeltaTime(HexStrUtils.hexStringToBytes(tim));//差值
        } else {
            //周重复计算时间差
            getWeekTimeDistance();
        }
        String time = Utils.stampToDate(1000 * 10 + System.currentTimeMillis()
                                                + Long.parseLong(HexStrUtils.bytesToHexString
                (mTimerInfo.getDeltaTime()), 16) * 1000);
        mTimerInfo.setTime(time);

        LogUtil.e("添加定时器:" + mTimerInfo.toString());
        if (isTimerRepeat(mTimerInfo)) {
            ToastTools.short_Toast(this, getString(R.string.timer_exist));
            mLoadDialog.dismiss();
            return;
        }
        mTimerInfo.setUseTime(true);
        LogUtil.e(mTimerInfo.toString());
        addTimer(mTimerInfo);

    }

    /**
     * 计算周重复 差值
     */
    private void getWeekTimeDistance() {
        String   tim = "";
        Calendar cal = Calendar.getInstance();
        int      wk  = cal.get(Calendar.DAY_OF_WEEK);//当前星期几
        if (wk == 1) {
            wk = 7;
        } else {
            wk = wk - 1;
        }
        LogUtil.e("TAG", "wk=" + wk);
        LogUtil.e(" week_list.size=" + week_list.size());
        boolean isTim = false;
        for (int i = wk - 1; i < 7; i++) {
            if ("1".equals(week_list.get(i))) {
                if (Utils.getRelative(hourStr + ":" + minStr) > 0) {
                    // tim = Utils.convert10To16((int) Utils.getRelativeTime2(hourStr + ":" + minStr,i-wk+1));
                    tim = Utils.convert10To16(Utils.getR(mCurHour, mCurMinute, (i + 1) - wk));
                    isTim = true;
                    break;
                } else if (i != wk - 1) {
                    //tim = Utils.convert10To16((int) Utils.getRelativeTime2(hourStr + ":" + minStr, i - wk + 1));
                    tim = Utils.convert10To16(Utils.getR(mCurHour, mCurMinute, (i + 1) - wk));
                    isTim = true;
                    break;
                }
            }
        }
        if (!isTim) {
            for (int i = 0; i <= (wk - 1); i++) {
                if ("1".equals(week_list.get(i))) {
                    // tim = Utils.convert10To16((int) Utils.getRelativeTime2(hourStr + ":" + minStr, 7 - wk + i + 1));
                    tim = Utils.convert10To16(Utils.getR(mCurHour, mCurMinute, 7 - wk + (i + 1)));
                }
            }
        }
        tim = Utils.getString(tim);
        Log.e("TAG", "tim=" + tim);
        mTimerInfo.setDeltaTime(HexStrUtils.hexStringToBytes(tim));//差值
    }

    /**
     * 判断定时器是否重复
     *
     * @param timer
     * @return
     */
    private boolean isTimerRepeat(TimerInfo timer) {

        for (TimerInfo timer1 : Constant.list) {
            if (timer1.getWeekFlag() == timer.getWeekFlag()) {
                if (timer1.getTime().substring(0, 16).equals(timer.getTime().substring(0, 16))) {
                    LogUtil.e(timer1.getTime().substring(0, 16) + "///" + timer.getTime().substring(0, 16));
                    if (timer1.getTimerId() == timer.getTimerId()) {
                        return false;
                    }
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 添加定时器
     *
     * @param timerInfo
     */
    public void addTimer(TimerInfo timerInfo) {
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
        LogUtil.e(Arrays.toString(data));
        sendCommand(Constant.DPID, data);
    }


    private void sendCommand(String key, Object value) {
        ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<String, Object>();
        hashMap.put(key, value);
        mDevice.write(hashMap, 10101);
        Log.e("TAG", "下发数据=" + hashMap.toString());
        handler.postDelayed(runnable, 8000);

    }

    @Override
    public void didReceiveData(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, Object>
            dataMap, int sn) {
        Log.e("TAG", result + "----sn=" + sn + "收到数据：" + dataMap.toString());
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
                ToastTools.short_Toast(AddTimingActivity.this, getString(R.string.add_time_success));
                Intent intent = new Intent();
                intent.putExtra("add", true);
                setResult(11, intent);
                ActivityBusiness.getInstance().finishActivity(AddTimingActivity.this);
            }
            if (msg.what == 0x02) {
                mLoadDialog.dismiss();
                ToastTools.short_Toast(AddTimingActivity.this, getString(R.string.add_time_error));
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
