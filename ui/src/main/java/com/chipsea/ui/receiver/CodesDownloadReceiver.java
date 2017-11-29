package com.chipsea.ui.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/3/3.
 */

public class CodesDownloadReceiver extends BroadcastReceiver {
    public static final String TAG = "CodesDownloadReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(action)) {
            // 点击下载进度通知时, 对应的下载ID以数组的方式传递
            long[] ids = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
            Log.i(TAG, "用户点击了通知" + "ids: " + Arrays.toString(ids));
        } else if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            /*
             * 获取下载完成对应的下载ID, 这里下载完成指的不是下载成功, 下载失败也算是下载完成,
             * 所以接收到下载完成广播后, 还需要根据 id 手动查询对应下载请求的成功与失败.
             */
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
            Log.i(TAG, "onReceive:id " + downloadId + "下载完成");
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            Cursor cursor = manager.query(query);
            if (!cursor.moveToFirst()) {
                cursor.close();
                return;
            }
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            // 下载文件在本地保存的路径（Android 7.0 以后 COLUMN_LOCAL_FILENAME 字段被弃用, 需要用 COLUMN_LOCAL_URI 字段来获取本地文件路径的 Uri）
            String localFilename = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
            // 已下载的字节大小
            long downloadedSoFar = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            // 下载文件的总字节大小
            long totalSize = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            cursor.close();
/*
 * 判断是否下载成功，其中状态 status 的值有 5 种:
 *     DownloadManager.STATUS_SUCCESSFUL:   下载成功
 *     DownloadManager.STATUS_FAILED:       下载失败
 *     DownloadManager.STATUS_PENDING:      等待下载
 *     DownloadManager.STATUS_RUNNING:      正在下载
 *     DownloadManager.STATUS_PAUSED:       下载暂停
 */         Intent broadcast = new Intent(context.getPackageName() + TAG) ;
            broadcast.putExtra("downloadId", downloadId) ;
            broadcast.putExtra("downloadResult",status) ;
            context.sendBroadcast(broadcast);
            if (status == DownloadManager.STATUS_SUCCESSFUL) {
    /*
     * 特别注意: 查询获取到的 localFilename 才是下载文件真正的保存路径，在创建
     * 请求时设置的保存路径不一定是最终的保存路径，因为当设置的路径已是存在的文件时，
     * 下载器会自动重命名保存路径，例如: .../demo-1.apk, .../demo-2.apk
     */     Log.i(TAG, "下载成功, 打开文件, 文件路径: " + localFilename);


            }else{
                Log.i(TAG, "下载失败, 打开文件, 文件路径: " + localFilename);
            }
        }
    }
}
