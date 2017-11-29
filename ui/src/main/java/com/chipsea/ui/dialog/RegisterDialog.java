package com.chipsea.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.chipsea.code.business.VerifyCodeBusiness;
import com.chipsea.code.util.StandardUtil;
import com.chipsea.configuration.Config;
import com.chipsea.ui.R;
import com.chipsea.view.dialog.BaseDialog;
import com.chipsea.view.edit.CustomEditText;
import com.chipsea.view.text.CustomTextView;

import java.util.Locale;


public class RegisterDialog extends BaseDialog implements View.OnClickListener {
    private Dialog             _context;
    private ViewHolder         mViewHolder;
    private StandardUtil       mStandardUtil;
    private VerifyCodeBusiness mVerifyBusibess;
    private Context            mContext;

    private class ViewHolder {
        CustomEditText phoneNumber, password, verifyCode;
        CustomTextView getVerifyCode, register;
        ImageView closeImg;
    }

    public RegisterDialog(Context context) {
        super(context);
        mContext = context;
        _context = this.dialog;
        View vv = LayoutInflater.from(context).inflate(
                R.layout.dialog_register, null);
        addView(vv);
        mStandardUtil = StandardUtil.getInstance(context);

        mViewHolder = new ViewHolder();
        mViewHolder.phoneNumber = (CustomEditText) vv.findViewById(R.id.register_phone_number);
        mViewHolder.password = (CustomEditText) vv.findViewById(R.id.register_password);
        mViewHolder.verifyCode = (CustomEditText) vv.findViewById(R.id.register_verification_code);
        mViewHolder.getVerifyCode = (CustomTextView) vv.findViewById(R.id.register_get_verification);
        mViewHolder.getVerifyCode.setOnClickListener(this);
        mViewHolder.register = (CustomTextView) vv.findViewById(R.id.register);
        mViewHolder.register.setOnClickListener(this);
        mViewHolder.closeImg = (ImageView) vv.findViewById(R.id.closeImg);
        mViewHolder.closeImg.setOnClickListener(this);

        if (Config.USE_PHONE_FOR_LOGIN) {
            mViewHolder.phoneNumber.setHint(context.getString(R.string.loginPhoneHint));
        } else {
            mViewHolder.phoneNumber.setHint(context.getString(R.string.loginEmailHint));
        }
    }

    private boolean checkInputOK() {
        String phone = mViewHolder.phoneNumber.getText().toString();
        if (phone.equals("")) {
            mStandardUtil.showToast(R.string.loginAccountEmptyTip);
            return false;
        }

        if (Config.USE_PHONE_FOR_LOGIN) {
            if (!StandardUtil.isMobileNO(phone)) {
                mStandardUtil.showToast(R.string.loginPhoneNumberTip);
                return false;
            }
        } else {
            if (!StandardUtil.isEmail(phone)) {
                mStandardUtil.showToast(R.string.loginEmailNumberTip);
                return false;
            }
        }

        String password = mViewHolder.password.getText().toString();
        if (password.equals("")) {
            mStandardUtil.showToast(R.string.loginPasswordHint);
            return false;
        }

        int length = password.length();
        if (length > 18 || length < 6) {
            mStandardUtil.showToast(R.string.loginLengthLimitTip);
            return false;
        }

        return true;
    }

    private void getVerificationCode() {
        if (!checkInputOK()) {
            return;
        }
        String phone = mViewHolder.phoneNumber.getText().toString();
        //UserUtils.CheckPhoneExist(phone, new WIFICallback<Boolean>(){
        //	@Override
        //	public void onSuccess(Boolean data) {
        //		if(data==true){
        //			mStandardUtil.showToast(R.string.registerExist);
        //		}else{
        //			sendVerifyCode();
        //		}
        //	}

        //	@Override
        //	public void onFailure(String msg, int code) {
        //		mStandardUtil.showToast(mStandardUtil.getMessage(code,msg));
        //	}
        //});
    }

    private boolean isChinese() {
        String localLan = mContext.getResources().getConfiguration().locale
                .getDisplayLanguage();
        return localLan.equals(Locale.CHINESE.getDisplayLanguage());
    }

    private void sendVerifyCode() {
        int templateId = 0;
        if (Config.USE_PHONE_FOR_LOGIN) {

        } else {
            if (!isChinese()) {
                templateId = 2;
            } else {
                templateId = 1;
            }
        }

        String phone = mViewHolder.phoneNumber.getText().toString();
       //UserUtils.sendVerifyCode(phone, templateId, new WIFIVoidCallback() {
       //    @Override
       //    public void onSuccess() {
       //        mViewHolder.getVerifyCode.setClickable(false);
       //        mViewHolder.getVerifyCode.setText("600");
       //        mVerifyBusibess = new VerifyCodeBusiness(_context,
       //                                                 mViewHolder.getVerifyCode);
       //        mVerifyBusibess.startTimerforDialog();
       //    }

       //    @Override
       //    public void onFailure(String msg, int code) {
       //        mStandardUtil.showToast(mStandardUtil.getMessage(code, msg));
       //    }
       //});
    }

    private void bindWithAccount() {
        String phone    = mViewHolder.phoneNumber.getText().toString();
        String password = mViewHolder.password.getText().toString();
        String verCode  = mViewHolder.verifyCode.getText().toString();
        if (verCode.length() == 0) {
            mStandardUtil.showToast(R.string.registerVerificationCodeTip);
            return;
        }
        String defName = "OKOK_" + phone.substring(phone.length() - 4, phone.length());
       //UserUtils.bindWithAccount("", phone, password, defName, verCode, new WIFIVoidCallback() {
       //    @Override
       //    public void onSuccess() {
       //        _context.dismiss();
       //        if (mVerifyBusibess != null) {
       //            mVerifyBusibess.cancelTask();
       //        }
       //        if (l != null) {
       //            l.onClick(null);
       //        }
       //    }

       //    @Override
       //    public void onFailure(String msg, int code) {
       //        mStandardUtil.showToast(mStandardUtil.getMessage(code, msg));
       //    }
       //});

    }


    @Override
    public void onClick(View v) {
        if (v == mViewHolder.closeImg) {
            if (mVerifyBusibess != null) {
                mVerifyBusibess.cancelTask();
            }
            this.dismiss();
        } else if (v == mViewHolder.getVerifyCode) {
            getVerificationCode();
        } else {
            bindWithAccount();
        }

    }
}
