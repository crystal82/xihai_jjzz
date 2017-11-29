package com.chipsea.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chipsea.code.business.ActivityBusiness;
import com.chipsea.code.util.SystemBarTintManager;
import com.chipsea.ui.GosBaseActivity;
import com.chipsea.ui.R;
import com.chipsea.ui.app.BaseApplication;
import com.chipsea.view.text.CustomTextView;
import com.chipsea.view.utils.CustomToast;

public class CommonActivity extends GosBaseActivity implements View.OnClickListener {
    protected BaseApplication app;
    //手指上下滑动时的最小速度
    private static final int YSPEED_MIN = 1000;

    //手指向右滑动时的最小距离
    private static final int XDISTANCE_MIN = 100;

    //手指向上滑或下滑时的最小距离
    private static final int YDISTANCE_MIN = 100;

    //记录手指按下时的横坐标。
    private float xDown;

    //记录手指按下时的纵坐标。
    private float yDown;

    //记录手指移动时的横坐标。
    private float xMove;

    //记录手指移动时的纵坐标。
    private float yMove;

    //用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;
    public ViewHolder mViewHolder;
    private LayoutInflater mInflater;
    SystemBarTintManager tintManager ;
    public  class ViewHolder {
        public RelativeLayout sildingFinishLayout;
        LinearLayout parentView;
        LinearLayout titleLayout ;
        LinearLayout commonContentLayout;
        ImageButton commonLeftBto , commonRightBto ;
        CustomTextView commonTitleText ,commonRightText ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.main_color);//通知栏所需颜色
        }
        setContentView(R.layout.activity_common);
        app = (BaseApplication) getApplication();

        init();
    }
    protected void setStatusBarColor(int colorId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            tintManager.setStatusBarTintResource(colorId);//通知栏所需颜色
        }
    }
    public void hintTitleBar(){
        mViewHolder.titleLayout.setVisibility(View.GONE);
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        System.gc();
    }

    private void init() {
        mViewHolder = new ViewHolder();
        mViewHolder.sildingFinishLayout = (RelativeLayout) findViewById(R.id.slide_layout);
        mViewHolder.parentView = (LinearLayout) findViewById(R.id.common_parent);
        mViewHolder.titleLayout = (LinearLayout) findViewById(R.id.titleLayout);
        mViewHolder.commonLeftBto = (ImageButton) findViewById(R.id.commonLeftBto);
        mViewHolder.commonTitleText = (CustomTextView) findViewById(R.id.commonTitleText);
        mViewHolder.commonRightBto = (ImageButton) findViewById(R.id.commonRightBto);
        mViewHolder.commonRightText = (CustomTextView) findViewById(R.id.commonRightText);
        mViewHolder.commonContentLayout = (LinearLayout) findViewById(R.id.commonContentLayout);


        mViewHolder.commonLeftBto.setOnClickListener(this);
        mViewHolder.commonRightBto.setOnClickListener(this);
        mViewHolder.commonRightText.setOnClickListener(this);
//        mViewHolder.sildingFinishLayout
//                .setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {
//                    @Override
//                    public void onSildingFinish() {
//                        finishSelf();
//                    }
//                });
//        mViewHolder.sildingFinishLayout.setTouchView(mViewHolder.parentView);
        mInflater = LayoutInflater.from(this);
    }

    public void setTouchView(View touchView) {
//        mViewHolder.sildingFinishLayout.setTouchView(touchView);
    }

    protected void onRight() {

    }

    protected void onLeft() {

    }

    /**
     * 创建VelocityTracker对象，并将触摸界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    /**
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getYVelocity();
        return Math.abs(velocity);
    }

    @Override
    public void finish() {
        super.finish();

    }

    /**
     * 添加试图
     *
     * @param layoutId
     */
    protected void addView(int layoutId) {
        if (mInflater != null) {
            View view = mInflater.inflate(layoutId,
                    mViewHolder.commonContentLayout, false);
            mViewHolder.commonContentLayout.addView(view);
        }
    }
    protected void setLeftImage(int resId){
      if(mInflater != null){
          mViewHolder.commonLeftBto.setImageResource(resId);
      }
    }
    protected void setTitleContent(String title){
        mViewHolder.commonTitleText.setText(title);
    }
    protected void setTitleLayoutBackground(int layoutColorId){
        if(layoutColorId != 0){
            mViewHolder.titleLayout.setBackgroundColor(getResources().getColor(layoutColorId));
        }
    }
    protected void setContentSub(int layoutId,int titileId){
        setContentSub(layoutId,0,titileId,0) ;
    }
    protected void setContentSub(int layoutId,int layoutColorId,int titileId){
        setContentSub(layoutId,layoutColorId,titileId,0) ;
    }
    protected void setContentSub(int layoutId,int layoutColorId,int titileId,int rightImgId){
        if(mInflater != null){
            if(layoutId != 0){
                View view = mInflater.inflate(layoutId,
                        mViewHolder.commonContentLayout, false);
                mViewHolder.commonContentLayout.addView(view);
            }
        }
        if(layoutColorId != 0){
            mViewHolder.titleLayout.setBackgroundColor(getResources().getColor(layoutColorId));
        }
        if(titileId != 0){
            mViewHolder.commonTitleText.setText(getResources().getString(titileId));
        }
        if(rightImgId != 0){
            mViewHolder.commonRightBto.setVisibility(View.VISIBLE);
            mViewHolder.commonRightBto.setImageResource(rightImgId);
        }
    }
    /**
     * 添加试图
     *
     * @param view
     */
    protected void addView(View view) {
        if (mViewHolder != null) {
            mViewHolder.commonContentLayout.addView(view);
        }
    }


    public void setMiddle(String text){
        setMiddle(text,0,0,0,0);
    }

    /**
     * 设置中间视图
     *
     * @param text           空则传0
     * @param drawableStart
     * @param drawableTop
     * @param drawableEnd
     * @param drawableButtom
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setMiddle(String text, int drawableStart, int drawableTop, int drawableEnd,
                          int drawableButtom) {
        if (mViewHolder != null) {
            if (text != null) {
                mViewHolder.commonTitleText.setText(text);
            } else {
                mViewHolder.commonTitleText.setText("");
            }
            mViewHolder.commonTitleText.setCompoundDrawablesRelativeWithIntrinsicBounds(drawableStart, drawableTop,
                    drawableEnd, drawableButtom);
        }
    }

    /**
     * 设置中间字体大小
     *
     * @param textSize
     */
    public void setMiddleTextSize(int textSize) {
        mViewHolder.commonTitleText.setTextSize(textSize);
    }


    public void setMiddleLayoutParams(int left, int top, int right, int bottom) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mViewHolder.commonTitleText.getLayoutParams();
        params.setMargins(left, top, right, bottom);
        mViewHolder.commonTitleText.setLayoutParams(params);
    }




    /**
     * 设置右边文字
     *
     * @param titileId
     */
    public void setRightText(int titileId) {
        mViewHolder.commonRightText.setVisibility(View.VISIBLE);
        mViewHolder.commonRightText.setText(getResources().getString(titileId));
    }



    public void showToast(Object text) {
        CustomToast.showToast(this, text.toString(), Toast.LENGTH_SHORT);
    }

    public void showToast(int resId) {
        CustomToast.showToast(this, resId, Toast.LENGTH_SHORT);
    }

    public void startCommonActivity(Intent intent){
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finishSelf();
    }

    protected void finishSelf(){
        if(ActivityBusiness.getInstance().finishActivityEx(this)){
            ActivityBusiness.getInstance().AppExit(this);
        }
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    @Override
    public void onClick(View v) {
        if (v == mViewHolder.commonLeftBto) {
            if(!onLeftClick()){
                finishSelf();
            }
        }else if (v == mViewHolder.commonRightBto) {
            onRightClick();
        } else if(v == mViewHolder.commonRightText){
            onRightTextClick(v);
        }else{
            onOtherClick(v);
        }
    }

    protected void onOtherClick(View v) {

    }
    protected boolean onLeftClick() {
        return false;
    }

    protected void onRightClick() {

    }


    protected void onRightTextClick(View v) {

    }
}
