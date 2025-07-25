package test.app.manager.rn;

import static android.os.Environment.getExternalStorageDirectory;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class RnFileUtils {
    private static String tag = "RnFileUtils";

    public static void removeDirectory(File file) throws IOException {
        RNLog.d(tag, "删除文件夹 file=" + file.toString());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                String name = f.getName();
                if (name.equals(".") || name.equals("..")) {
                    continue;
                }
                removeDirectory(f);
            }
        }
        if (file.exists() && !file.delete()) {
            RNLog.d(tag, "文件存在 删除 file=" + file.toString());
        }
    }

    public static File getRootFile(Context context) {
        //File file = new File(Environment.getExternalStorageDirectory(), "_update_rn");
        File file = new File(context.getFilesDir(), "_update_rn");
        if (!file.exists()) {
            file.mkdir();
        }
        return file;

    }

    public static void tets(Context context) {
        File filesDir = context.getFilesDir();//内部缓存 /data/user/0/com.guomin.app.seletcimage/files
        File cacheDir = context.getCacheDir();//内部缓存 /data/user/0/com.guomin.app.seletcimage/cache
        File externalCacheDir = context.getExternalCacheDir();//外部缓存 /storage/emulated/0/Android/data/com.guomin.app.seletcimage/cache
        File directory = getExternalStorageDirectory();//外部储存 /storage/emulated/0
        Log.d("====>", "filesDir=" + filesDir.getPath());
        Log.d("====>", "cacheDir=" + cacheDir.getPath());
        Log.d("====>", "externalCacheDir=" + externalCacheDir.getPath());
        Log.d("====>", "directory=" + directory.getPath());
    }
}
