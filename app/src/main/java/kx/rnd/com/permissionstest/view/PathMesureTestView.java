package kx.rnd.com.permissionstest.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import kx.rnd.com.permissionstest.R;

/**
 *
 */
public class PathMesureTestView extends View {
    private static final float C = 0.551915024494f;     // 一个常量，用来计算绘制圆形贝塞尔曲线控制点的位置
    private int mCenterX, mCenterY;
    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private float currentValue = 0;     // 用于纪录当前的位置,取值范围[0,1]映射Path的整个长度
    private float[] pos;                // 当前点的实际位置
    private float[] tan;                // 当前点的tangent值,用于计算图片所需旋转的角度
    private Bitmap mBitmap;             // 箭头图片
    private Matrix mMatrix;             // 矩阵,用于对图片进行一些操作
    private float[] mData = new float[8];               // 顺时针记录绘制圆形的四个数据点
    private float[] mCtrl = new float[16];              // 顺时针记录绘制圆形的八个控制点
    private int mCircleRadius = 200;  // 圆的半径
    private float mDifference = mCircleRadius * C;        // 圆形的控制点与数据点的差值

    public PathMesureTestView(Context context) {
        super(context);
        init(null, 0);
    }

    public PathMesureTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PathMesureTestView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        mPaint = new Paint();
        pos = new float[2];
        tan = new float[2];
        BitmapFactory.Options options = new BitmapFactory.Options();
        mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.arrow, options);
        mMatrix = new Matrix();
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
        /**
         * 在这里额外测试了com.caverock:androidsvg:1.2.2-beta-1这个SVG解析库
         */
        canvas.save();
        try {
            SVG svg = SVG.getFromResource(getContext(), R.raw.monitor);
            svg.setDocumentHeight(mHeight);
            svg.setDocumentWidth(mWidth);
            float documentWidth = svg.getDocumentWidth();
            float documentHeight = svg.getDocumentHeight();
            canvas.translate(-documentWidth / 2, -documentHeight / 2);
            RectF rectF = new RectF(-mWidth / 2, -mHeight / 2, mWidth / 2, mHeight / 2);
            svg.renderToCanvas(canvas,rectF);
        } catch (SVGParseException e) {
            e.printStackTrace();
        }
        canvas.restore();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(8);
        mPaint.setStyle(Paint.Style.STROKE);

//        Path path = new Path();                                 // 创建 Path
//        path.addCircle(0, 0, mWidth < mHeight ? mWidth * 2 / 5 : mHeight * 2 / 5, Path.Direction.CW);           // 添加一个圆形
        Path path = new Path();
        path.moveTo(mData[0], mData[1]);
        path.cubicTo(mCtrl[2], mCtrl[3], mCtrl[4], mCtrl[5], mData[2], mData[3]);
        path.cubicTo(mCtrl[6], mCtrl[7], mCtrl[8], mCtrl[9], mData[4], mData[5]);
        path.cubicTo(mCtrl[10], mCtrl[11], mCtrl[12], mCtrl[13], mData[6], mData[7]);
        path.cubicTo(mCtrl[14], mCtrl[15], mCtrl[0], mCtrl[1], mData[0], mData[1]);
        canvas.drawPath(path, mPaint);

        PathMeasure measure = new PathMeasure(path, false);     // 创建 PathMeasure
        currentValue += 0.001;                                  // 计算当前的位置在总长度上的比例[0,1]
        if (currentValue >= 1) {
            currentValue = 0;
        }
        measure.getPosTan(measure.getLength() * currentValue, pos, tan);        // 获取当前位置的坐标以及趋势
        mMatrix.reset();                                                        // 重置Matrix
        float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI); // 计算图片旋转角度
        mMatrix.postRotate(degrees + 90, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);   // 旋转图片
        mMatrix.postTranslate(pos[0] - mBitmap.getWidth() / 2, pos[1] - mBitmap.getHeight() / 2);   // 将图片绘制中心调整到与当前点重合
        canvas.drawPath(path, mPaint);                                   // 绘制 Path
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);                     // 绘制箭头
        invalidate();//页面刷新此处是在 onDraw 里面调用了 invalidate 方法来保持界面不断刷新，但并不提倡这么做，正确对做法应该是使用 线程 或者 ValueAnimator 来控制界面的刷新，关于控制页面刷新这一部分会在后续的 动画部分 详细讲解，同样敬请期待
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


    private void initData() {
        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;
        mCircleRadius = Math.min(mWidth, mHeight) * 2 / 5;
        mDifference = mCircleRadius * C;

        // 顺时针初始化数据点
        mData[0] = 0;
        mData[1] = -mCircleRadius * 2 / 5;

        mData[2] = mCircleRadius;
        mData[3] = 0;

        mData[4] = 0;
        mData[5] = mCircleRadius;

        mData[6] = -mCircleRadius;
        mData[7] = 0;

        // 顺时针初始化控制点
        mCtrl[0] = mData[0] - mDifference;
        mCtrl[1] = mData[1] * 5 / 2;

        mCtrl[2] = mData[0] + mDifference;
        mCtrl[3] = mData[1] * 5 / 2;

        mCtrl[4] = mData[2];
        mCtrl[5] = mData[3] - mDifference;

        mCtrl[6] = mData[2] - mCircleRadius * 1 / 10;
        mCtrl[7] = mData[3] + mDifference;

        mCtrl[8] = mData[4] + mDifference;
        mCtrl[9] = mData[5] - mCircleRadius * 2 / 5;

        mCtrl[10] = mData[4] - mDifference;
        mCtrl[11] = mData[5] - mCircleRadius * 2 / 5;

        mCtrl[12] = mData[6] + mCircleRadius * 1 / 10;
        mCtrl[13] = mData[7] + mDifference;

        mCtrl[14] = mData[6];
        mCtrl[15] = mData[7] - mDifference;

    }
}
