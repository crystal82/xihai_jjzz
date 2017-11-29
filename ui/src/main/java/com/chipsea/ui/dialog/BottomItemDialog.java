package com.chipsea.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chipsea.ui.R;
import com.chipsea.view.dialog.BottomDialog;


public class BottomItemDialog extends BottomDialog implements
        OnClickListener{
    private ViewHolder mHolder;

    public BottomItemDialog(Context context) {
        super(context);
    }

    public BottomItemDialog(Context context, String item1Str, String item2Str) {
        super(context);
        View rootView = LayoutInflater.from(context).inflate(
                R.layout.two_item_bottom_pop, null);
        mHolder = new ViewHolder() ;
        mHolder.item1 = (TextView) rootView.findViewById(R.id.item1);
        mHolder.item2 = (TextView) rootView.findViewById(R.id.item2);
        mHolder.cancel = (TextView) rootView.findViewById(R.id.cancel);
        mHolder.item1.setText(item1Str);
        mHolder.item2.setText(item2Str);
        mHolder.item1.setOnClickListener(this);
        mHolder.item2.setOnClickListener(this);
        mHolder.cancel.setOnClickListener(this);
        addView(rootView);
    }

    class ViewHolder {
        TextView item1;
        TextView item2;
        TextView cancel ;

    }

    @Override
    public void onClick(View v) {
        if (v == mHolder.item1) {
            if (l != null) {
                l.onClick(mHolder.item1);
            }
        }else if(v == mHolder.item2){
            if (l != null) {
                l.onClick(mHolder.item2);
            }
        }
        dismiss();
    }


}
