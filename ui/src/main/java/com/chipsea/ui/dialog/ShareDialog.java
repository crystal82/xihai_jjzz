package com.chipsea.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;

import com.chipsea.ui.R;
import com.chipsea.ui.adapter.ShareAdapter;
import com.chipsea.view.dialog.BottomDialog;
import com.chipsea.view.text.CustomTextView;

public class ShareDialog extends BottomDialog implements OnClickListener {

    private ViewHolder mHolder;
    private static ShareDialog mShareDialog;

    public static ShareDialog show(Context context) {
        close();
        mShareDialog = new ShareDialog(context);
        return mShareDialog;
    }

    public static void close(){
        if(mShareDialog != null){
            mShareDialog.dismiss();
            mShareDialog = null;
        }
    }

    private ShareDialog(Context context) {
        super(context);
        View rootView = LayoutInflater.from(context).inflate(
                R.layout.dialog_share, null);
        mHolder = new ViewHolder();
        mHolder.gridView = (GridView) rootView.findViewById(R.id.share_gridview);
        mHolder.gridView.setAdapter(new ShareAdapter(context));
        mHolder.cancel = (CustomTextView) rootView.findViewById(R.id.share_cancel);
        mHolder.cancel.setOnClickListener(this);
        addView(rootView);
    }

    public void addItemOnclickListner(AdapterView.OnItemClickListener onItemClickListener) {
        mHolder.gridView.setOnItemClickListener(onItemClickListener);
    }

    class ViewHolder {
        GridView gridView;
        CustomTextView cancel;
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
