package com.library.baseui.utile.img.req;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;

//加载gif
public class BitReq implements RequestListener<Bitmap> {
    private Context context;
    //
    private String imgUrl;
    private File file;
    private ImageView imageView;

    public BitReq(Context context, ImageView imageView, String imgUrl) {
        this.context = context;
        this.imageView = imageView;
        this.imgUrl = imgUrl;
    }
    public BitReq(Context context, ImageView imageView, File file) {
        this.context = context;
        this.imageView = imageView;
        this.file = file;
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
        int width = resource.getWidth();
        int height = resource.getHeight();
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
        return false;
    }


}
