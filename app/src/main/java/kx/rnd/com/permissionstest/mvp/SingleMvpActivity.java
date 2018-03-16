package kx.rnd.com.permissionstest.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import kx.rnd.com.permissionstest.R;
import kx.rnd.com.permissionstest.mvp.base.BaseMvpActivity;
import kx.rnd.com.permissionstest.mvp.presenter.MvpPresenter;
import kx.rnd.com.permissionstest.mvp.view.MvpView;

/**
 * author: 梦境缠绕
 * created on: 2018/3/15 0015 11:30
 * description:
 */

public class SingleMvpActivity extends BaseMvpActivity implements MvpView {
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_psw)
    TextView tvPsw;
    private MvpPresenter mvpPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_mvp);
        ButterKnife.bind(this);
        mvpPresenter = new MvpPresenter();
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
