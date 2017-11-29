package com.chipsea.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.chipsea.view.R;

/**
 * Created by hfei on 2015/10/20.
 */
public class BaseDialog {

    protected Dialog dialog;
    protected View.OnClickListener l;
    protected Context context;

    public BaseDialog(Context context) {
        this.context = context;
        dialog = new Dialog(context, R.style.dialog_style);
}

    public void addView(View vv) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (dm.widthPixels * 0.9f), ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.addContentView(vv, params);
    }

    public void showDialog() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void setOnClickListener(View.OnClickListener l) {
        this.l = l;
    }
}
