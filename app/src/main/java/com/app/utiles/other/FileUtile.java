package com.app.utiles.other;

import android.graphics.Bitmap;
import android.os.Environment;

import com.app.ui.activity.base.BaseApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;



/**
 * Created by Administrator on 2016/3/10.
 */
public class FileUtile {
    /***
     * SD卡根目录
     */
    public final static String CFG_PATH_SDCARD_ROOT = Environment
            .getExternalStorageDirectory().getAbsolutePath();
    //-----------------------------------------------------//
    /***
     * teamtask 的 SD卡根目录
     */
    public final static String CFG_PATH_SDCARD_DIR = CFG_PATH_SDCARD_ROOT
            + File.separator + "amity";
    /***
     * teamtask的image目录
     */
    public final static String CFG_PATH_SDCARD_IMAGE = CFG_PATH_SDCARD_DIR
            + File.separator + "image";
    //测试文件路径
    private final static String CFG_PATH_SDCARD_TEXT = CFG_PATH_SDCARD_DIR + File.separator + "test";

    /**
     * 判断sd卡是否有用
     */
    public static boolean IsCanUseSdCard() {
        try {
            return Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 私有 获取图片缓存目录
     *
     * @return
     */
    public static String getImageCachePathPrivate() {
        String cachePath;
        if (IsCanUseSdCard()) {
            File file = new File(CFG_PATH_SDCARD_IMAGE);
            if (!file.exists()) {
                file.mkdirs();
            }
            cachePath = file.getPath();
        } else {
            cachePath = BaseApplication.context.getCacheDir().getPath()
                    + File.separator + "image";
        }
        return cachePath;
    }

    /**
     * 写入图片
     */
    public static String saveBitmap(Bitmap mBitmap, String bitName) {
        String path = getImageCachePathPrivate();
        path += File.separator + bitName + ".png";
        File f = new File(path);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            path = "";
        }
        return path;
    }

    /**
     * 文件写入sd卡
     */
    public static void writeTxet(String text, String name) {
        FileOutputStream fout = null;
        File file = new File(CFG_PATH_SDCARD_TEXT);
        if (!file.exists()) {
            file.mkdirs();
        }
        name = CFG_PATH_SDCARD_TEXT + File.separator + name;
        try {
            fout = new FileOutputStream(name);
            byte[] bytes = text.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fout != null) {
                fout = null;
            }
        }

    }
}
