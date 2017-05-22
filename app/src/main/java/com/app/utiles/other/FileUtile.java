package com.app.utiles.other;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;

import com.app.net.common.thread.NetSourceThreadPool;
import com.app.ui.activity.base.BaseApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by Administrator on 2016/3/10.
 */
public class FileUtile {
    /***
     * SD卡根目录
     */
    public final static String SDCARD_ROOT = Environment
            .getExternalStorageDirectory().getAbsolutePath();
    //-----------------------------------------------------//
    /***
     * teamtask 的 SD卡根目录 project
     */
    public final static String PROJECT_ROOT = SDCARD_ROOT
            + File.separator + "gj";
    /***
     * teamtask的image目录
     */
    public final static String PROJECT_IMAGE = PROJECT_ROOT
            + File.separator + "image";
    //数据库
    private final static String PROJECT_DB = PROJECT_ROOT + File.separator + "db";
    //文本
    private final static String PROJECT_TXT = PROJECT_ROOT + File.separator + "txt";

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
            File file = new File(PROJECT_IMAGE);
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
     * 数据库写入sd卡，获取写入地址
     */
    public static String getLoactionBd(String fileName) {
        File file = new File(PROJECT_DB);
        if (!file.exists()) {
            file.mkdirs();
        }
        String path = PROJECT_DB + File.separator + fileName;
        return path;
    }
//====================================================

    /**
     * 写入图片
     * isVisible true 相册可见
     */
    public static String saveBitmap(Bitmap mBitmap, String bitName, boolean isVisible) {
        String path = getImageCachePathPrivate();
        path += File.separator + bitName;
        if (isVisible) {
            path += ".png";
        }
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
//====================================================

    /**
     * 文件写入sd卡
     */
    public static void writeTxet(String text, String name) {
        FileOutputStream fout = null;
        File file = new File(PROJECT_TXT);
        if (!file.exists()) {
            file.mkdirs();
        }
        name = PROJECT_TXT + File.separator + name;
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
//====================================================

    /***
     * @param fileName 写入sd卡的数据库名称
     * @param source 数据库文件源文件
     * @param isChange 数据库文件是否有修改
     * @return 写入sd卡的数据库文件
     */
    public static File getFile(Context context, String fileName, String source, boolean isChange) {
        String path = getLoactionBd(fileName);
        File file = new File(path);
        boolean exists = file.exists();
        if (isChange && exists) {
            file.delete();
        }
        if (exists) {
            return file;
        }
        try {
            // 读取文件 写入到sd卡
            AssetManager manager = context.getAssets();
            InputStream inputStream = manager.open(source);
            writeDB(inputStream, fileName);
        } catch (IOException e) {
            DLog.e("写入失败", "-----------");
            e.printStackTrace();
            return null;
        }
        path = getLoactionBd(fileName);
        file = new File(path);
        exists = file.exists();
        if (exists) {
            return file;
        } else {
            return null;
        }
    }

    /**
     * 写入数据库文件
     */
    private static void writeDB(InputStream inputStream, String fileName) {
        String path = getLoactionBd(fileName);
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(path);
            byte[] buffer = new byte[1024];
            int szie = 0;
            while ((szie = inputStream.read(buffer)) > 0) {
                fout.write(buffer);
            }
            fout.flush();
            inputStream.close();
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            inputStream = null;
            fout = null;
        }
    }

    //=============================================
    public static void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteFile(f);
            }
            file.delete();
        }
    }

    //删除图片缓存目录
    //删除图片目录
    public static void deleteFile() {
        NetSourceThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                String fileName =  getImageCachePathPrivate();
                FileUtile.deleteFile(new File(fileName));
            }
        });
    }
}
