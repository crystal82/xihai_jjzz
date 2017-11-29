package com.chipsea.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.chipsea.code.util.ScreenUtils;
import com.chipsea.ui.R;
import com.chipsea.ui.dialog.ShareDialog;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by xulj on 2016/6/30.
 */
public class ShareItemClickListenerImp implements AdapterView.OnItemClickListener, PlatformActionListener {
    private Context context ;
    private ScrollView scrollView;
    public ShareItemClickListenerImp(Context context) {
        this(context, null);
    }

    public ShareItemClickListenerImp(Context context, ScrollView scrollView) {
        this.context = context;
        this.scrollView = scrollView;
        ShareSDK.initSDK(context);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ShareDialog.close();
        String fileName = "";
        if (scrollView != null) {
            fileName = ScreenUtils.oneKeyShoot(scrollView);
        } else {
            fileName = ScreenUtils.oneKeyShoot((Activity) context);
        }
       if (4 == position) {
           Platform.ShareParams sp = new Platform.ShareParams();
           sp.setText(context.getString(R.string.share_text));
           sp.setImagePath(fileName);
           Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
           weibo.setPlatformActionListener(this); // 设置分享事件回调
           weibo.share(sp);// 执行图文分享
        }else if (3 == position) {
            Platform.ShareParams sp = new Platform.ShareParams();
            sp.setTitle(context.getString(R.string.share));
            sp.setTitleUrl("http://www.tookok.cn"); // 标题的超链接
            sp.setText(context.getString(R.string.share_text));
            sp.setImagePath(fileName);
            sp.setSite(context.getString(R.string.share));
            sp.setSiteUrl("http://www.tookok.cn");
            Platform qzone = ShareSDK.getPlatform(QZone.NAME);
            qzone.setPlatformActionListener(this); // 设置分享事件回调
            qzone.share(sp);// 执行图文分享
        }else if (2 == position) {
           Platform.ShareParams sp = new Platform.ShareParams();
           sp.setTitle(context.getString(R.string.share));
           sp.setTitleUrl("http://www.tookok.cn"); // 标题的超链接
           sp.setText(context.getString(R.string.share_text));
           sp.setImagePath(fileName);
           Platform qq = ShareSDK.getPlatform(QQ.NAME);
           qq.setPlatformActionListener(this); // 设置分享事件回调
           qq.share(sp);// 执行图文分享
       } else if (1 == position) {
            Platform.ShareParams sp = new Platform.ShareParams();
            sp.setShareType(Platform.SHARE_IMAGE);
            sp.setText(context.getString(R.string.share_text));
            sp.setImagePath(fileName);
            Platform wechart = ShareSDK.getPlatform(WechatMoments.NAME);
            wechart.setPlatformActionListener(this); // 设置分享事件回调
            wechart.share(sp);// 执行图文分享
        } else if (0 == position) {
            Platform.ShareParams sp = new Platform.ShareParams();
            sp.setShareType(Platform.SHARE_IMAGE);
            sp.setText(context.getString(R.string.share_text));
            sp.setImagePath(fileName);
            Platform wechart = ShareSDK.getPlatform(Wechat.NAME);
            wechart.setPlatformActionListener(this); // 设置分享事件回调
            wechart.share(sp);// 执行图文分享
        }
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (platform.getName().equals(Wechat.NAME) || platform.getName().equals(WechatMoments.NAME)) {
            return;
        }
        show(R.string.share_completed);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        show(R.string.share_failed);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        show(R.string.share_canceled);
    }
    private void show(final int textid) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, textid, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
