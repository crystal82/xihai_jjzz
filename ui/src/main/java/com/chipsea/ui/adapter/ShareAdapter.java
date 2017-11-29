package com.chipsea.ui.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.chipsea.code.util.ImagePresser;
import com.chipsea.ui.R;
import com.chipsea.view.text.CustomTextView;

/**
 * Created by Administrator on 2016/6/12.
 */
public class ShareAdapter extends BaseAdapter {

    private Context context;
    private int[] name = new int[]{R.string.wechat, R.string.wechatmoments,  R.string.qq, R.string.qzone,R.string.sinaweibo};
    private int[] img = new int[]{R.mipmap.share_wechat, R.mipmap.share_pyq, R.mipmap.share_qq, R.mipmap.share_zone,R.mipmap.share_weibo};

    public ShareAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        return name[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CustomTextView textView = new CustomTextView(context);
        textView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setTextColor(context.getResources().getColor(R.color.gray_text));
        textView.setTextSize(30);
        textView.setTypeface(CustomTextView.LTEX);
        textView.setText(name[position]);
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, img[position], 0, 0);
        textView.setCompoundDrawablePadding(ImagePresser.dip2px(context,10));
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}
