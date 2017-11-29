package com.chipsea.ui.fragment;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chipsea.code.util.LogUtil;
import com.chipsea.code.util.ScreenUtils;
import com.chipsea.code.util.SystemBarTintManager;
import com.chipsea.ui.R;
import com.chipsea.ui.activity.MainActivity;
import com.chipsea.view.dialog.LoadDialog;
import com.chipsea.view.text.CustomTextView;
import com.chipsea.view.utils.CustomToast;

public class LazyFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = LazyFragment.class.getSimpleName();
    protected MainActivity instance;
    protected View         mParentView;
    public    ViewHolder   mViewHolder;
    SystemBarTintManager tintManager;
    private LayoutInflater mInflater;
    private LoadDialog     mLoadDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.i(TAG, "onCreateView()");
        instance = (MainActivity) getActivity();
        mParentView = inflater.inflate(R.layout.activity_common, container, false);
        return mParentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.i(TAG, "onActivityCreated()");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            tintManager = new SystemBarTintManager(instance);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.main_color);//通知栏所需颜色
            Window window = getActivity().getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mInflater = LayoutInflater.from(instance);
        init();
    }

    private void init() {
        mViewHolder = new ViewHolder();
        mViewHolder.sildingFinishLayout = (RelativeLayout) mParentView.findViewById(R.id.slide_layout);
        mViewHolder.parentView = (LinearLayout) mParentView.findViewById(R.id.common_parent);
        mViewHolder.titleLayout = (LinearLayout) mParentView.findViewById(R.id.titleLayout);
        mViewHolder.commonLeftBto = (ImageButton) mParentView.findViewById(R.id.commonLeftBto);
        mViewHolder.commonTitleText = (CustomTextView) mParentView.findViewById(R.id.commonTitleText);
        mViewHolder.commonRightBto = (ImageButton) mParentView.findViewById(R.id.commonRightBto);
        mViewHolder.commonRightText = (CustomTextView) mParentView.findViewById(R.id.commonRightText);
        mViewHolder.commonContentLayout = (LinearLayout) mParentView.findViewById(R.id.commonContentLayout);
        mViewHolder.commonLeftBto.setImageResource(R.mipmap.menu_icon);
        mViewHolder.titleLayout.setPadding(0, ScreenUtils.getStatusBarHeight(getActivity()), 0, 6);

        mViewHolder.commonLeftBto.setOnClickListener(this);
        mViewHolder.commonRightBto.setOnClickListener(this);
        mViewHolder.commonRightText.setOnClickListener(this);

    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window                     win       = instance.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int                  bits      = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onClick(View v) {
        if (v == mViewHolder.commonLeftBto) {
            instance.openMenu();
        } else if (v == mViewHolder.commonRightBto) {
            onRightClick();
        } else if (v == mViewHolder.commonRightText) {
            onRightTextClick(v);
        } else {
            onOtherClick(v);
        }
    }

    protected void onRightClick() {

    }


    protected void onRightTextClick(View v) {

    }

    protected void onOtherClick(View v) {

    }

    public class ViewHolder {
        public RelativeLayout sildingFinishLayout;
        LinearLayout parentView;
        LinearLayout titleLayout;
        LinearLayout commonContentLayout;
        ImageButton  commonLeftBto, commonRightBto;
        CustomTextView commonTitleText, commonRightText;
    }

    public void showLoadDialog() {
        if (mLoadDialog == null) {
            Log.i(TAG, "showLoadDialog: 初始化");
            mLoadDialog = LoadDialog.getShowDialog(instance);
        }
        mLoadDialog.show();
    }

    public void dismissLoadDialog() {
        if (mLoadDialog != null && mLoadDialog.isShowing()) {
            mLoadDialog.dismiss();
            Log.i(TAG, "dismissLoadDialog: 关闭dialog");
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        LogUtil.i(TAG, "onCreateAnimation()");
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.i(TAG, "onDestroy()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.i(TAG, "onDestroyView()");
    }

    protected void setContentSub(int layoutId, int titileId) {
        setContentSub(layoutId, 0, titileId, 0);
    }

    protected void setContentSub(int layoutId, int layoutColorId, int titileId) {
        setContentSub(layoutId, layoutColorId, titileId, 0);
    }

    protected void setContentSub(int layoutId, int layoutColorId, int titileId, int rightImgId) {
        if (mInflater != null) {
            if (layoutId != 0) {
                View view = mInflater.inflate(layoutId, mViewHolder.commonContentLayout, false);
                mViewHolder.commonContentLayout.addView(view);
            }
        }
        if (layoutColorId != 0) {
            mViewHolder.titleLayout.setBackgroundColor(getResources().getColor(R.color.gray_text));
        }
        if (titileId != 0) {
            mViewHolder.commonTitleText.setText(getResources().getString(titileId));
        }
        if (rightImgId != 0) {
            mViewHolder.commonRightBto.setVisibility(View.VISIBLE);
            mViewHolder.commonRightBto.setImageResource(rightImgId);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.i(TAG, "onDetach()");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.i(TAG, "onPause()");
    }

    @Override
    public void onResume() {
        LogUtil.i(TAG, "onResume()");
        instance.closeMenu();
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        LogUtil.i(TAG, "onHiddenChanged()");
        super.onHiddenChanged(hidden);
        if (!hidden) {
            instance.closeMenu();
        }
    }

    public void showToast(String text) {
        CustomToast.showToast(instance, text, Toast.LENGTH_SHORT);
    }

    public void showToast(int resId) {
        CustomToast.showToast(instance, resId, Toast.LENGTH_SHORT);
    }

    public void startCommonActivity(Intent intent) {
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }
}
