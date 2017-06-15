package kx.rnd.com.permissionstest.activity.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import kx.rnd.com.permissionstest.R;

public class ProductDetailActivity extends AppCompatActivity {
    private String a = "http://h.hiphotos.baidu.com/zhidao/pic/itema.jpg";
    private String b = "http://c.hiphotos.baidu.com/zhidao/pic/item/f11f3a292df5e0fecb27ff235f6034a85edf72d0.jpg";
    private String c = "http://img.game333.net/uploads/news/2015/06/30/2015063090648313.jpg";
    private String d = "http://attachments.gfan.com/forum/attachments2/201301/25/1211561z89dgszjmgg99b8.jpg";
    private String e = "http://attachments.gfan.com/forum/attachments2/201301/25/1212073svv9taav0kaovvl.jpg.thumb.jpg";
    private String f = "http://attachments.gfan.com/forum/attachments2/201302/11/211033hw76ztzh9j6gewtu.jpg.thumb.jpg";
    private String g = "http://cdn.toy-tma.com/wp-content/uploads/2016/11/dota-2-official-702x336.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ImageView iv_01 = (ImageView) findViewById(R.id.iv_01);
        ImageView iv_02 = (ImageView) findViewById(R.id.iv_02);
        ImageView iv_03 = (ImageView) findViewById(R.id.iv_03);
        ImageView iv_04 = (ImageView) findViewById(R.id.iv_04);
        ImageView iv_05 = (ImageView) findViewById(R.id.iv_05);
        ImageView iv_06 = (ImageView) findViewById(R.id.iv_06);
        int width = getWindowManager().getDefaultDisplay().getWidth();
        setImage(this, d, iv_01, width);
        setImage(this, g, iv_02, width);
        setImage(this, c, iv_03, width);
        setImage(this, d, iv_04, width);
        setImage(this, e, iv_05, width);
        setImage(this, f, iv_06, width);
    }

    private void setImage(Context context, String url, final ImageView imageView, final int definiteWidth) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(context).asBitmap().load(url).apply(requestOptions).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                float w = bitmap.getWidth();
                float h = bitmap.getHeight(); //获取bitmap信息，可赋值给外部变量操作，也可在此时行操作。
                float i = h / w;
                int height = (int) (definiteWidth * i);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(definiteWidth, height));
                imageView.setImageBitmap(bitmap);
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.4f, 1.0f);
                alphaAnimation.setDuration(2000);
                imageView.setAnimation(alphaAnimation);
                alphaAnimation.start();
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                imageView.setLayoutParams(new LinearLayout.LayoutParams(definiteWidth, definiteWidth));
                imageView.setImageResource(R.drawable.ic_launcher);
            }

            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {
                imageView.setLayoutParams(new LinearLayout.LayoutParams(definiteWidth, definiteWidth));
                imageView.setImageResource(R.drawable.image);
            }
        });
    }
}
