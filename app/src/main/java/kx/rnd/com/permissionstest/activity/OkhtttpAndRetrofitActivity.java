package kx.rnd.com.permissionstest.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kx.rnd.com.permissionstest.R;
import kx.rnd.com.permissionstest.bean.DouBanBean;
import kx.rnd.com.permissionstest.interfaces.DouBanService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OkhtttpAndRetrofitActivity extends Activity {

    @BindView(R.id.btn_only_retrofit)
    Button btnOnlyRetrofit;
    @BindView(R.id.tv_response)
    TextView tvResponse;
    @BindView(R.id.btn_retrofit_rxjava)
    Button btnRetrofitRxjava;
    private String baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhtttp_and_retrofit);
        baseUrl = "https://api.douban.com/v2/movie/";
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_only_retrofit, R.id.btn_retrofit_rxjava})
    public void onClick(View view) {
        tvResponse.setText("清空操作");
        switch (view.getId()) {
            case R.id.btn_only_retrofit:
                //进行网络请求（只用retrofit）
                Toast.makeText(this, "进行网络请求（只用retrofit）", Toast.LENGTH_SHORT).show();
                getMovie1();
                break;
            case R.id.btn_retrofit_rxjava:
                //进行网络请求（retrofit+rxjava(无封装)）
                Toast.makeText(this, "进行网络请求（retrofit+rxjava(无封装)）", Toast.LENGTH_SHORT).show();
                getMovie2();
                break;
        }
    }

    //网络请求（只用retrofit）
    private void getMovie1() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DouBanService douBanService = retrofit.create(DouBanService.class);
        Call<DouBanBean> call = douBanService.getTopMovie1(0, 10);
        call.enqueue(new Callback<DouBanBean>() {
            @Override
            public void onResponse(Call<DouBanBean> call, Response<DouBanBean> response) {
                tvResponse.setText(response.body().getTitle() + "\n" + "1本页总数" + response.body().getCount() + "\n" + "1开始位置" + response.body().getStart() + "\n" + "1总数量" + response.body().getTotal() + "\n" + "1得到数据数量" + response.body().getSubjects().size());
            }

            @Override
            public void onFailure(Call<DouBanBean> call, Throwable t) {
                tvResponse.setText(t.getMessage());
            }
        });
    }

    //网络请求（retrofit+rxjava(无封装)）
    private void getMovie2() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        DouBanService douBanService = retrofit.create(DouBanService.class);
        douBanService.getTopMovie2(0, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DouBanBean>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(OkhtttpAndRetrofitActivity.this, "请求完成了！！！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        tvResponse.setText(e.getMessage());
                    }

                    @Override
                    public void onNext(DouBanBean douBanBean) {
                        tvResponse.setText(douBanBean.getTitle() + "\n" + "2本页总数" + douBanBean.getCount() + "\n" + "2开始位置" + douBanBean.getStart() + "\n" + "2总数量" + douBanBean.getTotal() + "\n" + "2得到数据数量" + douBanBean.getSubjects().size());
                    }
                });
    }

}
