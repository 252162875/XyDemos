package kx.rnd.com.permissionstest.activity.customview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kx.rnd.com.permissionstest.R;
import kx.rnd.com.permissionstest.view.decoration.FloatDecoration;
import kx.rnd.com.permissionstest.view.decoration.GroupListener;

public class FloatDecorationActivity extends AppCompatActivity {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float_decoration);
        ButterKnife.bind(this);
        data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            data.add("模拟数据：" + i);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        MyAdapter myAdapter = new MyAdapter(R.layout.activity_mym_swipe_menu_layout, data);
        FloatDecoration floatDecoration = FloatDecoration.Builder.init(new GroupListener() {
            @Override
            public String getGroupName(int position) {
                return "分组：" + position / 5;
            }

            @Override
            public View getGroupView(int position) {
                TextView textView = new TextView(FloatDecorationActivity.this);
                textView.setBackgroundResource(R.drawable.dota2_3);
//                textView.setBackgroundColor(Color.argb(55,55,55,55));
                textView.setTextColor(Color.BLUE);
                textView.setTextSize(70);
                textView.setText("分组：" + position / 5);
                return textView;
            }

            @Override
            public int getLastIndexInGroup(int position) {
                int i = position / 5;
                return 5 * i+4;
            }
        }).isAlignLeft(true).setGroupHeight(350).build();
        mRecyclerView.addItemDecoration(floatDecoration);
        mRecyclerView.setAdapter(myAdapter);
    }

    public class MyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public MyAdapter(@LayoutRes int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            TextView textview = helper.getView(R.id.tv_content);
            textview.setText(item);
        }
    }
}
