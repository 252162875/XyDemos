package kx.rnd.com.permissionstest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * TODO: document your custom view class.
 */
public class CustomViewDayOne extends View {
    private float mCum = 0;
    private float mFloatCum = 0.0f;

    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private float mStartAngle = 0;
    private ArrayList<Integer> mColors;
    private ArrayList<Integer> mdata;

    public CustomViewDayOne(Context context) {
        super(context);
        init(null, 0);
    }

    public CustomViewDayOne(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CustomViewDayOne(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        mPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthsize = MeasureSpec.getSize(widthMeasureSpec);      //取出宽度的确切数值
        int widthmode = MeasureSpec.getMode(widthMeasureSpec);      //取出宽度的测量模式

        int heightsize = MeasureSpec.getSize(heightMeasureSpec);    //取出高度的确切数值
        int heightmode = MeasureSpec.getMode(heightMeasureSpec);    //取出高度的测量模式
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);//注意：如果对View的宽高进行修改了，不要调用 super.onMeasure( widthMeasureSpec, heightMeasureSpec); 要调用 setMeasuredDimension( widthsize, heightsize); 这个函数。
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        //设置画笔属性
        mPaint.setColor(Color.BLACK);       //设置画笔颜色
        mPaint.setStyle(Paint.Style.FILL);  //设置画笔模式为填充
        mPaint.setStrokeWidth(10f);         //设置画笔宽度为10px,试验发现这个宽度是向外扩散的
        //设置背景色
        canvas.drawARGB(255, 139, 197, 186);
        //画点
        canvas.drawPoint(20, 20, mPaint);     //在坐标(200,200)位置绘制一个点
        float[] floats = {50, 50, 60, 60, 70, 70};
        canvas.drawPoints(floats, mPaint); //绘制一组点，坐标位置由float数组指定
        //画线
        canvas.drawLine(80, 80, 160, 80, mPaint);
        float[] lineFloats = {80, 100, 160, 100, 80, 120, 160, 120};
        canvas.drawLines(lineFloats, mPaint);
        //矩形
        mPaint.setStyle(Paint.Style.STROKE);//设置画笔模式为非填充
        canvas.drawRect(180, 20, 220, 60, mPaint);
        mPaint.setStyle(Paint.Style.FILL);  //设置画笔模式为填充
        Rect rect = new Rect(240, 20, 280, 60);
        canvas.drawRect(rect, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);//设置画笔模式为非填充
        RectF rectF = new RectF(300.5f, 20.5f, 340.5f, 60.5f);//RectF取的是float值精度比Rect高
        canvas.drawRect(rectF, mPaint);
        //圆角矩形
        RectF rectR = new RectF(360.5f, 20.5f, 420.5f, 60.5f);
        canvas.drawRoundRect(rectR, 5, 5, mPaint);//这里的两个参数实际上是椭圆的两个半径
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(440.5f, 20.5f, 500.5f, 60.5f, 10, 10, mPaint);//在API21的时候才添加上，所以我们一般使用的都是第一种
        }
        //
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLUE);
        RectF rectR1 = new RectF(520.5f, 20.5f, 620.5f, 80.5f);
        canvas.drawRect(rectR1, mPaint);//先画个矩形
        mPaint.setColor(Color.GREEN);//改变画笔颜色再画圆角矩形
        canvas.drawRoundRect(rectR1, 60, 40, mPaint);//实际上在rx为宽度的一半，ry为高度的一半时，刚好是一个椭圆，通过上面我们分析的原理推算一下就能得到，而当rx大于宽度的一半，ry大于高度的一半时，实际上是无法计算出圆弧的，所以drawRoundRect对大于该数值的参数进行了限制(修正)，凡是大于一半的参数均按照一半来处理
        //画椭圆
        mPaint.setColor(Color.BLUE);//改变画笔颜色
        RectF rectF1 = new RectF(640, 20, 700, 60);
        canvas.drawOval(rectF1, mPaint);
        mPaint.setColor(Color.RED);//改变画笔颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawOval(720, 20, 780, 60, mPaint);//在API21的时候才添加上，所以我们一般使用的都是第一种
        }
        //画圆
        canvas.drawCircle(110, 280, 80, mPaint);// 绘制一个圆心坐标在(110,280)，半径为80 的圆。
        //画圆弧
        RectF rectF2 = new RectF(200, 100, 300, 180);//长方形
        canvas.drawArc(rectF2, 0.0f, 135.0f, false, mPaint);//不包含中心
        RectF rectF3 = new RectF(300, 100, 400, 180);
        canvas.drawArc(rectF3, 45.0f, 90.0f, true, mPaint);//包含中心

        RectF rectF4 = new RectF(400, 100, 500, 200);//正方形
        canvas.drawArc(rectF4, -40.0f, 205.0f, true, mPaint);//不包含中心
        RectF rectF5 = new RectF(500, 100, 600, 200);
        canvas.drawArc(rectF5, 45.0f, 90.0f, false, mPaint);//包含中心
        //画饼状图
        drawPie(canvas);
    }

    private void drawPie(Canvas canvas) {
        if (mColors == null || mColors.size() == 0 || mdata == null || mdata.size() == 0 || mColors.size() != mdata.size()) {
            return;
        }
        canvas.translate(mWidth / 2, (mHeight - 360) / 2 + 360);
        float r = Math.min(mWidth, mHeight - 360) / 2 * 0.8f;
        RectF rectF = new RectF(-r, -r, r, r);
//        canvas.drawRect(rectF, mPaint);//背景矩形
        for (int i = 0; i < mdata.size(); i++) {
            mPaint.setColor(mColors.get(i));
            float mCurrentAngle = mdata.get(i) / mCum * 360;
            canvas.drawArc(rectF, mStartAngle, mCurrentAngle, true, mPaint);
            mStartAngle += mCurrentAngle;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
    }

    // 设置起始角度
    public void setStartAngle(float mStartAngle) {
        this.mStartAngle = mStartAngle;
        invalidate();
    }

    public void setData(ArrayList<Integer> data, ArrayList<Integer> mColors) {
        this.mColors = mColors;
        this.mdata = data;
        mCum = 0;
        for (int p : data) {
            mCum += p;
        }
        invalidate();   // 刷新
    }

}
