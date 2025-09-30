package com.library.baseui.utile.img;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.webp.decoder.WebpDrawable;
import com.bumptech.glide.integration.webp.decoder.WebpDrawableTransformation;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.library.baseui.utile.img.req.BitReq;
import com.library.baseui.utile.img.req.GifReq;
import com.library.baseui.utile.img.req.WebPGifReq;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Objects;

import sj.mblog.Logx;


/**
 * Created by Administrator on 2016/8/19.
 */
public class ImageLoadingUtile {
    public static void loadImageTest(Context contxt, String loadingUrl, int defaultPng, ImageView iv) {
        //loadingGifAuto(contxt, loadingUrl, defaultPng, iv);
        //loadingGif(contxt, loadingUrl, defaultPng, iv);
        //loadingGifWebP2(contxt, loadingUrl, defaultPng, iv);
        loadingFileType(contxt, loadingUrl, defaultPng, iv);
        loadingBitType(contxt, loadingUrl, defaultPng, iv);
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
        Glide.with(contxt)
                .setDefaultRequestOptions(new RequestOptions())//设置默认的请求项
                .load(loadingUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//将图片缓存到磁盘中
                .format(DecodeFormat.PREFER_RGB_565)//解码为565格式，减小内存
                .skipMemoryCache(true)//不将图片缓存到内存中
                .placeholder(defaultPng)
                .into(iv);
    }


    public static void loadingFileType(Context contxt, String loadingUrl, int defaultPng, final ImageView iv) {
        iv.setImageResource(defaultPng);
        Glide.with(contxt).asFile()
                .load(loadingUrl)
                .placeholder(defaultPng)
                .into(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                        ImageFile.redImgFileType(resource);
                    }

                });
    }

    public static void loadingBitType(Context contxt, String loadingUrl, int defaultPng, final ImageView iv) {
        iv.setImageResource(defaultPng);
        Glide.with(contxt).asBitmap()
                .load(loadingUrl)
                .placeholder(defaultPng)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ImageFile.redImgBitType(resource);
                    }

                });
    }

    /***
     * 下载文件 要在子线程里远行
     * @param context
     * @param imgUrl
     * @return
     */
    public static File downLoadImageFile(Context context, String imgUrl) {
        File imageFile = null;
        FutureTarget<File> target = Glide.with(context)
                .asFile()
                .load(imgUrl)
                .submit();
        try {
            imageFile = target.get();
        } catch (Exception e) {
            Logx.d("文件下载出错：" + e.getMessage());
        }
        return imageFile;
    }

    /**
     * 下载图片 要在子线程里远行
     *
     * @param context
     * @param imgUrl
     * @return
     */
    public static Bitmap downLoadImageBit(Context context, String imgUrl) {
        Bitmap bitmap = null;
        try {
            FutureTarget<Bitmap> glide = Glide.with(context).asBitmap().load(imgUrl)
                    .into(480, 800);
            if (glide.isCancelled()) {
                return null;
            }
            bitmap = glide.get();
        } catch (Exception e) {
            Logx.d("图片下载出错：" + e.getMessage());
        }

        return bitmap;
    }

    /**
     * 停止下载
     *
     * @param glide
     */
    public static void downStop(FutureTarget<Objects> glide) {
        if (glide == null) {
            return;
        }
        if (!glide.isDone()) {
            glide.onStop();
            glide.onDestroy();
        }
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

        Glide.with(contxt)
                .setDefaultRequestOptions(new RequestOptions())//设置默认的请求项
                .load(loadingUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//将图片缓存到磁盘中
                .format(DecodeFormat.PREFER_RGB_565)//解码为565格式，减小内存
                .skipMemoryCache(true)//不将图片缓存到内存中
                .placeholder(defaultPng)
                .centerCrop()
                .into(iv);
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
        Glide.with(contxt)
                .setDefaultRequestOptions(new RequestOptions())//设置默认的请求项
                .load(loadingUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//将图片缓存到磁盘中
                .format(DecodeFormat.PREFER_RGB_565)//解码为565格式，减小内存
                .skipMemoryCache(true)//不将图片缓存到内存中
                .placeholder(defaultPng)
                .transform(new GlideCircleTransform(contxt))
                .into(iv);
    }

    public static void loadImageTestPicasso(Context contxt, String loadingUrl, int defaultPng, ImageView iv) {
        if (defaultPng > 0) {
            iv.setImageResource(defaultPng);
        }
        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load(loadingUrl)
                .placeholder(defaultPng)
                .into(iv);
    }


    //加载圆角图片
    public static void loadingCircularBead(Context contxt, String loadingUrl, int defaultPng, ImageView iv) {
        loadingCircularBead(contxt, loadingUrl, defaultPng, 10, iv);
    }

    public static void loadingCircularBead(Context contxt, String loadingUrl, int defaultPng, int radius, ImageView iv) {
        if (!isRun(contxt)) {
            return;
        }
        Glide.with(contxt).load(loadingUrl)
                .placeholder(defaultPng)
                .transform(new GlideCircularBeadTransform(contxt, radius))
                .into(iv);
    }

    //加载会话图片 type:三角形所在的边（0:左边；1:右边）
    public static void loadImageChat(Context contxt, String loadingUrl, int defaultPng, final ImageView iv, final int type) {
        iv.setImageResource(defaultPng);
        Glide.with(contxt).asBitmap()
                .load(loadingUrl)
                .placeholder(defaultPng)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ChatTransform chatTransform = new ChatTransform(type);
                        resource = chatTransform.drawBitmap(resource, 200);
                        iv.setImageBitmap(resource);
                    }


                });
    }

    //加载gif
    public static void loadingGif(Context context, String loadingUrl, int defaultPng, ImageView iv) {
        if (TextUtils.isEmpty(loadingUrl)) {
            iv.setImageResource(defaultPng);
            return;
        }
        if (!isRun(context)) {
            return;
        }
        Glide.with(context).asGif()
                .load(loadingUrl)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//将图片缓存到磁盘中
                .format(DecodeFormat.PREFER_RGB_565)//解码为565格式，减小内存
                .skipMemoryCache(true)//不将图片缓存到内存中
                .placeholder(defaultPng)
                .listener(new GifReq(context, iv, loadingUrl))
                .into(iv);
    }

    //加载webp 动图
    public static void loadingGifWebP(Context context, String loadingUrl, int defaultPng, ImageView iv) {
        //webp动图
        Transformation<Bitmap> transformation = new CenterInside();
        Glide.with(context)
                .load(loadingUrl)//不是本地资源就改为url即可
                .optionalTransform(transformation)
                .optionalTransform(WebpDrawable.class, new WebpDrawableTransformation(transformation))
                .addListener(new WebPGifReq(context, iv, loadingUrl, defaultPng))
//                .skipMemoryCache(true)
                .into(iv);
    }


    //加载webp 动图
    public static void loadingGifWebP2(Context context, String loadingUrl, int defaultPng, ImageView iv) {
        //webp动图
        Glide.with(context)
                .load(loadingUrl)//不是本地资源就改为url即可
                .addListener(new WebPGifReq(context, iv, loadingUrl, defaultPng))
                .into(iv);
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
        Glide.with(context).asBitmap()
                .load(loadingUrl)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//将图片缓存到磁盘中
                .format(DecodeFormat.PREFER_RGB_565)//解码为565格式，减小内存
                .skipMemoryCache(true)//不将图片缓存到内存中
                .placeholder(defaultPng)
                .listener(new BitReq(context, iv, loadingUrl))
                .into(iv);
    }

    public static void loadingAuto(Context context, File file, int defaultPng, ImageView iv) {
        if (!isRun(context)) {
            return;
        }

        Glide.with(context).asBitmap()
                .load(file)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//将图片缓存到磁盘中
                .format(DecodeFormat.PREFER_RGB_565)//解码为565格式，减小内存
                .skipMemoryCache(true)//不将图片缓存到内存中
                .placeholder(defaultPng)
                .listener(new BitReq(context, iv, file))
                .into(iv);
    }

    public static void loadingGifAuto(Context context, String loadingUrl, int defaultPng, ImageView iv) {
        if (TextUtils.isEmpty(loadingUrl)) {
            iv.setImageResource(defaultPng);
            return;
        }
        if (!isRun(context)) {
            return;
        }
        Glide.with(context).asGif()
                .load(loadingUrl)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//将图片缓存到磁盘中
                .format(DecodeFormat.PREFER_RGB_565)//解码为565格式，减小内存
                .skipMemoryCache(true)//不将图片缓存到内存中
                .placeholder(defaultPng)
                .listener(new GifReq(context, iv, loadingUrl))
                .into(iv);
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
