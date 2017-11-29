package com.chipsea.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.chipsea.code.business.ActivityBusiness;
import com.chipsea.code.util.LogUtil;
import com.chipsea.code.util.PrefsUtil;
import com.chipsea.code.util.StandardUtil;
import com.chipsea.configuration.Config;
import com.chipsea.mode.entity.AccountInfo;
import com.chipsea.ui.R;
import com.chipsea.ui.activity.LoginRegisterActivity;
import com.chipsea.ui.activity.MainActivity;
import com.chipsea.ui.dialog.CommonDialog;
import com.chipsea.ui.dialog.NickNameDialog;
import com.chipsea.ui.dialog.RegisterDialog;
import com.chipsea.ui.dialog.SetPasswordDialog;
import com.chipsea.ui.photoUtils.ImageLoadingUtils;
import com.chipsea.ui.photoUtils.view.CirImageAct;
import com.chipsea.ui.utils.MyUtils;
import com.chipsea.view.CircleImageView;
import com.chipsea.view.dialog.LoadDialog;
import com.chipsea.view.text.CustomTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;


public class AccountFragment extends PhotographFragment implements PlatformActionListener {
    private static final String TAG = "AccountFragment";

    private ViewHolder mViewHolder;
    private PrefsUtil  mPrefsUtil;

    private        NickNameDialog    mNickNameDialog;
    private        RegisterDialog    registerDialog;
    private static SetPasswordDialog setPasswordDialog;
    private        AccountInfo       mAccountInfo;
    private        StandardUtil      mStandardUtil;
    private LoadDialog mLoadDialog = null;
    private int        mLoginType  = 0;
    private Context mContext;
    private String  mLogoFile;
    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .showImageOnFail(R.mipmap.role_head_default)
            .showImageOnLoading(R.mipmap.role_head_default)
            .build();
    private MainActivity mActivity;
    private LinearLayout mLl_user_echo;
    private LinearLayout mLl_language;
    private LinearLayout mLl_idea;
    private LinearLayout mLl_about;
    private AlertDialog  mAlertLanguageDialog;
    private String[]     mLanguages;

