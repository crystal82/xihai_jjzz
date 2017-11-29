package com.chipsea.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chipsea.code.listener.ManagerRoleCallback;
import com.chipsea.ui.R;
import com.gizwits.gizwifisdk.api.GizUserInfo;

import java.util.ArrayList;
import java.util.List;

public class ManagerDeviceRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<GizUserInfo>   infos;
    private Context             context;
    private Bitmap              mBitmap;
    private ManagerRoleCallback callback;

    public ManagerDeviceRecyclerAdapter(Context context, ManagerRoleCallback callback) {
        this.context = context;
        this.infos = new ArrayList<>();
        this.callback = callback;
    }

    public void setBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            this.mBitmap = bitmap;
            notifyDataSetChanged();
        }
    }

    public void setInfos(List<GizUserInfo> infos) {
        if (infos != null) {
            this.infos.clear();
            this.infos.addAll(infos);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new TopHolder(LayoutInflater.from(context).inflate(R.layout.manager_device_top_layout, parent,
                                                                      false));
        } else {
            return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.manager_device_item_layout, parent,
                                                                       false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder tempHolder, final int position) {
        if (tempHolder instanceof TopHolder) {
            TopHolder topHolder = (TopHolder) tempHolder;
            setImg(mBitmap, topHolder.codeImager);
        } else {
            final GizUserInfo info       = infos.get(position - 1);
            ItemHolder        itemHolder = (ItemHolder) tempHolder;
            itemHolder.nameText.setText(info.getPhone());
            itemHolder.deleteBto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.deleteInfo(info);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return infos.size() + 1;
    }


    static class TopHolder extends RecyclerView.ViewHolder {
        ImageView codeImager;

        public TopHolder(View itemView) {
            super(itemView);
            codeImager = (ImageView) itemView.findViewById(R.id.codeImager);
        }
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        TextView  nameText;
        ImageView deleteBto;

        public ItemHolder(View itemView) {
            super(itemView);
            nameText = (TextView) itemView.findViewById(R.id.nameText);
            deleteBto = (ImageView) itemView.findViewById(R.id.deleteBto);
        }
    }

    public void setBitMap() {

    }

    public void setImg(Bitmap bitmap, ImageView img) {
        if (mBitmap == null || img == null) {
            return;
        }
        //bitmap = BitmapUtil.createQRCode(mBitmap, context.getResources().getDimensionPixelOffset(R.dimen.share_img));
        img.setImageBitmap(bitmap);
    }
}
