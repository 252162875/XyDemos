package kx.rnd.com.permissionstest.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kx.rnd.com.permissionstest.R;

public class SecondActivity extends Activity {

    @BindView(R.id.btn_buletooth)
    Button btnBuletooth;
    @BindView(R.id.btn_muiltdownload)
    Button btnMuiltdownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);
    }


    public void autoPairBlueTooth() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();


        if (!defaultAdapter.isEnabled()) {
            defaultAdapter.enable();//异步的，不会等待结果，直接返回。
        } else {
            defaultAdapter.startDiscovery();
        }
    }

    @OnClick({R.id.btn_buletooth, R.id.btn_muiltdownload, R.id.btn_verticalscroll, R.id.btn_polygon, R.id.btn_watch, R.id.btn_behavior, R.id.btn_okhttp_retrofit, R.id.btn_svg_test, R.id.btn_custom_view, R.id.btn_baidu_map})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_buletooth:
                autoPairBlueTooth();
                break;
            case R.id.btn_muiltdownload:
                Intent downLoadIntent = new Intent(SecondActivity.this, MuiltDownLoadActivity.class);
                startActivity(downLoadIntent);
                break;
            case R.id.btn_verticalscroll:
                Intent verticalScrollIntent = new Intent(SecondActivity.this, VerticalScrollActivity.class);
                startActivity(verticalScrollIntent);
                break;
            case R.id.btn_polygon:
                Intent polygonIntent = new Intent(SecondActivity.this, PolygonActivity.class);
                startActivity(polygonIntent);
                break;
            case R.id.btn_watch:
                Intent watchIntent = new Intent(SecondActivity.this, WatchActivity.class);
                startActivity(watchIntent);
                break;
            case R.id.btn_behavior:
                Intent behaviorIntent = new Intent(SecondActivity.this, BehaviorActivity.class);
                startActivity(behaviorIntent);
                break;
            case R.id.btn_okhttp_retrofit:
                Intent okhtttpAndRetrofitIntent = new Intent(SecondActivity.this, OkhtttpAndRetrofitActivity.class);
                startActivity(okhtttpAndRetrofitIntent);
                break;
            case R.id.btn_svg_test:
                Intent svgIntent = new Intent(SecondActivity.this, SvgTestActivity.class);
                startActivity(svgIntent);
                break;
            case R.id.btn_custom_view:
                Intent customViewIntent = new Intent(SecondActivity.this, CustomViewActivity.class);
                startActivity(customViewIntent);
                break;
            case R.id.btn_baidu_map:
                Intent baiduMapIntent = new Intent(SecondActivity.this, BaiduMapActivity.class);
                startActivity(baiduMapIntent);
                break;
        }
    }
}
