package kx.rnd.com.permissionstest.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.OverScroller;

public class NestedScrollLayout extends LinearLayout implements NestedScrollingChild {
    /**
     * 只能有三个以及childView，且第三个childView可以滑动
     * 其他两个可以用容器装其他view
     */
    private View mTop;
    private View mNav;
    private View mScrollView;
    private int mTopViewHeight;
    private boolean aaa;
    private boolean isFling;
    private OverScroller mScroller;
    private NestedScrollingParentHelper mNestedScrollingParentHelper;
    private CanNotScrollDownListener listener;

    public NestedScrollLayout(Context context) {
        super(context);
    }

    public NestedScrollLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        mScroller = new OverScroller(context);
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
    }

    public NestedScrollLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        isFling = false;
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }


    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (aaa) {
            return;
        }
        boolean hiddenTop = dy > 0 && getScrollY() < mTopViewHeight;
        boolean showTop = dy < 0 && getScrollY() > 0 && !ViewCompat.canScrollVertically(target, -1);
        if (hiddenTop || showTop) {
            scrollBy(0, dy);
            consumed[1] = dy;
            if (!isFling && this.listener != null) {
                if (getScrollY() == 0) {
                    this.listener.canNotScrollDown(true);
                } else {
                    this.listener.canNotScrollDown(false);
                }
            }
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (aaa) {
            return false;
        }
        int scrollY = getScrollY();
        if (scrollY >= mTopViewHeight || scrollY == 0) {
            return false;
        } else {
            fling((int) velocityY);
            return true;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onStopNestedScroll(View child) {
        mNestedScrollingParentHelper.onStopNestedScroll(child);
    }


    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //不限制顶部的高度
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int a = getMeasuredHeight();
        int b = mTop.getMeasuredHeight();
        int c = mNav.getMeasuredHeight();
        int d = mScrollView.getMeasuredHeight();
        ViewGroup.LayoutParams params = mScrollView.getLayoutParams();
        params.height = getMeasuredHeight() - mNav.getMeasuredHeight();
        int height = params.height;
        if (height + mTop.getMeasuredHeight() + mNav.getMeasuredHeight() < a) {
            aaa = true;
            mScrollView.measure(widthMeasureSpec, height);
        } else {
            aaa = false;
            mScrollView.measure(widthMeasureSpec, heightMeasureSpec - mNav.getMeasuredHeight());
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = mTop.getMeasuredHeight();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTop = getChildAt(0);
        mNav = getChildAt(1);
        mScrollView = getChildAt(2);
    }

    public void fling(int velocityY) {
        isFling = true;
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, mTopViewHeight);
        invalidate();
        if (this.listener != null) {
            if (mScroller.getFinalY() == 0) {
                this.listener.canNotScrollDown(true);
            } else {
                this.listener.canNotScrollDown(false);
            }
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mTopViewHeight) {
            y = mTopViewHeight;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }

    public void setCanNotScrollDownListener(CanNotScrollDownListener listener) {
        this.listener = listener;
    }

    public interface CanNotScrollDownListener {
        void canNotScrollDown(boolean z);
    }
}