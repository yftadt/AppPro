package com.library.baseui.utile.img;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.library.baseui.utile.app.APKInfo;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * 圆角
 * Created by Admin on 2016/8/16.
 */
public class GlideCircularBeadTransform extends BitmapTransformation {
    private float radius = 0f;

    public GlideCircularBeadTransform(Context context) {
        this(context, 4);
    }

    public GlideCircularBeadTransform(Context context, int dp) {
        this.radius = APKInfo.getInstance().getDIPTOPX(dp);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        //取正方形图片 在source上以X，Y坐标(左上角)为起点，而宽与高则是size与size开始截图
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
        //返回一个正好匹配给定宽、高和配置的只包含透明像素的Bitmap。
        // 如果BitmapPool中找不到这样的Bitmap，就返回null。
        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }
        //
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, squared.getWidth(), squared.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);
        return result;
    }


    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}