package kx.rnd.com.permissionstest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 贝赛尔曲线练习
 */
public class BezierTest2View extends View {
    public static final int CONTROL_A = 1;
    public static final int CONTROL_B = 2;
    private int mode = CONTROL_A;
    private int mWidth;
    private int mHeight;
    private int centerX, centerY;
    private PointF start, end, control1, control2;
    private Paint mPaint;

    public BezierTest2View(Context context) {
        super(context);
        init(null, 0);
    }

    public BezierTest2View(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BezierTest2View(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        mPaint = new Paint();
        start = new PointF(0, 0);
        end = new PointF(0, 0);
        control1 = new PointF(0, 0);
        control2 = new PointF(0, 0);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        mPaint.setStyle(Paint.Style.STROKE);
        // 绘制数据点和控制点
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(20);
        canvas.drawPoint(start.x, start.y, mPaint);
        canvas.drawPoint(end.x, end.y, mPaint);
        canvas.drawPoint(control1.x, control1.y, mPaint);
        canvas.drawPoint(control2.x, control2.y, mPaint);

        // 绘制辅助线
        mPaint.setStrokeWidth(4);
        float[] a = {start.x, start.y, control1.x, control1.y, control1.x, control1.y, control2.x, control2.y, control2.x, control2.y, end.x, end.y};
        canvas.drawLines(a, mPaint);

        // 绘制贝塞尔曲线
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(8);
        Path path = new Path();
        path.moveTo(start.x, start.y);
        path.cubicTo(control1.x, control1.y, control2.x, control2.y, end.x, end.y);
        canvas.drawPath(path, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthsize = MeasureSpec.getSize(widthMeasureSpec);      //取出宽度的确切数值
        int widthmode = MeasureSpec.getMode(widthMeasureSpec);      //取出宽度的测量模式

        int heightsize = MeasureSpec.getSize(heightMeasureSpec);    //取出高度的确切数值
        int heightmode = MeasureSpec.getMode(heightMeasureSpec);    //取出高度的测量模式
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);//注意：如果对View的宽高进行修改了，不要调用 super.onMeasure( widthMeasureSpec, heightMeasureSpec); 要调用 setMeasuredDimension( widthsize, heightsize); 这个函数。

    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF pointF = new PointF(event.getX(), event.getY());
        //根据手指位置判断拖动哪个点,从而设置mode
        minDisatance(control1, control2, pointF);
        // 根据触摸位置更新控制点，并提示重绘
        switch (mode) {
            case CONTROL_A:
                control1.x = event.getX();
                control1.y = event.getY();
                break;
            case CONTROL_B:
                control2.x = event.getX();
                control2.y = event.getY();
                break;
        }
        invalidate();
        return true;
    }

    /***
     * 根据手指位置判断拖动哪个点
     * @param p1
     * @param p2
     * @param des 手指位置的Point
     */
    private void minDisatance(PointF p1, PointF p2, PointF des) {
        double sqrt1 = Math.sqrt((Math.pow(Math.abs((p1.x - des.x)), 2) + Math.pow(Math.abs((p1.y - des.y)), 2)));
        double sqrt2 = Math.sqrt((Math.pow(Math.abs((p2.x - des.x)), 2) + Math.pow(Math.abs((p2.y - des.y)), 2)));
        if (sqrt1 < sqrt2) {
            setMode(CONTROL_A);
        } else {
            setMode(CONTROL_B);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        centerX = mWidth / 2;
        centerY = mHeight / 2;
        // 初始化数据点和控制点的位置
        start.x = centerX - 200;
        start.y = centerY;
        end.x = centerX + 200;
        end.y = centerY;
        control1.x = centerX;
        control1.y = centerY - 100;
        control2.x = centerX + 50;
        control2.y = centerY - 100;
    }
}
