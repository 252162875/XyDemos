package kx.rnd.com.permissionstest.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import kx.rnd.com.permissionstest.AppBarStateChangeListener;
import kx.rnd.com.permissionstest.R;
import kx.rnd.com.permissionstest.SliderHelper;

public class CollapsingToolbarTestActivity extends AppCompatActivity {
    //网络图
    HashMap<String, String> url_maps = new HashMap<String, String>();
    //网络地址
    HashMap<String, String> adUrl_maps = new HashMap<String, String>();
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.slider)
    SliderLayout mSlider;
    @BindView(R.id.custom_indicator)
    PagerIndicator custom_indicator;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.ll_title)
    LinearLayout ll_title;
    @BindView(R.id.iv_search)
    ImageView iv_search;
    public String[] data = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collapsing_toolbar_test);
        ButterKnife.bind(this);
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
        recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerview.setAdapter(new MyAdapter());
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        appbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                Log.d("STATE", state.name());
                if (state == State.EXPANDED) {
                    //展开状态
                    Toast.makeText(CollapsingToolbarTestActivity.this, "展开状态", Toast.LENGTH_SHORT).show();
                } else if (state == State.COLLAPSED) {
                    //折叠状态
//                    ll_title.setVisibility(View.VISIBLE);
                    Toast.makeText(CollapsingToolbarTestActivity.this, "折叠状态", Toast.LENGTH_SHORT).show();
                } else {
                    //中间状态
//                    ll_title.setVisibility(View.GONE);
                    Toast.makeText(CollapsingToolbarTestActivity.this, "中间状态", Toast.LENGTH_SHORT).show();
                }
            }
        });
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CollapsingToolbarTestActivity.this, "搜索", Toast.LENGTH_SHORT).show();
            }
        });

        //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
        mCollapsingToolbarLayout.setTitle("这是第" + 1 + "个页面");
        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.rgb(255, 64, 129));//设置收缩后Toolbar上字体的颜色
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CollapsingToolbarTestActivity.this, "FloatingActionButton", Toast.LENGTH_SHORT).show();
            }
        });
        SliderHelper.slidersetting(custom_indicator, url_maps, adUrl_maps, mSlider);
        mSlider.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCollapsingToolbarLayout.setTitle("这是第" + (position + 1) + "个页面");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_behavior_second, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof MyViewHolder) {
                ((MyViewHolder) holder).tv_name.setText(data[position]);
            }
        }

        @Override
        public int getItemCount() {
            return data.length;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_name;

            public MyViewHolder(View root) {
                super(root);
                tv_name = (TextView) root.findViewById(R.id.tv_name);
            }
        }
    }
}
