package com.chipsea.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chipsea.code.listener.SelectAirRecyclerviewCallback;
import com.chipsea.mode.entity.AirModel;
import com.chipsea.ui.R;
import com.chipsea.view.text.CustomTextView;
import com.chipsea.view.text.MarqueeTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xulj on 2016/9/8.
 */
public class SelectAirRecyclerViewAdapter extends RecyclerView.Adapter<SelectAirRecyclerViewAdapter.MyHolder>{
    private List<AirModel> datas = new ArrayList<>();
    private Context context;
    private SelectAirRecyclerviewCallback<AirModel> callback ;

    public void setDatas(List<AirModel> datas) {
        if (datas != null) {
            this.datas.clear();
            this.datas.addAll(datas);
        }
        notifyDataSetChanged();
    }
    public SelectAirRecyclerViewAdapter(Context context , SelectAirRecyclerviewCallback<AirModel> callback) {
        this.context = context;
        this.callback = callback ;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.select_air_item, parent, false);
       return new MyHolder(view) ;
    }

    @Override
    public int getItemViewType(int position) {
      return  1 ;
    }

    @Override
    public void onBindViewHolder(final MyHolder tempHolder, final int position) {
        final AirModel model = datas.get(position) ;
        String searchStr = model.getM_search_string() ;
        String[] strArray = searchStr.split(" ");
        if(strArray.length > 0){
            tempHolder.airBrand.setText(strArray[0]);
            String str = "" ;
            for (int i = 1; i <strArray.length ; i++) {
                str += strArray[i] + " " ;
            }
            tempHolder.cateText.setText(str);
        }else {
            tempHolder.airBrand.setText(searchStr);
            tempHolder.cateText.setText("");
        }
        tempHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback != null){
                    callback.onClickItem(model) ;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        LinearLayout itemLayout ;
        CustomTextView airBrand;
        MarqueeTextView cateText;

        public MyHolder(View itemView) {
            super(itemView);
            itemLayout = (LinearLayout) itemView.findViewById(R.id.itemLayout);
            airBrand = (CustomTextView) itemView.findViewById(R.id.airBrand);
            cateText = (MarqueeTextView) itemView.findViewById(R.id.cateText);
        }
    }
}
