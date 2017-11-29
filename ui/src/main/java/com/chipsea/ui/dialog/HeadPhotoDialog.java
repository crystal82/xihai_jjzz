package com.chipsea.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.chipsea.ui.R;
import com.chipsea.view.dialog.BaseDialog;

public class HeadPhotoDialog extends BaseDialog implements OnClickListener {

	private TextView camera,alum;
	private ImageView cancelButton;
	private int mode;

	public HeadPhotoDialog(Context context) {
		super(context);
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_photo, null);
		addView(view);
		camera = (TextView) view.findViewById(R.id.head_camera);
		alum = (TextView) view.findViewById(R.id.head_alum);
		cancelButton = (ImageView) view.findViewById(R.id.cancel);
		camera.setOnClickListener(this);
		alum.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
	}

	public int getMode(){
		return mode;
	}

	@Override
	public void onClick(View view) {
		if(view == camera){
			if(l != null){
				mode = 0;
				l.onClick(camera);
			}
		}else if(view == alum){
			if(l != null){
				mode = 1;
				l.onClick(alum);
			}
		}
		dismiss();
	}
}
