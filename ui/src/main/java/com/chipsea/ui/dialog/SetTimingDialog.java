package com.chipsea.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.chipsea.code.listener.SetTimingCallback;
import com.chipsea.code.util.LogUtil;
import com.chipsea.ui.R;
import com.chipsea.view.dialog.BasePopudialog;
import com.chipsea.view.text.CustomTextView;
import com.chipsea.view.wheel.TosAdapterView;
import com.chipsea.view.wheel.WheelView;
import com.chipsea.view.wheel.adapter.WheelViewAdapter;

public class SetTimingDialog extends BasePopudialog implements OnClickListener, TosAdapterView.OnItemSelectedListener {

    private ViewHolder       mHolder;
    private Context          mContext;
    private boolean          mIsCanZero;
    private WheelViewAdapter hourAdapter;
    private WheelViewAdapter minAdapter;
    private int              hourValue, minValue;
    private SetTimingCallback listener;
    private int centerColer = 0xff000000;
    private int othersColor = 0x64000000;

    @Override
    public void onItemSelected(TosAdapterView<?> parent, View view, int position, long id) {
        if (parent == mHolder.hourWheelView) {
            hourAdapter.setTextColor(position, centerColer, othersColor);
            minAdapter.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            hourValue = hourAdapter.getItem(position);
            LogUtil.e("" + hourValue);
        } else if (parent == mHolder.minWheelView) {
            minAdapter.setTextColor(position, centerColer, othersColor);
            minAdapter.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            minValue = minAdapter.getItem(position);
            LogUtil.e("" + minValue);
        }
    }

    @Override
    public void onNothingSelected(TosAdapterView<?> parent) {
        LogUtil.e(">>>>>>>>>");
    }

    private class ViewHolder {
        ImageView      button_no;
        ImageView      button_ok;
        WheelView      hourWheelView;
        WheelView      minWheelView;
        View           height_cancleView;
        CustomTextView titleName;
    }

    public SetTimingDialog(Context context, int hour, int min, String titile, SetTimingCallback listener, boolean
            isCanZero) {
        super(context);
        this.mContext = context;
        this.mIsCanZero = isCanZero;
        this.hourValue = hour;
        this.minValue = min;
        this.listener = listener;
        View rootView = LayoutInflater.from(context).inflate(
                R.layout.dialog_set_timing, null);
        this.setContentView(rootView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setAnimationStyle(R.style.popwindow_anim_style);
        initView(titile, rootView);
    }

    private void initView(String titile, View v) {
        mHolder = new ViewHolder();
        mHolder.titleName = (CustomTextView) v.findViewById(R.id.titleName);
        mHolder.titleName.setText(titile);
        mHolder.height_cancleView = v.findViewById(R.id.height_cancleView);
        mHolder.button_no = (ImageView) v.findViewById(R.id.button_no);
        mHolder.button_ok = (ImageView) v.findViewById(R.id.button_ok);
        mHolder.hourWheelView = (WheelView) v.findViewById(R.id.hourWheelView);
        mHolder.minWheelView = (WheelView) v.findViewById(R.id.minWheelView);
        hourAdapter = new WheelViewAdapter(mContext, 0, 23);
        hourAdapter.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        minAdapter = new WheelViewAdapter(mContext, 0, 59);
        minAdapter.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        initWheelView(mHolder.hourWheelView, hourAdapter, hourValue);
        initWheelView(mHolder.minWheelView, minAdapter, minValue);

        mHolder.height_cancleView.setOnClickListener(this);
        mHolder.button_no.setOnClickListener(this);
        mHolder.button_ok.setOnClickListener(this);
        listener.timeResult(hourValue, minValue);

        mHolder.hourWheelView.setOnItemSelectedListener(this);
        mHolder.minWheelView.setOnItemSelectedListener(this);
    }

    /**
     * wheelView 初始化
     *
     * @param wheelView
     * @param adapter
     * @param cur
     */
    private void initWheelView(WheelView wheelView, WheelViewAdapter adapter,
                               int cur) {
        wheelView.setBackgroundColor(Color.TRANSPARENT);
        wheelView.setAdapter(adapter);
        wheelView.setSelection(cur);
        adapter.setTextColor(cur, centerColer, othersColor);
    }

    @Override
    public void onClick(View view) {
        if (view == mHolder.button_no) {

        } else if (view == mHolder.button_ok) {
            if (hourValue == 0 && minValue == 0 && !mIsCanZero) {
                Toast.makeText(mContext, R.string.please_set_down_useful, Toast.LENGTH_SHORT).show();
                return;
            }
            listener.timeResult(hourValue, minValue);
        }
        dismiss();
    }

}
