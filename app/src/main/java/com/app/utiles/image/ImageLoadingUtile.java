package com.app.utiles.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

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

    //加载会话图片 type:三角形所在的边（0:左边；1:右边）
    public static void loadingChat(Context contxt, String loadingUrl, int defaultPng
            , ImageView iv, int type) {
        BitmapTransformation bitmapTransformation = new GlideChatTransform(
                contxt, type);

        Glide.with(contxt)
                .load(loadingUrl)
                .placeholder(defaultPng)
                .transform(bitmapTransformation)
                .into(iv);
    }

    public static void clear(final Context context, int type) {
        //清内存
        if (type == 0) {
            Glide.get(context).clearMemory();
        }
        //清缓存
        if (type == 1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Glide.get(context).clearDiskCache();

                }
            }).start();
        }

    }
}
