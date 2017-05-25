package kx.rnd.com.permissionstest.activity.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kx.rnd.com.permissionstest.R;
import kx.rnd.com.permissionstest.view.BezierTest3View;

public class BezierTestActivity extends AppCompatActivity {

    @BindView(R.id.btv_xin)
    BezierTest3View btvXin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btv_xin)
    public void onViewClicked() {
        btvXin.rePlay();
    }
}
