package com.app.utiles.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2016/8/19.
 */
public class ImageLoadingUtile {
    //加载圆形图片
    public static void loadingCircle(Context contxt, String loadingUrl, int
            defaultPng, ImageView iv) {
        Glide.with(contxt)
                .load(loadingUrl)
                .placeholder(defaultPng)
                .transform(new GlideCircleTransform(contxt))
                .into(iv);
    }

    //加载图片
    public static void loading(Context contxt, String loadingUrl, int
            defaultPng, ImageView iv) {
        Glide.with(contxt)
                .load(loadingUrl)
                .placeholder(defaultPng)
                .into(iv);
    }
}
