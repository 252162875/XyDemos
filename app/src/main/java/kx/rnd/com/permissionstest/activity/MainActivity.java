package kx.rnd.com.permissionstest.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kx.rnd.com.permissionstest.R;

public class MainActivity extends Activity implements LocationSource, AMapLocationListener {
    final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;
    String mMobile = "";
    //    @BindView(R.id.tv_text)
    TextView tvText;
    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.btn_nextpage)
    Button btnNextpage;
    private AMap aMap;
    private boolean isFirst = true;
    OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;
    private MarkerOptions markerOption;
    private Marker marker;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tvText = (TextView) findViewById(R.id.tv_text);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);
        //声明mLocationOption对象
        AMapLocationClient mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        //启动定位
        mlocationClient.startLocation();
        //初始化地图变量
        if (aMap == null) {
            aMap = mMapView.getMap();
            aMap.setMapType(AMap.MAP_TYPE_NIGHT);
            aMap.setTrafficEnabled(true);
            UiSettings uiSettings = aMap.getUiSettings();
            uiSettings.setCompassEnabled(true);//指南针
            aMap.setLocationSource(this);// 设置定位监听
            uiSettings.setMyLocationButtonEnabled(true); // 显示默认的定位按钮
            aMap.setMyLocationEnabled(true);// 可触发定位并显示定位层
            uiSettings.setAllGesturesEnabled(true);
            LatLng latLng = new LatLng(39.906901, 116.397972);
            markerOption = new MarkerOptions();
            LatLng latLng1 = new LatLng(34.341568, 108.940174);
            markerOption.position(latLng1);
            markerOption.title("西安市").snippet("西安市：34.341568, 108.940174");

            markerOption.draggable(true);
            markerOption.icon(

                    BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(),
                                    R.mipmap.ic_launcher)));
            // 将Marker设置为贴地显示，可以双指下拉看效果
            markerOption.setFlat(true);
            marker = aMap.addMarker(markerOption);
            AMap.OnMarkerClickListener listener = new AMap.OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(Marker arg0) {
                    // TODO Auto-generated method stub
                    Toast.makeText(MainActivity.this, "onMarkerClick", Toast.LENGTH_SHORT)
                            .show();
                    return false;
                }
            };

//绑定标注点击事件
            aMap.setOnMarkerClickListener(listener);
        }
//        ButterKnife.bind(this);
        tvText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCall("13109257723");
            }
        });
    }

    private void callDirectly(String mobile) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.CALL");
        intent.setData(Uri.parse("tel:" + mobile));
        startActivity(intent);
    }


    public void onCall(String mobile) {
        this.mMobile = mobile;
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_CALL_PHONE);
                return;
            } else {
                //上面已经写好的拨号方法
                callDirectly(mobile);
            }
        } else {
            //上面已经写好的拨号方法
            callDirectly(mobile);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    callDirectly(mMobile);
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "CALL_PHONE Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        //然后可以移动到定位点,使用animateCamera就有动画效果
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
    }

    @Override
    public void deactivate() {
        Toast.makeText(this, "deactivate", Toast.LENGTH_SHORT).show();
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @OnClick(R.id.btn_nextpage)
    public void onClick() {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息

                //取出经纬度
                latLng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());

                //添加Marker显示定位位置
                if (marker == null) {
                    //如果是空的添加一个新的,icon方法就是设置定位图标，可以自定义
                    marker = aMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
                } else {
                    //已经添加过了，修改位置即可
                    marker.setPosition(latLng);
                }

                if (isFirst) {
                    isFirst = false;
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                }

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }
}
