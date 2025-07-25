package test.app.manager.task;

import android.content.Context;



import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import test.app.manager.clear.ClearBean;


public class TaskManager {
    private static TaskManager manager;
    private Executor executor;
    public static final int TASK_TYPE_RN_UPDATE = 1;//rn 更新
    public static final int TASK_TYPE_CLEAR_CACHE = 2;//清理缓存

    private TaskManager() {
        executor = Executors.newSingleThreadExecutor();
    }

    //Rn更新
    public void onDownloadRn(BaseTask baseTask, BeanTask bean) {
        bean.taskType = TASK_TYPE_RN_UPDATE;
        baseTask.executeOnExecutor(executor, bean);
    }

    //=================缓存目录 读取 或者 删除=======================================

    //==================读取外部私有目录
    public void onExClearRootRed(BaseTask baseTask, Context context) {
        File file = context.getExternalCacheDir().getParentFile();
        onClearRed(baseTask, file);
    }
    public void onExClearRootDel(BaseTask baseTask, Context context) {
        File file = context.getExternalCacheDir().getParentFile();
        onClearDel(baseTask, file);
    }

    //外部缓存
    public void onExClearCacheRed(BaseTask baseTask, Context context) {
        //
        File file = context.getExternalCacheDir();
        onClearRed(baseTask, file);
    }

    public void onExClearCacheDel(BaseTask baseTask, Context context) {
        //
        File file = context.getExternalCacheDir();
        onClearDel(baseTask, file);
    }


    //===========================读取内部私有目录
    public void onInClearRootRed(BaseTask baseTask, Context context) {
        File file = context.getFilesDir().getParentFile();
        onClearRed(baseTask, file);
    }
    public void onInClearRootDel(BaseTask baseTask, Context context) {
        File file = context.getFilesDir().getParentFile();
        onClearDel(baseTask, file);
    }


    //读取内部 指定私有目录
    public void onInClearRed(BaseTask baseTask, int type, Context context) {
        File file1 = context.getFilesDir();
        File file2 = context.getCacheDir();
        switch (type) {
            case 1:
                onClearRed(baseTask, file1);
                break;
            case 2:
                onClearRed(baseTask, file2);
                break;
            case 3:
                onClearRed(baseTask, file1, file2);
                break;
        }

    }
    public void onInClearDel(BaseTask baseTask, int type, Context context) {
        File file1 = context.getFilesDir();
        File file2 = context.getCacheDir();
        switch (type) {
            case 1:
                onClearDel(baseTask, file1);
                break;
            case 2:
                onClearDel(baseTask, file2);
                break;
            case 3:
                onClearDel(baseTask, file1, file2);
                break;
        }

    }
    //内部缓存 短剧，视频 图片缓存目录
    public void onInClearImgRed(BaseTask baseTask, Context context) {
        // /cache/image_manager_disk_cache
        File file = new File(context.getCacheDir(), "image_manager_disk_cache");
        onClearRed(baseTask, file);
    }

    public void onInClearImgDel(BaseTask baseTask, Context context) {
        // /cache/image_manager_disk_cache
        File file = new File(context.getCacheDir(), "image_manager_disk_cache");
        onClearDel(baseTask, file);
    }

//========================================================================
    public void onClearDel(BaseTask baseTask, File... file) {
        if (file == null) {
            return;
        }
        ClearBean bean = new ClearBean();
        bean.taskType = TASK_TYPE_CLEAR_CACHE;
        bean.files = file;
        bean.type = 2;
        baseTask.executeOnExecutor(executor, bean);
    }

    public void onClearRed(BaseTask baseTask, File... file) {
        if (file == null) {
            return;
        }
        ClearBean bean = new ClearBean();
        bean.taskType = TASK_TYPE_CLEAR_CACHE;
        bean.files = file;
        bean.type = 1;
        baseTask.executeOnExecutor(executor, bean);
    }

    public static void clearData() {
        if (manager != null) {
            manager.executor = null;
            manager = null;
        }
    }

    public static TaskManager getInstance() {
        if (manager == null) {
            manager = new TaskManager();
        }
        return manager;
    }

}
