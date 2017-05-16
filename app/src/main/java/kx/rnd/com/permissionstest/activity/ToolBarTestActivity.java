package kx.rnd.com.permissionstest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import kx.rnd.com.permissionstest.R;
import kx.rnd.com.permissionstest.SliderHelper;

public class ToolBarTestActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.slider)
    SliderLayout mSlider;
    @BindView(R.id.custom_indicator)
    PagerIndicator custom_indicator;
    @BindView(R.id.iv_test)
    ImageView iv_test;
    //网络图
    HashMap<String, String> url_maps = new HashMap<String, String>();
    //网络地址
    HashMap<String, String> adUrl_maps = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_bar_test);
        ButterKnife.bind(this);
        Picasso.with(this).load("http://op21a2eey.bkt.clouddn.com/0806250113%20%281%29.jpg").fit().into(iv_test);
        //两个map的key要对应一致，因为在SliderHelper.slidersetting中是分别对应起来的，否则就要写成对象的形式封装进去(偷懒)
        url_maps.put("IMG-" + 1, "http://op21a2eey.bkt.clouddn.com/0806250113%20%281%29.jpg");
        adUrl_maps.put("IMG-" + 1, "http://op21a2eey.bkt.clouddn.com/0806250113%20%281%29.jpg");
        url_maps.put("IMG-" + 2, "http://op21a2eey.bkt.clouddn.com/0806250113%20%282%29.jpg");
        adUrl_maps.put("IMG-" + 2, "http://op21a2eey.bkt.clouddn.com/0806250113%20%282%29.jpg");
        url_maps.put("IMG-" + 3, "http://op21a2eey.bkt.clouddn.com/0806250113%20%283%29.jpg");
        adUrl_maps.put("IMG-" + 3, "http://op21a2eey.bkt.clouddn.com/0806250113%20%283%29.jpg");
        url_maps.put("IMG-" + 4, "http://op21a2eey.bkt.clouddn.com/0806250113%20%284%29.jpg");
        adUrl_maps.put("IMG-" + 4, "http://op21a2eey.bkt.clouddn.com/0806250113%20%284%29.jpg");
        url_maps.put("IMG-" + 5, "http://op21a2eey.bkt.clouddn.com/0806250113%20%285%29.jpg");
        adUrl_maps.put("IMG-" + 5, "http://op21a2eey.bkt.clouddn.com/0806250113%20%285%29.jpg");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//默认是true，表示：显示小箭头，false则不显示小箭头
        getSupportActionBar().setHomeButtonEnabled(true);//默认是true,表示左边箭头是否可点击,但是在魅族5.0测试无效(不知道是不是因为设置了 toolbar.setNavigationOnClickListener ?)
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        SliderHelper.slidersetting(custom_indicator, url_maps, adUrl_maps, mSlider);
    }
}
