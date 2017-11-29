package com.chipsea.ui.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;

import com.chipsea.code.util.FileUtil;
import com.chipsea.code.util.ImagePresser;
import com.chipsea.code.util.JLog;
import com.chipsea.code.util.ScreenUtils;
import com.chipsea.ui.R;
import com.chipsea.view.CropImageView;
import com.chipsea.view.text.CustomTextView;

import java.io.File;

public class CropImageActivity extends Activity implements OnClickListener {

	private static final String TAG = "CropImageActivity" ;
	private ViewHolders mViewHolders;
	private String mHeadPicName;
	private Uri imageUri; // 图片URI

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crop_image);
		init();
	}

	private void init() {
		mViewHolders = new ViewHolders();
		mViewHolders.cropImageView = (CropImageView) findViewById(R.id.cropimage);
		mViewHolders.sure = (CustomTextView) findViewById(R.id.sure);
		mViewHolders.cancle = (CustomTextView) findViewById(R.id.cancle);
		mViewHolders.sure.setOnClickListener(this);
		mViewHolders.cancle.setOnClickListener(this);

		String flag = getIntent().getStringExtra(PhotographActivity.FLAG_PICTURE_INTENT);
		mHeadPicName=getIntent().getStringExtra(PhotographActivity.FLAG_HEADPIC_NAME);
		imageUri = Uri.fromFile(new File(FileUtil.PATH_PICTURE, mHeadPicName));

		Bitmap scaleBitmap = null;
		if (flag != null) {
			if (flag.equals(PhotographActivity.FLAG_PHOTO_NAME)) {
				Bitmap bitmap = getBitmapFromUri(imageUri,
						getContentResolver());
				int degree = ImagePresser
						.readPictureDegree(imageUri.getPath());
				JLog.e(TAG,"degree = " + degree);
				if (degree == 90 || degree == 270) {
					scaleBitmap = ImagePresser.zoomBitmap(bitmap,
							ScreenUtils.getScreenHeight(this),
							ScreenUtils.getScreenWidth(this));
					scaleBitmap = ImagePresser.rotaingImageView(degree,
							scaleBitmap);
				} else {
					scaleBitmap = ImagePresser.zoomBitmap(bitmap,
							ScreenUtils.getScreenWidth(this),
							ScreenUtils.getScreenHeight(this));
				}
				bitmap.recycle();
			} else if (flag.equals(PhotographActivity.FLAG_ABLUM_NAME)) {
				scaleBitmap = BitmapFactory
						.decodeFile(imageUri.getPath());

			}
		}
		Drawable drawable = new BitmapDrawable(getResources(), scaleBitmap);
		mViewHolders.cropImageView.setDrawable(drawable, 100, 100);
	}

	/**
	 * 通过uri获取图片
	 *
	 * @param uri
	 * @param contentResolver
	 * @return
	 */
	public static Bitmap getBitmapFromUri(Uri uri,
										  ContentResolver contentResolver) {
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

	/**
	 * 控件控制变量
	 * */
	private class ViewHolders {
		CropImageView cropImageView;
		CustomTextView cancle;
		CustomTextView sure;
	}

	@Override
	public void onClick(View v) {
		if (v == mViewHolders.sure) {
			Bitmap bitmap = mViewHolders.cropImageView.getCropImage();
			if (bitmap != null) {
				Bitmap comBitmap = ImagePresser.comp(bitmap);
				ImagePresser.savePhotoToSDCard(comBitmap, FileUtil.PATH_PICTURE,mHeadPicName);
				bitmap.recycle();
				setResult(RESULT_OK);
			}
		}
		finish();
	}
}
