package com.library.baseui.utile.img;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.webp.decoder.WebpDecoder;
import com.bumptech.glide.integration.webp.decoder.WebpDrawable;
import com.bumptech.glide.integration.webp.decoder.WebpDrawableTransformation;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.library.baseui.utile.img.req.GlideBitReq;
import com.library.baseui.utile.img.req.GlideGifReq;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.reflect.Field;


/**
 * Created by Administrator on 2016/8/19.
 */
public class ImageLoadingUtile {
    public static void loadImageTest(Context contxt, String loadingUrl, int defaultPng, ImageView iv) {

        //loadingGifAuto(contxt, loadingUrl, defaultPng, iv);
        //loadingGif(contxt, loadingUrl, defaultPng, iv);
        loadingGifWebP(contxt, loadingUrl, defaultPng, iv);
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
        Glide.with(contxt).asBitmap().load(loadingUrl).placeholder(defaultPng).into(new SimpleTarget<Bitmap>() {
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
                .listener(new GlideGifReq(context, iv, loadingUrl))
                .into(iv);
    }

    //加载webp 动图
    public static void loadingGifWebP(Context context, String loadingUrl, int defaultPng, ImageView iv) {
//        if(mWebpDrawable!=null&&!mWebpDrawable.isRunning()){
//            mWebpDrawable.startFromFirstFrame();
//            mWebpDrawable.stop();
//        }

        //webp动图
        Transformation<Bitmap> transformation = new CenterInside();
        Glide.with(context)
                .load(loadingUrl)//不是本地资源就改为url即可
                .optionalTransform(transformation)
                .optionalTransform(WebpDrawable.class, new WebpDrawableTransformation(transformation))
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        iv.setImageResource(defaultPng);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        if (resource instanceof WebpDrawable) {
                            WebpDrawable mWebpDrawable = (WebpDrawable) resource;
                            try {
                                //已知三方库的bug，webp的动图每一帧的时间间隔于实际的有所偏差，需要反射三方库去修改
                                //https://github.com/zjupure/GlideWebpDecoder/issues/33
                                Field gifStateField = mWebpDrawable.getClass().getDeclaredField("state");
                                gifStateField.setAccessible(true);//开放权限
                                Class gifStateClass = Class.forName("com.bumptech.glide.integration.webp.decoder.WebpDrawable$WebpState");
                                Field gifFrameLoaderField = gifStateClass.getDeclaredField("frameLoader");
                                gifFrameLoaderField.setAccessible(true);

                                Class gifFrameLoaderClass = Class.forName("com.bumptech.glide.integration.webp.decoder.WebpFrameLoader");
                                Field gifDecoderField = gifFrameLoaderClass.getDeclaredField("webpDecoder");
                                gifDecoderField.setAccessible(true);

                                WebpDecoder webpDecoder = (WebpDecoder) gifDecoderField.get(gifFrameLoaderField.get(gifStateField.get(resource)));
                                Field durations = webpDecoder.getClass().getDeclaredField("mFrameDurations");
                                durations.setAccessible(true);
                                int[] args = (int[]) durations.get(webpDecoder);
                                if (args.length > 0) {
                                    for (int i = 0; i < args.length; i++) {
                                        if (args[i] > 30) {
                                            //加载glide会比ios慢 这边把gif的间隔减少15s
                                            args[i] = args[i] - 15;
                                        }
                                    }
                                }
                                durations.set(webpDecoder, args);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            //需要设置为循环1次才会有onAnimationEnd回调
                            mWebpDrawable.setLoopCount(-1);
                            mWebpDrawable.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                @Override
                                public void onAnimationStart(Drawable drawable) {
                                    super.onAnimationStart(drawable);
                                }

                                @Override
                                public void onAnimationEnd(Drawable drawable) {
                                    super.onAnimationEnd(drawable);
                                    //第二次播放webp动图的时候 会显示改webp动图最后一帧的图片 然后才能正常显示
                                    if (mWebpDrawable != null && !mWebpDrawable.isRunning()) {
                                        mWebpDrawable.startFromFirstFrame();
                                        mWebpDrawable.stop();
                                    }
                                    mWebpDrawable.unregisterAnimationCallback(this);
                                }
                            });
                        }

                        return false;
                    }
                })
//                .skipMemoryCache(true)
                .into(iv);
    }

    public static void setCancelGifWebPOnResume(WebpDrawable mWebpDrawable) {
        //解决使用webp动图播放一次的时候 页面重新显示之后 webp动图还会播放一次的问题
        //在onresume调用即可
        if (mWebpDrawable == null) {
            return;
        }
        try {
            Field isRunning = mWebpDrawable.getClass().getDeclaredField("isRunning");
            isRunning.setAccessible(true);
            isRunning.setBoolean(mWebpDrawable, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                .listener(new GlideBitReq(context, iv, loadingUrl))
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
                .listener(new GlideBitReq(context, iv, file))
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
                .listener(new GlideGifReq(context, iv, loadingUrl))
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
