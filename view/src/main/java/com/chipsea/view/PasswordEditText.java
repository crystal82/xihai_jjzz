package com.chipsea.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

import com.chipsea.code.util.PasswordKeyListener;
import com.chipsea.view.edit.CustomEditText;

public class PasswordEditText extends FrameLayout {

    public static final int text     = 0;
    public static final int password = 1;
    public static final int number   = 2;

    private Drawable checkDrawable;
    private Drawable editDrawable;
    private int      drawablePadding;
    private String   hint;
    private int      textsize;
    private int      textColor;
    private int      typeface;
    private boolean isCheckable = false;
    private int     inputType   = text;
    private CustomEditText editText;
    private CheckBox       checkBox;
    private Context        context;
    private PasswordKeyListener passwordKeyListener = new PasswordKeyListener();

    public PasswordEditText(Context context) {
        super(context);
        init(context);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PasswordEditText);
        textsize = array.getInt(R.styleable.PasswordEditText_pEditTextSize, 35);
        typeface = array.getInt(R.styleable.PasswordEditText_pEditTypeface, CustomEditText.EX);
        inputType = array.getInt(R.styleable.PasswordEditText_pEditInputType, inputType);
        checkDrawable = array.getDrawable(R.styleable.PasswordEditText_pEditCheckDrawable);
        editDrawable = array.getDrawable(R.styleable.PasswordEditText_pEditDrawable);
        drawablePadding = array.getDimensionPixelSize(R.styleable.PasswordEditText_pEditDrawablePadding, 0);
        hint = array.getString(R.styleable.PasswordEditText_pEdittextHint);
        textColor = array.getColor(R.styleable.PasswordEditText_pEditTextColor, Color.BLACK);
        isCheckable = array.getBoolean(R.styleable.PasswordEditText_pCheckable, false);
        array.recycle();
        init(context);
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        editText = new CustomEditText(context);
        LayoutParams params =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
        editText.setLayoutParams(params);
        editText.setHintTextColor(context.getResources().getColor(R.color.gray_text));
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setTextSize(textsize);
        editText.setTypeface(typeface);
        editText.setGravity(Gravity.CENTER);
        editText.setTextColor(textColor);
        editText.setHint(hint);
        editText.setCursorDrawableColor(Color.BLACK);
        editText.setCompoundDrawablesWithIntrinsicBounds(editDrawable, null, null, null);
        editText.setCompoundDrawablePadding(drawablePadding);
        switch (inputType) {
            case text:
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case password:
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                editText.setKeyListener(passwordKeyListener);
                break;
            case number:
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
        }
        LayoutParams params1 =
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params1.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        checkBox = new CheckBox(context);
        checkBox.setLayoutParams(params1);
        checkBox.setButtonDrawable(checkDrawable);
        if (!isCheckable) {
            checkBox.setVisibility(View.GONE);
        } else {
            initCheck();
            editText.setPadding(editText.getPaddingLeft(), editText.getPaddingTop(),
                                editText.getPaddingRight() + checkDrawable.getBounds().width() + drawablePadding +
                                        getPaddingRight(),
                                editText.getPaddingBottom());
        }
        addView(editText);
        addView(checkBox);
    }

    private void initCheck() {
        checkBox.setChecked(true);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                editText.setKeyListener(passwordKeyListener);
                editText.setSelection(editText.getText().toString().length());
            }
        });
    }

    public void setGravity(int gravity) {
        editText.setGravity(gravity);
    }

    public Object getText() {
        return editText.getText();
    }

    public void setText(int textId) {
        editText.setText(textId);
    }

    public void setText(String text) {
        editText.setText(text);
    }

    public Drawable getCheckDrawable() {
        return checkDrawable;
    }

    public void setCheckDrawable(Drawable checkDrawable) {
        if (isCheckable) {
            this.checkDrawable = checkDrawable;
            checkBox.setButtonDrawable(checkDrawable);
        }
    }

    public Drawable getEditDrawable() {
        return editDrawable;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setEditDrawable(int editDrawableId) {
        editDrawable = context.getResources().getDrawable(editDrawableId);
        editText.setCompoundDrawablesRelativeWithIntrinsicBounds(editDrawable, null, null, null);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setEditDrawable(Drawable editDrawable) {
        this.editDrawable = editDrawable;
        editText.setCompoundDrawablesRelativeWithIntrinsicBounds(editDrawable, null, null, null);
    }

    public int getDrawablePadding() {
        return drawablePadding;
    }

    public void setDrawablePadding(int drawablePadding) {
        this.drawablePadding = drawablePadding;
        editText.setCompoundDrawablePadding(drawablePadding);
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
        editText.setHint(hint);
    }

    public int getTextsize() {
        return textsize;
    }

    public void setTextsize(int textsize) {
        this.textsize = textsize;
        editText.setTextSize(textsize);
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        editText.setTextColor(textColor);
    }

    public int getTypeface() {
        return typeface;
    }

    public void setTypeface(int typeface) {
        this.typeface = typeface;
        editText.setTypeface(typeface);
    }

    public boolean isCheckable() {
        return isCheckable;
    }

    public void setCheckable(boolean checkable) {
        isCheckable = checkable;
        if (isCheckable) {
            checkBox.setChecked(true);
            checkBox.setVisibility(View.VISIBLE);
        } else {
            checkBox.setChecked(false);
            checkBox.setVisibility(View.GONE);
        }
    }

    public int getInputType() {
        return inputType;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
        editText.setInputType(inputType);
    }
}
