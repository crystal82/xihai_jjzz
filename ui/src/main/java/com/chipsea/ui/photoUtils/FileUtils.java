package com.chipsea.ui.photoUtils;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rain1_wen on 2016/8/22.
 */
public class FileUtils {
    private static final String tag = FileUtils.class.getName();

    public FileUtils() {
    }

    public static int getUrlFileSize(String urlFile) {
        try {
            URL               e    = new URL(urlFile);
            HttpURLConnection conn = (HttpURLConnection) e.openConnection();
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);
            int size = conn.getContentLength();
            conn.disconnect();
            return size;
        } catch (Exception var4) {
            Log.e(tag, "getUrlFileSize", var4);
            return 0;
        }
    }

    public static long getFileSize(String filename) {
        File file = new File(filename);
        long size = 0L;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            File[] var8  = files;
            int    var7  = files.length;

            for (int var6 = 0; var6 < var7; ++var6) {
                File f = var8[var6];
                size += getFileSize(f.getAbsolutePath());
            }
        } else {
            size = file.length();
        }

        return size;
    }

    public static File createDir(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            file.mkdirs();
        }

        return file;
    }

    public static void writeImage(Bitmap bitmap, String destPath, int quality) {
        try {
            deleteFile(destPath);
            if (createFile(destPath)) {
                FileOutputStream out = new FileOutputStream(destPath);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建一个文件，创建成功返回true
     *
     * @param filePath
     * @return
     */
    public static boolean createFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                return file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 删除一个文件
     *
     * @param filePath 要删除的文件路径名
     * @return true if this file was deleted, false otherwise
     */
    public static boolean deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                return file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
