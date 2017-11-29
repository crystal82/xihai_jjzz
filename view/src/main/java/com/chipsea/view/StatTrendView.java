
package com.chipsea.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.chipsea.mode.entity.StatEntity;

import java.util.List;

public class StatTrendView extends StatTrendBaseView {
    public static  float VALID_AREA = 10 ;
    private int selectIndex = -1 ;
    private List<StatEntity> statEntities;
    protected Paint selectPointText ;
    protected float selectPointtextSize;
    /**
     * textY轴坐标
     */
    protected float textYAxis[]  ;

    public float[] getValueYAxis() {
        return valueYAxis;
    }

    public void setValueYAxis(float[] valueYAxis) {
        this.valueYAxis = valueYAxis;
        minYValue = valueYAxis[0] ;
        maxYValue = valueYAxis[valueYAxis.length - 1] ;
    }

    private float[] valueYAxis;

    public void setStatEntities(List<StatEntity> statEntities) {
        this.statEntities = statEntities;
        if(statEntities.size()  > 0){
            selectIndex = statEntities.size() - 1 ;
        }
        isReset = true ;
    }

    public StatTrendView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init() ;
    }

    private void init(){
        VALID_AREA = VALID_AREA * mDensity ;
        setShowYin(true);
        selectPointtextSize = 20 * mDensity ;
        selectPointText = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectPointText.setTextSize(selectPointtextSize);
        selectPointText.setTextAlign(Paint.Align.CENTER);
        selectPointText.setColor(getResources().getColor(R.color.main_color));
    }
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        if(statEntities == null)
            return;
        super.onDraw(canvas);
    }
    protected  void drawYin() {
       int lenth = statEntities.size() ;
            if(lenth > 0){
                float[]  pointXAxis= new float[lenth] ;
                float[]  topYAxis= new float[lenth] ;
                for (int i = 0; i < statEntities.size(); i++) {
                    pointXAxis[i] = textXAxis[statEntities.get(i).getxPosition()] ;
                    topYAxis[i] = statEntities.get(i).getAxis() ;
                }
                int i = 0 ;
                Path mPath=new Path();
                mPath.moveTo(pointXAxis[0], mHeight - mSpace - XTextSize);
                while (true){
                    if(i >  pointXAxis.length-1){
                        break;
                    }
                    mPath.lineTo(pointXAxis[i], topYAxis[i]);
                    ++ i ;
                }
                -- i ;
                mPath.lineTo(pointXAxis[i], mHeight - mSpace - XTextSize);
                mPath.close();
                yinPaint.setAlpha(100);
                mCanvas.drawPath(mPath, yinPaint) ;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(statEntities == null || statEntities.size() == 0)return super.onTouchEvent(event) ;
        float downX = event.getX();
        float downY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < statEntities.size(); i++) {
                    StatEntity statEntity = statEntities.get(i) ;
                    int xPosition = statEntity.getxPosition() ;
                    float xAxis = textXAxis[xPosition] ;
                    float yAxis =statEntity.getAxis() ;
                    if(downX >= (xAxis-VALID_AREA) && downX<= (xAxis+VISIBLE) && downY>=(yAxis-VALID_AREA) && downY<=(yAxis + VALID_AREA)){
                        selectIndex = i ;
                        isReset = true;
                        break;
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                for (int i = 0; i < statEntities.size(); i++) {
                    StatEntity statEntity = statEntities.get(i) ;
                    int xPosition = statEntity.getxPosition() ;
                    float xAxis = textXAxis[xPosition] ;
                    float yAxis =statEntity.getAxis() ;
                    if(downX >= (xAxis-VALID_AREA) && downX<= (xAxis+VISIBLE) && downY>=(yAxis-VALID_AREA) && downY<=(yAxis + VALID_AREA)){
                        selectIndex = i ;
                        isReset = true;
                        break;
                    }
                }
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void drawLineAndPoint() {
        for (int i = 0; i < statEntities.size() ; i++) {
            StatEntity tempEntity1 = statEntities.get(i) ;
            if(i < statEntities.size() - 1){
                StatEntity tempEntity2 = statEntities.get(i + 1) ;
                // 画线
                pointPaint.setPathEffect(null);
                mCanvas.drawLine(textXAxis[tempEntity1.getxPosition()], tempEntity1.getAxis(), textXAxis[tempEntity2.getxPosition()], tempEntity2.getAxis(), linePaint);
            }
            // 画点
            if(i == selectIndex){
                mCanvas.drawCircle(textXAxis[tempEntity1.getxPosition()], tempEntity1.getAxis(), radius + 3, pointPaint);
                mCanvas.drawCircle(textXAxis[tempEntity1.getxPosition()],tempEntity1.getAxis(), interiorRadius - 1, interiorPaint);
                String selectText = tempEntity1.getValue() + "°"  ;
                Rect rect = new Rect();
                selectPointText.getTextBounds(selectText, 0, selectText.length(), rect);
                int width = rect.width() ;
                if(textXAxis[tempEntity1.getxPosition()] + width/2 >= mWidth){
                    mCanvas.drawText(selectText, mWidth - width/2 , tempEntity1.getAxis() - 3 * mSpace , selectPointText);   //画x轴
                }else {
                    mCanvas.drawText(selectText, textXAxis[tempEntity1.getxPosition()]  , tempEntity1.getAxis() - 3 * mSpace , selectPointText);   //画x轴
                }

            }else {
                mCanvas.drawCircle(textXAxis[tempEntity1.getxPosition()], tempEntity1.getAxis(), radius, pointPaint);
                mCanvas.drawCircle(textXAxis[tempEntity1.getxPosition()],tempEntity1.getAxis(), interiorRadius, interiorPaint);
            }
        }
    }
    @Override
    protected void computeYAxis() {
        textYAxis = new float[valueYAxis.length] ;
        float yifenY = (mHeight - XTextSize - 4 * mSpace) /(maxYValue - minYValue) ;
        for (int i = 0; i < valueYAxis.length; i++) {
            float value = yifenY * (valueYAxis[i] - minYValue) ;
            textYAxis[i] = mHeight - value  - XTextSize -  2 * mSpace;
        }
        for (int i = 0; i < statEntities.size(); i++) {
            StatEntity entity = statEntities.get(i) ;
            entity.setAxis(mHeight - yifenY * (entity.getValue() - minYValue)  - XTextSize -  2 * mSpace);
        }
    }

    @Override
    protected void drawText() {
            super.drawText();
            for (int i = 0; i < valueYAxis.length; i++) {//画y轴text
                mCanvas.drawText(valueYAxis[i] + "" , mSpace + YTextSize, textYAxis[i] + YTextSize/2 , YTextPaint);
        }
    }
}
