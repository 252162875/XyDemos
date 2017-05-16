package kx.rnd.com.permissionstest.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import kx.rnd.com.permissionstest.R;

public class BehaviorTestOneActivity extends AppCompatActivity {
    @BindView(R.id.btn)
    Button btn;
    private int titleBarHeight;
    private int statusBarHeight;
    //以下两个值代表手指按下时控件的坐标
    float x = 0.0f;
    float y = 0.0f;
    //以下两个值代表手指按下时手指的坐标
    float xx = 0.0f;
    float yy = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior_test1);
        ButterKnife.bind(this);
        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = v.getX();
                        y = v.getY();
                        xx = event.getRawX();
                        yy = event.getRawY();
                        Toast.makeText(BehaviorTestOneActivity.this, "x === " + x + "\n" + "y === " + y + "\n" + "event.getRawX() === " + event.getRawX() + "\n" + "event.getRawY() === " + event.getRawY(), Toast.LENGTH_LONG).show();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        v.setX(event.getRawX() - Math.abs(xx - x));
                        v.setY(event.getRawY() - Math.abs(yy - y) + v.getHeight() + v.getPaddingTop() - titleBarHeight - statusBarHeight);
                        break;
                    case MotionEvent.ACTION_UP:
                        x = v.getX();
                        y = v.getY();
                        Toast.makeText(BehaviorTestOneActivity.this, "x === " + x + "\n" + "y === " + y + "\n" + "event.getRawX() === " + event.getRawX() + "\n" + "event.getRawY() === " + event.getRawY(), Toast.LENGTH_LONG).show();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        // 状态栏高度
        statusBarHeight = frame.top;
        View v = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        int contentTop = v.getTop();

        // statusBarHeight是上面所求的状态栏的高度
        titleBarHeight = contentTop - statusBarHeight;

//        textView = (TextView) findViewById(R.id.textView1);
//        textView.setText("标题栏的高度" + Integer.toString(titleBarHeight) + "\n"
//                + "标题栏高度" + statusBarHeight + "\n" + "视图的宽度" + v.getWidth()
//                + "\n" + "视图的高度（不包含状态栏和标题栏）" + v.getHeight());
    }
}
