package kx.rnd.com.permissionstest.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kx.rnd.com.permissionstest.R;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class BaiduMapActivity extends Activity {

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    @BindView(R.id.bmapView)
    MapView mMapView;
    @BindView(R.id.iv_mode)
    ImageView ivMode;
    @BindView(R.id.iv_snapshot)
    ImageView iv_snapshot;
    private BaiduMap mBaiduMap;
    private int[] mode = {BaiduMap.MAP_TYPE_NORMAL, BaiduMap.MAP_TYPE_SATELLITE, BaiduMap.MAP_TYPE_NONE};
    private int i = 0;
    private double v;
    private double v1;
    private Marker marker;
    private MarkerOptions options;
    private PoiSearch mPoiSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu_map);
        ButterKnife.bind(this);
        //第一步，创建POI检索实例
        mPoiSearch = PoiSearch.newInstance();
        //第二步，创建POI检索监听者；
        OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
            public void onGetPoiResult(PoiResult result) {
                //获取POI检索结果
                String address = result.toString();
                //uid是POI检索中获取的POI ID信息
                mPoiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(result.getAllPoi().get(0).uid));
            }

            public void onGetPoiDetailResult(PoiDetailResult result) {
                //获取Place详情页检索结果
                if (result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //详情检索失败
                    // result.error请参考SearchResult.ERRORNO
                    Toast.makeText(BaiduMapActivity.this, "poi检索失败了", Toast.LENGTH_SHORT).show();
                } else {
                    //检索成功
                    String address = result.getAddress();
                    Toast.makeText(BaiduMapActivity.this, "查询出'西安市科技二路'的第一个数据地址是:" + address, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        };
        //第三步，设置POI检索监听者；
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
        //第四步，发起检索请求；
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city("西安")
                .keyword("科技二路")
                .pageNum(10));
        mBaiduMap = mMapView.getMap();
        BaiduMapActivityPermissionsDispatcher.getLocationWithCheck(this);//BaiduMap初始化完成再调用，否则在MyLocationListener 的onReceiveLocation里面mBaiduMap可能为NULL
//        // 将底图标注设置为隐藏，只显示交通情况
//        mBaiduMap.showMapPoi(false);
        //开启交通图
        mBaiduMap.setTrafficEnabled(true);
        //设置是否允许定位图层
        mBaiduMap.setMyLocationEnabled(true);
        BitmapDescriptor bitmap0 = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_geo);
        MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, bitmap0);
        mBaiduMap.setMyLocationConfiguration(myLocationConfiguration);
        mBaiduMap.setOnMyLocationClickListener(new BaiduMap.OnMyLocationClickListener() {
            @Override
            public boolean onMyLocationClick() {
                Toast.makeText(BaiduMapActivity.this, "我的位置图标被点击了", Toast.LENGTH_SHORT).show();
                if (iv_snapshot.getVisibility() == View.VISIBLE) {
                    iv_snapshot.setVisibility(View.GONE);
                } else {
                    iv_snapshot.setVisibility(View.VISIBLE);
                    //地图截图
                    mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
                        @Override
                        public void onSnapshotReady(Bitmap bitmap) {
                            iv_snapshot.setImageBitmap(bitmap);
                        }
                    });
                }
                return true;
            }
        });

      /*  //设置定位数据, 只有先允许定位图层后设置数据才会生效，参见 setMyLocationEnabled(boolean)//这块定位逻辑放在了定位成功回调里面
        MyLocationData.Builder builder = new MyLocationData.Builder();
        MyLocationData locationData = builder.latitude(34.341568).longitude(108.940174).build();
        mBaiduMap.setMyLocationData(locationData);
        //更新地图状态，这里是显示到前面设置的西安市
        MyLocationData locationData1 = mBaiduMap.getLocationData();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(new LatLng(locationData1.latitude, locationData1.longitude), 15);
        mBaiduMap.animateMapStatus(mapStatusUpdate);*/

        //设置是否允许楼块效果
        mBaiduMap.setBuildingsEnabled(true);
        //开启城市热力图
