package kx.rnd.com.permissionstest.activity.customview;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kx.rnd.com.permissionstest.R;
import kx.rnd.com.permissionstest.TestFragment;
import kx.rnd.com.permissionstest.utils.UiUtils;

public class Scroll360Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll360_2);
//        RecyclerView recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
//        recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
//        recyclerview.setAdapter(new TestAdapter());
        ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tablayout = (TabLayout) findViewById(R.id.tablayout);
        tablayout.addTab(tablayout.newTab().setText("Tab 1"));
        tablayout.addTab(tablayout.newTab().setText("Tab 2"));
        tablayout.addTab(tablayout.newTab().setText("Tab 3"));
        //ViewPager的适配器
        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager,true);
    }

    class MyAdapter extends FragmentPagerAdapter {
        private String[] titles = {"页面1", "页面2", "页面3"};
        private List<Fragment> list;

        public MyAdapter(FragmentManager fm) {
            super(fm);
            list = new ArrayList<>();
            list.add(new TestFragment());
            list.add(new TestFragment());
            list.add(new TestFragment());
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        //重写这个方法，将设置每个Tab的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
