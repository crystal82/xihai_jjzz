package com.chipsea.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chipsea.code.business.VerifyCodeBusiness;
import com.chipsea.code.listener.WIFICallback;
import com.chipsea.code.listener.WIFIVoidCallback;
import com.chipsea.code.util.PrefsUtil;
import com.chipsea.code.util.StandardUtil;
import com.chipsea.configuration.Config;
import com.chipsea.ui.R;
import com.chipsea.ui.utils.MyUtils;
import com.chipsea.view.edit.CustomEditText;
import com.chipsea.view.text.CustomTextView;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizUserAccountType;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;

import java.util.Locale;

import chipsea.wifiplug.lib.GosDeploy;


public class ForgetPasswordActivity extends CommonActivity implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "ForgetPasswordActivity";
    private ViewHolder mViewHolder;
    private Activity   mActInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentSub(R.layout.activity_forget_password, R.string.loginForgetPassword2);
        mActInstance = this;
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        GizWifiSDK.sharedInstance().setListener(gizWifiSDKListener);
    }

    private void initView() {
        mViewHolder = new ViewHolder();
        mViewHolder.phoneNumber = (CustomEditText) findViewById(R.id.forget_phone_number);
        mViewHolder.password = (CustomEditText) findViewById(R.id.forget_new_password);
        mViewHolder.surePassword = (CustomEditText) findViewById(R.id.forget_sure_new_password);
        mViewHolder.verificationCode = (CustomEditText) findViewById(R.id.forget_verification);
        mViewHolder.getVerificationCode = (CustomTextView) findViewById(R.id.forget_get_verification);
        mViewHolder.submit = (CustomTextView) findViewById(R.id.forget_pwd_Submit);
        mViewHolder.cbLaws2 = (CheckBox) findViewById(R.id.cbLaws2);
        mViewHolder.cbLaws3 = (CheckBox) findViewById(R.id.cbLaws3);
        if (Config.USE_PHONE_FOR_LOGIN) {
            mViewHolder.phoneNumber.setHint(getString(R.string.loginPhoneHint));
        } else {
            mViewHolder.phoneNumber.setHint(getString(R.string.loginEmailHint));
        }
        mViewHolder.password.setInputType(0x81);
        mViewHolder.surePassword.setInputType(0x81);
        mViewHolder.cbLaws2.setChecked(true);
        mViewHolder.cbLaws3.setChecked(true);

        initEvent();
    }

    private void initEvent() {
        mViewHolder.getVerificationCode.setOnClickListener(this);
        mViewHolder.submit.setOnClickListener(this);
        mViewHolder.cbLaws2.setOnCheckedChangeListener(this);
        mViewHolder.cbLaws3.setOnCheckedChangeListener(this);
    }

    private boolean checkInputOk() {
        String phone = mViewHolder.phoneNumber.getText().toString();
        if (phone.equals("")) {
            showToast(R.string.loginAccountEmptyTip);
            return false;
        }

        if (Config.USE_PHONE_FOR_LOGIN) {
            if (!StandardUtil.isMobileNO(phone)) {
                showToast(R.string.loginPhoneNumberTip);
                return false;
            }
        } else {
            if (!StandardUtil.isEmail(phone)) {
                showToast(R.string.loginEmailNumberTip);
                return false;
            }
        }

        String newPassword = mViewHolder.password.getText().toString();
        if (!MyUtils.checkPsd(newPassword)) {
            showToast(R.string.loginPasswordHint);
            return false;
        }
        String surePassword = mViewHolder.surePassword.getText().toString();
        if (!newPassword.equals(surePassword)) {
            showToast(R.string.loginPwdDifferentTip);
            return false;
        }

        return true;

    }

    @Override
    public void onOtherClick(View v) {
        if (v == mViewHolder.getVerificationCode) {
            if (!checkInputOk()) return;
            getVerifyCode();
        } else if (v == mViewHolder.submit) {
            submit();
        }
    }

    private boolean isChinese() {
        String localLan = getResources().getConfiguration().locale
                .getDisplayLanguage();
        return localLan.equals(Locale.CHINESE.getDisplayLanguage());
    }

    public void getVerifyCode() {
        progressDialog.show();
        String AppSecret = GosDeploy.setAppSecret();
        String phone     = mViewHolder.phoneNumber.getText().toString();
        GizWifiSDK.sharedInstance().requestSendPhoneSMSCode(
                AppSecret,
                phone);
    }

    private void submit() {
        if (!checkInputOk()) return;
        String code        = mViewHolder.verificationCode.getText().toString();
        String phone       = mViewHolder.phoneNumber.getText().toString();
        String newPassword = mViewHolder.password.getText().toString();
        if (TextUtils.isEmpty(code)) {
            showToast(R.string.registerVerificationCodeTip);
            return;
        }
        GizWifiSDK.sharedInstance().resetPassword(
                phone, code, newPassword,
                GizUserAccountType.GizUserPhone);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int            i = buttonView.getId();
        CustomEditText view;
        if (i == R.id.cbLaws2) {
            view = mViewHolder.password;
        } else {
            view = mViewHolder.surePassword;
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
        CustomEditText phoneNumber, password, surePassword, verificationCode;
        CustomTextView getVerificationCode, submit;
        CheckBox cbLaws2, cbLaws3;
    }

    private GizWifiSDKListener gizWifiSDKListener = new GizWifiSDKListener() {

        /** 用于手机验证码 */
        public void didRequestSendPhoneSMSCode(GizWifiErrorCode result, String token) {
            progressDialog.cancel();
            if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
                showToast(toastError(result));
            } else {
                mViewHolder.getVerificationCode.setClickable(false);
                mViewHolder.getVerificationCode.setText("60");
                VerifyCodeBusiness busibess = new VerifyCodeBusiness(mActInstance, mViewHolder.getVerificationCode);
                busibess.startTimer();
            }
        }

        /** 用于重置密码 */
        public void didChangeUserPassword(GizWifiErrorCode result) {
            progressDialog.cancel();
            if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
                showToast(toastError(result));
            } else {
                showToast(R.string.reset_successful);
                finish();
            }
        }
    };
}
