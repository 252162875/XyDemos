package kx.rnd.com.permissionstest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * 贝赛尔曲线练习
 */
public class BezierTest3View extends View {
    private int mCenterX, mCenterY;
    private static final float C = 0.551915024494f;     // 一个常量，用来计算绘制圆形贝塞尔曲线控制点的位置
    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private int mCircleRadius = 200;  // 圆的半径
    private float mDifference = mCircleRadius * C;        // 圆形的控制点与数据点的差值
    private float[] mData = new float[8];               // 顺时针记录绘制圆形的四个数据点
    private float[] mCtrl = new float[16];              // 顺时针记录绘制圆形的八个控制点
    private float mDuration = 1000;                     // 变化总时长
    private float mCurrent = 0;                         // 当前已进行时长
    private float mCount = 100;                         // 将时长总共划分多少份
    private float mPiece = mDuration / mCount;            // 每一份的时长

    public BezierTest3View(Context context) {
        super(context);
        init(null, 0);
    }

    public BezierTest3View(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BezierTest3View(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        mPaint = new Paint();
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
        canvas.translate(mCenterX, mCenterY); // 将坐标系移动到画布中央

        mPaint.setStyle(Paint.Style.STROKE);
        // 绘制数据点和控制点
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(20);

        // 绘制辅助线
        mPaint.setStrokeWidth(4);

        // 绘制贝塞尔曲线
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(8);
        Path path = new Path();
        path.moveTo(mData[0], mData[1]);
        path.cubicTo(mCtrl[2], mCtrl[3], mCtrl[4], mCtrl[5], mData[2], mData[3]);
        path.cubicTo(mCtrl[6], mCtrl[7], mCtrl[8], mCtrl[9], mData[4], mData[5]);
        path.cubicTo(mCtrl[10], mCtrl[11], mCtrl[12], mCtrl[13], mData[6], mData[7]);
        path.cubicTo(mCtrl[14], mCtrl[15], mCtrl[0], mCtrl[1], mData[0], mData[1]);
        canvas.drawPath(path, mPaint);

        mCurrent += mPiece;
        if (mCurrent < mDuration) {

            mData[1] += 120 / mCount;
            mCtrl[9] -= 80 / mCount;
            mCtrl[11] -= 80 / mCount;

            mCtrl[6] -= 20 / mCount;
            mCtrl[12] += 20 / mCount;

            postInvalidateDelayed((long) mPiece);
        }
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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        initData();
    }

    public void rePlay() {
        initData();
        invalidate();
    }

    private void initData() {
        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;
        mCurrent = 0;
        mCircleRadius = Math.min(mWidth, mHeight) * 2 / 5;
        mDifference = mCircleRadius * C;
//        int a  = Math.min(mWidth, mHeight) * 2 / 5;
        // 顺时针初始化数据点
        mData[0] = 0;
        mData[1] = -mCircleRadius;

        mData[2] = mCircleRadius;
        mData[3] = 0;

        mData[4] = 0;
        mData[5] = mCircleRadius;

        mData[6] = -mCircleRadius;
        mData[7] = 0;

        // 顺时针初始化控制点
        mCtrl[0] = mData[0] - mDifference;
        mCtrl[1] = mData[1];

        mCtrl[2] = mData[0] + mDifference;
        mCtrl[3] = mData[1];

        mCtrl[4] = mData[2];
        mCtrl[5] = mData[3] - mDifference;

        mCtrl[6] = mData[2];
        mCtrl[7] = mData[3] + mDifference;

        mCtrl[8] = mData[4] + mDifference;
        mCtrl[9] = mData[5];

        mCtrl[10] = mData[4] - mDifference;
        mCtrl[11] = mData[5];

        mCtrl[12] = mData[6];
        mCtrl[13] = mData[7] + mDifference;

        mCtrl[14] = mData[6];
        mCtrl[15] = mData[7] - mDifference;
    }
}
