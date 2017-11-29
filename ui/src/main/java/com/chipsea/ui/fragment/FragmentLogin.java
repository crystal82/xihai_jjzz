package com.chipsea.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chipsea.code.util.PrefsUtil;
import com.chipsea.code.util.StandardUtil;
import com.chipsea.configuration.Config;
import com.chipsea.ui.R;
import com.chipsea.ui.activity.ForgetPasswordActivity;
import com.chipsea.view.dialog.LoadDialog;
import com.chipsea.view.edit.CustomEditText;
import com.chipsea.view.text.CustomTextView;
import com.gizwits.gizwifisdk.api.GizWifiSDK;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;

public class FragmentLogin extends Fragment implements View.OnClickListener, PlatformActionListener {
    private ViewHolder   mViewHolder;
    private StandardUtil mStandardUtil;
    private String TAG = "FragmentLogin";
    private LoadDialog mLoadDialog;
    private String     mPhone;
    private String     mPassword;
    private PrefsUtil  mPrefsUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mStandardUtil = StandardUtil.getInstance(getContext());
        mPrefsUtil = PrefsUtil.getInstance(getContext());
        mViewHolder = new ViewHolder();
        mViewHolder.password = (CustomEditText) view.findViewById(R.id.login_password);
        mViewHolder.login = (CustomTextView) view.findViewById(R.id.login);
        mViewHolder.forgetPasswod = (CustomTextView) view.findViewById(R.id.login_forget_password);
        mViewHolder.qq = (ImageView) view.findViewById(R.id.qq_login);
        mViewHolder.sina = (ImageView) view.findViewById(R.id.sina_login);
        mViewHolder.wechat = (ImageView) view.findViewById(R.id.wechat_login);
        mViewHolder.thirdpartLayout = (LinearLayout) view.findViewById(R.id.thirdPartLayout);
        mViewHolder.phoneNumber = (CustomEditText) view.findViewById(R.id.login_phone_number);
        mViewHolder.cbLaws = (CheckBox) view.findViewById(R.id.cbLaws);
        if (Config.THIRDPART_LOGIN) {
            mViewHolder.thirdpartLayout.setVisibility(View.VISIBLE);
        } else {
            mViewHolder.thirdpartLayout.setVisibility(View.INVISIBLE);
        }
        if (Config.USE_PHONE_FOR_LOGIN) {
            mViewHolder.phoneNumber.setHint(getString(R.string.loginPhoneHint));
        } else {
            mViewHolder.phoneNumber.setHint(getString(R.string.loginEmailHint));
        }
        mViewHolder.password.setInputType(0x81);
        mViewHolder.cbLaws.setChecked(true);
        initEvent();
        return view;
    }

    private void initEvent() {
        mViewHolder.login.setOnClickListener(this);
        mViewHolder.forgetPasswod.setOnClickListener(this);
        mViewHolder.qq.setOnClickListener(this);
        mViewHolder.wechat.setOnClickListener(this);
        mViewHolder.sina.setOnClickListener(this);

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
        if (v == mViewHolder.login) {
            login();
        } else if (v == mViewHolder.forgetPasswod) {
            Intent intent = new Intent(getActivity(), ForgetPasswordActivity.class);
            getActivity().startActivity(intent);
        } else if (v == mViewHolder.qq) {
            loginForQQ();
        } else if (v == mViewHolder.wechat) {
            loginForWX();
        } else if (v == mViewHolder.sina) {
            loginForSina();
        }
    }

    public void showDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mLoadDialog == null) {
                    mLoadDialog = LoadDialog.getShowDialog(getContext());
                }
                mLoadDialog.show();
            }
        });
    }

    public void closeDialog() {
        if (mLoadDialog != null) {
            mLoadDialog.dismiss();
        }
    }

    private void loginForWX() {
        ShareSDK.initSDK(getContext());
        Platform wx = ShareSDK.getPlatform(Wechat.NAME);
        if (wx.isValid()) {
            wx.removeAccount();
        }
        authorize(wx);
    }

    private void loginForSina() {
        ShareSDK.initSDK(getContext());
        Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
        if (sina.isValid()) {
            sina.removeAccount();
        }
        authorize(sina);
    }

    private void loginForQQ() {
        ShareSDK.initSDK(getContext());
        Platform qzone = ShareSDK.getPlatform(QZone.NAME);
        if (qzone.isValid()) {
            qzone.removeAccount();
        }
        authorize(qzone);
    }

    private void authorize(Platform plat) {
        showDialog();
        plat.setPlatformActionListener(this);
        plat.SSOSetting(false);
        plat.authorize();
    }

    @Override
    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        closeDialog();
        mStandardUtil.showToast(throwable.getMessage());
    }

    @Override
    public void onCancel(Platform platform, int i) {
        closeDialog();
    }

    private class ViewHolder {
        CustomEditText phoneNumber, password;
        CustomTextView login, forgetPasswod;
        ImageView qq, sina, wechat;
        LinearLayout thirdpartLayout;
        CheckBox cbLaws;
    }

    private void login() {
        mPhone = mViewHolder.phoneNumber.getText().toString();
        if (mPhone.equals("")) {
            mStandardUtil.showToast(R.string.loginAccountEmptyTip);
            return;
        }

        if (Config.USE_PHONE_FOR_LOGIN) {
            if (!StandardUtil.isMobileNO(mPhone)) {
                mStandardUtil.showToast(R.string.loginPhoneNumberTip);
                return;
            }
        } else {
            if (!StandardUtil.isEmail(mPhone)) {
                mStandardUtil.showToast(R.string.loginEmailNumberTip);
                return;
            }
        }

        mPassword = mViewHolder.password.getText().toString();
        if (mPassword.equals("")) {
            mStandardUtil.showToast(R.string.loginPwdTip);
            return;
        }

        int length = mPassword.length();
        if (length > 18 || length < 6) {
            mStandardUtil.showToast(R.string.loginLengthLimitTip);
            return;
        }

        showDialog();
        GizWifiSDK.sharedInstance().userLogin(mPhone, mPassword);
    }

    public void saveInfo(String uid, String token) {
        if (!TextUtils.isEmpty(mPhone) && !TextUtils.isEmpty(mPassword)) {
            mPrefsUtil.setAccountInfo(mPhone, mPassword, uid, token);
        }
    }
}
