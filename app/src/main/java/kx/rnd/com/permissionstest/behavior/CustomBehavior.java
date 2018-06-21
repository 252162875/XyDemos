package kx.rnd.com.permissionstest.behavior;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import kx.rnd.com.permissionstest.R;


//泛型为child类型
public class CustomBehavior extends CoordinatorLayout.Behavior<ImageView> {
    private Context mContext;
    //头像的最终大小
    private float mCustomFinalHeight;

    //最终头像的Y
    private float mFinalAvatarY;

    private float mStartAvatarY;

    private float mStartAvatarX;

    private int mAvatarMaxHeight;

    private LinearInterpolator interpolator = new LinearInterpolator();

    public CustomBehavior(Context context, AttributeSet attrs) {
        mContext = context;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomBehavior);
            //获取缩小以后的大小
            mCustomFinalHeight = a.getDimension(R.styleable.CustomBehavior_finalHeight, 0);
            a.recycle();
        }
    }


    // 如果dependency为Toolbar
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency) {
        return dependency.getId() == R.id.view;
    }


    //当dependency变化的时候调用
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency) {
        //初始化属性
        //init(child, dependency);
        mFinalAvatarY = dependency.getHeight() / 2;
        if (mStartAvatarY == 0) {
            mStartAvatarY = dependency.getY();
        }
        if (mStartAvatarX == 0) {
            mStartAvatarX = child.getX();
        }

        if (mAvatarMaxHeight == 0) {
            mAvatarMaxHeight = child.getHeight();
        }


        //child.setY(dependency.getY());

        //让ImageView跟随toolbar垂直移动

        float y = dependency.getY();
        int height = dependency.getHeight();
        Log.e("ASDASD","y = "+y);
        Log.e("ASDASD","height = "+height);
        child.setY(y + height / 2 - mCustomFinalHeight / 2);
//        float percent = dependency.getY() / mStartAvatarY;
        float percent = dependency.getY() / mStartAvatarY;
        Log.e("PERCENT", percent + "");

        //float x = mStartAvatarX*(1+percent);
        float x = mStartAvatarX * (1 + interpolator.getInterpolation(percent));

        Log.e("wing", "started x " + mStartAvatarX + " currentX " + x);

        if (x > mStartAvatarX + ((mAvatarMaxHeight - mCustomFinalHeight)) / 2) {
            child.setX(x);
        } else {
            child.setX(mStartAvatarX + ((mAvatarMaxHeight - mCustomFinalHeight)) / 2);
        }

        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        layoutParams.height = (int) ((mAvatarMaxHeight - mCustomFinalHeight) * percent + mCustomFinalHeight);
        layoutParams.width = (int) ((mAvatarMaxHeight - mCustomFinalHeight) * percent + mCustomFinalHeight);
        child.setLayoutParams(layoutParams);

        return true;
    }


}
