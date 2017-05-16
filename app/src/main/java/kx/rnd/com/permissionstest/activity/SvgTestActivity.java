package kx.rnd.com.permissionstest.activity;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.eftimoff.androipathview.PathView;
import com.jrummyapps.android.widget.AnimatedSvgView;

import kx.rnd.com.permissionstest.R;
import kx.rnd.com.permissionstest.SVG;

/**
 * 用AnimatedSvgView + xml文件只需要得到path做成xml就OK了，但是不能控制进度
 * 用PathView + svg文件需要完整的svg文件，可以控制进度，兼容性和适配有待测试
 * 用ImageView + xml文件 + 官方objectAnimator + animated-vector 只支持api21+ 并且效果不是理想绘制轮廓的效果
 * 这里解释一下这几个属性；
 * android:width=”600dp” 实际显示的宽度为600dp
 * android:height=”600dp” 实际显示的高度为600dp
 * android:viewportHeight=”512” 矢量限定的宽度为512，这没有单位；
 * android:viewportWidth=”512” 矢量限定的高度为512
 * 其实以上两个属性，就是我们 svg 图片的宽高
 **/
public class SvgTestActivity extends AppCompatActivity {

    private AnimatedSvgView svgView;
    private int index = -1;
    private PathView pathView;
    private SeekBar seekbar;
    private ImageView iv_svg_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg_test);
        svgView = (AnimatedSvgView) findViewById(R.id.animated_svg_view);
        pathView = (PathView) findViewById(R.id.pathView);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        iv_svg_2 = (ImageView) findViewById(R.id.iv_svg_2);
        animateImage();
        iv_svg_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateImage();
            }
        });
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float p = progress / 100.0f;
                pathView.setPercentage(p);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        pathView.useNaturalColors();
        pathView.setFillAfter(true);
        pathView.getPathAnimator().listenerStart(new PathView.AnimatorBuilder.ListenerStart() {
            @Override
            public void onAnimationStart() {
                Toast.makeText(SvgTestActivity.this, "pathView动画开始", Toast.LENGTH_SHORT).show();
            }
        }).listenerEnd(new PathView.AnimatorBuilder.ListenerEnd() {
            @Override
            public void onAnimationEnd() {
                Toast.makeText(SvgTestActivity.this, "pathView动画结束", Toast.LENGTH_SHORT).show();
            }
        }).
//        pathView.getSequentialPathAnimator().
        delay(100).
                duration(1500).
                interpolator(new AccelerateDecelerateInterpolator()).
                start();

        pathView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathView.getPathAnimator().
                        delay(100).
                        duration(1500).
                        interpolator(new AccelerateDecelerateInterpolator()).
                        start();
            }
        });
        setSvg(SVG.values()[0]);
        svgView.postDelayed(new Runnable() {

            @Override
            public void run() {
                svgView.start();
            }
        }, 500);


        svgView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (svgView.getState() == AnimatedSvgView.STATE_FINISHED) {
                    svgView.start();
                }
            }
        });

        svgView.setOnStateChangeListener(new AnimatedSvgView.OnStateChangeListener() {

            @Override
            public void onStateChange(int state) {
                if (state == AnimatedSvgView.STATE_TRACE_STARTED) {
                    findViewById(R.id.btn_previous).setEnabled(false);
                    findViewById(R.id.btn_next).setEnabled(false);
                } else if (state == AnimatedSvgView.STATE_FINISHED) {
                    findViewById(R.id.btn_previous).setEnabled(index != -1);
                    findViewById(R.id.btn_next).setEnabled(true);
                    if (index == -1) index = 0; // first time
                }
            }
        });
    }

    public void onNext(View view) {
        if (++index >= SVG.values().length) index = 0;
        setSvg(SVG.values()[index]);
    }

    public void onPrevious(View view) {
        if (--index < 0) index = SVG.values().length - 1;
        setSvg(SVG.values()[index]);
    }

    private void setSvg(SVG svg) {
        svgView.setGlyphStrings(svg.glyphs);
        svgView.setFillColors(svg.colors);
        svgView.setViewportSize(svg.width, svg.height);
        svgView.setTraceResidueColor(0x32000000);
        svgView.setTraceColors(svg.colors);
        svgView.rebuildGlyphData();
        svgView.start();
    }

    // 只支持5.0以上.
    private void animateImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 获取动画效果
            AnimatedVectorDrawable mAnimatedVectorDrawable = (AnimatedVectorDrawable)
                    ContextCompat.getDrawable(SvgTestActivity.this, R.drawable.anim_form);
            iv_svg_2.setImageDrawable(mAnimatedVectorDrawable);
            if (mAnimatedVectorDrawable != null) {
                mAnimatedVectorDrawable.start();
            }
        }
    }
}
