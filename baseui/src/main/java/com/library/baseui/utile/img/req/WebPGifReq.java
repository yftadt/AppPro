package com.library.baseui.utile.img.req;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.webp.decoder.WebpDecoder;
import com.bumptech.glide.integration.webp.decoder.WebpDrawable;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.library.baseui.utile.HandlerUtil;
import com.library.baseui.utile.img.ImageLoadingUtile;

import java.io.File;
import java.lang.reflect.Field;

import sj.mblog.Logx;

//加载gif
public class WebPGifReq implements RequestListener<Drawable> {
    private Context context;
    private String imgUrl;
    private ImageView imageView;
    private int defImgRes;
    public WebPGifReq(Context context, ImageView imageView, String imgUrl,int defImgRes) {
        this.context = context;
        this.imageView = imageView;
        this.imgUrl = imgUrl;
        this.defImgRes = defImgRes;
    }
    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
        imageView.setImageResource(defImgRes);
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
                Logx.d("加载webp 动图失败：" + e.getMessage());
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
}
