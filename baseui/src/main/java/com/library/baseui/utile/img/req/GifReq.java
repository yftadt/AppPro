package com.library.baseui.utile.img.req;


import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.library.baseui.utile.HandlerUtil;
import com.library.baseui.utile.img.ImageLoadingUtile;

import java.io.File;

import sj.mblog.Logx;

//加载gif
public class GifReq implements RequestListener<GifDrawable> {
    private Context context;
    private String imgUrl;
    private ImageView imageView;

    public GifReq(Context context, ImageView imageView, String imgUrl) {
        this.context = context;
        this.imageView = imageView;
        this.imgUrl = imgUrl;
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
        Logx.d("gif加载失败 :" + e.getMessage());
        // File cacheDir = Glide.getPhotoCacheDir(context); // 获取缓存路径
        new Thread(new Runnable() {
            @Override
            public void run() {
                checkFile();
            }
        }).start();

        return false;
    }

    @Override
    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
        int width = resource.getIntrinsicHeight();
        int height = resource.getIntrinsicHeight();
        // 获取图片原始宽高
        //
        int newWidth = width;
        int newHeight = height;
        int maxWidth = imageView.getMaxWidth();
        if (width > maxWidth) {
            newWidth = maxWidth;
            double ratio = ((double) width) / ((double) maxWidth);
            newHeight = (int) (height / ratio);
        }
        // 设置图片高度
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = newWidth;
        params.height = newHeight;
        imageView.setLayoutParams(params);
        Logx.d("gif加载成功");
        return false;
    }

    private void checkFile() {
        try {
            FutureTarget<File> futureTarget = Glide.with(context)
                    .asFile().load(imgUrl)
                    .submit();
            // 正常获取缓存文件
            File cachedFile = futureTarget.get();
            boolean exists = cachedFile.exists();
            if (exists) {

                //文件存在
                HandlerUtil.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        Logx.d("gif加载失败 但是缓存文件存在");
                        ImageLoadingUtile.loadingAuto(context, cachedFile, 0, imageView);
                    }
                });
            }
        } catch (Exception ex) {
            Logx.d("gif加载失败 gif取缓存文件失败 :" + ex.getMessage());
        }
    }
}
