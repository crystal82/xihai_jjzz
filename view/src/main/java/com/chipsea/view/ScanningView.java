package com.chipsea.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class ScanningView extends SurfaceView {
   //View的大小
    private float mWidth;
    private float mHeight;

    private float centerX; // 圆心X
    private float centerY; // 圆心Y
    private float radius;//
    private float maxRadius;
    private float waveSpeed = 4.0f;
    private int waveNum = 3;
    private List<Float> waveList;

    private Paint mPaintLine;
    private Paint mPaint;
    private boolean isStart = false;
    private ScanThread mThread;
    private int start = 0;
    private Paint mPaintC;
    private Paint mPaintCC;

    public ScanningView(Context context) {
        super(context);
    }

    public ScanningView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScanningView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 初始化
     */
    private void init() {
        mWidth = getWidth();
        mHeight = getHeight();
        centerX = mWidth / 2.0F;
        centerY = mHeight / 2.0F;

        radius = centerX>centerY?centerY/2:centerX/2;
        maxRadius = radius*2;

        initPaint();
        initWave();
        mThread = new ScanThread();

        setBackgroundColor(getResources().getColor(R.color.main_color));
    }

    private void initWave(){
    	if(waveNum <=0)return;

    	float wavePading = (maxRadius - radius)/waveNum;
    	waveList = new ArrayList<Float>();
    	for(int i = 0; i<waveNum; i++){
    		waveList.add(radius+i*wavePading);
    	}
    }

    private void initPaint() {

        mPaintCC = new Paint();
        mPaintCC.setAntiAlias(true);
        mPaintCC.setStyle(Style.FILL);
        mPaintCC.setColor(getResources().getColor(R.color.main_color_press));
        mPaintCC.setStrokeCap(Paint.Cap.ROUND);

        mPaintC = new Paint();
        mPaintC.setAntiAlias(true);
        mPaintC.setColor(0xffffffff);
        mPaintC.setStyle(Paint.Style.FILL);

        mPaint = new Paint();
        mPaint.setColor(0x9D00ff00);
        mPaint.setAntiAlias(true);

        mPaintLine = new Paint();
        mPaintLine.setAntiAlias(true);
        mPaintLine.setStrokeWidth(3.0F);
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setColor(Color.LTGRAY);

        isReverse1=false;

    }

    public void start() {
        if (!isStart) {
            isStart = true;
            new Thread(new ScanThread()).start();
        }
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public void threadNotif(){
        Thread.State state = mThread.getState() ;
        mThread.notify();
    }
    public void stop() {
        isStart = false;
        Thread.interrupted() ;
    }
   //初始化角度渐变  从透明-蓝色
    private Shader mShader ;
    private Matrix matrix = new Matrix();

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //canvas.save();

        //波纹扩散
        for(int i = 0; i<waveList.size(); i++){
        	float waveRadius = waveList.get(i);
        	if(waveRadius>maxRadius){
        		waveRadius = radius;
        	}

        	int alpha = (int) (255.0F * (1.0F - (waveRadius - radius) / (maxRadius - radius)));
        	mPaintLine.setAlpha(alpha);
        	canvas.drawCircle(centerX, centerY, waveRadius, mPaintLine);
        	waveRadius +=waveSpeed;
        	waveList.set(i, waveRadius);
        }

        //画外圆
        canvas.drawCircle(centerX, centerY, radius+3, mPaintC);
        canvas.drawCircle(centerX, centerY, radius, mPaintCC);
        //画内圆
        canvas.drawCircle(centerX, centerY, radius * 0.8f, mPaintC);

        mShader =  new SweepGradient(centerX, centerY, Color.TRANSPARENT, Color.parseColor("#46A0A0"));
        mPaint.setShader(mShader);
        //设置矩阵
        canvas.concat(matrix);

        //扫描图
        canvas.drawArc(new RectF(centerX-radius, centerY-radius, centerX+radius, centerY+radius), 0, 360, true, mPaint);



        canvas.restore();
        //canvas.save();
        canvas.scale(0.5f+change1/360,0.5f+change1/360);

//        if(isFisrt){
//            isFisrt=false;
//            start();
//
//        }
    }
    private boolean isFisrt=true;
    //不断改变的值  用于透明度ֵ
    private int change1;
    private int change2;

    //是否开始反方向变化   从透明度255-0
    private boolean isReverse1=false;
    private boolean isReverse2=false;

    protected class ScanThread extends Thread {
        @Override
        public void run() {
            while (isStart) {
                  //change1 小于0或者=0 即 说，说明是反方向执行过程中  所以设置为0和重置标志 true
                if(change1<0){
                    isReverse1=false;
                    change1=0;
                }

                if(change2<0){
                    isReverse2=false;
                    change2=0;
                }

                //正方向下的 增值
                if(!isReverse2)change2+=2;
                if(!isReverse1)change1+=4;

                //正方向
                 if(change1>=255){
                     isReverse1=true;
                     change1=255;
                 }
                if(change2>=255){
                    isReverse2=true;
                    change2=255;
                }
                //
                if(isReverse1)change1-=8;
                if(isReverse2)change2-=4;

                if(start>=360) start=0;
                start+=waveSpeed;

                matrix.reset();
                matrix.postRotate(start, centerX, centerY);

                postInvalidate();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setThrend(ScanThread thrend){
        mThread = thrend ;
        mThread.start();
        setStart(true);
    }
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            init();
        } else {
            stop();
        }
    }

}