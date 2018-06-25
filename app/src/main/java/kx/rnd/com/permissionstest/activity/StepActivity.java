package kx.rnd.com.permissionstest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import kx.rnd.com.permissionstest.R;
import kx.rnd.com.permissionstest.view.StepView;

public class StepActivity extends AppCompatActivity {
    private int step = 3;
    private ArrayList<String> strings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        strings.add("报名");
        strings.add("签到");
        strings.add("签到确认");
        strings.add("完成");
        strings.add("完成确认");
        strings.add("结算");
        final StepView step_view = (StepView) findViewById(R.id.step_view);
        Button btn_next_step = (Button) findViewById(R.id.btn_next_step);
        btn_next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (step + 1 >= strings.size()) {
                    step = 0;
                } else {
                    step++;
                }
                step_view.setStep(step);
            }
        });
        step_view.setAllSteps(strings);
        step_view.setStep(step);
    }
}
