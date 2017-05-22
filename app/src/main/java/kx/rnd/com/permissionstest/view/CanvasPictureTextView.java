package kx.rnd.com.permissionstest.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.drawable.PictureDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import kx.rnd.com.permissionstest.R;

/**
 * TODO: document your custom view class.
 */
public class CanvasPictureTextView extends View {
    private static final int ANIM_NULL = 0;         //动画状态-没有
    private static final int ANIM_CHECK = 1;        //动画状态-开启
    private static final int ANIM_UNCHECK = 2;      //动画状态-结束

    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private Picture picture;
    private Context context;
    private boolean hasRecorded = false;
    private Bitmap okBitmap;
    private int animCurrentPage = -1;       // 当前页码
    private int animMaxPage = 13;           // 总页数
    private int animDuration = 500;         // 动画时长
    private int animState = ANIM_NULL;      // 动画状态
    private boolean isCheck = false;        // 是否只选中状态
    private Handler mHandler;

    public CanvasPictureTextView(Context context) {
        super(context);
        this.context = context;
        init(null, 0);
    }

    public CanvasPictureTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs, 0);
    }

    public CanvasPictureTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        okBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.checkmark);
        mPaint = new Paint();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (animCurrentPage < animMaxPage && animCurrentPage >= 0) {
                    invalidate();
                    if (animState == ANIM_NULL)
                        return;
                    if (animState == ANIM_CHECK) {

                        animCurrentPage++;
                    } else if (animState == ANIM_UNCHECK) {
                        animCurrentPage--;
                    }

                    this.sendEmptyMessageDelayed(0, animDuration / animMaxPage);
                    Log.e("AAA", "Count=" + animCurrentPage);
                } else {
                    if (isCheck) {
                        animCurrentPage = animMaxPage - 1;
                    } else {
                        animCurrentPage = -1;
                    }
                    invalidate();
                    animState = ANIM_NULL;
                }
            }
        };
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
        //注意：在使用Picture之前请关闭硬件加速，以免引起不必要的问题(https://github.com/GcsSloop/AndroidNote/issues/7)
//        picture.draw(canvas);
//        canvas.drawPicture(picture);
        //绘制的内容根据选区进行了缩放
//        canvas.drawPicture(picture, new RectF(0, 0, picture.getWidth(), 200));
        PictureDrawable pictureDrawable = new PictureDrawable(picture);
        // 设置绘制区域 -- 注意此处所绘制的实际内容不会缩放
        pictureDrawable.setBounds(0, 0, picture.getWidth(), 500);
        pictureDrawable.draw(canvas);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.share);
        canvas.drawBitmap(bitmap, new Matrix(), mPaint);
        canvas.drawBitmap(bitmap, 200, 200, mPaint);


        // 得出图像边长
        int sideLength = okBitmap.getHeight();

        // 将画布坐标系移动到画布中央
        canvas.translate(mWidth / 2, mHeight / 2);
        // 绘制背景圆形
        canvas.drawCircle(0, 0, 240, mPaint);
        // 指定图片绘制区域(左上角的四分之一)
        Rect src = new Rect(sideLength * animCurrentPage, 0, sideLength * (animCurrentPage + 1), sideLength);
        // 指定图片在屏幕上显示的区域
        Rect dst = new Rect(-200, -200, 200, 200);
        // 绘制图片
        canvas.drawBitmap(okBitmap, src, dst, null);
    }

    /**
     * 选择
     */
    public void check() {
        if (animState != ANIM_NULL || isCheck)
            return;
        animState = ANIM_CHECK;
        animCurrentPage = 0;
        mHandler.sendEmptyMessageDelayed(0, animDuration / animMaxPage);
        isCheck = true;
    }

    /**
     * 取消选择
     */
    public void unCheck() {
        if (animState != ANIM_NULL || (!isCheck))
            return;
        animState = ANIM_UNCHECK;
        animCurrentPage = animMaxPage - 1;
        mHandler.sendEmptyMessageDelayed(0, animDuration / animMaxPage);
        isCheck = false;
    }

    /**
     * 设置动画时长
     *
     * @param animDuration
     */
    public void setAnimDuration(int animDuration) {
        if (animDuration <= 0)
            return;
        this.animDuration = animDuration;
    }

    /**
     * 设置背景圆形颜色
     *
     * @param color
     */
    public void setBackgroundColor(int color) {
        mPaint.setColor(color);
    }

    private void record() {
        picture = new Picture();
        Canvas canvas1 = picture.beginRecording(mWidth, mHeight);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        canvas1.translate(mWidth / 2, mHeight / 2);
        canvas1.drawCircle(0, 0, 200, paint);
        picture.endRecording();
        hasRecorded = true;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        if (!hasRecorded) {
            record();
        }
    }
}
