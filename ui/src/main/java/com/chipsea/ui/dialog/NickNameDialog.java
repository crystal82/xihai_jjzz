package com.chipsea.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.chipsea.code.util.StandardUtil;
import com.chipsea.ui.R;
import com.chipsea.view.dialog.BaseDialog;
import com.chipsea.view.edit.CustomEditText;
import com.chipsea.view.text.CustomTextView;


public class NickNameDialog extends BaseDialog implements OnClickListener {

	private CustomTextView sure,cancel;
	private CustomEditText nickname;
	private Context _context;

	public NickNameDialog(Context context) {
		super(context);
		_context=context;
		View vv = LayoutInflater.from(context).inflate(
				R.layout.dialog_update_nickname, null);
		addView(vv);
		nickname = (CustomEditText) vv.findViewById(R.id.edit_nickname);
		cancel = (CustomTextView) vv.findViewById(R.id.edit_cancle);
		sure = (CustomTextView) vv.findViewById(R.id.edit_sure);
		cancel.setOnClickListener(this);
		sure.setOnClickListener(this);
	}


	public void setText(String text){
		if(nickname != null){
			nickname.setText(text);
			nickname.setSelection(text.length());
		}
	}

	public void setText(int textId){
		setText(context.getString(textId));
	}

	public Object getText(){
		if(nickname != null){
			return nickname.getText();
		}
		return null;
	}

	private void show(int textId) {
		Toast.makeText(_context, textId, Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onClick(View view) {
		if(view == sure){

			if(!StandardUtil.isNameOk(nickname.getText().toString())){
				show(R.string.accountChangeNickTip);
				return;
			}

			if(l != null){
				l.onClick(sure);
			}
		}
		dismiss();
	}
}
