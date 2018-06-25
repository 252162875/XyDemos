package kx.rnd.com.permissionstest.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import kx.rnd.com.permissionstest.R;
import kx.rnd.com.permissionstest.utils.SizeUtil;

/**
 * author: 梦境缠绕
 * created on: 2018/6/21 0021 10:51
 * description:
 */

public class StepView extends View {
    private int step = 0;
    private int width, height;
    private int realWidth;
    private int realHeight;
    private int defaultWidth = 400;//控件默认宽度
    private int defaultHeight = 400;//控件默认高度
    private int centerX;//中心点X值
    private int centerY;//中心点Y值
    private float distanceX;//横向每个元素距离
    private ArrayList<String> strings = new ArrayList<>(5);
    private float vDistance;//文字和圆点纵向距离
    private float mDotSize; //圆点大小
    private float mLineWidth; //线宽
    private float mTextSize; //文字大小

    private int mDotSelectedImg; //圆点选中
    private int mDotUnSelectedImg; //圆点未选中
    private int mLineSelectedColor; //线段选中颜色
    private int mLineUnSelectedColor; //线段未选中颜色
    private int mTextSelectedColor; //文字选中颜色
    private int mTextUnSelectedColor; //文字未选中颜色
    private Paint mPaint; //画笔
    private float distanceTextX;

    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        obtainStyledAttrs(attrs); //获取自定义的属性
        init(); //初始化画笔
    }

    //获取自定义的属性
    private void obtainStyledAttrs(AttributeSet attrs) {
        TypedArray array = null;
        try {
            array = getContext().obtainStyledAttributes(attrs, R.styleable.StepView);
            mDotSize = array.getDimension(R.styleable.StepView_sv_dotsize, DptoPx(20));
            mLineWidth = array.getDimension(R.styleable.StepView_sv_linewidth, DptoPx(1));
            vDistance = array.getDimension(R.styleable.StepView_sv_dot_text_distance, DptoPx(20));
            mTextSize = array.getDimension(R.styleable.StepView_sv_textsize, SptoPx(15));

            mDotSelectedImg = array.getResourceId(R.styleable.StepView_sv_dot_selected_color, R.drawable.dot_select_red);
            mDotUnSelectedImg = array.getResourceId(R.styleable.StepView_sv_dot_unselected_color, R.drawable.dot_unselect_red);
            mLineSelectedColor = array.getColor(R.styleable.StepView_sv_line_selected_color, Color.RED);
            mLineUnSelectedColor = array.getColor(R.styleable.StepView_sv_line_unselected_color, Color.BLACK);
            mTextSelectedColor = array.getColor(R.styleable.StepView_sv_text_selected_color, Color.RED);
            mTextUnSelectedColor = array.getColor(R.styleable.StepView_sv_text_unselected_color, Color.BLACK);
        } catch (Exception e) {
            //一旦出现错误全部使用默认值
            mDotSize = DptoPx(20);
            mLineWidth = DptoPx(1);
            vDistance = DptoPx(20);
            mTextSize = SptoPx(15);

            mDotSelectedImg = R.drawable.dot_select_red;
            mDotUnSelectedImg = R.drawable.dot_unselect_red;
            mLineSelectedColor = Color.RED;
            mLineUnSelectedColor = Color.BLACK;
            mTextSelectedColor = Color.RED;
            mTextUnSelectedColor = Color.BLACK;
        } finally {
            if (array != null) {
                array.recycle();
            }
        }
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//设置抗锯齿
        mPaint.setDither(true);//防抖动
        strings.add("STEP1");
        strings.add("STEP2");
        strings.add("STEP3");
    }

    //Dp转px
    private float DptoPx(float value) {
        return SizeUtil.Dp2Px(getContext(), value);
    }

    //sp转px
    private float SptoPx(float value) {
        return SizeUtil.Sp2Px(getContext(), value);
    }

    public void setAllSteps(ArrayList<String> strings) {
        this.strings.clear();
        this.strings.addAll(strings);
        invalidate();
    }

    public void setStep(int step) {
        if (step + 1 > strings.size()) {
            throw new RuntimeException("setStep参数值大于setAllSteps参数集合最大值（默认长度3，注意setAllSteps在setStep之前调用）");
        }
        this.step = step;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas);
        drawDot(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // 当布局参数设置为wrap_content时，设置默认值
        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            width = defaultWidth;
            height = defaultHeight;
            // 宽 / 高任意一个布局参数为= wrap_content时，都设置默认值
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            width = defaultWidth;
            height = heightSize;
        } else if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            width = widthSize;
            height = defaultHeight;
        } else {
            width = widthSize;
            height = heightSize;
        }

        realWidth = width - getPaddingLeft() - getPaddingRight();
        realHeight = width - getPaddingTop() - getPaddingBottom();
        centerX = width / 2;
        centerY = height / 2;
        distanceX = (float) realWidth / (strings.size() - 1);//注意个数不能为0
        Rect strRectStart = new Rect();
        mPaint.getTextBounds(strings.get(0), 0, strings.get(0).length(), strRectStart);
        Rect strRectEnd = new Rect();
        mPaint.getTextBounds(strings.get(strings.size() - 1), 0, strings.get(strings.size() - 1).length(), strRectEnd);
        int realTextWidth = realWidth - strRectStart.width() / 2 - strRectEnd.width() / 2;
        //注意个数不能为0
        distanceTextX = (float) realTextWidth / (strings.size() - 1);
        setMeasuredDimension(width, height);
    }

    private void drawText(Canvas canvas) {
//        canvas.drawColor(Color.GREEN);
        mPaint.setTextSize(mTextSize);//设置默认的字体大小为15sp
        ArrayList<Rect> rects = new ArrayList<>();
        for (int i = 0; i < strings.size(); i++) {
            if (i <= step) {
                mPaint.setColor(mTextSelectedColor);
            } else {
                mPaint.setColor(mTextUnSelectedColor);
            }
            getStringRects(rects, i);
            canvas.drawText(strings.get(i), getPaddingLeft() + (float) (rects.get(0).width() / 2) + distanceTextX * i - (float) (rects.get(i).width() / 2), centerY + vDistance / 2 + rects.get(0).height(), mPaint);
        }
    }


    private void drawDot(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));//canvas加抗锯齿
        mPaint.setColor(Color.BLACK);
        Bitmap bitmap1 = BitmapFactory.decodeResource(getContext().getResources(), mDotSelectedImg);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getContext().getResources(), mDotUnSelectedImg);
        ArrayList<Rect> rects = new ArrayList<>();
        for (int i = 0; i < strings.size(); i++) {
            getStringRects(rects, i);
            if (i <= step) {
                canvas.drawBitmap(bitmap1, null, new RectF(getPaddingLeft() + (float) (rects.get(0).width() / 2) + distanceTextX * i - mDotSize / 2, centerY - vDistance / 2 - mDotSize, getPaddingLeft() + (float) (rects.get(0).width() / 2) + distanceTextX * i + mDotSize / 2, centerY - vDistance / 2), mPaint);
            } else {
                canvas.drawBitmap(bitmap2, null, new RectF(getPaddingLeft() + (float) (rects.get(0).width() / 2) + distanceTextX * i - mDotSize / 2, centerY - vDistance / 2 - mDotSize, getPaddingLeft() + (float) (rects.get(0).width() / 2) + distanceTextX * i + mDotSize / 2, centerY - vDistance / 2), mPaint);
            }
        }
        mPaint.setStrokeWidth(mLineWidth);
        for (int i = 0; i < strings.size() - 1; i++) {
            if (i < step) {
                mPaint.setColor(mLineSelectedColor);
            } else {
                mPaint.setColor(mLineUnSelectedColor);
            }
            canvas.drawLine(getPaddingLeft() + (float) (rects.get(0).width() / 2) + distanceTextX * i + mDotSize / 2, centerY - vDistance / 2 - mDotSize / 2, getPaddingLeft() + (float) (rects.get(0).width() / 2) + distanceTextX * i + mDotSize / 2 + distanceTextX - mDotSize, centerY - vDistance / 2 - mDotSize / 2, mPaint);
        }
    }

    private void getStringRects(ArrayList<Rect> rects, int i) {
        Rect strRect = new Rect();
        mPaint.getTextBounds(strings.get(i), 0, strings.get(i).length(), strRect);//得到字符串的尺寸
        rects.add(strRect);
    }
}
