package kx.rnd.com.permissionstest.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

import kx.rnd.com.permissionstest.R;
import kx.rnd.com.permissionstest.utils.SizeUtil;

public class WatchView extends View {

    private float mRadius; //外圆半径
    private float mPadding; //边距
    private float mTextSize; //文字大小
    private float mHourPointWidth; //时针宽度
    private float mMinutePointWidth; //分针宽度
    private float mSecondPointWidth; //秒针宽度
    private int mPointRadius; // 指针圆角
    private float mPointEndLength; //指针末尾的长度

    private int mColorLong; //长线的颜色
    private int mColorShort; //短线的颜色
    private int mHourPointColor; //时针的颜色
    private int mMinutePointColor; //分针的颜色
    private int mSecondPointColor; //秒针的颜色

    private Paint mPaint; //画笔

    public WatchView(Context context) {
        this(context, null);
    }

    public WatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        obtainStyledAttrs(attrs); //获取自定义的属性
        init(); //初始化画笔
    }

    //画笔初始化
    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//设置抗锯齿
        mPaint.setDither(true);//防抖动
    }

    //获取自定义的属性
    private void obtainStyledAttrs(AttributeSet attrs) {
        TypedArray array = null;
        try {
            array = getContext().obtainStyledAttributes(attrs, R.styleable.WatchView);
            mPadding = array.getDimension(R.styleable.WatchView_wv_padding, DptoPx(10));
            mTextSize = array.getDimension(R.styleable.WatchView_wv_textsize, SptoPx(16));
            mHourPointWidth = array.getDimension(R.styleable.WatchView_wv_hour_width, DptoPx(5));
            mMinutePointWidth = array.getDimension(R.styleable.WatchView_wv_minute_pointer_color, DptoPx(3));
            mSecondPointWidth = array.getDimension(R.styleable.WatchView_wv_second_width, DptoPx(2));
            mPointRadius = (int) array.getDimension(R.styleable.WatchView_wv_pointer_corner_radius, DptoPx(10));
            mPointEndLength = array.getDimension(R.styleable.WatchView_wv_pointer_end_length, DptoPx(10));

            mColorLong = array.getColor(R.styleable.WatchView_wv_scale_long_color, Color.argb(225, 0, 0, 0));
            mColorShort = array.getColor(R.styleable.WatchView_wv_scale_short_color, Color.argb(125, 0, 0, 0));
            mHourPointColor = array.getColor(R.styleable.WatchView_wv_hour_pointer_color, Color.BLACK);
            mMinutePointColor = array.getColor(R.styleable.WatchView_wv_minute_pointer_color, Color.BLACK);
            mSecondPointColor = array.getColor(R.styleable.WatchView_wv_second_pointer_color, Color.RED);
        } catch (Exception e) {
            //一旦出现错误全部使用默认值
            mPadding = DptoPx(10);
            mTextSize = SptoPx(16);
            mHourPointWidth = DptoPx(5);
            mMinutePointWidth = DptoPx(3);
            mSecondPointWidth = DptoPx(2);
            mPointRadius = (int) DptoPx(10);
            mPointEndLength = DptoPx(10);

            mColorLong = Color.argb(225, 0, 0, 0);
            mColorShort = Color.argb(125, 0, 0, 0);
            mHourPointColor = Color.BLACK;
            mMinutePointColor = Color.BLACK;
            mSecondPointColor = Color.RED;
        } finally {
            if (array != null) {
                array.recycle();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 2000; //设定一个最小值
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        Log.e("AT_MOST_widthMode", widthMode == MeasureSpec.AT_MOST ? "1" : "2");//wrap_content
        Log.e("AT_MOST_heightMode", heightMode == MeasureSpec.AT_MOST ? "1" : "2");//wrap_content
        Log.e("UNSPECIFIED_widthMode", widthMode == MeasureSpec.UNSPECIFIED ? "1" : "2");
        Log.e("UNSPECIFIED_heightMode", heightMode == MeasureSpec.UNSPECIFIED ? "1" : "2");
        Log.e("EXACTLY_widthMode", widthMode == MeasureSpec.EXACTLY ? "1" : "2");//match_parent,精确值
        Log.e("EXACTLY_heightMode", heightMode == MeasureSpec.EXACTLY ? "1" : "2");//match_parent,精确值
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            try {
                throw new NoDetermineSizeException("宽度高度至少有一个确定的值,不能同时为wrap_content");
            } catch (NoDetermineSizeException e) {
                e.printStackTrace();
            }
        } else { //至少有一个为确定值,要获取其中的最小值
            if (widthMode == MeasureSpec.EXACTLY) {
                width = Math.min(widthSize, width);
            }
            if (heightMode == MeasureSpec.EXACTLY) {
                width = Math.min(heightSize, width);
            }
            setMeasuredDimension(width, width);
        }
    }

    //获取值应该在测量完成之后,所以在onSizeChange里面获取
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mRadius = (Math.min(w, h) - mPadding) / 2;
        mPointEndLength = mRadius / 6; //尾部指针默认为半径的六分之一
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        //绘制外圆背景
        paintCircle(canvas);
        //绘制刻度
        paintScale(canvas);
        //绘制指针
        paintPointer(canvas);
        canvas.restore();
        //每秒刷新
        postInvalidateDelayed(1000);
    }

    //绘制外圆背景
    private void paintCircle(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);//设置实心，画出来就是填充效果
        canvas.drawCircle(0, 0, mRadius, mPaint);
    }

    //绘制刻度
    private void paintScale(Canvas canvas) {
        mPaint.setStrokeWidth(SizeUtil.Dp2Px(getContext(), 1));//设置画笔宽度
        int lineWidth = 0;//定义刻度长度
        //一共59个刻度,当刻度是第0个或者5的倍数时，刻度长，其他时候刻度短
        for (int i = 0; i < 60; i++) {
            if (i % 5 == 0) { //整点
                lineWidth = 40;
                mPaint.setStrokeWidth(SizeUtil.Dp2Px(getContext(), 1.5f));
                mPaint.setColor(mColorLong);
                mPaint.setTextSize(mTextSize);//设置字体大小
                String text = ((i / 5) == 0 ? 12 : (i / 5)) + "";//设置要现实的文字（根据位置算出要显示的文字）
                Rect textBound = new Rect();
                mPaint.getTextBounds(text, 0, text.length(), textBound);//计算文字所占矩阵(矩形)大小
                mPaint.setColor(Color.BLACK);//设置文字颜色
                canvas.save();
                canvas.translate(0, -mRadius + DptoPx(5) + lineWidth + mPadding + (textBound.bottom - textBound.top) / 2);
                canvas.rotate(-6 * i);
                mPaint.setStyle(Paint.Style.FILL);//设置实心
//                canvas.drawText(text, -(textBound.right - textBound.left) / 2, textBound.bottom, mPaint);
                canvas.drawText(text, -(textBound.right + textBound.left) / 2, -(textBound.bottom + textBound.top) / 2, mPaint);//这块牵扯到drawText的文字与矩阵（矩形）位置的四边关系
                canvas.restore();

            } else { //非整点
                lineWidth = 30;
                mPaint.setColor(mColorShort);
                mPaint.setStrokeWidth(SizeUtil.Dp2Px(getContext(), 1));
            }
            canvas.drawLine(0, -mRadius + SizeUtil.Dp2Px(getContext(), 10), 0, -mRadius + SizeUtil.Dp2Px(getContext(), 10) + lineWidth, mPaint);//X方向没有改变，Y方向上向上绘制10dp长度
            canvas.rotate(6);//在for循环里面每次画线完就旋转360/60 = 6 度
        }
    }

    //绘制指针
    private void paintPointer(Canvas canvas) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY); //时
        int minute = calendar.get(Calendar.MINUTE); //分
        int second = calendar.get(Calendar.SECOND); //秒
        int angleSecond = second * 360 / 60; //秒针转过的角度
        int angleMinute = minute * 360 / 60 + (second / 10); //分针转过的角度(每10秒走1度)
        int angleHour = (hour % 12) * 360 / 12 + (minute / 2); //时针转过的角度(每2分钟走1度)
        //绘制时针
        canvas.save();
        canvas.rotate(angleHour); //旋转到时针的角度
        RectF rectFHour = new RectF(-mHourPointWidth / 2, -mRadius * 3 / 5, mHourPointWidth / 2, mPointEndLength);
        mPaint.setColor(mHourPointColor); //设置指针颜色
       /* 画笔样式分三种：
        1.Paint.Style.STROKE：描边
        2.Paint.Style.FILL_AND_STROKE：描边并填充
        3.Paint.Style.FILL：填充*/
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mHourPointWidth); //设置边界宽度
        canvas.drawRoundRect(rectFHour, mPointRadius, mPointRadius, mPaint); //绘制时针
        canvas.restore();
        //绘制分针
        canvas.save();
        canvas.rotate(angleMinute);
        RectF rectFMinute = new RectF(-mMinutePointWidth / 2, -mRadius * 3.5f / 5, mMinutePointWidth / 2, mPointEndLength);
        mPaint.setColor(mMinutePointColor);
        mPaint.setStrokeWidth(mMinutePointWidth);
        canvas.drawRoundRect(rectFMinute, mPointRadius, mPointRadius, mPaint);
        canvas.restore();
        //绘制秒针
        canvas.save();
        canvas.rotate(angleSecond);
        RectF rectFSecond = new RectF(-mSecondPointWidth / 2, -mRadius + 15, mSecondPointWidth / 2, mPointEndLength);
        mPaint.setColor(mSecondPointColor);
        mPaint.setStrokeWidth(mSecondPointWidth);
        canvas.drawRoundRect(rectFSecond, mPointRadius, mPointRadius, mPaint);
        canvas.restore();
        //绘制中心小圆
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mSecondPointColor);
        canvas.drawCircle(0, 0, mSecondPointWidth * 4, mPaint);
    }

    //Dp转px
    private float DptoPx(int value) {
        return SizeUtil.Dp2Px(getContext(), value);
    }

    //sp转px
    private float SptoPx(int value) {
        return SizeUtil.Sp2Px(getContext(), value);
    }

    class NoDetermineSizeException extends Exception {
        public NoDetermineSizeException(String message) {
            super(message);
        }
    }
}