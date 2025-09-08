package com.library.baseui.utile.img;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;


import androidx.core.content.FileProvider;
import androidx.multidex.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import sj.mblog.Logx;

public class ImageFile {
    private static String fileDir = "hn_earn";

    //图片地址装 Uri
    public static Uri getFileUri(Context context, File file) {
        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID,
                file);
        return uri;
    }

    //保存至相册、SD卡
    public static String saveBitmapFile(Context context, Bitmap bitmap) {
        String str = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            str = saveBitmapToMediaStore(context, bitmap);
        } else {
            //保存在sd卡
            File appDir = new File(Environment.getExternalStorageDirectory(),
                    fileDir);
            str = saveBitmapToFile(context, bitmap, appDir, true);
        }
        return str;
    }

    //保存至私有目录
    public static String saveBitmapFileToPri(Context context, Bitmap bitmap) {
        File appDir = new File(context.getExternalCacheDir(), fileDir);
        String str = saveBitmapToFile(context, bitmap, appDir, false);
        return str;
    }

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
     * @param context       上下文
     * @param bitmap        图片
     * @param fileFolder    文件夹
     * @param isUpdateAlbum true 更新相册
     * @return
     */
    private static String saveBitmapToFile(Context context, Bitmap bitmap, File fileFolder, boolean isUpdateAlbum) {
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
        boolean isSuccess;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        if (isSuccess) {
            // 最后通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(new File(file.getPath()))));
        }
        return file.getAbsolutePath();
    }

}
