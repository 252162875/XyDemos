package kx.rnd.com.permissionstest.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import java.util.ArrayList;

import kx.rnd.com.permissionstest.R;

/**
 * 侧滑控件
 */
public class MymSwipeMenuLayout extends ViewGroup {
    private final String TAG = "MymSwipeMenuLayout";
    private final ArrayList<View> mMatchParentChildren = new ArrayList<>(1);
    private int mScaledTouchSlop;//一个常量，表示滑动到多少像素时可以开始滑动（自己是这样理解的）Distance in pixels a touch can wander before we think the user is scrolling
    private Scroller mScroller;//滑动器
    private View mLeftView = null;//左边区域View
    private View mRightView = null;//右边区域View
    private View mContentView = null;//内容区域 View
    private int mLeftViewResID;//左边区域View ID
    private int mRightViewResID;//右边区域View ID
    private int mContentViewResID;//内容区域 View ID
    private boolean mCanLeftSwipe = true;//左边区域是否可划出
    private boolean mCanRightSwipe = true;//左边区域是否可划出
    private float mFraction = 0.5f;
    private boolean isSwipeing;
    private PointF mLastP;
    private PointF mFirstP;
    private MarginLayoutParams mContentViewLayoutParams;
    private static MymSwipeMenuLayout mViewCache;
    private static SwipeState mStateCache;

    public MymSwipeMenuLayout(Context context) {
        this(context, null);
    }