//        mBaiduMap.setBaiduHeatMapEnabled(true);
        //  地图Logo 默认在左下角显示，不可以移除。
        mMapView.setLogoPosition(LogoPosition.logoPostionRightTop);

        //定义Maker坐标点
        v = 34.241568;
        v1 = 108.940174;
        LatLng point = new LatLng(v, v1);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.arrow);
        //构建MarkerOption，用于在地图上添加Marker
        //设置marker的位置
        //设置marker图标
        //设置marker所在层级
        //设置手势拖拽
        options = new MarkerOptions()
                .position(point)  //设置marker的位置
                .icon(bitmap)  //设置marker图标
                .zIndex(9) //设置marker所在层级
                .alpha(0.9f)  //设置透明度
                .draggable(true);

        // 生长动画
        options.animateType(MarkerOptions.MarkerAnimateType.grow);
        //将marker添加到地图上
        marker = (Marker) mBaiduMap.addOverlay(options);
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(BaiduMapActivity.this, "ID为" + marker.getPeriod() + "的marker被点击了", Toast.LENGTH_SHORT).show();
                showWindow(marker.getPosition().latitude, marker.getPosition().longitude);
                return true;
            }
        });
        //调用BaiduMap对象的setOnMarkerDragListener方法设置marker拖拽的监听
        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
                //拖拽中
            }

            public void onMarkerDragEnd(Marker marker) {
                //拖拽结束
                LatLng position = marker.getPosition();
                Toast.makeText(BaiduMapActivity.this, "" + position.latitude + "---" + position.longitude, Toast.LENGTH_SHORT).show();
            }

            public void onMarkerDragStart(Marker marker) {
                //开始拖拽
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        //第五步，释放POI检索实例；
        mPoiSearch.destroy();
        mLocationClient.stop();   //添加这句就行了
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @OnClick({R.id.iv_mode, R.id.iv_anim, R.id.iv_polygon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_mode:
                if (i == 3) {
                    i = 0;
                }
                mBaiduMap.setMapType(mode[i]);
                i++;
                mBaiduMap.clear();//清空地图所有的 Overlay 覆盖物以及 InfoWindow
                break;
            case R.id.iv_anim:
                marker = (Marker) mBaiduMap.addOverlay(options);
                break;
            case R.id.iv_polygon:
                drawPolygon();
                drawCircle();
                drawPolyline();
                drawText("测试写文字");
//                marker.remove();//移除marker箭头图标
                break;
        }
    }

    private void drawText(String text) {
        //定义文字所显示的坐标点
        LatLng pt = new LatLng(34.341568, 108.940174);
        //构建文字Option对象，用于在地图上添加文字
        TextOptions textOptions = new TextOptions();
        textOptions.bgColor(0xFFFF0000).fontSize(24).fontColor(0xFFFFFFFF).text(text).position(pt).rotate(45);
        //在地图上添加该文字对象并显示
        mBaiduMap.addOverlay(textOptions);
    }

    private void drawPolygon() {
        //定义多边形的五个顶点 v = 34.341568;v1 = 108.940174;
        LatLng pt1 = new LatLng(34.341568, 108.940174);
        LatLng pt2 = new LatLng(34.351568, 108.950174);
        LatLng pt3 = new LatLng(34.371568, 108.950174);
        LatLng pt4 = new LatLng(34.371568, 108.970174);
        LatLng pt5 = new LatLng(34.341568, 108.950174);
        List<LatLng> pts = new ArrayList<LatLng>();
        pts.add(pt1);
        pts.add(pt2);
        pts.add(pt3);
        pts.add(pt4);
        pts.add(pt5);
        //构建用户绘制多边形的Option对象
        OverlayOptions polygonOption = new PolygonOptions()
                .points(pts)
                .stroke(new Stroke(1, 0xAA00FF00))
                .fillColor(0xAAFF0000);
        //在地图上添加多边形Option，用于显示
        mBaiduMap.addOverlay(polygonOption);
    }

    private void drawCircle() {
        //定义圆形圆心
        LatLng pt1 = new LatLng(34.341568, 108.940174);


        //构建用户绘制圆形的Option对象
        CircleOptions circleOptions = new CircleOptions();
        //半径单位是米
        circleOptions.center(pt1).fillColor(0xAA0000FF).radius(1000);
        //在地图上添加圆形Option，用于显示
        mBaiduMap.addOverlay(circleOptions);
    }

    private void drawPolyline() {
        //定义折线的5个点
        LatLng pt1 = new LatLng(34.341568, 108.940174);
        LatLng pt2 = new LatLng(34.451568, 108.450174);
        LatLng pt3 = new LatLng(34.141568, 108.140174);
        LatLng pt4 = new LatLng(34.241568, 108.160174);
        List<LatLng> pts = new ArrayList<LatLng>();
        pts.add(pt1);
        pts.add(pt2);
        pts.add(pt3);
        pts.add(pt4);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(0xFFFFDDEE);
        colors.add(0xFFAADDAA);
        colors.add(0xFFFF00EE);
        BitmapDescriptor bitmapDescriptor1 = BitmapDescriptorFactory.fromResource(R.drawable.icon_road_blue_arrow);
        BitmapDescriptor bitmapDescriptor2 = BitmapDescriptorFactory.fromResource(R.drawable.icon_road_red_arrow);
        BitmapDescriptor bitmapDescriptor3 = BitmapDescriptorFactory.fromResource(R.drawable.icon_road_green_arrow);
        ArrayList<BitmapDescriptor> bitmapDescriptors = new ArrayList<>();
        ArrayList<Integer> textureIndexes = new ArrayList<>();
        bitmapDescriptors.add(bitmapDescriptor1);
        bitmapDescriptors.add(bitmapDescriptor2);
        bitmapDescriptors.add(bitmapDescriptor3);
        textureIndexes.add(0);
        textureIndexes.add(1);
        textureIndexes.add(2);
        textureIndexes.add(0);

        //构建用户绘制折线的Option对象
        PolylineOptions polylineOptions = new PolylineOptions();
        //折线颜色和折线坐标点的设置
        polylineOptions.color(0xFFFF00FF).points(pts).width(15).customTextureList(bitmapDescriptors).textureIndex(textureIndexes);
        //在地图上添加折线Option，用于显示
        mBaiduMap.addOverlay(polylineOptions);
    }

    private void showWindow(double l, double l1) {
        //创建InfoWindow展示的view
        Button button = new Button(getApplicationContext());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaiduMap.hideInfoWindow();
            }
        });
        button.setText("测试");
        button.setBackgroundResource(R.drawable.popup);
        //定义用于显示该InfoWindow的坐标点
        LatLng pt = new LatLng(l, l1);
        //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
        InfoWindow mInfoWindow = new InfoWindow(button, pt, -47);
        //显示InfoWindow
        mBaiduMap.showInfoWindow(mInfoWindow);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void getLocation() {
        //第一步，初始化LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        //第三步，实现BDLocationListener接口(myListener)
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        //第二步，配置定位SDK参数
        initLocation();
        //第四步，开始定位
        mLocationClient.start();
    }

    /**
     * 配置定位SDK参数
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 0;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BaiduMapActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void noPermission(PermissionRequest request) {
        showRationaleDialog("必须有权限才能使用,现在就去设置吗？", request);
    }

    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void juJue() {
        Toast.makeText(this, "用户拒绝了申请", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void juJueAndNoTiShi() {
        Toast.makeText(this, "拒绝且不提示", Toast.LENGTH_SHORT).show();
    }

    private void showRationaleDialog(String messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("算了吧", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    /**
     * 第三步，实现BDLocationListener接口
     */
    public class MyLocationListener implements BDLocationListener {
        /* 必须在清单文件配置定位服务
        <service
               android:name="com.baidu.location.f"
               android:enabled="true"
               android:process=":remote"></service>
               */
        @Override
        public void onReceiveLocation(BDLocation location) {

            //设置定位数据, 只有先允许定位图层后设置数据才会生效，参见 setMyLocationEnabled(boolean)
            MyLocationData.Builder builder = new MyLocationData.Builder();
            MyLocationData locationData = builder.accuracy(location.getRadius()).direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();// 此处设置开发者获取到的方向信息，顺时针0-360
            mBaiduMap.setMyLocationData(locationData);
            //更新地图状态，这里是显示到前面设置的西安市
            MyLocationData locationData1 = mBaiduMap.getLocationData();
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(new LatLng(locationData1.latitude, locationData1.longitude), 15);
            mBaiduMap.animateMapStatus(mapStatusUpdate);
            Toast.makeText(BaiduMapActivity.this, "定位成功:" + locationData1.latitude + "---" + locationData1.longitude, Toast.LENGTH_SHORT).show();


            //获取定位结果
            StringBuffer sb = new StringBuffer(256);

            sb.append("time : ");
            sb.append(location.getTime());    //获取定位时间

            sb.append("\nerror code : ");
            sb.append(location.getLocType());    //获取类型类型

            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());    //获取纬度信息

            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());    //获取经度信息

            sb.append("\nradius : ");
            sb.append(location.getRadius());    //获取定位精准度
            int locType = location.getLocType();
            if (location.getLocType() == BDLocation.TypeGpsLocation) {

                // GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());    // 单位：公里每小时

                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());    //获取卫星数

                sb.append("\nheight : ");
                sb.append(location.getAltitude());    //获取海拔高度信息，单位米

                sb.append("\ndirection : ");
                sb.append(location.getDirection());    //获取方向信息，单位度

                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {

                // 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\noperationers : ");
                sb.append(location.getOperators());    //获取运营商信息

                sb.append("\ndescribe : ");
                sb.append("网络定位成功");

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                // 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");

            } else if (location.getLocType() == BDLocation.TypeServerError) {

                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");

            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

            }

            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());    //位置语义化信息

            List<Poi> list = location.getPoiList();    // POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }

            Log.e("BaiduLocationApiDem", sb.toString());
        }
    }
}
