package kx.rnd.com.permissionstest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class CanvasRotateView extends View {

    private Paint mPaint;
    private int mWidth;
    private int mHeight;

    public CanvasRotateView(Context context) {
        super(context);
        init(null, 0);
    }

    public CanvasRotateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CanvasRotateView(Context context, AttributeSet attrs, int defStyle) {
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
        //
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        canvas.translate(mWidth / 2, mHeight / 2);
        mPaint.setColor(Color.RED);
        canvas.drawCircle(0, 0, 370, mPaint);
        canvas.drawCircle(0, 0, 200, mPaint);
        canvas.drawCircle(0, 0, 150, mPaint);
        RectF rectF = new RectF(200, -25, 350, 25);
        for (int i = 0; i < 8; i++) {
            mPaint.setColor(Color.BLUE);
            canvas.rotate(i * 45);//不设置旋转中心的话默认是画布中心
            mPaint.setColor(Color.GREEN);
            canvas.drawLine(150, 0, 200, 0, mPaint);
            canvas.save();
            canvas.skew(1, 0);
            canvas.drawRect(rectF, mPaint);
            canvas.restore();
        }

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
    }
}
