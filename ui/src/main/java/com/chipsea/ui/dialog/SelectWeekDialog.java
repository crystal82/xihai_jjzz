package com.chipsea.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.chipsea.code.listener.WeekSelectChangeListener;
import com.chipsea.ui.R;
import com.chipsea.ui.adapter.SelectWeekAdapter;
import com.chipsea.view.dialog.BasePopudialog;

import java.util.List;

public class SelectWeekDialog extends BasePopudialog implements AdapterView.OnItemClickListener, OnClickListener {

    private ListView          listView;
    private SelectWeekAdapter adapter;
    ImageView button_no;
    ImageView button_ok;
    private WeekSelectChangeListener listener;

    public void setWeekChangeListener(WeekSelectChangeListener listener) {
        this.listener = listener;
    }

    public SelectWeekDialog(Context context, List<String> selectData) {
        super(context);
        this.mContext = context;
        View rootView = LayoutInflater.from(context).inflate(
                R.layout.dialog_select_week, null);
        this.setContentView(rootView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setAnimationStyle(R.style.popwindow_anim_style);
        button_no = (ImageView) rootView.findViewById(R.id.button_no);
        button_ok = (ImageView) rootView.findViewById(R.id.button_ok);
        listView = (ListView) rootView.findViewById(R.id.listview);
        adapter = new SelectWeekAdapter(context, selectData);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        button_no.setOnClickListener(this);
        button_ok.setOnClickListener(this);
    }


    public void adapterNotifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.setSelectData(position);
    }

    @Override
    public void onClick(View view) {
        if (view == button_no) {

        } else if (view == button_ok) {
            if (listener != null) {
                listener.onChanged(adapter.getSelectData());
            }
        }
        dismiss();
    }
}
