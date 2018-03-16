package kx.rnd.com.permissionstest.mvpdagger;

import android.os.Bundle;
import android.widget.TextView;

import java.util.TreeMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import kx.rnd.com.permissionstest.R;
import kx.rnd.com.permissionstest.mvpdagger.base.BaseMvpActivity;
import kx.rnd.com.permissionstest.mvpdagger.component.DaggerMvpPlusDaggerComponent;
import kx.rnd.com.permissionstest.mvpdagger.presenter.MvpPresenter;
import kx.rnd.com.permissionstest.mvpdagger.view.MvpView;

public class MvpPlusDaggerActivity extends BaseMvpActivity implements MvpView {
    @Inject
    MvpPresenter mvpPresenter;
    @BindView(R.id.tv_username)
    TextView tvUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp_plus_dagger);
        ButterKnife.bind(this);
        DaggerMvpPlusDaggerComponent.builder().build().inject(this);
        mvpPresenter.attachView(this);
        TreeMap<String, String> map = new TreeMap<>();
        map.put("uid", "normal");
        mvpPresenter.getData(map);
    }

    @Override
    public void showData(String data) {
        tvUsername.setText(data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mvpPresenter.detachView();
    }
}
