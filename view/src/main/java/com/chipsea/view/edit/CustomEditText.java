package com.chipsea.view.edit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chipsea.view.R;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 * Created by hfei on 2015/10/8.
 */
public class CustomEditText extends EditText {

    /**
     * 四种字体风格
     */
    public static final int EX = 0;
    public static final int HVCN = 1;
    public static final int LTEX = 2;
    public static final int LT = 3;

    private Context mContext;

    public CustomEditText(Context context) {
        super(context);
        mContext = context;
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        int textsize = array.getInt(R.styleable.CustomTextView_customTextSize,35);
        setTextSize(textsize);
        int typeface = array.getInt(R.styleable.CustomTextView_customTypeface,LTEX);
        setTypeface(typeface);
        array.recycle();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void setTextSize(int size) {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getFontSize(mContext, size));
    }

    public void setTypeface(int typeface) {
        switch (typeface) {
            case CustomEditText.EX:
                setTypeface(getExTypeface());
                break;
            case CustomEditText.HVCN:
                setTypeface(getHvCnTypeface());
                break;
            case CustomEditText.LT:
                setTypeface(getLtTypeface());
                break;
            case CustomEditText.LTEX:
                setTypeface(getLtExTypeface());
                break;
            default:
                break;
        }
    }

    /**
     * EX字体风格
     * @return
     */
    public Typeface getExTypeface() {
        if (!isChinese()) {
            return Typeface
                    .createFromAsset(mContext.getAssets(), "fonts/ex.otf");
        }
        return Typeface.SANS_SERIF;
    }

    /**
     * HVCN字体风格
     * @return
     */
    public Typeface getHvCnTypeface() {
        if (!isChinese()) {
            return Typeface.createFromAsset(mContext.getAssets(),
                                            "fonts/hvcn.otf");
        }
        return Typeface.SANS_SERIF;
    }

    /**
     * LTEX字体风格
     * @return
     */
    public Typeface getLtExTypeface() {
        if (!isChinese()) {
            return Typeface.createFromAsset(mContext.getAssets(),
                                            "fonts/ltex.otf");
        }
        return Typeface.SANS_SERIF;
    }

    /**
     * LT字体风格
     * @return
     */
    public Typeface getLtTypeface() {
        if (!isChinese()) {
            return Typeface
                    .createFromAsset(mContext.getAssets(), "fonts/lt.otf");
        }
        return Typeface.SANS_SERIF;
    }

    /**
     * 检查语言环境
     *
     * @return 是否为中文环境
     */
    private boolean isChinese() {
        String localLan = mContext.getResources().getConfiguration().locale
                .getDisplayLanguage();
        return localLan.equals(Locale.CHINESE.getDisplayLanguage());
    }

    /**
     * 获取字体大小
     *
     * @param context
     * @param textSize 被计算字体大小值
     * @return 计算后的字体大小值
     */
    private int getFontSize(Context context, int textSize) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        // screenWidth = screenWidth > screenHeight ?screenWidth :screenHeight;
        int rate = (int) (textSize * (float) screenHeight / 1280);
        return rate;
    }
    /**
     * 代码设置光标颜色
     * @param color    光标颜色
     */
    public void setCursorDrawableColor(int color) {
        try {
            Field fCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");//获取这个字段
            fCursorDrawableRes.setAccessible(true);//代表这个字段、方法等等可以被访问
            int mCursorDrawableRes = fCursorDrawableRes.getInt(this);

            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(this);

            Class<?> clazz = editor.getClass();
            Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);

            Drawable[] drawables = new Drawable[2];
            drawables[0] = this.getContext().getResources().getDrawable(mCursorDrawableRes);
            drawables[1] = this.getContext().getResources().getDrawable(mCursorDrawableRes);
            drawables[0].setColorFilter(color, PorterDuff.Mode.SRC_IN);//SRC_IN 上下层都显示。下层居上显示。
            drawables[1].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            fCursorDrawable.set(editor, drawables);
        } catch (Throwable ignored) {
        }
    }
}
