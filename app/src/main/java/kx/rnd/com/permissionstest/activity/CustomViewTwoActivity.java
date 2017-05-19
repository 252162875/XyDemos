package kx.rnd.com.permissionstest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kx.rnd.com.permissionstest.R;

public class CustomViewTwoActivity extends AppCompatActivity {

    @BindView(R.id.btn_translate_scale)
    Button btn_translate_scale;
    @BindView(R.id.btn_rotate)
    Button btn_rotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view_two);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_translate_scale, R.id.btn_rotate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_translate_scale:
                Intent tanslateIntent = new Intent(CustomViewTwoActivity.this, CanvasTranslateScaleActivity.class);
                startActivity(tanslateIntent);
                break;
            case R.id.btn_rotate:
                Intent scaleIntent = new Intent(CustomViewTwoActivity.this, CanvasRotateActivity.class);
                startActivity(scaleIntent);
                break;
        }
    }
}
