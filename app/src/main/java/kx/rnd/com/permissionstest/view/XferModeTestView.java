package kx.rnd.com.permissionstest.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

import kx.rnd.com.permissionstest.R;

/**
 * TODO: document your custom view class.
 */
public class XferModeTestView extends View {
    private String mString; // TODO: use a default from R.string...
    private int mTextColor = Color.RED; // TODO: use a default from R.color...
    private float mTextSize = 0; // TODO: use a default from R.dimen...
    private int position = 0;
    private Paint mPaint;
    private float mTextWidth;
    private float mTextFloat;
    private float mTextHeight;
    private int strokeWidth = 10;
    private int circleRadius;
    private float rectSize;
    int circleColor = 0xffffcc44;//黄色
    int rectColor = 0xff66aaff;//蓝色
    private static final String[] sLabels = {
            "Clear", "Src", "Dst", "SrcOver",
            "DstOver", "SrcIn", "DstIn", "SrcOut",
            "DstOut", "SrcATop", "DstATop", "Xor",
            "Darken", "Lighten", "Multiply", "Screen"
    };
    private static final Xfermode[] sModes = {
            new PorterDuffXfermode(PorterDuff.Mode.CLEAR),
            new PorterDuffXfermode(PorterDuff.Mode.SRC),
            new PorterDuffXfermode(PorterDuff.Mode.DST),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER),
            new PorterDuffXfermode(PorterDuff.Mode.DST_OVER),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_IN),
            new PorterDuffXfermode(PorterDuff.Mode.DST_IN),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT),
            new PorterDuffXfermode(PorterDuff.Mode.DST_OUT),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),
            new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP),
            new PorterDuffXfermode(PorterDuff.Mode.XOR),
            new PorterDuffXfermode(PorterDuff.Mode.DARKEN),
            new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN),
            new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY),
            new PorterDuffXfermode(PorterDuff.Mode.SCREEN)
    };

    public XferModeTestView(Context context) {
        super(context);
        init(null, 0);
    }

    public XferModeTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public XferModeTestView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.XferModeTestView, defStyle, 0);

        mString = a.getString(
                R.styleable.XferModeTestView_string);
        mTextColor = a.getColor(
                R.styleable.XferModeTestView_textColor,
                mTextColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mTextSize = a.getDimension(
                R.styleable.XferModeTestView_textSize,
                mTextSize);


        a.recycle();

        // Set up a default TextPaint object
        mPaint = new Paint();
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setStrokeWidth(strokeWidth);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        mPaint.setTextSize(mTextSize);
        mTextWidth = mPaint.measureText(mString);

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        mTextFloat = Math.abs(fontMetrics.ascent);
        mTextHeight = Math.abs(fontMetrics.ascent) + Math.abs(fontMetrics.leading) + Math.abs(fontMetrics.descent);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;
        //设置背景色
        canvas.drawARGB(255, 139, 197, 186);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        canvas.save();
        int layer = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);
        //画文字
        mPaint.setColor(mTextColor);
        canvas.drawText(sLabels[position], paddingLeft, mTextFloat + paddingTop, mPaint);//mTextFloat + paddingTop浮动的话顶部会有缝隙mTextHeight是ascent的绝对值
//        canvas.drawText(mString, paddingLeft, getTop() + paddingTop, mPaint);//getTop() + paddingTop浮动的话文字将会做到紧贴控件顶部的效果
        canvas.translate(0, mTextHeight + paddingTop);
        //画边框
        mPaint.setStyle(Paint.Style.STROKE);//画边框选择不填充的模式
        mPaint.setColor(0xff00ff00);
        canvas.drawRect(strokeWidth / 2 - paddingLeft, strokeWidth / 2 - paddingRight, getRight() - getLeft() - strokeWidth / 2 - paddingTop, getBottom() - getTop() - strokeWidth / 2 - mTextHeight - paddingBottom, mPaint);//画笔中心在stroke的中心所以这里strokeWidth / 2
        mPaint.setStyle(Paint.Style.FILL);//画完边框再次把模式改为填充，因为接下来要填充的画圆和矩形
        //画圆
        mPaint.setColor(circleColor);
        float left = circleRadius + strokeWidth;
        float top = circleRadius + strokeWidth;
        canvas.drawCircle(left, top, circleRadius, mPaint);
        mPaint.setXfermode(sModes[position]);
        //画矩形
        mPaint.setColor(rectColor);
        float rectRight = circleRadius + rectSize;
        float rectBottom = circleRadius + rectSize;
        canvas.drawRect(left, top, rectRight, rectBottom, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(layer);
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mTextColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mTextColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mTextSize;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mTextSize = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        circleRadius = w / 3;
        rectSize = w * 0.5f;
    }

    public void nextMode() {
        if ((position + 1) > sModes.length - 1) {
            position = 0;
        } else {
            position += 1;
        }
        invalidateTextPaintAndMeasurements();
        invalidate();
    }

    public void preMode() {
        if ((position - 1) < 0) {
            position = sModes.length - 1;
        } else {
            position -= 1;
        }
        invalidateTextPaintAndMeasurements();
        invalidate();
    }
}
