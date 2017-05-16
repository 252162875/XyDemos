package kx.rnd.com.permissionstest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import kx.rnd.com.permissionstest.R;

public class PolygonView extends View {
    private String[] str = {"语文", "数学", "英语", "物理", "化学", "生物", "历史"};
    private float oneRadius; //外层菱形圆半径
    int center;//中心点X值
    int defaultSize = 400;//控件默认尺寸（长宽相同）
    private Paint strPaint;//字体画笔
    private Paint centerPaint;//中心线画笔
    private Paint rankPaint;//可变的各等级进度画笔
    private Paint firstPaint;//最外层多边形画笔
    private Paint secondPaint;//第二层多边形画笔
    private Paint thirdPaint;//第三层多边形画笔
    private Paint fourthPaint;//第四层多边形画笔
    private Rect strRect;//字体矩阵
    private float f1, f2, f3, f4, f5, f6, f7;
    private float distance;//计算坐标用的距离


    public PolygonView(Context context) {
        this(context, null);
    }

    public PolygonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PolygonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defaultSize = dp2px(defaultSize);
        //初始化字体画笔
        strPaint = new Paint();
        strPaint.setAntiAlias(true);//设置抗锯齿
        strPaint.setColor(Color.BLUE);
        strPaint.setTextSize(dp2px(16));//设置默认的字体大小为16dp
        strRect = new Rect();
        strPaint.getTextBounds(str[0], 0, str[0].length(), strRect);//得到字符串的尺寸
        //初始化中心线画笔
        centerPaint = new Paint();
        centerPaint.setAntiAlias(true);
        centerPaint.setColor(getResources().getColor(R.color.colorC));
        //初始化各等级进度画笔
        rankPaint = new Paint();
        rankPaint.setAntiAlias(true);
        rankPaint.setColor(getResources().getColor(R.color.colorR));
        rankPaint.setStrokeWidth(4);
        rankPaint.setStyle(Paint.Style.STROKE);//设置空心
        //初始化最外层多边形画笔
        firstPaint = new Paint();
        firstPaint.setAntiAlias(true);
        firstPaint.setColor(getResources().getColor(R.color.color4));
        firstPaint.setStyle(Paint.Style.FILL);//设置实心(实心代表画出来会填充区域)
        //初始化第二层多边形画笔
        secondPaint = new Paint();
        secondPaint.setAntiAlias(true);
        secondPaint.setColor(getResources().getColor(R.color.color3));
        secondPaint.setStyle(Paint.Style.FILL);//设置实心(实心代表画出来会填充区域)
        //初始化第三层多边形画笔
        thirdPaint = new Paint();
        thirdPaint.setAntiAlias(true);
        thirdPaint.setColor(getResources().getColor(R.color.color2));
        thirdPaint.setStyle(Paint.Style.FILL);//设置实心(实心代表画出来会填充区域)
        // 初始化第三层多边形画笔
        fourthPaint = new Paint();
        fourthPaint.setAntiAlias(true);
        fourthPaint.setColor(getResources().getColor(R.color.color1));
        fourthPaint.setStyle(Paint.Style.FILL);//设置实心(实心代表画出来会填充区域)
    }

    public void setValue1(float value) {
        f1 = oneRadius-oneRadius / 4 * value;
        invalidate();
    }

    public void setValue2(float value) {
        f2 = oneRadius-oneRadius / 4 * value;
        invalidate();
    }

    public void setValue3(float value) {
        f3 = oneRadius-oneRadius / 4 * value;
        invalidate();
    }

    public void setValue4(float value) {
        f4 = oneRadius-oneRadius / 4 * value;
        invalidate();
    }

    public void setValue5(float value) {
        f5 = oneRadius-oneRadius / 4 * value;
        invalidate();
    }

    public void setValue6(float value) {
        f6 = oneRadius-oneRadius / 4 * value;
        invalidate();
    }

    public void setValue7(float value) {
        f7 = oneRadius-oneRadius / 4 * value;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawFont(canvas);
        drawFirst(canvas);
        drawSecond(canvas);
        drawThird(canvas);
        drawFourth(canvas);
        drawCenter(canvas);
        drawRank(canvas);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width, height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = Math.min(widthSize, defaultSize);
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = Math.min(heightSize, defaultSize);
        }
        center = width / 2;//初始化中心点的坐标值（由于是正方形所以宽高相同）
        oneRadius = center - getPaddingTop() - 2 * strRect.height();
        f1 = oneRadius - oneRadius / 4 * 1;
        f2 = oneRadius - oneRadius / 4 * 1;
        f3 = oneRadius - oneRadius / 4 * 1;
        f4 = oneRadius - oneRadius / 4 * 1;
        f5 = oneRadius - oneRadius / 4 * 1;
        f6 = oneRadius - oneRadius / 4 * 1;
        f7 = oneRadius - oneRadius / 4 * 1;
        setMeasuredDimension(width, height);
    }

    /**
     * 绘制字体
     *
     * @param canvas
     */
    private void drawFont(Canvas canvas) {
        canvas.drawText(str[0], center - strRect.width() / 2, (float) (getPaddingTop() + 1.5 * strRect.height()), strPaint);
        canvas.drawText(str[1], (float) (center + Math.sin(Math.toRadians(360 / 7)) * oneRadius + strRect.height() / 2), (float) ((getPaddingTop() + 2 * strRect.height() + oneRadius - Math.abs(Math.cos(Math.toRadians(360 / 7)) * oneRadius))), strPaint);
        canvas.drawText(str[2], (float) (center + Math.sin(Math.toRadians(360 / 7 + 360 / 7 / 2)) * oneRadius + strRect.height() / 2), (float) (Math.cos(Math.toRadians(360 / 7 + 360 / 7 / 2)) * oneRadius) + center + strRect.height() / 2, strPaint);
        canvas.drawText(str[3], (float) (center + Math.sin(Math.toRadians(360 / 7 / 2)) * oneRadius - strRect.height() / 2 + strRect.width() / 2), (float) ((Math.cos(Math.toRadians(360 / 7 / 2)) * oneRadius) + center + strRect.height()), strPaint);
        canvas.drawText(str[4], (float) (center - Math.sin(Math.toRadians(360 / 7 / 2)) * oneRadius + strRect.height() / 2 - strRect.width() * 1.5), (float) ((Math.cos(Math.toRadians(360 / 7 / 2)) * oneRadius) + center + strRect.height()), strPaint);
        canvas.drawText(str[5], (float) (center - Math.sin(Math.toRadians(360 / 7 + 360 / 7 / 2)) * oneRadius - strRect.height() / 2 - strRect.width()), (float) (Math.cos(Math.toRadians(360 / 7 + 360 / 7 / 2)) * oneRadius) + center + strRect.height() / 2, strPaint);
        canvas.drawText(str[6], (float) (center - Math.sin(Math.toRadians(360 / 7)) * oneRadius - strRect.height() / 2 - strRect.width()), (float) ((getPaddingTop() + 2 * strRect.height() + oneRadius - Math.abs(Math.cos(Math.toRadians(360 / 7)) * oneRadius))), strPaint);

    }

    /**
     * 画七条中心线
     *
     * @param canvas
     */
    private void drawCenter(Canvas canvas) {
        //绘制七边形中心线
        canvas.save();//保存当前状态
        canvas.rotate(0, center, center);
        float startY = getPaddingTop() + 2 * strRect.height();
        float endY = center;
        float du = (float) (360 / 7 + 0.5);
        for (int i = 0; i < 7; i++) {
            canvas.drawLine(center, startY, center, endY, centerPaint);
            canvas.rotate(du, center, center);
        }
        canvas.restore();//恢复之前状态
    }

    /**
     * 画可变的多边形
     *
     * @param canvas
     */
    private void drawRank(Canvas canvas) {
        Path path = new Path();
        path.moveTo(center, getPaddingTop() + 2 * strRect.height() + f1);//默认显示在f1（距离最外边3/4半径）位置处
        path.lineTo((float) (center + Math.sin(Math.toRadians(360 / 7)) * (oneRadius - f2)), (float) (getPaddingTop() + 2 * strRect.height() + (oneRadius) - Math.abs(Math.cos(Math.toRadians(360 / 7)) * (oneRadius - f2))));
        path.lineTo((float) (center + Math.sin(Math.toRadians(360 / 7 + 360 / 7 / 2)) * (oneRadius - f3)), (float) (Math.cos(Math.toRadians(360 / 7 + 360 / 7 / 2)) * (oneRadius - f3)) + center);
        path.lineTo((float) (center + Math.sin(Math.toRadians(360 / 7 / 2)) * (oneRadius - f4)), (float) (Math.cos(Math.toRadians(360 / 7 / 2)) * (oneRadius - f4)) + center);
        path.lineTo((float) (center - Math.sin(Math.toRadians(360 / 7 / 2)) * (oneRadius - f5)), (float) (Math.cos(Math.toRadians(360 / 7 / 2)) * (oneRadius - f5)) + center);
        path.lineTo((float) (center - Math.sin(Math.toRadians(360 / 7 + 360 / 7 / 2)) * (oneRadius - f6)), (float) (Math.cos(Math.toRadians(360 / 7 + 360 / 7 / 2)) * (oneRadius - f6)) + center);
        path.lineTo((float) (center - Math.sin(Math.toRadians(360 / 7)) * (oneRadius - f7)), (float) (getPaddingTop() + 2 * strRect.height() + (oneRadius) - Math.abs(Math.cos(Math.toRadians(360 / 7)) * (oneRadius - f7))));
        path.close();
        canvas.drawPath(path, rankPaint);
    }

    /**
     * 画最外层的多边形
     *
     * @param canvas
     */
    private void drawFirst(Canvas canvas) {
        Path path = new Path();
        path.moveTo(center, getPaddingTop() + 2 * strRect.height());
        path.lineTo((float) (center + Math.sin(Math.toRadians(360 / 7)) * oneRadius), (float) (getPaddingTop() + 2 * strRect.height() + (oneRadius) - Math.abs(Math.cos(Math.toRadians(360 / 7)) * oneRadius)));
        path.lineTo((float) (center + Math.sin(Math.toRadians(360 / 7 + 360 / 7 / 2)) * oneRadius), (float) (Math.cos(Math.toRadians(360 / 7 + 360 / 7 / 2)) * oneRadius) + center);
        path.lineTo((float) (center + Math.sin(Math.toRadians(360 / 7 / 2)) * oneRadius), (float) (Math.cos(Math.toRadians(360 / 7 / 2)) * oneRadius) + center);
        path.lineTo((float) (center - Math.sin(Math.toRadians(360 / 7 / 2)) * oneRadius), (float) (Math.cos(Math.toRadians(360 / 7 / 2)) * oneRadius) + center);
        path.lineTo((float) (center - Math.sin(Math.toRadians(360 / 7 + 360 / 7 / 2)) * oneRadius), (float) (Math.cos(Math.toRadians(360 / 7 + 360 / 7 / 2)) * oneRadius) + center);
        path.lineTo((float) (center - Math.sin(Math.toRadians(360 / 7)) * oneRadius), (float) (getPaddingTop() + 2 * strRect.height() + (oneRadius) - Math.abs(Math.cos(Math.toRadians(360 / 7)) * oneRadius)));
        path.close();
        canvas.drawPath(path, firstPaint);
    }

    /**
     * 画第二层的多边形
     *
     * @param canvas
     */
    private void drawSecond(Canvas canvas) {
        distance = oneRadius / 4;
        Path path = new Path();
        path.moveTo(center, getPaddingTop() + 2 * strRect.height() + distance);
        path.lineTo((float) (center + Math.sin(Math.toRadians(360 / 7)) * (oneRadius - distance)), (float) (getPaddingTop() + 2 * strRect.height() + (oneRadius) - Math.abs(Math.cos(Math.toRadians(360 / 7)) * (oneRadius - distance))));
        path.lineTo((float) (center + Math.sin(Math.toRadians(360 / 7 + 360 / 7 / 2)) * (oneRadius - distance)), (float) (Math.cos(Math.toRadians(360 / 7 + 360 / 7 / 2)) * (oneRadius - distance)) + center);
        path.lineTo((float) (center + Math.sin(Math.toRadians(360 / 7 / 2)) * (oneRadius - distance)), (float) (Math.cos(Math.toRadians(360 / 7 / 2)) * (oneRadius - distance)) + center);
        path.lineTo((float) (center - Math.sin(Math.toRadians(360 / 7 / 2)) * (oneRadius - distance)), (float) (Math.cos(Math.toRadians(360 / 7 / 2)) * (oneRadius - distance)) + center);
        path.lineTo((float) (center - Math.sin(Math.toRadians(360 / 7 + 360 / 7 / 2)) * (oneRadius - distance)), (float) (Math.cos(Math.toRadians(360 / 7 + 360 / 7 / 2)) * (oneRadius - distance)) + center);
        path.lineTo((float) (center - Math.sin(Math.toRadians(360 / 7)) * (oneRadius - distance)), (float) (getPaddingTop() + 2 * strRect.height() + (oneRadius) - Math.abs(Math.cos(Math.toRadians(360 / 7)) * (oneRadius - distance))));
        path.close();
        canvas.drawPath(path, secondPaint);
    }

    /**
     * 画第三层的多边形
     *
     * @param canvas
     */
    private void drawThird(Canvas canvas) {
        distance = oneRadius / 2;
        Path path = new Path();
        path.moveTo(center, getPaddingTop() + 2 * strRect.height() + distance);
        path.lineTo((float) (center + Math.sin(Math.toRadians(360 / 7)) * (oneRadius - distance)), (float) (getPaddingTop() + 2 * strRect.height() + (oneRadius) - Math.abs(Math.cos(Math.toRadians(360 / 7)) * (oneRadius - distance))));
        path.lineTo((float) (center + Math.sin(Math.toRadians(360 / 7 + 360 / 7 / 2)) * (oneRadius - distance)), (float) (Math.cos(Math.toRadians(360 / 7 + 360 / 7 / 2)) * (oneRadius - distance)) + center);
        path.lineTo((float) (center + Math.sin(Math.toRadians(360 / 7 / 2)) * (oneRadius - distance)), (float) (Math.cos(Math.toRadians(360 / 7 / 2)) * (oneRadius - distance)) + center);
        path.lineTo((float) (center - Math.sin(Math.toRadians(360 / 7 / 2)) * (oneRadius - distance)), (float) (Math.cos(Math.toRadians(360 / 7 / 2)) * (oneRadius - distance)) + center);
        path.lineTo((float) (center - Math.sin(Math.toRadians(360 / 7 + 360 / 7 / 2)) * (oneRadius - distance)), (float) (Math.cos(Math.toRadians(360 / 7 + 360 / 7 / 2)) * (oneRadius - distance)) + center);
        path.lineTo((float) (center - Math.sin(Math.toRadians(360 / 7)) * (oneRadius - distance)), (float) (getPaddingTop() + 2 * strRect.height() + (oneRadius) - Math.abs(Math.cos(Math.toRadians(360 / 7)) * (oneRadius - distance))));
        path.close();
        canvas.drawPath(path, thirdPaint);
    }

    /**
     * 画第四层的多边形
     *
     * @param canvas
     */
    private void drawFourth(Canvas canvas) {
        distance = (oneRadius / 4) * 3;
        Path path = new Path();
        path.moveTo(center, getPaddingTop() + 2 * strRect.height() + distance);
        path.lineTo((float) (center + Math.sin(Math.toRadians(360 / 7)) * (oneRadius - distance)), (float) (getPaddingTop() + 2 * strRect.height() + (oneRadius) - Math.abs(Math.cos(Math.toRadians(360 / 7)) * (oneRadius - distance))));
        path.lineTo((float) (center + Math.sin(Math.toRadians(360 / 7 + 360 / 7 / 2)) * (oneRadius - distance)), (float) (Math.cos(Math.toRadians(360 / 7 + 360 / 7 / 2)) * (oneRadius - distance)) + center);
        path.lineTo((float) (center + Math.sin(Math.toRadians(360 / 7 / 2)) * (oneRadius - distance)), (float) (Math.cos(Math.toRadians(360 / 7 / 2)) * (oneRadius - distance)) + center);
        path.lineTo((float) (center - Math.sin(Math.toRadians(360 / 7 / 2)) * (oneRadius - distance)), (float) (Math.cos(Math.toRadians(360 / 7 / 2)) * (oneRadius - distance)) + center);
        path.lineTo((float) (center - Math.sin(Math.toRadians(360 / 7 + 360 / 7 / 2)) * (oneRadius - distance)), (float) (Math.cos(Math.toRadians(360 / 7 + 360 / 7 / 2)) * (oneRadius - distance)) + center);
        path.lineTo((float) (center - Math.sin(Math.toRadians(360 / 7)) * (oneRadius - distance)), (float) (getPaddingTop() + 2 * strRect.height() + (oneRadius) - Math.abs(Math.cos(Math.toRadians(360 / 7)) * (oneRadius - distance))));
        path.close();
        canvas.drawPath(path, fourthPaint);
    }


    /**
     * dp转px
     *
     * @param values
     * @return
     */
    public int dp2px(int values) {

        float density = getResources().getDisplayMetrics().density;
        return (int) (values * density + 0.5f);
    }
}