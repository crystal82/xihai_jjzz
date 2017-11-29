package com.chipsea.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chipsea.ui.R;
import com.chipsea.view.dialog.BaseDialog;

public class CommonDialog extends BaseDialog implements View.OnClickListener {

    private ViewHolder mViewHolder;


    private class ViewHolder {
        TextView ok_bto , cancel_bto;
        TextView content ,title;
    }

    public CommonDialog(Context context , String contentStr, String sureStr) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.item_base_dialog, null);
        addView(view);
        mViewHolder = new ViewHolder();
        mViewHolder.ok_bto = (TextView) view.findViewById(R.id.ok_bto);
        mViewHolder.cancel_bto = (TextView) view.findViewById(R.id.cancel_bto);
        mViewHolder.title = (TextView) view.findViewById(R.id.title);
        mViewHolder.content = (TextView) view.findViewById(R.id.content);
        mViewHolder.ok_bto.setText(sureStr);
        mViewHolder.content.setText(contentStr);
        mViewHolder.ok_bto.setOnClickListener(this);
        mViewHolder.cancel_bto.setOnClickListener(this);
    }

    public void setTitle(String title){
        mViewHolder.title.setText(title);
    }
    public void dismiss() {
        if (this.dialog != null) {
            this.dialog.dismiss();
        }
    }


    public void onClick(View view) {
        if (view == mViewHolder.ok_bto) {
            if(l != null){
                l.onClick(view);
            }
        }
        dismiss();
    }

    public void showDialog() {
        if (this.dialog != null) {
            this.dialog.show();
        }
    }
}

