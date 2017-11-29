package com.chipsea.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.chipsea.ui.R;
import com.chipsea.view.dialog.BaseDialog;


public class DeviceShareDialog extends BaseDialog {
	private Context _context;
	public DeviceShareDialog(Context context) {
		super(context);
		_context=context;
		View vv = LayoutInflater.from(context).inflate(
				R.layout.dialog_share_device, null);
		addView(vv);

	}
}
