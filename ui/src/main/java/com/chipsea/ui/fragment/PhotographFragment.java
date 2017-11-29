package com.chipsea.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import com.chipsea.code.util.LogUtil;
import com.chipsea.code.util.PrefsUtil;
import com.chipsea.ui.R;
import com.chipsea.ui.dialog.HeadPhotoDialog;
import com.chipsea.ui.photoUtils.CropImageUtils;
import com.chipsea.ui.photoUtils.ImageLoadingUtils;
import com.chipsea.view.dialog.LoadDialog;

import java.io.File;

/**
 * 图片获取类
 * Created by hfei on 2016/5/23.
 */
public class PhotographFragment extends LazyFragment {

    /**
     * 图片获取方式标志
     */
    public static final String FLAG_PICTURE_INTENT = "picture";
    public static final String FLAG_PHOTO_NAME     = "photo";
    public static final String FLAG_ABLUM_NAME     = "ablum";
    public static final String FLAG_HEADPIC_NAME   = "headpicname";

    private static final int    FLAG_ABLUM  = 0;
    private static final int    FLAG_PHOTO  = 1;
    private static final int    FLAG_RESULT = 2;
    private static final String TAG         = "PhotographActivity";
    private String          mPicName;
    private HeadPhotoDialog mHeadPhotoDialog;
    private LoadDialog      dialog;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mHeadPhotoDialog != null)
            mHeadPhotoDialog.dismiss();
    }

    protected void setHeadPicName(String fName) {
        mPicName = fName;
    }

    /**
     * 弹出图片选择框
     */
    protected void onHeadPhotoDialog() {
        if (mHeadPhotoDialog == null) {
            mHeadPhotoDialog = new HeadPhotoDialog(getActivity());
        }
        mHeadPhotoDialog.showDialog();
        mHeadPhotoDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHeadPhotoDialog.getMode() == 0) {
                    //Uri imageUri=Uri.fromFile(new File(FileUtil.PATH_PICTURE, mPicName));
                    //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    //startActivityForResult(intent, FLAG_PHOTO);
                    CropImageUtils.takeFromCamera(PhotographFragment.this, CropImageUtils.IMAGE_CODE, CropImageUtils
                            .fileName);
                    //getImageFromCamera();

                } else if (mHeadPhotoDialog.getMode() == 1) {
                    //Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                    //intent.setType("image/*");
                    //startActivityForResult(intent, FLAG_ABLUM);
                    CropImageUtils.takeFromGallery(PhotographFragment.this, CropImageUtils.IMAGE_CODE);
                    //getImageFromGallery();
                }
            }
        });
    }

    private final int REQUEST_CODE_CAPTURE_CAMEIA = 1;
    private final int REQUEST_CODE_PICK_IMAGE     = 2;
    private final int REQUEST_CODE_CROP           = 3;
    private String cropPath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.d(resultCode + " ---PhotographFragment---:" + requestCode);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        switch (requestCode) {
            // 拍照和本地选择获取图片
            case CropImageUtils.IMAGE_CODE:
                LogUtil.d("---PhotographFragment---IMAGE_CODE");
                CropImageUtils.cropPicture(data, this);
                break;
            // 裁剪图片后结果
            case CropImageUtils.REQUEST_CropPictureActivity:
                LogUtil.d("---PhotographFragment---:REQUEST_CropPictureActivity");
                String path = data.getStringExtra("cropImagePath");
                PrefsUtil.setUserImgPath(path);
                initImgUI();
                break;
            case REQUEST_CODE_PICK_IMAGE:
                LogUtil.d("---PhotographFragment---:REQUEST_CODE_PICK_IMAGE:" + data.getData());
                if (data != null) {
                    Uri uri = data.getData();
                    crop(uri);
                }
                break;
            case REQUEST_CODE_CROP:
                LogUtil.d("----PhotographFragment----REQUEST_CODE_CROP");
                PrefsUtil.setUserImgPath(cropPath);
                initImgUI();
                break;
            case REQUEST_CODE_CAPTURE_CAMEIA:
                LogUtil.d("----PhotographFragment----REQUEST_CODE_CAPTURE_CAMEIA");
                crop(Uri.fromFile(new File(capturePath)));
                PrefsUtil.setUserImgPath(capturePath);
                //SPWristbandConfigInfo.setAvatarPath(this, capturePath);
                initImgUI();
                break;
            default:
                break;
        }
    }

    public void initImgUI() {

    }

    private String capturePath;
    private final String SAVED_IMAGE_DIR_PATH = Environment.getExternalStorageDirectory() + "/telilace/";

    public void getImageFromGallery() {
        final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");// select image file
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // file browser has been found on the device
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        } else {
            // no file browser, please download one.
            showToast(R.string.settings_personage_make_sure_sdcard);
        }
    }

    public void getImageFromCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            String out_file_path    = SAVED_IMAGE_DIR_PATH;
            File   dir              = new File(out_file_path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            capturePath = SAVED_IMAGE_DIR_PATH + "header.jpg";
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(capturePath)));
            getImageByCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(getImageByCamera, REQUEST_CODE_CAPTURE_CAMEIA);
        } else {
            showToast(R.string.settings_personage_make_sure_sdcard);
        }
    }

    private void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);// we only need the uri

        //cropPath = SAVED_IMAGE_DIR_PATH + System.currentTimeMillis() + ".jpg";
        cropPath = ImageLoadingUtils.getUniqueImagePath(String.valueOf(System.currentTimeMillis()));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(cropPath)));

        startActivityForResult(intent, REQUEST_CODE_CROP);
    }
}