    public void onRePsdSuccess() {
        LogUtil.d("setPasswordDialog != null:" + (setPasswordDialog != null));
        if (setPasswordDialog != null) {
            setPasswordDialog.onSetPsdSuccess();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentSub(R.layout.activity_account, R.string.settingAccount);
        mActivity = (MainActivity) getActivity();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(instance)
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(config);
        mContext = getActivity();
        mPrefsUtil = PrefsUtil.getInstance(getActivity());
        mStandardUtil = StandardUtil.getInstance(getActivity());
        mLanguages = getResources().getStringArray(R.array.LanguageItem);
        initView();
        refreshAccout();
        initImgUI();
    }

    private void initView() {
        mLl_user_echo = (LinearLayout) mParentView.findViewById(R.id.ll_user_echo);
        mLl_language = (LinearLayout) mParentView.findViewById(R.id.ll_language);
        mLl_idea = (LinearLayout) mParentView.findViewById(R.id.ll_idea);
        mLl_about = (LinearLayout) mParentView.findViewById(R.id.ll_about);
        mLl_user_echo.setOnClickListener(this);
        mLl_language.setOnClickListener(this);
        mLl_idea.setOnClickListener(this);
        mLl_about.setOnClickListener(this);

        mViewHolder = new ViewHolder();
        mViewHolder.headImage = (CircleImageView) mParentView.findViewById(R.id.my_head_image);
        mViewHolder.registePanel = (LinearLayout) mParentView.findViewById(R.id.my_registe_panel);
        mViewHolder.pwdPanel = (LinearLayout) mParentView.findViewById(R.id.pwd_change_panel);
        mViewHolder.thirdPartPanel = (LinearLayout) mParentView.findViewById(R.id.thirdPartPanel);
        mViewHolder.nikeName = (CustomTextView) mParentView.findViewById(R.id.my_nick_name);
        mViewHolder.phoneNumber = (CustomTextView) mParentView.findViewById(R.id.my_phone_number);
        mViewHolder.registe = (CustomTextView) mParentView.findViewById(R.id.my_registe_account);
        mViewHolder.changePwd = (CustomTextView) mParentView.findViewById(R.id.my_change_password);
        mViewHolder.logout = (CustomTextView) mParentView.findViewById(R.id.my_logout);
        mViewHolder.bindQQ = (CustomTextView) mParentView.findViewById(R.id.bind_qq);
        mViewHolder.bindSina = (CustomTextView) mParentView.findViewById(R.id.bind_sina);
        mViewHolder.bindWeiXin = (CustomTextView) mParentView.findViewById(R.id.bind_winxin);
        mViewHolder.headImage.setOnClickListener(this);
        mViewHolder.bindQQ.setOnClickListener(this);
        mViewHolder.bindSina.setOnClickListener(this);
        mViewHolder.bindWeiXin.setOnClickListener(this);
        mViewHolder.registePanel.setOnClickListener(this);
        mViewHolder.pwdPanel.setOnClickListener(this);
        mViewHolder.logout.setOnClickListener(this);
        //TODO:昵称修改
        //mViewHolder.nikeName.setOnClickListener(this);
        mViewHolder.pwdPanel.setOnClickListener(this);
        mAccountInfo = mPrefsUtil.getAccountInfo();

        if (Config.THIRDPART_LOGIN) {
            mViewHolder.thirdPartPanel.setVisibility(View.VISIBLE);
        } else {
            mViewHolder.thirdPartPanel.setVisibility(View.INVISIBLE);
        }
    }

    private void initData() {
        if (TextUtils.isEmpty(mAccountInfo.getUserName())) {
            mViewHolder.registePanel.setVisibility(View.VISIBLE);
            mViewHolder.pwdPanel.setVisibility(View.GONE);
        } else {
            mViewHolder.pwdPanel.setVisibility(View.VISIBLE);
            mViewHolder.phoneNumber.setText(mAccountInfo.getUserName());
        }

        if (TextUtils.isEmpty(mAccountInfo.getNickName())) {
            mViewHolder.nikeName.setText(R.string.settingDefautNick);
        } else {
            mViewHolder.nikeName.setText(mAccountInfo.getNickName());
        }

        //TODO:本地头像
        //mLogoFile = "myPhoto.jpg";
        //setHeadPicName(mLogoFile);
        //showHeadPic();
    }

    private void showHeadPic() {
        //ImageLoader.getInstance().displayImage(url, mViewHolder.headImage); // Incoming options will be

    }

    private void onNickNameDialog() {
        if (mNickNameDialog == null) {
            mNickNameDialog = new NickNameDialog(getActivity());
        }
        //mNickNameDialog.setText(mAccountInfo.getNickName());
        mNickNameDialog.showDialog();
        mNickNameDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoadDialog == null) {
                    mLoadDialog = LoadDialog.getShowDialog(mContext);
                }
                mLoadDialog.show();
                final String nickName = mNickNameDialog.getText().toString();
                //UserUtils.changeNickName(nickName, new WIFIVoidCallback() {

                //    @Override
                //    public void onSuccess() {
                //        mLoadDialog.dismiss();
                //        mViewHolder.nikeName.setText(nickName);
                //    }

                //    @Override
                //    public void onFailure(String msg, int code) {
                //        mLoadDialog.dismiss();
                //        showToast(mStandardUtil.getMessage(code, msg));
                //    }
                //});

            }
        });
    }

    private void refreshAccout() {
        //if (mLoadDialog == null) {
        //    mLoadDialog = LoadDialog.getShowDialog(mContext);
        //}
        //mLoadDialog.show();
        initData();
    }

    private void onRegisterDialog() {
        if (registerDialog == null) {
            registerDialog = new RegisterDialog(getActivity());
        }
        registerDialog.showDialog();
        registerDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshAccout();
            }
        });
    }

    //TODO:修改密码
    private void onPwdChangeDialog() {
        LogUtil.d("-----onPwdChangeDialog----");
        setPasswordDialog = new SetPasswordDialog(getActivity());
        setPasswordDialog.showDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (setPasswordDialog != null) {
            setPasswordDialog.dismiss();
            setPasswordDialog = null;
        }
    }

    @Override
    protected void onOtherClick(View v) {
        super.onOtherClick(v);
        if (v == mViewHolder.headImage) {
            onHeadPhotoDialog();
        } else if (v == mViewHolder.logout) {
            onLogoutClick();
        } else if (v == mViewHolder.nikeName) {
            onNickNameDialog();
        } else if (v == mViewHolder.registePanel) {
            onRegisterDialog();
        } else if (v == mViewHolder.pwdPanel) {
            onPwdChangeDialog();
        } else if (v == mViewHolder.bindQQ) {
            loginForQQ();
        } else if (v == mViewHolder.bindSina) {
            loginForSina();
        } else if (v == mViewHolder.bindWeiXin) {
            loginForWX();
        } else if (v == mLl_user_echo) {
            showToast("mLl_user_echo");
        } else if (v == mLl_language) {
            showLanguageDialog();
        } else if (v == mLl_idea) {
            showToast("mLl_idea");
        } else if (v == mLl_about) {
            showToast("mLl_about");
        } else if (v.getId() == R.id.tv_china) {
            MyUtils.changeAppLanguage(getActivity(), "zh");
            PrefsUtil.setStringValue("LANGUAGE", "zh");
            getActivity().recreate();
            mAlertLanguageDialog.dismiss();
        } else if (v.getId() == R.id.tv_english) {
            MyUtils.changeAppLanguage(getActivity(), "en");
            PrefsUtil.setStringValue("LANGUAGE", "en");
            getActivity().recreate();//刷新界面
            mAlertLanguageDialog.dismiss();
        }

    }

    private void showLanguageDialog() {
        mAlertLanguageDialog = MyUtils.showDialog(getActivity(),
                                                  R.layout.dialog_language_info,
                                                  R.id.ll_language_dialog,
                                                  new MyUtils.DialogEvent() {
                                                      @Override
                                                      public void onDataSet(View layout, AlertDialog dialog) {
                                                          layout.findViewById(R.id.tv_china).setOnClickListener
                                                                  (AccountFragment.this);
                                                          layout.findViewById(R.id.tv_english).setOnClickListener
                                                                  (AccountFragment.this);
                                                      }
                                                  });
    }

    private CommonDialog commonDialog;

    private void onLogoutClick() {
        if (commonDialog == null) {
            commonDialog = new CommonDialog(instance, getString(R.string.logoutHint), getString(R.string.sure));
            commonDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commonDialog.showDialog();
                    logoutToClean();
                    Intent intent = new Intent(getActivity(), LoginRegisterActivity.class);
                    startActivity(intent);
                    ActivityBusiness.getInstance().finishAllActivity();
                }
            });
        }
        commonDialog.showDialog();
    }

    private class ViewHolder {
        CircleImageView headImage;
        LinearLayout    registePanel, pwdPanel, thirdPartPanel;
        CustomTextView nikeName, phoneNumber, registe, changePwd, logout, bindQQ, bindWeiXin, bindSina;
        CustomTextView registe_tag_layout;
    }

    private void logoutToClean() {
        PrefsUtil instance = PrefsUtil.getInstance(getActivity());
        instance.logoutToClean();
    }

    private void loginForWX() {
        ShareSDK.initSDK(mContext);
        Platform wx = ShareSDK.getPlatform(Wechat.NAME);
        if (wx.isValid()) {
            wx.removeAccount();
        }
        authorize(wx);
    }

    private void loginForSina() {
        ShareSDK.initSDK(mContext);
        Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
        if (sina.isValid()) {
            sina.removeAccount();
        }
        authorize(sina);
    }

    private void loginForQQ() {
        ShareSDK.initSDK(mContext);
        Platform qzone = ShareSDK.getPlatform(QZone.NAME);
        if (qzone.isValid()) {
            qzone.removeAccount();
        }
        authorize(qzone);
    }

    private void authorize(Platform plat) {
        plat.setPlatformActionListener(this);
        plat.SSOSetting(false);
        plat.authorize();
    }

    @Override
    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
        final String openId   = arg0.getDb().getUserId(); // 获取用户在此平台的ID
        final String token    = arg0.getDb().getToken(); // 获取用户在此平台的ID
        final String nickName = arg0.getDb().getUserName();

        mLoginType = 1;
        if (arg0.getName().equalsIgnoreCase(QZone.NAME)) {
            mLoginType = 1;
        } else if (arg0.getName().equalsIgnoreCase(Wechat.NAME)) {
            mLoginType = 2;
        } else if (arg0.getName().equalsIgnoreCase(SinaWeibo.NAME)) {
            mLoginType = 3;
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        mStandardUtil.showToast(throwable.getMessage());
    }

    @Override
    public void onCancel(Platform platform, int i) {

    }

    @Override
    public void initImgUI() {
        String userImgPath = CirImageAct.getSDPath() + "/picture/crop" +
                PrefsUtil.getStringValue(PrefsUtil.KEY_USER_NAME, "") + ".png";
        LogUtil.d("----initImgUI----:" + userImgPath);
        if (TextUtils.isEmpty(userImgPath)) {
            mViewHolder.headImage.setImageResource(R.mipmap.role_head_default);
        } else {
            ImageLoadingUtils.getImage(mViewHolder.headImage, userImgPath, R.mipmap.role_head_default);
        }
    }

}
