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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import sj.mblog.Logx;

/**
 * Created by Administrator on 2016/8/19.
 */
public class ImageLoadingUtile {

    public static void loadImageTest(Context contxt, String loadingUrl, int defaultPng, ImageView iv) {


        loadImageTestGlide(contxt, loadingUrl, defaultPng, iv);
        //loadImageTestPicasso(contxt, loadingUrl, defaultPng, iv);
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

    public static void loadImageTestGlide(Context contxt, String loadingUrl, int defaultPng, ImageView iv) {
        RequestListener<Drawable> requestListenerDrawable = new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@androidx.annotation.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Logx.d("加载失败：" + "nmodel=" + model.toString() + " isFirstResource=" + isFirstResource + "\n" + e);
                GlideException glideException = (GlideException) e;
                glideException.logRootCauses("加载失败:");
                return false; // 返回false以继续后续的失败处理（例如重试）或true以取消后续处理（例如不重试）
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                Logx.d("加载成功：Image loaded successfully");
                return false; // 同上，返回false以继续后续处理或true以取消后续处理
            }
        };
        RequestListener<Bitmap> requestListenerBit = new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@androidx.annotation.Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                Logx.d("加载失败：" + "nmodel=" + model.toString() + " isFirstResource=" + isFirstResource + "\n" + e);
                GlideException glideException = (GlideException) e;
                glideException.logRootCauses("加载失败:");
                return false; // 返回false以继续后续的失败处理（例如重试）或true以取消后续处理（例如不重试）
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                Logx.d("加载成功：Image loaded successfully");
                return false; // 同上，返回false以继续后续处理或true以取消后续处理
            }
        };
        RequestListener<File> requestListenerFile = new RequestListener<File>() {
            @Override
            public boolean onLoadFailed(@androidx.annotation.Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                Logx.d("加载失败：" + "nmodel=" + model.toString() + " isFirstResource=" + isFirstResource + "\n" + e);
                GlideException glideException = (GlideException) e;
                glideException.logRootCauses("加载失败:");
                return false; // 返回false以继续后续的失败处理（例如重试）或true以取消后续处理（例如不重试）
            }

            @Override
            public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                Logx.d("加载成功：Image loaded successfully");
                return false; // 同上，返回false以继续后续处理或true以取消后续处理
            }
        };
        Glide.with(contxt).asFile().load(loadingUrl)
                //.signature()
                .addListener(requestListenerFile).placeholder(defaultPng)
                .signature(new ObjectKey(System.currentTimeMillis())) // 或者使用其他唯一标识
                //.skipMemoryCache(true)
                //.skipMemoryCache(false)
                //.diskCacheStrategy(DiskCacheStrategy.NONE)//不使用缓存
                //.into(iv);
                .into(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(File resource, Transition<? super File> transition) {
                        boolean isExists = resource.exists();
                        // 这里resource就是图片的缓存文件
                        String path = resource.getAbsolutePath();
                        Logx.d("GlideCache", "图片路径: " + path);
                        Drawable drawable = Drawable.createFromPath(path);

                        //iv.setImageDrawable(drawable);
                        //可用
                        Uri imageUri = Uri.fromFile(resource);
                        //iv.setImageURI(imageUri);
                        byte[] imageData = null;
                        try {
                            imageData = readFileToByteArray(resource);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        if (imageData == null) {
                            return;
                        }
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                        iv.setImageBitmap(bitmap);


                    }
                });
    }

    public static byte[] readFileToByteArray(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It has to be an int type.
        if (length > Integer.MAX_VALUE) {
            throw new IOException("File is too large");
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read the file " + file.getName());
        }
        is.close();

        return bytes;
    }

    //加载圆形图片
    public static void loadingCircle(Context contxt, String loadingUrl, int defaultPng, ImageView iv) {
        if (!isRun(contxt)) {
            return;
        }
        Glide.with(contxt).load(loadingUrl).placeholder(defaultPng).transform(new GlideCircleTransform(contxt)).into(iv);
    }

    //加载圆形图片 没有默认头像
    public static void loadingCircle(Context contxt, String loadingUrl, ImageView iv) {
        if (!isRun(contxt)) {
            return;
        }
        Glide.with(contxt).load(loadingUrl).transform(new GlideCircleTransform(contxt)).into(iv);
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

    //加载图片
    public static void loading(Context contxt, String loadingUrl, int defaultPng, ImageView iv) {
        if (TextUtils.isEmpty(loadingUrl)) {
            iv.setImageResource(defaultPng);
            return;
        }
        Glide.with(contxt).load(loadingUrl).placeholder(defaultPng).into(iv);
    }

    //加载本地图片
    public static void loading(Context contxt, String loadingUrl, ImageView iv) {
      /*  if (!isExistMainActivity(contxt.getClass(),contxt)){
            return;
        }*/
        Glide.with(contxt).load(loadingUrl).into(iv);
    }

    public static void loading7N(Context contxt, String loadingUrl, int defaultPng, ImageView iv) {
        loading(contxt, loadingUrl, defaultPng, iv);
    }

    //加载图片 居中显示
    public static void loadingCenterCrop(Context contxt, String loadingUrl, int defaultPng, ImageView iv) {
        loadingCenterCrop(contxt, loadingUrl, defaultPng, iv, false);
    }

    public static void loadingCenterCrop(Context contxt, String loadingUrl, int defaultPng, ImageView iv, boolean is7Nsmall) {
        if (!isRun(contxt)) {
            return;
        }
        if (TextUtils.isEmpty(loadingUrl)) {
            iv.setImageResource(defaultPng);
            return;
        }
        if (is7Nsmall) {
            loadingUrl = loadingSmall7N(loadingUrl);
        }
        Glide.with(contxt).load(loadingUrl).placeholder(defaultPng).centerCrop().into(iv);
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


    //加载7牛上的小图片
    private static String loadingSmall7N(String loadingUrl) {
        if (!TextUtils.isEmpty(loadingUrl) && loadingUrl.startsWith("http")) {
            loadingUrl += "!avatar";
        }
        return loadingUrl;
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
