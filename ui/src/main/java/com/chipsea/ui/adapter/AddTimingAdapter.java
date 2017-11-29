package com.chipsea.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.chipsea.code.util.Constant;
import com.chipsea.mode.entity.TimingEntity;
import com.chipsea.ui.R;
import com.chipsea.view.text.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.swipeitemlayout.BGASwipeItemLayout;

/**
 * Created by hfei on 2015/10/19.
 */
public class AddTimingAdapter extends BaseAdapter {
    private int index;
    private Context context;
    private List<String> dates ;
    private int selectIndex ;

    public int getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    public AddTimingAdapter(Context context,List<String> dates) {
        this.context = context;
       this.dates = dates ;
    }
    @Override
    public int getCount() {
        return dates.size() ;
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
        ViewHolder holder = null ;
        if(convertView  == null){
            holder = new ViewHolder() ;
            convertView = LayoutInflater.from(context).inflate(R.layout.add_timing_liet_item,null) ;
            holder.checkTag = (ImageView) convertView.findViewById(R.id.checkTag);
            holder.typeName = (CustomTextView) convertView.findViewById(R.id.typeName);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.typeName.setText(dates.get(position));
        if(selectIndex == position){
            holder.checkTag.setVisibility(View.VISIBLE);
            holder.typeName.setTextColor(context.getResources().getColor(R.color.main_color));
        }else {
            holder.checkTag.setVisibility(View.GONE);
            holder.typeName.setTextColor(context.getResources().getColor(R.color.gray_text));
        }
        return convertView;
    }
    private class ViewHolder {
        ImageView checkTag ;
        CustomTextView typeName ;
    }

}
