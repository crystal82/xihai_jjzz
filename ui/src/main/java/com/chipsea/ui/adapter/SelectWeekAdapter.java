package com.chipsea.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.chipsea.code.util.Constant;
import com.chipsea.code.util.LogUtil;
import com.chipsea.ui.R;
import com.chipsea.view.text.CustomTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hfei on 2015/10/19.
 */
public class SelectWeekAdapter extends BaseAdapter {
    private Context      context;
    private List<String> dates;
    private List<String> selectData;

    public List<String> getSelectData() {
        LogUtil.e("selectData="+selectData.size());
        return selectData;
    }

    public void setSelectData(int i) {
        if (selectData.get(i).equals("1")) {
            selectData.set(i, "0");
        } else {
            selectData.set(i, "1");
        }
        notifyDataSetChanged();
    }

    public SelectWeekAdapter(Context context, List<String> selectData) {
        this.context = context;
        this.dates = Constant.getWeekStrConstact(context);
        this.selectData=selectData;

    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.select_week_liet_item, null);
            holder.checkTag = (ImageView) convertView.findViewById(R.id.checkTag);
            holder.weekName = (CustomTextView) convertView.findViewById(R.id.weekName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String tempWeek = dates.get(position);
        holder.weekName.setText(tempWeek);
        if (selectData.get(position).equals("1")) {
            holder.checkTag.setVisibility(View.VISIBLE);
        } else {
            holder.checkTag.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView      checkTag;
        CustomTextView weekName;
    }

}