    public MymSwipeMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MymSwipeMenuLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }


    /***
     * 初始化
     * @param context
     * @param attrs
     * @param defStyleAttr
     */

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        //创建辅助对象
        ViewConfiguration mViewConfiguration = ViewConfiguration.get(context);
        mScaledTouchSlop = mViewConfiguration.getScaledTouchSlop();
        mScroller = new Scroller(context);
        //获取配置的属性值
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MymSwipeMenuLayout, defStyleAttr, 0);
        try {
            int indexCount = typedArray.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int attr = typedArray.getIndex(i);
                if (attr == R.styleable.MymSwipeMenuLayout_leftView) {
                    mLeftViewResID = typedArray.getResourceId(R.styleable.MymSwipeMenuLayout_leftView, -1);
                } else if (attr == R.styleable.MymSwipeMenuLayout_rightView) {
                    mRightViewResID = typedArray.getResourceId(R.styleable.MymSwipeMenuLayout_rightView, -1);
                } else if (attr == R.styleable.MymSwipeMenuLayout_contentView) {
                    mContentViewResID = typedArray.getResourceId(R.styleable.MymSwipeMenuLayout_contentView, -1);
                } else if (attr == R.styleable.MymSwipeMenuLayout_canLeftSwipe) {
                    mCanLeftSwipe = typedArray.getBoolean(R.styleable.MymSwipeMenuLayout_canLeftSwipe, true);
                } else if (attr == R.styleable.MymSwipeMenuLayout_canRightSwipe) {
                    mCanRightSwipe = typedArray.getBoolean(R.styleable.MymSwipeMenuLayout_canRightSwipe, true);
                } else if (attr == R.styleable.MymSwipeMenuLayout_fraction) {
                    mFraction = typedArray.getFloat(R.styleable.MymSwipeMenuLayout_fraction, 0.5f);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //1、获取childView的个数
        int childCount = getChildCount();
        //参考frameLayout测量代码
        //2、判断我们的MymSwipeMenuLayout的宽高是明确的具体数值还是匹配或者包裹父布局，为什么要处理呢，自行百度
        boolean measureMatchParentChildren = MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY || MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;
        mMatchParentChildren.clear();
        int maxWidth = 0;
        int maxHeight = 0;
        int childState = 0;
        //3、开始遍历childViews进行测量
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            //4、如果view是GONE，那么我们就不需要测量它了，因为它是隐藏的嘛
            if (child.getVisibility() != View.GONE) {
                //5、测量子childView
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
                //6、获取childView中宽的最大值
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin);
                //7、获取childView中高的最大值
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin);
                childState = combineMeasuredStates(childState, child.getMeasuredState());
                //8、如果child中有MATCH_PARENT的，需要再次测量，这里先添加到mMatchParentChildren集合中
                if (measureMatchParentChildren) {
                    if (layoutParams.width == LayoutParams.MATCH_PARENT || layoutParams.height == LayoutParams.MATCH_PARENT) {
                        mMatchParentChildren.add(child);
                    }
                }
            }
        }

        // Check against our minimum height and width
        //9、我们的MymSwipeMenuLayout的宽度和高度还要考虑背景的大小哦
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
        //10、设置我们的MymSwipeMenuLayout的具体宽高
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState), resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));
        //11、MymSwipeMenuLayout的宽高已经知道了，前面MATCH_PARENT的child的值当然我们也能知道了 ，所以这次再次测量它
        childCount = mMatchParentChildren.size();
        if (childCount > 1) {
            for (int i = 0; i < childCount; i++) {
                View child = mMatchParentChildren.get(i);
                final MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
                //12、以下是重新设置child测量所需的MeasureSpec对象
                final int childWidthMeasureSpec;
                if (layoutParams.width == LayoutParams.MATCH_PARENT) {
                    final int width = Math.max(0, getMeasuredWidth()
                            - layoutParams.leftMargin - layoutParams.rightMargin);
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                            width, MeasureSpec.EXACTLY);
                } else {
                    childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, layoutParams.leftMargin + layoutParams.rightMargin, layoutParams.width);
                }
                final int childHeightMeasureSpec;
                if (layoutParams.height == FrameLayout.LayoutParams.MATCH_PARENT) {
                    final int height = Math.max(0, getMeasuredHeight()
                            - layoutParams.topMargin - layoutParams.bottomMargin);
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                            height, MeasureSpec.EXACTLY);
                } else {
                    childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                            layoutParams.topMargin + layoutParams.bottomMargin,
                            layoutParams.height);
                }
                //13、重新测量child
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);//注意：如果对View的宽高进行修改了，不要调用 super.onMeasure( widthMeasureSpec, heightMeasureSpec); 要调用 setMeasuredDimension( widthsize, heightsize); 这个函数。

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childcount = getChildCount();
        int left = 0 + getPaddingLeft();
        int right = 0 + getPaddingRight();
        int top = 0 + getPaddingTop();
        int bottom = 0 + getPaddingBottom();
        //1、根据我们配置的id获取对象的View对象，里面我们自动帮用户设置了setClickable(true);当然你也可以让用户自己去配置，这样做是为了响应touch事件
        for (int i = 0; i < childcount; i++) {
            View child = getChildAt(i);
            if (mLeftView == null && child.getId() == mLeftViewResID) {
                Log.i(TAG, "找到左边按钮view");
                mLeftView = child;
                mLeftView.setClickable(true);
            } else if (mRightView == null && child.getId() == mRightViewResID) {
                Log.i(TAG, "找到右边按钮view");
                mRightView = child;
                mRightView.setClickable(true);
            } else if (mContentView == null && child.getId() == mContentViewResID) {
                Log.i(TAG, "找到内容View");
                mContentView = child;
                mContentView.setClickable(true);
            }
        }

        //2、布局contentView，contentView是放在屏幕中间的
        if (mContentView != null) {
            mContentViewLayoutParams = (MarginLayoutParams) mContentView.getLayoutParams();
            int cTop = top + mContentViewLayoutParams.topMargin;
            int cLeft = left + mContentViewLayoutParams.leftMargin;
            int cRight = left + mContentViewLayoutParams.leftMargin + mContentView.getMeasuredWidth();
            int cBottom = cTop + mContentView.getMeasuredHeight();
            mContentView.layout(cLeft, cTop, cRight, cBottom);
        }

        if (mLeftView != null) {
            MarginLayoutParams leftViewLayoutParams = (MarginLayoutParams) mLeftView.getLayoutParams();
            int lTop = top + leftViewLayoutParams.topMargin;
            int lLeft = 0 - mLeftView.getMeasuredWidth() + leftViewLayoutParams.leftMargin + leftViewLayoutParams.rightMargin;
            int lRight = 0 - leftViewLayoutParams.rightMargin;
            int lBottom = lTop + mLeftView.getMeasuredHeight();
            mLeftView.layout(lLeft, lTop, lRight, lBottom);
        }

        if (mRightView != null) {
            MarginLayoutParams rightViewLayoutParams = (MarginLayoutParams) mRightView.getLayoutParams();
            int lTop = top + rightViewLayoutParams.topMargin;
            int lLeft = mContentView.getRight() + mContentViewLayoutParams.rightMargin + rightViewLayoutParams.leftMargin;
            int lRight = lLeft + mRightView.getMeasuredWidth();
            int lBottom = lTop + mRightView.getMeasuredHeight();
            mRightView.layout(lLeft, lTop, lRight, lBottom);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "getScrollX()=" + getScrollX() + "\r\nmLeftView.getLeft()=" + mLeftView.getLeft() + "\r\nmRightView.getRight()=" + mRightView.getRight() + "\r\ngetLeft()=" + getLeft());
                //经过测试，getScrollX表示整个可是范围相对于初始化时（初始化getScrollX为0）移动的距离（可视范围右移的话感觉就是内容左移，此时getScrollX是负值；相反亦然）
                isSwipeing = false;
                if (mLastP == null) {
                    mLastP = new PointF();
                }
                mLastP.set(ev.getRawX(), ev.getRawY());
                if (mFirstP == null) {
                    mFirstP = new PointF();
                }
                mFirstP.set(ev.getRawX(), ev.getRawY());
                if (mViewCache != null) {
                    if (mViewCache != this) {
                        mViewCache.handlerSwipeMenu(SwipeState.CLOSE);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                isSwipeing = true;
                float distanceX = mLastP.x - ev.getRawX();
                float distanceY = mLastP.y - ev.getRawY();
                if (Math.abs(distanceY) > mScaledTouchSlop * 2) {
                    break;
                }
                //当处于水平滑动时，禁止父类拦截
                if (Math.abs(distanceX) > mScaledTouchSlop * 2 || Math.abs(getScrollX()) > mScaledTouchSlop * 2) {
                    requestDisallowInterceptTouchEvent(true);
                }
                scrollBy((int) (distanceX), 0);//滑动使用scrollBy

                //越界修正
                if (getScrollX() < 0) {
                    if (!mCanRightSwipe || mLeftView == null) {
                        scrollTo(0, 0);
                    } else {//左滑
                        if (getScrollX() < mLeftView.getLeft()) {
                            scrollTo(mLeftView.getLeft(), 0);
                        }

                    }
                } else if (getScrollX() > 0) {
                    if (!mCanLeftSwipe || mRightView == null) {
                        scrollTo(0, 0);
                    } else {
                        if (getScrollX() > mRightView.getRight() - mContentView.getRight() - mContentViewLayoutParams.rightMargin) {
                            scrollTo(mRightView.getRight() - mContentView.getRight() - mContentViewLayoutParams.rightMargin, 0);
                        }
                    }
                }

                mLastP.set(ev.getRawX(), ev.getRawY());

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                SwipeState result = isShouldOpen(getScrollX());
                handlerSwipeMenu(result);
                break;

            default:
                break;

        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Log.d(TAG, "dispatchTouchEvent() called with: " + "ev = [" + event + "]");

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                //对左边界进行处理
                float distance = mLastP.x - event.getRawX();
                if (Math.abs(distance) > mScaledTouchSlop) {
                    // 当手指拖动值大于mScaledTouchSlop值时，认为应该进行滚动，拦截子控件的事件
                    return true;
                }
                break;

            }

        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        //判断Scroller是否执行完毕：
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //通知View重绘-invalidate()->onDraw()->computeScroll()
            invalidate();
        }
    }

    /**
     * 根据当前的scrollX的值判断松开手后应处于何种状态
     *
     * @param scrollX
     * @return
     */
    private SwipeState isShouldOpen(int scrollX) {
        if (getScrollX() < 0 && mLeftView != null) {
            //➡滑动
            //获得leftView的测量长度
            if (Math.abs(mLeftView.getWidth() * mFraction) < Math.abs(getScrollX())) {
                return SwipeState.LEFTOPEN;
            }

        } else if (getScrollX() > 0 && mRightView != null) {
            //⬅️滑动
            if (Math.abs(mRightView.getWidth() * mFraction) < Math.abs(getScrollX())) {
                return SwipeState.RIGHTOPEN;
            }

        }
        return SwipeState.CLOSE;
    }

    /**
     * 自动设置状态
     *
     * @param state
     */
    private void handlerSwipeMenu(SwipeState state) {

        //mScroller滑动的时候其实是滑动父布局的可视范围，所以这里滑动的值看起来好像是相反的（其实动的是父布局）
        if (state == SwipeState.LEFTOPEN) {
            mScroller.startScroll(getScrollX(), 0, mLeftView.getLeft() - getScrollX(), 0);//getScrollX,屏幕原点X坐标减去调用视图左上角X坐标,源码解释是：视图的显示部分的左边缘，以像素为单位
            mViewCache = this;
            mStateCache = state;
        } else if (state == SwipeState.RIGHTOPEN) {
            mViewCache = this;
            mScroller.startScroll(getScrollX(), 0, mRightView.getRight() - mContentView.getRight() - mContentViewLayoutParams.rightMargin - getScrollX(), 0);
            mStateCache = state;
        } else {
            mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
            mViewCache = null;
            mStateCache = null;

        }
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (this == mViewCache) {
            mViewCache.handlerSwipeMenu(SwipeState.CLOSE);
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this == mViewCache) {
            mViewCache.handlerSwipeMenu(mStateCache);
        }
    }

    public void resetStatus() {
        if (mViewCache != null) {
            if (mStateCache != null && mStateCache != SwipeState.CLOSE && mScroller != null) {
                mScroller.startScroll(mViewCache.getScrollX(), 0, -mViewCache.getScrollX(), 0);
                mViewCache.invalidate();
                mViewCache = null;
                mStateCache = null;
            }
        }
    }


    public float getFraction() {
        return mFraction;
    }

    public void setFraction(float mFraction) {
        this.mFraction = mFraction;
    }

    public boolean isCanLeftSwipe() {
        return mCanLeftSwipe;
    }

    public void setCanLeftSwipe(boolean mCanLeftSwipe) {
        this.mCanLeftSwipe = mCanLeftSwipe;
    }

    public boolean isCanRightSwipe() {
        return mCanRightSwipe;
    }

    public void setCanRightSwipe(boolean mCanRightSwipe) {
        this.mCanRightSwipe = mCanRightSwipe;
    }

    public static MymSwipeMenuLayout getViewCache() {
        return mViewCache;
    }


    public static SwipeState getStateCache() {
        return mStateCache;
    }
}
