package kx.rnd.com.permissionstest;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.ArrayList;
import java.util.HashMap;

public class SliderHelper {
    public static void slidersetting(PagerIndicator pagerindicator, HashMap<String, String> url_maps, final HashMap<String, String> adUrl_maps, SliderLayout mSlider) {
        final Context context = mSlider.getContext();
        for (final String name : url_maps.keySet()) {
            final TextSliderView textSliderView = new TextSliderView(context);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {

                        @Override
                        public void onSliderClick(BaseSliderView baseSliderView) {
//                            Toast.makeText(mActivity, baseSliderView.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
                            if (!adUrl_maps.get(name).isEmpty()) {
                                Toast.makeText(context, adUrl_maps.get(name), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mSlider.addSlider(textSliderView);
        }
        mSlider.setDuration(4000);
//        mSlider.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
//
//            @Override
//            public void onPageSelected(int position) {
//                Log.d("Slider Demo", "Page Changed: " + position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {}
//        });
        mSlider.setCustomIndicator(pagerindicator);//设置自己的小圆点
        mSlider.setCustomAnimation(new DescriptionAnimation());//设置下面广告标题布局
        //切换效果的一个枚举
        ArrayList<String> transformerStr = new ArrayList<>();
        for (int i = 0; i < SliderLayout.Transformer.values().length; i++) {
            SliderLayout.Transformer transformer = SliderLayout.Transformer.values()[i];
            transformerStr.add(transformer.toString());
        }
        mSlider.setPresetTransformer(transformerStr.get(9));//设置切换效果
        mSlider.startAutoCycle();
    }
}