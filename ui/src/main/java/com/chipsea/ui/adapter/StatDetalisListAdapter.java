package com.chipsea.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chipsea.mode.entity.StatDetailEntity;
import com.chipsea.ui.R;

import java.util.ArrayList;

/**
 * Created by hfei on 2015/10/19.
 */
public class StatDetalisListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<StatDetailEntity> infos;

    public StatDetalisListAdapter(Context context) {
        this.context = context;
        infos = new ArrayList<>() ;
    }

    public void add(StatDetailEntity entity){
        if(entity!=null){
            infos.add(0,entity);
            if(infos.size()>10){
                infos.remove(infos.size()-1);
            }
            notifyDataSetChanged();
        }
    }

    public ArrayList<StatDetailEntity> getData(){
        return infos;
    }

    @Override
    public int getCount() {
        return infos == null?0:infos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null ;
        StatDetailEntity entity=infos.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.stat_detalis_list_item, parent, false);
            holder.timeText = (TextView) convertView.findViewById(R.id.timeText);
            holder.elecNumber = (TextView) convertView.findViewById(R.id.elecNumber);
            convertView.setTag(holder);
        } else if (convertView.getTag() instanceof ViewHolder) {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.elecNumber.setText(entity.voltage + "V " + entity.electricCurrent + "A " + entity.instantaneousPower + "W");
        holder.timeText.setText(entity.Time);
        return convertView;
    }

    private class ViewHolder {

        TextView timeText,elecNumber;
    }
}
