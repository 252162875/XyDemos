package kx.rnd.com.permissionstest.activity.customview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kx.rnd.com.permissionstest.R;

public class CustomViewActivity extends AppCompatActivity {

    @BindView(R.id.btn_xfremode_test)
    Button btnXfremodeTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_xfremode_test, R.id.btn_customview_day_one, R.id.btn_canvas, R.id.btn_path, R.id.btn_bezier})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_xfremode_test:
                Intent xfreModeTestIntent = new Intent(CustomViewActivity.this, XfreModeTestActivity.class);
                startActivity(xfreModeTestIntent);
                break;
            case R.id.btn_customview_day_one:
                Intent customViewOneIntent = new Intent(CustomViewActivity.this, CustomViewOneActivity.class);
                startActivity(customViewOneIntent);
                break;
            case R.id.btn_canvas:
                Intent customViewTwoIntent = new Intent(CustomViewActivity.this, CustomViewTwoActivity.class);
                startActivity(customViewTwoIntent);
                break;
            case R.id.btn_path:
                Intent pathTestActivityIntent = new Intent(CustomViewActivity.this, PathTestActivity.class);
                startActivity(pathTestActivityIntent);
                break;
            case R.id.btn_bezier:
                Intent bezierActivityIntent = new Intent(CustomViewActivity.this, BezierTestActivity.class);
                startActivity(bezierActivityIntent);
                break;
        }
    }
}
