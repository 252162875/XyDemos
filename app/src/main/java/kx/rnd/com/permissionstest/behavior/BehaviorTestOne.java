package kx.rnd.com.permissionstest.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BehaviorTestOne extends CoordinatorLayout.Behavior<TextView> {
    public BehaviorTestOne(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //告知监听的dependency是Button
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, TextView child, View dependency) {
        return dependency instanceof Button;
    }

    //当 dependency(Button)变化的时候，可以对child(TextView)进行操作
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, TextView child, View dependency) {
        child.setX(dependency.getX() + 200);
        child.setY(dependency.getY() + 200);
        child.setText(dependency.getX() + "," + dependency.getY());
        return true;
    }
}