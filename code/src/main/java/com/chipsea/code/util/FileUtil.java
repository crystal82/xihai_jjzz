package com.chipsea.code.util;

import android.content.Context;
import android.os.Environment;

import com.chipsea.code.listener.WIFIVoidCallback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * 文件工具类
 */
public class FileUtil {
    /**
     * 开始消息提示常量
     */
    public static final int startDownloadMeg = 1;

    /**
     * 更新消息提示常量
     */
    public static final int updateDownloadMeg = 2;

    /**
     * 完成消息提示常量
     */
    public static final int endDownloadMeg = 3;

    /**
     * chipsea目录！
     */
    public static final String CHIPSEA_ROOT_DIR  = "/chipsea/download/";
    public static final String CHIPSEA_ICON_DIR  = "/chipsea/icon/";
    public static final String CHIPSEA_LOG_DIR   = "/chipsea/log/";
    public static final String CHIPSEA_SHARE_DIR = "/chipsea/share/";
    public static final String CHIPSEA_CODES     = "/chipsea/codes/";

    public static final String CHIPSEA_ROOT   = Environment
            .getExternalStorageDirectory() + "/chipsea/";
    /**
     * 头像存储路径
     */
    public static final String PATH_ICON_PATH = Environment
            .getExternalStorageDirectory() + CHIPSEA_ICON_DIR;

    /**
     * 分享图片路径
     */
    public static final String PATH_PHOTO_SHARE = Environment
            .getExternalStorageDirectory() + CHIPSEA_SHARE_DIR;
    /**
     * 图片存储路径
     */
    public static final String PATH_PICTURE     = Environment
            .getExternalStorageDirectory() + CHIPSEA_ICON_DIR;
    /**
     * 异常日志存储路径
     */
    public static final String PATH_LOG         = Environment
            .getExternalStorageDirectory() + CHIPSEA_LOG_DIR;

    /**
     * 截屏
     */
    public static final String SHOT_SCREEN_IMAGE_NAME = "shotimage.png"; // 截屏名称
    public static final String SCREENSHOT_IMAG_PATH   = PATH_PICTURE
            + SHOT_SCREEN_IMAGE_NAME; // 截屏路径
    /**
     * codes text文件存储位置
     */
    public static final String PATH_CODES             = Environment
            .getExternalStorageDirectory() + CHIPSEA_CODES;
    /**
     * 拍照与相册
     */
    //public static final String PICTURE_TMP_IMAGE_NAME = "tmp_image.jpg";
    //public static final String PICTURE_TMP_IMAGE_PATH = PATH_PICTURE
    //		+ PICTURE_TMP_IMAGE_NAME;

    /**
     * 检验SDcard状态
     *
     * @return boolean
     */
    public static boolean checkSDCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static void uploadFile(String filePath, String fileName, final WIFIVoidCallback callback) {
        //csACUtil.uploadFile(filePath, fileName, new VoidCallback() {
        //	@Override
        //	public void success() {
        //		if(callback!=null){
        //			callback.onSuccess();
        //		}
        //	}

        //	@Override
        //	public void error(ACException e) {
        //		if(callback!=null) {
        //			callback.onFailure(e.getMessage(),e.getErrorCode());
        //		}
        //	}
        //});
    }

    public static void downloadFile(String filePath, String fileName, final WIFIVoidCallback callback) {
        //csACUtil.downloadFile(filePath, fileName, new VoidCallback() {
        //    @Override
        //    public void success() {
        //        if (callback != null) {
        //            callback.onSuccess();
        //        }
        //    }

        //    @Override
        //    public void error(ACException e) {
        //        if (callback != null) {
        //            callback.onFailure(e.getMessage(), e.getErrorCode());
        //        }
        //    }
        //});
    }

    /**
     * 保存文件文件到目录
     *
     * @param context
     * @return 文件保存的目录
     */
    public static String setMkdir(Context context) {
        String filePath;
        if (checkSDCard()) {
            filePath = Environment.getExternalStorageDirectory()
                    + File.separator + "myfile";
        } else {
            filePath = context.getCacheDir().getAbsolutePath() + File.separator
                    + "myfile";
        }
        File file = new File(filePath);
        if (!file.exists()) {
            boolean b = file.mkdirs();
            LogUtil.e("file", "文件不存在  创建文件    " + b);
        } else {
            LogUtil.e("file", "文件存在");
        }
        return filePath;
    }

    /**
     * 得到文件的名称
     *
     * @return
     * @throws IOException
     */
    public static String getFileName(String url) {
        String name = null;
        try {
            name = url.substring(url.lastIndexOf("/") + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 得到文件的名称
     *
     * @return
     * @throws IOException
     */
    public static String getFileType(String url) {
        String name = null;
        try {
            name = url.substring(url.lastIndexOf(".") + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 创建目录
     *
     * @param dirName
     * @return
     */
    public static String createSDDir(String dirName) {
        if (checkSDCard()) {
            File dir = new File(Environment.getExternalStorageDirectory()
                                        + dirName);
            dir.mkdirs(); // mkdirs 连chipsea/download两个目录都创建
            // mkdir 只能创建一个~~
            return dir.getAbsolutePath();
        }
        return "";
    }

    /**
     * 判断文件或文件夹是否存在
     *
     * @param fileName
     * @return
     */
    public static boolean isFileExist(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory()
                                     .getAbsolutePath() + fileName);
        return file.exists();
    }

    /**
     * 关闭输入流>/br>
     *
     * @param input
     */
    public static void closeInputStream(InputStream input) {
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
                LogUtil.e("InputStream",
                          "InputStream IOException " + e.getMessage());
            }
        }
    }

    /**
     * 关闭输出流</br>
     *
     * @param output
     */
    public static void closeOutputStream(OutputStream output) {
        if (output != null) {
            try {
                output.close();
            } catch (IOException e) {
                LogUtil.e("OutputStream",
                          "OutputStream IOException " + e.getMessage());
            }
        }
    }

    /**
     * 删除目录下面的所有文件
     *
     * @param file
     */
    public static void deleteDirAllFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                // file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                deleteDirAllFile(childFiles[i]);
            }
            // file.delete();
        }
    }
}
