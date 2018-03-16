package kx.rnd.com.permissionstest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kx.rnd.com.permissionstest.R;
import kx.rnd.com.permissionstest.mvp.SingleMvpActivity;
import kx.rnd.com.permissionstest.mvp.base.BaseMvpActivity;
import kx.rnd.com.permissionstest.mvpdagger.MvpPlusDaggerActivity;

public class MvpTestActivity extends BaseMvpActivity {


    @BindView(R.id.tv_mvp)
    TextView tvMvp;
    @BindView(R.id.tv_mvp_dagger)
    TextView tvMvpDagger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp_test);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.tv_mvp, R.id.tv_mvp_dagger})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_mvp:
                Intent intent = new Intent(this, SingleMvpActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_mvp_dagger:
                Intent intent2 = new Intent(this, MvpPlusDaggerActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
