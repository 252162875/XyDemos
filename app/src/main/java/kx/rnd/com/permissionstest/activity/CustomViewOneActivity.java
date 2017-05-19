package kx.rnd.com.permissionstest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import kx.rnd.com.permissionstest.R;
import kx.rnd.com.permissionstest.view.CustomViewDayOne;

public class CustomViewOneActivity extends AppCompatActivity {
    // 颜色表 (注意: 此处定义颜色使用的是ARGB，带Alpha通道的)
    private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};
    @BindView(R.id.cv_pie)
    CustomViewDayOne cvPie;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view_one);
        ButterKnife.bind(this);
        ArrayList<Integer> data = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            int j = new Random().nextInt();
            int o = Math.abs(j % 2);
            if (o == 0) {
                o = 2;
            }
            data.add(i * o - i + 5);
            colors.add(mColors[i]);
        }
        cvPie.setData(data, colors);
        cvPie.setStartAngle(45.0f);
    }
}
