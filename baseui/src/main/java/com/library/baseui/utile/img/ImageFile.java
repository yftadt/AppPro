package com.library.baseui.utile.img;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;


import androidx.core.content.FileProvider;
import androidx.multidex.BuildConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Random;

import sj.mblog.Logx;

public class ImageFile {
    private static String fileDir = "hn_earn";

    //图片文件地址转 Uri
    //私有目录下的也可以，通过intent分享Uri出去时要加：
    //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);)
    public static Uri getFileUri(Context context, File file) {
        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, file);
        return uri;
    }

    //uri 持久化
    public static Uri getFileUri(Context context, Uri uri) {
        int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
        context.getContentResolver().takePersistableUriPermission(uri, flag);
        return uri;
    }

    //保存至相册、SD卡
    public static String saveBitmapFile(Context context, Bitmap bitmap) {
        String str = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            str = saveBitmapToMediaStore(context, bitmap);
        } else {
            //保存在sd卡 并且更新到相册
            File appDir = new File(Environment.getExternalStorageDirectory(), fileDir);
            str = saveBitmapToFile(context, bitmap, appDir);
        }
        return str;
    }

    //保存至私有目录
    public static String saveBitmapFileToPri(Context context, Bitmap bitmap) {
        File appDir = new File(context.getExternalCacheDir(), fileDir);
        String str = saveBitmapToFile(context, bitmap, appDir);
        return str;
    }


    /**
     * 使用  MediaStore 保存图片到相册 不用权限
     *
     * @param context
     * @param bitmap
     * @return uri
     */
    private static String saveBitmapToMediaStore(Context context, Bitmap bitmap) {
        Random random = new Random();
        int randomNumber = random.nextInt(90000);
        String fileName = System.currentTimeMillis() + "_" + randomNumber;//+ ".png";
        String str = "";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg"); //根据文件类型修改 MIME_TYPE
        String path = Environment.DIRECTORY_PICTURES + File.separator + fileDir;
        values.put(MediaStore.Images.Media.RELATIVE_PATH, path); //设置存储路径和文件夹名
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        if (uri != null) {
            try (OutputStream fos = contentResolver.openOutputStream(uri)) {
                boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                //os.write(data); // 写入数据 byte[] data
                fos.flush();
                fos.close();
                str = uri.toString();
                MediaScannerConnection.scanFile(context, new String[]{uri.getPath()}, null, null);
                Logx.e("保存图片成功 isSuccess:" + isSuccess, "--->" + uri);
            } catch (IOException e) {
                e.printStackTrace();
                Logx.e("保存图片失败", e.getLocalizedMessage());
            }
        } else {
            Logx.e("保存图片失败", "Failed to create the MediaStore record");
        }
        return str;
    }

    /**
     * @param context    上下文
     * @param bitmap     图片
     * @param fileFolder 文件夹
     * @return
     */
    private static String saveBitmapToFile(Context context, Bitmap bitmap, File fileFolder) {
        if (bitmap == null) {
            return "";
        }
        Random random = new Random();
        int randomNumber = random.nextInt(90000);
        String fileName = System.currentTimeMillis() + "_" + randomNumber + ".png";
        if (!fileFolder.exists()) {
            fileFolder.mkdir();
        }
        File file = new File(fileFolder, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(file.getPath()))));

        return file.getAbsolutePath();
    }

}
