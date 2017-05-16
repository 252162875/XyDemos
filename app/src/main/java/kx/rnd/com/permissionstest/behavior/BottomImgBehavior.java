package kx.rnd.com.permissionstest.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import kx.rnd.com.permissionstest.R;

/**
 *
 */
public class BottomImgBehavior extends CoordinatorLayout.Behavior<ImageView> {
    private int mFrameMaxHeight = 100;
    private int mStartY;
    private float imgStartX;

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency) {
        return dependency.getId() == R.id.view;
    }

    public BottomImgBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child,
                                          View dependency) {
        //记录开始的Y坐标  也就是toolbar起始Y坐标
        if (mStartY == 0) {
            mStartY = (int) dependency.getY();
        }
        if (imgStartX == 0) {
            imgStartX = child.getX();
        }

        //计算toolbar从开始移动到最后的百分比
        float percent = dependency.getY() / mStartY;

        //改变child的坐标(从消失，到可见)
        child.setX(imgStartX + child.getWidth() * (1 - percent));
        return true;
    }
}
