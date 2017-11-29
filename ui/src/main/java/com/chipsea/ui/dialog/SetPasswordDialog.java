package com.chipsea.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.chipsea.code.util.LogUtil;
import com.chipsea.code.util.PrefsUtil;
import com.chipsea.code.util.StandardUtil;
import com.chipsea.ui.R;
import com.chipsea.ui.utils.MyUtils;
import com.chipsea.view.dialog.BaseDialog;
import com.chipsea.view.edit.CustomEditText;
import com.chipsea.view.text.CustomTextView;
import com.gizwits.gizwifisdk.api.GizWifiSDK;


public class SetPasswordDialog extends BaseDialog implements View.OnClickListener, CompoundButton
        .OnCheckedChangeListener {
    private Context      _context;
    private StandardUtil mStandardUtil;
    private ViewHolder   mHolder;
    public  String       mSurePassword;

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int            i = buttonView.getId();
        CustomEditText view;
        if (i == R.id.cbLaws1) {
            view = mHolder.oldPassword;

        } else if (i == R.id.cbLaws2) {
            view = mHolder.newPassword;
        } else {
            view = mHolder.surePassword;
        }
        String psw = view.getText().toString();
        if (isChecked) {
            view.setInputType(0x81);
        } else {
            view.setInputType(0x90);
        }
        view.setSelection(psw.length());
    }

    private class ViewHolder {
        CustomEditText oldPassword, newPassword, surePassword;
        CustomTextView changeButton;
        ImageView      closeImg;
        CheckBox       cbLaws1, cbLaws2, cbLaws3;
    }

    public SetPasswordDialog(Context context) {
        super(context);
        _context = context;
        mStandardUtil = StandardUtil.getInstance(context);
        View vv = LayoutInflater.from(context).inflate(
                R.layout.dialog_set_password, null);
        addView(vv);
        mHolder = new ViewHolder();
        mHolder.closeImg = (ImageView) vv.findViewById(R.id.closeImg);
        mHolder.oldPassword = (CustomEditText) vv.findViewById(R.id.old_password);
        mHolder.newPassword = (CustomEditText) vv.findViewById(R.id.new_password);
        mHolder.surePassword = (CustomEditText) vv.findViewById(R.id.sure_password);
        mHolder.changeButton = (CustomTextView) vv.findViewById(R.id.change_password);
        mHolder.cbLaws1 = (CheckBox) vv.findViewById(R.id.cbLaws1);
        mHolder.cbLaws2 = (CheckBox) vv.findViewById(R.id.cbLaws2);
        mHolder.cbLaws3 = (CheckBox) vv.findViewById(R.id.cbLaws3);

        mHolder.oldPassword.setInputType(0x81);
        mHolder.newPassword.setInputType(0x81);
        mHolder.surePassword.setInputType(0x81);
        mHolder.cbLaws1.setChecked(true);
        mHolder.cbLaws2.setChecked(true);
        mHolder.cbLaws3.setChecked(true);

        mHolder.changeButton.setOnClickListener(this);
        mHolder.closeImg.setOnClickListener(this);
        mHolder.cbLaws1.setOnCheckedChangeListener(this);
        mHolder.cbLaws2.setOnCheckedChangeListener(this);
        mHolder.cbLaws3.setOnCheckedChangeListener(this);
    }


    private void changePassword() {
        String oldPassword = mHolder.oldPassword.getText().toString();
        if (oldPassword.equals("")) {
            mStandardUtil.showToast(R.string.loginOldPasswordHint);
            return;
        }

        String newPassword = mHolder.newPassword.getText().toString();
        if (!MyUtils.checkPsd(newPassword)) {
            mStandardUtil.showToast(R.string.loginPasswordHint);
            return;
        }
        if (newPassword.equals(oldPassword)) {
            mStandardUtil.showToast(R.string.settingSamPwdTip);
            return;
        }

        mSurePassword = mHolder.surePassword.getText().toString();
        if (!newPassword.equals(mSurePassword)) {
            mStandardUtil.showToast(R.string.loginPwdDifferentTip);
            return;
        }

        GizWifiSDK.sharedInstance().changeUserPassword(
                PrefsUtil.getStringValue(PrefsUtil.KEY_USER_TOKEN, ""),
                oldPassword,
                mSurePassword);
    }


    @Override
    public void onClick(View v) {
        if (v == mHolder.changeButton) {
            changePassword();
        } else {
            this.dismiss();
        }
    }

    public void onSetPsdSuccess() {
        LogUtil.d("-----onSetPsdSuccess----");
        PrefsUtil.setStringValue(PrefsUtil.KEY_USER_PSD, mSurePassword);
        dismiss();
    }
}
