package kx.rnd.com.permissionstest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import kx.rnd.com.permissionstest.R;
import kx.rnd.com.permissionstest.view.PolygonView;

public class PolygonActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.polygon)
    PolygonView polygon;
    @BindView(R.id.sb1)
    SeekBar sb1;
    @BindView(R.id.sb2)
    SeekBar sb2;
    @BindView(R.id.sb3)
    SeekBar sb3;
    @BindView(R.id.sb4)
    SeekBar sb4;
    @BindView(R.id.sb5)
    SeekBar sb5;
    @BindView(R.id.sb6)
    SeekBar sb6;
    @BindView(R.id.sb7)
    SeekBar sb7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polygon);
        ButterKnife.bind(this);
//        polygon = (PolygonView) findViewById(R.id.polygon);
//        sb1 = (SeekBar) findViewById(R.id.sb1);
//        sb2 = (SeekBar) findViewById(R.id.sb2);
//        sb3 = (SeekBar) findViewById(R.id.sb3);
//        sb4 = (SeekBar) findViewById(R.id.sb4);
//        sb5 = (SeekBar) findViewById(R.id.sb5);
//        sb6 = (SeekBar) findViewById(R.id.sb6);
//        sb7 = (SeekBar) findViewById(R.id.sb7);
        sb1.setOnSeekBarChangeListener(this);
        sb2.setOnSeekBarChangeListener(this);
        sb3.setOnSeekBarChangeListener(this);
        sb4.setOnSeekBarChangeListener(this);
        sb5.setOnSeekBarChangeListener(this);
        sb6.setOnSeekBarChangeListener(this);
        sb7.setOnSeekBarChangeListener(this);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float values = (float) (seekBar.getProgress() / 10.0);
        switch (seekBar.getId()) {
            case R.id.sb1:
                polygon.setValue1(values);
                break;
            case R.id.sb2:
                polygon.setValue2(values);
                break;
            case R.id.sb3:
                polygon.setValue3(values);
                break;
            case R.id.sb4:
                polygon.setValue4(values);
                break;
            case R.id.sb5:
                polygon.setValue5(values);
                break;
            case R.id.sb6:
                polygon.setValue6(values);
                break;
            case R.id.sb7:
                polygon.setValue7(values);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
