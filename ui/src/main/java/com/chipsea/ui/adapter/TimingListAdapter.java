package com.chipsea.ui.adapter;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.chipsea.code.listener.TimerListener;
import com.chipsea.code.util.HexStrUtils;
import com.chipsea.code.util.LogUtil;
import com.chipsea.mode.entity.TimerInfo;
import com.chipsea.ui.R;
import com.chipsea.ui.utils.Constant;
import com.chipsea.ui.utils.MyUtils;
import com.chipsea.ui.utils.Utils;
import com.chipsea.view.text.CustomTextView;
import com.gizwits.gizwifisdk.api.GizDeviceScheduler;
import com.gizwits.gizwifisdk.enumration.GizScheduleWeekday;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bingoogolapple.swipeitemlayout.BGASwipeItemLayout;

/**
 * Created by maxiao on 2017/11/8.
 */

public class TimingListAdapter extends BaseAdapter implements View.OnClickListener {

    private List<BGASwipeItemLayout> mOpenedSil = new ArrayList<BGASwipeItemLayout>();
    private int index;
    private Context context;
    private List<TimerInfo> mTime;
    private final LayoutInflater mInflater;
    private TimerListener timerListener;
    private List<String> weeks;

    public TimingListAdapter(Context context, List<TimerInfo> list) {
        this.context = context;
        this.mTime = list;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        weeks = Constant.getWeekStrConstact(context);
    }

    @Override
    public int getCount() {
        return mTime.size() == 0 ? 1 : mTime.size();
    }

    @Override
    public Object getItem(int position) {
        return mTime.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return mTime.size() == 0 ? 0 : 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int ty = getItemViewType(position);
        if (ty == 0) {
            convertView = LayoutInflater.from(context).inflate(R.layout.timing_list_timing_layout, parent, false);
            LinearLayout timingLayout = (LinearLayout) convertView.findViewById(R.id.timingLayout);
            timingLayout.setOnClickListener(this);
            return convertView;
        } else {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.timing_list_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final TimerInfo timerInfo = mTime.get(position);

            holder.stateText.setText(
                    timerInfo.isOpenTime() ?
                            context.getString(R.string.timingOpenTime) : context.getString(R.string.timingColseTime));
            holder.stateBto.setChecked(timerInfo.isUseTime());

            holder.timeText.setText(timerInfo.getTime().substring(11, 16));
            if (timerInfo.getWeekFlag() == Constant.TIMER_TYPE_ONE_FLAG) {
                long l = System.currentTimeMillis();
                long ll = Long.parseLong(HexStrUtils.bytesToHexString(timerInfo.getDeltaTime()), 16);
                long delta = Utils.getRelative("24:00");
                long t1 = 24 * 3600 + delta;
                String res = "";
                if (ll < delta) {
                    res = context.getString(R.string.today);
                } else if (ll < t1) {
                    res = context.getString(R.string.tomorrow);
                } else {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                    Date date = new Date(l + ll * 1000);
                    res = simpleDateFormat.format(date);
                }
                holder.weekText.setText(res + " " + context.getString(R.string.only_once));
            } else if (timerInfo.getWeekFlag() == Constant.TIMER_TYPE_FLAG_EVERY_DAY) {
                holder.weekText.setText(context.getString(R.string.every_day));
            } else if (timerInfo.getWeekFlag() == Constant.TIMER_TYPE_FLAG_WORKING_DAY) {
                holder.weekText.setText(context.getString(R.string.timingWorkday));
            } else if (timerInfo.getWeekFlag() == Constant.TIMER_TYPE_FLAG_WEEK_DAY) {
                holder.weekText.setText(context.getString(com.chipsea.code.R.string.timingWeekend));
            } else {
                String w = "";
                String week = Integer.toBinaryString((int) timerInfo.getWeekFlag() + 256);//加256 确保转为二进制时 有8位
                LogUtil.e(week);
                week = new StringBuilder(week).reverse().toString();//反转
                for (int i = 0; i < 7; i++) {
                    if (week.substring(i, i + 1).equals("1")) {
                        if (w.equals("")) {
                            w = weeks.get(i);
                        } else {
                            w = w + "," + weeks.get(i);
                        }
                    }
                }
                holder.weekText.setText(w);
            }


            //删除监听
            holder.delText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timerListener.onRemove(timerInfo);
                    Log.e("TAG", "delete");
                    mTime.remove(position);
                    notifyDataSetChanged();
                }
            });

            holder.stateBto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timerInfo.setUseTime(!timerInfo.isUseTime());
                    /*重新计算差值*/
                    String l = Utils.getString(
                            Long.toHexString((
                                    Utils.date2TimeStamp(
                                            timerInfo.getTime(), "yyyy:MM:dd:HH:mm:ss") - System.currentTimeMillis() - 15000) / 1000));
                    timerInfo.setDeltaTime(HexStrUtils.hexStringToBytes(l));
                    timerListener.switchOnChanged(timerInfo);
                }
            });

            LogUtil.e(index + "-----" + position);
            //  if (index != position) {
            holder.swipeItemLayout.close();
            LogUtil.e(">>>>>>>>>>>>>>>>>>>>close");
            // }
            holder.swipeItemLayout.setDelegate(new BGASwipeItemLayout.BGASwipeItemLayoutDelegate() {
                @Override
                public void onBGASwipeItemLayoutOpened(BGASwipeItemLayout swipeItemLayout) {
                    closeOpenedSwipeItemLayoutWithAnim();
                    mOpenedSil.add(swipeItemLayout);
                    index = position;
                }

                @Override
                public void onBGASwipeItemLayoutClosed(BGASwipeItemLayout swipeItemLayout) {
                    mOpenedSil.remove(swipeItemLayout);
                }

                @Override
                public void onBGASwipeItemLayoutStartOpen(BGASwipeItemLayout swipeItemLayout) {
                    closeOpenedSwipeItemLayoutWithAnim();
                }
            });

            return convertView;
        }
    }

    public void closeOpenedSwipeItemLayoutWithAnim() {
        for (BGASwipeItemLayout sil : mOpenedSil) {
            sil.closeWithAnim();
        }
        mOpenedSil.clear();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.timingLayout) {
            if (timerListener != null) {
                timerListener.onAdd();
            }
        }
    }

    class ViewHolder {
        CustomTextView stateText;
        CustomTextView weekText;
        CustomTextView timeText;
        CustomTextView delText;
        ToggleButton stateBto;
        BGASwipeItemLayout swipeItemLayout;

        public ViewHolder(View v) {
            stateText = (CustomTextView) v.findViewById(R.id.stateText);
            weekText = (CustomTextView) v.findViewById(R.id.weekText);
            timeText = (CustomTextView) v.findViewById(R.id.timeText);
            delText = (CustomTextView) v.findViewById(R.id.timedelete);
            stateBto = (ToggleButton) v.findViewById(R.id.stateBto);
            swipeItemLayout = (BGASwipeItemLayout) v.findViewById(R.id.swipe_view);
        }
    }


    public void setTimerListener(TimerListener listener) {
        this.timerListener = listener;
    }

}
