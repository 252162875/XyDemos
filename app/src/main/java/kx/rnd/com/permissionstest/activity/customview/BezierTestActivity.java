package kx.rnd.com.permissionstest.activity.customview;

import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kx.rnd.com.permissionstest.R;
import kx.rnd.com.permissionstest.view.BezierTest3View;

public class BezierTestActivity extends AppCompatActivity {

    @BindView(R.id.btv_xin)
    BezierTest3View btvXin;
    @BindView(R.id.image_bg)
    ImageView image_bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier);
        ButterKnife.bind(this);
        image_bg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        /**
         * 在这里额外测试了com.caverock:androidsvg:1.2.2-beta-1这个SVG解析库
         */
        try {
            SVG svg = SVG.getFromResource(this, R.raw.bird);
            Picture picture = svg.renderToPicture();
            Drawable pictureDrawable = new PictureDrawable(picture);
            image_bg.setImageDrawable(pictureDrawable);
        } catch (SVGParseException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btv_xin)
    public void onViewClicked() {
        btvXin.rePlay();
    }
}
