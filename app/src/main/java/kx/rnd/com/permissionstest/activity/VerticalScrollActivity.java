package kx.rnd.com.permissionstest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kx.rnd.com.permissionstest.R;
import kx.rnd.com.permissionstest.view.VerticalMarqueeView;

public class VerticalScrollActivity extends AppCompatActivity {
    /**
     * 设置显示的view
     */
    List<View> views = new ArrayList<View>();
    @BindView(R.id.vm_scroller)
    VerticalMarqueeView vmScroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_scroll);
        ButterKnife.bind(this);
        setView();
        vmScroller.setViews(views);
        vmScroller.setOnItemClickListener(new VerticalMarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Toast.makeText(VerticalScrollActivity.this, "点击了第" + position + "个Item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.vm_scroller)
    public void onClick() {

        return;
    }

    /**
     * 设置布局文件
     */

    private void setView() {
        for (int i = 0; i < 3; i++) {
            final int position = i;
            // 设置滚动的单个布局
            LinearLayout moreView = (LinearLayout) LayoutInflater.from(VerticalScrollActivity.this).inflate(R.layout.item_vertical_scroll, null);
            // 初始化布局的控件
            TextView tv1 = (TextView) moreView.findViewById(R.id.tv1);
            // 进行对控件赋值
            tv1.setText("SOUL第" + i + "个信息");
            // 添加到循环滚动数组里面去
            views.add(moreView);
        }
    }
}
