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

    @OnClick({R.id.btn_360scroll, R.id.btn_product_detail, R.id.btn_xfremode_test, R.id.btn_customview_day_one, R.id.btn_canvas, R.id.btn_path, R.id.btn_bezier, R.id.btn_swipe, R.id.btn_floatdecoration})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_360scroll:
                Intent scroll360Intent = new Intent(CustomViewActivity.this, Scroll360Activity.class);
                startActivity(scroll360Intent);
                break;
            case R.id.btn_product_detail:
                Intent productDetailIntent = new Intent(CustomViewActivity.this, ProductDetailActivity.class);
                startActivity(productDetailIntent);
                break;
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
            case R.id.btn_swipe:
                Intent mymSwipeMenuLayoutActivityIntent = new Intent(CustomViewActivity.this, MymSwipeMenuLayoutActivity.class);
                startActivity(mymSwipeMenuLayoutActivityIntent);
                break;
            case R.id.btn_floatdecoration:
                Intent floatDecorationActivityIntent = new Intent(CustomViewActivity.this, FloatDecorationActivity.class);
                startActivity(floatDecorationActivityIntent);
                break;
        }
    }
}
