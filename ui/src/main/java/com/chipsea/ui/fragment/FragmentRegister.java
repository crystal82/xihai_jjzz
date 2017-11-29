package com.chipsea.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chipsea.code.business.VerifyCodeBusiness;
import com.chipsea.code.util.PrefsUtil;
import com.chipsea.code.util.StandardUtil;
import com.chipsea.configuration.Config;
import com.chipsea.ui.R;
import com.chipsea.ui.activity.AddDeviceActivity;
import com.chipsea.ui.activity.LoginRegisterActivity;
import com.chipsea.ui.activity.MainActivity;
import com.chipsea.ui.utils.MyUtils;
import com.chipsea.view.dialog.LoadDialog;
import com.chipsea.view.edit.CustomEditText;
import com.chipsea.view.text.CustomTextView;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizUserAccountType;

import chipsea.wifiplug.lib.GosDeploy;

public class FragmentRegister extends Fragment implements View.OnClickListener {
    private ViewHolder            mViewHolder;
    private StandardUtil          mStandardUtil;
    private PrefsUtil             mPrefsUtil;
    public  String                mPassword;
    public  String                mPhone;
    private LoginRegisterActivity mActivity;
    private LoadDialog            mLoadDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        mStandardUtil = StandardUtil.getInstance(getContext());
        mPrefsUtil = PrefsUtil.getInstance(getContext());

        mViewHolder = new ViewHolder();
        mViewHolder.phoneNumber = (CustomEditText) view.findViewById(R.id.register_phone_number);
        mViewHolder.password = (CustomEditText) view.findViewById(R.id.register_password);
        mViewHolder.verificationCode = (CustomEditText) view.findViewById(R.id.register_verification_code);
        mViewHolder.getVerificationCode = (CustomTextView) view.findViewById(R.id.register_get_verification);
        mViewHolder.register = (CustomTextView) view.findViewById(R.id.register);
        if (Config.USE_PHONE_FOR_LOGIN) {
            mViewHolder.phoneNumber.setHint(getString(R.string.loginPhoneHint));
        } else {
            mViewHolder.phoneNumber.setHint(getString(R.string.loginEmailHint));
        }
        mViewHolder.cbLaws = (CheckBox) view.findViewById(R.id.cbLaws);
        mViewHolder.password.setInputType(0x81);
        mViewHolder.cbLaws.setChecked(true);
        initEvent();
        return view;
    }

    private void initEvent() {
        mViewHolder.getVerificationCode.setOnClickListener(this);
        mViewHolder.register.setOnClickListener(this);
        mViewHolder.cbLaws.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String psw = mViewHolder.password.getText().toString();
                if (isChecked) {
                    mViewHolder.password.setInputType(0x81);
                } else {
                    mViewHolder.password.setInputType(0x90);
                }
                mViewHolder.password.setSelection(psw.length());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mViewHolder.getVerificationCode) {
            //mStandardUtil.showToast("是否登陆:" + UserUtils.isLogin());
            getVerificationCode();
        } else if (v == mViewHolder.register) {
            register();
        }
    }

    private void register() {
        if (!checkInputOK()) {
            return;
        }
        mPhone = "";
        String email   = "";
        String defName = "";
        if (Config.USE_PHONE_FOR_LOGIN) {
            mPhone = mViewHolder.phoneNumber.getText().toString();
            email = "";
            defName = Config.DEF_ACCOUNT_NAME + mPhone.substring(mPhone.length() - 4);
        } else {
            email = mViewHolder.phoneNumber.getText().toString();
            mPhone = "";
            defName = email.split("@")[0];
        }
        mPassword = mViewHolder.password.getText().toString();
        String verCode = mViewHolder.verificationCode.getText().toString();
        if (TextUtils.isEmpty(verCode)) {
            mStandardUtil.showToast(R.string.registerVerificationCodeTip);
            return;
        }
        mActivity = (LoginRegisterActivity) getActivity();
        mLoadDialog = mActivity.progressDialog;
        mLoadDialog.show();
        GizWifiSDK.sharedInstance().registerUser(mPhone, mPassword, verCode,
                                                 GizUserAccountType.GizUserPhone);


    }

    private void goAddDeviceActivity() {
        Intent intent = new Intent(getActivity(), AddDeviceActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    private void goMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
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
        if (!MyUtils.checkPsd(password)) {
            mStandardUtil.showToast(R.string.loginPasswordHint);
            return false;
        }

        return true;
    }

    private void getVerificationCode() {
        if (!checkInputOK()) {
            return;
        }
        String phone     = mViewHolder.phoneNumber.getText().toString();
        String AppSecret = GosDeploy.setAppSecret();
        GizWifiSDK.sharedInstance().requestSendPhoneSMSCode(AppSecret, phone);

    }

    //验证码设备
    public void verifyStart() {
        VerifyCodeBusiness busibess = new VerifyCodeBusiness(getActivity(),
                                                             mViewHolder.getVerificationCode);
        busibess.startTimer();
    }

    public void doLogin() {
        GizWifiSDK.sharedInstance().userLogin(mPhone, mPassword);
    }

    private class ViewHolder {
        CustomEditText phoneNumber;
        CustomEditText password;
        CustomEditText verificationCode;
        CustomTextView getVerificationCode;
        CustomTextView register;
        CheckBox       cbLaws;
    }
}
