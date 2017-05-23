package kx.rnd.com.permissionstest.activity.customview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kx.rnd.com.permissionstest.R;
import kx.rnd.com.permissionstest.view.CanvasPictureTextView;

public class CanvasPictureTextActivity extends AppCompatActivity {

    @BindView(R.id.canvas_pt)
    CanvasPictureTextView canvasPt;
    @BindView(R.id.btn_check)
    Button btnCheck;
    @BindView(R.id.btn_uncheck)
    Button btnUncheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_picture_text);
        ButterKnife.bind(this);
        canvasPt.setBackgroundColor(Color.BLUE);
        canvasPt.setAnimDuration(300);
    }

    @OnClick({R.id.btn_check, R.id.btn_uncheck, R.id.canvas_pt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_check:
                canvasPt.check();
                break;
            case R.id.btn_uncheck:
                canvasPt.unCheck();
                break;
            case R.id.canvas_pt:
                if (canvasPt.isChecked()) {
                    canvasPt.unCheck();
                } else {
                    canvasPt.check();
                }
                break;
        }
    }
}
