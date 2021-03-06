package kx.rnd.com.permissionstest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class PathTestView extends View {
    private int mWidth;
    private int mHeight;

    private Paint mPaint;

    public PathTestView(Context context) {
        super(context);
        init(null, 0);
    }

    public PathTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PathTestView(Context context, AttributeSet attrs, int defStyle) {
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


        mPaint.setColor(Color.GREEN);           // 画笔颜色
        mPaint.setStyle(Paint.Style.STROKE);    // 填充模式 - 描边
        mPaint.setStrokeWidth(1);              // 边框宽度 - 1
        canvas.translate(mWidth / 2, mHeight / 2);
        canvas.drawLine(-mWidth / 2, 0, mWidth / 2, 0, mPaint);
        canvas.drawLine(0, -mWidth / 2, 0, mWidth / 2, mPaint);
        mPaint.setStrokeWidth(10);              // 边框宽度 - 10
        mPaint.setStyle(Paint.Style.FILL);    // 填充模式
        mPaint.setColor(Color.RED);
        canvas.drawPoint(100, 100, mPaint);
        canvas.drawPoint(150, 150, mPaint);
        canvas.drawPoint(100, -100, mPaint);
        Path path = new Path();
        path.rMoveTo(100, 100);
        path.rLineTo(50, 50);
        path.lineTo(100, -100);
        path.close();//close方法:注意：close的作用是封闭路径，与连接当前最后一个点和第一个点并不等价。如果连接了最后一个点和第一个点仍然无法形成封闭图形，则close什么 也不做。
        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.MAGENTA);
        mPaint.setStyle(Paint.Style.STROKE);    // 填充模式 - 描边
        canvas.drawPath(path, mPaint);

        Path path1 = new Path();
        path1.moveTo(0, 0);
        path1.lineTo(-50, -50);
        path1.moveTo(-100, -50);
        path1.lineTo(-100, -200);
        canvas.drawPath(path1, mPaint);
        Path path2 = new Path();
        path2.moveTo(-30, 0);
        path2.lineTo(-80, -50);
        path2.setLastPoint(-130, -50);//setLastPoint相对于moveTo 具体看效果
        path2.lineTo(-130, -200);
        canvas.drawPath(path2, mPaint);

        Path path3 = new Path();
        path3.lineTo(-50, 50);
        path3.addCircle(-50, 100, 50, Path.Direction.CW);
        canvas.drawPath(path3, mPaint);

        Path path4 = new Path();
        path4.addRect(-300, -200, -150, -10, Path.Direction.CW);
        Path path5 = new Path();
        path5.addCircle(-300, -250, 50, Path.Direction.CW);
        path4.addPath(path5, -10, -10);
        canvas.drawPath(path4, mPaint);

        //将画布想上、右方向移动100(坐标系重置为新的坐标系)
        canvas.translate(100, -100);
        RectF rectF = new RectF(0, -200, 200, 0);
        Path path6 = new Path();
        path6.lineTo(100, -100);
        path6.addArc(rectF, 0, 270);
        canvas.drawPath(path6, mPaint);

        mPaint.setColor(Color.RED);
        RectF rectF1 = new RectF(-350, 250, -250, 350);
        Path path7 = new Path();
        path7.moveTo(-250, 250);
        path7.rLineTo(-50, 50);
        path7.arcTo(rectF1, 0, 270,false);//forceMoveTo默认是false,true的时候貌似和addArc一样样的~~~(true:将最后一个点移动到圆弧起点，即不连接最后一个点与圆弧起点;false:不移动，而是连接最后一个点与圆弧起点)
        canvas.drawPath(path7, mPaint);
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
    }
}
