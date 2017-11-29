package com.chipsea.ui.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;

import com.chipsea.code.util.FileUtil;
import com.chipsea.code.util.ImagePresser;
import com.chipsea.code.util.JLog;
import com.chipsea.ui.dialog.HeadPhotoDialog;
import com.chipsea.view.dialog.LoadDialog;

import java.io.File;

/**
 * 图片获取类
 * Created by hfei on 2016/5/23.
 */
public class PhotographActivity extends CommonActivity{

    /**
     * 图片获取方式标志
     */
    public static final String FLAG_PICTURE_INTENT = "picture";
    public static final String FLAG_PHOTO_NAME = "photo";
    public static final String FLAG_ABLUM_NAME = "ablum";
    public static final String FLAG_HEADPIC_NAME="headpicname";

    private static final int FLAG_ABLUM = 0;
    private static final int FLAG_PHOTO = 1;
    private static final int FLAG_RESULT = 2;
    private static final String TAG = "PhotographActivity";
    private String mPicName;
    private HeadPhotoDialog mHeadPhotoDialog;
    private LoadDialog dialog ;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mHeadPhotoDialog != null)
            mHeadPhotoDialog.dismiss();
    }

    protected void setHeadPicName(String fName){
        mPicName=fName;
    }

    /**
     * 弹出图片选择框
     */
    protected void onHeadPhotoDialog() {
        if (mHeadPhotoDialog == null) {
            mHeadPhotoDialog = new HeadPhotoDialog(this);
        }
        mHeadPhotoDialog.showDialog();
        mHeadPhotoDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHeadPhotoDialog.getMode() == 0) {
                    Uri imageUri=Uri.fromFile(new File(FileUtil.PATH_PICTURE, mPicName));
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, FLAG_PHOTO);
                } else if (mHeadPhotoDialog.getMode() == 1) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                    intent.setType("image/*");
                    startActivityForResult(intent, FLAG_ABLUM);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bitmap bitmap = null;
            final Bitmap[] comBitmap = {null};
            switch (requestCode) {
                /**
                 * ==========1. 拍照片模式============
                 */
                case FLAG_PHOTO:
                    // 将保存在本地的图片取出并缩小后显示在界面上
                    if (mPicName != null) {
                        Intent intent = new Intent(this,
                                CropImageActivity.class);
                        intent.putExtra(FLAG_PICTURE_INTENT, FLAG_PHOTO_NAME);
                        intent.putExtra(FLAG_HEADPIC_NAME,mPicName);
                        startActivityForResult(intent, FLAG_RESULT);
                    }
                    break;
                /**
                 * ==========2. 从图库中取出模式============
                 */
                case FLAG_ABLUM:
                    if (mPicName != null) {
                        int degree = ImagePresser
                                .getOrientation(this,data.getData());
                        JLog.e(TAG,"degree = " + degree);

                        bitmap = getBitmapFromUri(data.getData(),
                                getContentResolver());
                        if (degree == 90 || degree == 270) {
                            bitmap = ImagePresser.rotaingImageView(degree,
                                    bitmap);
                        }
                        if (bitmap != null) {
                            if(dialog==null){
                                dialog =LoadDialog.getShowDialog(this) ;
                            }
                            dialog.show();
                            final Bitmap finalBitmap = bitmap;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    comBitmap[0] = ImagePresser.comp(finalBitmap);
                                    // 因为有些手机不能返回剪切后的图片，所以先存储再剪裁存储路径图片
                                    ImagePresser.savePhotoToSDCard(comBitmap[0],
                                            FileUtil.PATH_PICTURE,mPicName);
                                    if ((finalBitmap != null) && (!finalBitmap.isRecycled())) {
                                        finalBitmap.recycle();
                                    }
                                    handler.sendEmptyMessage(1);
                                }
                            }).start();
                        }
                    }
                    break;
                /**
                 * 保存图片
                 */
                case FLAG_RESULT:
                    if (mPicName != null) {
                        onPhotographResult(FileUtil.PATH_PICTURE + mPicName);
                    }
                    break;
                default:
                    break;
            }
        }
    }

     Handler handler = new Handler(){
         @Override
         public void handleMessage(Message msg) {
             super.handleMessage(msg);
             dialog.dismiss();
             Intent intent = new Intent(PhotographActivity.this, CropImageActivity.class);
             intent.putExtra(FLAG_PICTURE_INTENT, FLAG_ABLUM_NAME);
             intent.putExtra(FLAG_HEADPIC_NAME,mPicName);
             startActivityForResult(intent, FLAG_RESULT);
         }
     } ;
    /**
     * 最终图片回调
     * @param path 图片存放路径
     */
    protected void onPhotographResult(String path) {}

    /**
     * 通过uri获取图片
     *
     * @param uri
     * @param contentResolver
     * @return
     */
    public static Bitmap getBitmapFromUri(Uri uri, ContentResolver contentResolver) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(contentResolver,
                    uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
