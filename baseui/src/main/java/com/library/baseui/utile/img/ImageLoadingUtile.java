package com.library.baseui.utile.img;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.library.baseui.utile.img.req.GlideBitReq;
import com.library.baseui.utile.img.req.GlideGifReq;
import com.squareup.picasso.Picasso;

import java.io.File;


/**
 * Created by Administrator on 2016/8/19.
 */
public class ImageLoadingUtile {
    public static void loadImageTest(Context contxt, String loadingUrl, int defaultPng, ImageView iv) {
        loadingGifAuto(contxt, loadingUrl, defaultPng, iv);
    }

    //加载图片
    public static void loading(Context contxt, String loadingUrl, int defaultPng, ImageView iv) {
        if (!isRun(contxt)) {
            return;
        }
        if (TextUtils.isEmpty(loadingUrl)) {
            iv.setImageResource(defaultPng);
            return;
        }
        Glide.with(contxt).setDefaultRequestOptions(new RequestOptions())//设置默认的请求项
                .load(loadingUrl).diskCacheStrategy(DiskCacheStrategy.ALL)//将图片缓存到磁盘中
                .format(DecodeFormat.PREFER_RGB_565)//解码为565格式，减小内存
                .skipMemoryCache(true)//不将图片缓存到内存中
                .placeholder(defaultPng).into(iv);
    }

    //加载图片 居中
    public static void loadingCenterCrop(Context contxt, String loadingUrl, int defaultPng, ImageView iv) {
        if (!isRun(contxt)) {
            return;
        }
        if (TextUtils.isEmpty(loadingUrl)) {
            iv.setImageResource(defaultPng);
            return;
        }

        Glide.with(contxt).setDefaultRequestOptions(new RequestOptions())//设置默认的请求项
                .load(loadingUrl).diskCacheStrategy(DiskCacheStrategy.ALL)//将图片缓存到磁盘中
                .format(DecodeFormat.PREFER_RGB_565)//解码为565格式，减小内存
                .skipMemoryCache(true)//不将图片缓存到内存中
                .placeholder(defaultPng).centerCrop().into(iv);
    }

    //加载本地图片
    public static void loading(Context contxt, String loadingUrl, ImageView iv) {
      /*  if (!isExistMainActivity(contxt.getClass(),contxt)){
            return;
        }*/
        Glide.with(contxt).load(loadingUrl).into(iv);
    }

    //加载圆形图片
    public static void loadingCircle(Context contxt, String loadingUrl, int defaultPng, ImageView iv) {
        if (!isRun(contxt)) {
            return;
        }
        Glide.with(contxt).setDefaultRequestOptions(new RequestOptions())//设置默认的请求项
                .load(loadingUrl).diskCacheStrategy(DiskCacheStrategy.ALL)//将图片缓存到磁盘中
                .format(DecodeFormat.PREFER_RGB_565)//解码为565格式，减小内存
                .skipMemoryCache(true)//不将图片缓存到内存中
                .placeholder(defaultPng).transform(new GlideCircleTransform(contxt)).into(iv);
    }

    public static void loadImageTestPicasso(Context contxt, String loadingUrl, int defaultPng, ImageView iv) {
        if (defaultPng > 0) {
            iv.setImageResource(defaultPng);
        }
        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load(loadingUrl).placeholder(defaultPng).into(iv);
    }


    //加载圆角图片
    public static void loadingCircularBead(Context contxt, String loadingUrl, int defaultPng, ImageView iv) {
        loadingCircularBead(contxt, loadingUrl, defaultPng, 10, iv);
    }

    public static void loadingCircularBead(Context contxt, String loadingUrl, int defaultPng, int radius, ImageView iv) {
        if (!isRun(contxt)) {
            return;
        }
        Glide.with(contxt).load(loadingUrl).placeholder(defaultPng).transform(new GlideCircularBeadTransform(contxt, radius)).into(iv);
    }

    //加载会话图片 type:三角形所在的边（0:左边；1:右边）
    public static void loadImageChat(Context contxt, String loadingUrl, int defaultPng, final ImageView iv, final int type) {
        iv.setImageResource(defaultPng);
        Glide.with(contxt).asBitmap().load(loadingUrl).placeholder(defaultPng).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                ChatTransform chatTransform = new ChatTransform(type);
                resource = chatTransform.drawBitmap(resource, 200);
                iv.setImageBitmap(resource);
            }


        });
    }

    //====================================宽高按比例来显示
    //加载图片
    public static void loadingAuto(Context context, String loadingUrl, int defaultPng, ImageView iv) {
        if (TextUtils.isEmpty(loadingUrl)) {
            iv.setImageResource(defaultPng);
            return;
        }
        if (!isRun(context)) {
            return;
        }
        Glide.with(context).asBitmap().load(loadingUrl).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).diskCacheStrategy(DiskCacheStrategy.ALL)//将图片缓存到磁盘中
                .format(DecodeFormat.PREFER_RGB_565)//解码为565格式，减小内存
                .skipMemoryCache(true)//不将图片缓存到内存中
                .placeholder(defaultPng).listener(new GlideBitReq(context, iv, loadingUrl)).into(iv);
    }

    public static void loadingAuto(Context context, File file, int defaultPng, ImageView iv) {
        if (!isRun(context)) {
            return;
        }

        Glide.with(context).asBitmap().load(file).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).diskCacheStrategy(DiskCacheStrategy.ALL)//将图片缓存到磁盘中
                .format(DecodeFormat.PREFER_RGB_565)//解码为565格式，减小内存
                .skipMemoryCache(true)//不将图片缓存到内存中
                .placeholder(defaultPng).listener(new GlideBitReq(context, iv, file)).into(iv);
    }

    public static void loadingGifAuto(Context context, String loadingUrl, int defaultPng, ImageView iv) {
        if (TextUtils.isEmpty(loadingUrl)) {
            iv.setImageResource(defaultPng);
            return;
        }
        if (!isRun(context)) {
            return;
        }
        Glide.with(context).asGif().load(loadingUrl).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).diskCacheStrategy(DiskCacheStrategy.ALL)//将图片缓存到磁盘中
                .format(DecodeFormat.PREFER_RGB_565)//解码为565格式，减小内存
                .skipMemoryCache(true)//不将图片缓存到内存中
                .placeholder(defaultPng).listener(new GlideGifReq(context, iv, loadingUrl)).into(iv);
    }


    private static boolean isRun(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            boolean isFinishing = activity.isFinishing();
            if (isFinishing) {
                return false;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
                return false;
            }
        }
        return true;
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
