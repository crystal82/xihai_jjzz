package com.chipsea.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.chipsea.code.util.ViewUtil;

/**
 * 描述：环形的ProgressBar
 */
public class AriStateView extends View {
    private static final int xSesion = 45 ;
    private static int DEGREES = 180 / xSesion ;
    private int lineWidth = 3 ,lineHeight = 8 ,lineGap = 3;
    private Paint linePaint ;
    private Paint textPaint ;
    private int textSize = 8 ;
    private int currdDegree;
    //绘制轨迹
    private Paint pathPaint = null;
    //绘制填充
    private Paint fillArcPaint = null;
    private RectF oval;
    //环的路径宽度
    private int pathWidth = 8;
    private int width;
    private int height;
    //默认圆的半径
    private int radius ;

    // 指定了光源的方向和环境光强度来添加浮雕效果
    /**
     * The emboss.
     */
    private EmbossMaskFilter emboss = null;
    // 设置光源的方向
    /**
     * The direction.
     */
    float[] direction = new float[]{1, 1, 1};
    //设置环境光亮度
    float light = 0.4f;
    float specular = 6;
    // 向 mask应用一定级别的模糊

    float blur = 3.5f;
    //指定了一个模糊的样式和半径来处理 Paint 的边缘
    private BlurMaskFilter mBlur = null;
    //view重绘的标记
    private boolean reset = false;

    /**
     * Instantiates a new ab circle progress bar.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public AriStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        pathWidth = ViewUtil.dip2px(context,pathWidth);
        lineWidth = ViewUtil.dip2px(context,lineWidth);
        lineHeight = ViewUtil.dip2px(context,lineHeight);
        lineGap = ViewUtil.dip2px(context,lineGap);
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.parseColor("#F5F5F5"));
        linePaint.setStrokeWidth(lineWidth);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(ViewUtil.dip2px(context,textSize));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.parseColor("#cccccc"));

        pathPaint = new Paint();
        // 设置是否抗锯齿
        pathPaint.setAntiAlias(true);
        // 帮助消除锯齿
        pathPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        // 设置中空的样式
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setDither(true);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);
        pathPaint.setColor(Color.parseColor("#F5F5F5"));

        fillArcPaint = new Paint();
        // 设置是否抗锯齿
        fillArcPaint.setAntiAlias(true);
        // 帮助消除锯齿
        fillArcPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        // 设置中空的样式
        fillArcPaint.setStyle(Paint.Style.STROKE);
        fillArcPaint.setDither(true);
        fillArcPaint.setStrokeJoin(Paint.Join.ROUND);
        fillArcPaint.setStrokeCap(Paint.Cap.ROUND);
        fillArcPaint.setStrokeWidth(pathWidth);

        oval = new RectF();
        emboss = new EmbossMaskFilter(direction, light, specular, blur);
        mBlur = new BlurMaskFilter(20, BlurMaskFilter.Blur.NORMAL);
    }
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (reset) {
            canvas.drawColor(Color.TRANSPARENT);
            reset = false;
        }
        this.width = getMeasuredWidth();
        this.height = getMeasuredHeight();
        int pointX =  width/2;
        int pointY = height/2;
        this.radius =( getMeasuredWidth() - pathWidth - 2  * lineHeight - 2 * lineGap) / 2;
        // 设置画笔宽度
        pathPaint.setStrokeWidth(pathWidth);
        //添加浮雕效果
        pathPaint.setMaskFilter(emboss);
        // 在中心的地方画个半径为r的圆
        canvas.drawCircle(this.width / 2, this.height / 2, radius, pathPaint);
        //边线
        pathPaint.setStrokeWidth(1f);
        canvas.drawCircle(this.width / 2, this.height / 2, radius + pathWidth / 2 , pathPaint);
        canvas.drawCircle(this.width / 2, this.height / 2, radius - pathWidth / 2 , pathPaint);
        //设置线的类型,边是圆的

        // 设置类似于左上角坐标，右下角坐标
        oval.set(this.width / 2 - radius, this.height / 2 - radius, this.width / 2 + radius, this.height / 2 + radius);
        // 画圆弧，第二个参数为：起始角度，第三个为跨的角度，第四个为true的时候是实心，false的时候为空心
        canvas.drawArc(oval, 180, currdDegree, false, fillArcPaint);
        canvas.save();
        canvas.translate(0,pointY);
        canvas.rotate(0, pointX, 0);
        for(int i = 0;i<xSesion + 1;i++){
            canvas.drawLine(0, 0, lineHeight, 0, linePaint);
            canvas.rotate(DEGREES, pointX, 0);
        }
        canvas.restore();
        drawAriText(canvas) ;
    }

    private void drawAriText(Canvas canvas) {
        String text1 = "16°C" ;
        String text2 = "23°C" ;
        String text3 = "30°C" ;
        Rect rect = new Rect();
        textPaint.getTextBounds(text1, 0, text1.length(), rect);
        int w = rect.width();
        int h = rect.height();
        drawAngleText(canvas,text1,lineHeight + lineGap + pathWidth + w /2 ,height/2 ,-90);
        drawAngleText(canvas,text2, width/2, lineHeight +  2 * lineGap + pathWidth + h ,0);
        drawAngleText(canvas,text3,width - (lineHeight +  lineGap + pathWidth + w/4),height/2 ,-90);
    }

    void drawAngleText(Canvas canvas , String text , float x , float y, float angle){
        if(angle != 0){
            canvas.rotate(angle, x, y);
        }
        canvas.drawText(text, x, y, textPaint);
        if(angle != 0){
            canvas.rotate(-angle, x, y);
        }
    }
    /**
     * Sets the progress.
     *
     * @param temp 当前度数
     */
    public void seTemp(int temp) {
        this.currdDegree = (temp - 16) * 180 / 14 ;
        this.invalidate();
    }
    public void setPaintColor(int arcColor){
        fillArcPaint.setColor(getResources().getColor(arcColor));
        this.invalidate();
    }

    /* (non-Javadoc)
     * @see android.view.View#onMeasure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int d = (width >= height) ? height : width;
        setMeasuredDimension(d,d);
    }

    /**
     * 描述：重置进度.
     */
    public void reset() {
        reset = true;
        this.currdDegree = 0;
        this.invalidate();
    }

}
