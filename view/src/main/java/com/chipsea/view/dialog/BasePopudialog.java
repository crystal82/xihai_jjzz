package com.chipsea.view.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.PopupWindow;

public class BasePopudialog extends PopupWindow {

	protected View.OnClickListener l;
	protected Context mContext;

	public BasePopudialog(Context context) {
		mContext = context;
		// 设置背景灰暗
		ColorDrawable cd = new ColorDrawable(0x7e000000);
		setBackgroundDrawable(cd);

		setOutsideTouchable(true);
		setFocusable(true);
	}

	public void setOnClickListener(
			View.OnClickListener l) {
		this.l = l;
	}

}
