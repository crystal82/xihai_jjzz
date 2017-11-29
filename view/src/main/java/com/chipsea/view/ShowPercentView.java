package com.chipsea.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ShowPercentView extends View{
    private static final int xSesion = 90 ;
    private static int DEGREES = 360 / xSesion ;

    private Paint percentPaint;

    private Paint textPaint;
    private Paint smollTextPaint ;
    private int min = 30 ;

    private int percent = 45;
    private int percentLineColor;

    private int percentLineWidth = 4;
    private int lineHeight = 10;


    public ShowPercentView(Context context) {
        super(context);
        init(null, 0);
    }

    public ShowPercentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ShowPercentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // TODO Auto-generated method stub  
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ShowPercentView, defStyle, 0);
        percentLineColor = a.getColor(R.styleable.ShowPercentView_percentLineColor, Color.GREEN);
        a.recycle();
        float mDensity = getResources().getDisplayMetrics().density;
        percentLineWidth = (int) (1 * mDensity);
        lineHeight = (int) (10*mDensity);

        percentPaint = new Paint();
        percentPaint.setAntiAlias(true);


        float textSize = 60 * mDensity ;

        textPaint = new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);

        smollTextPaint = new Paint();
        smollTextPaint.setTextSize(textSize/3);
        smollTextPaint.setAntiAlias(true);
        smollTextPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub  
        super.onDraw(canvas);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int pointX =  width/2;
        int pointY = height/2;

        float textWidth = textPaint.measureText(min + "");
        canvas.drawText(min + "",pointX - textWidth/2,pointY + textPaint.getTextSize()/2 ,textPaint);

        float smollTextWidth = smollTextPaint.measureText("分钟");
        canvas.drawText("分钟", pointX - smollTextWidth/2,pointY  + textWidth/getTextMaginHightSesion() + smollTextWidth/2 ,smollTextPaint);

        percentPaint.setColor(Color.BLACK);
        percentPaint.setStrokeWidth(percentLineWidth);

        canvas.save();
        canvas.translate(0,pointY);
        canvas.rotate(90, pointX, 0);
        for(int i = 0;i<xSesion;i++){
            canvas.drawLine(0, 0, lineHeight, 0, percentPaint);
            canvas.rotate(DEGREES, pointX, 0);
        }
        canvas.restore();

        percentPaint.setStrokeWidth(percentLineWidth);
        percentPaint.setColor(percentLineColor);

        canvas.save();
        canvas.translate(0,pointY);
        canvas.rotate(90, pointX, 0);
        for(int i = 0;i<percent;i++){
            canvas.drawLine(0, 0, lineHeight, 0, percentPaint);
            canvas.rotate(DEGREES, pointX, 0);
        }
        canvas.restore();
    }
    public int getTextMaginHightSesion(){
        int result =  1 ;
        if(min<10){
            result = 1 ;
        }else if(min >=10 && min<100){
            result = 2 ;
        }else if(min>=100 && min<1000){
            result = 3 ;
        }else {
            result = 4 ;
        }
        return result ;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub  
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);   
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int d = (width >= height) ? height : width;
        setMeasuredDimension(d,d);
    }

    public void setMin(int min) {
        // TODO Auto-generated method stub  
        this.min = min;
        this.percent =  min >= 60 ? xSesion :(int) (min % 60 * 1.5);
        postInvalidate();
    }
}  